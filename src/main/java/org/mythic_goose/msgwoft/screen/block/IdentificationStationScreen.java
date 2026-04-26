package org.mythic_goose.msgwoft.screen.block;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.mythic_goose.msgwoft.MSGWOFT;

public class IdentificationStationScreen extends AbstractContainerScreen<IdentificationStationMenu> {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
            MSGWOFT.MOD_ID, "textures/gui/identification_table/identification_table.png"
    );
    private static final ResourceLocation BULB = ResourceLocation.fromNamespaceAndPath(
            MSGWOFT.MOD_ID, "textures/gui/widget/progress/bulb_progress.png"
    );

    private static final int BULB_W = 9;
    private static final int BULB_H = 13;

    public IdentificationStationScreen(IdentificationStationMenu menu,
                                       Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        // Hide both title labels like Kaupenjoe does
        this.titleLabelY     = 1000;
        this.inventoryLabelY = 1000;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

        int x = (this.width  - this.imageWidth)  / 2;
        int y = (this.height - this.imageHeight) / 2;

        // Draw main background
        graphics.blit(TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight, 256, 256);

        renderProgressBulb(graphics, x, y);
    }

    private void renderProgressBulb(GuiGraphics graphics, int x, int y) {
        if (menu.isCrafting()) {
            int filled  = menu.getScaledBulbProgress();
            int yOffset = BULB_H - filled;
            graphics.blit(BULB,
                    x + 83,
                    y + 21 + yOffset,
                    0, yOffset,
                    BULB_W, filled,
                    BULB_W, BULB_H
            );
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics, mouseX, mouseY, partialTick);
        super.render(graphics, mouseX, mouseY, partialTick);
        renderTooltip(graphics, mouseX, mouseY);
    }
}