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
        int count = 0;
        for (AbstractCard c : AbstractDungeon.actionManager.cardsPlayedThisTurn) {
            if (c.type == AbstractCard.CardType.ATTACK) {
                count++;
            }
        }
        return count;
    }

    public static boolean lastCardWasAttack(AbstractCard self) {
        if (AbstractDungeon.actionManager.cardsPlayedThisTurn.isEmpty()) {
            return false;
        }

        AbstractCard lastCard = AbstractDungeon.actionManager.cardsPlayedThisTurn.get(AbstractDungeon.actionManager.cardsPlayedThisTurn.size() - 1);

        if (lastCard == self) {
            if (AbstractDungeon.actionManager.cardsPlayedThisTurn.size() < 2) {
                return false;
            }
            lastCard = AbstractDungeon.actionManager.cardsPlayedThisTurn.get(AbstractDungeon.actionManager.cardsPlayedThisTurn.size() - 2);
        }

        return lastCard.type == AbstractCard.CardType.ATTACK;
    }

    public static boolean isFirstAttack(AbstractCard self) {
        return AbstractDungeon.actionManager.cardsPlayedThisCombat.stream()
                .noneMatch(c -> c.type == AbstractCard.CardType.ATTACK && c != self);
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
}
