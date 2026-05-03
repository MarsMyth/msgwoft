package org.mythic_goose.msgwoft.recipe;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;
import org.mythic_goose.msgwoft.init.ModItems;

import java.util.HashMap;
import java.util.Map;

public class DryingRackRecipes {
    private static final Map<Item, Item> RECIPES = new HashMap<>();

    static {
        RECIPES.put(Items.ROTTEN_FLESH, ModItems.MONSTER_JERKY);
    }

    public static @Nullable ItemStack getResult(ItemStack input) {
        Item result = RECIPES.get(input.getItem());
        return result != null ? new ItemStack(result) : null;
    }
}