package org.mythic_goose.msgwoft.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.mythic_goose.msgwoft.item.IonisedVoidShardItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(ItemStack.class)
public class ItemDurabilityMixin {

    @Inject(
            method = "hurtAndBreak(ILnet/minecraft/server/level/ServerLevel;Lnet/minecraft/server/level/ServerPlayer;Ljava/util/function/Consumer;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onHurtAndBreak(int amount, ServerLevel level, ServerPlayer player, Consumer<Item> onBroken, CallbackInfo ci) {
        if (player == null) return;
        for (ItemStack stack : player.getInventory().items) {
            if (stack.getItem() instanceof IonisedVoidShardItem) {
                ci.cancel();
                return;
            }
        }
    }
}