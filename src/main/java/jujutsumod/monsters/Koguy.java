package jujutsumod.monsters;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;

import static jujutsumod.BasicMod.makeID;

public class Koguy extends AbstractMonster {
    public static final String ID = makeID("Koguy");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;

    // Move bytes
    private static final byte BITE = 1;
    private static final byte BUZZ = 2;

    public Koguy(float x, float y) {
        super(NAME, ID, 16, 0.0F, 0.0F, 150.0F, 150.0F, null, x, y);
        
        // HP setup based on ascension
        if (AbstractDungeon.ascensionLevel >= 7) {
            setHp(14, 18);
        } else {
            setHp(12, 16);
        }

        // Damage setup
        int biteDmg = 5;
        if (AbstractDungeon.ascensionLevel >= 2) {
            biteDmg = 6;
        }
        this.damage.add(new DamageInfo(this, biteDmg));
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case BITE:
                addToBot(new AnimateSlowAttackAction(this));
                addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                break;
            case BUZZ:
                addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, 1, true), 1));
                break;
        }
        addToBot(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int num) {
        // AI Logic: 60% Bite, 40% Buzz
        if (num < 60) {
            setMove(MOVES[0], BITE, Intent.ATTACK, this.damage.get(0).base);
        } else {
            setMove(MOVES[1], BUZZ, Intent.DEBUFF);
        }
    }
}
