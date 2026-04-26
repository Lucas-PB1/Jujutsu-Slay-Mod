package jujutsumod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import jujutsumod.character.Itadori;
import jujutsumod.patches.CustomTags;
import jujutsumod.powers.SixEyesPower;
import jujutsumod.util.CardStats;

public class SixEyes extends BaseCard {
    public static final String ID = makeID("SixEyes");
    private static final CardStats info = new CardStats(
            Itadori.Meta.CARD_COLOR,
            CardType.POWER,
            CardRarity.RARE,
            CardTarget.SELF,
            3
    );

    public SixEyes() {
        super(ID, info);
        setMagic(3, 2);
        tags.add(CustomTags.LIMITLESS);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int energy = upgraded ? 2 : 1;
        addToBot(new ApplyPowerAction(p, p, new SixEyesPower(p, energy, magicNumber, upgraded), energy));
    }
}
