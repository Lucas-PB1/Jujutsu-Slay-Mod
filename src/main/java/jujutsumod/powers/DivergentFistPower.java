package jujutsumod.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import static jujutsumod.BasicMod.makeID;

public class DivergentFistPower extends BasePower {
    public static final String POWER_ID = makeID("DivergentFistPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private final AbstractCreature source;
    private final int damageAmount;

    public DivergentFistPower(AbstractCreature owner, AbstractCreature source, int amount) {
        super(POWER_ID, PowerType.DEBUFF, false, owner, amount);
        this.source = source;
        this.damageAmount = amount;
        updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (!isPlayer) {
            flash();
            addToBot(new DamageAction(owner, new DamageInfo(source, damageAmount, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
            addToBot(new RemoveSpecificPowerAction(owner, source, this));
        }
    }

    @Override
    public void updateDescription() {
        description = powerStrings.DESCRIPTIONS[0] + damageAmount + powerStrings.DESCRIPTIONS[1];
    }
}
