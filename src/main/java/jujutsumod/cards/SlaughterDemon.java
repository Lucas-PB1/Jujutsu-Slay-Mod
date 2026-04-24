package jujutsumod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import jujutsumod.character.Itadori;
import jujutsumod.util.CardStats;

public class SlaughterDemon extends BaseCard {
    public static final String ID = makeID("SlaughterDemon");
    private static final CardStats info = new CardStats(
            Itadori.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.COMMON,
            CardTarget.ENEMY,
            1
    );

    public SlaughterDemon() {
        super(ID, info);
        setDamage(8, 3);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int finalDamage = damage;
        // Se for o primeiro ataque do combate (simplificado: se não jogou ataques ainda)
        if (AbstractDungeon.actionManager.cardsPlayedThisCombat.stream().noneMatch(c -> c.type == CardType.ATTACK)) {
            finalDamage += 4;
        }
        
        addToBot(new DamageAction(m, new DamageInfo(p, finalDamage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
    }
}
