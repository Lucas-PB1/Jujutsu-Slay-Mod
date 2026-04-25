package jujutsumod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import jujutsumod.character.Itadori;
import jujutsumod.util.CardStats;

public class Cleave extends BaseCard {
    public static final String ID = makeID("Cleave");
    private static final CardStats info = new CardStats(
            Itadori.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.ENEMY,
            1
    );

    public Cleave() {
        super(ID, info);
        setDamage(8, 3);
        tags.add(jujutsumod.patches.CustomTags.SHRINE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int finalDamage = damage;
        if (m != null && m.currentBlock > 0) {
            finalDamage = (int)(finalDamage * 1.5f);
        }
        addToBot(new DamageAction(m, new DamageInfo(p, finalDamage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
    }
}
