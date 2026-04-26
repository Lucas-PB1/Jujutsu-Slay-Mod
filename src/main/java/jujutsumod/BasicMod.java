package jujutsumod;

import basemod.BaseMod;
import basemod.interfaces.*;
import jujutsumod.character.Itadori;
import jujutsumod.util.GeneralUtils;
import jujutsumod.util.KeywordInfo;
import jujutsumod.util.Sounds;
import jujutsumod.util.TextureLoader;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglFileHandle;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.ModInfo;
import com.evacipated.cardcrawl.modthespire.Patcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.localization.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.scannotation.AnnotationDB;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.*;

@SpireInitializer
public class BasicMod implements
        EditCharactersSubscriber,
        EditStringsSubscriber,
        EditKeywordsSubscriber,
        AddAudioSubscriber,
        PostInitializeSubscriber,
        EditCardsSubscriber,
        EditRelicsSubscriber {
    public static ModInfo info;
    public static String modID;
    static { loadModInfo(); }
    private static final String resourcesFolder = checkResourcesPath();
    public static final Logger logger = LogManager.getLogger(modID);
    public static boolean DEBUG_MODE = true;

    public static String makeID(String id) {
        return modID + ":" + id;
    }

    public static void initialize() {
        new BasicMod();

        Itadori.Meta.registerColor();
    }

    public BasicMod() {
        BaseMod.subscribe(this);
        logger.info("{} subscribed to BaseMod.", modID);
    }

    @Override
    public void receiveEditRelics() {
        BaseMod.addRelicToCustomPool(new jujutsumod.relics.SukunaFinger(), Itadori.Meta.CARD_COLOR);
        
        BaseMod.addRelicToCustomPool(new com.megacrit.cardcrawl.relics.RedSkull(), Itadori.Meta.CARD_COLOR);
        BaseMod.addRelicToCustomPool(new com.megacrit.cardcrawl.relics.PaperFrog(), Itadori.Meta.CARD_COLOR);
        BaseMod.addRelicToCustomPool(new com.megacrit.cardcrawl.relics.SelfFormingClay(), Itadori.Meta.CARD_COLOR);
        BaseMod.addRelicToCustomPool(new com.megacrit.cardcrawl.relics.CharonsAshes(), Itadori.Meta.CARD_COLOR);
        BaseMod.addRelicToCustomPool(new com.megacrit.cardcrawl.relics.ChampionsBelt(), Itadori.Meta.CARD_COLOR);
        BaseMod.addRelicToCustomPool(new com.megacrit.cardcrawl.relics.MagicFlower(), Itadori.Meta.CARD_COLOR);
    }

    @Override
    public void receivePostInitialize() {
        BaseMod.addPotion(com.megacrit.cardcrawl.potions.BloodPotion.class, com.badlogic.gdx.graphics.Color.RED.cpy(), com.badlogic.gdx.graphics.Color.FIREBRICK.cpy(), null, com.megacrit.cardcrawl.potions.BloodPotion.POTION_ID, Itadori.Meta.YOUR_CHARACTER);
        BaseMod.addPotion(com.megacrit.cardcrawl.potions.Elixir.class, com.badlogic.gdx.graphics.Color.GRAY.cpy(), com.badlogic.gdx.graphics.Color.WHITE.cpy(), null, com.megacrit.cardcrawl.potions.Elixir.POTION_ID, Itadori.Meta.YOUR_CHARACTER);
        BaseMod.addPotion(com.megacrit.cardcrawl.potions.HeartOfIron.class, com.badlogic.gdx.graphics.Color.GOLD.cpy(), com.badlogic.gdx.graphics.Color.ORANGE.cpy(), null, com.megacrit.cardcrawl.potions.HeartOfIron.POTION_ID, Itadori.Meta.YOUR_CHARACTER);

        Texture badgeTexture = TextureLoader.getTexture(imagePath("badge.png"));
        BaseMod.registerModBadge(badgeTexture, info.Name, GeneralUtils.arrToString(info.Authors), info.Description, null);

        for (AbstractCard card : CardLibrary.getAllCards()) {
            if (card.color == Itadori.Meta.CARD_COLOR) {
                UnlockTracker.unlockCard(card.cardID);
            }
        }
    }

    private static String getLangString()
    {
        return Settings.language.name().toLowerCase();
    }
    private static final String defaultLanguage = "eng";

    public static final Map<String, KeywordInfo> keywords = new HashMap<>();

    @Override
    public void receiveEditStrings() {
        loadLocalization(defaultLanguage);
        if (!defaultLanguage.equals(getLangString())) {
            try {
                loadLocalization(getLangString());
            }
            catch (GdxRuntimeException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadLocalization(String lang) {
        BaseMod.loadCustomStringsFile(CardStrings.class,
                localizationPath(lang, "CardStrings.json"));
        BaseMod.loadCustomStringsFile(CharacterStrings.class,
                localizationPath(lang, "CharacterStrings.json"));
        BaseMod.loadCustomStringsFile(EventStrings.class,
                localizationPath(lang, "EventStrings.json"));
        BaseMod.loadCustomStringsFile(OrbStrings.class,
                localizationPath(lang, "OrbStrings.json"));
        BaseMod.loadCustomStringsFile(PotionStrings.class,
                localizationPath(lang, "PotionStrings.json"));
        BaseMod.loadCustomStringsFile(PowerStrings.class,
                localizationPath(lang, "PowerStrings.json"));
        BaseMod.loadCustomStringsFile(RelicStrings.class,
                localizationPath(lang, "RelicStrings.json"));
        BaseMod.loadCustomStringsFile(UIStrings.class,
                localizationPath(lang, "UIStrings.json"));
    }

    @Override
    public void receiveEditKeywords()
    {
        Gson gson = new Gson();
        String json = Gdx.files.internal(localizationPath(defaultLanguage, "Keywords.json")).readString(String.valueOf(StandardCharsets.UTF_8));
        KeywordInfo[] keywords = gson.fromJson(json, KeywordInfo[].class);
        for (KeywordInfo keyword : keywords) {
            keyword.prep();
            registerKeyword(keyword);
        }

        if (!defaultLanguage.equals(getLangString())) {
            try
            {
                json = Gdx.files.internal(localizationPath(getLangString(), "Keywords.json")).readString(String.valueOf(StandardCharsets.UTF_8));
                keywords = gson.fromJson(json, KeywordInfo[].class);
                for (KeywordInfo keyword : keywords) {
                    keyword.prep();
                    registerKeyword(keyword);
                }
            }
            catch (Exception e)
            {
                logger.warn("{} does not support {} keywords.", modID, getLangString());
            }
        }
    }

    private void registerKeyword(KeywordInfo info) {
        BaseMod.addKeyword(modID.toLowerCase(), info.PROPER_NAME, info.NAMES, info.DESCRIPTION, info.COLOR);
        if (!info.ID.isEmpty())
        {
            keywords.put(info.ID, info);
        }
    }

    @Override
    public void receiveEditCharacters() {
        Itadori.Meta.registerCharacter();
    }

    @Override
    public void receiveEditCards() {
        BaseMod.addCard(new jujutsumod.cards.Strike());
        BaseMod.addCard(new jujutsumod.cards.Defend());
        BaseMod.addCard(new jujutsumod.cards.DivergentFist());
        BaseMod.addCard(new jujutsumod.cards.BlackFlash());
        BaseMod.addCard(new jujutsumod.cards.CursedEnergyInfusion());
        BaseMod.addCard(new jujutsumod.cards.SlaughterDemon());
        BaseMod.addCard(new jujutsumod.cards.CursedFlow());
        BaseMod.addCard(new jujutsumod.cards.MartialArtsMastery());
        BaseMod.addCard(new jujutsumod.cards.CursedEnergyReinforcement());
        BaseMod.addCard(new jujutsumod.cards.Dismantle());
        BaseMod.addCard(new jujutsumod.cards.Cleave());
        BaseMod.addCard(new jujutsumod.cards.Spiderweb());
        BaseMod.addCard(new jujutsumod.cards.DivineFlame());
        BaseMod.addCard(new jujutsumod.cards.MalevolentShrine());
        BaseMod.addCard(new jujutsumod.cards.DivineDog());
        BaseMod.addCard(new jujutsumod.cards.Nue());
        BaseMod.addCard(new jujutsumod.cards.Gama());
        BaseMod.addCard(new jujutsumod.cards.Mahoraga());
        BaseMod.addCard(new jujutsumod.cards.GreatSerpent());
        BaseMod.addCard(new jujutsumod.cards.MaxElephant());
        BaseMod.addCard(new jujutsumod.cards.RabbitEscape());
        BaseMod.addCard(new jujutsumod.cards.RoundDeer());
        BaseMod.addCard(new jujutsumod.cards.PiercingOx());
        BaseMod.addCard(new jujutsumod.cards.TigerFuneral());
        BaseMod.addCard(new jujutsumod.cards.Agito());
        BaseMod.addCard(new jujutsumod.cards.TenShadowsTechnique());
        BaseMod.addCard(new jujutsumod.cards.Rabbit());
    }

    @Override
    public void receiveAddAudio() {
        loadAudio();
    }

    private static final String[] AUDIO_EXTENSIONS = { ".ogg", ".wav", ".mp3" };
    private void loadAudio() {
        try {
            Field[] fields = Sounds.class.getDeclaredFields();
            outer:
            for (Field f : fields) {
                int modifiers = f.getModifiers();
                if (Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers) && f.getType().equals(String.class)) {
                    String s = (String) f.get(null);
                    if (s == null) {
                        s = audioPath(f.getName());

                        for (String ext : AUDIO_EXTENSIONS) {
                            String testPath = s + ext;
                            if (Gdx.files.internal(testPath).exists()) {
                                s = testPath;
                                BaseMod.addAudio(s, s);
                                f.set(null, s);
                                continue outer;
                            }
                        }
                        throw new Exception("Failed to find an audio file \"" + f.getName() + "\" in " + resourcesFolder + "/audio; check to ensure the capitalization and filename are correct.");
                    }
                    else {
                        if (Gdx.files.internal(s).exists()) {
                            BaseMod.addAudio(s, s);
                        }
                        else {
                            throw new Exception("Failed to find audio file \"" + s + "\"; check to ensure this is the correct filepath.");
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            logger.error("Exception occurred in loadAudio: ", e);
        }
    }

    public static String localizationPath(String lang, String file) {
        return resourcesFolder + "/localization/" + lang + "/" + file;
    }

    public static String audioPath(String file) {
        return resourcesFolder + "/audio/" + file;
    }
    public static String imagePath(String file) {
        return resourcesFolder + "/images/" + file;
    }
    public static String characterPath(String file) {
        return resourcesFolder + "/images/character/" + file;
    }
    public static String powerPath(String file) {
        return resourcesFolder + "/images/powers/" + file;
    }
    public static String relicPath(String file) {
        return resourcesFolder + "/images/relics/" + file;
    }

    private static String checkResourcesPath() {
        String name = BasicMod.class.getName();
        int separator = name.indexOf('.');
        name = name.substring(0, separator);

        FileHandle resources = new LwjglFileHandle(name, Files.FileType.Internal);

        if (!resources.exists()) {
            throw new RuntimeException("\n\tFailed to find resources folder; expected it to be at  \"resources/" + name + "\".");
        }
        if (!resources.child("images").exists()) {
            throw new RuntimeException("\n\tFailed to find the 'images' folder in the mod's 'resources/" + name + "' folder; Make sure the " +
                    "images folder is in the correct location.");
        }
        if (!resources.child("localization").exists()) {
            throw new RuntimeException("\n\tFailed to find the 'localization' folder in the mod's 'resources/" + name + "' folder; Make sure the " +
                    "localization folder is in the correct location.");
        }

        return name;
    }

    private static void loadModInfo() {
        Optional<ModInfo> infos = Arrays.stream(Loader.MODINFOS).filter((modInfo)->{
            AnnotationDB annotationDB = Patcher.annotationDBMap.get(modInfo.jarURL);
            if (annotationDB == null)
                return false;
            Set<String> initializers = annotationDB.getAnnotationIndex().getOrDefault(SpireInitializer.class.getName(), Collections.emptySet());
            return initializers.contains(BasicMod.class.getName());
        }).findFirst();
        if (infos.isPresent()) {
            info = infos.get();
            modID = info.ID;
        }
        else {
            throw new RuntimeException("Failed to determine mod info/ID based on initializer.");
        }
    }
}
