package com.meowj.langutils.storages;

import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Logger;

public class ItemBannerPatternStorage extends Storage<Material> {

    public ItemBannerPatternStorage(@NotNull String fallbackLocale) {
        super(fallbackLocale);
    }

    @Override
    @Nullable
    public ConfigurationSection load(@NotNull String locale, @NotNull Configuration langConfig,
                                     @NotNull String node,   @NotNull Logger logger) {
        ConfigurationSection entries = super.load(locale, langConfig, node, logger);
        return entries;
    }

}
