package unqualified.chemistry.util;

import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class RenderUtils {
    private static void drawVertex(VertexConsumer builder, MatrixStack.Entry matricesEntry, float x, float y, float z, float u, float v, int packedLight, int color) {
        builder.vertex(matricesEntry, x, y, z)
                .color(color)
                .texture(u, v)
                .overlay(0)
                .light(packedLight)
                .normal(matricesEntry, 0, 1, 0);
    }

    public static void drawYFace(VertexConsumer builder, MatrixStack.Entry matricesEntry,
                                  float x0, float z0, float x1, float z1, float y,
                                  float u0, float v0, float u1, float v1,
                                  int packedLight, int color, boolean topFace) {
        if (topFace) {
            // Top face - counter-clockwise winding
            drawVertex(builder, matricesEntry, x0, y, z0, u0, v1, packedLight, color);
            drawVertex(builder, matricesEntry, x0, y, z1, u0, v0, packedLight, color);
            drawVertex(builder, matricesEntry, x1, y, z1, u1, v0, packedLight, color);
            drawVertex(builder, matricesEntry, x1, y, z0, u1, v1, packedLight, color);
        } else {
            // Bottom face - clockwise winding (faces down)
            drawVertex(builder, matricesEntry, x0, y, z0, u0, v1, packedLight, color);
            drawVertex(builder, matricesEntry, x1, y, z0, u1, v1, packedLight, color);
            drawVertex(builder, matricesEntry, x1, y, z1, u1, v0, packedLight, color);
            drawVertex(builder, matricesEntry, x0, y, z1, u0, v0, packedLight, color);
        }
    }

    public static void drawZFace(VertexConsumer builder, MatrixStack.Entry matricesEntry,
                                  float x0, float y0, float x1, float y1, float z,
                                  float u0, float v0, float u1, float v1,
                                  int packedLight, int color, boolean positiveZ) {
        if (positiveZ) {
            // North face (positive Z)
            drawVertex(builder, matricesEntry, x0, y0, z, u0, v1, packedLight, color);
            drawVertex(builder, matricesEntry, x1, y0, z, u1, v1, packedLight, color);
            drawVertex(builder, matricesEntry, x1, y1, z, u1, v0, packedLight, color);
            drawVertex(builder, matricesEntry, x0, y1, z, u0, v0, packedLight, color);
        } else {
            // South face (negative Z)
            drawVertex(builder, matricesEntry, x0, y0, z, u0, v1, packedLight, color);
            drawVertex(builder, matricesEntry, x0, y1, z, u0, v0, packedLight, color);
            drawVertex(builder, matricesEntry, x1, y1, z, u1, v0, packedLight, color);
            drawVertex(builder, matricesEntry, x1, y0, z, u1, v1, packedLight, color);
        }
    }

    public static void drawXFace(VertexConsumer builder, MatrixStack.Entry matricesEntry,
                                  float y0, float z0, float y1, float z1, float x,
                                  float u0, float v0, float u1, float v1,
                                  int packedLight, int color, boolean positiveX) {
        if (positiveX) {
            // East face (positive X)
            drawVertex(builder, matricesEntry, x, y0, z0, u0, v1, packedLight, color);
            drawVertex(builder, matricesEntry, x, y1, z0, u0, v0, packedLight, color);
            drawVertex(builder, matricesEntry, x, y1, z1, u1, v0, packedLight, color);
            drawVertex(builder, matricesEntry, x, y0, z1, u1, v1, packedLight, color);
        } else {
            // West face (negative X)
            drawVertex(builder, matricesEntry, x, y0, z0, u0, v1, packedLight, color);
            drawVertex(builder, matricesEntry, x, y0, z1, u1, v1, packedLight, color);
            drawVertex(builder, matricesEntry, x, y1, z1, u1, v0, packedLight, color);
            drawVertex(builder, matricesEntry, x, y1, z0, u0, v0, packedLight, color);
        }
    }
}
