package com.meowj.langutils.storages;

import com.meowj.langutils.LangUtils;
import com.meowj.langutils.misc.Remaper;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.TropicalFish.Pattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;

public class TropicalFishTypeStorage extends Storage<Pattern> {

    public TropicalFishTypeStorage(@NotNull String fallbackLocale) {
        super(fallbackLocale);
    }

    @Override
    public ConfigurationSection load(@NotNull String locale, @NotNull Configuration langConfig,
                                     @NotNull String config, @Nullable Remaper remaper) {

        ConfigurationSection entries = super.load(locale, langConfig, config, remaper);

        if (entries != null) {
            String entryName;
            String localized;

            for (Pattern pattern : Pattern.values()) {

                entryName = pattern.name().toLowerCase(Locale.ROOT);
                localized = entries.getString(entryName);

                if (localized == null || localized.isEmpty()) {
                    if (locale.equals(fallbackLocale)) {
                        Bukkit.getLogger().log(
                                Level.SEVERE,
                                "TropicalFish type name {0} is missing in fallback language {1}.",
                                new String[]{entryName, locale});
                    }
                    continue;
                }

                addEntry(locale, pattern, localized, remaper);
            }
        }

        return entries;
    }

    @Override
    public void addEntry(@NotNull String locale, @NotNull Pattern pattern,
                         @NotNull String localized, Remaper remaper) {

        locale = LangUtils.fixLocale(locale);
        Map<Pattern, String> pairMap = pairStorage.computeIfAbsent(locale, s -> new EnumMap<>(Pattern.class));
        pairMap.put(pattern, localized);

        remapping(locale, pairMap, remaper);
    }

    @Override
    @NotNull
    public String getEntry(@NotNull String locale, @NotNull Pattern pattern) {
        String result = super.getEntry(locale, pattern);
        return result == null ? pattern.name().toLowerCase(Locale.ROOT) : result;
    }

}