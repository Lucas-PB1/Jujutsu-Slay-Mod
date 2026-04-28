package jujutsumod.registry;

import basemod.BaseMod;
import com.megacrit.cardcrawl.relics.ChampionsBelt;
import com.megacrit.cardcrawl.relics.CharonsAshes;
import com.megacrit.cardcrawl.relics.MagicFlower;
import com.megacrit.cardcrawl.relics.PaperFrog;
import com.megacrit.cardcrawl.relics.RedSkull;
import com.megacrit.cardcrawl.relics.SelfFormingClay;
import jujutsumod.character.Itadori;
import jujutsumod.relics.SukunaFinger;

public final class RelicRegistry {
    private RelicRegistry() {}

    public static void registerRelics() {
        BaseMod.addRelicToCustomPool(new SukunaFinger(), Itadori.Meta.CARD_COLOR);
        BaseMod.addRelicToCustomPool(new RedSkull(), Itadori.Meta.CARD_COLOR);
        BaseMod.addRelicToCustomPool(new PaperFrog(), Itadori.Meta.CARD_COLOR);
        BaseMod.addRelicToCustomPool(new SelfFormingClay(), Itadori.Meta.CARD_COLOR);
        BaseMod.addRelicToCustomPool(new CharonsAshes(), Itadori.Meta.CARD_COLOR);
        BaseMod.addRelicToCustomPool(new ChampionsBelt(), Itadori.Meta.CARD_COLOR);
        BaseMod.addRelicToCustomPool(new MagicFlower(), Itadori.Meta.CARD_COLOR);
    }
}
