package jujutsumod.registry;

import basemod.BaseMod;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import jujutsumod.BasicMod;
import jujutsumod.util.KeywordInfo;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public final class LocalizationRegistry {
    public static final String DEFAULT_LANGUAGE = "eng";
    public static final Map<String, KeywordInfo> KEYWORDS = new HashMap<>();

    private static final Gson GSON = new Gson();
    private static final LocalizationFile[] LOCALIZATION_FILES = {
            new LocalizationFile(CardStrings.class, "CardStrings.json"),
            new LocalizationFile(CharacterStrings.class, "CharacterStrings.json"),
            new LocalizationFile(EventStrings.class, "EventStrings.json"),
            new LocalizationFile(OrbStrings.class, "OrbStrings.json"),
            new LocalizationFile(PotionStrings.class, "PotionStrings.json"),
            new LocalizationFile(PowerStrings.class, "PowerStrings.json"),
            new LocalizationFile(RelicStrings.class, "RelicStrings.json"),
            new LocalizationFile(UIStrings.class, "UIStrings.json"),
            new LocalizationFile(MonsterStrings.class, "MonsterStrings.json")
    };

    private LocalizationRegistry() {}

    public static void loadStrings() {
        loadLocalization(DEFAULT_LANGUAGE);
        String currentLanguage = getCurrentLanguage();
        if (!DEFAULT_LANGUAGE.equals(currentLanguage)) {
            try {
                loadLocalization(currentLanguage);
            }
            catch (GdxRuntimeException e) {
                BasicMod.logger.warn("{} does not support {} localization.", BasicMod.modID, currentLanguage, e);
            }
        }
    }

    public static void loadKeywords() {
        loadKeywords(DEFAULT_LANGUAGE);
        String currentLanguage = getCurrentLanguage();
        if (!DEFAULT_LANGUAGE.equals(currentLanguage)) {
            try {
                loadKeywords(currentLanguage);
            }
            catch (Exception e) {
                BasicMod.logger.warn("{} does not support {} keywords.", BasicMod.modID, currentLanguage);
            }
        }
    }

    private static void loadLocalization(String lang) {
        for (LocalizationFile file : LOCALIZATION_FILES) {
            BaseMod.loadCustomStringsFile(file.stringType, BasicMod.localizationPath(lang, file.fileName));
        }
    }

    private static void loadKeywords(String lang) {
        String json = Gdx.files.internal(BasicMod.localizationPath(lang, "Keywords.json")).readString(String.valueOf(StandardCharsets.UTF_8));
        KeywordInfo[] keywords = GSON.fromJson(json, KeywordInfo[].class);
        for (KeywordInfo keyword : keywords) {
            registerKeyword(keyword);
        }
    }

    private static void registerKeyword(KeywordInfo info) {
        info.prep();
        BaseMod.addKeyword(BasicMod.modID.toLowerCase(), info.PROPER_NAME, info.NAMES, info.DESCRIPTION, info.COLOR);
        if (!info.ID.isEmpty()) {
            KEYWORDS.put(info.ID, info);
        }
    }

    private static String getCurrentLanguage() {
        return Settings.language.name().toLowerCase();
    }

    private static class LocalizationFile {
        private final Class<?> stringType;
        private final String fileName;

        private LocalizationFile(Class<?> stringType, String fileName) {
            this.stringType = stringType;
            this.fileName = fileName;
        }
    }
}
