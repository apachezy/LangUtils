package com.meowj.langutils.nms;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ItemHelper {

    @Nullable
    Integer getShieldBaseColorOrdinal(@NotNull ItemStack bukkitItem);

}
