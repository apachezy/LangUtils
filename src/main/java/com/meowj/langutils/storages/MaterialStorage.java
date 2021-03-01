package com.meowj.langutils.storages;

import com.meowj.langutils.LangUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.Map;
import java.util.logging.Level;

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
                        Bukkit.getLogger().log(
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
        // 试图获取 Legacy Material 的 NamespacedKey 会出现异常
        return result == null ? material.getKey().toString() : result;
    }

}