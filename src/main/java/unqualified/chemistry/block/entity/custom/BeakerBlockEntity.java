package unqualified.chemistry.block.entity.custom;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import org.jetbrains.annotations.Nullable;

import unqualified.chemistry.block.entity.ModBlockEntities;
import unqualified.chemistry.screen.custom.BeakerScreenHandler;

public class BeakerBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory<BlockPos> {
    private static final int MAX_CAPACITY = 1000; // 1000ml
    private int currentVolume = 0;
    private FluidType fluidType = FluidType.EMPTY;
    private int fluidColor = 0xFFFFFF;

    public BeakerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BEAKER_BE, pos, state);
    }

    public enum FluidType {
        EMPTY, WATER, POTION
    }

    public int getCurrentVolume() {
        return currentVolume;
    }

    public int getMaxCapacity() {
        return MAX_CAPACITY;
    }

    public FluidType getFluidType() {
        return fluidType;
    }

    public int getFluidColor() {
        return fluidColor;
    }

    public float getFluidHeight() {
        return (float) currentVolume / MAX_CAPACITY * 0.1875f; // Scale to 0-3/16 block height
    }

    public boolean addFluid(ItemStack container) {
        if (currentVolume >= MAX_CAPACITY) return false;

        int addAmount = 250; // Standard bottle amount

        if (isWaterBottle(container)) {
            if (fluidType != FluidType.EMPTY && fluidType != FluidType.WATER) return false;

            fluidType = FluidType.WATER;
            fluidColor = 0x3F76E4; // Water blue
        } else if (container.isOf(Items.POTION)) {
            if (fluidType != FluidType.EMPTY && fluidType != FluidType.POTION) return false;

            fluidType = FluidType.POTION;
            PotionContentsComponent potionContents = container.get(DataComponentTypes.POTION_CONTENTS);
            if (potionContents != null) {
                fluidColor = potionContents.getColor();
            } else {
                fluidColor = 0xFF55FF; // Default potion color
            }
        } else {
            return false;
        }

        currentVolume = Math.min(currentVolume + addAmount, MAX_CAPACITY);
        markDirty();
        return true;
    }

    public ItemStack removeFluid() {
        if (currentVolume <= 0) return ItemStack.EMPTY;

        int removeAmount = 250;
        if (currentVolume < removeAmount) return ItemStack.EMPTY;

        ItemStack result = ItemStack.EMPTY;

        if (fluidType == FluidType.WATER) {
            result = new ItemStack(Items.POTION);
            result.set(DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT);
        } else if (fluidType == FluidType.POTION) {
            result = new ItemStack(Items.POTION);
            PotionContentsComponent potionContents = new PotionContentsComponent(
                    Registries.POTION.getEntry((Potion) Potions.WATER)
            );
            result.set(DataComponentTypes.POTION_CONTENTS, potionContents);
        }

        currentVolume -= removeAmount;
        if (currentVolume <= 0) {
            fluidType = FluidType.EMPTY;
            fluidColor = 0xFFFFFF;
        }

        markDirty();
        return result;
    }

    private boolean isWaterBottle(ItemStack stack) {
        if (!stack.isOf(Items.POTION)) {
            return false;
        }

        PotionContentsComponent potionContents = stack.get(DataComponentTypes.POTION_CONTENTS);
        return potionContents != null && potionContents.equals(PotionContentsComponent.DEFAULT);
    }

    @Override
    protected void writeData(WriteView view) {
        super.writeData(view);
        view.putInt("Volume", currentVolume);
        view.putString("FluidType", fluidType.name());
        view.putInt("FluidColor", fluidColor);
    }

    @Override
    protected void readData(ReadView view) {
        super.readData(view);
        currentVolume = view.getInt("Volume", 0);
        fluidType = FluidType.valueOf(view.getString("FluidType", "EMPTY"));
        fluidColor = view.getInt("FluidColor", 0xFFFFFF);
    }

    @Override
    public BlockPos getScreenOpeningData(ServerPlayerEntity player) {
        return this.pos;
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