package com.meowj.langutils.storages;

import com.meowj.langutils.LangUtils;
import com.meowj.langutils.misc.Remaper;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Villager.Career;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;

public class VillagerCareerStorage extends Storage<Career> {

    public VillagerCareerStorage(@NotNull String fallbackLocale) {
        super(fallbackLocale);
    }

    @Override
    @Nullable
    public ConfigurationSection load(@NotNull String locale, @NotNull Configuration langConfig,
                                                  @NotNull String config, @Nullable Remaper remaper) {

        ConfigurationSection entries = super.load(locale, langConfig, config, remaper);

        if (entries != null) {
            for (Career career : Career.values()) {

                String entryName = career.name().toLowerCase(Locale.ROOT);
                String localized = entries.getString(entryName);

                if (localized == null || localized.isEmpty()) {
                    if (locale.equals(fallbackLocale)) {
                        Bukkit.getLogger().log(
                                Level.SEVERE,
                                "Villager Career name {0} is missing in fallback language {1}.",
                                new String[]{entryName, locale});
                    }
                    continue;
                }

                addEntry(locale, career, localized, remaper);
            }
        }

        return entries;
    }

    @Override
    public void addEntry(@NotNull String locale, @NotNull Career career, @NotNull String localized, Remaper remaper) {
        locale = LangUtils.fixLocale(locale);
        Map<Career, String> pairMap = pairStorage.computeIfAbsent(locale, s -> new EnumMap<>(Career.class));
        pairMap.put(career, localized);

        remapping(locale, pairMap, remaper);
    }

    @Override
    @NotNull
    public String getEntry(@NotNull String locale, @NotNull Career career) {
        String result = super.getEntry(locale, career);
        return result == null ? career.name().toLowerCase(Locale.ROOT) : result;
    }

}
