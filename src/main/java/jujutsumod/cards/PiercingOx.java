package jujutsumod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import jujutsumod.character.Itadori;
import jujutsumod.patches.CustomTags;
import jujutsumod.util.CardStats;
import jujutsumod.util.CombatUtils;

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
        setMagic(2, 1);
        setCostUpgrade(1);
        setExhaust(true);
        tags.add(CustomTags.TEN_SHADOWS);
        tags.add(CustomTags.SHIKIGAMI);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SMASH));
    }

    @Override
    public void applyPowers() {
        int count = CombatUtils.countTenShadowsOrShikigamiPlayedThisCombat();
        this.baseDamage = count * magicNumber;
        super.applyPowers();
        this.rawDescription = cardStrings.DESCRIPTION;
        initializeDescription();
    }

    @Override
    public void calculateCardDamage(AbstractMonster m) {
        int count = CombatUtils.countTenShadowsOrShikigamiPlayedThisCombat();
        this.baseDamage = count * magicNumber;
        super.calculateCardDamage(m);
        this.rawDescription = cardStrings.DESCRIPTION;
        initializeDescription();
    }
}
