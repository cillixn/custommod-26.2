package com.cillixn.custommod.item.custom;


import com.cillixn.custommod.CustomMod;
import com.cillixn.custommod.item.ModItems;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.LightningBoltRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderSet;
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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.entity.EntityTickList;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityEvent;
import net.neoforged.neoforge.event.entity.item.ItemTossEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;


import java.awt.dnd.DropTargetEvent;
import java.util.Collection;
import java.util.Properties;





@EventBusSubscriber(modid = CustomMod.MOD_ID)
public class Mjolnir extends Item {


    public Mjolnir(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {

        Player player = pContext.getPlayer();
        BlockPos blockClicked = pContext.getClickedPos();

        if(!pContext.getLevel().isClientSide())
        {
            if(player.getMainHandItem().getItem() == ModItems.MJOLNIR.get())
            {
                EntityTypes.LIGHTNING_BOLT.spawn((ServerLevel) pContext.getLevel(), blockClicked, EntitySpawnReason.NATURAL);
            }
        }


        return super.useOn(pContext);
    }

//    @Override
//    public boolean onDroppedByPlayer(ItemStack item, Player player) {
//        return super.onDroppedByPlayer(item, player);
//    }
//
//    public InteractionResult throwHammer(UseOnContext pContext) {
//
//        Player player = pContext.getPlayer();
//        ItemStack item = pContext.getItemInHand();
//
//        if(!pContext.getLevel().isClientSide()) {
//            if(item.getItem() == ModItems.MJOLNIR.get() && onDroppedByPlayer(item, player)) {
//
//
//                ItemEntity droppedItem = pContext.getLevel().getEntitiesOfClass(item.getItem().getClass(), );
//            }
//        }
//        return super.useOn(pContext);
//    }

    @SubscribeEvent
    private static void throwHammer(ItemTossEvent event) {

        Level level = event.getPlayer().level();
        Vec3 playerHeadAngle = event.getPlayer().getHeadLookAngle();

        if(!level.isClientSide()) {
            ItemEntity mjolnir = event.getEntity();

            if(mjolnir.getItem().getItem() == ModItems.MJOLNIR.get()) {
                mjolnir.addDeltaMovement(new Vec3(playerHeadAngle.x() * 1.2, playerHeadAngle.y() * 1.2, playerHeadAngle.z() * 1.2));
            }

            if(mjolnir.getKnownSpeed() == Vec3.ZERO && mjolnir.getDeltaMovement() == Vec3.ZERO) {
                EntityTypes.LIGHTNING_BOLT.spawn((ServerLevel) level, mjolnir.getOnPos(), EntitySpawnReason.NATURAL);
            }



        }

    }


}
