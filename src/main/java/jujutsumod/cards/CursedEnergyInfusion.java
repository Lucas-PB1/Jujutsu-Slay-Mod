package jujutsumod.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import jujutsumod.actions.InfusionAction;
import jujutsumod.character.Itadori;
import jujutsumod.util.CardStats;

public class CursedEnergyInfusion extends BaseCard {
    public static final String ID = makeID("CursedEnergyInfusion");
    private static final CardStats info = new CardStats(
            Itadori.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            2
    );

    public CursedEnergyInfusion() {
        super(ID, info);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        boolean blackFlashPlayed = false;
        for (AbstractCard c : AbstractDungeon.actionManager.cardsPlayedThisTurn) {
            if (c.cardID.equals(BlackFlash.ID)) {
                blackFlashPlayed = true;
                break;
            }
        }
        
        addToBot(new InfusionAction(blackFlashPlayed));
    }
}
