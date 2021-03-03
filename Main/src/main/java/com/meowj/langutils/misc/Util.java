package com.meowj.langutils.misc;

import com.meowj.langutils.lang.LanguageHelper;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.banner.PatternType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.TropicalFish.Pattern;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
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

            switch (itm.getType()) {
                case BLACK_BANNER:
                case BLUE_BANNER:
                case BROWN_BANNER:
                case CYAN_BANNER:
                case GRAY_BANNER:
                case GREEN_BANNER:
                case LIGHT_BLUE_BANNER:
                case LIGHT_GRAY_BANNER:
                case LIME_BANNER:
                case MAGENTA_BANNER:
                case ORANGE_BANNER:
                case PINK_BANNER:
                case PURPLE_BANNER:
                case RED_BANNER:
                case YELLOW_BANNER:
                case WHITE_BANNER:
                    BannerMeta bannerMeta = (BannerMeta) meta;
                    for (org.bukkit.block.banner.Pattern pattern : bannerMeta.getPatterns()) {
                        player.sendMessage(
                                "  " + ChatColor.GRAY
                                     + LanguageHelper.getBannerPatternName(pattern, player));
                    }
                    break;

                default:
                    break;
            }
        } else {
            org.bukkit.block.banner.Pattern pattern1 = new org.bukkit.block.banner.Pattern(DyeColor.ORANGE, PatternType.CURLY_BORDER);
            org.bukkit.block.banner.Pattern pattern2 = new org.bukkit.block.banner.Pattern(DyeColor.ORANGE, PatternType.CURLY_BORDER);

            sender.sendMessage(String.format(
                    "%d, %d | %s, %s",
                    pattern1.hashCode(), pattern2.hashCode(), pattern1.toString(), pattern2.toString()));
        }
    }

}
