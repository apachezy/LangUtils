package com.meowj.langutils.storages;

import com.meowj.langutils.LangUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Villager.Profession;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;

public class ProfessionStorage extends Storage<Profession> {

    public ProfessionStorage(@NotNull String fallbackLocale) {
        super(fallbackLocale);
    }

    @Override
    public @Nullable ConfigurationSection load(@NotNull String locale, @NotNull Configuration langConfig, @NotNull String node) {
        ConfigurationSection entries = super.load(locale, langConfig, node);

        if (entries != null) {
            String entryName;
            String localized;

            for (Profession profession : Profession.values()) {
                entryName = profession.getKey().getKey();
                localized = entries.getString(entryName);
                if (localized == null || localized.isEmpty()) {
                    if (locale.equals(fallbackLocale)) {
                        Bukkit.getLogger().severe(
                                "Villagers profession name "
                                        + entryName
                                        + " is missing in fallback language "
                                        + locale + ".");
                    }
                    continue;
                }
                addEntry(locale, profession, localized);
            }
        }

        return entries;
    }

    @Override
    public void addEntry(@NotNull String locale, @NotNull Profession profession, @NotNull String localized) {
        locale = LangUtils.fixLocale(locale);
        Map<Profession, String> pairMap = pairStorage.computeIfAbsent(locale, s -> new EnumMap<>(Profession.class));
        pairMap.put(profession, localized);

        remapping(locale, pairMap);
    }

    @Override
    @NotNull
    public String getEntry(@NotNull String locale, @NotNull Profession profession) {
        String result = super.getEntry(locale, profession);
        return result == null ? profession.getKey().toString() : result;
    }

}
