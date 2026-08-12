package com.cillixn.custommod.item.custom;

import com.cillixn.custommod.item.ModItems;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ToolMaterial;
import net.neoforged.neoforge.client.model.obj.ObjMaterialLibrary;
import net.neoforged.neoforge.common.Tags;

public class Vibranium extends ObjMaterialLibrary.Material {

    public Vibranium(String name) {
        super(name);
    }

    public static final ToolMaterial VIBRANIUM = new ToolMaterial(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 1000, 9f, 2,  30, Tags.Items.INGOTS_IRON);
}
