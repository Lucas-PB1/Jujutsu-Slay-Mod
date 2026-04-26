package jujutsumod.powers;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.PowerStrings;

import static jujutsumod.BasicMod.makeID;

import java.util.ArrayList;
import java.util.List;

public class AuthenticMutualLovePower extends BasePower {
    public static final String POWER_ID = makeID("AuthenticMutualLovePower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public AuthenticMutualLovePower(AbstractCreature owner, int amount) {
        super(POWER_ID, PowerType.BUFF, false, owner, amount);
    }

    @Override
    public void atStartOfTurnPostDraw() {
        flash();
        // Add 1 Focus
        addToBot(new com.megacrit.cardcrawl.actions.common.ApplyPowerAction(owner, owner, new com.megacrit.cardcrawl.powers.FocusPower(owner, amount), amount));
        
        // Add random card
        AbstractCard card = getRandomOtherClassCard();
        if (card != null) {
            card.setCostForTurn(0);
            addToBot(new MakeTempCardInHandAction(card, 1));
        }
    }

    private AbstractCard getRandomOtherClassCard() {
        List<AbstractCard> cards = new ArrayList<>();
        for (AbstractCard c : CardLibrary.getAllCards()) {
            if (c.type != AbstractCard.CardType.CURSE && c.type != AbstractCard.CardType.STATUS && 
                c.rarity != AbstractCard.CardRarity.SPECIAL && c.rarity != AbstractCard.CardRarity.BASIC &&
                c.color != AbstractCard.CardColor.COLORLESS && c.color != AbstractDungeon.player.getCardColor()) {
                cards.add(c);
            }
        }
        if (cards.isEmpty()) return null;
        return cards.get(AbstractDungeon.cardRandomRng.random(cards.size() - 1)).makeCopy();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }
}
