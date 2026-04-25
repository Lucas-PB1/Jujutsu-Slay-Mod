package jujutsumod.actions;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.select.HandCardSelectScreen;

public class InfusionAction extends AbstractGameAction {
    private AbstractPlayer p;
    private boolean reduceCost;

    public InfusionAction(boolean reduceCost) {
        this.p = AbstractDungeon.player;
        this.reduceCost = reduceCost;
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (this.p.hand.isEmpty()) {
                this.isDone = true;
                return;
            }
            if (this.p.hand.size() == 1) {
                processCard(this.p.hand.getTopCard());
                this.isDone = true;
                return;
            }
            AbstractDungeon.handCardSelectScreen.open("Infuse", 1, false, false);
            tickDuration();
            return;
        }

        boolean wereCardsSelected = ReflectionHacks.getPrivate(AbstractDungeon.handCardSelectScreen, HandCardSelectScreen.class, "wereCardsSelected");

        if (!wereCardsSelected) {
            if (!AbstractDungeon.handCardSelectScreen.selectedCards.group.isEmpty()) {
                for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                    processCard(c);
                    this.p.hand.addToTop(c);
                }
                ReflectionHacks.setPrivate(AbstractDungeon.handCardSelectScreen, HandCardSelectScreen.class, "wereCardsSelected", true);
                AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
                this.isDone = true;
            }
        }
        tickDuration();
    }

    private void processCard(AbstractCard c) {
        if (c.canUpgrade()) {
            c.upgrade();
        }
        if (this.reduceCost) {
            c.modifyCostForCombat(-1);
        }
        c.superFlash();
        c.applyPowers();
    }
}
