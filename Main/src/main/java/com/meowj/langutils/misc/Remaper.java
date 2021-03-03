package com.meowj.langutils.misc;

import org.jetbrains.annotations.NotNull;

public class Remaper {

    private Remaper() {
    }

    public static String remap(String lang) {
        switch (lang) {
            case "af_za": return "af";
            case "ar_sa": return "ar";
            case "ast_es": return "ast";
            case "az_az": return "az";
            case "ba_ru": return "ba";
            case "be_by": return "be";
            case "bg_bg": return "bg";
            case "br_fr": return "br";
            case "bs_ba": return "bs";
            case "ca_es": return "ca";
            case "cs_cz": return "cs";
            case "cy_gb": return "cy";
            case "da_dk": return "da";
            case "de_de": return "de";
            case "el_gr": return "el";
            case "en_us": return "en";
            case "eo_uy": return "eo";
            case "es_es": return "es";
            case "et_ee": return "et";
            case "eu_es": return "eu";
            case "fa_ir": return "fa";
            case "fi_fi": return "fi";
            case "fil_ph": return "fil";
            case "fo_fo": return "fo";
            case "fr_fr": return "fr";
            case "fra_de": return "fra";
            case "fy_nl": return "fy";
            case "ga_ie": return "ga";
            case "gd_gb": return "gd";
            case "gl_es": return "gl";
            case "got_de": return "got";
            case "gv_im": return "gv";
            case "haw_us": return "haw";
            case "he_il": return "he";
            case "hi_in": return "hi";
            case "hr_hr": return "hr";
            case "hu_hu": return "hu";
            case "hy_am": return "hy";
            case "id_id": return "id";
            case "ig_ng": return "ig";
            case "io_en": return "io";
            case "is_is": return "is";
            case "it_it": return "it";
            case "ja_jp": return "ja";
            case "jbo_en": return "jbo";
            case "ka_ge": return "ka";
            case "kab_kab": return "kab";
            case "kk_kz": return "kk";
            case "kn_in": return "kn";
            case "ko_kr": return "ko";
            case "kw_gb": return "kw";
            case "la_la": return "la";
            case "lb_lu": return "lb";
            case "li_li": return "li";
            case "lol_us": return "lol";
            case "lt_lt": return "lt";
            case "lv_lv": return "lv";
            case "mi_nz": return "mi";
            case "mk_mk": return "mk";
            case "mn_mn": return "mn";
            case "moh_ca": return "moh";
            case "ms_my": return "ms";
            case "mt_mt": return "mt";
            case "nds_de": return "nds";
            case "nl_nl": return "nl";
            case "nn_no": return "nn";
            case "no_no": return "no";
            case "oc_fr": return "oc";
            case "oj_ca": return "oj";
            case "pl_pl": return "pl";
            case "pt_pt": return "pt";
            case "qya_aa": return "qya";
            case "ro_ro": return "ro";
            case "ru_ru": return "ru";
            case "se_no": return "se";
            case "sk_sk": return "sk";
            case "sl_si": return "sl";
            case "so_so": return "so";
            case "sq_al": return "sq";
            case "sr_sp": return "sr";
            case "sv_se": return "sv";
            case "ta_in": return "ta";
            case "th_th": return "th";
            case "tl_ph": return "tl";
            case "tlh_aa": return "tlh";
            case "tr_tr": return "tr";
            case "tt_ru": return "tt";
            case "tzl_tzl": return "tzl";
            case "uk_ua": return "uk";
            case "val_es": return "val";
            case "vec_it": return "vec";
            case "vi_vn": return "vi";
            case "yi_de": return "yi";
            case "yo_ng": return "yo";
            case "zh_cn": return "zh";
            default: return null;
        }
    }

    @NotNull
    public static String getLang(@NotNull String locale) {
        int i = locale.indexOf('_');
        if (i > 0) {
            return locale.substring(0, i);
        }
        return locale;
    }

}
