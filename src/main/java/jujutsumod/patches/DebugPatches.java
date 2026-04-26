package jujutsumod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.campfire.CampfireSmithEffect;
import jujutsumod.BasicMod;

@SpirePatch(clz = CampfireSmithEffect.class, method = "update")
public class DebugPatches {
    @SpirePostfixPatch
    public static void Postfix(CampfireSmithEffect __instance) {
        if (BasicMod.DEBUG_MODE && __instance.isDone) {
            // Revert the room phase so the campfire stays open
            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.INCOMPLETE;
        }
    }
}
