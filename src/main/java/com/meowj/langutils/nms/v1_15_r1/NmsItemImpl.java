package com.meowj.langutils.nms.v1_15_r1;

import com.meowj.langutils.nms.NmsItem;
import net.minecraft.server.v1_15_R1.ItemStack;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class NmsItemImpl implements NmsItem {

    @Override
    @Nullable
    public Integer getShieldBaseColorOrdinal(@NotNull org.bukkit.inventory.ItemStack bukkitItem) {
        if (bukkitItem.getType() != Material.SHIELD) {
            return null;
        }

        ItemStack nmsItem = CraftItemStack.asNMSCopy(bukkitItem);
        NBTTagCompound tag = nmsItem.getTag();
        if (tag == null) {
            return null;
        }

        NBTTagCompound entityTag = tag.getCompound("BlockEntityTag");
        if (entityTag == null) {
            return null;
        }

        if (entityTag.hasKeyOfType("Base", 99)) {
            return entityTag.getInt("Base");
        }

        return null;
    }
}
