package com.meowj.langutils.storages;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;

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
                        plugin.error(
                                "Enchantment name "
                                        + entryName
                                        + " is missing in fallback language "
                                        + locale + ".");
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
