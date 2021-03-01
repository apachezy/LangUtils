package com.meowj.langutils.storages;

import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

public class IntegerStrage extends Storage<Integer> {

    public IntegerStrage(@NotNull String fallbackLocale) {
        super(fallbackLocale);
    }

    @Override
    public ConfigurationSection load(@NotNull String locale, @NotNull Configuration langConfig, @NotNull String node) {
        ConfigurationSection entries = super.load(locale, langConfig, node);

        if (entries != null) {
            String localized;

            for (String key : entries.getKeys(false)) {
                try {
                    localized = entries.getString(key);
                    if (localized == null || localized.isEmpty()) {

                        if (locale.equals(fallbackLocale)) {
                            Bukkit.getLogger().log(
                                    Level.SEVERE,
                                    "Null value in {0}.{1} in fallback language {2}.",
                                    new String[]{node, key, locale});
                        }

                        continue;
                    }

                    addEntry(locale,Integer.parseInt(key), localized);
                } catch (NumberFormatException ignored) {
                    Bukkit.getLogger().log(
                            Level.SEVERE,
                            "Wrong key in {0}.{1} in fallback language {2}.",
                            new String[]{node, key, locale});
                }
            }
        }

        return entries;
    }

}