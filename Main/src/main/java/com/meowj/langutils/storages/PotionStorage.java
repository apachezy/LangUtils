package com.meowj.langutils.storages;

import com.meowj.langutils.LangUtils;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PotionStorage extends Storage<PotionType> {

    public PotionStorage(@NotNull String fallbackLocale) {
        super(fallbackLocale);
    }

    @Override
    public ConfigurationSection load(@NotNull String locale, @NotNull Configuration langConfig,
                                     @NotNull String config, @NotNull Logger logger) {
        ConfigurationSection entries = super.load(locale, langConfig, config, logger);

        if (entries != null) {
            for (PotionType potionType : PotionType.values()) {

                String entryName = potionType.name().toLowerCase(Locale.ROOT);
                String localized = entries.getString(entryName);

                if (localized == null || localized.isEmpty()) {
                    if (locale.equals(fallbackLocale)) {
                        logger.log(
                                Level.SEVERE,
                                "PotionType name {0} is missing in fallback language {1}.",
                                new String[]{entryName, locale});
                    }
                    continue;
                }

                addEntry(locale, potionType, localized);
            }
        }

        return entries;
    }

    @Override
    public void addEntry(@NotNull String locale, @NotNull PotionType potionType, @NotNull String localized) {
        locale = LangUtils.fixLocale(locale);
        Map<PotionType, String> pairMap = pairStorage.computeIfAbsent(locale, s -> new EnumMap<>(PotionType.class));
        pairMap.put(potionType, localized);

        remapping(locale, pairMap);
    }

    @Override
    @NotNull
    public String getEntry(@NotNull String locale, @NotNull PotionType potionType) {
        String result = super.getEntry(locale, potionType);
        return result == null ? potionType.name().toLowerCase(Locale.ROOT) : result;
    }

}