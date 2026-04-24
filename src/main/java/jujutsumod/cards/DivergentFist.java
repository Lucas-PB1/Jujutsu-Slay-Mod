package jujutsumod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import jujutsumod.character.Itadori;
import jujutsumod.powers.DivergentFistPower;
import jujutsumod.util.CardStats;

public class DivergentFist extends BaseCard {
    public static final String ID = makeID("DivergentFist");
    private static final CardStats info = new CardStats(
            Itadori.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.BASIC,
            CardTarget.ENEMY,
            1
    );

    public DivergentFist() {
        super(ID, info);
        setDamage(7, 3); // Dano imediato: 7 (Upgrade +3)
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Primeiro impacto
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        
        // Segundo impacto (Power)
        addToBot(new ApplyPowerAction(m, p, new DivergentFistPower(m, p, damage), damage));
    }
}
