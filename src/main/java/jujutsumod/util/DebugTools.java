package jujutsumod.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import jujutsumod.cards.Agito;
import jujutsumod.cards.CurtainLimitless;
import jujutsumod.cards.HollowPurple;
import jujutsumod.cards.InfinityBarrier;
import jujutsumod.cards.LapseBlue;
import jujutsumod.cards.Rabbit;
import jujutsumod.cards.RabbitEscape;
import jujutsumod.cards.ReversalRed;
import jujutsumod.cards.SixEyes;
import jujutsumod.cards.UnlimitedVoid;

public final class DebugTools {
    private static final int DEBUG_ENERGY_GAIN = 99;

    private DebugTools() {}

    public static void update() {
        if (AbstractDungeon.player == null || AbstractDungeon.currMapNode == null) {
            return;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.G)) {
            addTestCards();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.U)) {
            upgradeVisibleCards();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.H)) {
            healAndGainEnergy();
        }
    }

    private static void addTestCards() {
        AbstractCard[] cardsToTest = {
                new Agito(),
                new LapseBlue(),
                new ReversalRed(),
                new InfinityBarrier(),
                new HollowPurple(),
                new SixEyes(),
                new UnlimitedVoid(),
                new CurtainLimitless(),
                new RabbitEscape(),
                new Rabbit()
        };

        for (AbstractCard card : cardsToTest) {
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(card.makeStatEquivalentCopy()));
            AbstractDungeon.player.masterDeck.addToBottom(card.makeStatEquivalentCopy());
        }
    }

    private static void upgradeVisibleCards() {
        upgradeCards(AbstractDungeon.player.masterDeck.group);
        upgradeCards(AbstractDungeon.player.hand.group);
    }

    private static void upgradeCards(Iterable<AbstractCard> cards) {
        for (AbstractCard card : cards) {
            if (card.canUpgrade()) {
                card.upgrade();
            }
        }
    }

    private static void healAndGainEnergy() {
        AbstractDungeon.player.heal(AbstractDungeon.player.maxHealth);
        AbstractDungeon.player.gainEnergy(DEBUG_ENERGY_GAIN);
    }
}
