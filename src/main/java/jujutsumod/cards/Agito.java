package jujutsumod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import jujutsumod.character.Itadori;
import jujutsumod.patches.CustomTags;
import jujutsumod.powers.AgitoPower;
import jujutsumod.util.CardStats;

public class Agito extends BaseCard {
    public static final String ID = makeID("Agito");
    private static final CardStats info = new CardStats(
            Itadori.Meta.CARD_COLOR,
            CardType.POWER,
            CardRarity.RARE,
            CardTarget.SELF,
            3
    );

    public Agito() {
        super(ID, info);
        setMagic(8, 1);
        setCustomVar("BLOCK", 6, 1);
        setCustomVar("STR", 1, 1);
        setCustomVar("HEAL", 2, 1);
        tags.add(CustomTags.TEN_SHADOWS);
        tags.add(CustomTags.SHIKIGAMI);
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(1);
            upgradeCustomVar("BLOCK", 1);
            upgradeCustomVar("STR", 1);
            upgradeCustomVar("HEAL", 1);
            initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int dmg = magicNumber;
        int blk = customVar("BLOCK");
        int str = customVar("STR");
        int hl = customVar("HEAL");
        
        addToBot(new ApplyPowerAction(p, p, new AgitoPower(p, dmg, blk, str, hl), dmg));
    }
}
