package jujutsumod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import jujutsumod.character.Itadori;
import jujutsumod.patches.CustomTags;
import jujutsumod.powers.TenShadowsTechniquePower;
import jujutsumod.util.CardStats;

public class TenShadowsTechnique extends BaseCard {
    public static final String ID = makeID("TenShadowsTechnique");
    private static final CardStats info = new CardStats(
            Itadori.Meta.CARD_COLOR,
            CardType.POWER,
            CardRarity.RARE,
            CardTarget.SELF,
            2
    );

    public TenShadowsTechnique() {
        super(ID, info);
        setMagic(1, 1);
        setCostUpgrade(1);
        tags.add(CustomTags.TEN_SHADOWS);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new TenShadowsTechniquePower(p, magicNumber), magicNumber));
    }
}
