# Guia de Criação de Cartas - Jujutsu Mod

Este documento descreve os padrões para criar novas cartas para o mod do Itadori.

## 1. Imagens (Arte da Carta)

As imagens devem ser salvas em `src/main/resources/jujutsumod/images/cards/[tipo]/`.

### Dimensões e Formatos
- **Arte Padrão (In-game)**:
  - **Tamanho**: 250 x 190 pixels
  - **Formato**: PNG
  - **Exemplo**: `default.png`
- **Arte de Retrato (Preview/Zoom)**:
  - **Tamanho**: 500 x 380 pixels
  - **Formato**: PNG
  - **Nomenclatura**: Deve terminar com `_p` (ex: `default_p.png`)

### Pastas por Tipo
- `attack/`: Para cartas de Ataque.
- `skill/`: Para cartas de Habilidade.
- `power/`: Para cartas de Poder.

---

## 2. Implementação em Java

As cartas devem estender a classe `BaseCard`.

### Estrutura Básica
```java
public class MinhaCarta extends BaseCard {
    public static final String ID = makeID("MinhaCarta");
    private static final CardStats info = new CardStats(
        Itadori.Meta.CARD_COLOR, // Cor da carta
        CardType.ATTACK,         // Tipo
        CardRarity.BASIC,       // Raridade
        CardTarget.ENEMY,        // Alvo
        1                        // Custo de energia
    );

    public MinhaCarta() {
        super(ID, info);
        setDamage(6, 3); // Dano base: 6, Upgrade: +3
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AttackEffect.SLASH_DIAGONAL));
    }
}
```

---

## 3. Localização (Texto)

Adicione o texto da carta em `src/main/resources/jujutsumod/localization/eng/CardStrings.json`:

```json
"${modID}:MinhaCarta": {
  "NAME": "Nome da Carta",
  "DESCRIPTION": "Causa !D! de dano."
}
```
- `!D!` é um placeholder para o dano.
- `!B!` é para o bloqueio (Block).
- `!M!` é para o valor mágico (Magic Number).

---

## 4. Registro

Não esqueça de registrar a carta na classe `BasicMod` dentro do método `receiveEditCards()` (ou similar via anotação se usar AutoAdd). No nosso caso, usamos a anotação `@SpireInitializer` e assinantes.
