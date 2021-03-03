package com.meowj.langutils.storages;

import com.meowj.langutils.LangUtils;
import org.bukkit.block.Biome;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BiomeStorage extends Storage<Biome> {

    public BiomeStorage(@NotNull String fallbackLocale) {
        super(fallbackLocale);
    }

    @Override
    @Nullable
    public ConfigurationSection load(@NotNull String locale, @NotNull Configuration langConfig,
                                     @NotNull String config, @NotNull Logger logger) {
        ConfigurationSection entries = super.load(locale, langConfig, config, logger);

        if (entries != null) {
            for (Biome biome : Biome.values()) {

                String entryName = biome.getKey().getKey();
                String localized = entries.getString(entryName);

                if (localized == null || localized.isEmpty()) {
                    if (locale.equals(fallbackLocale)) {
                        logger.log(
                                Level.SEVERE,
                                "Biome name {0} is missing in fallback language {1}.",
                                new String[]{entryName, locale});
                    }
                    continue;
                }

                addEntry(locale, biome, localized);
            }
        }

        return entries;
    }

    @Override
    public void addEntry(@NotNull String locale, @NotNull Biome biome, @NotNull String localized) {
        locale = LangUtils.fixLocale(locale);
        Map<Biome, String> pairMap = pairStorage.computeIfAbsent(locale, s -> new EnumMap<>(Biome.class));
        pairMap.put(biome, localized);

        remapping(locale, pairMap);
    }

    @Override
    @NotNull
    public String getEntry(@NotNull String locale, @NotNull Biome biome) {
        String result = super.getEntry(locale, biome);
        return result == null ? biome.getKey().toString() : result;
    }

}