package com.meowj.langutils.storages;

import org.bukkit.block.banner.Pattern;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Logger;

public class BannerPatternStorage extends Storage<Pattern> {

    protected BannerPatternStorage(@NotNull String fallbackLocale) {
        super(fallbackLocale);
    }

    @Override
    public @Nullable ConfigurationSection load(@NotNull String locale, @NotNull Configuration langConfig,
                                               @NotNull String node,   @NotNull Logger logger) {
        return super.load(locale, langConfig, node, logger);
    }

}
