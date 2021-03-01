package com.meowj.langutils.storages;

import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.logging.Level;

public class PotionEffectStorage extends Storage<PotionEffectType> {

    public PotionEffectStorage(@NotNull String fallbackLocale) {
        super(fallbackLocale);
    }

    @Override
    public ConfigurationSection load(@NotNull String locale, @NotNull Configuration langConfig, @NotNull String node) {
        ConfigurationSection entries = super.load(locale, langConfig, node);

        if (entries != null) {
            String entryName;
            String localized;

            for (PotionEffectType effect : PotionEffectType.values()) {
                entryName = effect.getName().toLowerCase(Locale.ROOT);
                localized = entries.getString(entryName);

                if (localized == null || localized.isEmpty()) {
                    if (locale.equals(fallbackLocale)) {
                        Bukkit.getLogger().log(
                                Level.SEVERE,
                                "PotionEffectType name {0} is missing in fallback language {1}.",
                                new String[]{entryName, locale});
                    }
                    continue;
                }

                addEntry(locale, effect, localized);
            }
        }

        return entries;
    }

    @Override
    @NotNull
    public String getEntry(@NotNull String locale, @NotNull PotionEffectType potionEffectType) {
        String result = super.getEntry(locale, potionEffectType);
        return result == null ? potionEffectType.getName().toLowerCase(Locale.ROOT) : result;
    }

}