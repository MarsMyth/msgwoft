package org.mythic_goose.msgwoft.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.io.InputStreamReader;
import java.util.*;

public class ChemistryStationRecipeManager {

    private static final List<ChemistryStationRecipe> RECIPES = new ArrayList<>();

    public static void loadRecipes(MinecraftServer server) {
        RECIPES.clear();

        var resources = server.getResourceManager().listResources(
                "chemistry_recipes",
                id -> id.getPath().endsWith(".json"));

        for (var entry : resources.entrySet()) {
            try (var reader = new InputStreamReader(entry.getValue().open())) {
                JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
                ChemistryStationRecipe recipe = parseRecipe(json);
                if (recipe != null) RECIPES.add(recipe);
            } catch (Exception e) {
                System.err.println("[ChemistryStation] Failed to load recipe "
                        + entry.getKey() + ": " + e.getMessage());
            }
        }

        System.out.println("[ChemistryStation] Loaded " + RECIPES.size() + " recipe(s).");
    }

    private static ChemistryStationRecipe parseRecipe(JsonObject json) {
        try {
            String outputId    = json.get("output").getAsString();
            int    outputCount = json.has("output_count") ? json.get("output_count").getAsInt() : 1;

            List<ChemistryStationRecipe.Ingredient> ingredients = new ArrayList<>();
            JsonArray arr = json.getAsJsonArray("ingredients");
            for (var el : arr) {
                JsonObject ing = el.getAsJsonObject();
                String colorStr = ing.get("color").getAsString().toUpperCase();
                String itemId   = ing.get("item").getAsString();
                ChemistryStationRecipe.Color color =
                        ChemistryStationRecipe.Color.valueOf(colorStr);
                ingredients.add(new ChemistryStationRecipe.Ingredient(color, itemId));
            }

            if (ingredients.size() > 12) {
                System.err.println("[ChemistryStation] Recipe for " + outputId
                        + " has " + ingredients.size() + " ingredients (max 12), truncating.");
                ingredients = ingredients.subList(0, 12);
            }

            return new ChemistryStationRecipe(outputId, outputCount, ingredients);
        } catch (Exception e) {
            System.err.println("[ChemistryStation] Malformed recipe: " + e.getMessage());
            return null;
        }
    }

    // ── Sequence match (strict, ordered) ─────────────────────────────────────

    /**
     * Matches the player's queued color sequence against recipe ingredient order exactly.
     * e.g. [RED, RED, GREEN, BLUE] must match the first 4 ingredient colors in order.
     */
    public static Optional<ChemistryStationRecipe> findMatchBySequence(
            List<ChemistryStationRecipe.Color> sequence) {
        outer:
        for (ChemistryStationRecipe recipe : RECIPES) {
            List<ChemistryStationRecipe.Ingredient> ings = recipe.getIngredients();
            if (ings.size() != sequence.size()) continue;
            for (int i = 0; i < ings.size(); i++) {
                if (ings.get(i).color() != sequence.get(i)) continue outer;
            }
            return Optional.of(recipe);
        }
        return Optional.empty();
    }

    /**
     * Verifies that the chemical slots actually contain the right items
     * and enough count to fulfil the recipe before consuming anything.
     *
     * Slot layout expected in the NonNullList:
     *   index 1 = RED, 2 = GREEN, 3 = BLUE, 4 = PURPLE
     */
    public static boolean slotsHaveIngredients(ChemistryStationRecipe recipe,
                                               NonNullList<ItemStack> items) {
        for (ChemistryStationRecipe.Color color : ChemistryStationRecipe.Color.values()) {
            int needed = recipe.countOf(color);
            if (needed == 0) continue;

            int slotIdx = colorToSlotIndex(color);
            ItemStack slot = items.get(slotIdx);
            if (slot.isEmpty()) return false;

            // Item type must match what the recipe lists for this color
            String requiredId = recipe.itemIdFor(color);
            String actualId   = BuiltInRegistries.ITEM.getKey(slot.getItem()).toString();
            if (!actualId.equals(requiredId)) return false;

            if (slot.getCount() < needed) return false;
        }
        return true;
    }

    // ── Legacy slot-based match (kept for any preview/tooltip use) ────────────

    public static Optional<ChemistryStationRecipe> findMatch(
            ItemStack red, ItemStack green, ItemStack blue, ItemStack purple) {
        for (ChemistryStationRecipe recipe : RECIPES) {
            if (slotMatches(red,    recipe, ChemistryStationRecipe.Color.RED)
                    && slotMatches(green,  recipe, ChemistryStationRecipe.Color.GREEN)
                    && slotMatches(blue,   recipe, ChemistryStationRecipe.Color.BLUE)
                    && slotMatches(purple, recipe, ChemistryStationRecipe.Color.PURPLE)) {
                return Optional.of(recipe);
            }
        }
        return Optional.empty();
    }

    private static boolean slotMatches(ItemStack slot,
                                       ChemistryStationRecipe recipe,
                                       ChemistryStationRecipe.Color color) {
        int required = recipe.countOf(color);
        if (required == 0) return true;
        if (slot.isEmpty()) return false;
        String slotId = BuiltInRegistries.ITEM.getKey(slot.getItem()).toString();
        String reqId  = recipe.itemIdFor(color);
        return slotId.equals(reqId) && slot.getCount() >= required;
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    public static int colorToSlotIndex(ChemistryStationRecipe.Color color) {
        return switch (color) {
            case RED    -> 1;
            case GREEN  -> 2;
            case BLUE   -> 3;
            case PURPLE -> 4;
        };
    }

    public static List<ChemistryStationRecipe> getAllRecipes() {
        return Collections.unmodifiableList(RECIPES);
    }

    public static ItemStack buildResult(ChemistryStationRecipe recipe) {
        Item item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(recipe.getOutputItemId()));
        return new ItemStack(item, recipe.getOutputCount());
    }

    public static void loadFromPacket(List<ChemistryStationRecipe> recipes) {
        RECIPES.clear();
        RECIPES.addAll(recipes);
        System.out.println("[ChemistryStation] Client received " + RECIPES.size() + " recipe(s).");
    }
}