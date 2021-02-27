package com.meowj.langutils.misc;

import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.inventory.meta.TropicalFishBucketMeta;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;

public class Util {

    public static String getShortTime(int duration) {
        int m = duration / 20 / 60;
        int s = duration / 20 % 60;
        return String.format("%d:%02d", m, s);
    }

    public static PotionType getPotionType(@NotNull ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta instanceof PotionMeta) {
            return ((PotionMeta) meta).getBasePotionData().getType();
        }
        throw new UnsupportedOperationException("The given item is not a potion.");
    }

    @Nullable
    public static String getPlayerHeadOwner(@NotNull ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta instanceof SkullMeta) {
            OfflinePlayer ofp = ((SkullMeta) meta).getOwningPlayer();
            if (ofp != null) {
                return ofp.getName();
            }
        }
        return null;
    }

    @Nullable
    public static Integer getTropicalFishVariant(@NotNull TropicalFishBucketMeta meta) {
        if (meta.hasVariant()) {
            try {
                Field variantField = meta.getClass().getDeclaredField("variant");
                variantField.setAccessible(true);
                return (Integer) variantField.get(meta);
            } catch (NoSuchFieldException | IllegalAccessException ignored) {
                // nothing
            }
        }
        return null;
    }

}
