# AI Project Context

This file gives coding agents and contributors the minimum project context needed to make safe changes.

## Project Goal

This is a Slay the Spire mod named `Jujutsu Kaisen: The Cursed Spire`. It adds Yuji Itadori as a playable character with Jujutsu Kaisen themed cards, powers, relics, monsters, localization and image assets.

## Technical Constraints

- Use Java 8 syntax and APIs.
- Keep package names under `jujutsumod`.
- Build through Maven. `mvn validate` checks JSON resources; `mvn clean package` builds the jar.
- Dependencies are local system jars from a Steam Slay the Spire installation: ModTheSpire, BaseMod and StSLib.
- Do not assume dependency downloads from Maven Central will solve missing Slay the Spire jars.

## Key Entry Points

- `src/main/java/jujutsumod/BasicMod.java`
  - Main mod initializer.
  - Registers localization, keywords, cards, relics, potions, audio and monsters.
- `src/main/java/jujutsumod/character/Itadori.java`
  - Character stats, starter deck, starter relic, visual assets and card color.
- `src/main/java/jujutsumod/cards/BaseCard.java`
  - Shared card behavior, upgrade helpers and dynamic variables.
- `src/main/java/jujutsumod/powers/BasePower.java`
  - Shared power setup and texture loading.
- `src/main/resources/jujutsumod/localization`
  - Text loaded by BaseMod.

## Coding Conventions

- Cards extend `BaseCard`.
- Powers extend `BasePower` unless the engine requires a direct `AbstractPower` implementation.
- Card IDs use `public static final String ID = makeID("ClassName")`.
- Power IDs use `public static final String POWER_ID = makeID("ClassNamePower")`.
- Put shared combat checks in `CombatUtils` instead of duplicating loops over `cardsPlayedThisTurn` or `cardsPlayedThisCombat`.
- Put reusable asset path and texture behavior in `TextureLoader` or `BasicMod` path helpers.
- Keep `BasicMod` registration methods simple. If registration lists keep growing, introduce registries rather than adding more inline logic.
- Keep debug-only behavior behind `BasicMod.DEBUG_MODE`.

## Adding A Card

1. Add `src/main/java/jujutsumod/cards/NewCard.java`.
2. Define `ID` and a private static `CardStats`.
3. Call `super(ID, info)` in the constructor.
4. Use `setDamage`, `setBlock`, `setMagic` and `setCustomVar` for card numbers.
5. Implement `use(AbstractPlayer p, AbstractMonster m)`.
6. Add English and Portuguese entries to `CardStrings.json`.
7. Add card art to `images/cards/{attack|skill|power}/NewCard.png` and `NewCard_p.png`.
8. Register the card in `BasicMod.receiveEditCards()`.
9. Run `mvn validate` at minimum.

## Localization Rules

- Keep matching keys between `eng` and `ptb` JSON files.
- Use `NL` for line breaks in card text.
- Use Slay the Spire placeholders:
  - `!D!` damage
  - `!B!` block
  - `!M!` magic number
  - `!${modID}:KEY!` custom dynamic variables from `BaseCard`
- Keywords referenced with `*keyword*` must exist in `Keywords.json`.

## Asset Rules

- Card art:
  - Normal: `250x190`
  - Portrait: `500x380`, suffix `_p`
- Power icons:
  - Normal icons in `images/powers`
  - Large icons in `images/powers/large`
- Relics:
  - Normal icons in `images/relics`
  - Large icons in `images/relics/large`
- Prefer using `scripts/process_images.py` for generated or resized assets.

## Review Hotspots

- `BasicMod.receiveEditCards()` is currently manual and easy to forget.
- `BasicMod.receiveEditStrings()` and `receiveEditKeywords()` should stay in sync with all localization files.
- `DivergentFist` has custom upgrade behavior that transforms it into Black Flash; changes here should be tested in-game.
- `BaseCard` dynamic variables are powerful but stateful. When changing copy, upgrade or calculation logic, test previews, upgrades and AoE cards.
- Existing files may have local uncommitted edits. Do not revert unrelated changes.
