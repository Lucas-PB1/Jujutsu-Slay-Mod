package jujutsumod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import jujutsumod.character.Itadori;
import jujutsumod.patches.CustomTags;
import jujutsumod.util.CardStats;

public class PureLoveBeam extends BaseCard {
    public static final String ID = makeID("PureLoveBeam");
    private static final CardStats info = new CardStats(
            Itadori.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.RARE,
            CardTarget.ENEMY,
            -1 // X cost
    );

    public PureLoveBeam() {
        super(ID, info);
        setDamage(5, 2);
        tags.add(CustomTags.SHIKIGAMI);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int effect = this.energyOnUse;
        if (p.hasRelic("Chemical X")) {
            effect += 2;
        }

        if (effect > 0) {
            for (int i = 0; i < effect; i++) {
                addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.FIRE));
            }
        }

        if (!this.freeToPlayOnce) {
            p.energy.use(this.energyOnUse);
        }
    }

    @Override
    public void applyPowers() {
        int realBaseDamage = this.baseDamage;
        int focusAmount = 0;
        if (AbstractDungeon.player.hasPower("Focus")) {
            focusAmount = AbstractDungeon.player.getPower("Focus").amount;
        }
        this.baseDamage += focusAmount;
        super.applyPowers();
        this.baseDamage = realBaseDamage;
        this.isDamageModified = (this.damage != this.baseDamage);
    }

    @Override
    public void calculateCardDamage(AbstractMonster m) {
        int realBaseDamage = this.baseDamage;
        int focusAmount = 0;
        if (AbstractDungeon.player.hasPower("Focus")) {
            focusAmount = AbstractDungeon.player.getPower("Focus").amount;
        }
        this.baseDamage += focusAmount;
        super.calculateCardDamage(m);
        this.baseDamage = realBaseDamage;
        this.isDamageModified = (this.damage != this.baseDamage);
    }
}
