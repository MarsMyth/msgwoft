package org.mythic_goose.msgwoft.client.tooltip;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.mythic_goose.msgwoft.recipe.ChemistryStationRecipe;

import java.util.List;

/**
 * Renders a scaled-down version of the Written Recipe GUI inside a tooltip.
 * Shown when the player holds Shift while hovering the item.
 *
 * Layout (all pixel values are at 0.5× scale of the full GUI):
 *
 *   [ bar ]  [ RED  ][ GRN ][ BLU ][ PRP ]     ← bar + slots side-by-side
 *                ▼
 *            [ output ]
 */
public class RecipeTooltipComponent implements ClientTooltipComponent {

    // ── Textures (shared with WrittenRecipeScreen) ────────────────────────────
    private static final ResourceLocation SLOT_RED    = loc("textures/gui/widget/slots/red_slot.png");
    private static final ResourceLocation SLOT_GREEN  = loc("textures/gui/widget/slots/green_slot.png");
    private static final ResourceLocation SLOT_BLUE   = loc("textures/gui/widget/slots/blue_slot.png");
    private static final ResourceLocation SLOT_PURPLE = loc("textures/gui/widget/slots/purple_slot.png");

    private static final ResourceLocation SLOT_RED_EMPTY    = loc("textures/gui/widget/slots/red_slot_empty.png");
    private static final ResourceLocation SLOT_GREEN_EMPTY  = loc("textures/gui/widget/slots/green_slot_empty.png");
    private static final ResourceLocation SLOT_BLUE_EMPTY   = loc("textures/gui/widget/slots/blue_slot_empty.png");
    private static final ResourceLocation SLOT_PURPLE_EMPTY = loc("textures/gui/widget/slots/purple_slot_empty.png");

    private static final ResourceLocation SEG_RED    = loc("textures/gui/written_recipe/icons/red_chemical.png");
    private static final ResourceLocation SEG_GREEN  = loc("textures/gui/written_recipe/icons/green_chemical.png");
    private static final ResourceLocation SEG_BLUE   = loc("textures/gui/written_recipe/icons/blue_chemical.png");
    private static final ResourceLocation SEG_PURPLE = loc("textures/gui/written_recipe/icons/purple_chemical.png");
    private static final ResourceLocation SEG_EMPTY  = loc("textures/gui/written_recipe/icons/empty_segment.png");

    // ── Mini-GUI dimensions (roughly half the full GUI) ───────────────────────
    private static final int BAR_SEGMENTS = 12;
    private static final int SEG_W        = 26;
    private static final int SEG_H_MINI   = 3;
    private static final int BAR_H        = BAR_SEGMENTS * SEG_H_MINI; // 36px

    private static final int SLOT_MINI    = 14;
    private static final int ITEM_MINI    = 10;

    private static final int GAP          = 6;

    private static final int SLOT_ROW_W   = 4 * (SLOT_MINI + 2);
    private static final int WIDGET_W     = SEG_W + GAP + SLOT_ROW_W;

    private static final int ARROW_H      = 8;
    private static final int OUT_H        = SLOT_MINI + 2;
    private static final int WIDGET_H     = BAR_H + ARROW_H + OUT_H;

    // ── Data ──────────────────────────────────────────────────────────────────
    private final ChemistryStationRecipe recipe;

    public RecipeTooltipComponent(ChemistryStationRecipe recipe) {
        this.recipe = recipe;
    }

    // ── ClientTooltipComponent ────────────────────────────────────────────────

    @Override
    public int getHeight() {
        return WIDGET_H + 4;
    }

    @Override
    public int getWidth(Font font) {
        return WIDGET_W;
    }

