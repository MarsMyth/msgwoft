package org.mythic_goose.msgwoft.client.tooltip;

import net.minecraft.world.inventory.tooltip.TooltipComponent;
import org.mythic_goose.msgwoft.recipe.ChemistryStationRecipe;

/**
 * Server-side data carrier. Minecraft passes this to the client tooltip
 * system which then constructs a RecipeTooltipComponent from it.
 */
public record RecipeTooltipData(ChemistryStationRecipe recipe) implements TooltipComponent {
}