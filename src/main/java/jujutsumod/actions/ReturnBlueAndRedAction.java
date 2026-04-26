package jujutsumod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import jujutsumod.cards.LapseBlue;
import jujutsumod.cards.ReversalRed;

public class ReturnBlueAndRedAction extends AbstractGameAction {
    public ReturnBlueAndRedAction() {
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    @Override
    public void update() {
        CardGroup exhaustPile = AbstractDungeon.player.exhaustPile;
        AbstractCard blue = null;
        AbstractCard red = null;

        for (AbstractCard c : exhaustPile.group) {
            if (blue == null && c.cardID.equals(LapseBlue.ID)) {
                blue = c;
            } else if (red == null && c.cardID.equals(ReversalRed.ID)) {
                red = c;
            }
            if (blue != null && red != null) break;
        }

        if (blue != null) {
            exhaustPile.removeCard(blue);
            AbstractDungeon.player.hand.addToHand(blue);
            blue.unfadeOut();
            blue.lighten(false);
        }
        if (red != null) {
            exhaustPile.removeCard(red);
            AbstractDungeon.player.hand.addToHand(red);
            red.unfadeOut();
            red.lighten(false);
        }

        AbstractDungeon.player.hand.refreshHandLayout();
        this.isDone = true;
    }
}
