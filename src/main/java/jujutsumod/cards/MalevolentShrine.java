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
        setMagic(2, 1);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Territory Dispute: Remove other domains
        addToBot(new com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction(p, p, jujutsumod.powers.UnlimitedVoidPower.POWER_ID));
        addToBot(new com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction(p, p, jujutsumod.powers.AuthenticMutualLovePower.POWER_ID));
        
        addToBot(new ApplyPowerAction(p, p, new MalevolentShrinePower(p, magicNumber), magicNumber));
    }
}
