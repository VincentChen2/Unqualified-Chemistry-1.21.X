package unqualified.chemistry.screen.custom;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import unqualified.chemistry.Unqualified_Chemistry;
import unqualified.chemistry.block.entity.custom.BeakerBlockEntity;

public class BeakerScreen extends HandledScreen<BeakerScreenHandler> {
    private static final Identifier TEXTURE = Identifier.of(Unqualified_Chemistry.MOD_ID, "textures/gui/beaker.png");

    public BeakerScreen(BeakerScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundWidth = 176;
        this.backgroundHeight = 166;
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        context.drawTexture(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight, 256, 256);

        // Draw fluid information
        if (handler.getBlockEntity() != null) {
            BeakerBlockEntity beaker = handler.getBlockEntity();
            String volumeText = "Volume: " + beaker.getCurrentVolume() + " / " + beaker.getMaxCapacity() + " ml";
            String fluidText = "Fluid: " + beaker.getFluidType().toString();

            context.drawText(this.textRenderer, volumeText, x + 8, y + 25, 0xFFFFFFFF, true);
            context.drawText(this.textRenderer, fluidText, x + 8, y + 40, 0xFFFFFFFF, true);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }

    @Override
    protected void init() {
        super.init();
        titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;
    }
}