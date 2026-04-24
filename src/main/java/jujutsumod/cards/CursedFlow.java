package jujutsumod.cards;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import jujutsumod.character.Itadori;
import jujutsumod.util.CardStats;

public class CursedFlow extends BaseCard {
    public static final String ID = makeID("CursedFlow");
    private static final CardStats info = new CardStats(
            Itadori.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.COMMON,
            CardTarget.SELF,
            1
    );

    public CursedFlow() {
        super(ID, info);
        setBlock(7, 3);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int finalBlock = block;
        
        // Verifica se a última carta jogada foi um ataque
        if (!AbstractDungeon.actionManager.cardsPlayedThisTurn.isEmpty()) {
            AbstractCard lastCard = AbstractDungeon.actionManager.cardsPlayedThisTurn.get(AbstractDungeon.actionManager.cardsPlayedThisTurn.size() - 1);
            if (lastCard.type == CardType.ATTACK) {
                finalBlock += 4;
            }
        }
        
        addToBot(new GainBlockAction(p, p, finalBlock));
    }
}
