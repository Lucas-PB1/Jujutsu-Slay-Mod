package jujutsumod.actions;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.select.HandCardSelectScreen;
import jujutsumod.cards.Rabbit;

public class RabbitEscapeRedesignAction extends AbstractGameAction {
    private AbstractPlayer p;
    private boolean upgraded;

    public RabbitEscapeRedesignAction(boolean upgraded) {
        this.p = AbstractDungeon.player;
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.EXHAUST;
        this.upgraded = upgraded;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (this.p.hand.isEmpty()) {
                this.isDone = true;
                return;
            }

            AbstractDungeon.handCardSelectScreen.open("Exhaust to gain Rabbits", 99, true, true);
            tickDuration();
            return;
        }

        if (AbstractDungeon.screen != AbstractDungeon.CurrentScreen.HAND_SELECT) {
            if (!AbstractDungeon.handCardSelectScreen.selectedCards.group.isEmpty()) {
                int count = AbstractDungeon.handCardSelectScreen.selectedCards.group.size();
                for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                    this.p.hand.moveToExhaustPile(c);
                }
                
                for (int i = 0; i < count; i++) {
                    AbstractCard rabbit = new Rabbit();
                    if (upgraded) rabbit.upgrade();
                    addToBot(new MakeTempCardInHandAction(rabbit, 1));
                }
                
                AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
            }
            this.isDone = true;
        }
        tickDuration();
    }
}
