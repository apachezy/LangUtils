package com.meowj.langutils.nms.v1_13_r2;

import com.meowj.langutils.nms.NmsItem;
import net.minecraft.server.v1_13_R2.ItemStack;
import net.minecraft.server.v1_13_R2.NBTBase;
import net.minecraft.server.v1_13_R2.NBTTagCompound;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class NmsItemImpl implements NmsItem {

    @Override
    @Nullable
    @SuppressWarnings("deprecation")
    public DyeColor getShieldBaseColor(@NotNull org.bukkit.inventory.ItemStack bukkitItem) {
        if (bukkitItem.getType() != Material.SHIELD) {
            return null;
        }

        ItemStack nmsItem = CraftItemStack.asNMSCopy(bukkitItem);
        NBTTagCompound tag = nmsItem.getTag();

        if (tag != null) {
            NBTTagCompound entityTag = tag.getCompound("BlockEntityTag");

            if (entityTag.hasKeyOfType("Base", 99)) {
                return DyeColor.getByWoolData((byte) entityTag.getInt("Base"));
            }
        }

        return null;
    }

    @Override
    @NotNull
    public List<Pattern> getShiedPatterns(@NotNull org.bukkit.inventory.ItemStack bukkitItem) {
        List<Pattern> result = new ArrayList<>();

        if (bukkitItem.getType() != Material.SHIELD) {
            return result;
        }

        ItemStack nmsItem = CraftItemStack.asNMSCopy(bukkitItem);

        NBTTagCompound tag = nmsItem.getTag();
        if (tag == null) {
            return result;
        }

        for (NBTBase nbtBase : tag.getCompound("BlockEntityTag").getList("Patterns", 10)) {
            NBTTagCompound entries = (NBTTagCompound) nbtBase;

            if (!entries.hasKeyOfType("Color", 99) || !entries.hasKeyOfType("Pattern", 8)) {
                continue;
            }

            @SuppressWarnings("deprecation")
            DyeColor color = DyeColor.getByWoolData((byte) entries.getInt("Color"));
            PatternType tp = PatternType.getByIdentifier(entries.getString("Pattern"));

            if (color != null && tp != null) {
                result.add(new Pattern(color, tp));
            }
        }

        return result;
    }

}
