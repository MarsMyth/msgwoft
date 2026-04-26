package org.mythic_goose.msgwoft.item;

import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.mythic_goose.msgwoft.entity.FlashbangEntity;
import org.mythic_goose.msgwoft.init.ModSounds;

public class FlashbangItem extends Item {

    public FlashbangItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            FlashbangEntity flashbang = new FlashbangEntity(level, player);
            flashbang.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 2.5F, 0.5F);
            level.addFreshEntity(flashbang);

            // Play a throw/pin-pull sound — reuse land or add a fb.throw sound
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    ModSounds.FB_LAND, SoundSource.PLAYERS, 1.0F, 1.2F);

            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }
}