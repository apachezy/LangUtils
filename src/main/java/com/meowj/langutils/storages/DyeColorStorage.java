package com.meowj.langutils.storages;

import com.meowj.langutils.LangUtils;
import org.bukkit.DyeColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;

public class DyeColorStorage extends Storage<DyeColor> {

    public DyeColorStorage(@NotNull String fallbackLocale) {
        super(fallbackLocale);
    }

    @Override
    public ConfigurationSection load(@NotNull String locale, @NotNull Configuration langConfig, @NotNull String node) {
        ConfigurationSection entries = super.load(locale, langConfig, node);

        if (entries != null) {
            String entryName;
            String localized;

            for (DyeColor color : DyeColor.values()) {
                entryName = color.name().toLowerCase(Locale.ROOT);
                localized = entries.getString(entryName);
                if (localized == null || localized.isEmpty()) {
                    if (locale.equals(fallbackLocale)) {
                        plugin.error(
                                "DyeColor name "
                                        + entryName
                                        + " is missing in fallback language "
                                        + locale + ".");
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
        Map<DyeColor, String> pairMap = storage.computeIfAbsent(locale, s -> new EnumMap<>(DyeColor.class));
        pairMap.put(color, localized);
        remapping(locale, pairMap);
    }

    @Override
    @NotNull
    public String getEntry(@NotNull String locale, @NotNull DyeColor color) {
        String result = super.getEntry(locale, color);
        return result == null ? color.name().toLowerCase(Locale.ROOT) : result;
    }

}
