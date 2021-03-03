package com.meowj.langutils.storages;

import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;
import java.util.logging.Logger;

public class IntegerStorage extends Storage<Integer> {

    public IntegerStorage(@NotNull String fallbackLocale) {
        super(fallbackLocale);
    }

    @Override
    public ConfigurationSection load(@NotNull String locale, @NotNull Configuration langConfig,
                                     @NotNull String config, @NotNull Logger logger) {
        ConfigurationSection entries = super.load(locale, langConfig, config, logger);

        if (entries != null) {
            for (String key : entries.getKeys(false)) {
                try {
                    String localized = entries.getString(key);
                    if (localized == null || localized.isEmpty()) {

                        if (locale.equals(fallbackLocale)) {
                            logger.log(
                                    Level.SEVERE,
                                    "Null value in {0}.{1} in fallback language {2}.",
                                    new String[]{config, key, locale});
                        }

                        continue;
                    }

                    addEntry(locale, Integer.parseInt(key), localized);
                } catch (NumberFormatException ignored) {
                    Bukkit.getLogger().log(
                            Level.SEVERE,
                            "Wrong key in {0}.{1} in fallback language {2}.",
                            new String[]{config, key, locale});
                }
            }
        }

        return entries;
    }

}