package jujutsumod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.LoseDexterityPower;
import jujutsumod.character.Itadori;
import jujutsumod.patches.CustomTags;
import jujutsumod.util.CardStats;

public class Nue extends BaseCard {
    public static final String ID = makeID("Nue");
    private static final CardStats info = new CardStats(
            Itadori.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.ENEMY,
            2
    );

    public Nue() {
        super(ID, info);
        setDamage(12, 4);
        setCostUpgrade(1);
        tags.add(CustomTags.TEN_SHADOWS);
        tags.add(CustomTags.SHIKIGAMI);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.LIGHTNING));
        
        if (m != null && m.currentBlock == 0) {
            addToBot(new ApplyPowerAction(p, p, new DexterityPower(p, 2), 2));
            addToBot(new ApplyPowerAction(p, p, new LoseDexterityPower(p, 2), 2));
        }

        if (isTenShadowsActive()) {
            addToBot(new GainEnergyAction(1));
        }
    }

    @Override
    public void triggerOnGlowCheck() {
        this.glowColor = isTenShadowsActive() ? AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy() : AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
    }
}
