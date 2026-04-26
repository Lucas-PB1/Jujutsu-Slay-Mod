package jujutsumod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.BlurPower;
import jujutsumod.character.Itadori;
import jujutsumod.patches.CustomTags;
import jujutsumod.util.CardStats;

public class RikaProtectiveEmbrace extends BaseCard {
    public static final String ID = makeID("RikaProtectiveEmbrace");
    private static final CardStats info = new CardStats(
            Itadori.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.COMMON,
            CardTarget.SELF,
            1
    );

    public RikaProtectiveEmbrace() {
        super(ID, info);
        setBlock(8, 3);
        tags.add(CustomTags.SHIKIGAMI);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, block));
        
        // If the player has any Power active, gain 1 Blur
        if (!p.powers.isEmpty()) {
            boolean hasPower = false;
            for (com.megacrit.cardcrawl.powers.AbstractPower pow : p.powers) {
                if (pow.type == com.megacrit.cardcrawl.powers.AbstractPower.PowerType.BUFF) {
                    hasPower = true;
                    break;
                }
            }
            if (hasPower) {
                addToBot(new ApplyPowerAction(p, p, new BlurPower(p, 1), 1));
            }
        }
    }
}
