package com.cillixn.custommod.item;

import com.cillixn.custommod.CustomMod;
import com.cillixn.custommod.item.custom.Mjolnir;
import com.google.common.eventbus.Subscribe;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;


public class ModItems {

    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(CustomMod.MOD_ID); // holds all the items

    public static final DeferredItem<Item> MJOLNIR = ITEMS.registerItem("mjolnir", properties -> new Mjolnir(properties.axe(ToolMaterial.NETHERITE, 12f, 2.4f).fireResistant()));

    public static final DeferredItem<Item> VIBRANIUM = ITEMS.registerSimpleItem("vibranium");

    // used to register the items to the event bus
    @Subscribe
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
