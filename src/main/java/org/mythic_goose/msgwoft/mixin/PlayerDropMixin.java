package org.mythic_goose.msgwoft.mixin;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.mythic_goose.msgwoft.item.WrittenRecipeItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerDropMixin {
    @Inject(method = "drop(Lnet/minecraft/world/item/ItemStack;ZZ)Lnet/minecraft/world/entity/item/ItemEntity;",
            at = @At("HEAD"))
    private void onDrop(ItemStack itemStack, boolean bl, boolean bl2, CallbackInfoReturnable<ItemEntity> cir) {
        if (itemStack != null && itemStack.getItem() instanceof WrittenRecipeItem) {
            System.out.println("[WrittenRecipe] drop() called — stack=" + itemStack
                    + " nbt=" + itemStack.get(DataComponents.CUSTOM_DATA));
        }
    }
}
