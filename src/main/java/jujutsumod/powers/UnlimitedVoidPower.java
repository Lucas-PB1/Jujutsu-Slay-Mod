package jujutsumod.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

import static jujutsumod.BasicMod.makeID;

public class UnlimitedVoidPower extends BasePower {
    public static final String POWER_ID = makeID("UnlimitedVoidPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private int turnDamage;

    public UnlimitedVoidPower(AbstractCreature owner, int reductionAmount, int damageAmount) {
        super(POWER_ID, PowerType.BUFF, false, owner, reductionAmount);
        this.turnDamage = damageAmount;
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        // When stacking, we need to know if the new source was upgraded.
        // But ApplyPowerAction only gives us one 'amount'.
        // We'll assume the stackAmount passed is the reduction value.
        // We'll estimate damage: reduction 3 -> 5 damage, reduction 5 -> 10 damage.
        super.stackPower(stackAmount);
        if (stackAmount == 3) this.turnDamage += 5;
        else if (stackAmount == 5) this.turnDamage += 10;
        else this.turnDamage += (stackAmount * 2); // Fallback
    }

    @Override
    public void atStartOfTurn() {
        addToBot(new DamageAllEnemiesAction(owner, DamageInfo.createDamageMatrix(turnDamage, true), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.FIRE));
    }

    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType type) {
        if (type == DamageInfo.DamageType.NORMAL) {
            float newDamage = damage - (float)amount;
            if (newDamage <= 0 && damage > 0) {
                flash();
                addToBot(new GainEnergyAction(1));
                return 0;
            }
            return Math.max(0, newDamage);
        }
        return damage;
    }

    @Override
    public void updateDescription() {
        description = powerStrings.DESCRIPTIONS[0] + turnDamage + powerStrings.DESCRIPTIONS[1] + amount + powerStrings.DESCRIPTIONS[2];
    }
}
