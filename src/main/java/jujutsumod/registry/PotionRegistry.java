package jujutsumod.registry;

import basemod.BaseMod;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.potions.BloodPotion;
import com.megacrit.cardcrawl.potions.Elixir;
import com.megacrit.cardcrawl.potions.HeartOfIron;
import jujutsumod.character.Itadori;

public final class PotionRegistry {
    private PotionRegistry() {}

    public static void registerPotions() {
        BaseMod.addPotion(BloodPotion.class, Color.RED.cpy(), Color.FIREBRICK.cpy(), null, BloodPotion.POTION_ID, Itadori.Meta.YOUR_CHARACTER);
        BaseMod.addPotion(Elixir.class, Color.GRAY.cpy(), Color.WHITE.cpy(), null, Elixir.POTION_ID, Itadori.Meta.YOUR_CHARACTER);
        BaseMod.addPotion(HeartOfIron.class, Color.GOLD.cpy(), Color.ORANGE.cpy(), null, HeartOfIron.POTION_ID, Itadori.Meta.YOUR_CHARACTER);
    }
}
