package org.mythic_goose.msgwoft.client.gui.components;

import java.util.function.Supplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class UniqueButton extends UniqueAbstractButton {
    public static final int SMALL_WIDTH = 120;
    public static final int DEFAULT_WIDTH = 150;
    public static final int BIG_WIDTH = 200;
    public static final int DEFAULT_HEIGHT = 20;
    public static final int DEFAULT_SPACING = 8;
    protected static final UniqueButton.CreateNarration DEFAULT_NARRATION = (supplier) -> (MutableComponent)supplier.get();
    protected final UniqueButton.OnPress onPress;
    protected final UniqueButton.CreateNarration createNarration;

    public final WidgetSprites widgetSprites;

    public static UniqueButton.Builder builder(Component component, UniqueButton.OnPress onPress, WidgetSprites widgetSprites) {
        return new UniqueButton.Builder(component, onPress, widgetSprites);
    }

    protected UniqueButton(int i, int j, int k, int l, Component component, UniqueButton.OnPress onPress, UniqueButton.CreateNarration createNarration, WidgetSprites widgetSprites) {
        super(i, j, k, l, component, widgetSprites);
        this.onPress = onPress;
        this.createNarration = createNarration;
        this.widgetSprites = widgetSprites;
    }

    @Override
    public void onPress() {
        this.onPress.onPress(this);
    }

    protected @NotNull MutableComponent createNarrationMessage() {
        return this.createNarration.createNarrationMessage(super::createNarrationMessage);
    }

    @Override
    public void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        this.defaultButtonNarrationText(narrationElementOutput);
    }

    @Environment(EnvType.CLIENT)
    public static class Builder {
        private final Component message;
        private final UniqueButton.OnPress onPress;
        private final WidgetSprites widgetSprites; // Added field
        @Nullable
        private Tooltip tooltip;
        private int x;
        private int y;
        private int width = 150;
        private int height = 20;
        private UniqueButton.CreateNarration createNarration;

        // Updated constructor to accept WidgetSprites
        public Builder(Component component, UniqueButton.OnPress onPress, WidgetSprites widgetSprites) {
            this.createNarration = UniqueButton.DEFAULT_NARRATION;
            this.message = component;
            this.onPress = onPress;
            this.widgetSprites = widgetSprites;
        }

        public UniqueButton.Builder pos(int i, int j) {
            this.x = i;
            this.y = j;
            return this;
        }

        public UniqueButton.Builder width(int i) {
            this.width = i;
            return this;
        }

        public UniqueButton.Builder size(int i, int j) {
            this.width = i;
            this.height = j;
            return this;
        }

        public UniqueButton.Builder bounds(int i, int j, int k, int l) {
            return this.pos(i, j).size(k, l);
        }

        public UniqueButton.Builder tooltip(@Nullable Tooltip tooltip) {
            this.tooltip = tooltip;
            return this;
        }

        public UniqueButton.Builder createNarration(UniqueButton.CreateNarration createNarration) {
            this.createNarration = createNarration;
            return this;
        }

        // Changed return type from Button to UniqueButton
        public UniqueButton build() {
            UniqueButton button = new UniqueButton(this.x, this.y, this.width, this.height, this.message, this.onPress, this.createNarration, this.widgetSprites);
            button.setTooltip(this.tooltip);
            return button;
        }
    }

    @Environment(EnvType.CLIENT)
    public interface CreateNarration {
        MutableComponent createNarrationMessage(Supplier<MutableComponent> supplier);
    }

    @Environment(EnvType.CLIENT)
    public interface OnPress {
        void onPress(UniqueButton button);
    }
}