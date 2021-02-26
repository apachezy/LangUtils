package com.meowj.langutils.storages;

import com.meowj.langutils.LangUtils;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.Map;

public class MaterialStorage extends Storage<Material> {

    public MaterialStorage(@NotNull String fallbackLocale) {
        super(fallbackLocale);
    }

    @Override
    public ConfigurationSection load(@NotNull String locale, @NotNull Configuration langConfig, @NotNull String node) {
        ConfigurationSection entries = super.load(locale, langConfig, node);

        if (entries != null) {
            String entryName;
            String localized;

            for (Material material : Material.values()) {
                entryName = material.getKey().getKey();
                localized = entries.getString(entryName);
                if (localized == null || localized.isEmpty()) {
                    if (locale.equals(fallbackLocale)) {
                        plugin.error(
                                "Material name "
                                        + entryName
                                        + " is missing in fallback language "
                                        + locale + ".");
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
        Map<Material, String> pairMap = storage.computeIfAbsent(locale, s -> new EnumMap<>(Material.class));
        pairMap.put(material, localized);
        remapping(locale, pairMap);
    }

    @Override
    @NotNull
    public String getEntry(@NotNull String locale, @NotNull Material material) {
        String result = super.getEntry(locale, material);
        return result == null ? material.getKey().toString() : result;
    }

}
