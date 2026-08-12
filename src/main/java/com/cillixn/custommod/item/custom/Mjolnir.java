package com.cillixn.custommod.item.custom;


import com.cillixn.custommod.item.ModItems;
import net.minecraft.client.renderer.entity.LightningBoltRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;


import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.effects.SpawnParticlesEffect;
import net.minecraft.world.item.enchantment.effects.SummonEntityEffect;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;


import java.util.Properties;
import java.util.logging.Level;

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

    @Override
    public boolean onDroppedByPlayer(ItemStack item, Player player) {
        return super.onDroppedByPlayer(item, player);
    }

    public InteractionResult throwHammer(UseOnContext pContext) {

        Player player = pContext.getPlayer();
        ItemStack item = pContext.getItemInHand();

        if(!pContext.getLevel().isClientSide()) {
            if(item.getItem() == ModItems.MJOLNIR.get() && onDroppedByPlayer(item, player)) {


            }
        }
        return super.useOn(pContext);
    }

}
