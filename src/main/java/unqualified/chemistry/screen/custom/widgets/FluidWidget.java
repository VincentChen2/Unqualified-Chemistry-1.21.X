package unqualified.chemistry.screen.custom.widgets;

import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.texture.Sprite;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.world.World;
import unqualified.chemistry.util.ScreenUtils;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class FluidWidget implements Drawable, Widget {
    private final SingleVariantStorage<FluidVariant> fluidStorage;
    private final Supplier<BlockPos> posSupplier;
    private final int width, height;
    private int x, y;

    public FluidWidget(SingleVariantStorage<FluidVariant> fluidStorage, Supplier<BlockPos> posSupplier, int width, int height, int x, int y) {
        this.fluidStorage = fluidStorage;
        this.posSupplier = posSupplier;
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
    }

    public static Builder builder(SingleVariantStorage<FluidVariant> fluidStorage) {
        return new Builder(fluidStorage);
    }


    private static boolean isPointWithinBounds(int x, int y, int width, int height, int pointX, int pointY) {
        return pointX >= x && pointX <= x + width &&
                pointY >= y && pointY <= y + height;
    }

    private static long getMilliliters(long amount) {
        return ((amount / FluidConstants.BOTTLE) * 500);
    }

    protected void drawTooltip(DrawContext context, int mouseX, int mouseY) {
        Fluid fluid = this.fluidStorage.variant.getFluid();
        long fluidAmount = this.fluidStorage.getAmount();
        long fluidCapacity = this.fluidStorage.getCapacity();
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        if (fluid != null && fluidAmount > 0) {
            List<Text> texts = List.of(
                    Text.translatable(fluid.getDefaultState().getBlockState().getBlock().getTranslationKey()),
                    Text.literal("%s / %s ml".formatted(getMilliliters(fluidAmount), getMilliliters(fluidCapacity)))
            );
            context.drawTooltip(textRenderer, texts, mouseX, mouseY);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        Fluid fluid = this.fluidStorage.variant.getFluid();
        long fluidAmount = this.fluidStorage.getAmount();
        long fluidCapacity = this.fluidStorage.getCapacity();
        int fluidHeight = Math.round(((float) fluidAmount / fluidCapacity) * this.height);
        FluidRenderHandler fluidRenderHandler = FluidRenderHandlerRegistry.INSTANCE.get(fluid);
        BlockPos pos = this.posSupplier.get();
        FluidState fluidState = fluid.getDefaultState();
        World world = MinecraftClient.getInstance().world;

        if (fluidRenderHandler == null) return;
        Sprite sprite = fluidRenderHandler.getFluidSprites(world, pos, fluidState)[0];
        int tintColor = fluidRenderHandler.getFluidColor(world, pos, fluidState);

        //TODO: Make opacity variable based on fluid type
        //Extract RGB components and set alpha to 192 (three-quarters opacity)
        int red = ColorHelper.getRed(tintColor);
        int green = ColorHelper.getGreen(tintColor);
        int blue = ColorHelper.getBlue(tintColor);
        tintColor = ColorHelper.getArgb(192, red, green, blue);

        if (fluidAmount <= 0) return;
        if (world == null) return;

        ScreenUtils.renderTiledSprite(context, sprite, this.x, this.y + this.height - fluidHeight, this.width, fluidHeight, tintColor);

        if (isPointWithinBounds(this.x, this.y, this.width, this.height, mouseX, mouseY)) {
            drawTooltip(context, mouseX, mouseY);
        }
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getY() {
        return this.y;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public void forEachChild(Consumer<ClickableWidget> consumer) {

    }

    public static class Builder {
        private final SingleVariantStorage<FluidVariant> fluidStorage;
        private Supplier<BlockPos> posSupplier = () -> null ;
        private int width, height;
        private int x, y;

        public Builder(SingleVariantStorage<FluidVariant> fluidStorage) {
            this.fluidStorage = fluidStorage;
        }

        public Builder x(int x) {
            this.x = x;
            return this;
        }

        public Builder y(int y) {
            this.y = y;
            return this;
        }
        public Builder position(int x, int y) {
            this.x = x;
            this.y = y;
            return this;
        }

        public Builder width(int width) {
            this.width = width;
            return this;
        }

        public Builder height(int height) {
            this.height = height;
            return this;
        }

        public Builder size(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public Builder bounds(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            return this;
        }

        public Builder posSupplier(Supplier<BlockPos> posSupplier) {
            this.posSupplier = posSupplier;
            return this;
        }

        public FluidWidget build() {
            return new FluidWidget(this.fluidStorage, this.posSupplier, this.width, this.height, this.x, this.y);
        }
    }
}
