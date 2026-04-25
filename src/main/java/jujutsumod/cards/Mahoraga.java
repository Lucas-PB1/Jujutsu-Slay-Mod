package jujutsumod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import jujutsumod.character.Itadori;
import jujutsumod.patches.CustomTags;
import jujutsumod.powers.MahoragaPower;
import jujutsumod.util.CardStats;

public class Mahoraga extends BaseCard {
    public static final String ID = makeID("Mahoraga");
    private static final CardStats info = new CardStats(
            Itadori.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.RARE,
            CardTarget.ENEMY,
            3
    );

    public Mahoraga() {
        super(ID, info);
        setDamage(20, 10);
        setMagic(5, 2); // Quantidade de adaptação por hit sofrido
        tags.add(CustomTags.TEN_SHADOWS);
        tags.add(CustomTags.SHIKIGAMI);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Dano massivo
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HEAVY));
        
        // Ganhar Artifact (Imunidade)
        addToBot(new ApplyPowerAction(p, p, new ArtifactPower(p, 2), 2));
        
        // Iniciar Adaptação
        addToBot(new ApplyPowerAction(p, p, new MahoragaPower(p, magicNumber), magicNumber));
    }
}
