package unqualified.chemistry.block.entity.renderer;

import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.command.ModelCommandRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.fluid.FluidState;
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

    private int getLightLevel(World world, BlockPos pos) {
        if (world == null) return LightmapTextureManager.MAX_LIGHT_COORDINATE;

        int blockLight = world.getLightLevel(LightType.BLOCK, pos);
        int skyLight = world.getLightLevel(LightType.SKY, pos);
        return LightmapTextureManager.pack(blockLight, skyLight);
    }

    private static void drawVertex(VertexConsumer builder, MatrixStack.Entry matricesEntry, float x, float y, float z, float u, float v, int packedLight, int color) {
        builder.vertex(matricesEntry, x, y, z)
                .color(color)
                .texture(u, v)
                .overlay(0)
                .light(packedLight)
                .normal(matricesEntry, 0, 1, 0);
    }

    private static void drawQuad(VertexConsumer builder, MatrixStack.Entry matricesEntry, float x0, float y0, float z0, float x1, float y1, float z1, float u0, float v0, float u1, float v1, int packedLight, int color) {
        //TODO: Fix Culling Issues
        drawVertex(builder, matricesEntry, x0, y0, z0, u0, v0, packedLight, color);
        drawVertex(builder, matricesEntry, x0, y1, z0, u0, v1, packedLight, color);
        drawVertex(builder, matricesEntry, x1, y1, z1, u1, v1, packedLight, color);
        drawVertex(builder, matricesEntry, x1, y0, z1, u1, v0, packedLight, color);
    }

    @Override
    public void updateRenderState(BeakerBlockEntity blockEntity, BeakerBlockEntityRenderState state, float tickProgress, Vec3d cameraPos, @Nullable ModelCommandRenderer.CrumblingOverlayCommand crumblingOverlay) {
        BlockEntityRenderer.super.updateRenderState(blockEntity, state, tickProgress, cameraPos, crumblingOverlay);

        state.lightPosition = blockEntity.getPos();
        state.blockEntityWorld = blockEntity.getWorld();
        state.fluidColor = blockEntity.getFluidColor();
        state.fluidHeight = blockEntity.getFluidHeight();
        state.fluidStack = blockEntity.getFluid();
    }

    @Override
    public void render(BeakerBlockEntityRenderState state, MatrixStack matrices, OrderedRenderCommandQueue queue, CameraRenderState cameraState) {
        if (state.fluidStack.isBlank()) return;
        if (state.fluidHeight <= 0) return;
        if (state.blockEntityWorld == null) return;

        final Sprite sprite = FluidVariantRendering.getSprite(state.fluidStack);
        if (sprite == null) return;

        FluidState fluidState = state.fluidStack.getFluid().getDefaultState();
        final int light = getLightLevel(state.blockEntityWorld, state.lightPosition);
        final int color = state.fluidColor;

        // Define beaker inner dimensions (adjust these based on your beaker model)
        final float innerX0 = 7/16f;
        final float innerX1 = 9/16f;
        final float innerZ0 = 7/16f;
        final float innerZ1 = 9/16f;
        final float innerY0 = 0.01f; // Bottom of fluid area, not 0 to avoid z-fighting
        final float innerY1 = innerY0 + state.fluidHeight;

        // Create custom renderer for the fluid
        OrderedRenderCommandQueue.Custom customRenderer = (matricesEntry, vertexConsumer) -> {
            // Top face
            drawQuad(vertexConsumer, matricesEntry,
                    innerX0, innerY1, innerZ0,
                    innerX1, innerY1, innerZ1,
                    sprite.getMinU(), sprite.getMinV(),
                    sprite.getMaxU(), sprite.getMaxV(),
                    light, color);

            // Bottom face
            drawQuad(vertexConsumer, matricesEntry,
                    innerX0, innerY0, innerZ0,
                    innerX1, innerY0, innerZ1,
                    sprite.getMinU(), sprite.getMinV(),
                    sprite.getMaxU(), sprite.getMaxV(),
                    light, color);

            // North face (negative Z)
            drawQuad(vertexConsumer, matricesEntry,
                    innerX0, innerY0, innerZ0,
                    innerX1, innerY1, innerZ0,
                    sprite.getMinU(), sprite.getMinV(),
                    sprite.getMaxU(), sprite.getMaxV(),
                    light, color);

            // South face (positive Z)
            drawQuad(vertexConsumer, matricesEntry,
                    innerX0, innerY0, innerZ1,
                    innerX1, innerY1, innerZ1,
                    sprite.getMinU(), sprite.getMinV(),
                    sprite.getMaxU(), sprite.getMaxV(),
                    light, color);

            // West face (negative X)
            drawQuad(vertexConsumer, matricesEntry,
                    innerX0, innerY0, innerZ0,
                    innerX0, innerY1, innerZ1,
                    sprite.getMinU(), sprite.getMinV(),
                    sprite.getMaxU(), sprite.getMaxV(),
                    light, color);

            // East face (positive X)
            drawQuad(vertexConsumer, matricesEntry,
                    innerX1, innerY0, innerZ0,
                    innerX1, innerY1, innerZ1,
                    sprite.getMinU(), sprite.getMinV(),
                    sprite.getMaxU(), sprite.getMaxV(),
                    light, color);
        };

        // Submit the custom renderer with the appropriate render layer
        queue.submitCustom(matrices, RenderLayers.getEntityBlockLayer(fluidState.getBlockState()), customRenderer);
    }
}