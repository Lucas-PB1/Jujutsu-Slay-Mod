package jujutsumod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import jujutsumod.character.Itadori;
import jujutsumod.powers.MalevolentShrinePower;
import jujutsumod.util.CardStats;

public class MalevolentShrine extends BaseCard {
    public static final String ID = makeID("MalevolentShrine");
    private static final CardStats info = new CardStats(
            Itadori.Meta.CARD_COLOR,
            CardType.POWER,
            CardRarity.RARE,
            CardTarget.SELF,
            3
    );

    public MalevolentShrine() {
        super(ID, info);
        setMagic(4, 2); // Damage per card played
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new MalevolentShrinePower(p, magicNumber), magicNumber));
    }
}
