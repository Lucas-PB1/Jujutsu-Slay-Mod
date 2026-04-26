package jujutsumod.actions;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.select.HandCardSelectScreen;
import jujutsumod.cards.BlackFlash;

public class InfuseBlackFlashAction extends AbstractGameAction {
    private AbstractPlayer p;

    public InfuseBlackFlashAction() {
        this.p = AbstractDungeon.player;
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

            AbstractDungeon.handCardSelectScreen.open("Choose an Attack to Infuse into Black Flash", 1, false, false);
            tickDuration();
            return;
        }

        if (AbstractDungeon.screen != AbstractDungeon.CurrentScreen.HAND_SELECT) {
            if (!AbstractDungeon.handCardSelectScreen.selectedCards.group.isEmpty()) {
                for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                    if (c.type == AbstractCard.CardType.ATTACK) {
                        addToTop(new MakeTempCardInHandAction(new BlackFlash(), 1));
                        addToTop(new ExhaustSpecificCardAction(c, p.hand));
                    } else {
                        // If they somehow selected a non-attack (shouldn't happen with filter, but I'll add one if possible)
                        p.hand.addToTop(c);
                    }
                }
                AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
            }
            this.isDone = true;
        }
        tickDuration();
    }
}
