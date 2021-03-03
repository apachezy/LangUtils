package com.meowj.langutils.storages;

import com.meowj.langutils.LangUtils;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MaterialStorage extends Storage<Material> {

    public MaterialStorage(@NotNull String fallbackLocale) {
        super(fallbackLocale);
    }

    @Override
    public ConfigurationSection load(@NotNull String locale, @NotNull Configuration langConfig,
                                     @NotNull String node,   @NotNull Logger logger) {
        ConfigurationSection entries = super.load(locale, langConfig, node, logger);

        if (entries != null) {
            for (Material material : Material.values()) {

                String entryName = material.getKey().getKey();
                String localized = entries.getString(entryName);

                if (localized == null || localized.isEmpty()) {
                    if (locale.equals(fallbackLocale)) {
                        logger.log(
                                Level.SEVERE,
                                "Material name {0} is missing in fallback language {1}.",
                                new String[]{entryName, locale});
                    }
                    continue;
                }

                addEntry(locale, material, localized);
            }
        }

        return entries;
    }

    @Override
    public void addEntry(@NotNull String locale, @NotNull Material material, @NotNull String localized) {
        locale = LangUtils.fixLocale(locale);
        Map<Material, String> pairMap = pairStorage.computeIfAbsent(locale, s -> new EnumMap<>(Material.class));
        pairMap.put(material, localized);

        remapping(locale, pairMap);
    }

    @Override
    @NotNull
    public String getEntry(@NotNull String locale, @NotNull Material material) {
        String result = super.getEntry(locale, material);
        return result == null ? material.name().toLowerCase(Locale.ROOT) : result;
    }

}