package jujutsumod.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;
import static jujutsumod.BasicMod.makeID;

public class SukunaFinger extends BaseRelic {
    public static final String ID = makeID("SukunaFinger");

    public SukunaFinger() {
        super(ID, "SukunaFinger", RelicTier.STARTER, LandingSound.MAGICAL);
    }

    @Override
    public void atBattleStart() {
        flash();
        addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, 1), 1));
        addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
    }

    @Override
    public void onUseCard(AbstractCard c, com.megacrit.cardcrawl.actions.utility.UseCardAction action) {
        if (c.type == AbstractCard.CardType.ATTACK && AbstractDungeon.cardRandomRng.random(99) < 10) {
            flash();
            AbstractCard copy = c.makeStatEquivalentCopy();
            copy.cost = 0;
            copy.costForTurn = 0;
            copy.isCostModified = true;
            if (copy instanceof jujutsumod.cards.BaseCard) {
                ((jujutsumod.cards.BaseCard) copy).baseCost = 0;
            }
            copy.exhaust = true;
            addToBot(new com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction(copy));
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
