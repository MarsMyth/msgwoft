package org.mythic_goose.msgwoft.client.render;

import com.mojang.blaze3d.vertex.VertexConsumer;

public class AlphaVertexConsumer implements VertexConsumer {
    private final VertexConsumer delegate;
    private final float alpha; // 0.0–1.0

    public AlphaVertexConsumer(VertexConsumer delegate, float alpha) {
        this.delegate = delegate;
        this.alpha = Math.clamp(alpha, 0f, 1f);
    }

    @Override
    public VertexConsumer setColor(int r, int g, int b, int a) {
        return delegate.setColor(r, g, b, Math.round(a * alpha));
    }

    // The packed-all-in-one vertex path used by ItemRenderer in 1.21
    @Override
    public void addVertex(float x, float y, float z,
                          int color,
                          float u, float v,
                          int uv2, int uv1,
                          float nx, float ny, float nz) {
        int a = (color >>> 24) & 0xFF;
        int scaledA = Math.round(a * alpha);
        int newColor = (scaledA << 24) | (color & 0x00FFFFFF);
        delegate.addVertex(x, y, z, newColor, u, v, uv2, uv1, nx, ny, nz);
    }

    // ── Delegate everything else ───────────────────────────────────────────────

    @Override
    public VertexConsumer addVertex(float x, float y, float z) {
        return delegate.addVertex(x, y, z);
    }

    @Override
    public VertexConsumer setUv(float u, float v) {
        return delegate.setUv(u, v);
    }

    @Override
    public VertexConsumer setUv1(int u, int v) {
        return delegate.setUv1(u, v);
    }

    @Override
    public VertexConsumer setUv2(int u, int v) {
        return delegate.setUv2(u, v);
    }

    @Override
    public VertexConsumer setNormal(float nx, float ny, float nz) {
        return delegate.setNormal(nx, ny, nz);
    }
}