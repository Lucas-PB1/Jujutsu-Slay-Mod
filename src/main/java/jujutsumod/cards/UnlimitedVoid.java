package jujutsumod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import jujutsumod.character.Itadori;
import jujutsumod.patches.CustomTags;
import jujutsumod.powers.UnlimitedVoidPower;
import jujutsumod.util.CardStats;

public class UnlimitedVoid extends BaseCard {
    public static final String ID = makeID("UnlimitedVoid");
    private static final CardStats info = new CardStats(
            Itadori.Meta.CARD_COLOR,
            CardType.POWER,
            CardRarity.RARE,
            CardTarget.SELF,
            3
    );

    public UnlimitedVoid() {
        super(ID, info);
        setMagic(3, 2); // Base reduction 3, upgrade +2 = 5
        tags.add(CustomTags.LIMITLESS);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int reduction = magicNumber;
        int damage = upgraded ? 10 : 5;
        addToBot(new ApplyPowerAction(p, p, new UnlimitedVoidPower(p, reduction, damage), reduction));
    }
}
