package jujutsumod.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import jujutsumod.patches.CustomTags;

import java.util.HashSet;
import java.util.Set;

import static jujutsumod.BasicMod.makeID;

public class TenShadowsTechniquePower extends BasePower {
    public static final String POWER_ID = makeID("TenShadowsTechniquePower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    
    private final Set<String> uniqueShadowsPlayed = new HashSet<>();
    private boolean fullyMastered = false;

    public TenShadowsTechniquePower(AbstractCreature owner, int amount) {
        super(POWER_ID, PowerType.BUFF, false, owner, amount);
        this.priority = 100; // Run late
        updateDescription();
    }

    @Override
    public void onInitialApplication() {
        addToBot(new ApplyPowerAction(owner, owner, new StrengthPower(owner, amount), amount));
        addToBot(new ApplyPowerAction(owner, owner, new DexterityPower(owner, amount), amount));
        
        for (AbstractCard c : AbstractDungeon.actionManager.cardsPlayedThisCombat) {
            checkCard(c);
        }
    }

    private void checkCard(AbstractCard card) {
        if (card.hasTag(CustomTags.TEN_SHADOWS) && !card.cardID.equals(jujutsumod.cards.TenShadowsTechnique.ID)) {
            uniqueShadowsPlayed.add(card.cardID);
            if (uniqueShadowsPlayed.size() >= 10 && !fullyMastered) {
                fullyMastered = true;
                flash();
            }
        }
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.hasTag(CustomTags.TEN_SHADOWS) && !card.purgeOnUse && fullyMastered && !card.cardID.equals(jujutsumod.cards.TenShadowsTechnique.ID)) {
            flash();
            AbstractMonster m = null;
            if (action.target != null) {
                m = (AbstractMonster)action.target;
            }

            AbstractCard tmp = card.makeSameInstanceOf();
            AbstractDungeon.player.limbo.addToBottom(tmp);
            tmp.current_x = card.current_x;
            tmp.current_y = card.current_y;
            tmp.target_x = (float) Settings.WIDTH / 2.0F - 300.0F * Settings.scale;
            tmp.target_y = (float)Settings.HEIGHT / 2.0F;
            tmp.calculateCardDamage(m);
            tmp.purgeOnUse = true;
            AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(tmp, m, card.energyOnUse, true, true), true);
        }
        
        checkCard(card);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        if (!fullyMastered) {
            description = powerStrings.DESCRIPTIONS[0] + uniqueShadowsPlayed.size() + powerStrings.DESCRIPTIONS[1];
        } else {
            description = powerStrings.DESCRIPTIONS[2];
        }
    }
}
