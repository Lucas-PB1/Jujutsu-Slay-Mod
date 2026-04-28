package jujutsumod.registry;

import basemod.BaseMod;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.monsters.MonsterInfo;
import jujutsumod.monsters.Koguy;

public final class MonsterRegistry {
    private static final float KOGUY_EXORDIUM_WEIGHT = 2.0F;

    private MonsterRegistry() {}

    public static void registerMonsters() {
        BaseMod.addMonster(Koguy.ID, () -> new Koguy(0.0F, 0.0F));
        BaseMod.addMonsterEncounter(Exordium.ID, new MonsterInfo(Koguy.ID, KOGUY_EXORDIUM_WEIGHT));
    }
}
