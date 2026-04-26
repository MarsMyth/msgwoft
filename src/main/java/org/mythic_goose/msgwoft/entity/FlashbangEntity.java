package org.mythic_goose.msgwoft.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.NotNull;
import org.mythic_goose.msgwoft.init.ModEntities;
import org.mythic_goose.msgwoft.init.ModItems;
import org.mythic_goose.msgwoft.init.ModSounds;
import org.mythic_goose.msgwoft.network.FlashbangPacket;
import java.util.HashSet;
import java.util.Set;

import java.util.List;

public class FlashbangEntity extends ThrowableProjectile implements ItemSupplier {

    @Override
    public @NotNull ItemStack getItem() {
        return new ItemStack(ModItems.FLASHBANG); // your flashbang item
    }

    // Synced so client knows when to play the flash effect
    private static final EntityDataAccessor<Boolean> HAS_DETONATED =
            SynchedEntityData.defineId(FlashbangEntity.class, EntityDataSerializers.BOOLEAN);

    /** Ticks until detonation (2 seconds = 40 ticks). Counts DOWN. */
    private int fuseTicks = 40;

    /** Whether the grenade has already hit a surface (fuse starts ticking after that). */
    private boolean hasLanded = false;

    /** Radius in blocks that the flash affects. */
    private static final double FLASH_RADIUS = 10.0;

    public FlashbangEntity(EntityType<? extends FlashbangEntity> type, Level level) {
        super(type, level);
    }

    public FlashbangEntity(Level level, LivingEntity thrower) {
        super(ModEntities.FLASHBANG, thrower, level);
    }

    // ── Data ──────────────────────────────────────────────────────────────────

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(HAS_DETONATED, false);
    }

    public boolean hasDetonated() {
        return this.entityData.get(HAS_DETONATED);
    }

    // ── Tick ──────────────────────────────────────────────────────────────────

    @Override
    public void tick() {
        if (!level().isClientSide) {
            fuseTicks--;
            if (fuseTicks <= 0 && !hasDetonated()) {
                detonate();
            }
        }
        super.tick();
    }

    // ── Collision callbacks ───────────────────────────────────────────────────

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if (!hasLanded) {
            hasLanded = true;
            BlockPos pos = result.getBlockPos().relative(result.getDirection());
            this.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
            this.setDeltaMovement(Vec3.ZERO);
            this.setNoGravity(true);

            // Play land sound server-side (broadcasts to all nearby clients)
            if (!level().isClientSide) {
                level().playSound(null, getX(), getY(), getZ(),
                        ModSounds.FB_LAND, SoundSource.NEUTRAL, 1.0F, 1.0F);
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (!hasLanded) {
            hasLanded = true;
            this.setDeltaMovement(Vec3.ZERO);
        }
    }

    // ── Detonation ────────────────────────────────────────────────────────────

    private static final double PERMANENT_BLIND_RADIUS = 3.0; // blocks, adjust to taste

    private void detonate() {
        this.entityData.set(HAS_DETONATED, true);

        List<LivingEntity> nearby = level().getEntitiesOfClass(
                LivingEntity.class,
                new AABB(blockPosition()).inflate(FLASH_RADIUS)
        );

        // Track who already got a packet so we don't double-send
        Set<ServerPlayer> alreadySent = new HashSet<>();

        for (LivingEntity entity : nearby) {
            double dist = entity.distanceTo(this);
            if (dist > FLASH_RADIUS) continue;

            if (hasLineOfSight(entity)) {
                double fraction = 1.0 - (dist / FLASH_RADIUS);
                int slowTicks = (int) Mth.lerp(fraction, 20, 80);

                if (dist <= PERMANENT_BLIND_RADIUS) {
                    entity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, Integer.MAX_VALUE, 0, false, false, false));
                } else {
                    int blindTicks = (int) Mth.lerp(fraction, 20, 140);
                    entity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, blindTicks, 0, false, false));
                }

                entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, slowTicks, 1, false, false));

                if (entity instanceof ServerPlayer serverPlayer) {
                    FlashbangPacket.send(serverPlayer, (float) fraction, this.position());
                    alreadySent.add(serverPlayer);
                }
            }
        }

        // Send light-only packet (intensity = 0) to all nearby players who weren't already sent one
        // They'll see the light but get no screen flash or blindness
        List<ServerPlayer> allNearbyPlayers = level().getEntitiesOfClass(
                ServerPlayer.class,
                new AABB(blockPosition()).inflate(32.0) // wider radius for the light
        );
        for (ServerPlayer player : allNearbyPlayers) {
            if (!alreadySent.contains(player)) {
                FlashbangPacket.send(player, 0.0f, this.position()); // 0 intensity = light only, no flash
            }
        }
        // Explosion sound — heard by everyone in range regardless of LOS
        level().playSound(null, getX(), getY(), getZ(),
                ModSounds.FB_EXPLODE, SoundSource.NEUTRAL, 4.0F, 1.0F);

        level().levelEvent(null, 1023, blockPosition(), 0);
        this.discard();
    }

    private boolean hasLineOfSight(LivingEntity entity) {
        Vec3 start = this.position();
        Vec3 end = entity.getEyePosition();

        BlockHitResult result = level().clip(new ClipContext(
                start, end,
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                this
        ));

        // If the ray hit a block before reaching the target, no line of sight
        return result.getType() == HitResult.Type.MISS;
    }

    // ── Gravity ───────────────────────────────────────────────────────────────

    @Override
    protected double getDefaultGravity() {
        // Lower gravity = flatter throw, feels like a real grenade arc
        return 0.03;
    }

    // ── NBT ───────────────────────────────────────────────────────────────────

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("FuseTicks", fuseTicks);
        tag.putBoolean("HasLanded", hasLanded);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        fuseTicks = tag.getInt("FuseTicks");
        hasLanded = tag.getBoolean("HasLanded");
    }
}