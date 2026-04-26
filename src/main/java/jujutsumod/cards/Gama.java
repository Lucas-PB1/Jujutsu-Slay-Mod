package jujutsumod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import jujutsumod.character.Itadori;
import jujutsumod.patches.CustomTags;
import jujutsumod.powers.GamaPower;
import jujutsumod.util.CardStats;

public class Gama extends BaseCard {
    public static final String ID = makeID("Gama");
    private static final CardStats info = new CardStats(
            Itadori.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.COMMON,
            CardTarget.ENEMY,
            1
    );

    public Gama() {
        super(ID, info);
        setMagic(4, 2);
        tags.add(CustomTags.TEN_SHADOWS);
        tags.add(CustomTags.SHIKIGAMI);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m != null) {
            addToBot(new ApplyPowerAction(m, p, new GamaPower(m, magicNumber), magicNumber));
        }
    }
}
