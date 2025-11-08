package unqualified.chemistry.block.entity.custom;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.text.Text;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import unqualified.chemistry.block.entity.ModBlockEntities;
import unqualified.chemistry.screen.custom.BeakerScreenHandler;
import unqualified.chemistry.util.FluidUtils;

import java.util.List;
import java.util.Optional;

public class BeakerBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory<BlockPos>, ImplementedInventory {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(2, ItemStack.EMPTY);

    public final SingleVariantStorage<FluidVariant> fluidStorage = new SingleVariantStorage<>() {
        @Override
        protected FluidVariant getBlankVariant() {
            return FluidVariant.blank();
        }

        //TODO: Implement a smaller FluidConstant with less capacity to better reflect actual capacity of beaker (make 1 droplet = 1ml)
        @Override
        protected long getCapacity(FluidVariant variant) {
            return (FluidConstants.BOTTLE) * 2; // 1 Bottle = 27000 Droplets = 500ml
        }

        @Override
        protected void onFinalCommit() {
            markDirty();
            assert getWorld() != null;
            getWorld().updateListeners(pos, getCachedState(), getCachedState(), 3);
        }
    };

    public BeakerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BEAKER_BE, pos, state);
    }

    public FluidVariant getFluid(){
        return fluidStorage.variant;
    }

    public int getFluidColor() {
        return FluidVariantRendering.getColor(this.fluidStorage.variant);
    }

    public float getFluidHeight() {
        return (((float) fluidStorage.getAmount() / fluidStorage.getCapacity()) * 0.1875f);
    }

    @Override
    public BlockPos getScreenOpeningData(ServerPlayerEntity player) {
        return this.pos;
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public Text getDisplayName() {
        return Text.literal("Beaker");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new BeakerScreenHandler(syncId, playerInventory, this.pos);
    }

    private boolean hasFluidStackInFirstSlot() {
        return !inventory.getFirst().isEmpty() && FluidUtils.isContainer(inventory.getFirst())
                && !FluidUtils.isContainerEmpty(inventory.getFirst());
    }

    private boolean hasFluidHandlerInSecondSlot() {
        return !inventory.get(1).isEmpty() && FluidUtils.isContainer(inventory.get(1))
                && (FluidUtils.isContainerEmpty(inventory.get(1)) || FluidUtils.doesContainerStillHaveSpace(inventory.get(1))) && !fluidStorage.isResourceBlank();
    }


    private void transferFluidToBeaker() {
        if (inventory.getFirst().isOf(Items.POTION)) {
            PotionContentsComponent potionContents = inventory.getFirst().get(DataComponentTypes.POTION_CONTENTS);

            if (potionContents != null && potionContents.potion().isPresent()) {
                RegistryEntry<Potion> potionType = potionContents.potion().get();

                if (potionType.matches(Potions.WATER)) {
                    if (fluidStorage.variant.isOf(Fluids.WATER) || fluidStorage.isResourceBlank()) {
                        try (Transaction transaction = Transaction.openOuter()) {
                            long inserted = this.fluidStorage.insert(FluidVariant.of(Fluids.WATER), FluidConstants.BOTTLE, transaction);

                            if (inserted > 0) {
                                inventory.set(0, new ItemStack(Items.GLASS_BOTTLE));
                                transaction.commit();

                            } else {

                                transaction.abort();
                            }
                        }
                    }

                }
            }
        }
    }

    private void transferFluidFromBeaker() {
        ItemStack bottleStack = inventory.get(1);

        if (bottleStack.isOf(Items.GLASS_BOTTLE) &&
                fluidStorage.variant.isOf(Fluids.WATER) &&
                fluidStorage.getAmount() >= FluidConstants.BOTTLE) {

            try (Transaction transaction = Transaction.openOuter()) {
                long extracted = this.fluidStorage.extract(FluidVariant.of(Fluids.WATER), FluidConstants.BOTTLE, transaction);

                if (extracted > 0) {
                    // Create water potion
                    ItemStack waterPotion = new ItemStack(Items.POTION);
                    PotionContentsComponent waterContents = new PotionContentsComponent(
                            Optional.of(Potions.WATER),
                            Optional.empty(),
                            List.of(),
                            Optional.empty()
                    );
                    waterPotion.set(DataComponentTypes.POTION_CONTENTS, waterContents);

                    // Set the water potion in the second slot
                    inventory.set(1, waterPotion);

                    // Handle the remaining bottles
                    if (bottleStack.getCount() > 1) {
                        ItemStack remainingBottles = bottleStack.copy();
                        remainingBottles.setCount(bottleStack.getCount() - 1);

                        // Try to put remaining bottles in the first slot if it's empty
                        if (inventory.get(0).isEmpty()) {
                            inventory.set(0, remainingBottles);
                        } else {
                            // Drop the remaining bottles on the ground
                            if (world != null && !world.isClient()) {
                                ItemScatterer.spawn(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, remainingBottles);
                            }
                        }
                    }

                    transaction.commit();
                } else {
                    transaction.abort();
                }
            }
        }
    }

    public void tick(World world, BlockPos pos, BlockState state) {
        if (hasFluidStackInFirstSlot()) {
            transferFluidToBeaker();
        }

        if(hasFluidHandlerInSecondSlot()) {
            transferFluidFromBeaker();
        }
    }

    @Override
    protected void writeData(WriteView view) {
        super.writeData(view);
        Inventories.writeData(view, inventory);
        SingleVariantStorage.writeData(this.fluidStorage, FluidVariant.CODEC, view);
    }

    @Override
    protected void readData(ReadView view) {
        super.readData(view);
        Inventories.readData(view, inventory);
        SingleVariantStorage.readData(this.fluidStorage, FluidVariant.CODEC, FluidVariant::blank, view);
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return createNbt(registryLookup);
    }
}