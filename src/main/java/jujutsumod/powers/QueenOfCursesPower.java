package jujutsumod.powers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import jujutsumod.patches.CustomTags;

import static jujutsumod.BasicMod.makeID;

public class QueenOfCursesPower extends BasePower {
    public static final String POWER_ID = makeID("QueenOfCursesPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public QueenOfCursesPower(AbstractCreature owner, int amount) {
        super(POWER_ID, PowerType.BUFF, false, owner, amount);
    }

    @Override
    public float atDamageGive(float damage, com.megacrit.cardcrawl.cards.DamageInfo.DamageType type, AbstractCard card) {
        if (card.hasTag(CustomTags.SHIKIGAMI)) {
            return damage * (1.0f + (amount * 0.5f));
        }
        return damage;
    }

    @Override
    public float modifyBlock(float blockAmount, AbstractCard card) {
        if (card.hasTag(CustomTags.SHIKIGAMI)) {
            return blockAmount * (1.0f + (amount * 0.5f));
        }
        return blockAmount;
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + (amount * 50) + DESCRIPTIONS[1];
    }
}
