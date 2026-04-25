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

public class MaxElephant extends BaseCard {
    public static final String ID = makeID("MaxElephant");
    private static final CardStats info = new CardStats(
            Itadori.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.ENEMY,
            2
    );

    public MaxElephant() {
        super(ID, info);
        setDamage(15, 5);
        tags.add(CustomTags.TEN_SHADOWS);
        tags.add(CustomTags.SHIKIGAMI);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SMASH));
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        int count = (int) AbstractDungeon.player.hand.group.stream()
                .filter(c -> c.hasTag(CustomTags.SHIKIGAMI) && c != this)
                .count();
        
        this.setCostForTurn(Math.max(0, this.cost - count));
    }

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c) {
        if (c.hasTag(CustomTags.SHIKIGAMI)) {
            this.applyPowers();
        }
    }

    @Override
    public void onMoveToDiscard() {
        this.costForTurn = this.cost;
        this.isCostModifiedForTurn = false;
    }
}
