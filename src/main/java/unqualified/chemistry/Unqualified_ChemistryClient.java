package unqualified.chemistry;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.BlockRenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import unqualified.chemistry.block.ModBlocks;
import unqualified.chemistry.block.entity.ModBlockEntities;
import unqualified.chemistry.block.entity.renderer.BeakerBlockEntityRenderer;
import unqualified.chemistry.screen.ModScreenHandlers;
import unqualified.chemistry.screen.custom.BeakerScreen;

public class Unqualified_ChemistryClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockEntityRendererFactories.register(ModBlockEntities.BEAKER_BE, BeakerBlockEntityRenderer::new);
        HandledScreens.register(ModScreenHandlers.BEAKER_SCREEN_HANDLER, BeakerScreen::new);

        BlockRenderLayerMap.putBlock(ModBlocks.BEAKER_BLOCK, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(ModBlocks.BEAKER_BLOCK, BlockRenderLayer.TRANSLUCENT);

    }
}
