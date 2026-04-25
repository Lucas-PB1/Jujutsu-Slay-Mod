package jujutsumod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import jujutsumod.cards.RabbitEscape;

public class RabbitEscapeAction extends AbstractGameAction {
    private boolean freeToPlayOnce;
    private int block;
    private AbstractPlayer p;
    private int energyOnUse;
    private boolean upgraded;

    public RabbitEscapeAction(AbstractPlayer p, int block, boolean upgraded, boolean freeToPlayOnce, int energyOnUse) {
        this.p = p;
        this.block = block;
        this.upgraded = upgraded;
        this.freeToPlayOnce = freeToPlayOnce;
        this.energyOnUse = energyOnUse;
        this.duration = Settings.ACTION_DUR_XFAST;
        this.actionType = ActionType.BLOCK;
    }

    @Override
    public void update() {
        int effect = EnergyPanel.totalCount;
        if (this.energyOnUse != -1) {
            effect = this.energyOnUse;
        }
        if (this.p.hasRelic("Chemical X")) {
            effect += 2;
            this.p.getRelic("Chemical X").flash();
        }

        if (effect > 0) {
            for (int i = 0; i < effect; i++) {
                addToBot(new GainBlockAction(p, p, block));
                
                AbstractCard rabbit = new RabbitEscape();
                if (upgraded) rabbit.upgrade();
                rabbit.setCostForTurn(0);
                rabbit.exhaust = true;
                rabbit.rawDescription += " NL Exhaust.";
                rabbit.initializeDescription();
                addToBot(new MakeTempCardInHandAction(rabbit, 1));
            }

            if (!this.freeToPlayOnce) {
                this.p.energy.use(EnergyPanel.totalCount);
            }
        }
        this.isDone = true;
    }
}
