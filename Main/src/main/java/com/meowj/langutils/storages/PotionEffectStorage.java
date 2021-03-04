package com.meowj.langutils.storages;

import com.meowj.langutils.misc.Remaper;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.logging.Level;

public class PotionEffectStorage extends Storage<PotionEffectType> {

    public PotionEffectStorage(@NotNull String fallbackLocale) {
        super(fallbackLocale);
    }

    @Override
    public ConfigurationSection load(@NotNull String locale, @NotNull Configuration langConfig,
                                     @NotNull String config, @Nullable Remaper remaper) {
        ConfigurationSection entries = super.load(locale, langConfig, config, remaper);

        if (entries != null) {
            for (PotionEffectType effect : PotionEffectType.values()) {

                String entryName = effect.getName().toLowerCase(Locale.ROOT);
                String localized = entries.getString(entryName);

                if (localized == null || localized.isEmpty()) {
                    if (locale.equals(fallbackLocale)) {
                        Bukkit.getLogger().log(
                                Level.SEVERE,
                                "PotionEffectType name {0} is missing in fallback language {1}.",
                                new String[]{entryName, locale});
                    }
                    continue;
                }

                addEntry(locale, effect, localized, remaper);
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