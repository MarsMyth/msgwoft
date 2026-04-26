package org.mythic_goose.msgwoft.creativetab;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.List;

public record SectionTextured(
        String id,
        Component title,
        ResourceLocation texture,
        int textColor,
        List<Item> items
) implements Section {

    /** Builds the texture path automatically: [modId]:textures/gui/tab_overlay/[id].png */
    public static SectionTextured of(String modId, String id, Component title, int textColor, List<Item> items) {
        return new SectionTextured(
                id,
                title,
                ResourceLocation.fromNamespaceAndPath(modId, "textures/gui/tab_overlay/" + id + ".png"),
                textColor,
                items
        );
    }
}