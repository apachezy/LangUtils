package com.meowj.langutils.nms;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.block.Banner;
import org.bukkit.block.banner.Pattern;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
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

    @Nullable
    public static String getVersion() {
        return version;
    }

    /**
     * The base color of the shield is a bit special.
     * <p>
     * The official way is to call {@link BlockStateMeta#getBlockState()}
     * then call {@link Banner#getBaseColor()}. However, doing so will
     * mistake a shield without a basic color as "white". So, we use NBT
     * to get the basic color of the shield，if the shield is colorless,
     * there will be no "Base" tag in its NBT data.
     *
     * @param bukkitItem A shield Item Stack.
     * @return Returns the base color of the shield.
     */
    @Nullable
    public static DyeColor getShieldBaseColor(@NotNull ItemStack bukkitItem) {
        if (nmsItem == null) {
            throw new IllegalStateException(
                    "LangUtils is not initialized and " +
                    "may not support this server version.");
        }
        return nmsItem.getShieldBaseColor(bukkitItem);
    }

    @NotNull
    public static List<Pattern> getShiedPatterns(@NotNull ItemStack bukkitItem) {
        if (nmsItem == null) {
            throw new IllegalStateException(
                    "LangUtils is not initialized and " +
                    "may not support this server version.");
        }
        return nmsItem.getShiedPatterns(bukkitItem);
    }

}
