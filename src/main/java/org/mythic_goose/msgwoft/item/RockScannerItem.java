package org.mythic_goose.msgwoft.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import org.mythic_goose.msgwoft.init.ModItems;
import org.mythic_goose.msgwoft.util.ModTags;

import java.util.Random;

public class RockScannerItem extends Item {

    private static final String SCANNED_KEY = "ScannedPositions";
    private static final Random RANDOM = new Random();

    public RockScannerItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        Level level = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();
        if (level.isClientSide()) return InteractionResult.SUCCESS;
        if (!level.getBlockState(pos).is(ModTags.Blocks.VALID_SCANNER)) {
            ctx.getPlayer().displayClientMessage(
                    Component.literal("§cThis block cannot be scanned."), true
            );
            return InteractionResult.FAIL;
        }

        ItemStack scanner = ctx.getItemInHand();
        long packed = pos.asLong();

        // Read existing custom data, or start fresh
        CustomData existing = scanner.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        CompoundTag tag = existing.copyTag();

        ListTag scanned = tag.getList(SCANNED_KEY, 4); // 4 = LongTag type

        // Check for duplicate
        for (int i = 0; i < scanned.size(); i++) {
            if (((LongTag) scanned.get(i)).getAsLong() == packed) {
                ctx.getPlayer().displayClientMessage(
                        Component.literal("§cThis block has already been scanned."), true
                );
                return InteractionResult.FAIL;
            }
        }

        // Record position
        scanned.add(LongTag.valueOf(packed));
        tag.put(SCANNED_KEY, scanned);
        scanner.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));

        // Generate DataSpecimen
        ItemStack specimen = new ItemStack(ModItems.DATA_SPECIMEN);
        CompoundTag specimenTag = new CompoundTag();

        if (!level.getBlockState(pos).is(Blocks.BEDROCK)) {
            specimenTag.putFloat("Concentration",   rollStat());
            specimenTag.putFloat("Crystallization", rollStat());
            specimenTag.putFloat("Quality",         rollStat());
        } else {
            specimenTag.putFloat("Concentration",   forcePerfect());
            specimenTag.putFloat("Crystallization", forcePerfect());
            specimenTag.putFloat("Quality",         forcePerfect());
        }

// Store the block's registry name so the specimen knows its source
        specimenTag.putString("SourceBlock", level.getBlockState(pos).getBlock().getDescriptionId());
        specimen.set(DataComponents.CUSTOM_DATA, CustomData.of(specimenTag));

        // Drop at player's feet
        ItemEntity entity = new ItemEntity(level,
                ctx.getPlayer().getX(),
                ctx.getPlayer().getY(),
                ctx.getPlayer().getZ(),
                specimen);
        level.addFreshEntity(entity);

        ctx.getPlayer().displayClientMessage(
                Component.literal("§aScan complete. Data specimen collected."), true
        );
        return InteractionResult.SUCCESS;
    }

    private static float rollStat() {
        // Average of several random rolls — clusters results toward the middle (30–70% range)
        // via the central limit theorem. More rolls = tighter bell curve.
        double sum = 0;
        for (int i = 0; i < 5; i++) {
            sum += RANDOM.nextDouble();
        }
        double mid = sum / 5.0; // 0.0–1.0, strongly centered around 0.5

        // Stretch slightly so the full 0–100% range is still reachable, just very rare
        double stretched = Math.pow(mid, 0.85);

        return (float) Math.min(1.0, stretched);
    }

    private static float forcePerfect() {
        return 1.0f;
    }
}