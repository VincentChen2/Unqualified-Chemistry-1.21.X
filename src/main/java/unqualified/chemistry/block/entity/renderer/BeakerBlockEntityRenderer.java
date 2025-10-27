package unqualified.chemistry.block.entity.renderer;

import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.command.ModelCommandRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import unqualified.chemistry.block.entity.custom.BeakerBlockEntity;

public class BeakerBlockEntityRenderer implements BlockEntityRenderer<BeakerBlockEntity, BeakerBlockEntityRenderState> {

    public BeakerBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
    }

    @Override
    public BeakerBlockEntityRenderState createRenderState() {
        return new BeakerBlockEntityRenderState();
    }

    @Override
    public void updateRenderState(BeakerBlockEntity blockEntity, BeakerBlockEntityRenderState state, float tickProgress, Vec3d cameraPos, @Nullable ModelCommandRenderer.CrumblingOverlayCommand crumblingOverlay) {
        BlockEntityRenderer.super.updateRenderState(blockEntity, state, tickProgress, cameraPos, crumblingOverlay);

        state.lightPosition = blockEntity.getPos();
        state.blockEntityWorld = blockEntity.getWorld();
        state.fluidColor = blockEntity.getFluidColor();
        state.fluidHeight = blockEntity.getFluidHeight();
    }

    @Override
    public void render(BeakerBlockEntityRenderState state, MatrixStack matrices, OrderedRenderCommandQueue queue, CameraRenderState cameraState) {
        if (state.fluidHeight <= 0) return;

        matrices.push();

        // Position fluid inside beaker
        // These values position the fluid at the bottom center of the beaker
        matrices.translate(0.5f, 0.0625f, 0.5f);

        // Scale the fluid to match the beaker's interior and current volume
        matrices.scale(0.8f, state.fluidHeight, 0.8f);

        // Move back to corner for proper rendering
        matrices.translate(-0.5f, 0f, -0.5f);

        // Submit fluid rendering command
        submitFluidCube(matrices, queue, state.fluidColor, getLightLevel(state.blockEntityWorld, state.lightPosition));

        matrices.pop();
    }

    private void submitFluidCube(MatrixStack matrices, OrderedRenderCommandQueue queue, int fluidColor, int light) {
        // Extract RGB components from fluid color
        float red = ((fluidColor >> 16) & 0xFF) / 255.0f;
        float green = ((fluidColor >> 8) & 0xFF) / 255.0f;
        float blue = (fluidColor & 0xFF) / 255.0f;
        float alpha = 0.8f; // Semi-transparent

        //TODO:Implement Fluid Cube
    }

    private int getLightLevel(World world, BlockPos pos) {
        if (world == null) return LightmapTextureManager.MAX_LIGHT_COORDINATE;

        int blockLight = world.getLightLevel(LightType.BLOCK, pos);
        int skyLight = world.getLightLevel(LightType.SKY, pos);
        return LightmapTextureManager.pack(blockLight, skyLight);
    }
}