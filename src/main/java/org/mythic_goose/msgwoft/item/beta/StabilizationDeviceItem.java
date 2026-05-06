package org.mythic_goose.msgwoft.item.beta;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.mythic_goose.msgwoft.init.ModItems;

import java.util.List;

public class StabilizationDeviceItem extends Item {

    public StabilizationDeviceItem(Properties properties) {
        super(properties.durability(15));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack heldStack = player.getItemInHand(interactionHand);

        if (!level.isClientSide) {
            boolean repairedAny = false;

            // Iterate over all inventory slots
            for (ItemStack stack : player.getInventory().items) {
                // Skip this item itself
                if (stack == heldStack) continue;
                if (stack.is(ModItems.STABILIZATION_DEVICE)) continue;
                // Skip items with no durability or already at full durability
                if (!stack.isDamageableItem()) continue;
                if (stack.getDamageValue() == 0) continue;

                // Repair to full durability
                stack.setDamageValue(0);
                repairedAny = true;
            }

            // Also check armor and offhand slots
            for (ItemStack stack : player.getInventory().armor) {
                if (!stack.isDamageableItem()) continue;
                if (stack.getDamageValue() == 0) continue;
                stack.setDamageValue(0);
                repairedAny = true;
            }

            ItemStack offhand = player.getInventory().offhand.get(0);
            if (offhand != heldStack && offhand.isDamageableItem() && offhand.getDamageValue() > 0) {
                offhand.setDamageValue(0);
                repairedAny = true;
            }

            if (repairedAny) {
                // Damage this item by 1
                heldStack.hurtAndBreak(1, player, player.getEquipmentSlotForItem(heldStack));
            }
        }

        return InteractionResultHolder.success(heldStack);
    }
    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {
        list.add(Component.translatable("tooltip.repair_kit"));
    }
}