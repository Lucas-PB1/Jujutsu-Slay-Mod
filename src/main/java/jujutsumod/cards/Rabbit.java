package jujutsumod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.LoseDexterityPower;
import jujutsumod.character.Itadori;
import jujutsumod.patches.CustomTags;
import jujutsumod.util.CardStats;

public class Rabbit extends BaseCard {
    public static final String ID = makeID("Rabbit");
    public Rabbit() {
        super(ID, 0, CardType.SKILL, CardTarget.SELF, CardRarity.SPECIAL, CardColor.COLORLESS, jujutsumod.util.TextureLoader.getCardTextureString("RabbitEscape", CardType.SKILL));
        setBlock(8, 3);
        setExhaust(true);
        tags.add(CustomTags.TEN_SHADOWS);
        tags.add(CustomTags.SHIKIGAMI);
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(3);
            initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, block));
        addToBot(new ApplyPowerAction(p, p, new DexterityPower(p, 1), 1));
        addToBot(new ApplyPowerAction(p, p, new LoseDexterityPower(p, 1), 1));
    }
}
