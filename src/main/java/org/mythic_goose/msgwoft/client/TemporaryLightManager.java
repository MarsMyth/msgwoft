package org.mythic_goose.msgwoft.client;

import foundry.veil.api.client.render.light.data.PointLightData;
import foundry.veil.api.client.render.light.renderer.LightRenderHandle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Tracks short-lived Veil lights and removes them after their lifetime expires.
 * Call {@link #tick()} each client tick (e.g. from a ClientTickEvent).
 */
public class TemporaryLightManager {

    private record Entry(LightRenderHandle<PointLightData> handle, int lifetimeTicks) {}

    private static final List<Entry> lights = new ArrayList<>();

    /** Store the handle returned by lightRenderer.addLight(), not the PointLightData itself. */
    public static void addTemporary(LightRenderHandle<PointLightData> handle, int ticks) {
        lights.add(new Entry(handle, ticks));
    }

    /** Call once per client tick. */
    public static void tick() {
        List<Entry> next = new ArrayList<>();

        for (Entry e : lights) {
            int remaining = e.lifetimeTicks() - 1;
            if (remaining <= 0) {
                e.handle().free(); // correct Veil API to remove a light
            } else {
                next.add(new Entry(e.handle(), remaining));
            }
        }

        lights.clear();
        lights.addAll(next);
    }
}