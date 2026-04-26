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

    /**
     * Server-side constructor — called from WrittenRecipeItem#use via openMenu.
     * Resolves the recipe directly from the ItemStack.
     */
    public WrittenRecipeMenu(int containerId, Inventory playerInventory, ItemStack stack) {
        super(ModMenuTypes.WRITTEN_RECIPE, containerId);
        this.recipe = WrittenRecipeItem.getRecipe(stack);
    }

    /**
     * Client-side constructor — ExtendedScreenHandlerType decodes the Integer
     * from the packet and passes it directly here (NOT a buf, NOT a BlockPos).
     */
    public WrittenRecipeMenu(int containerId, Inventory playerInventory, Integer recipeIndex) {
        super(ModMenuTypes.WRITTEN_RECIPE, containerId);
        List<ChemistryStationRecipe> recipes = ChemistryStationRecipeManager.getAllRecipes();
        this.recipe = (recipeIndex != null && recipeIndex >= 0 && recipeIndex < recipes.size())
                ? recipes.get(recipeIndex)
                : null;
    }

    public ChemistryStationRecipe getRecipe() {
        return recipe;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}