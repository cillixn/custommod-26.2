package com.cillixn.custommod.item.custom;


import com.cillixn.custommod.CustomMod;
import com.cillixn.custommod.item.ModItems;
import com.google.common.eventbus.Subscribe;
import net.minecraft.advancements.triggers.LightningStrikeTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.LightningBoltRenderer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderSet;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.pig.Pig;
import net.minecraft.world.entity.animal.sheep.Sheep;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;


import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.effects.SpawnParticlesEffect;
import net.minecraft.world.item.enchantment.effects.SummonEntityEffect;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.entity.EntityTickList;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.startup.Server;
import net.neoforged.neoforge.event.entity.EntityEvent;
import net.neoforged.neoforge.event.entity.EntityStruckByLightningEvent;
import net.neoforged.neoforge.event.entity.item.ItemTossEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;


import java.awt.dnd.DropTargetEvent;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Properties;





@EventBusSubscriber(modid = CustomMod.MOD_ID)
public class Mjolnir extends Item {

    private static boolean thrown = false;

    public Mjolnir(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {

        Player player = pContext.getPlayer();
        BlockPos blockClicked = pContext.getClickedPos();

        if (!pContext.getLevel().isClientSide()) {
            if (player.getMainHandItem().getItem() == ModItems.MJOLNIR.get()) {
                EntityTypes.LIGHTNING_BOLT.spawn((ServerLevel) pContext.getLevel(), blockClicked, EntitySpawnReason.NATURAL);
            }
        }


        return super.useOn(pContext);
    }


    @SubscribeEvent
    private static void throwHammer(ItemTossEvent event) {
        Level level = event.getPlayer().level();
        Vec3 playerHeadAngle = event.getPlayer().getHeadLookAngle();

        if (!level.isClientSide()) {
            ItemEntity mjolnir = event.getEntity();
            mjolnir.setDefaultPickUpDelay();
            if(mjolnir.getItem().getItem() == ModItems.MJOLNIR.get()) {
                mjolnir.addDeltaMovement(new Vec3(playerHeadAngle.x() * 1.5, playerHeadAngle.y() * 1.5, playerHeadAngle.z() * 1.5));

                thrown = true;
            }

        }

    }

    @SubscribeEvent
    private static void tickEvent(ServerTickEvent.Pre event) {

        ServerLevel overworld = event.getServer().getLevel(Level.OVERWORLD);
//        Level nether = event.getServer().getLevel(Level.NETHER);
//        Level end = event.getServer().getLevel(Level.END);




        if(!overworld.isClientSide()) {
            Iterable<Entity> entities = Objects.requireNonNull(overworld).getAllEntities();

            for (Entity entity : entities) {
                if (!(entity instanceof ItemEntity)) {
                    continue;
                }

                if (((ItemEntity) entity).getItem().getItem() != ModItems.MJOLNIR.get()) {
                    continue;
                }
                entity.setInvulnerable(true);
                var closeEntities = overworld
                        .getEntities(entity, entity.getBoundingBox().inflate(0.5))
                        .stream()
                        .filter(entity1 -> entity1 instanceof LivingEntity && !(entity1 instanceof Player))
                        .toList();

                if (!closeEntities.isEmpty()) {
                    EntityTypes.LIGHTNING_BOLT.spawn(overworld, entity.blockPosition(), EntitySpawnReason.NATURAL);
                    for (Entity player : entities) {
                        if (player instanceof Player) {
                            Vec3 playerPosition = player.position();

                            entity.setDeltaMovement(Vec3.ZERO);
                            entity.setPos(playerPosition);
                            ((ItemEntity) entity).setNoPickUpDelay();
                        }
                    }
                }
                for (Entity player : entities) {
                    if (player instanceof Player) {
                        List<Block> blocks = List.of(
                                Blocks.AIR,
                                Blocks.SHORT_GRASS,
                                Blocks.TALL_GRASS,
                                Blocks.SHORT_DRY_GRASS,
                                Blocks.TALL_DRY_GRASS,
                                Blocks.SEAGRASS,
                                Blocks.SEAGRASS,
                                Blocks.TALL_SEAGRASS);
                        if (!(blocks.contains(entity.getBlockStateOn().getBlock()))) {
                            Vec3 playerPosition = player.position();

                            if (thrown) {
                                entity.setDeltaMovement(Vec3.ZERO);
                                entity.setPos(playerPosition);
                                ((ItemEntity) entity).setNoPickUpDelay();
                            }
                            thrown = false;
                        }
                    }
                }



//                if (entity instanceof ItemEntity mjolnir) {
//                    if(mjolnir.getItem().getItem() == ModItems.MJOLNIR.get()) {
//                        for (Entity currentEntity : entities) {
//                            if (currentEntity instanceof LivingEntity && !(currentEntity instanceof Player) && currentEntity.getBoundingBox().intersects(mjolnir.getBoundingBox())) {
//                                EntityTypes.LIGHTNING_BOLT.spawn((ServerLevel) event.getServer().getLevel(Level.OVERWORLD), currentEntity.blockPosition(), EntitySpawnReason.NATURAL);
//                              lightningStruck = true;
//                            }
//                            if (lightningStruck) {
//                                mjolnir.addDeltaMovement(new Vec3(-playerHeadX * 1.3, -playerHeadY * 1.3, -playerHeadZ * 1.3));
//                            }
//                        }
//                    }
//
//                }
            }

        }

    }

//    @SubscribeEvent
//    private static void lightningStrike(EntityStruckByLightningEvent event) {
//
//        if (!(event.getEntity().level().isClientSide())) {
//
//            if (event.getEntity() instanceof LivingEntity) {
//                return;
//            }
//            if (event.getEntity() instanceof ItemEntity mjolnir && ((ItemEntity) event.getEntity()).getItem().getItem() == ModItems.MJOLNIR.get()) {
//                event.getEntity().setInvulnerable(true);
//                lightningStruck = true;
//            }
//        }
//    }


}
