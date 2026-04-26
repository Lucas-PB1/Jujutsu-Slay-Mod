package jujutsumod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import jujutsumod.character.Itadori;
import jujutsumod.powers.ImmeasurableCursedEnergyPower;
import jujutsumod.util.CardStats;

public class ImmeasurableCursedEnergy extends BaseCard {
    public static final String ID = makeID("ImmeasurableCursedEnergy");
    private static final CardStats info = new CardStats(
            Itadori.Meta.CARD_COLOR,
            CardType.POWER,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            2
    );

    public ImmeasurableCursedEnergy() {
        super(ID, info);
        setCostUpgrade(1);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new ImmeasurableCursedEnergyPower(p, 1), 1));
    }
}
