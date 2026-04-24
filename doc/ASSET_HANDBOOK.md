# 📖 Jujutsu Kaisen: The Cursed Spire - Asset Handbook

Este guia documenta o workflow técnico e as descobertas feitas durante o desenvolvimento visual do mod. Use estas diretrizes para manter a consistência em futuras atualizações.

---

## 🛠️ O Workflow de Produção
Sempre siga esta ordem para garantir que a arte não pareça um "corpo estranho" no jogo:
1. **Geração (IA)**: Usar prompts que foquem no estilo "Slay the Spire" (hand-drawn, textured fabric, 2D).
2. **Remoção de Fundo**: Utilizar o script `process_images.py` que usa IA (`rembg`) para isolar o objeto.
3. **Autocrop**: O script remove espaços vazios da geração original para normalizar o tamanho do objeto.
4. **Escala & Âncora**: Aplicação das medidas exatas de UI documentadas abaixo.

---

## 📏 Tabela de Medidas e Configurações

| Tipo de Asset | Tamanho (px) | Escala Real | Âncora | Observação |
| :--- | :--- | :--- | :--- | :--- |
| **Card Art** | 500x380 | 100% | Centro | Arte principal da carta. |
| **Card Back (L)** | 1024x1024 | 100% | Centro | Fundo da carta (High Res). |
| **Card Back (S)** | 512x512 | 100% | Centro | Fundo da carta (Standard). |
| **Relíquia** | 128x128 | 70% | Centro | O recuo de 30% evita que a relíquia toque as bordas. |
| **Poder (Buff)** | 32x32 | 70% | Centro | Ícones que aparecem abaixo do personagem. |
| **Select Portrait**| 1920x1200 | 100% | Centro | Imagem de fundo na seleção de personagem. |
| **Select Button** | 200x200 | 70% | Centro | Ícone circular de seleção. |

---

## 💡 Descobertas Críticas (Pulos do Gato)

### 1. O Enigma do Orbe de Energia (`energy_orb.png`)
Descobrimos que o jogo não centraliza o orbe que vai atrás do custo da carta. Ele espera uma imagem de 512x512 onde o orbe é pequeno e deslocado.
*   **Escala**: 20% do quadro.
*   **Coordenadas Mágicas**: `X=36, Y=8` (medido a partir do canto superior esquerdo).
*   **Por que?**: Isso alinha o brilho azul exatamente sob o número do custo da carta.

### 2. Suavidade em Baixa Resolução (`small_orb.png`)
Para ícones minúsculos (22x22), a remoção de fundo simples gera bordas serrilhadas (formato de cruz).
*   **Solução**: Aplicar uma **Máscara de Gradiente Radial**.
*   **Técnica**: Manter um núcleo sólido (100% opaco) no centro e usar um Gaussian Blur na transparência das bordas. Isso cria o efeito de brilho profissional do Slay the Spire.

### 3. Card Backs Profissionais
Fundos de cartas não devem ser imagens quadradas sólidas.
*   **Formato**: Devem ter a silhueta de uma carta vertical.
*   **Transparência**: Tudo que estiver fora da moldura da carta DEVE ser transparente, ou o jogo exibirá um bloco preto feio.

---

## 🚀 Como usar o Script CLI
Nosso script `scripts/process_images.py` automatiza tudo isso.

**Exemplo para Relíquia:**
```powershell
python scripts/process_images.py arte_gerada.png relic
```

**Exemplo para Orbe de Energia (Gera os 3 tamanhos e aplica as coordenadas 36,8):**
```powershell
python scripts/process_images.py arte_gerada.png orb
```

---

## 🎨 Paleta de Cores Identitária
*   **Itadori Pink**: `255, 110, 150` (Usado em `ITADORI_PINK_COLOR` no Java).
*   **Cursed Blue**: Energia amaldiçoada padrão dos orbes e bordas.
*   **Uniform Navy**: O azul escuro base para todos os fundos de cartas.
