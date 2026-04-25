package jujutsumod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import jujutsumod.character.Itadori;
import jujutsumod.patches.CustomTags;
import jujutsumod.util.CardStats;

public class PiercingOx extends BaseCard {
    public static final String ID = makeID("PiercingOx");
    private static final CardStats info = new CardStats(
            Itadori.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.ENEMY,
            2
    );

    public PiercingOx() {
        super(ID, info);
        setExhaust(true);
        tags.add(CustomTags.TEN_SHADOWS);
        tags.add(CustomTags.SHIKIGAMI);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int totalCards = AbstractDungeon.actionManager.cardsPlayedThisCombat.size();
        
        boolean active = p.hasPower(jujutsumod.powers.TenShadowsTechniquePower.POWER_ID);
        if (!active) {
            for (AbstractCard c : AbstractDungeon.actionManager.cardsPlayedThisTurn) {
                if (c.hasTag(CustomTags.TEN_SHADOWS) && c != this) {
                    active = true;
                    break;
                }
            }
        }

        int finalDamage = totalCards;
        if (active) {
            finalDamage *= 2;
        }

        addToBot(new DamageAction(m, new DamageInfo(p, finalDamage, damageTypeForTurn), AbstractGameAction.AttackEffect.SMASH));
    }

    @Override
    public void applyPowers() {
        int totalCards = AbstractDungeon.actionManager.cardsPlayedThisCombat.size();
        int originalBase = this.baseDamage;
        this.baseDamage = totalCards + originalBase; // Mantém o bônus acumulado da Técnica
        super.applyPowers();
        this.baseDamage = originalBase; // Restaura para não acumular infinitamente por frame
        
        boolean active = AbstractDungeon.player.hasPower(jujutsumod.powers.TenShadowsTechniquePower.POWER_ID);
        if (!active) {
            for (AbstractCard c : AbstractDungeon.actionManager.cardsPlayedThisTurn) {
                if (c.hasTag(CustomTags.TEN_SHADOWS) && c != this) {
                    active = true;
                    break;
                }
            }
        }
        if (active) {
            this.damage *= 2;
        }
        this.isDamageModified = this.damage != totalCards;
        this.rawDescription = cardStrings.DESCRIPTION + cardStrings.EXTENDED_DESCRIPTION[0];
        initializeDescription();
    }

    @Override
    public void onMoveToDiscard() {
        this.rawDescription = cardStrings.DESCRIPTION;
        initializeDescription();
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
