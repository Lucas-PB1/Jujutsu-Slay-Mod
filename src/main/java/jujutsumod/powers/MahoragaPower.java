package jujutsumod.powers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import jujutsumod.cards.Mahoraga;

import static jujutsumod.BasicMod.makeID;

public class MahoragaPower extends BasePower {
    public static final String POWER_ID = makeID("MahoragaPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public MahoragaPower(AbstractCreature owner, int amount) {
        super(POWER_ID, PowerType.BUFF, false, owner, amount);
        updateDescription();
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (damageAmount > 0 && info.type == DamageInfo.DamageType.NORMAL) {
            flash();
            for (AbstractCard c : AbstractDungeon.player.hand.group) {
                if (c instanceof Mahoraga) {
                    c.baseDamage += amount;
                    c.applyPowers();
                    c.flash();
                }
            }
            for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
                if (c instanceof Mahoraga) { c.baseDamage += amount; }
            }
            for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
                if (c instanceof Mahoraga) { c.baseDamage += amount; }
            }
        }
        return damageAmount;
    }

    @Override
    public void updateDescription() {
        description = powerStrings.DESCRIPTIONS[0] + amount + powerStrings.DESCRIPTIONS[1];
    }
}
