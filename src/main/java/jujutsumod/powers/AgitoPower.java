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

    public AgitoPower(AbstractCreature owner, int amount) {
        super(POWER_ID, PowerType.BUFF, false, owner, amount);
        updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            flash();
            int damageAmt = 7 + amount;
            int blockAmt = 5 + amount;
            int strengthAmt = amount;
            int healAmt = 1 + amount;

            addToBot(new DamageRandomEnemyAction(new DamageInfo(owner, damageAmt, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.SLASH_HEAVY));
            addToBot(new GainBlockAction(owner, owner, blockAmt));
            addToBot(new HealAction(owner, owner, healAmt));
            addToBot(new ApplyPowerAction(owner, owner, new StrengthPower(owner, strengthAmt), strengthAmt));
        }
    }

    @Override
    public void updateDescription() {
        int damageAmt = 7 + amount;
        int blockAmt = 5 + amount;
        int strengthAmt = amount;
        int healAmt = 1 + amount;

        description = powerStrings.DESCRIPTIONS[0] + damageAmt + 
                     powerStrings.DESCRIPTIONS[1] + blockAmt + 
                     powerStrings.DESCRIPTIONS[2] + strengthAmt + 
                     powerStrings.DESCRIPTIONS[3] + healAmt + 
                     powerStrings.DESCRIPTIONS[4];
    }
}
