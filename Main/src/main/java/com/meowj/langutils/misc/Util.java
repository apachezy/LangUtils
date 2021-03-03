package com.meowj.langutils.misc;

import com.meowj.langutils.lang.LanguageHelper;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.TropicalFish.Pattern;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.inventory.meta.TropicalFishBucketMeta;
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
    public static String getPlayerHeadOwner(@NotNull ItemStack item) {
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

            return (pcol & 255) << 24 | (bcol & 255) << 16 | (patt & 255) << 8 | type;
        }
        return null;
    }

    public static void testPlayerHandle(CommandSender sender, String lang) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            String locale = lang == null ? player.getLocale() : lang;
            ItemStack itm = player.getInventory().getItemInMainHand();

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

                    break;

                default:
                    break;
            }

            player.sendMessage(LanguageHelper.getItemName(player.getInventory().getItemInMainHand(), lang));
        }
    }

}
