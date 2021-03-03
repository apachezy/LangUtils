package com.meowj.langutils.storages;

import com.meowj.langutils.LangUtils;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Villager.Profession;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProfessionStorage extends Storage<Profession> {

    public ProfessionStorage(@NotNull String fallbackLocale) {
        super(fallbackLocale);
    }

    @Override
    public ConfigurationSection load(@NotNull String locale, @NotNull Configuration langConfig,
                                     @NotNull String config, @NotNull Logger logger) {
        ConfigurationSection entries = super.load(locale, langConfig, config, logger);

        if (entries != null) {
            for (Profession profession : Profession.values()) {

                String entryName = profession.getKey().getKey();
                String localized = entries.getString(entryName);

                if (localized == null || localized.isEmpty()) {
                    if (locale.equals(fallbackLocale)) {
                        logger.log(
                                Level.SEVERE,
                                "Villager Profession name {0} is missing in fallback language {1}.",
                                new String[]{entryName, locale});
                    }
                    continue;
                }

                addEntry(locale, profession, localized);
            }
        }

        return entries;
    }

    @Override
    public void addEntry(@NotNull String locale, @NotNull Profession profession, @NotNull String localized) {
        locale = LangUtils.fixLocale(locale);
        Map<Profession, String> pairMap = pairStorage.computeIfAbsent(locale, s -> new EnumMap<>(Profession.class));
        pairMap.put(profession, localized);

        remapping(locale, pairMap);
    }

    @Override
    @NotNull
    public String getEntry(@NotNull String locale, @NotNull Profession profession) {
        String result = super.getEntry(locale, profession);
        return result == null ? profession.getKey().toString() : result;
    }

}