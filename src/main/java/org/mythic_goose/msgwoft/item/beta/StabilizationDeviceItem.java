package org.mythic_goose.msgwoft.item.beta;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class StabilizationDeviceItem extends Item {

    public StabilizationDeviceItem(Properties properties) {
        super(properties.durability(20));
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {
        list.add(Component.translatable("tooltip.beta_item"));

        list.add(Component.literal("§7Right-click to repair all your items to full"));
    }
}