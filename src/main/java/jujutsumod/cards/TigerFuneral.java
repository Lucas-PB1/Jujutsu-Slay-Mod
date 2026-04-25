package jujutsumod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import jujutsumod.character.Itadori;
import jujutsumod.patches.CustomTags;
import jujutsumod.util.CardStats;

public class TigerFuneral extends BaseCard {
    public static final String ID = makeID("TigerFuneral");
    private static final CardStats info = new CardStats(
            Itadori.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.ENEMY,
            1
    );

    public TigerFuneral() {
        super(ID, info);
        setDamage(10, 4);
        setMagic(1, 1); // Strength gain
        tags.add(CustomTags.TEN_SHADOWS);
        tags.add(CustomTags.SHIKIGAMI);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new com.megacrit.cardcrawl.actions.common.DiscardAction(p, p, 1, false));
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HEAVY));
        
        boolean active = p.hasPower(jujutsumod.powers.TenShadowsTechniquePower.POWER_ID);
        if (!active) {
            for (AbstractCard c : AbstractDungeon.actionManager.cardsPlayedThisTurn) {
                if (c.hasTag(CustomTags.TEN_SHADOWS) && c != this) {
                    active = true;
                    break;
                }
            }
        }

        int strAmount = active ? magicNumber * 2 : magicNumber;
        addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, strAmount), strAmount));
    }

    @Override
    public void triggerOnGlowCheck() {
        boolean active = AbstractDungeon.player.hasPower(jujutsumod.powers.TenShadowsTechniquePower.POWER_ID);
        if (!active) {
            for (AbstractCard c : AbstractDungeon.actionManager.cardsPlayedThisTurn) {
                if (c.hasTag(CustomTags.TEN_SHADOWS)) {
                    active = true;
                    break;
                }
            }
        }
        this.glowColor = active ? AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy() : AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
    }
}
