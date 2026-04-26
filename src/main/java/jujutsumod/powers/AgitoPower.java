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
            addToBot(new DamageRandomEnemyAction(new DamageInfo(owner, 10 * amount, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.SLASH_HEAVY));
            addToBot(new GainBlockAction(owner, owner, 6 * amount));
            addToBot(new HealAction(owner, owner, 2 * amount));
            addToBot(new ApplyPowerAction(owner, owner, new StrengthPower(owner, amount), amount));
        }
    }

    @Override
    public void updateDescription() {
        description = powerStrings.DESCRIPTIONS[0] + (10 * amount) + 
                     powerStrings.DESCRIPTIONS[1] + (6 * amount) + 
                     powerStrings.DESCRIPTIONS[2] + amount + 
                     powerStrings.DESCRIPTIONS[3] + (2 * amount) + 
                     powerStrings.DESCRIPTIONS[4];
    }
}
