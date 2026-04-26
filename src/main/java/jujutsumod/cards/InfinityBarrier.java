package jujutsumod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import jujutsumod.character.Itadori;
import jujutsumod.patches.CustomTags;
import jujutsumod.powers.InfinityPower;
import jujutsumod.util.CardStats;

public class InfinityBarrier extends BaseCard {
    public static final String ID = makeID("InfinityBarrier");
    private static final CardStats info = new CardStats(
            Itadori.Meta.CARD_COLOR,
            CardType.POWER,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            2
    );

    public InfinityBarrier() {
        super(ID, info);
        setMagic(2, 1);
        tags.add(CustomTags.LIMITLESS);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new InfinityPower(p, magicNumber), magicNumber));
    }
}
