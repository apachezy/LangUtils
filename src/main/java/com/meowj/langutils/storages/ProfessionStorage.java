package com.meowj.langutils.storages;

import com.meowj.langutils.LangUtils;
import com.meowj.langutils.misc.Remaper;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Villager.Profession;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;

public class ProfessionStorage extends Storage<Profession> {

    public ProfessionStorage(@NotNull String fallbackLocale) {
        super(fallbackLocale);
    }

    @Override
    @Nullable
    public ConfigurationSection load(@NotNull String locale, @NotNull Configuration langConfig,
                                     @NotNull String config, @Nullable Remaper remaper) {

        ConfigurationSection entries = super.load(locale, langConfig, config, remaper);

        if (entries != null) {
            for (Profession profession : Profession.values()) {

                String entryName = profession.name().toLowerCase(Locale.ROOT);
                String localized = entries.getString(entryName);

                if (localized == null || localized.isEmpty()) {
                    if (locale.equals(fallbackLocale)) {
                        Bukkit.getLogger().log(
                                Level.SEVERE,
                                "Villager Profession name {0} is missing in fallback language {1}.",
                                new String[]{entryName, locale});
                    }
                    continue;
                }

                addEntry(locale, profession, localized, remaper);
            }
        }

        return entries;
    }

    @Override
    public void addEntry(@NotNull String locale, @NotNull Profession profession,
                         @NotNull String localized, Remaper remaper) {
        locale = LangUtils.fixLocale(locale);
        Map<Profession, String> pairMap = pairStorage.computeIfAbsent(locale, s -> new EnumMap<>(Profession.class));
        pairMap.put(profession, localized);

        remapping(locale, pairMap, remaper);
    }

    @Override
    @NotNull
    public String getEntry(@NotNull String locale, @NotNull Profession profession) {
        String result = super.getEntry(locale, profession);
        return result == null ? profession.name().toLowerCase(Locale.ROOT) : result;
    }

}