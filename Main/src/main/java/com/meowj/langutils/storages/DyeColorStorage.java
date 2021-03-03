package com.meowj.langutils.storages;

import com.meowj.langutils.LangUtils;
import org.bukkit.DyeColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DyeColorStorage extends Storage<DyeColor> {

    public DyeColorStorage(@NotNull String fallbackLocale) {
        super(fallbackLocale);
    }

    @Override
    public ConfigurationSection load(@NotNull String locale, @NotNull Configuration langConfig,
                                     @NotNull String config, @NotNull Logger logger) {
        ConfigurationSection entries = super.load(locale, langConfig, config, logger);

        if (entries != null) {
            for (DyeColor color : DyeColor.values()) {

                String entryName = color.name().toLowerCase(Locale.ROOT);
                String localized = entries.getString(entryName);

                if (localized == null || localized.isEmpty()) {
                    if (locale.equals(fallbackLocale)) {
                        logger.log(
                                Level.SEVERE,
                                "DyeColor name {0} is missing in fallback language {1}.",
                                new String[]{entryName, locale});
                    }
                    continue;
                }

                addEntry(locale, color, localized);
            }
        }

        return entries;
    }

    @Override
    public void addEntry(@NotNull String locale, @NotNull DyeColor color, @NotNull String localized) {
        locale = LangUtils.fixLocale(locale);
        Map<DyeColor, String> pairMap = pairStorage.computeIfAbsent(locale, s -> new EnumMap<>(DyeColor.class));
        pairMap.put(color, localized);

        remapping(locale, pairMap);
    }

}