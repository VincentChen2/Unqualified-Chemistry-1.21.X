package unqualified.chemistry.block.entity.renderer;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.client.render.block.entity.state.BlockEntityRenderState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BeakerBlockEntityRenderState extends BlockEntityRenderState {
    public BlockPos lightPosition;
    public World blockEntityWorld;
    public int fluidColor;
    public float fluidHeight;
    public FluidVariant fluidStack;
}