package com.meowj.langutils.storages;

import com.meowj.langutils.LangUtils;
import com.meowj.langutils.misc.Named;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NamedStorage extends Storage<Named> {

    public NamedStorage(@NotNull String fallbackLocale) {
        super(fallbackLocale);
    }

    @Override
    @Nullable
    public ConfigurationSection load(@NotNull String locale, @NotNull Configuration langConfig,
                                     @NotNull String config, @NotNull Logger logger) {
        ConfigurationSection entries = super.load(locale, langConfig, config, logger);

        if (entries != null) {
            for (Named named : Named.values()) {

                String entryName = named.name().toLowerCase(Locale.ROOT);
                String localized = entries.getString(entryName);

                if (localized == null || localized.isEmpty()) {
                    if (locale.equals(fallbackLocale)) {
                        logger.log(
                                Level.SEVERE,
                                "Named entry {0} is missing in fallback language {1}.",
                                new String[]{entryName, locale});
                    }
                    continue;
                }

                addEntry(locale, named, localized);
            }
        }

        return entries;
    }

    @Override
    public void addEntry(@NotNull String locale, @NotNull Named named, @NotNull String localized) {
        locale = LangUtils.fixLocale(locale);
        Map<Named, String> pairMap = pairStorage.computeIfAbsent(locale, s -> new EnumMap<>(Named.class));
        pairMap.put(named, localized);

        remapping(locale, pairMap);
    }

}