# Handbook de Assets - Jujutsu Kaisen: The Cursed Spire

Este documento registra o workflow visual e as decisoes praticas que ajudam os assets a parecerem parte natural de Slay the Spire.

## 1. Workflow de producao

Siga esta ordem para novos assets:

1. Gerar ou selecionar a arte base.
2. Remover fundo quando o asset precisar de transparencia.
3. Fazer autocrop para remover espacos vazios indesejados.
4. Ajustar escala e ancora visual.
5. Exportar nos tamanhos finais.
6. Conferir em jogo ou em preview comparando com assets oficiais.

## 2. Estilo visual

Direcao recomendada:

- Arte 2D pintada, com textura e contorno organico.
- Contraste claro entre personagem/objeto e fundo.
- Silhueta forte para leitura em tamanhos pequenos.
- Iluminacao coerente com cartas e UI de Slay the Spire.

Evite:

- Imagem com cara de render 3D plastico.
- Fundo muito detalhado em icones pequenos.
- Asset cortado perto demais das bordas.
- Transparencia com halos brancos ou serrilhado forte.

## 3. Tabela consolidada

| Tipo | Tamanho padrao | Tamanho grande | Escala visual | Ancora |
| :--- | :--- | :--- | :--- | :--- |
| Carta | `250 x 190` | `500 x 380` | `100%` | centro |
| Poder | `32 x 32` | `128 x 128` | `70%` | centro |
| Reliquia | `128 x 128` | `256 x 256` | `60-70%` | centro |
| Card back pequeno | `512 x 512` | n/a | `100%` | centro |
| Card back grande | `1024 x 1024` | n/a | `100%` | centro |
| Select button | `200 x 200` | n/a | `70%` | centro |
| Select portrait | `1920 x 1200` | n/a | `100%` | centro |
| Monstro pequeno | `400 x 400` | n/a | `80%` | base central |
| Monstro grande | `800 x 800` | n/a | `85%` | base central |

## 4. Orbe de energia

O jogo nao centraliza o orbe de custo da carta como uma imagem comum. Ele espera uma imagem grande onde o orbe visual fica pequeno e deslocado.

Padrao descoberto:

- Arquivo principal: `energy_orb.png`.
- Quadro: `512 x 512`.
- Escala visual do orbe: aproximadamente `20%` do quadro.
- Posicao do brilho: `X=36`, `Y=8`, medido a partir do canto superior esquerdo.

Motivo: essa posicao alinha o brilho sob o numero de custo da carta.

## 5. Small orb

Para `small_orb.png`, a leitura em tamanho pequeno e mais importante que detalhe.

Regras:

- Evite borda serrilhada.
- Use mascara radial suave.
- Mantenha nucleo mais opaco no centro.
- Use blur leve na transparencia das bordas.

## 6. Card backs

Fundos de cartas nao devem ser quadrados solidos.

Regras:

- A silhueta precisa parecer uma carta vertical.
- Tudo fora da moldura deve ser transparente.
- A versao pequena e a versao grande devem ter a mesma composicao.
- Teste ataques, habilidades e poderes separadamente.

## 7. Monstros

Monstros devem ser ancorados pela base, nao pelo centro geometrico.

Regras:

- Pes ou base visual perto da borda inferior.
- Corpo principal centralizado horizontalmente.
- Espaco inferior extra apenas quando o monstro for voador.
- Bosses podem ocupar mais altura, mas ainda precisam respeitar hitbox e intencoes.

## 8. Paleta

Paleta atual do mod:

- Itadori Pink: `RGB(255, 110, 150)`, usado em `ITADORI_PINK_COLOR`.
- Cursed Blue: energia amaldicoada de orbes e efeitos.
- Uniform Navy: azul escuro base para fundos de cartas.

Use a paleta como referencia, mas nao force todos os assets para uma unica cor. Cada carta deve continuar legivel e tematica.

## 9. Checklist de aceite

- Asset tem tamanho correto.
- Fundo esta transparente quando necessario.
- Silhueta continua clara no menor tamanho.
- Nao ha halo branco nas bordas.
- Nome e pasta seguem `asset-guide.md`.
- Asset foi comparado com pelo menos um asset oficial ou ja aprovado do mod.
