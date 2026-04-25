package jujutsumod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import jujutsumod.character.Itadori;
import jujutsumod.patches.CustomTags;
import jujutsumod.util.CardStats;

public class Toad extends BaseCard {
    public static final String ID = makeID("Toad");
    private static final CardStats info = new CardStats(
            Itadori.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.COMMON,
            CardTarget.SELF_AND_ENEMY,
            1
    );

    public Toad() {
        super(ID, info);
        setBlock(7, 3);
        setMagic(3, 1);
        tags.add(CustomTags.TEN_SHADOWS);
        tags.add(CustomTags.SHIKIGAMI);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, block));
        if (isTenShadowsActive() && m != null) {
            addToBot(new ApplyPowerAction(m, p, new StrengthPower(m, -magicNumber), -magicNumber));
        }
    }

    @Override
    public void triggerOnGlowCheck() {
        this.glowColor = isTenShadowsActive() ? AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy() : AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
    }
}
