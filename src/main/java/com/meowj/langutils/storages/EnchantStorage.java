package com.meowj.langutils.storages;

import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

public class EnchantStorage extends Storage<Enchantment> {

    public EnchantStorage(@NotNull String fallbackLocale) {
        super(fallbackLocale);
    }

    @Override
    public ConfigurationSection load(@NotNull String locale, @NotNull Configuration langConfig, @NotNull String node) {
        ConfigurationSection entries = super.load(locale, langConfig, node);

        if (entries != null) {
            String entryName;
            String localized;

            for (Enchantment enchant : Enchantment.values()) {
                entryName = enchant.getKey().getKey();
                localized = entries.getString(entryName);

                if (localized == null || localized.isEmpty()) {
                    if (locale.equals(fallbackLocale)) {
                        Bukkit.getLogger().log(
                                Level.SEVERE,
                                "Enchantment name {0} is missing in fallback language {1}.",
                                new String[]{entryName, locale});
                    }
                    continue;
                }

                addEntry(locale, enchant, localized);
            }
        }

        return entries;
    }

    @Override
    @NotNull
    public String getEntry(@NotNull String locale, @NotNull Enchantment enchant) {
        String result = super.getEntry(locale, enchant);
        return result == null ? enchant.getKey().toString() : result;
    }

}