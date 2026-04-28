package jujutsumod.registry;

import basemod.BaseMod;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import jujutsumod.cards.Agito;
import jujutsumod.cards.AuthenticMutualLove;
import jujutsumod.cards.BlackFlash;
import jujutsumod.cards.Cleave;
import jujutsumod.cards.CurtainLimitless;
import jujutsumod.cards.CursedEnergyInfusion;
import jujutsumod.cards.CursedEnergyReinforcement;
import jujutsumod.cards.CursedFlow;
import jujutsumod.cards.Defend;
import jujutsumod.cards.Dismantle;
import jujutsumod.cards.DivergentFist;
import jujutsumod.cards.DivineDog;
import jujutsumod.cards.DivineFlame;
import jujutsumod.cards.Gama;
import jujutsumod.cards.GreatSerpent;
import jujutsumod.cards.HollowPurple;
import jujutsumod.cards.ImmeasurableCursedEnergy;
import jujutsumod.cards.InfinityBarrier;
import jujutsumod.cards.KatanaStrike;
import jujutsumod.cards.LapseBlue;
import jujutsumod.cards.Mahoraga;
import jujutsumod.cards.MalevolentShrine;
import jujutsumod.cards.MartialArtsMastery;
import jujutsumod.cards.MaxElephant;
import jujutsumod.cards.Nue;
import jujutsumod.cards.PiercingOx;
import jujutsumod.cards.PureLoveBeam;
import jujutsumod.cards.QueenOfCurses;
import jujutsumod.cards.Rabbit;
import jujutsumod.cards.RabbitEscape;
import jujutsumod.cards.ReversalRed;
import jujutsumod.cards.RikaProtectiveEmbrace;
import jujutsumod.cards.RikaSoulGrasp;
import jujutsumod.cards.RoundDeer;
import jujutsumod.cards.SixEyes;
import jujutsumod.cards.SlaughterDemon;
import jujutsumod.cards.Spiderweb;
import jujutsumod.cards.Strike;
import jujutsumod.cards.TenShadowsTechnique;
import jujutsumod.cards.TigerFuneral;
import jujutsumod.cards.UnlimitedVoid;
import jujutsumod.character.Itadori;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public final class CardRegistry {
    private static final List<Supplier<AbstractCard>> CARDS = Arrays.asList(
            Strike::new,
            Defend::new,
            DivergentFist::new,
            BlackFlash::new,
            CursedEnergyInfusion::new,
            SlaughterDemon::new,
            CursedFlow::new,
            MartialArtsMastery::new,
            CursedEnergyReinforcement::new,
            Dismantle::new,
            Cleave::new,
            Spiderweb::new,
            DivineFlame::new,
            MalevolentShrine::new,
            DivineDog::new,
            Nue::new,
            Gama::new,
            Mahoraga::new,
            GreatSerpent::new,
            MaxElephant::new,
            RabbitEscape::new,
            RoundDeer::new,
            PiercingOx::new,
            TigerFuneral::new,
            Agito::new,
            TenShadowsTechnique::new,
            Rabbit::new,
            LapseBlue::new,
            ReversalRed::new,
            InfinityBarrier::new,
            HollowPurple::new,
            SixEyes::new,
            UnlimitedVoid::new,
            CurtainLimitless::new,
            KatanaStrike::new,
            RikaProtectiveEmbrace::new,
            ImmeasurableCursedEnergy::new,
            RikaSoulGrasp::new,
            QueenOfCurses::new,
            AuthenticMutualLove::new,
            PureLoveBeam::new
    );

    private CardRegistry() {}

    public static void registerCards() {
        for (Supplier<AbstractCard> card : CARDS) {
            BaseMod.addCard(card.get());
        }
    }

    public static void unlockCharacterCards() {
        for (AbstractCard card : CardLibrary.getAllCards()) {
            if (card.color == Itadori.Meta.CARD_COLOR) {
                UnlockTracker.unlockCard(card.cardID);
            }
        }
    }
}
