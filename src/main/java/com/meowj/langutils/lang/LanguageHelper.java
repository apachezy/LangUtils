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
import org.bukkit.entity.Villager.Career;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.TropicalFishBucketMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;

@SuppressWarnings("unused")
public class LanguageHelper {

    private static final String[] ROMAN_NUM = new String[]{
            "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"
    };

    private LanguageHelper() {
    }

    /**
     * Get the display name of the item. If the item does not have a display
     * name, {@link #getItemName(ItemStack, String)} will be called to get the
     * localized item name.
     *
     * @param item   The ItemStack.
     * @param locale This locale will be used to get the localized name.
     * @return Returns the display name or localized name of the item.
     */
    public static @NotNull String getItemDisplayName(@NotNull ItemStack item, @NotNull String locale) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null && meta.hasDisplayName()) {
            return meta.getDisplayName();
        }
        return getItemName(item, locale);
    }

    /**
     * This method is similar to {@link #getItemDisplayName(ItemStack,
     * String)}.
     */
    public static @NotNull String getItemDisplayName(@NotNull ItemStack item, Player player) {
        return getItemDisplayName(item, player.getLocale());
    }

    /**
     * Get the localized name of the item so that players can see the familiar
     * name.
     *
     * @param item   The name of the ItemStack will be translated.
     * @param locale This locale will be used for translation.
     * @return Returns the localized name of the item.
     */
    public static @NotNull String getItemName(@NotNull ItemStack item, @NotNull String locale) {
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
                String name = getColoredShiedName(item, locale);
                return name == null ? getMaterialName(material, locale) : name;

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

    /**
     * This method is similar to {@link #getItemName(ItemStack, String)}.
     */
    public static @NotNull String getItemName(@NotNull ItemStack item, @NotNull Player player) {
        return getItemName(item, player.getLocale());
    }

    /**
     * Get the localized name of the material so that players can see the
     * familiar name.
     *
     * @param material The name of the Material will be translated.
     * @param locale   This locale will be used for translation.
     * @return Returns the localized name of the Material.
     */
    public static @NotNull String getMaterialName(@NotNull Material material, @NotNull String locale) {
        return LangUtils.materialStorage.getEntry(locale, material);
    }

    /**
     * This method is similar to {@link #getMaterialName(Material, String)}.
     */
    public static @NotNull String getMaterialName(@NotNull Material material, @NotNull Player player) {
        return getMaterialName(material, player.getLocale());
    }

    /**
     * Get the localized name of the biome so that players can see the familiar
     * name.
     *
     * @param biome  The name of the Biome will be translated.
     * @param locale This locale will be used for translation.
     * @return Returns the localized name of the Biome.
     */
    public static @NotNull String getBiomeName(@NotNull Biome biome, @NotNull String locale) {
        return LangUtils.biomeStorage.getEntry(locale, biome);
    }

    /**
     * This method is similar to {@link #getBiomeName(Biome, String)}.
     */
    public static @NotNull String getBiomeName(@NotNull Biome biome, @NotNull Player player) {
        return getBiomeName(biome, player.getLocale());
    }

    /**
     * Get the display name of the entity. If the given entity does not have a
     * display name, {@link #getEntityName(Entity, String)} will be called to
     * translate the entity name.
     *
     * @param entity The entity whose display name is to be obtained.
     * @param locale This locale will be used to get the localized name.
     * @return Returns the display name or localized name of the Entity.
     */
    public static @NotNull String getEntityDisplayName(@NotNull Entity entity, @NotNull String locale) {
        String dspName = entity.getCustomName();
        return dspName == null ? getEntityName(entity, locale) : dspName;
    }

    /**
     * This method is similar to {@link #getEntityDisplayName(Entity, String)}.
     */
    public static @NotNull String getEntityDisplayName(@NotNull Entity entity, @NotNull Player player) {
        return getEntityDisplayName(entity, player.getLocale());
    }

    /**
     * Get the localized name of the entity so that players can see the familiar
     * name. If the correct entity name cannot be found, an "Unknown Entity" may
     * be returned.
     *
     * @param entity The name of the Entity will be translated.
     * @param locale This locale will be used for translation.
     * @return Returns the localized name of the Entity.
     */
    public static @NotNull String getEntityName(@NotNull Entity entity, @NotNull String locale) {
        String name = LangUtils.entityStorage.getEntry(locale, entity.getType());
        if (name == null) {
            String temp = Named.UNKNOWN_ENTITY.getLocalized(locale);
            name = String.format(temp, entity.getEntityId() + "(" + entity.getName() + ")");
        }
        return name;
    }

    /**
     * This method is similar to {@link #getEntityName(Entity, String)}.
     */
    public static @NotNull String getEntityName(@NotNull Entity entity, @NotNull Player player) {
        return getEntityName(entity, player.getLocale());
    }

    /**
     * Get the localized name of the entity type so that players can see the
     * familiar name. If the correct entity name cannot be found, an "Unknown
     * Entity" may be returned.
     *
     * @param entityType The name of the EntityType will be translated.
     * @param locale     This locale will be used for translation.
     * @return Returns the localized name of the EntityType.
     */
    public static @NotNull String getEntityName(@NotNull EntityType entityType, @NotNull String locale) {
        String name = LangUtils.entityStorage.getEntry(locale, entityType);
        if (name == null) {
            String temp = Named.UNKNOWN_ENTITY.getLocalized(locale);
            name = String.format(temp, entityType);
        }
        return name;
    }

    /**
     * This method is similar to {@link #getEntityName(EntityType, String)}.
     */
    public static @NotNull String getEntityName(@NotNull EntityType entityType, @NotNull Player player) {
        return getEntityName(entityType, player.getLocale());
    }

    /**
     * Get the enchantment level indicated by Roman numerals. If the enchantment
     * level exceeds 10, It will return a localized key similar to
     * "enchantment.level.11".
     *
     * @param level  The enchantment level.
     * @param locale The locale will be ignored.
     * @return Returns the enchantment level indicated by Roman numerals or the
     *         localized key of the enchantment level.
     */
    public static @NotNull String getEnchantmentLevelName(int level, @NotNull String locale) {
        if (level < 1) {
            return "";
        }
        if (level <= ROMAN_NUM.length) {
            return ROMAN_NUM[level - 1];
        }
        return "enchantment.level." + level;
    }

    /**
     * This method is similar to {@link #getEnchantmentLevelName(int, String)}.
     */
    public static @NotNull String getEnchantmentLevelName(int level, @NotNull Player player) {
        return getEnchantmentLevelName(level, player.getLocale());
    }

    /**
     * Get the localized name of the enchantment so that players can see the
     * familiar name. If the correct Enchantment name cannot be found, an
     * "Unknown Enchantment" may be returned.
     *
     * @param enchant The name of the Enchantment will be translated.
     * @param locale  This locale will be used for translation.
     * @return Returns the localized name of the Enchantment.
     */
    public static @NotNull String getEnchantmentName(@NotNull Enchantment enchant, @NotNull String locale) {
        String name = LangUtils.enchantStorage.getEntry(locale, enchant);
        if (name == null) {
            String temp = Named.UNKNOWN_ENCHANTMENT.getLocalized(locale);
            name = String.format(temp, enchant.getKey().getKey());
        }
        return name;
    }

    /**
     * This method is similar to {@link #getEnchantmentName(Enchantment,
     * String)}.
     */
    public static @NotNull String getEnchantmentName(@NotNull Enchantment enchant, @NotNull Player player) {
        return getEnchantmentName(enchant, player.getLocale());
    }

    /**
     * Combine the names and levels of the localized enchantments to make them
     * look like the original ones in the game.
     *
     * @param enchant The name of the Enchantment will be translated.
     * @param level   The enchantment level.
     * @param locale  This locale will be used for translation.
     * @return Returns the name and level of the enchantment that can be used as
     *         a display.
     */
    public static @NotNull String getEnchantmentDisplayName(@NotNull Enchantment enchant, int level, @NotNull String locale) {
        String enName = getEnchantmentName(enchant, locale);
        String lvName = getEnchantmentLevelName(level, locale);
        return enName + (lvName.length() > 0 ? " " + lvName : "");
    }

    /**
     * This method is similar to {@link #getEnchantmentDisplayName(Enchantment,
     * int, String)}.
     */
    public static @NotNull String getEnchantmentDisplayName(@NotNull Enchantment enchant, int level, @NotNull Player player) {
        return getEnchantmentDisplayName(enchant, level, player.getLocale());
    }

    /**
     * This method is similar to {@link #getEnchantmentDisplayName(Enchantment,
     * int, String)}.
     */
    public static @NotNull String getEnchantmentDisplayName(@NotNull Entry<Enchantment, Integer> entry, @NotNull String locale) {
        return getEnchantmentDisplayName(entry.getKey(), entry.getValue(), locale);
    }

    /**
     * This method is similar to {@link #getEnchantmentDisplayName(Entry,
     * String)}.
     */
    public static @NotNull String getEnchantmentDisplayName(@NotNull Entry<Enchantment, Integer> entry, @NotNull Player player) {
        return getEnchantmentDisplayName(entry.getKey(), entry.getValue(), player);
    }

    /**
     * Get the localized name of the drinking potion bottle.
     *
     * @param potionType The PotionType of drinking potion bottle.
     * @param locale     The target language.
     * @return The translated name of the potion bottle.
     */
    public static @NotNull String getPotionName(@NotNull PotionType potionType, @NotNull String locale) {
        return LangUtils.potionStorage.getEntry(locale, potionType);
    }

    /**
     * This method is similar to {@link #getPotionName(PotionType, String)}.
     */
    public static @NotNull String getPotionName(@NotNull PotionType potionType, @NotNull Player player) {
        return getPotionName(potionType, player.getLocale());
    }

    /**
     * Get the localized name of the splash potion bottle.
     *
     * @param potionType The PotionType of splash potion bottle.
     * @param locale     The target language.
     * @return The translated name of the potion bottle.
     */
    public static @NotNull String getSplashPotionName(@NotNull PotionType potionType, @NotNull String locale) {
        return LangUtils.splashPotionStorage.getEntry(locale, potionType);
    }

    /**
     * This method is similar to {@link #getSplashPotionName(PotionType,
     * String)}.
     */
    public static @NotNull String getSplashPotionName(@NotNull PotionType potionType, @NotNull Player player) {
        return getSplashPotionName(potionType, player.getLocale());
    }

    /**
     * Get the localized name of the lingering potion bottle.
     *
     * @param potionType The PotionType of lingering potion bottle.
     * @param locale     The target language.
     * @return The translated name of the potion bottle.
     */
    public static @NotNull String getLingeringPotionName(@NotNull PotionType potionType, @NotNull String locale) {
        return LangUtils.lingeringPotionStorage.getEntry(locale, potionType);
    }

    /**
     * This method is similar to {@link #getLingeringPotionName(PotionType,
     * String)}.
     */
    public static @NotNull String getLingeringPotionName(@NotNull PotionType potionType, @NotNull Player player) {
        return getLingeringPotionName(potionType, player.getLocale());
    }

    /**
     * Get the localized name of the TippedArrow.
     *
     * @param potionType The PotionType of the TippedArrow.
     * @param locale     The target language.
     * @return The translated name of the TippedArrow.
     */
    public static @NotNull String getTippedArrowName(@NotNull PotionType potionType, @NotNull String locale) {
        return LangUtils.tippedArrowStorage.getEntry(locale, potionType);
    }

    /**
     * This method is similar to {@link #getTippedArrowName(PotionType,
     * String)}.
     */
    public static @NotNull String getTippedArrowName(@NotNull PotionType potionType, @NotNull Player player) {
        return getTippedArrowName(potionType, player.getLocale());
    }

    /**
     * Translate the name of the base effect of the potion. If the PotionType
     * has no base effect, the translation of "No Effects" is returned. e.g.
     * Water Bottle, Mundane Potion.
     *
     * @param potionType The basic effect of PotionType.
     * @param locale     The target language.
     * @return Return the translation result.
     */
    public static @NotNull String getPotionBaseEffectName(@NotNull PotionType potionType, @NotNull String locale) {
        PotionEffectType effectType = potionType.getEffectType();
        if (effectType == null) {
            return Named.NONE_EFFECTS.getLocalized(locale);
        }
        return getPotionEffectName(effectType, locale);
    }

    /**
     * This method is similar to {@link #getPotionBaseEffectName(PotionType,
     * String)}
     */
    public static @NotNull String getPotionBaseEffectName(@NotNull PotionType potionType, @NotNull Player player) {
        return getPotionBaseEffectName(potionType, player.getLocale());
    }

    /**
     * Get the localized name of the PotionEffectType.
     *
     * @param effectType The PotionEffectType.
     * @param locale     The target language.
     * @return The translated name of the PotionEffectType.
     */
    public static @NotNull String getPotionEffectName(@NotNull PotionEffectType effectType, @NotNull String locale) {
        String name = LangUtils.effectStorage.getEntry(locale, effectType);
        if (name == null) {
            String temp = Named.UNKNOWN_EFFECT.getLocalized(locale);
            name = String.format(temp, effectType.getName());
        }
        return name;
    }

    /**
     * This method is similar to {@link #getPotionEffectName(PotionEffectType,
     * String)}
     */
    public static @NotNull String getPotionEffectName(@NotNull PotionEffectType effectType, @NotNull Player player) {
        return getPotionEffectName(effectType, player.getLocale());
    }

    /**
     * Get the "level" of the potion effect. This method is only meaningful for
     * customizing potion effects, because the level of the potion basic effects
     * is predefined.
     *
     * @param amplifier The potion effect "Level"
     * @param locale    The target language.
     * @return The effect level of the potion represented by Roman numerals.
     */
    public static @NotNull String getEffectAmplifierName(int amplifier, @NotNull String locale) {
        if (amplifier < 1) {
            return "";
        }
        if (amplifier < ROMAN_NUM.length) {
            return ROMAN_NUM[amplifier];
        }
        return "potion.potency." + amplifier;
    }

    /**
     * This method is similar to {@link #getEffectAmplifierName(int, String)}
     */
    public static @NotNull String getEffectAmplifierName(int amplifier, @NotNull Player player) {
        return getEffectAmplifierName(amplifier, player.getLocale());
    }

    /**
     * Combine the names and "level" and duration of the localized PotionEffect
     * to make them look like the original ones in the game.
     *
     * @param effect The name of the PotionEffect will be translated.
     * @param locale This locale will be used for translation.
     * @return Returns the display format of the localized potion effect.
     */
    public static @NotNull String getPotionEffectDisplay(@NotNull PotionEffect effect, @NotNull String locale) {
        String name = getPotionEffectName(effect.getType(), locale);
        String ampl = getEffectAmplifierName(effect.getAmplifier(), locale);

        String localized;

        if (ampl.length() > 0) {
            localized = Named.POTION_WITH_AMPLIFIER.getLocalized(locale);
            name = String.format(localized, name, ampl);
        }

        int duration = effect.getDuration();
        if (duration > 20) {
            localized = Named.POTION_WITH_DURATION.getLocalized(locale);
            name = String.format(localized, name, Util.getShortTime(duration));
        }

        return name;
    }

    /**
     * This method is similar to {@link #getPotionEffectDisplay(PotionEffect,
     * String)}
     */
    public static @NotNull String getPotionEffectDisplay(@NotNull PotionEffect effect, @NotNull Player player) {
        return getPotionEffectDisplay(effect, player.getLocale());
    }

    /**
     * Get the localized name of the tropical fish category. The category of
     * tropical fish are represented as patterns.
     *
     * @param pattern Pattern of tropical fish.
     * @param locale  This locale will be used for translation.
     * @return Returns the translated name of the tropical fish pattern.
     */
    public static @NotNull String getTropicalFishTypeName(@NotNull TropicalFish.Pattern pattern, @NotNull String locale) {
        return LangUtils.tropicalFishTypeStorage.getEntry(locale, pattern);
    }

    /**
     * This method is similar to {@link #getTropicalFishTypeName(TropicalFish.Pattern,
     * String)}
     */
    public static @NotNull String getTropicalFishTypeName(@NotNull TropicalFish.Pattern pattern, @NotNull Player player) {
        return getTropicalFishTypeName(pattern, player.getLocale());
    }

    /**
     * Get the names of 22 predefined tropical fish according to the 'variant'
     * tag of TropicalFish.
     *
     * @param meta   Metadata carrying information about tropical fish.
     * @param locale This locale will be used for translation.
     * @return If variant is predefined, return the name of the tropical fish,
     *         otherwise return null.
     * @see <a href="https://minecraft.gamepedia.com/Tropical_Fish#Entity_data">
     *         gamepedia</a>
     */
    public static @Nullable String getPredefinedTropicalFishName(@NotNull TropicalFishBucketMeta meta, @NotNull String locale) {
        Integer variant = Util.getTropicalFishVariant(meta);
        if (variant != null) {
            return LangUtils.tropicalFishNameStorage.getEntry(locale, variant);
        }
        return null;
    }

    /**
     * This method is similar to {@link #getPredefinedTropicalFishName(TropicalFishBucketMeta,
     * String)}
     */
    public static @Nullable String getPredefinedTropicalFishName(@NotNull TropicalFishBucketMeta meta, @NotNull Player player) {
        return getPredefinedTropicalFishName(meta, player.getLocale());
    }

    /**
     * Get the localized name of the dye color.
     *
     * @param color  The dye color of the name to be translated.
     * @param locale This locale will be used for translation.
     * @return The translated dye color name.
     */
    public static @NotNull String getDyeColorName(@NotNull DyeColor color, @NotNull String locale) {
        String result = LangUtils.dyeColorStorage.getEntry(locale, color);
        return result != null ? result : color.name().toLowerCase(Locale.ROOT);
    }

    /**
     * This method is similar to {@link #getDyeColorName(DyeColor, String)}
     */
    public static @NotNull String getDyeColorName(@NotNull DyeColor color, @NotNull Player player) {
        return getDyeColorName(color, player.getLocale());
    }

    /**
     * Get the localized name of the villager's professional level. Completing a
     * trade with a villager increases its professional-level slightly.
     * <p>
     * <b>This method is only applicable to Minecraft v1.14 and
     * above.</b>
     *
     * @param level  Professional level of the villager to be translated (1-5).
     * @param locale This locale will be used for translation.
     * @return The translated professional-level name of the villager.
     **/
    public static @NotNull String getVillagerLevelName(int level, @NotNull String locale) {
        String result = LangUtils.villagerLevelStorage.getEntry(locale, level);
        return result == null ? "merchant.level." + level : result;
    }

    /**
     * This method is similar to {@link #getVillagerLevelName(int, String)}
     */
    public static @NotNull String getVillagerLevelName(int level, @NotNull Player player) {
        return getVillagerLevelName(level, player.getLocale());
    }

    /**
     * Get the localized professional-name of the villager.
     * <p>
     * <b>This method is only applicable to Minecraft v1.14 and above.
     * To get the career name of the villager in Minecraft v1.13, please use
     * {@link #getVillagerCareerName(Career, String)}</b>
     *
     * @param profession The profession of villager.
     * @param locale     This locale will be used for translation.
     * @return The translated professional name of the villager.
     */
    public static @NotNull String getVillagerProfessionName(@NotNull Profession profession, @NotNull String locale) {
        return LangUtils.professionStorage.getEntry(locale, profession);
    }

    /**
     * This method is similar to {@link #getVillagerProfessionName(Profession,
     * String)}
     */
    public static @NotNull String getVillagerProfessionName(@NotNull Profession profession, @NotNull Player player) {
        return getVillagerProfessionName(profession, player.getLocale());
    }

    /**
     * Get the localized career-name of the villager.
     * <p>
     * <b>This method is only applicable to Minecraft v1.13.
     * To get the name of the villager profession in Minecraft v1.14 and above,
     * please use {@link #getVillagerProfessionName(Profession, String)}</b>
     *
     * @param career The Career of villager.
     * @param locale This locale will be used for translation.
     * @return The translated Career name of the villager.
     */
    public static @NotNull String getVillagerCareerName(@NotNull Career career, @NotNull String locale) {
        return LangUtils.villagerCareerStorage.getEntry(locale, career);
    }

    /**
     * This method is similar to {@link #getVillagerCareerName(Career, String)}
     */
    public static @NotNull String getVillagerCareerName(@NotNull Career career, @NotNull Player player) {
        return getVillagerCareerName(career, player.getLocale());
    }

    /**
     * Get the localized name of the banner pattern.
     *
     * @param pattern The banner pattern whose name will be translated.
     * @param locale  This locale will be used for translation.
     * @return The translated banner pattern name.
     */
    public static @NotNull String getBannerPatternName(@NotNull Pattern pattern, @NotNull String locale) {
        String result = LangUtils.bannerPatternStorage.getEntry(locale, Util.getPatternMixedCode(pattern));
        if (result == null || result.isEmpty()) {
            return pattern.getColor().name().toLowerCase(Locale.ROOT)
                    + "_"
                    + pattern.getPattern().name().toLowerCase(Locale.ROOT);
        }
        return result;
    }

    /**
     * This method is similar to {@link #getBannerPatternName(Pattern, String)}
     */
    public static @NotNull String getBannerPatternName(@NotNull Pattern pattern, @NotNull Player player) {
        return getBannerPatternName(pattern, player.getLocale());
    }

    /**
     * Get the name of the shield with base color. After the shield and the
     * banner are combined, will get a shield with base color.
     *
     * @param shield The shield whose base color name will be translated.
     * @param locale This locale will be used for translation.
     * @return The translated name of the base color of the shield.
     */
    public static @Nullable String getColoredShiedName(@NotNull ItemStack shield, @NotNull String locale) {
        DyeColor color = NMS.getShieldBaseColor(shield);
        if (color == null) {
            return null;
        }
        return LangUtils.coloredShiedStorage.getEntry(locale, color);
    }

    /**
     * This method is similar to {@link #getColoredShiedName(ItemStack,
     * String)}
     */
    public static @Nullable String getColoredShiedName(@NotNull ItemStack shield, @NotNull Player player) {
        return getColoredShiedName(shield, player.getLocale());
    }

    /**
     * This is a utility method to easily obtain the pattern of the shield
     * merged with the patterned banner.
     *
     * @param shied The shield ItemStack.
     * @return If the shield has a banner pattern on it, a list containing the
     *         banner pattern will be returned. Otherwise, an empty list will be
     *         returned.
     */
    public static @NotNull List<Pattern> getShiedPatterns(@NotNull ItemStack shied) {
        return NMS.getShiedPatterns(shied);
    }

}