package jujutsumod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import jujutsumod.character.Itadori;
import jujutsumod.util.CardStats;

public class CursedEnergyReinforcement extends BaseCard {
    public static final String ID = makeID("CursedEnergyReinforcement");
    private static final CardStats info = new CardStats(
            Itadori.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.COMMON,
            CardTarget.SELF,
            1
    );

    public CursedEnergyReinforcement() {
        super(ID, info);
        setBlock(7, 3);
        setMagic(4, 2); 
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, block));
        addToBot(new ApplyPowerAction(p, p, new VigorPower(p, magicNumber), magicNumber));
    }
}
