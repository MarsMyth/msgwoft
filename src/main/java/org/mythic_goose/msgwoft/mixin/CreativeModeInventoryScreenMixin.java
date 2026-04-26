package org.mythic_goose.msgwoft.mixin;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.world.item.CreativeModeTab;
import org.mythic_goose.msgwoft.creativetab.BannerRenderer;
import org.mythic_goose.msgwoft.init.ModCreativeTabs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreativeModeInventoryScreen.class)
public class CreativeModeInventoryScreenMixin {

    @Shadow
    private static CreativeModeTab selectedTab;

    @Inject(method = "render", at = @At("TAIL"))
    private void msgwoft$renderBanners(GuiGraphics graphics, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (selectedTab == ModCreativeTabs.CORE) {
            BannerRenderer.render((CreativeModeInventoryScreen)(Object)this, graphics);
        }
    }
}