package org.mythic_goose.msgwoft.recipe;

import net.minecraft.world.item.ItemStack;

import java.util.List;

/**
 * Represents one Chemistry Station recipe loaded from JSON.
 *
 * JSON format:
 * {
 *   "output": "minecraft:diamond",
 *   "output_count": 1,
 *   "ingredients": [
 *     { "color": "red",    "item": "minecraft:cobblestone" },
 *     { "color": "red",    "item": "minecraft:cobblestone" },
 *     { "color": "green",  "item": "minecraft:sand" },
 *     { "color": "blue",   "item": "minecraft:clay_ball" },
 *     { "color": "purple", "item": "minecraft:dirt" },
 *     { "color": "purple", "item": "minecraft:dirt" }
 *   ]
 * }
 *
 * Each entry = one bar segment of that color.
 * Max 12 ingredients total. Each color maps to one of the 4 chemical slots.
 * Multiple entries of the same color = multiple items consumed from that slot.
 *
 * Red = Minerals (Diamonds, Netherite, Gold, Copper, Emeralds, Iron, Coal)
 * Green = Blocks
 * Blue = Miscellaneous items
 * Purple = Rare+ Miscellaneous items
 */
public class ChemistryStationRecipe {

    public enum Color { RED, GREEN, BLUE, PURPLE }

    public record Ingredient(Color color, String itemId) {}

    private final String outputItemId;
    private final int outputCount;
    private final List<Ingredient> ingredients;

    public ChemistryStationRecipe(String outputItemId, int outputCount,
                                  List<Ingredient> ingredients) {
        this.outputItemId  = outputItemId;
        this.outputCount   = outputCount;
        this.ingredients   = List.copyOf(ingredients);
    }

    public String getOutputItemId() { return outputItemId; }
    public int    getOutputCount()  { return outputCount; }

    /** All ingredients in order — each entry = one bar segment. */
    public List<Ingredient> getIngredients() { return ingredients; }

    /** Total bar segments this recipe requires (≤ 12). */
    public int getTotalSegments() { return ingredients.size(); }

    /** How many items of a given color are required. */
    public int countOf(Color color) {
        return (int) ingredients.stream().filter(i -> i.color() == color).count();
    }

    /** The required item id for a given color (null if that color isn't used). */
    public String itemIdFor(Color color) {
        return ingredients.stream()
                .filter(i -> i.color() == color)
                .map(Ingredient::itemId)
                .findFirst()
                .orElse(null);
    }
}