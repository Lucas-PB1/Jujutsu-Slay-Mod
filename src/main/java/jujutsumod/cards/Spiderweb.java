package jujutsumod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import jujutsumod.character.Itadori;
import jujutsumod.util.CardStats;

public class Spiderweb extends BaseCard {
    public static final String ID = makeID("Spiderweb");
    private static final CardStats info = new CardStats(
            Itadori.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.ALL_ENEMY,
            3
    );

    public Spiderweb() {
        super(ID, info);
        setDamage(10, 4);
        setMagic(1);
        isMultiDamage = true;
        tags.add(jujutsumod.patches.CustomTags.SHRINE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAllEnemiesAction(p, multiDamage, damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_HEAVY));
        for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            addToBot(new ApplyPowerAction(mo, p, new VulnerablePower(mo, magicNumber, false), magicNumber));
        }
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        int count = jujutsumod.util.CombatUtils.countCardPlayedThisCombat(Cleave.ID);
        this.setCostForTurn(this.baseCost - count);
    }

    @Override
    public void onMoveToDiscard() {
        this.cost = this.baseCost;
        this.isCostModified = false;
    }
}
