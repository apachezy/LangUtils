package com.meowj.langutils.storages;

import com.meowj.langutils.misc.Remaper;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Level;

public class IntegerStorage extends Storage<Integer> {

    public IntegerStorage(@NotNull String fallbackLocale) {
        super(fallbackLocale);
    }

    @Override
    @Nullable
    public ConfigurationSection load(@NotNull String locale, @NotNull Configuration langConfig,
                                     @NotNull String config, @Nullable Remaper remaper) {

        ConfigurationSection entries = super.load(locale, langConfig, config, remaper);

        if (entries != null) {
            for (String key : entries.getKeys(false)) {
                try {
                    String localized = entries.getString(key);
                    if (localized == null || localized.isEmpty()) {

                        if (locale.equals(fallbackLocale)) {
                            Bukkit.getLogger().log(
                                    Level.SEVERE,
                                    "Null value in {0}.{1} in fallback language {2}.",
                                    new String[]{config, key, locale});
                        }

                        continue;
                    }

                    addEntry(locale, Integer.parseInt(key), localized, remaper);
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