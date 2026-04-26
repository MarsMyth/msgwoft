package org.mythic_goose.msgwoft.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import org.mythic_goose.msgwoft.init.ModItems;

import java.util.List;
import java.util.Random;

public class DataSpecimenItem extends Item {
    private static final Random RANDOM = new Random();

    public DataSpecimenItem(Properties properties) {
        super(properties);
    }

    public static ItemStack create(float con, float cry, float qua, String sourceBlock) {
        ItemStack stack = new ItemStack(ModItems.DATA_SPECIMEN);

        CompoundTag tag = new CompoundTag();
        tag.putFloat("Concentration",   con);
        tag.putFloat("Crystallization", cry);
        tag.putFloat("Quality",         qua);
        tag.putString("SourceBlock",    sourceBlock);
        tag.putInt("Design", RANDOM.nextInt(3));
        tag.putInt("Color",  randomColor());

        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        return stack;
    }

    public static int randomColor() {
        int r = 100 + RANDOM.nextInt(156);
        int g = 100 + RANDOM.nextInt(156);
        int b = 100 + RANDOM.nextInt(156);
        return 0xFF000000 | (r << 16) | (g << 8) | b;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> tooltip, TooltipFlag flag) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        if (data == null) return;

        CompoundTag tag = data.copyTag();

        tooltip.add(Component.literal("§9Unidentified Specimen"));
        tooltip.add(Component.literal("§8   - Identify to improve stats"));
        tooltip.add(Component.literal(" "));

        float con = tag.getFloat("Concentration")   * 100f;
        float cry = tag.getFloat("Crystallization") * 100f;
        float qua = tag.getFloat("Quality")         * 100f;

        if (con > 100f && cry > 100f && qua > 100f) {
            tooltip.add(Component.literal("§6✦ Perfect specimen"));
        }

        tooltip.add(Component.literal("§7Concentration:   " + getValueColor(con) + fmt(con)));
        tooltip.add(Component.literal("§7Crystallization: " + getValueColor(cry) + fmt(cry)));
        tooltip.add(Component.literal("§7Quality:         " + getValueColor(qua) + fmt(qua)));

        if (tag.contains("SourceBlock")) {
            String raw = tag.getString("SourceBlock");
            String[] parts = raw.split("\\.");
            String blockName = parts.length > 0 ? parts[parts.length - 1] : raw;
            String display = blockName.substring(0, 1).toUpperCase() + blockName.replace("_", " ").substring(1);
            tooltip.add(Component.literal("§7Source: §f" + display));
        }


    }
    private static String fmt(float v) {
        return String.format("%.0f%%", v);
    }

    private static String getValueColor(float val) {
        if (val > 95f)      return "§a";
        else if (val > 75f) return "§2";
        else if (val > 50f) return "§e";
        else if (val > 35f) return "§6";
        else if (val > 10f) return "§c";
        else                return "§4";
    }
}