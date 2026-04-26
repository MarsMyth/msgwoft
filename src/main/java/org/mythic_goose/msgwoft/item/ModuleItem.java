package org.mythic_goose.msgwoft.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;
import java.util.Objects;

public class ModuleItem extends Item {
    public final String moduleType;
    
    public ModuleItem(Properties properties, String moduleType) {
        super(properties);
        this.moduleType = moduleType;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {
        list.add(Component.literal(" "));
        
        if (Objects.equals(moduleType, "Luck")) {
            list.add(Component.translatable("tooltip.module.when_in_identity"));
            list.add(Component.translatable("tooltip.luck_module.identify"));
            list.add(Component.translatable("tooltip.module.when_in_chemistry"));
            list.add(Component.translatable("tooltip.luck_module.chemistry"));
        } else if (Objects.equals(moduleType, "Efficiency")) {
            list.add(Component.translatable("tooltip.module.when_in_identity"));
            list.add(Component.translatable("tooltip.efficiency_module.identify"));
            
        }

        super.appendHoverText(itemStack, tooltipContext, list, tooltipFlag);
    }
}
