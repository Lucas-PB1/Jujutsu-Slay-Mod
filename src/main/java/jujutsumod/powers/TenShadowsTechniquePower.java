package jujutsumod.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import jujutsumod.patches.CustomTags;

import static jujutsumod.BasicMod.makeID;

public class TenShadowsTechniquePower extends BasePower {
    public static final String POWER_ID = makeID("TenShadowsTechniquePower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public TenShadowsTechniquePower(AbstractCreature owner, int amount) {
        super(POWER_ID, PowerType.BUFF, false, owner, amount);
        updateDescription();
    }

    @Override
    public void onAfterCardPlayed(AbstractCard card) {
        if (card.hasTag(CustomTags.TEN_SHADOWS)) {
            flash();
            boolean changed = false;
            if (card.baseDamage >= 0 && card.type == AbstractCard.CardType.ATTACK) {
                card.baseDamage += amount;
                changed = true;
            }
            if (card.baseBlock >= 0 && card.type == AbstractCard.CardType.SKILL) {
                card.baseBlock += amount;
                changed = true;
            }
            
            if (changed) {
                card.applyPowers();
                card.initializeDescription();
            }
        }
    }

    @Override
    public void updateDescription() {
        description = powerStrings.DESCRIPTIONS[0] + amount + powerStrings.DESCRIPTIONS[1];
    }
}
