package jujutsumod.powers;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.utility.ScryAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import jujutsumod.patches.CustomTags;

import static jujutsumod.BasicMod.makeID;

public class SixEyesPower extends BasePower {
    public static final String POWER_ID = makeID("SixEyesPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private int scryAmount;
    private boolean reduceCost;

    public SixEyesPower(AbstractCreature owner, int energyAmount, int scryAmount, boolean reduceCost) {
        super(POWER_ID, PowerType.BUFF, false, owner, energyAmount);
        this.scryAmount = scryAmount;
        this.reduceCost = reduceCost;
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        // stackAmount is energyAmount.
        // If energyAmount is 2, it's an upgraded version.
        if (stackAmount >= 2) {
            this.scryAmount += 5;
            this.reduceCost = true;
        } else {
            this.scryAmount += 3;
        }
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        flash();
        addToBot(new ScryAction(scryAmount));
        addToBot(new GainEnergyAction(amount));
    }

    @Override
    public void onInitialApplication() {
        if (reduceCost) {
            reduceCosts();
        }
    }

    @Override
    public void onCardDraw(AbstractCard card) {
        if (reduceCost && card.hasTag(CustomTags.LIMITLESS)) {
            if (card.cost >= 1) {
                card.costForTurn = card.cost - 1;
                card.isCostModifiedForTurn = true;
            }
        }
    }

    private void reduceCosts() {
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            if (c.hasTag(CustomTags.LIMITLESS) && c.cost >= 1) {
                c.costForTurn = c.cost - 1;
                c.isCostModifiedForTurn = true;
            }
        }
    }

    @Override
    public void updateDescription() {
        description = powerStrings.DESCRIPTIONS[0] + scryAmount + powerStrings.DESCRIPTIONS[1] + amount + powerStrings.DESCRIPTIONS[2];
        if (reduceCost) {
            description += powerStrings.DESCRIPTIONS[3];
        }
    }
}
