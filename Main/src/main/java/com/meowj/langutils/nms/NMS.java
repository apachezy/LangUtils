package com.meowj.langutils.nms;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class NMS {

    private NMS() {
    }

    private static ItemHelper nmsItem;

    public static boolean init() {
        String pkg = Bukkit.getServer().getClass().getPackage().getName();
        String ver = pkg.substring(pkg.lastIndexOf('.') + 1);

        try {
            nmsItem = (ItemHelper) Class
                    .forName("com.meowj.langutils.nms.ItemHelper_" + ver)
                    .getConstructor()
                    .newInstance();
        } catch (ReflectiveOperationException e) {
            return false;
        }

        return true;
    }

    @Nullable
    public static Integer getShieldBaseColorOrdinal(@NotNull ItemStack bukkitItem) {
        return nmsItem.getShieldBaseColorOrdinal(bukkitItem);
    }

}
