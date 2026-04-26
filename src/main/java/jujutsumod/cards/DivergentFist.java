package jujutsumod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import jujutsumod.character.Itadori;
import jujutsumod.powers.DivergentFistPower;
import jujutsumod.util.CardStats;

public class DivergentFist extends BaseCard {
    public static final String ID = makeID("DivergentFist");
    private static final CardStats info = new CardStats(
            Itadori.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.BASIC,
            CardTarget.ENEMY,
            1
    );

    public DivergentFist() {
        super(ID, info);
        setDamage(7, 3);
        this.cardsToPreview = new BlackFlash();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            this.upgradeName();
            this.name = CardCrawlGame.languagePack.getCardStrings(makeID("BlackFlash")).NAME;
            this.rawDescription = CardCrawlGame.languagePack.getCardStrings(makeID("BlackFlash")).DESCRIPTION;
            this.baseDamage = 20;
            this.cost = 2;
            this.costForTurn = 2;
            this.upgradedCost = true;
            this.upgraded = true;
            this.loadCardImage(jujutsumod.util.TextureLoader.getCardTextureString("BlackFlash", CardType.ATTACK));
            this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (upgraded) {
            addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
            int attackCount = jujutsumod.util.CombatUtils.countAttacksPlayedThisTurn(this);
            if (attackCount > 0) {
                addToBot(new GainEnergyAction(attackCount));
            }
        } else {
            addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
            addToBot(new ApplyPowerAction(m, p, new DivergentFistPower(m, p, damage), damage));
        }
    }
}
