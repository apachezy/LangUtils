package com.meowj.langutils.storages;

import com.meowj.langutils.LangUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.TropicalFish.Pattern;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;

public class TropicalFishTypeStorage extends Storage<Pattern> {

    public TropicalFishTypeStorage(@NotNull String fallbackLocale) {
        super(fallbackLocale);
    }

    @Override
    public ConfigurationSection load(@NotNull String locale, @NotNull Configuration langConfig, @NotNull String node) {
        ConfigurationSection entries = super.load(locale, langConfig, node);

        if (entries != null) {
            String entryName;
            String localized;

            for (Pattern pattern : Pattern.values()) {
                entryName = pattern.name().toLowerCase(Locale.ROOT);
                localized = entries.getString(entryName);
                if (localized == null || localized.isEmpty()) {
                    if (locale.equals(fallbackLocale)) {
                        Bukkit.getLogger().severe(
                                "Tropical fish type name "
                                        + entryName
                                        + " is missing in fallback language "
                                        + locale + ".");
                    }
                    continue;
                }
                addEntry(locale, pattern, localized);
            }
        }

        return entries;
    }

    @Override
    public void addEntry(@NotNull String locale, @NotNull Pattern pattern, @NotNull String localized) {
        locale = LangUtils.fixLocale(locale);
        Map<Pattern, String> pairMap = pairStorage.computeIfAbsent(locale, s -> new EnumMap<>(Pattern.class));
        pairMap.put(pattern, localized);

        remapping(locale, pairMap);
    }

    @Override
    @NotNull
    public String getEntry(@NotNull String locale, @NotNull Pattern pattern) {
        String result = super.getEntry(locale, pattern);
        return result == null ? pattern.name().toLowerCase(Locale.ROOT) : result;
    }

}