    @Override
    public void renderImage(Font font, int x, int y, GuiGraphics gfx) {
        if (recipe == null) return;

        List<ChemistryStationRecipe.Ingredient> ings = recipe.getIngredients();
        ChemistryStationRecipe.Color[] colors = ChemistryStationRecipe.Color.values();

        // ── Vertical bar (left side) ──────────────────────────────────────────
        int barX       = x;
        int barTopY    = y;
        int barBottomY = barTopY + BAR_H;

        gfx.fill(barX - 1, barTopY - 1, barX + SEG_W + 1, barBottomY + 1, 0x55000000);

        for (int seg = 0; seg < BAR_SEGMENTS; seg++) {
            int segY = barBottomY - (seg + 1) * SEG_H_MINI;
            ResourceLocation segTex = seg < ings.size()
                    ? segTex(ings.get(seg).color())
                    : SEG_EMPTY;
            RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
            gfx.blit(segTex, barX, segY, 0, 0, SEG_W, SEG_H_MINI, SEG_W, 6);
        }

        // ── Ingredient slots (right of bar, vertical column) ─────────────────
        int slotRowX = barX + SEG_W + GAP;
        // Shift down so the column starts at the top of the bar (y), not above it
        int slotRowY = y;

        for (int i = 0; i < colors.length; i++) {
            ChemistryStationRecipe.Color color = colors[i];
            int count  = recipe.countOf(color);
            String itemId = recipe.itemIdFor(color);

            int sx = slotRowX;
            int sy = slotRowY + i * (SLOT_MINI + 2);

            if (count > 0 && itemId != null) {
                RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
                gfx.blit(slotTex(color), sx, sy, 0, 0, SLOT_MINI, SLOT_MINI, SLOT_MINI, SLOT_MINI);

                int itemOffX = sx + (SLOT_MINI - ITEM_MINI) / 2;
                int itemOffY = sy + (SLOT_MINI - ITEM_MINI) / 2;
                ItemStack stack = buildStack(itemId, count);

                gfx.pose().pushPose();
                float scale = (float) ITEM_MINI / 16f;
                gfx.pose().translate(itemOffX, itemOffY, 0);
                gfx.pose().scale(scale, scale, 1f);
                gfx.renderItem(stack, 0, 0);
                if (count > 1) {
                    gfx.pose().translate(0, 0, 200);
                    gfx.drawString(font, String.valueOf(count), 16 - font.width(String.valueOf(count)), 9, 0xFFFFFF, true);
                }
                gfx.pose().popPose();
            } else {
                RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
                gfx.blit(slotEmptyTex(color), sx, sy, 0, 0, SLOT_MINI, SLOT_MINI, SLOT_MINI, SLOT_MINI);
            }
        }

        // ── Down arrow ────────────────────────────────────────────────────────
        int arrowX = barX + SEG_W / 2 - font.width("▼") / 2;
        int arrowY = barBottomY + 2;
        gfx.drawString(font, "▼", arrowX, arrowY, 0x5a3a1a, false);

        // ── Output item ───────────────────────────────────────────────────────
        int outY  = arrowY + font.lineHeight;
        int outX  = barX + SEG_W / 2 - SLOT_MINI / 2;

        ItemStack output = buildStack(recipe.getOutputItemId(), recipe.getOutputCount());

        gfx.fill(outX - 1, outY - 1, outX + SLOT_MINI + 1, outY + SLOT_MINI + 1, 0x88c8a800);
        gfx.fill(outX,     outY,     outX + SLOT_MINI,     outY + SLOT_MINI,     0x55000000);

        gfx.pose().pushPose();
        float scale = (float) SLOT_MINI / 20f;
        gfx.pose().translate(outX, outY, 0);
        gfx.pose().scale(scale, scale, 1f);
        gfx.renderItem(output, 2, 2);
        if (recipe.getOutputCount() > 1) {
            gfx.pose().translate(0, 0, 200);
            String countStr = String.valueOf(recipe.getOutputCount());
            gfx.drawString(font, countStr, 18 - font.width(countStr), 10, 0xFFFFFF, true);
        }
        gfx.pose().popPose();
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private static ResourceLocation loc(String path) {
        return ResourceLocation.fromNamespaceAndPath("msgwoft", path);
    }

    private static ResourceLocation slotTex(ChemistryStationRecipe.Color c) {
        return switch (c) {
            case RED    -> SLOT_RED;
            case GREEN  -> SLOT_GREEN;
            case BLUE   -> SLOT_BLUE;
            case PURPLE -> SLOT_PURPLE;
        };
    }

    private static ResourceLocation slotEmptyTex(ChemistryStationRecipe.Color c) {
        return switch (c) {
            case RED    -> SLOT_RED_EMPTY;
            case GREEN  -> SLOT_GREEN_EMPTY;
            case BLUE   -> SLOT_BLUE_EMPTY;
            case PURPLE -> SLOT_PURPLE_EMPTY;
        };
    }

    private static ResourceLocation segTex(ChemistryStationRecipe.Color c) {
        return switch (c) {
            case RED    -> SEG_RED;
            case GREEN  -> SEG_GREEN;
            case BLUE   -> SEG_BLUE;
            case PURPLE -> SEG_PURPLE;
        };
    }

    private static ItemStack buildStack(String itemId, int count) {
        if (itemId == null) return ItemStack.EMPTY;
        try {
            var item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(itemId));
            if (item == Items.AIR) return ItemStack.EMPTY;
            return new ItemStack(item, count);
        } catch (Exception e) {
            return ItemStack.EMPTY;
        }
    }
}