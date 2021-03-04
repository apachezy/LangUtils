package com.meowj.langutils.misc;

import com.meowj.langutils.lang.LanguageHelper;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.TropicalFish.Pattern;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Util {

    private Util() {
    }

    public static String getShortTime(int duration) {
        int m = duration / 20 / 60;
        int s = duration / 20 % 60;
        return String.format("%d:%02d", m, s);
    }

    public static PotionType getPotionType(@NotNull ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta instanceof PotionMeta) {
            return ((PotionMeta) meta).getBasePotionData().getType();
        }
        throw new UnsupportedOperationException("The given item is not a potion.");
    }

    @Nullable
    public static String getPlayerHeadOwnerName(@NotNull ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta instanceof SkullMeta) {
            OfflinePlayer ofp = ((SkullMeta) meta).getOwningPlayer();
            if (ofp != null) {
                return ofp.getName();
            }
        }
        return null;
    }

    @Nullable
    public static Integer getTropicalFishVariant(@NotNull TropicalFishBucketMeta meta) {
        if (meta.hasVariant()) {
            Pattern pattern = meta.getPattern();

            // https://minecraft.gamepedia.com/Tropical_Fish#Entity_data

            int type = pattern.ordinal() > 5 ? 1 : 0;
            int patt = pattern.ordinal() % 6;
            int bcol = meta.getBodyColor().ordinal();
            int pcol = meta.getPatternColor().ordinal();

            return pcol << 24 | bcol << 16 | patt << 8 | type;
        }
        return null;
    }

    public static Integer getPatternMixedCode(@NotNull org.bukkit.block.banner.Pattern pattern) {
        int p = pattern.getPattern().ordinal();
        int c = pattern.getColor().ordinal();
        return p << 16 | c;
    }

    public static void testPlayerHandle(CommandSender sender, String lang) {
        if (sender instanceof Player) {

            Player player = (Player) sender;
            String locale = lang == null ? player.getLocale() : lang;
            ItemStack itm = player.getInventory().getItemInMainHand();

            player.sendMessage(LanguageHelper.getItemName(itm, locale));

            ItemMeta meta = itm.getItemMeta();
            if (meta == null) {
                return;
            }

            sendBannerPatterns(meta, player, locale);
            sendFishBucket    (meta, player, locale);
            sendPotionEffect  (meta, player, locale);
        }
    }

    private static void sendBannerPatterns(ItemMeta meta, Player player, String locale) {
        if (meta instanceof BannerMeta) {
            BannerMeta bannerMeta = (BannerMeta) meta;
            for (org.bukkit.block.banner.Pattern pattern : bannerMeta.getPatterns()) {
                player.sendMessage("  "
                        + ChatColor.GRAY
                        + LanguageHelper.getBannerPatternName(pattern, locale)
                        + " ("
                        + pattern.getPattern().name()
                        + " - " + pattern.getColor().name()
                        + ")");
            }
        }
    }

    private static void sendFishBucket(ItemMeta meta, Player player, String locale) {
        if (meta instanceof TropicalFishBucketMeta) {

            TropicalFishBucketMeta fishBucketMeta = (TropicalFishBucketMeta) meta;
            String message = LanguageHelper.getPredefinedTropicalFishName(fishBucketMeta, locale);

            if (message == null) {
                message = LanguageHelper.getDyeColorName(fishBucketMeta.getBodyColor(), locale)
                        + "-"
                        + LanguageHelper.getDyeColorName(fishBucketMeta.getPatternColor(), locale)
                        + " "
                        + LanguageHelper.getTropicalFishTypeName(fishBucketMeta.getPattern(), locale);
            }

            player.sendMessage("  " + ChatColor.GRAY + ChatColor.ITALIC + message);
        }
    }

    private static void sendPotionEffect(ItemMeta meta, Player player, String locale) {
        if (meta instanceof PotionMeta) {

            PotionMeta potionMeta = (PotionMeta) meta;
            PotionData potionData = potionMeta.getBasePotionData();

            String message = "  " + ChatColor.GRAY + ChatColor.ITALIC;
            message += LanguageHelper.getPotionName(potionData.getType(), locale);

            if (potionData.isUpgraded()) {
                message += "II";
            }

            player.sendMessage(message);

            for (PotionEffect eff : potionMeta.getCustomEffects()) {
                message = LanguageHelper.getPotionEffectDisplay(eff, locale);
                player.sendMessage(message);
            }
        }
    }

}
