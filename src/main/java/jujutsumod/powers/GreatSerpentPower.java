package jujutsumod.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.PoisonPower;
import jujutsumod.patches.CustomTags;

import static jujutsumod.BasicMod.makeID;

public class GreatSerpentPower extends BasePower {
    public static final String POWER_ID = makeID("GreatSerpentPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public GreatSerpentPower(AbstractCreature owner) {
        super(POWER_ID, PowerType.BUFF, false, owner, 1);
        updateDescription();
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (damageAmount > 0 && target != this.owner && info.type == DamageInfo.DamageType.NORMAL) {
            AbstractCard c = com.megacrit.cardcrawl.dungeons.AbstractDungeon.actionManager.cardsPlayedThisTurn.get(com.megacrit.cardcrawl.dungeons.AbstractDungeon.actionManager.cardsPlayedThisTurn.size() - 1);
            if (c.hasTag(CustomTags.TEN_SHADOWS)) {
                this.flash();
                addToTop(new ApplyPowerAction(target, owner, new PoisonPower(target, owner, damageAmount), damageAmount));
            }
        }
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        if (card.hasTag(CustomTags.TEN_SHADOWS) && card.type == AbstractCard.CardType.ATTACK) {
            addToBot(new RemoveSpecificPowerAction(owner, owner, this));
        }
    }

    @Override
    public void updateDescription() {
        description = powerStrings.DESCRIPTIONS[0];
    }
}
