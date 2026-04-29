package org.mythic_goose.msgwoft.mixin;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.mythic_goose.msgwoft.init.ModMobEffects;
import org.mythic_goose.msgwoft.item.SynchronisedOrbItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z", at = @At("HEAD"), cancellable = true)
    private void preventEffectIfHoldingOrb(MobEffectInstance effectInstance, Entity source, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity self = (LivingEntity) (Object) this;

        // Only block your specific custom effect
        if (!effectInstance.is(ModMobEffects.DIMENSIONAL_DESYNC)) return;

        ItemStack mainHand = self.getItemBySlot(EquipmentSlot.MAINHAND);
        ItemStack offHand  = self.getItemBySlot(EquipmentSlot.OFFHAND);

        if (mainHand.getItem() instanceof SynchronisedOrbItem
                || offHand.getItem()  instanceof SynchronisedOrbItem) {
            cir.setReturnValue(false); // cancel — effect never gets added
        }
    }
}