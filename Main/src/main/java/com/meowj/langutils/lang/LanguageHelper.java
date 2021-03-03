/*
 * Copyright (c) 2015 Jerrell Fang
 *
 * This project is Open Source and distributed under The MIT License (MIT)
 * (http://opensource.org/licenses/MIT)
 *
 * You should have received a copy of the The MIT License along with
 * this project.   If not, see <http://opensource.org/licenses/MIT>.
 *
 * Created by Meow J on 6/20/2015.
 * Refactored by ApacheZy on 2/27/2021.
 * Some methods to get the name of a item.
 * @author Meow J, ApacheZy
 */

package com.meowj.langutils.lang;

import com.meowj.langutils.LangUtils;
import com.meowj.langutils.misc.Named;
import com.meowj.langutils.misc.Util;
import com.meowj.langutils.nms.NMS;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.banner.Pattern;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TropicalFish;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.TropicalFishBucketMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.Map.Entry;

@SuppressWarnings("unused")
public class LanguageHelper {

    private static final String[] ROMAN_NUM = new String[]{
            "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"
    };

    private LanguageHelper() {
    }

    //region getItemDisplayName
    @NotNull
    public static String getItemDisplayName(@NotNull ItemStack item, @NotNull String locale) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null && meta.hasDisplayName()) {
            return meta.getDisplayName();
        }
        return getItemName(item, locale);
    }

    @NotNull
    public static String getItemDisplayName(@NotNull ItemStack item, Player player) {
        return getItemDisplayName(item, player.getLocale());
    }
    //endregion

    //region getItemName
    @NotNull
    public static String getItemName(@NotNull ItemStack item, @NotNull String locale) {
        Material material = item.getType();
        switch (material) {
            case POTION:
                return LangUtils.potionStorage.getEntry(locale, Util.getPotionType(item));

            case SPLASH_POTION:
                return LangUtils.splashPotionStorage.getEntry(locale, Util.getPotionType(item));

            case LINGERING_POTION:
                return LangUtils.lingeringPotionStorage.getEntry(locale, Util.getPotionType(item));

            case TIPPED_ARROW:
                return LangUtils.tippedArrowStorage.getEntry(locale, Util.getPotionType(item));

            case SHIELD:
                String shiledName = getColoredShiedName(item, locale);
                return shiledName == null ? getMaterialName(material, locale) : shiledName;

            case PLAYER_HEAD:
            case PLAYER_WALL_HEAD:
                String owner = Util.getPlayerHeadOwnerName(item);
                if (owner == null || owner.isEmpty()) {
                    return getMaterialName(material, locale);
                }
                return String.format(Named.PLAYER_HEAD_NAMED.getLocalized(locale), owner);

            default:
                return getMaterialName(material, locale);
        }
    }

    @NotNull
    public static String getItemName(@NotNull ItemStack item, @NotNull Player player) {
        return getItemName(item, player.getLocale());
    }
    //endregion

    //region getMaterialName
    @NotNull
    public static String getMaterialName(@NotNull Material material, @NotNull String locale) {
        return LangUtils.materialStorage.getEntry(locale, material);
    }

    @NotNull
    public static String getMaterialName(@NotNull Material material, @NotNull Player player) {
        return getMaterialName(material, player.getLocale());
    }
    //endregion

    //region getBiomeName
    @NotNull
    public static String getBiomeName(@NotNull Biome biome, @NotNull String locale) {
        return LangUtils.biomeStorage.getEntry(locale, biome);
    }

    @NotNull
    public static String getBiomeName(@NotNull Biome biome, @NotNull Player player) {
        return getBiomeName(biome, player.getLocale());
    }
    //endregion

    //region getEntityDisplayName
    @NotNull
    public static String getEntityDisplayName(@NotNull Entity entity, @NotNull String locale) {
        String dspName = entity.getCustomName();
        return dspName == null ? getEntityName(entity, locale) : dspName;
    }

    @NotNull
    public static String getEntityDisplayName(@NotNull Entity entity, @NotNull Player player) {
        return getEntityDisplayName(entity, player.getLocale());
    }
    //endregion

    //region getEntityName
    @NotNull
    public static String getEntityName(@NotNull Entity entity, @NotNull String locale) {
        EntityType type = entity.getType();
        if (type == EntityType.UNKNOWN) {
            String result = Named.UNKNOWN_ENTITY.getLocalized(locale);
            return String.format(result, entity.getEntityId());
        }
        return LangUtils.entityStorage.getEntry(locale, type);
    }

    @NotNull
    public static String getEntityName(@NotNull Entity entity, @NotNull Player player) {
        return getEntityName(entity, player.getLocale());
    }

    @NotNull
    public static String getEntityName(@NotNull EntityType entityType, @NotNull String locale) {
        if (entityType == EntityType.UNKNOWN) {
            String localized = Named.UNKNOWN_ENTITY.getLocalized(locale);
            return String.format(localized, entityType.name());
        }
        return LangUtils.entityStorage.getEntry(locale, entityType);
    }

    @NotNull
    public static String getEntityName(@NotNull EntityType entityType, @NotNull Player player) {
        return getEntityName(entityType, player.getLocale());
    }
    //endregion

    //region getEnchantmentLevelName
    @NotNull
    public static String getEnchantmentLevelName(int level, @NotNull String locale) {
        if (level < 1) {
            return "";
        }
        if (level <= ROMAN_NUM.length) {
            return ROMAN_NUM[level - 1];
        }
        return "enchantment.level." + level;
    }

    @NotNull
    public static String getEnchantmentLevelName(int level, @NotNull Player player) {
        return getEnchantmentLevelName(level, player.getLocale());
    }
    //endregion

    //region getEnchantmentName
    @NotNull
    public static String getEnchantmentName(@NotNull Enchantment enchant, @NotNull String locale) {
        return LangUtils.enchantStorage.getEntry(locale, enchant);
    }

    @NotNull
    public static String getEnchantmentName(@NotNull Enchantment enchant, @NotNull Player player) {
        return getEnchantmentName(enchant, player.getLocale());
    }
    //endregion

    //region getEnchantmentDisplayName
    @NotNull
    public static String getEnchantmentDisplayName(@NotNull Enchantment enchant, int level, @NotNull String locale) {
        String enName = getEnchantmentName(enchant, locale);
        String lvName = getEnchantmentLevelName(level, locale);
        return enName + (lvName.length() > 0 ? " " + lvName : "");
    }

    @NotNull
    public static String getEnchantmentDisplayName(@NotNull Enchantment enchant, int level, @NotNull Player player) {
        return getEnchantmentDisplayName(enchant, level, player.getLocale());
    }

    @NotNull
    public static String getEnchantmentDisplayName(@NotNull Entry<Enchantment, Integer> entry, @NotNull String locale) {
        return getEnchantmentDisplayName(entry.getKey(), entry.getValue(), locale);
    }

    @NotNull
    public static String getEnchantmentDisplayName(@NotNull Entry<Enchantment, Integer> entry, @NotNull Player player) {
        return getEnchantmentDisplayName(entry.getKey(), entry.getValue(), player);
    }
    //endregion

    //region getPotionName
    @NotNull
    public static String getPotionName(@NotNull PotionType potionType, @NotNull String locale) {
        return LangUtils.potionStorage.getEntry(locale, potionType);
    }

    @NotNull
    public static String getPotionName(@NotNull PotionType potionType, @NotNull Player player) {
        return getPotionName(potionType, player.getLocale());
    }
    //endregion

    //region getSplashPotionName
    @NotNull
    public static String getSplashPotionName(@NotNull PotionType potionType, @NotNull String locale) {
        return LangUtils.splashPotionStorage.getEntry(locale, potionType);
    }

    @NotNull
    public static String getSplashPotionName(@NotNull PotionType potionType, @NotNull Player player) {
        return getSplashPotionName(potionType, player.getLocale());
    }
    //endregion

    //region getLingeringPotionName
    @NotNull
    public static String getLingeringPotionName(@NotNull PotionType potionType, @NotNull String locale) {
        return LangUtils.lingeringPotionStorage.getEntry(locale, potionType);
    }

    @NotNull
    public static String getLingeringPotionName(@NotNull PotionType potionType, @NotNull Player player) {
        return getLingeringPotionName(potionType, player.getLocale());
    }
    //endregion

    //region getTippedArrowName
    @NotNull
    public static String getTippedArrowName(@NotNull PotionType potionType, @NotNull String locale) {
        return LangUtils.tippedArrowStorage.getEntry(locale, potionType);
    }

    @NotNull
    public static String getTippedArrowName(@NotNull PotionType potionType, @NotNull Player player) {
        return getTippedArrowName(potionType, player.getLocale());
    }
    //endregion

    //region getPotionEffectName
    @NotNull
    public static String getPotionEffectName(@NotNull PotionEffectType effectType, @NotNull String locale) {
        return LangUtils.effectStorage.getEntry(locale, effectType);
    }

    @NotNull
    public static String getPotionEffectName(@NotNull PotionEffectType effectType, @NotNull Player player) {
        return getPotionEffectName(effectType, player.getLocale());
    }
    //endregion

    //region getEffectAmplifierName
    @NotNull
    public static String getEffectAmplifierName(int amplifier, @NotNull String locale) {
        if (amplifier < 1) {
            return "";
        }
        if (amplifier < ROMAN_NUM.length) {
            return ROMAN_NUM[amplifier];
        }
        return "potion.potency." + amplifier;
    }

    @NotNull
    public static String getEffectAmplifierName(int amplifier, @NotNull Player player) {
        return getEffectAmplifierName(amplifier, player.getLocale());
    }
    //endregion

    //region getPotionEffectDisplay
    @NotNull
    public static String getPotionEffectDisplay(@NotNull PotionEffect effect, @NotNull String locale) {
        String effecName = getPotionEffectName(effect.getType(), locale);
        String amplifier = getEffectAmplifierName(effect.getAmplifier(), locale);

        String localized;

        if (amplifier.length() > 0) {
            localized = Named.POTION_WITH_AMPLIFIER.getLocalized(locale);
            effecName = String.format(localized, effecName, amplifier);
        }

        int duration = effect.getDuration();
        if (duration > 20) {
            localized = Named.POTION_WITH_DURATION.getLocalized(locale);
            effecName = String.format(localized, effecName, Util.getShortTime(duration));
        }

        return effecName;
    }

    public static String getPotionEffectDisplay(@NotNull PotionEffect effect, @NotNull Player player) {
        return getPotionEffectDisplay(effect, player.getLocale());
    }
    //endregion

    //region getTropicalFishTypeName
    @NotNull
    public static String getTropicalFishTypeName(@NotNull TropicalFish.Pattern pattern, @NotNull String locale) {
        return LangUtils.tropicalFishTypeStorage.getEntry(locale, pattern);
    }

    @NotNull
    public static String getTropicalFishTypeName(@NotNull TropicalFish.Pattern pattern, @NotNull Player player) {
        return getTropicalFishTypeName(pattern, player.getLocale());
    }
    //endregion

    //region getPredefinedTropicalFishName
    @Nullable
    public static String getPredefinedTropicalFishName(@NotNull TropicalFishBucketMeta meta, @NotNull String locale) {
        Integer variant = Util.getTropicalFishVariant(meta);
        if (variant != null) {
            return LangUtils.tropicalFishNameStorage.getEntry(locale, variant);
        }
        return null;
    }

    @Nullable
    public static String getPredefinedTropicalFishName(@NotNull TropicalFishBucketMeta meta, @NotNull Player player) {
        return getPredefinedTropicalFishName(meta, player.getLocale());
    }
    //endregion

    //region getDyeColorName
    @NotNull
    public static String getDyeColorName(@NotNull DyeColor color, @NotNull String locale) {
        String result = LangUtils.dyeColorStorage.getEntry(locale, color);
        return result != null ? result : color.name().toLowerCase(Locale.ROOT);
    }

    @NotNull
    public static String getDyeColorName(@NotNull DyeColor color, @NotNull Player player) {
        return getDyeColorName(color, player.getLocale());
    }
    //endregion

    //region getVillagerLevelName
    @NotNull
    public static String getVillagerLevelName(int level, @NotNull String locale) {
        String result = LangUtils.villagerLevelStorage.getEntry(locale, level);
        return result == null ? "merchant.level." + level : result;
    }

    @NotNull
    public static String getVillagerLevelName(int level, @NotNull Player player) {
        return getVillagerLevelName(level, player.getLocale());
    }
    //endregion

    //region getVillagerProfessionName
    @NotNull
    public static String getVillagerProfessionName(@NotNull Profession profession, @NotNull String locale) {
        return LangUtils.professionStorage.getEntry(locale, profession);
    }

    @NotNull
    public static String getVillagerProfessionName(@NotNull Profession profession, @NotNull Player player) {
        return getVillagerProfessionName(profession, player.getLocale());
    }
    //endregion

    //region getBannerPatterName
    @NotNull
    public static String getBannerPatternName(@NotNull Pattern pattern, @NotNull String locale) {
        String result = LangUtils.bannerPatternStorage.getEntry(locale, Util.getPatternMixedCode(pattern));

        if (result == null || result.isEmpty()) {
            return pattern.getColor().name().toLowerCase(Locale.ROOT) + "_"
                    + pattern.getPattern().name().toLowerCase(Locale.ROOT);
        }

        return result;
    }

    @NotNull
    public static String getBannerPatternName(@NotNull Pattern pattern, @NotNull Player player) {
        return getBannerPatternName(pattern, player.getLocale());
    }
    //endregion

    //region getColoredShiedName
    @Nullable
    public static String getColoredShiedName(@NotNull ItemStack shield, @NotNull String locale) {
        Integer colorOrdinal = NMS.getShieldBaseColorOrdinal(shield);
        if (colorOrdinal == null) {
            return null;
        }

        @SuppressWarnings("deprecation")
        DyeColor color = DyeColor.getByWoolData(colorOrdinal.byteValue());
        if (color == null) {
            return null;
        }

        return LangUtils.coloredShiedStorage.getEntry(locale, color);
    }

    @Nullable
    public static String getColoredShiedName(@NotNull ItemStack shield, @NotNull Player player) {
        return getColoredShiedName(shield, player.getLocale());
    }
    //endregion
}