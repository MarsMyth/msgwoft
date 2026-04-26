package org.mythic_goose.msgwoft.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class IonisedVoidShardItem extends Item {
    public IonisedVoidShardItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {

        list.add(Component.translatable("tooltip.preventing_durability"));

        super.appendHoverText(itemStack, tooltipContext, list, tooltipFlag);
    }
}
