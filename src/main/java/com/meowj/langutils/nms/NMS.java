package com.meowj.langutils.nms;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;


public class NMS {

    private NMS() {
    }

    private static NmsItem nmsItem;
    private static String version;

    public static boolean init() {
        String pkg = Bukkit.getServer().getClass().getPackage().getName();
        String ver = pkg.substring(pkg.lastIndexOf('.') + 1).toLowerCase(Locale.ROOT);
        version = ver.replaceAll("v|_R\\d", "").replace('_', '.');

        try {
            nmsItem = (NmsItem) Class
                    .forName("com.meowj.langutils.nms." + ver + ".NmsItemImpl")
                    .getConstructor()
                    .newInstance();

        } catch (ReflectiveOperationException e) {
            return false;
        }

        return true;
    }

    public static String getVersion() {
        return version;
    }

    @Nullable
    public static Integer getShieldBaseColorOrdinal(@NotNull ItemStack bukkitItem) {
        if (nmsItem == null) {
            throw new IllegalStateException(
                    "LangUtils is not initialized and " +
                    "may not support this server version.");
        }
        return nmsItem.getShieldBaseColorOrdinal(bukkitItem);
    }

}
