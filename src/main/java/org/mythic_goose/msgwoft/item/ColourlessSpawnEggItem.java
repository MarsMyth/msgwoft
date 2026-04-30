package org.mythic_goose.msgwoft.item;

import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Spawner;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;
import org.jetbrains.annotations.Nullable;

public class ColourlessSpawnEggItem extends Item {
    private static final Map<EntityType<? extends Mob>, ColourlessSpawnEggItem> BY_ID = Maps.newIdentityHashMap();
    private static final MapCodec<EntityType<?>> ENTITY_TYPE_FIELD_CODEC;
    private final EntityType<?> defaultType;

    public ColourlessSpawnEggItem(EntityType<? extends Mob> entityType, Item.Properties properties) {
        super(properties);
        this.defaultType = entityType;
        BY_ID.put(entityType, this);
    }

    public InteractionResult useOn(UseOnContext useOnContext) {
        Level level = useOnContext.getLevel();
        if (!(level instanceof ServerLevel)) {
            return InteractionResult.SUCCESS;
        } else {
            ItemStack itemStack = useOnContext.getItemInHand();
            BlockPos blockPos = useOnContext.getClickedPos();
            Direction direction = useOnContext.getClickedFace();
            BlockState blockState = level.getBlockState(blockPos);
            BlockEntity var8 = level.getBlockEntity(blockPos);
            if (var8 instanceof Spawner) {
                Spawner spawner = (Spawner)var8;
                EntityType<?> entityType = this.getType(itemStack);
                spawner.setEntityId(entityType, level.getRandom());
                level.sendBlockUpdated(blockPos, blockState, blockState, 3);
                level.gameEvent(useOnContext.getPlayer(), GameEvent.BLOCK_CHANGE, blockPos);
                itemStack.shrink(1);
                return InteractionResult.CONSUME;
            } else {
                BlockPos blockPos2;
                if (blockState.getCollisionShape(level, blockPos).isEmpty()) {
                    blockPos2 = blockPos;
                } else {
                    blockPos2 = blockPos.relative(direction);
                }

                EntityType<?> entityType = this.getType(itemStack);
                if (entityType.spawn((ServerLevel)level, itemStack, useOnContext.getPlayer(), blockPos2, MobSpawnType.SPAWN_EGG, true, !Objects.equals(blockPos, blockPos2) && direction == Direction.UP) != null) {
                    itemStack.shrink(1);
                    level.gameEvent(useOnContext.getPlayer(), GameEvent.ENTITY_PLACE, blockPos);
                }

                return InteractionResult.CONSUME;
            }
        }
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack itemStack = player.getItemInHand(interactionHand);
        BlockHitResult blockHitResult = getPlayerPOVHitResult(level, player, Fluid.SOURCE_ONLY);
        if (blockHitResult.getType() != Type.BLOCK) {
            return InteractionResultHolder.pass(itemStack);
        } else if (!(level instanceof ServerLevel)) {
            return InteractionResultHolder.success(itemStack);
        } else {
            BlockPos blockPos = blockHitResult.getBlockPos();
            if (!(level.getBlockState(blockPos).getBlock() instanceof LiquidBlock)) {
                return InteractionResultHolder.pass(itemStack);
            } else if (level.mayInteract(player, blockPos) && player.mayUseItemAt(blockPos, blockHitResult.getDirection(), itemStack)) {
                EntityType<?> entityType = this.getType(itemStack);
                Entity entity = entityType.spawn((ServerLevel)level, itemStack, player, blockPos, MobSpawnType.SPAWN_EGG, false, false);
                if (entity == null) {
                    return InteractionResultHolder.pass(itemStack);
                } else {
                    itemStack.consume(1, player);
                    player.awardStat(Stats.ITEM_USED.get(this));
                    level.gameEvent(player, GameEvent.ENTITY_PLACE, entity.position());
                    return InteractionResultHolder.consume(itemStack);
                }
            } else {
                return InteractionResultHolder.fail(itemStack);
            }
        }
    }

    public boolean spawnsEntity(ItemStack itemStack, EntityType<?> entityType) {
        return Objects.equals(this.getType(itemStack), entityType);
    }

    @Nullable
    public static ColourlessSpawnEggItem byId(@Nullable EntityType<?> entityType) {
        return (ColourlessSpawnEggItem)BY_ID.get(entityType);
    }

    public EntityType<?> getType(ItemStack itemStack) {
        CustomData customData = (CustomData)itemStack.getOrDefault(DataComponents.ENTITY_DATA, CustomData.EMPTY);
        return !customData.isEmpty() ? (EntityType)customData.read(ENTITY_TYPE_FIELD_CODEC).result().orElse(this.defaultType) : this.defaultType;
    }

    public FeatureFlagSet requiredFeatures() {
        return this.defaultType.requiredFeatures();
    }

    public Optional<Mob> spawnOffspringFromSpawnEgg(Player player, Mob mob, EntityType<? extends Mob> entityType, ServerLevel serverLevel, Vec3 vec3, ItemStack itemStack) {
        if (!this.spawnsEntity(itemStack, entityType)) {
            return Optional.empty();
        } else {
            Mob mob2;
            if (mob instanceof AgeableMob) {
                mob2 = ((AgeableMob)mob).getBreedOffspring(serverLevel, (AgeableMob)mob);
            } else {
                mob2 = (Mob)entityType.create(serverLevel);
            }

            if (mob2 == null) {
                return Optional.empty();
            } else {
                mob2.setBaby(true);
                if (!mob2.isBaby()) {
                    return Optional.empty();
                } else {
                    mob2.moveTo(vec3.x(), vec3.y(), vec3.z(), 0.0F, 0.0F);
                    serverLevel.addFreshEntityWithPassengers(mob2);
                    mob2.setCustomName((Component)itemStack.get(DataComponents.CUSTOM_NAME));
                    itemStack.consume(1, player);
                    return Optional.of(mob2);
                }
            }
        }
    }

    static {
        ENTITY_TYPE_FIELD_CODEC = BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("id");
    }
}
