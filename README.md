# Jujutsu Kaisen: The Cursed Spire

Mod de Slay the Spire baseado em Jujutsu Kaisen, com Yuji Itadori como personagem jogavel, cartas tematicas de Itadori, Sukuna, Dez Sombras, Gojo e Yuta, alem de poderes, reliquias, monstros, localizacao e assets proprios.

## Stack

- Java 8, exigido pelo ecossistema de Slay the Spire.
- Maven para validacao de JSON, compilacao e empacotamento.
- ModTheSpire, BaseMod e StSLib como dependencias de modding.
- Recursos em `src/main/resources/jujutsumod`.
- Scripts auxiliares de assets em `scripts/`.

## Estrutura

- `src/main/java/jujutsumod/BasicMod.java`: inicializador do mod, registro de strings, keywords, cartas, reliquias, pocoes, audio e monstros.
- `src/main/java/jujutsumod/character/Itadori.java`: personagem, deck inicial, reliquia inicial, cor de cartas e sprites.
- `src/main/java/jujutsumod/cards`: implementacoes das cartas. Todas devem estender `BaseCard`.
- `src/main/java/jujutsumod/powers`: poderes/buffs/debuffs. Todos devem estender `BasePower` quando possivel.
- `src/main/java/jujutsumod/actions`: acoes customizadas usadas por cartas e poderes.
- `src/main/java/jujutsumod/util`: helpers compartilhados.
- `src/main/resources/jujutsumod/localization`: textos por idioma.
- `src/main/resources/jujutsumod/images`: cartas, poderes, reliquias e personagem.
- `doc/`: guias de criacao, assets e planejamento de cartas.

## Build

O `pom.xml` espera encontrar Slay the Spire e os mods na instalacao Steam configurada em `steam.windows`, `steam.mac` ou `steam.linux`.

No Windows, este projeto tambem inclui:

```bat
compile.bat
```

Ou, com Java e Maven configurados:

```bash
mvn clean package
```

O build valida os JSONs em `src/main/resources/**/*.json` e copia o `.jar` para a pasta de mods do Slay the Spire durante o `package`.

## Fluxo para adicionar carta

1. Criar a classe em `src/main/java/jujutsumod/cards`.
2. Estender `BaseCard` e declarar `ID` com `makeID("NomeDaCarta")`.
3. Declarar `CardStats` com cor, tipo, raridade, alvo e custo.
4. Configurar numeros no construtor com `setDamage`, `setBlock`, `setMagic` ou `setCustomVar`.
5. Implementar `use(AbstractPlayer p, AbstractMonster m)`.
6. Adicionar textos em `eng/CardStrings.json` e `ptb/CardStrings.json`.
7. Adicionar artes em `images/cards/{attack|skill|power}` com versoes normal e `_p`.
8. Registrar em `BasicMod.receiveEditCards()`.
9. Rodar `mvn validate` ou `mvn clean package`.

Veja mais detalhes em `doc/card-guide.md`.

## Convencoes importantes

- IDs publicos usam o formato `JujutsuMod:Nome` via `BasicMod.makeID`.
- Nomes de arquivos de arte devem bater com o nome da classe sem o prefixo do mod.
- Placeholders de carta seguem o padrao do Slay the Spire: `!D!`, `!B!`, `!M!` e `!${modID}:KEY!`.
- Keywords devem existir nos arquivos `Keywords.json` dos idiomas suportados.
- Evite logica nova diretamente em `BasicMod`; prefira helpers, registries ou classes especificas quando a lista crescer.
- `DEBUG_MODE` deve permanecer `false` em builds normais.

## Documentacao util

- `AGENTS.md`: contexto rapido para agentes de IA e contribuidores.
- `doc/card-guide.md`: padrao para implementar cartas.
- `doc/asset-guide.md` e `doc/asset-handbook.md`: medidas e workflow visual.
- `doc/jjk-card-checklist.md`: backlog de cartas e sistemas planejados.
