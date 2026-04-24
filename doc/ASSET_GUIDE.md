# Jujutsu Kaisen Mod - Asset Guide

Este guia contém as especificações técnicas para todas as imagens utilizadas no mod do Itadori.

## 1. Cartas (Cards)
As cartas utilizam dois tamanhos. O script de processamento cria ambos automaticamente.
- **Portrait (Grande)**: `500 x 380` px. (Usada quando a carta é focada/ampliada).
- **Standard (Pequena)**: `250 x 190` px. (Usada na mão do jogador).
- **Proporção**: `1.31 : 1`.
- **Dica**: Use o script `process_images.py [arquivo] card` para gerar ambos com o corte correto.

## 2. Relíquias (Relics)
Para parecerem oficiais, as relíquias **não devem** ocupar o quadrado inteiro.
- **Standard**: `128 x 128` px.
- **Large**: `256 x 256` px. (Usada no compêndio).
- **Padding**: A arte deve ocupar cerca de **60% a 70%** do centro da imagem. Deixe o restante como margem transparente.

## 3. Seleção de Personagem (Character Select)
- **Portrait (Background)**: `1920 x 1200` px.
  - Pode ter fundo (cenário).
  - Use `--keep-bg` no script para não remover o cenário de Shibuya.
- **Button (Ícone)**: `200 x 200` px.
  - **Importante**: Deve ser centralizado com bastante margem (**70% de escala**).
  - Sem fundo (transparente).

## 4. Combate (Character Sprites)
- **Shoulder (Costas)**: `1920 x 1136` px. (Visão do jogador durante o combate).
- **Corpse (Morte)**: `512 x 512` px. (Imagem que aparece quando o HP chega a 0).

## 5. Como usar o Script de Precisão
O script `scripts/process_images.py` deve ser usado para garantir o redimensionamento e remoção de fundo (via IA).

**Comandos:**
```bash
# Para Cartas (cria normal e _p)
python scripts/process_images.py "imagem.png" card

# Para Relíquias (cria normal e _large)
python scripts/process_images.py "imagem.png" relic

# Para o Botão de Seleção (com transparência)
python scripts/process_images.py "imagem.png" select

# Para o Fundo de Seleção (mantendo cenário)
python scripts/process_images.py "imagem.png" select --keep-bg
```
