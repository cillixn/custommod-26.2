package com.cillixn.custommod.item;

import com.cillixn.custommod.CustomMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;


public class CreativeModeTabs {

    private static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CustomMod.MOD_ID);

    private static final DeferredHolder MOD_ITEMS = CREATIVE_MODE_TABS.register("mod_items_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("creativetab.custommod.mod_items"))
            .icon(() -> new ItemStack(ModItems.MJOLNIR.get()))

            .displayItems((itemDisplayParameters, output) -> {
                output.accept(ModItems.MJOLNIR.get());
            })
            .build());

    public static void registerCreativeModeTab(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
