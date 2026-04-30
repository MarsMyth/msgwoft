package org.mythic_goose.msgwoft.screen.item;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.mythic_goose.msgwoft.recipe.ChemistryStationRecipe;

import java.util.List;

public class WrittenRecipeScreen extends AbstractContainerScreen<WrittenRecipeMenu> {

    // ── Textures ──────────────────────────────────────────────────────────────
    private static final ResourceLocation SCROLL_TEX =
            ResourceLocation.fromNamespaceAndPath("msgwoft", "textures/gui/written_recipe/recipe_gui.png");

    // ── Color slot textures ───────────────────────────────────────────────────
    private static final ResourceLocation SLOT_RED    = slotTex("red_slot");
    private static final ResourceLocation SLOT_GREEN  = slotTex("green_slot");
    private static final ResourceLocation SLOT_BLUE   = slotTex("blue_slot");
    private static final ResourceLocation SLOT_PURPLE = slotTex("purple_slot");

    private static final ResourceLocation SLOT_RED_EMPTY    = slotTex("red_slot_empty");
    private static final ResourceLocation SLOT_GREEN_EMPTY  = slotTex("green_slot_empty");
    private static final ResourceLocation SLOT_BLUE_EMPTY   = slotTex("blue_slot_empty");
    private static final ResourceLocation SLOT_PURPLE_EMPTY = slotTex("purple_slot_empty");

    private static final ResourceLocation SEG_RED    = segTex("red_chemical");
    private static final ResourceLocation SEG_GREEN  = segTex("green_chemical");
    private static final ResourceLocation SEG_BLUE   = segTex("blue_chemical");
    private static final ResourceLocation SEG_PURPLE = segTex("purple_chemical");
    private static final ResourceLocation SEG_EMPTY  = segTex("empty_segment");

    private static ResourceLocation slotTex(String name) {
        return ResourceLocation.fromNamespaceAndPath("msgwoft",
                "textures/gui/widget/slots/" + name + ".png");
    }

    private static ResourceLocation segTex(String name) {
        return ResourceLocation.fromNamespaceAndPath("msgwoft",
                "textures/gui/written_recipe/icons/" + name + ".png");
    }

    private static ResourceLocation slotTexture(ChemistryStationRecipe.Color color) {
        return switch (color) {
            case RED    -> SLOT_RED;
            case GREEN  -> SLOT_GREEN;
            case BLUE   -> SLOT_BLUE;
            case PURPLE -> SLOT_PURPLE;
        };
    }

    private static ResourceLocation slotEmptyTexture(ChemistryStationRecipe.Color color) {
        return switch (color) {
            case RED    -> SLOT_RED_EMPTY;
            case GREEN  -> SLOT_GREEN_EMPTY;
            case BLUE   -> SLOT_BLUE_EMPTY;
            case PURPLE -> SLOT_PURPLE_EMPTY;
        };
    }

    private static ResourceLocation segmentTexture(ChemistryStationRecipe.Color color) {
        return switch (color) {
            case RED    -> SEG_RED;
            case GREEN  -> SEG_GREEN;
            case BLUE   -> SEG_BLUE;
            case PURPLE -> SEG_PURPLE;
        };
    }

    // ── GUI dimensions ────────────────────────────────────────────────────────
    private static final int GUI_W       = 178;
    private static final int GUI_H       = 220;
    private static final int CONTENT_X   = 20;
    private static final int CONTENT_Y   = 30;
    private static final int CONTENT_W   = 136;
    private static final int BAR_SEGMENTS = 12;
    private static final int SEG_W       = 26;
    private static final int SEG_H       = 6;
    private static final int SLOT_TEX_SIZE = 20;
    private static final int SLOT_SIZE   = 16;

    // ── State ─────────────────────────────────────────────────────────────────
    private ChemistryStationRecipe recipe;

    // ── Constructor ───────────────────────────────────────────────────────────
    public WrittenRecipeScreen(WrittenRecipeMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
        this.imageWidth      = GUI_W;
        this.imageHeight     = GUI_H;
        this.inventoryLabelY = GUI_H + 999;
        this.titleLabelY     = 10;
    }

    @Override
    protected void init() {
        super.init();
        recipe = ((WrittenRecipeMenu) menu).getRecipe();
    }

