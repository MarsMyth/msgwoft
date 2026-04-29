package org.mythic_goose.msgwoft.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.mythic_goose.msgwoft.init.ModMobEffects;

import java.util.List;

public class SynchronisedOrbItem extends Item {
    public SynchronisedOrbItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {
        list.add(Component.translatable("tooltip.no_recipe"));

        list.add(Component.translatable("tooltip.sychronised_orb.use"));
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int i, boolean bl) {
        // bl = true means this slot is the currently selected main hand slot
        // We also check if the item is in the off hand slot directly
        if (!level.isClientSide && entity instanceof LivingEntity livingEntity) {
            boolean isInMainHand = bl;
            boolean isInOffHand = livingEntity.getItemBySlot(EquipmentSlot.OFFHAND) == itemStack;

            if (isInMainHand || isInOffHand) {
                if (livingEntity.hasEffect(ModMobEffects.DIMENSIONAL_DESYNC)) {
                    livingEntity.removeEffect(ModMobEffects.DIMENSIONAL_DESYNC);
                }
            }
        }

        super.inventoryTick(itemStack, level, entity, i, bl);
    }
}