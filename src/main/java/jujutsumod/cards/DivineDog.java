package jujutsumod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import jujutsumod.character.Itadori;
import jujutsumod.patches.CustomTags;
import jujutsumod.util.CardStats;

public class DivineDog extends BaseCard {
    public static final String ID = makeID("DivineDog");
    private static final CardStats info = new CardStats(
            Itadori.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.COMMON,
            CardTarget.ENEMY,
            1
    );

    public DivineDog() {
        super(ID, info);
        setDamage(6, 3);
        setMagic(2, 1);
        tags.add(CustomTags.TEN_SHADOWS);
        tags.add(CustomTags.SHIKIGAMI);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        
        for (AbstractCard c : p.hand.group) {
            if (c.hasTag(CustomTags.TEN_SHADOWS) && c != this) {
                c.baseDamage += magicNumber;
                c.applyPowers();
                c.flash();
            }
        }
        for (AbstractCard c : p.drawPile.group) {
            if (c.hasTag(CustomTags.TEN_SHADOWS)) {
                c.baseDamage += magicNumber;
            }
        }
        for (AbstractCard c : p.discardPile.group) {
            if (c.hasTag(CustomTags.TEN_SHADOWS)) {
                c.baseDamage += magicNumber;
            }
        }
    }
}
