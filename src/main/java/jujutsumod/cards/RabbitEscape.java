package jujutsumod.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import jujutsumod.actions.RabbitEscapeAction;
import jujutsumod.character.Itadori;
import jujutsumod.patches.CustomTags;
import jujutsumod.util.CardStats;

public class RabbitEscape extends BaseCard {
    public static final String ID = makeID("RabbitEscape");
    private static final CardStats info = new CardStats(
            Itadori.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.COMMON,
            CardTarget.SELF,
            -1
    );

    public RabbitEscape() {
        super(ID, info);
        setBlock(4, 2);
        tags.add(CustomTags.TEN_SHADOWS);
        tags.add(CustomTags.SHIKIGAMI);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new RabbitEscapeAction(p, block, upgraded, freeToPlayOnce, energyOnUse));
    }
}
