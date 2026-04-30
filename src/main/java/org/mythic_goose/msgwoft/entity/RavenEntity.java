package org.mythic_goose.msgwoft.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mythic_goose.msgwoft.entity.goal.RavenDeliverBundleGoal;
import org.mythic_goose.msgwoft.entity.goal.RavenFollowOwnerGoal;
import org.mythic_goose.msgwoft.init.ModEntities;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;

public class RavenEntity extends TamableAnimal implements GeoAnimatable {

    private static final EntityDataAccessor<Optional<UUID>> RECEIVER_UUID =
            SynchedEntityData.defineId(RavenEntity.class, EntityDataSerializers.OPTIONAL_UUID);
    public static final EntityDataAccessor<String> TYPE =
            SynchedEntityData.defineId(RavenEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Boolean> SITTING =
            SynchedEntityData.defineId(RavenEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> GOING_TO_RECEIVER =
            SynchedEntityData.defineId(RavenEntity.class, EntityDataSerializers.BOOLEAN);

    private final AnimatableInstanceCache animatableCache = GeckoLibUtil.createInstanceCache(this);

    public float maxWingDeviation;
    public float prevMaxWingDeviation;
    private float wingFlapAccumulator = 1.0f;

    public RavenEntity(EntityType<? extends TamableAnimal> type, Level level) {
        super(type, level);
        this.moveControl = new FlyingMoveControl(this, 90, false);
        if (!level.isClientSide) {
            this.setRavenType(Type.getRandomType(this.random));
        }
    }

    // -------------------------------------------------------------------------
    // Goals & Attributes
    // -------------------------------------------------------------------------

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(1, new FloatGoal(this));
        goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
        goalSelector.addGoal(3, new RavenDeliverBundleGoal<>(this, 1.0, 6.0f, 128.0f, false));
        goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0, true));
        goalSelector.addGoal(4, new RavenFollowOwnerGoal(this, 1.0, 10.0f, 2.0f));
        goalSelector.addGoal(5, new BreedGoal(this, 1.0));
        goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0));
        goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0f));
        goalSelector.addGoal(7, new RandomLookAroundGoal(this));

        targetSelector.addGoal(0, new OwnerHurtByTargetGoal(this));
        targetSelector.addGoal(1, new OwnerHurtTargetGoal(this));
        targetSelector.addGoal(2, new HurtByTargetGoal(this));
    }

    public static AttributeSupplier.Builder createRavenAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0)
                .add(Attributes.ATTACK_DAMAGE, 2.0)
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.FLYING_SPEED, 0.7);
    }

    // -------------------------------------------------------------------------
    // Synched Entity Data
    // -------------------------------------------------------------------------

    // In 1.21.1, defineSynchedData takes a SynchedEntityData.Builder parameter
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SITTING, false);
        builder.define(RECEIVER_UUID, Optional.empty());
        builder.define(GOING_TO_RECEIVER, false);
        builder.define(TYPE, Type.DARK.toString());
    }

    // -------------------------------------------------------------------------
    // NBT
    // -------------------------------------------------------------------------

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putBoolean("sitting", isSitting());
        nbt.putBoolean("going", this.entityData.get(GOING_TO_RECEIVER));
        nbt.putString("Type", this.getRavenType().toString());
        UUID receiverUuid = this.getReceiverUuid();
        if (receiverUuid != null) {
            nbt.putUUID("Receiver", receiverUuid);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        setSitting(nbt.getBoolean("sitting"));
        this.entityData.set(GOING_TO_RECEIVER, nbt.getBoolean("goin"));

        if (nbt.contains("Type")) {
            try {
                this.setRavenType(Type.valueOf(nbt.getString("Type")));
            } catch (IllegalArgumentException e) {
                this.setRavenType(Type.DARK);
            }
        }

        if (nbt.hasUUID("Receiver")) {
            setReceiverUuid(nbt.getUUID("Receiver"));
        }
        // Note: ServerConfigHandler lookup removed - name->UUID lookup is not
        // available here. If you need this, add your own lookup implementation.
    }

    // -------------------------------------------------------------------------
    // Navigation
    // -------------------------------------------------------------------------

    @Override
    protected PathNavigation createNavigation(Level level) {
        FlyingPathNavigation nav = new FlyingPathNavigation(this, level);
        nav.setCanOpenDoors(false);
        nav.setCanFloat(true);
        nav.setCanPassDoors(true);
        return nav;
    }

    // -------------------------------------------------------------------------
    // Ticking
    // -------------------------------------------------------------------------

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        updateBundleDelivery();
        updateThreeEyedVariant();
    }

    private void updateBundleDelivery() {
        ItemStack held = this.getItemBySlot(EquipmentSlot.MAINHAND);
        // In 1.21.1, hasCustomHoverName() is replaced by checking the CUSTOM_NAME component
        if (!held.isEmpty() && held.get(DataComponents.CUSTOM_NAME) != null) {
            String recipientName = held.getHoverName().getString();
            Player recipient = getServer().getPlayerList().getPlayerByName(recipientName);
            if (recipient != null && recipient.getUUID() != null
                    && !held.getHoverName().toString().contains("Mouthpiece")) {
                this.setReceiverUuid(recipient.getUUID());
                return;
            }
        }
        this.setReceiverUuid(null);
        this.entityData.set(GOING_TO_RECEIVER, false);
    }

    private void updateThreeEyedVariant() {
        if (!this.hasCustomName()) return;
        String name = this.getCustomName().getString();
        if (name.equalsIgnoreCase("three_eyed")
                || name.equalsIgnoreCase("three_eyed_raven")
                || name.equalsIgnoreCase("three eyed")
                || name.equalsIgnoreCase("three eyed raven")
                || name.equalsIgnoreCase("three-eyed raven")) {
            this.setRavenType(Type.THREE_EYED);
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        flapWings();
    }

    private void flapWings() {
        this.prevMaxWingDeviation = this.maxWingDeviation;
        this.maxWingDeviation += (this.onGround() || this.isPassenger() ? -1 : 4) * 0.3f;
        this.maxWingDeviation = Mth.clamp(this.maxWingDeviation, 0.0f, 1.0f);
    }

    // -------------------------------------------------------------------------
    // Interaction
    // -------------------------------------------------------------------------

    @Override
    public @NotNull InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        // Give named bundle to raven
        // In 1.21.1, hasCustomHoverName() -> check CUSTOM_NAME component
        if (stack.is(Items.BUNDLE) && stack.get(DataComponents.CUSTOM_NAME) != null) {
            if (!this.level().isClientSide) {
                this.setItemSlot(EquipmentSlot.MAINHAND, stack.copy());
                player.setItemInHand(hand, ItemStack.EMPTY);
                updateBundleDelivery();
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        // Take bundle back from raven
        if (stack.isEmpty()
                && this.getMainHandItem().is(Items.BUNDLE)
                && !player.isShiftKeyDown()) {
            if (!this.level().isClientSide) {
                player.setItemInHand(hand, this.getMainHandItem());
                this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        // Toggle sit for tamed owner
        if (this.onGround() && this.isTame() && this.isOwnedBy(player) && stack.isEmpty()) {
            if (!this.level().isClientSide) {
                this.setSitting(!this.entityData.get(SITTING));
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        // Taming
        if (!isTame() && isFood(stack)) {
            if (!level().isClientSide) {
                // In 1.21.1, eat() signature is eat(Player, ItemStack)
                eat(level(), stack);
                if (random.nextInt(4) == 0) {
                    tame(player);
                    setOrderedToSit(true);
                    setTarget(null);
                    navigation.stop();
                    level().broadcastEntityEvent(this, (byte) 7);
                } else {
                    level().broadcastEntityEvent(this, (byte) 6);
                }
            }
            return InteractionResult.sidedSuccess(level().isClientSide);
        }

        // Heal tamed raven
        if (isTame() && isFood(stack) && getHealth() < getMaxHealth()) {
            if (!level().isClientSide) {
                eat(level(), stack);
                heal(4.0f);
            }
            return InteractionResult.sidedSuccess(level().isClientSide);
        }

        return super.mobInteract(player, hand);
    }

    // -------------------------------------------------------------------------
    // Combat & Damage
    // -------------------------------------------------------------------------

    // canAttackWithOwner no longer exists as an @Override in 1.21.1 TamableAnimal.
    // Replicate the logic by overriding wantsToAttack instead.
    @Override
    public boolean wantsToAttack(LivingEntity target, LivingEntity owner) {
        if (target instanceof TamableAnimal t && t.isTame()) return false;
        if (target instanceof AbstractHorse h && h.isTamed()) return false;
        if (target instanceof Player player
                && owner instanceof Player playerOwner
                && !playerOwner.canHarmPlayer(player)) {
            return false;
        }
        return !(target instanceof Creeper) && !(target instanceof Ghast);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (isInvulnerableTo(source)) return false;
        spawnFeatherParticles(3);
        setOrderedToSit(false);
        if (source.getEntity() != null
                && !(source.getEntity() instanceof Player)
                && !(source.getEntity() instanceof AbstractArrow)) {
            amount = (amount + 1) / 2f;
        }
        return super.hurt(source, amount);
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float damageMultiplier, DamageSource source) {
        return false;
    }

    @Override
    protected void checkFallDamage(double heightDifference, boolean onGround, BlockState state, BlockPos pos) {
        // Ravens don't take fall damage
    }

    // -------------------------------------------------------------------------
    // Equipment
    // -------------------------------------------------------------------------

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        this.setDropChance(EquipmentSlot.MAINHAND, 1.0f);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        // In 1.21.1, food info moved from Item to ItemStack via FoodProperties component
        FoodProperties food = stack.get(DataComponents.FOOD);
        return food != null && stack.is(ItemTags.MEAT);
    }

    // -------------------------------------------------------------------------
    // Particles & Effects
    // -------------------------------------------------------------------------

    public void spawnFeatherParticles(int count) {
        if (!level().isClientSide) return;
        float height = this.getBbHeight();
        if (height * 100 < 100) height = 1.0f;
        else height += 0.5f;

        for (int i = 0; i <= count; i++) {
            double randomHeight = (double) this.random.nextInt((int) (height * 10)) / 10.0;
            level().addParticle(
                    switch (this.getRavenType()) {
                        // TODO: Replace with your actual particle types from your mod's registry
                        // e.g. ModParticles.RAVEN_FEATHER.get() etc.
                        case DARK, THREE_EYED -> net.minecraft.core.particles.ParticleTypes.SMOKE;
                        case ALBINO -> net.minecraft.core.particles.ParticleTypes.WHITE_SMOKE;
                        case SEA_GREEN -> net.minecraft.core.particles.ParticleTypes.HAPPY_VILLAGER;
                    },
                    this.getX(), this.getY() + 0.2 + randomHeight, this.getZ(),
                    0, 0, 0
            );
        }
    }

    // -------------------------------------------------------------------------
    // Wings
    // -------------------------------------------------------------------------

    @Override
    protected void onFlap() {
        if (!isOrderedToSit()) {
            playSound(SoundEvents.PARROT_FLY, 0.15f, 1.0f);
        }
        this.wingFlapAccumulator = this.flyDist + this.maxWingDeviation / 2.0f;
    }

    @Override
    protected boolean isFlapping() {
        return this.flyDist > this.wingFlapAccumulator;
    }

    // -------------------------------------------------------------------------
    // Sounds
    // -------------------------------------------------------------------------

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        playSound(SoundEvents.PARROT_STEP, 0.15f, 1.0f);
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        // TODO: Replace with your actual sound: e.g. ModSounds.ENTITY_RAVEN_CAW.get()
        return SoundEvents.PARROT_AMBIENT;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        // TODO: Replace with your actual sound
        return SoundEvents.PARROT_HURT;
    }

    // -------------------------------------------------------------------------
    // Misc
    // -------------------------------------------------------------------------

    @Override
    public boolean isPushable() {
        return true;
    }

    public boolean isSitting() {
        return this.entityData.get(SITTING);
    }

    public void setSitting(boolean sitting) {
        this.entityData.set(SITTING, sitting);
        this.setOrderedToSit(sitting);
    }

    @Nullable
    public UUID getReceiverUuid() {
        return this.entityData.get(RECEIVER_UUID).orElse(null);
    }

    public void setReceiverUuid(@Nullable UUID uuid) {
        this.entityData.set(RECEIVER_UUID, Optional.ofNullable(uuid));
    }

    public Type getRavenType() {
        return Type.valueOf(this.entityData.get(TYPE));
    }

    public void setRavenType(Type type) {
        this.entityData.set(TYPE, type.toString());
    }

    // -------------------------------------------------------------------------
    // Breeding
    // -------------------------------------------------------------------------

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob other) {
        RavenEntity child = ModEntities.RAVEN.create(level);
        if (child == null) return null;

        // In 1.21.1, finalizeSpawn takes 4 args (no extra null at end)
        child.finalizeSpawn(level, level.getCurrentDifficultyAt(blockPosition()), MobSpawnType.BREEDING, null);

        UUID ownerUuid = getOwnerUUID();
        if (ownerUuid != null) {
            child.setOwnerUUID(ownerUuid);
            // In 1.21.1, setTame requires two booleans: tamed, and broadcastEvent
            child.setTame(true, true);
        }

        if (other instanceof RavenEntity otherRaven && random.nextFloat() < 0.95f) {
            child.entityData.set(TYPE, random.nextBoolean()
                    ? this.entityData.get(TYPE)
                    : otherRaven.entityData.get(TYPE));
        }

        return child;
    }

    // -------------------------------------------------------------------------
    // GeckoLib Animations
    // -------------------------------------------------------------------------

    private <E extends GeoAnimatable> PlayState predicate(AnimationState<E> state) {
        AnimationController<?> controller = state.getController();
        boolean isSitting = this.entityData.get(SITTING);

        if (isSitting) {
            controller.setAnimation(RawAnimation.begin().thenLoop("sitIdle"));
        } else if (!this.onGround()) {
            boolean fastFly = Math.abs(this.getDeltaMovement().y) > 0.1f;
            controller.setAnimation(RawAnimation.begin().thenLoop(fastFly ? "fastFly" : "fly"));
        } else {
            controller.setAnimation(RawAnimation.begin().thenLoop("idle"));
        }

        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 3, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.animatableCache;
    }

    @Override
    public double getTick(Object object) {
        return this.tickCount;
    }

    // -------------------------------------------------------------------------
    // Raven Type
    // -------------------------------------------------------------------------

    public enum Type {
        DARK(55),
        ALBINO(10),
        SEA_GREEN(35),
        THREE_EYED(0); // Name tag variant only

        private final int weight;
        private static final Random RANDOM = new Random();

        Type(int weight) {
            this.weight = weight;
        }

        public static Type getRandomType(RandomSource random) {
            int totalWeight = 0;
            for (Type type : values()) {
                totalWeight += type.weight;
            }

            int roll = RANDOM.nextInt(totalWeight);
            int accumulated = 0;
            for (Type type : values()) {
                accumulated += type.weight;
                if (roll < accumulated) return type;
            }

            return DARK;
        }
    }
}