package jujutsumod.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static jujutsumod.BasicMod.makeID;

public class AgitoPower extends BasePower {
    public static final String POWER_ID = makeID("AgitoPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    
    private int damageAmt;
    private int blockAmt;
    private int strengthAmt;
    private int healAmt;

    public AgitoPower(AbstractCreature owner, int damage, int block, int strength, int heal) {
        super(POWER_ID, PowerType.BUFF, false, owner, 1); // amount can just be '1' for the icon
        this.damageAmt = damage;
        this.blockAmt = block;
        this.strengthAmt = strength;
        this.healAmt = heal;
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        // Here we handle stacking logic. We use a trick: 
        // if stackAmount is 1, it's base. If 2, it's upgraded?
        // Actually, better to pass the values directly in the action.
        // But stackPower only gets the amount from ApplyPowerAction.
        // I'll make Agito card pass the damage as the amount.
        super.stackPower(stackAmount);
        if (stackAmount == 8) { // Base
            this.damageAmt += 8;
            this.blockAmt += 6;
            this.strengthAmt += 1;
            this.healAmt += 2;
        } else if (stackAmount == 9) { // Upgraded
            this.damageAmt += 9;
            this.blockAmt += 7;
            this.strengthAmt += 2;
            this.healAmt += 3;
        } else {
            // Fallback for weird stacks
            this.damageAmt += stackAmount;
        }
        updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            flash();
            addToBot(new DamageRandomEnemyAction(new DamageInfo(owner, damageAmt, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.SLASH_HEAVY));
            addToBot(new GainBlockAction(owner, owner, blockAmt));
            addToBot(new HealAction(owner, owner, healAmt));
            addToBot(new ApplyPowerAction(owner, owner, new StrengthPower(owner, strengthAmt), strengthAmt));
        }
    }

    @Override
    public void updateDescription() {
        description = powerStrings.DESCRIPTIONS[0] + damageAmt + 
                     powerStrings.DESCRIPTIONS[1] + blockAmt + 
                     powerStrings.DESCRIPTIONS[2] + strengthAmt + 
                     powerStrings.DESCRIPTIONS[3] + healAmt + 
                     powerStrings.DESCRIPTIONS[4];
    }
}
