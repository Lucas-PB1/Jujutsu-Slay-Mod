package jujutsumod.cards;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import jujutsumod.character.Itadori;
import jujutsumod.patches.CustomTags;
import jujutsumod.powers.SixEyesPower;
import jujutsumod.util.CardStats;

public class CurtainLimitless extends BaseCard {
    public static final String ID = makeID("CurtainLimitless");
    private static final CardStats info = new CardStats(
            Itadori.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.COMMON,
            CardTarget.SELF,
            1
    );

    public CurtainLimitless() {
        super(ID, info);
        setBlock(8, 3);
        tags.add(CustomTags.LIMITLESS);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, block));
        
        if (p.hasPower(SixEyesPower.POWER_ID)) {
            addToBot(new DrawCardAction(1));
        }
    }
}
