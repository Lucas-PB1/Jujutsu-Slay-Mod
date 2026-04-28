# Guia de Criacao de Cartas - Jujutsu Mod

Este guia define o padrao para criar e revisar cartas do mod. Ele tambem serve como contexto para agentes de IA.

## 1. Arquivos envolvidos

Para uma carta nova, normalmente voce altera:

- `src/main/java/jujutsumod/cards/NomeDaCarta.java`
- `src/main/resources/jujutsumod/localization/eng/CardStrings.json`
- `src/main/resources/jujutsumod/localization/ptb/CardStrings.json`
- `src/main/resources/jujutsumod/images/cards/{attack|skill|power}/NomeDaCarta.png`
- `src/main/resources/jujutsumod/images/cards/{attack|skill|power}/NomeDaCarta_p.png`
- `src/main/java/jujutsumod/BasicMod.java`, em `receiveEditCards()`

## 2. Estrutura Java padrao

Cartas devem estender `BaseCard`.

```java
package jujutsumod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import jujutsumod.character.Itadori;
import jujutsumod.util.CardStats;

public class MinhaCarta extends BaseCard {
    public static final String ID = makeID("MinhaCarta");

    private static final CardStats info = new CardStats(
            Itadori.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.COMMON,
            CardTarget.ENEMY,
            1
    );

    public MinhaCarta() {
        super(ID, info);
        setDamage(6, 3);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(
                m,
                new DamageInfo(p, damage, damageTypeForTurn),
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL
        ));
    }
}
```

## 3. Numeros e upgrades

Prefira os helpers de `BaseCard`:

- `setDamage(base, upgrade)`
- `setBlock(base, upgrade)`
- `setMagic(base, upgrade)`
- `setCostUpgrade(novoCusto)`
- `setExhaust(base, upgraded)`
- `setEthereal(base, upgraded)`
- `setInnate(base, upgraded)`
- `setSelfRetain(base, upgraded)`
- `setCustomVar("KEY", base, upgrade)` para placeholders como `!${modID}:KEY!`

Evite sobrescrever `upgrade()` quando os helpers resolvem. Sobrescreva apenas quando a carta muda de identidade, arte, preview ou regra de upgrade.

## 4. Localizacao

Adicione a mesma chave em `eng/CardStrings.json` e `ptb/CardStrings.json`.

```json
"${modID}:MinhaCarta": {
  "NAME": "Minha Carta",
  "DESCRIPTION": "Causa !D! de dano.",
  "UPGRADE_DESCRIPTION": "Causa !D! de dano. NL Compre 1 carta."
}
```

Placeholders comuns:

- `!D!`: dano
- `!B!`: bloqueio
- `!M!`: magic number
- `!${modID}:KEY!`: variavel customizada registrada por `setCustomVar`
- `NL`: quebra de linha
- `*keyword*`: keyword carregada de `Keywords.json`

Se usar uma keyword nova, adicione em todos os idiomas suportados.

## 5. Imagens

As imagens devem seguir o nome da classe sem prefixo:

- `NomeDaCarta.png`: arte padrao em jogo, `250 x 190`
- `NomeDaCarta_p.png`: retrato/preview, `500 x 380`

Pastas:

- `images/cards/attack`
- `images/cards/skill`
- `images/cards/power`

Use `scripts/process_images.py` quando precisar gerar os tamanhos finais.

## 6. Registro

Enquanto o projeto usa registro manual, adicione a carta em `BasicMod.receiveEditCards()`:

```java
BaseMod.addCard(new jujutsumod.cards.MinhaCarta());
```

Mantenha cartas relacionadas proximas na lista para facilitar leitura por arquipo/sistema.

## 7. Checklist de revisao

- A classe compila em Java 8.
- A carta estende `BaseCard`.
- `ID` bate com a chave de localizacao e com os nomes dos assets.
- `CardStats` usa tipo, raridade, alvo e custo corretos.
- Textos existem em `eng` e `ptb`.
- Placeholders usados no texto foram configurados no construtor.
- Keywords usadas no texto existem em `Keywords.json`.
- A carta foi registrada em `BasicMod.receiveEditCards()`.
- Artes normal e `_p` existem na pasta correta.
- `mvn validate` passa.

## 8. Sinais de alerta

- Duplicar loops de combate dentro de varias cartas: prefira `CombatUtils`.
- Colocar regras complexas em `BasicMod`: prefira actions, powers ou utils.
- Sobrescrever `upgrade()` para um upgrade simples de numero: use os helpers.
- Usar texto em apenas um idioma: mantenha `eng` e `ptb` sincronizados.
- Referenciar assets com capitalizacao diferente do nome real do arquivo.
