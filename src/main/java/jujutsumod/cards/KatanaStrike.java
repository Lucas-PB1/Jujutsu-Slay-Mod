package jujutsumod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import jujutsumod.character.Itadori;
import jujutsumod.util.CardStats;

public class KatanaStrike extends BaseCard {
    public static final String ID = makeID("KatanaStrike");
    private static final CardStats info = new CardStats(
            Itadori.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.COMMON,
            CardTarget.ENEMY,
            1
    );

    public KatanaStrike() {
        super(ID, info);
        setDamage(9, 3);
        setMagic(1, 1);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        
        // Gain Focus based on magicNumber
        addToBot(new ApplyPowerAction(p, p, new com.megacrit.cardcrawl.powers.FocusPower(p, magicNumber), magicNumber));
    }
}
