# Guia de Assets - Jujutsu Kaisen: The Cursed Spire

Este guia contem as especificacoes tecnicas para imagens usadas no mod. Use este arquivo como referencia rapida de medidas, nomes e pastas.

## 1. Convencoes gerais

- Formato padrao: `.png`.
- Fundo transparente para cartas recortadas, reliquias, icones, botoes e monstros.
- Nome do arquivo deve bater com o nome da classe Java quando existir uma classe correspondente.
- Evite espacos, acentos e caracteres especiais em nomes de arquivo.
- Preserve capitalizacao. Exemplo: `DivergentFist.png` e `DivergentFist_p.png`.

## 2. Cartas

Pasta:

- `src/main/resources/jujutsumod/images/cards/attack`
- `src/main/resources/jujutsumod/images/cards/skill`
- `src/main/resources/jujutsumod/images/cards/power`

Tamanhos:

| Uso | Nome | Tamanho |
| :--- | :--- | :--- |
| Arte padrao em jogo | `NomeDaCarta.png` | `250 x 190` |
| Retrato/preview | `NomeDaCarta_p.png` | `500 x 380` |

Regras:

- A proporcao deve ficar proxima de `1.31:1`.
- A versao `_p` deve ser a mesma composicao da versao padrao, apenas em maior resolucao.
- A imagem deve comunicar a acao da carta sem depender do texto.

## 3. Poderes

Pasta:

- `src/main/resources/jujutsumod/images/powers`
- `src/main/resources/jujutsumod/images/powers/large`

Tamanhos:

| Uso | Nome | Tamanho |
| :--- | :--- | :--- |
| Icone padrao | `NomePower.png` | `32 x 32` |
| Icone grande | `large/NomePower.png` | `128 x 128` |

Regras:

- O objeto principal deve ocupar cerca de `70%` da area.
- A silhueta precisa continuar legivel em `32 x 32`.
- Use contraste alto e formas simples.

## 4. Reliquias

Pasta:

- `src/main/resources/jujutsumod/images/relics`
- `src/main/resources/jujutsumod/images/relics/large`

Tamanhos:

| Uso | Nome | Tamanho |
| :--- | :--- | :--- |
| Icone padrao | `NomeRelic.png` | `128 x 128` |
| Icone grande | `large/NomeRelic.png` | `256 x 256` |

Regras:

- A arte nao deve ocupar o quadrado inteiro.
- Mantenha o objeto entre `60%` e `70%` do centro da imagem.
- Deixe margem transparente suficiente para nao tocar as bordas da UI.

## 5. Personagem

Pasta:

- `src/main/resources/jujutsumod/images/character`

Tamanhos:

| Asset | Caminho | Tamanho |
| :--- | :--- | :--- |
| Sprite principal | `main.png` | conforme animacao/escala do personagem |
| Ombro 1 | `shoulder.png` | `1920 x 1136` |
| Ombro 2 | `shoulder2.png` | `1920 x 1136` |
| Corpo derrotado | `corpse.png` | `512 x 512` |
| Botao de selecao | `select/button.png` | `200 x 200` |
| Retrato de selecao | `select/portrait.png` | `1920 x 1200` |

Regras:

- `select/button.png` deve ter fundo transparente e escala visual de aproximadamente `70%`.
- `select/portrait.png` pode manter cenario/fundo.
- Sprites de combate precisam alinhar bem com hitbox e sombra em jogo.

## 6. Monstros

Pasta sugerida:

- `src/main/resources/jujutsumod/images/monsters`

Tamanhos sugeridos:

| Categoria | Tamanho |
| :--- | :--- |
| Pequeno/minion | `400 x 400` |
| Normal/elite | `600 x 600` |
| Boss | `800 x 800` |

Regras:

- Fundo transparente.
- A base ou os pes devem ficar no centro inferior da imagem.
- O corpo principal deve ficar perto do centro para alinhar intencao, status e hitbox.
- Para inimigos voadores, deixe espaco inferior suficiente para a sombra.

## 7. Scripts

Use `scripts/process_images.py` para padronizar redimensionamento, recorte e transparencia.

```powershell
python scripts/process_images.py "imagem.png" card
python scripts/process_images.py "imagem.png" relic
python scripts/process_images.py "imagem.png" power
python scripts/process_images.py "imagem.png" select
python scripts/process_images.py "imagem.png" select --keep-bg
```

## 8. Checklist

- O arquivo esta na pasta correta.
- O nome bate com a classe Java ou com o ID esperado.
- O tamanho esta conforme este guia.
- O fundo transparente foi preservado quando necessario.
- A imagem continua legivel no menor tamanho usado pelo jogo.
- A versao grande e a versao pequena representam o mesmo asset.
