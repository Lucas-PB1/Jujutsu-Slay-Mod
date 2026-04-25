package jujutsumod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import jujutsumod.character.Itadori;
import jujutsumod.util.CardStats;

public class DivineFlame extends BaseCard {
    public static final String ID = makeID("DivineFlame");
    private static final CardStats info = new CardStats(
            Itadori.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.RARE,
            CardTarget.ENEMY,
            2
    );

    public DivineFlame() {
        super(ID, info);
        setDamage(10, 4);
        setMagic(8, 3); // Dano adicional por Shrine
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int count = 0;
        for (AbstractCard c : AbstractDungeon.actionManager.cardsPlayedThisTurn) {
            if (c.hasTag(jujutsumod.patches.CustomTags.SHRINE)) {
                count++;
            }
        }
        
        // O contador já inclui o uso de outras cartas antes desta.
        // Se quisermos que o dano base seja X + (count * Y)
        int finalDamage = damage + (count * magicNumber);
        
        addToBot(new DamageAction(m, new DamageInfo(p, finalDamage, damageTypeForTurn), AbstractGameAction.AttackEffect.FIRE));
    }

    @Override
    public void triggerOnGlowCheck() {
        boolean hasShrine = false;
        for (AbstractCard c : AbstractDungeon.actionManager.cardsPlayedThisTurn) {
            if (c.hasTag(jujutsumod.patches.CustomTags.SHRINE)) {
                hasShrine = true;
                break;
            }
        }
        if (hasShrine) {
            this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        } else {
            this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }
}
