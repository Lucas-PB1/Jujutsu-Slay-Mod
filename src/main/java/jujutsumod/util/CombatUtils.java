package jujutsumod.util;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import jujutsumod.patches.CustomTags;
import jujutsumod.powers.TenShadowsTechniquePower;

public class CombatUtils {

    public static int countTagsPlayedThisTurn(AbstractCard.CardTags tag) {
        int count = 0;
        for (AbstractCard c : AbstractDungeon.actionManager.cardsPlayedThisTurn) {
            if (c.hasTag(tag)) {
                count++;
            }
        }
        return count;
    }

    public static boolean hasTagBeenPlayedThisTurn(AbstractCard.CardTags tag, AbstractCard self) {
        for (AbstractCard c : AbstractDungeon.actionManager.cardsPlayedThisTurn) {
            if (c.hasTag(tag) && c != self) {
                return true;
            }
        }
        return false;
    }

    public static int countAttacksPlayedThisTurn() {
        return countAttacksPlayedThisTurn(null);
    }

    public static int countAttacksPlayedThisTurn(AbstractCard self) {
        int count = 0;
        for (AbstractCard c : AbstractDungeon.actionManager.cardsPlayedThisTurn) {
            if (c.type == AbstractCard.CardType.ATTACK && c != self) {
                count++;
            }
        }
        return count;
    }

    public static boolean lastCardWasAttack(AbstractCard self) {
        int size = AbstractDungeon.actionManager.cardsPlayedThisTurn.size();
        for (int i = size - 1; i >= 0; i--) {
            AbstractCard c = AbstractDungeon.actionManager.cardsPlayedThisTurn.get(i);
            if (c != self) {
                return c.type == AbstractCard.CardType.ATTACK;
            }
        }
        return false;
    }

    public static boolean isFirstAttack(AbstractCard self) {
        for (AbstractCard c : AbstractDungeon.actionManager.cardsPlayedThisCombat) {
            if (c.type == AbstractCard.CardType.ATTACK && c != self) {
                return false;
            }
        }
        return true;
    }

    public static boolean hasCardBeenPlayedThisTurn(String cardID) {
        for (AbstractCard c : AbstractDungeon.actionManager.cardsPlayedThisTurn) {
            if (c.cardID.equals(cardID)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isTenShadowsActive(AbstractCard self) {
        if (AbstractDungeon.player.hasPower(TenShadowsTechniquePower.POWER_ID)) {
            return true;
        }
        return hasTagBeenPlayedThisTurn(CustomTags.TEN_SHADOWS, self);
    }

    public static boolean isShrineActive(AbstractCard self) {
        return hasTagBeenPlayedThisTurn(CustomTags.SHRINE, self);
    }

    public static int countCardPlayedThisCombat(String cardID) {
        int count = 0;
        for (AbstractCard c : AbstractDungeon.actionManager.cardsPlayedThisCombat) {
            if (c.cardID.equals(cardID)) {
                count++;
            }
        }
        return count;
    }
}
