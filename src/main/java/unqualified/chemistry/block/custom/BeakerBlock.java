package unqualified.chemistry.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;

import org.jetbrains.annotations.Nullable;

import unqualified.chemistry.block.entity.custom.BeakerBlockEntity;

public class BeakerBlock extends BlockWithEntity implements BlockEntityProvider {
    private static final VoxelShape SHAPE = Block.createCuboidShape(5.5, 0, 5.5, 10.5, 9, 10.5);

    public static final MapCodec<BeakerBlock> CODEC = createCodec(BeakerBlock::new);

    public BeakerBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BeakerBlockEntity(pos, state);
    }

    @Override
    protected ActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos,
                                         PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.getBlockEntity(pos) instanceof BeakerBlockEntity beakerBlockEntity) {
            // Handle empty hand - open screen
            if (stack.isEmpty() && !player.isSneaking()) {
                if (!world.isClient()) {
                    player.openHandledScreen(beakerBlockEntity);
                }
                return ActionResult.SUCCESS;
            }

            // Handle fluid container interactions
            if (isWaterBottle(stack) || stack.isOf(Items.POTION)) {
                if (!world.isClient()) {
                    boolean success = beakerBlockEntity.addFluid(stack);
                    if (success) {
                        world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0f, 1.0f);

                        // Replace with empty bottle
                        if (!player.isCreative()) {
                            stack.decrement(1);
                            if (stack.isEmpty()) {
                                player.setStackInHand(hand, new ItemStack(Items.GLASS_BOTTLE));
                            } else {
                                player.getInventory().insertStack(new ItemStack(Items.GLASS_BOTTLE));
                            }
                        }
                    }
                    beakerBlockEntity.markDirty();
                    world.updateListeners(pos, state, state, 0);
                }
                return ActionResult.SUCCESS;
            }

            // Handle glass bottle to remove fluid
            if (stack.isOf(Items.GLASS_BOTTLE)) {
                if (!world.isClient()) {
                    ItemStack filledBottle = beakerBlockEntity.removeFluid();
                    if (!filledBottle.isEmpty()) {
                        world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0f, 1.0f);

                        if (!player.isCreative()) {
                            stack.decrement(1);
                            if (stack.isEmpty()) {
                                player.setStackInHand(hand, filledBottle);
                            } else {
                                player.getInventory().insertStack(filledBottle);
                            }
                        }
                    }
                    beakerBlockEntity.markDirty();
                    world.updateListeners(pos, state, state, 0);
                }
                return ActionResult.SUCCESS;
            }
        }

        return ActionResult.PASS;
    }

    // Helper method to detect water bottles
    private boolean isWaterBottle(ItemStack stack) {
        if (!stack.isOf(Items.POTION)) {
            return false;
        }

        return stack.getComponents().contains(DataComponentTypes.POTION_CONTENTS) &&
                PotionContentsComponent.DEFAULT.equals(stack.get(DataComponentTypes.POTION_CONTENTS));
    }
}