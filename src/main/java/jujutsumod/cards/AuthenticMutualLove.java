package jujutsumod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import jujutsumod.character.Itadori;
import jujutsumod.powers.AuthenticMutualLovePower;
import jujutsumod.util.CardStats;

public class AuthenticMutualLove extends BaseCard {
    public static final String ID = makeID("AuthenticMutualLove");
    private static final CardStats info = new CardStats(
            Itadori.Meta.CARD_COLOR,
            CardType.POWER,
            CardRarity.RARE,
            CardTarget.SELF,
            3
    );

    public AuthenticMutualLove() {
        super(ID, info);
        setMagic(1, 1);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Territory Dispute: Remove other domains
        addToBot(new com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction(p, p, jujutsumod.powers.UnlimitedVoidPower.POWER_ID));
        addToBot(new com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction(p, p, jujutsumod.powers.MalevolentShrinePower.POWER_ID));

        addToBot(new ApplyPowerAction(p, p, new AuthenticMutualLovePower(p, magicNumber), magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(2);
            upgradeMagicNumber(1);
            initializeDescription();
        }
    }
}
