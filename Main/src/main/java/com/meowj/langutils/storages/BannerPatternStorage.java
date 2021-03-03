package com.meowj.langutils.storages;

import com.meowj.langutils.misc.Util;
import org.bukkit.DyeColor;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Level;
import java.util.logging.Logger;

public class BannerPatternStorage extends Storage<Integer> {

    public BannerPatternStorage(@NotNull String fallbackLocale) {
        super(fallbackLocale);
    }

    @Override
    @Nullable
    public ConfigurationSection load(@NotNull String locale, @NotNull Configuration langConfig,
                                     @NotNull String config, @NotNull Logger logger) {
        ConfigurationSection entries = super.load(locale, langConfig, config, logger);

        if (entries == null) {
            return null;
        }

        for (PatternType paType : PatternType.values()) {
            if (paType == PatternType.BASE) {
                continue;
            }

            for (DyeColor color : DyeColor.values()) {

                Pattern pattern = new Pattern(color, paType);
                Integer mixCode = Util.getPatternMixedCode(pattern);
                String strValue = entries.getString(Integer.toString(mixCode));

                if (strValue == null || strValue.isEmpty()) {
                    if (locale.equals(fallbackLocale)) {
                        logger.log(
                                Level.SEVERE,
                                "BannerPattern {0}.{1} is missing in fallback language {2}.",
                                new Object[]{paType, color, locale});
                    }
                    continue;
                }

                addEntry(locale, mixCode, strValue);
            }
        }

        return entries;
    }

}
