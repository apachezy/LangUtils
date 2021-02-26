package com.meowj.langutils.storages;

import com.meowj.langutils.LangUtils;
import com.meowj.langutils.misc.Named;
import com.meowj.langutils.misc.Util;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;

public class NamedStorage extends Storage<Named> {

    public NamedStorage(@NotNull String fallbackLocale) {
        super(fallbackLocale);
    }

    @Override
    @Nullable
    public ConfigurationSection load(@NotNull String locale, @NotNull Configuration langConfig, @NotNull String node) {
        ConfigurationSection entries = super.load(locale, langConfig, node);

        if (entries != null) {
            String entryName;
            String localized;

            for (Named named : Named.values()) {
                entryName = named.name().toLowerCase(Locale.ROOT);
                localized = entries.getString(entryName);
                if (localized == null || localized.isEmpty()) {
                    if (locale.equals(fallbackLocale)) {
                        plugin.error(
                                "Named entry "
                                        + entryName
                                        + " is missing in fallback language "
                                        + locale + ".");
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
        Map<Named, String> pairMap = storage.computeIfAbsent(locale, s -> new EnumMap<>(Named.class));
        pairMap.put(named, localized);
        remapping(locale, pairMap);
    }


}
