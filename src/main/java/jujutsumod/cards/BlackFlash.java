package jujutsumod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import jujutsumod.character.Itadori;
import jujutsumod.util.CardStats;

public class BlackFlash extends BaseCard {
    public static final String ID = makeID("BlackFlash");
    private static final CardStats info = new CardStats(
            Itadori.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.RARE,
            CardTarget.ENEMY,
            2
    );

    public BlackFlash() {
        super(ID, info);
        setDamage(20, 8);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Causar o dano massivo
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        
        // Contar ataques jogados este turno
        int attackCount = 0;
        for (AbstractCard c : AbstractDungeon.actionManager.cardsPlayedThisTurn) {
            if (c.type == CardType.ATTACK) {
                attackCount++;
            }
        }
        
        // Gerar energia: 1 para cada 2 ataques (Nerf)
        int energyGain = attackCount / 2;
        if (energyGain > 0) {
            addToBot(new GainEnergyAction(energyGain));
        }
    }
}