    // ── Key handling ──────────────────────────────────────────────────────────

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        // Intercept Q (drop key) — close screen first, then drop so item isn't voided
        if (keyCode == 81 && minecraft != null && minecraft.player != null) {
            int slot = ((WrittenRecipeMenu) menu).getSlotIndex();
            minecraft.player.closeContainer();
            if (slot >= 0) {
                minecraft.gameMode.handleInventoryMouseClick(
                        minecraft.player.inventoryMenu.containerId,
                        slot, 0, ClickType.THROW, minecraft.player
                );
            }
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    // ── Rendering ─────────────────────────────────────────────────────────────

    @Override
    protected void renderBg(GuiGraphics gfx, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        gfx.blit(SCROLL_TEX, leftPos, topPos, 0, 0, GUI_W, GUI_H, 256, 256);
    }

    @Override
    public void render(GuiGraphics gfx, int mouseX, int mouseY, float partialTick) {
        super.render(gfx, mouseX, mouseY, partialTick);
        renderTooltip(gfx, mouseX, mouseY);
        renderContent(gfx, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics gfx, int mouseX, int mouseY) {
        Component title;
        if (recipe != null) {
            ItemStack output = buildStack(recipe.getOutputItemId(), recipe.getOutputCount());
            title = output.isEmpty()
                    ? Component.translatable("item.msgwoft.written_recipe")
                    : output.getHoverName().copy();
        } else {
            title = Component.translatable("item.msgwoft.written_recipe");
        }
        int titleX = GUI_W / 2 - font.width(title) / 2;
        gfx.drawString(font, title, titleX, titleLabelY, 0x5a3a1a, false);
    }

    // ── Content ───────────────────────────────────────────────────────────────

    private void renderContent(GuiGraphics gfx, int mouseX, int mouseY) {
        int baseX = leftPos + CONTENT_X;
        int baseY = topPos  + CONTENT_Y;
        int ink   = 0x5a3a1a;

        if (recipe == null) {
            gfx.drawString(font, Component.literal("No recipe assigned."),
                    baseX, baseY, ink, false);
            return;
        }

        List<ChemistryStationRecipe.Ingredient> ings = recipe.getIngredients();
        ChemistryStationRecipe.Color[] colors = ChemistryStationRecipe.Color.values();

        int barTotalH  = BAR_SEGMENTS * SEG_H;
        int blockW     = SEG_W + 10 + SLOT_TEX_SIZE;
        int barX       = baseX + (CONTENT_W - blockW) / 2;
        int barTopY    = baseY;
        int barBottomY = barTopY + barTotalH;

        // Bar background
        gfx.fill(barX - 1, barTopY - 1, barX + SEG_W + 1, barBottomY + 1, 0x55000000);

        // Bar segments
        for (int seg = 0; seg < BAR_SEGMENTS; seg++) {
            int segY = barBottomY - (seg + 1) * SEG_H;
            ResourceLocation segTex = seg < ings.size()
                    ? segmentTexture(ings.get(seg).color()) : SEG_EMPTY;
            RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
            gfx.blit(segTex, barX, segY, 0, 0, SEG_W, SEG_H, SEG_W, SEG_H);
        }

        // Ingredient slots
        int slotColumnX = barX + SEG_W + 10;
        int slotGap     = 24;

        for (int i = 0; i < colors.length; i++) {
            ChemistryStationRecipe.Color color = colors[i];
            int    count  = recipe.countOf(color);
            String itemId = recipe.itemIdFor(color);

            int texY  = barTopY + i * slotGap;
            int itemX = slotColumnX + (SLOT_TEX_SIZE - SLOT_SIZE) / 2;
            int itemY = texY        + (SLOT_TEX_SIZE - SLOT_SIZE) / 2;

            RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
            if (count > 0 && itemId != null) {
                gfx.blit(slotTexture(color), slotColumnX, texY, 0, 0,
                        SLOT_TEX_SIZE, SLOT_TEX_SIZE, SLOT_TEX_SIZE, SLOT_TEX_SIZE);
                ItemStack stack = buildStack(itemId, count);
                gfx.renderItem(stack, itemX, itemY);
                gfx.renderItemDecorations(font, stack, itemX, itemY);
                if (mouseX >= itemX && mouseX < itemX + SLOT_SIZE
                        && mouseY >= itemY && mouseY < itemY + SLOT_SIZE) {
                    gfx.renderTooltip(font, stack, mouseX, mouseY);
                }
            } else {
                gfx.blit(slotEmptyTexture(color), slotColumnX, texY, 0, 0,
                        SLOT_TEX_SIZE, SLOT_TEX_SIZE, SLOT_TEX_SIZE, SLOT_TEX_SIZE);
            }
        }

        // Arrow
        int sectionCenterX = barX + SEG_W / 2;
        int arrowY = barBottomY + 5;
        gfx.drawString(font, Component.literal("▼"),
                sectionCenterX - font.width("▼") / 2, arrowY, ink, false);

        // Output
        int outY     = arrowY + font.lineHeight + 4;
        int outSlotX = sectionCenterX - SLOT_SIZE / 2;
        ItemStack output = buildStack(recipe.getOutputItemId(), recipe.getOutputCount());

        gfx.fill(outSlotX - 2, outY - 2, outSlotX + SLOT_SIZE + 2, outY + SLOT_SIZE + 2, 0x88c8a800);
        gfx.fill(outSlotX - 1, outY - 1, outSlotX + SLOT_SIZE + 1, outY + SLOT_SIZE + 1, 0x55000000);

        gfx.renderItem(output, outSlotX, outY);
        gfx.renderItemDecorations(font, output, outSlotX, outY);

        if (mouseX >= outSlotX && mouseX < outSlotX + SLOT_SIZE
                && mouseY >= outY && mouseY < outY + SLOT_SIZE) {
            gfx.renderTooltip(font, output, mouseX, mouseY);
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

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