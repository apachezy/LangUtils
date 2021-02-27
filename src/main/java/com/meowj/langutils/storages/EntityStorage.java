package com.meowj.langutils.storages;

import com.meowj.langutils.LangUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;

public class EntityStorage extends Storage<EntityType> {

    public EntityStorage(@NotNull String fallbackLocale) {
        super(fallbackLocale);
    }

    @Override
    @Nullable
    public ConfigurationSection load(@NotNull String locale, @NotNull Configuration langConfig, @NotNull String node) {
        ConfigurationSection entries = super.load(locale, langConfig, node);

        if (entries == null) {
            return null;
        }

        String entryName;
        String localized;

        for (EntityType ent : EntityType.values()) {
            if (!ent.equals(EntityType.UNKNOWN)) {
                entryName = ent.getKey().getKey();
                localized = entries.getString(entryName);
                if (localized == null || localized.isEmpty()) {
                    if (locale.equals(fallbackLocale)) {
                        Bukkit.getLogger().severe(
                                "EntityType name "
                                        + entryName
                                        + " is missing in fallback language "
                                        + locale + ".");
                    }
                    continue;
                }
                addEntry(locale, ent, localized);
            }
        }

        return entries;
    }

    @Override
    public void addEntry(@NotNull String locale, @NotNull EntityType entityType, @NotNull String localized) {
        locale = LangUtils.fixLocale(locale);
        Map<EntityType, String> pairMap = pairStorage.computeIfAbsent(locale, s -> new EnumMap<>(EntityType.class));
        pairMap.put(entityType, localized);

        remapping(locale, pairMap);
    }

    @Override
    @NotNull
    public String getEntry(@NotNull String locale, @NotNull EntityType entityType) {
        String result = super.getEntry(locale, entityType);
        return result == null ? entityType.getKey().toString() : result;
    }

}
