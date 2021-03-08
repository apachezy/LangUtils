package com.meowj.langutils.nms;

import org.bukkit.DyeColor;
import org.bukkit.block.banner.Pattern;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface NmsItem {

    @Nullable
    DyeColor getShieldBaseColor(@NotNull ItemStack bukkitItem);

    @NotNull
    List<Pattern> getShiedPatterns(@NotNull ItemStack bukkitItem);

}
