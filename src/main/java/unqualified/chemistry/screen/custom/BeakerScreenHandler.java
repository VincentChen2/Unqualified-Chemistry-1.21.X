package unqualified.chemistry.screen.custom;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;
import unqualified.chemistry.block.entity.custom.BeakerBlockEntity;
import unqualified.chemistry.screen.ModScreenHandlers;

public class BeakerScreenHandler extends ScreenHandler {
    private final BeakerBlockEntity blockEntity;

    public BeakerScreenHandler(int syncId, PlayerInventory playerInventory, BlockPos pos) {
        this(syncId, playerInventory, playerInventory.player.getEntityWorld().getBlockEntity(pos));
    }

    public BeakerScreenHandler(int syncId, PlayerInventory playerInventory, BlockEntity blockEntity) {
        super(ModScreenHandlers.BEAKER_SCREEN_HANDLER, syncId);

        SimpleInventory inventory;
        if (blockEntity instanceof BeakerBlockEntity beaker) {
            this.blockEntity = beaker;
            inventory = new SimpleInventory(2);
        } else {
            this.blockEntity = null;
            inventory = new SimpleInventory(2);
        }

        this.addSlot(new Slot(inventory, 0, 44, 34));
        this.addSlot(new Slot(inventory, 1, 116, 34));
        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
    }

    public BeakerBlockEntity getBlockEntity() {
        return blockEntity;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int invSlot) {
        // Since the beaker has no inventory slots, quick move doesn't need to do anything
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        // Always allow access to the beaker screen
        return true;
    }

    private void addPlayerInventory(PlayerInventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }
}