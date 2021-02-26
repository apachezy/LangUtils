package com.meowj.langutils.storages;

import com.meowj.langutils.LangUtils;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;

public class PotionStorage extends Storage<PotionType> {

    public PotionStorage(@NotNull String fallbackLocale) {
        super(fallbackLocale);
    }

    @Override
    public ConfigurationSection load(@NotNull String locale, @NotNull Configuration langConfig, @NotNull String node) {
        ConfigurationSection entries = super.load(locale, langConfig, node);

        if (entries != null) {
            String entryName;
            String localized;

            for (PotionType potionType : PotionType.values()) {
                entryName = potionType.name().toLowerCase(Locale.ROOT);
                localized = entries.getString(entryName);
                if (localized == null || localized.isEmpty()) {
                    if (locale.equals(fallbackLocale)) {
                        plugin.error(
                                "PotionType name "
                                        + entryName
                                        + " is missing in fallback language "
                                        + locale + ".");
                    }
                    continue;
                }
                addEntry(locale, potionType, localized);
            }
        }

        return entries;
    }

    @Override
    public void addEntry(@NotNull String locale, @NotNull PotionType potionType, @NotNull String localized) {
        locale = LangUtils.fixLocale(locale);
        Map<PotionType, String> pairMap = storage.computeIfAbsent(locale, s -> new EnumMap<>(PotionType.class));
        pairMap.put(potionType, localized);
        remapping(locale, pairMap);
    }

    @Override
    @NotNull
    public String getEntry(@NotNull String locale, @NotNull PotionType potionType) {
        String result = super.getEntry(locale, potionType);
        return result == null ? potionType.name().toLowerCase(Locale.ROOT) : result;
    }

}
