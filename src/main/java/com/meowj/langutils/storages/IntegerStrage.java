package com.meowj.langutils.storages;

import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

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
                    int anInt = Integer.parseInt(key);
                    localized = entries.getString(key);
                    if (localized == null || localized.isEmpty()) {
                        if (locale.equals(fallbackLocale)) {
                            Bukkit.getLogger().severe(
                                    node + " in fallback language "
                                            + locale
                                            + " is missing a value corresponding to "
                                            + anInt);
                        }
                        continue;
                    }
                    addEntry(locale, anInt, localized);
                } catch (NumberFormatException ignored) {
                    // nothiing
                }
            }
        }

        return entries;
    }

}
