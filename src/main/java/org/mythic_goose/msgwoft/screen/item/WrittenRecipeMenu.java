package org.mythic_goose.msgwoft.screen.item;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.mythic_goose.msgwoft.init.ModMenuTypes;
import org.mythic_goose.msgwoft.item.WrittenRecipeItem;
import org.mythic_goose.msgwoft.recipe.ChemistryStationRecipe;
import org.mythic_goose.msgwoft.recipe.ChemistryStationRecipeManager;

import java.util.List;

public class WrittenRecipeMenu extends AbstractContainerMenu {

    private final ChemistryStationRecipe recipe;
    private final int slotIndex;

    // Server-side constructor — called from WrittenRecipeItem#use
    public WrittenRecipeMenu(int containerId, Inventory playerInventory, ItemStack stack, int slotIndex) {
        super(ModMenuTypes.WRITTEN_RECIPE, containerId);
        this.recipe    = WrittenRecipeItem.getRecipe(stack);
        this.slotIndex = slotIndex;
    }

    // Client-side constructor — receives recipeIndex from packet
    public WrittenRecipeMenu(int containerId, Inventory playerInventory, Integer recipeIndex) {
        super(ModMenuTypes.WRITTEN_RECIPE, containerId);
        List<ChemistryStationRecipe> recipes = ChemistryStationRecipeManager.getAllRecipes();
        this.recipe    = (recipeIndex != null && recipeIndex >= 0 && recipeIndex < recipes.size())
                ? recipes.get(recipeIndex) : null;
        this.slotIndex = -1;
    }

    @Override
    public boolean stillValid(Player player) {
        if (slotIndex < 0) return true;
        ItemStack held = player.getInventory().getItem(slotIndex);
        boolean valid = !held.isEmpty() && held.getItem() instanceof WrittenRecipeItem;
        System.out.println("[WrittenRecipe] stillValid check — slot=" + slotIndex
                + " item=" + held.getItem()
                + " empty=" + held.isEmpty()
                + " valid=" + valid);
        return valid;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    public ChemistryStationRecipe getRecipe() {
        return recipe;
    }

    public int getSlotIndex() {
        return slotIndex;
    }
}