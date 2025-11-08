package unqualified.chemistry.screen.custom;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import unqualified.chemistry.Unqualified_Chemistry;
import unqualified.chemistry.screen.custom.widgets.FluidWidget;

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

        addDrawable(FluidWidget.builder(this.handler.getBlockEntity().fluidStorage)
                .bounds(this.x + 80, this.y + 12, 16, 60 )
                .posSupplier(() -> this.handler.getBlockEntity().getPos())
                .build()
        );
    }
}