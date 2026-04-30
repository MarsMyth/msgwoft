package org.mythic_goose.msgwoft.item;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.mythic_goose.msgwoft.client.tooltip.RecipeTooltipData;
import org.mythic_goose.msgwoft.recipe.ChemistryStationRecipe;
import org.mythic_goose.msgwoft.recipe.ChemistryStationRecipeManager;
import org.mythic_goose.msgwoft.screen.item.WrittenRecipeMenu;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class WrittenRecipeItem extends Item {

    public static final String NBT_RECIPE_INDEX = "RecipeIndex";
    public static final String NBT_OPENED       = "Opened";

    public WrittenRecipeItem(Properties properties) {
        super(properties);
    }

    // ── Right-click ───────────────────────────────────────────────────────────

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            ItemStack modified = stack.copy();

            if (!hasNbt(modified, NBT_RECIPE_INDEX)) {
                List<ChemistryStationRecipe> recipes = ChemistryStationRecipeManager.getAllRecipes();
                if (!recipes.isEmpty()) {
                    int index = new Random().nextInt(recipes.size());
                    putInt(modified, NBT_RECIPE_INDEX, index);
                }
            }

            putByte(modified, NBT_OPENED, (byte) 1);
            player.setItemInHand(hand, modified);

            final int recipeIndex = getInt(modified, NBT_RECIPE_INDEX);
            // Resolve slot index now, before the lambda captures anything
            final int slotIndex = hand == InteractionHand.MAIN_HAND
                    ? player.getInventory().selected
                    : 40;

            if (player instanceof ServerPlayer serverPlayer) {
                serverPlayer.openMenu(new ExtendedScreenHandlerFactory<Integer>() {
                    @Override
                    public Component getDisplayName() {
                        return Component.translatable("item.msgwoft.written_recipe");
                    }

                    @Override
                    public AbstractContainerMenu createMenu(int id, Inventory inv, Player p) {
                        return new WrittenRecipeMenu(id, inv, modified, slotIndex);
                    }

                    @Override
                    public Integer getScreenOpeningData(ServerPlayer player) {
                        return recipeIndex;
                    }
                });
            }

            return InteractionResultHolder.sidedSuccess(modified, false);
        }

        return InteractionResultHolder.sidedSuccess(stack, true);
    }

    // ── Tooltip ───────────────────────────────────────────────────────────────

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context,
                                List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);

        boolean opened = getByte(stack, NBT_OPENED) == 1;

        if (Screen.hasShiftDown()) {
            if (opened) {
                tooltip.add(Component.literal("[Previewing recipe]")
                        .withStyle(ChatFormatting.DARK_GRAY));
            } else {
                tooltip.add(Component.literal("Recipe: Unknown")
                        .withStyle(ChatFormatting.DARK_GRAY));
            }
        } else {
            if (!opened) {
                tooltip.add(Component.literal("Recipe: Unknown")
                        .withStyle(ChatFormatting.DARK_GRAY));
            } else {
                String recipeName = getRecipeName(stack);
                tooltip.add(Component.literal("Recipe: " + recipeName)
                        .withStyle(ChatFormatting.GRAY));
                tooltip.add(Component.literal("Hold Shift to preview")
                        .withStyle(ChatFormatting.DARK_GRAY));
            }
        }
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        if (getByte(stack, NBT_OPENED) != 1) return Optional.empty();
        if (!Screen.hasShiftDown()) return Optional.empty();
        ChemistryStationRecipe recipe = getRecipe(stack);
        if (recipe == null) return Optional.empty();
        return Optional.of(new RecipeTooltipData(recipe));
    }

    // ── Public helpers ────────────────────────────────────────────────────────

    public static String getRecipeName(ItemStack stack) {
        if (!hasNbt(stack, NBT_RECIPE_INDEX)) return "Unknown";
        int index = getInt(stack, NBT_RECIPE_INDEX);
        List<ChemistryStationRecipe> recipes = ChemistryStationRecipeManager.getAllRecipes();
        if (index < 0 || index >= recipes.size()) return "Unknown";
        String rawId = recipes.get(index).getOutputItemId();
        String path  = rawId.contains(":") ? rawId.split(":")[1] : rawId;
        return capitalise(path.replace("_", " "));
    }

    public static ChemistryStationRecipe getRecipe(ItemStack stack) {
        if (!hasNbt(stack, NBT_RECIPE_INDEX)) return null;
        int index = getInt(stack, NBT_RECIPE_INDEX);
        List<ChemistryStationRecipe> recipes = ChemistryStationRecipeManager.getAllRecipes();
        if (index < 0 || index >= recipes.size()) return null;
        return recipes.get(index);
    }

    // ── CustomData helpers ────────────────────────────────────────────────────

    private static CompoundTag getOrCreateCustomData(ItemStack stack) {
        CustomData existing = stack.get(DataComponents.CUSTOM_DATA);
        return existing != null ? existing.copyTag() : new CompoundTag();
    }

    private static void saveCustomData(ItemStack stack, CompoundTag tag) {
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }

    public static boolean hasNbt(ItemStack stack, String key) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        return data != null && data.copyTag().contains(key);
    }

    public static void putInt(ItemStack stack, String key, int value) {
        CompoundTag tag = getOrCreateCustomData(stack);
        tag.putInt(key, value);
        saveCustomData(stack, tag);
    }

    public static void putByte(ItemStack stack, String key, byte value) {
        CompoundTag tag = getOrCreateCustomData(stack);
        tag.putByte(key, value);
        saveCustomData(stack, tag);
    }

    public static int getInt(ItemStack stack, String key) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        return data != null ? data.copyTag().getInt(key) : 0;
    }

    public static byte getByte(ItemStack stack, String key) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        return data != null ? data.copyTag().getByte(key) : 0;
    }

    // ── Internal ──────────────────────────────────────────────────────────────

    private static String capitalise(String s) {
        if (s == null || s.isEmpty()) return s;
        StringBuilder sb = new StringBuilder();
        for (String word : s.split(" ")) {
            if (!word.isEmpty())
                sb.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1)).append(" ");
        }
        return sb.toString().trim();
    }
}