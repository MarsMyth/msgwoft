package org.mythic_goose.msgwoft.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.core.Registry;

public class ModSounds {

    public static final SoundEvent FB_EXPLODE = register("fb.explode");
    public static final SoundEvent FB_LAND    = register("fb.land");
    public static final SoundEvent FB_RING1   = register("fb.ring1");
    public static final SoundEvent FB_RING2   = register("fb.ring2");

    private static SoundEvent register(String name) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath("msgwoft", name);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
    }

    public static void register() {
        // Calling this method is enough to trigger static init
    }
}