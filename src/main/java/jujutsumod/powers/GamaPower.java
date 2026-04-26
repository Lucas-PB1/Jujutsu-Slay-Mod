package jujutsumod.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;

import static jujutsumod.BasicMod.makeID;

public class GamaPower extends BasePower {
    public static final String POWER_ID = makeID("GamaPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public GamaPower(AbstractCreature owner, int amount) {
        super(POWER_ID, PowerType.DEBUFF, false, owner, amount);
        updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (!isPlayer) {
            flash();
            addToBot(new DamageAction(owner, new DamageInfo(AbstractDungeon.player, amount, DamageInfo.DamageType.HP_LOSS), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
            addToBot(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, amount));
        }
    }

    @Override
    public void updateDescription() {
        description = powerStrings.DESCRIPTIONS[0] + amount + powerStrings.DESCRIPTIONS[1] + amount + powerStrings.DESCRIPTIONS[2];
    }
}
