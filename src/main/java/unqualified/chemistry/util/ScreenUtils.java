package unqualified.chemistry.util;

import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class ScreenUtils {
    public static void renderTiledSprite(DrawContext context, Sprite sprite, int x, int y, int width, int height, int color) {
        int spriteWidth = sprite.getContents().getWidth();
        int spriteHeight = sprite.getContents().getHeight();

        int xCount = MathHelper.floor((float) width / spriteWidth);
        int yCount = MathHelper.floor((float) height / spriteHeight);
        int xRemainder = width % spriteWidth;
        int yRemainder = height % spriteHeight;

        Identifier atlasID = sprite.getAtlasId();
        float minU = sprite.getMinU();
        float minV = sprite.getMinV();

        for (int i = 0; i < xCount; i++) {
            for (int j = 0; j < yCount; j++) {
                int x1 = x + (i * spriteWidth);
                int y1 = y + (j * spriteHeight);
                int x2 = x1 + spriteWidth;
                int y2 = y1 + spriteHeight;

                float maxU = sprite.getMaxU();
                float maxV = sprite.getMaxV();
                context.drawTexturedQuad(RenderPipelines.GUI_TEXTURED,atlasID, x1, x2, y1, y2, minU, maxU, minV, maxV, color);
            }

            if (yRemainder > 0) {
                int x1 = x + (i * spriteWidth);
                int y1 = y + (yCount * spriteHeight);
                int x2 = x1 + spriteWidth;
                int y2 = y1 + yRemainder;

                float maxU = sprite.getMaxU();
                float maxV = minV + (sprite.getMaxV() - minV) * ((float) yRemainder / spriteHeight);
                context.drawTexturedQuad(RenderPipelines.GUI_TEXTURED,atlasID, x1, x2, y1, y2, minU, maxU, minV, maxV, color);
            }
        }

        if (xRemainder > 0) {
            for (int j = 0; j < yCount; j++) {
                int x1 = x + (xCount * spriteWidth);
                int y1 = y + (j * spriteHeight);
                int x2 = x1 + xRemainder;
                int y2 = y1 + spriteHeight;

                float maxU = minU + (sprite.getMaxU() - minU) * ((float) xRemainder / spriteWidth);
                float maxV = sprite.getMaxV();
                context.drawTexturedQuad(RenderPipelines.GUI_TEXTURED,atlasID, x1, x2, y1, y2, minU, maxU, minV, maxV, color);
            }

            if (yRemainder > 0) {
                int x1 = x + (xCount * spriteWidth);
                int y1 = y + (yCount * spriteHeight);
                int x2 = x1 + xRemainder;
                int y2 = y1 + yRemainder;

                float maxU = minU + (sprite.getMaxU() - minU) * ((float) xRemainder / spriteWidth);
                float maxV = minV + (sprite.getMaxV() - minV) * ((float) yRemainder / spriteHeight);
                context.drawTexturedQuad(RenderPipelines.GUI_TEXTURED,atlasID, x1, x2, y1, y2, minU, maxU, minV, maxV, color);
            }
        }
    }
}
