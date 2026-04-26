package org.mythic_goose.msgwoft.screen.block;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.mythic_goose.msgwoft.MSGWOFT;
import org.mythic_goose.msgwoft.block.entity.ChemistryStationBlockEntity;
import org.mythic_goose.msgwoft.client.gui.components.UniqueButton;
import org.mythic_goose.msgwoft.recipe.ChemistryStationRecipe;

import java.util.List;

public class ChemistryStationScreen extends AbstractContainerScreen<ChemistryStationMenu> {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
            MSGWOFT.MOD_ID, "textures/gui/chemistry_station/chemistry_station.png");

    private static final int GUI_WIDTH  = 256;
    private static final int GUI_HEIGHT = 256;

    private static final int BAR_X = 115;
    private static final int BAR_Y = 22;
    private static final int BAR_H = 84;
    private static final int MAX_SEGMENTS  = 12;
    private static final int SEG_H          = 6;
    private static final int SEG_GAP       = 1;

    private static final ResourceLocation SEG_RED    = ResourceLocation.fromNamespaceAndPath(MSGWOFT.MOD_ID, "textures/gui/chemistry_station/ingredients/red_chemical.png");
    private static final ResourceLocation SEG_GREEN  = ResourceLocation.fromNamespaceAndPath(MSGWOFT.MOD_ID, "textures/gui/chemistry_station/ingredients/green_chemical.png");
    private static final ResourceLocation SEG_BLUE   = ResourceLocation.fromNamespaceAndPath(MSGWOFT.MOD_ID, "textures/gui/chemistry_station/ingredients/blue_chemical.png");
    private static final ResourceLocation SEG_PURPLE = ResourceLocation.fromNamespaceAndPath(MSGWOFT.MOD_ID, "textures/gui/chemistry_station/ingredients/purple_chemical.png");

    private static final WidgetSprites RED_SPRITES = new WidgetSprites(
            ResourceLocation.fromNamespaceAndPath(MSGWOFT.MOD_ID, "button/red/button"),
            ResourceLocation.fromNamespaceAndPath(MSGWOFT.MOD_ID, "button/red/button_disabled"),
            ResourceLocation.fromNamespaceAndPath(MSGWOFT.MOD_ID, "button/red/button_highlighted")
    );
    private static final WidgetSprites GREEN_SPRITES = new WidgetSprites(
            ResourceLocation.fromNamespaceAndPath(MSGWOFT.MOD_ID, "button/green/button"),
            ResourceLocation.fromNamespaceAndPath(MSGWOFT.MOD_ID, "button/green/button_disabled"),
            ResourceLocation.fromNamespaceAndPath(MSGWOFT.MOD_ID, "button/green/button_highlighted")
    );
    private static final WidgetSprites BLUE_SPRITES = new WidgetSprites(
            ResourceLocation.fromNamespaceAndPath(MSGWOFT.MOD_ID, "button/blue/button"),
            ResourceLocation.fromNamespaceAndPath(MSGWOFT.MOD_ID, "button/blue/button_disabled"),
            ResourceLocation.fromNamespaceAndPath(MSGWOFT.MOD_ID, "button/blue/button_highlighted")
    );
    private static final WidgetSprites PURPLE_SPRITES = new WidgetSprites(
            ResourceLocation.fromNamespaceAndPath(MSGWOFT.MOD_ID, "button/purple/button"),
            ResourceLocation.fromNamespaceAndPath(MSGWOFT.MOD_ID, "button/purple/button_disabled"),
            ResourceLocation.fromNamespaceAndPath(MSGWOFT.MOD_ID, "button/purple/button_highlighted")
    );

    private static final WidgetSprites[] COLOR_SPRITES = { RED_SPRITES, GREEN_SPRITES, BLUE_SPRITES, PURPLE_SPRITES };

    private static final int ING_BTN_W = 14;
    private static final int ING_BTN_H = 14;
    private static final int ING_BTN_X = 227 - ING_BTN_W - 4;
    private static final int[] ING_BTN_Y = { 16, 54, 92, 130 };

    private static final int CRAFT_BTN_X = 103;
    private static final int CRAFT_BTN_Y = 145;
    private static final int CRAFT_BTN_W = 50;
    private static final int CRAFT_BTN_H = 14;

    private static final int STAT_X = 11;
    private static final int STAT_Y = 24;
    private static final int PROB_X = 11;
    private static final int PROB_Y = 110;

    private Button craftButton;

    // CHANGED: From Button[] to UniqueButton[]
    private final UniqueButton[] ingButtons = new UniqueButton[4];

    private static final ChemistryStationRecipe.Color[] COLORS = {
            ChemistryStationRecipe.Color.RED,
            ChemistryStationRecipe.Color.GREEN,
            ChemistryStationRecipe.Color.BLUE,
            ChemistryStationRecipe.Color.PURPLE
    };

    public ChemistryStationScreen(ChemistryStationMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth   = GUI_WIDTH;
        this.imageHeight  = GUI_HEIGHT;
        this.titleLabelX  = 8;
        this.titleLabelY  = 6;
        this.inventoryLabelX = 48;
        this.inventoryLabelY = 162;
    }

    @Override
    protected void init() {
        super.init();

        craftButton = Button.builder(Component.literal("Craft"), btn -> menu.clickCraftButton())
                .pos(leftPos + CRAFT_BTN_X, topPos + CRAFT_BTN_Y)
                .size(CRAFT_BTN_W, CRAFT_BTN_H)
                .build();
        addRenderableWidget(craftButton);

        for (int i = 0; i < 4; i++) {
            final int idx = i;
            ingButtons[i] = UniqueButton.builder(
                            Component.literal("+"),
                            btn -> menu.tryAddSegment(COLORS[idx]),
                            COLOR_SPRITES[idx])
                    .pos(leftPos + ING_BTN_X, topPos + ING_BTN_Y[idx])
                    .size(ING_BTN_W, ING_BTN_H)
                    .build();
            addRenderableWidget(ingButtons[i]);
        }
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        ChemistryStationBlockEntity be = menu.getBlockEntity();
        if (be.getItem(ChemistryStationBlockEntity.SLOT_PRIMARY).isEmpty()) {
            menu.clearBarQueue();
        }

        boolean hasPrimary = !be.getItem(ChemistryStationBlockEntity.SLOT_PRIMARY).isEmpty();
        int[] chemSlots = {
                ChemistryStationBlockEntity.SLOT_RED,
                ChemistryStationBlockEntity.SLOT_GREEN,
                ChemistryStationBlockEntity.SLOT_BLUE,
                ChemistryStationBlockEntity.SLOT_PURPLE
        };

        for (int i = 0; i < 4; i++) {
            final ChemistryStationRecipe.Color color = COLORS[i];
            long alreadyQueued = menu.getBarQueue().stream().filter(c -> c == color).count();
            int available = be.getItem(chemSlots[i]).getCount();
            ingButtons[i].active = hasPrimary
                    && available > 0
                    && alreadyQueued < available
                    && menu.getBarQueue().size() < MAX_SEGMENTS;
        }
        craftButton.active = hasPrimary && !menu.getBarQueue().isEmpty();
    }

    @Override
    public void onClose() {
        menu.clearBarQueue();
        super.onClose();
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        graphics.blit(TEXTURE, leftPos, topPos, 0, 0, GUI_WIDTH, GUI_HEIGHT);
        drawSegmentBar(graphics);
    }

    private void drawSegmentBar(GuiGraphics graphics) {
        List<ChemistryStationRecipe.Color> queue = menu.getBarQueue();
        int totalSegs = queue.size();
        if (totalSegs == 0) return;

        for (int i = 0; i < totalSegs; i++) {
            int fromBarBottom = i * (SEG_H + SEG_GAP);
            int yOffset = BAR_H - fromBarBottom - SEG_H;
            ResourceLocation tex = textureFor(queue.get(i));
            RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
            graphics.blit(tex,
                    leftPos + BAR_X,
                    topPos  + BAR_Y + yOffset,
                    0, 0,
                    26, SEG_H,
                    26, 6);
        }
    }

    private ResourceLocation textureFor(ChemistryStationRecipe.Color color) {
        return switch (color) {
            case RED    -> SEG_RED;
            case GREEN  -> SEG_GREEN;
            case BLUE   -> SEG_BLUE;
            case PURPLE -> SEG_PURPLE;
        };
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(font, title, titleLabelX, titleLabelY, 0x404040, false);
        graphics.drawString(font,
                Component.translatable("container.inventory"),
                inventoryLabelX, inventoryLabelY, 0x404040, false);

        ChemistryStationBlockEntity be = menu.getBlockEntity();
        if (!be.getItem(ChemistryStationBlockEntity.SLOT_PRIMARY).isEmpty()) {
            int col = 0xDDDDDD;
            graphics.drawString(font, String.format("Conc:  %.0f%%", be.getConcentration()   * 100f), STAT_X, STAT_Y, col, false);
            graphics.drawString(font, String.format("Crys:  %.0f%%", be.getCrystallization() * 100f), STAT_X, STAT_Y + 9, col, false);
            graphics.drawString(font, String.format("Qual:  %.0f%%", be.getQuality()         * 100f), STAT_X, STAT_Y + 18, col, false);
            graphics.drawString(font, String.format("Prob: %.0f%%",  be.getTotalProbability() * 100f), PROB_X, PROB_Y, 0x00FF44, false);
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics, mouseX, mouseY, partialTick);
        super.render(graphics, mouseX, mouseY, partialTick);
        renderTooltip(graphics, mouseX, mouseY);
    }
}