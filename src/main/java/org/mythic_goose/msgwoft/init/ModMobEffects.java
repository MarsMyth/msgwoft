package org.mythic_goose.msgwoft.init;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.mythic_goose.msgwoft.MSGWOFT;
import org.mythic_goose.msgwoft.effect.EmptyMobEffect;

public class ModMobEffects {

    public static final Holder.Reference<MobEffect> DIMENSIONAL_DESYNC = registerMobEffect("dimensional_desync",
            new EmptyMobEffect(MobEffectCategory.HARMFUL, 0x67574A).addAttributeModifier(
                    Attributes.MAX_HEALTH,
                    ResourceLocation.fromNamespaceAndPath(MSGWOFT.MOD_ID, "dimensional_desync"),
                    -0.95, // -25% (multiply operation)
                    AttributeModifier.Operation.ADD_MULTIPLIED_BASE
            ));


    private static Holder.Reference<MobEffect> registerMobEffect(String name, MobEffect statusEffect) {
        return Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, ResourceLocation.fromNamespaceAndPath(MSGWOFT.MOD_ID, name), statusEffect);
    }

    public static void registerEffects() {
        MSGWOFT.LOGGER.info("Registering Mod Effects for " + MSGWOFT.MOD_ID);
    }
}
