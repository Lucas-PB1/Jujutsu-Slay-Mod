package jujutsumod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import jujutsumod.cards.HollowPurple;
import jujutsumod.cards.LapseBlue;
import jujutsumod.cards.ReversalRed;

public class PurpleFusionAction extends AbstractGameAction {
    private AbstractCard currentCard;

    public PurpleFusionAction(AbstractCard currentCard) {
        this.currentCard = currentCard;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        String targetID = currentCard.cardID.equals(LapseBlue.ID) ? ReversalRed.ID : LapseBlue.ID;
        AbstractCard otherCard = null;

        // Find the other card in discard pile
        for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
            if (c.cardID.equals(targetID)) {
                otherCard = c;
                break;
            }
        }

        if (otherCard != null) {
            // Fusion trigger!
            addToTop(new MakeTempCardInHandAction(new HollowPurple(), 1));
            addToTop(new ExhaustSpecificCardAction(otherCard, AbstractDungeon.player.discardPile));
            
            // Handle current card exhaustion correctly based on where it is
            if (AbstractDungeon.player.hand.contains(currentCard)) {
                addToTop(new ExhaustSpecificCardAction(currentCard, AbstractDungeon.player.hand));
            } else if (AbstractDungeon.player.discardPile.contains(currentCard)) {
                addToTop(new ExhaustSpecificCardAction(currentCard, AbstractDungeon.player.discardPile));
            } else if (AbstractDungeon.player.limbo.contains(currentCard)) {
                addToTop(new ExhaustSpecificCardAction(currentCard, AbstractDungeon.player.limbo));
            }
        }

        this.isDone = true;
    }
}
