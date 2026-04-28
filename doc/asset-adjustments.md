# Log de Ajustes de Assets

Este arquivo registra ajustes ja feitos ou pendentes em assets especificos. Para padroes gerais, use `asset-guide.md`. Para decisoes de workflow, use `asset-handbook.md`.

## Padrao de status

Use um dos marcadores:

- `[todo]`: ainda precisa ser feito.
- `[doing]`: em andamento.
- `[done]`: concluido.
- `[review]`: precisa conferir em jogo.

Formato recomendado:

```markdown
### NomeDoAsset.png

- Status: [done]
- Tipo: carta | poder | reliquia | personagem | monstro
- Arquivos gerados:
  - `caminho/arquivo.png`
- Ajustes:
  - Descricao curta do que foi feito.
- Observacoes:
  - Riscos, pendencias ou conferencias em jogo.
```

## Ajustes registrados

### DivergentFistPower.png

- Status: [done]
- Tipo: poder
- Arquivos gerados:
  - `src/main/resources/jujutsumod/images/powers/DivergentFistPower.png`
  - `src/main/resources/jujutsumod/images/powers/large/DivergentFistPower.png`
- Ajustes:
  - Fundo removido de branco para transparente.
  - Convertido de JPG para PNG.
  - Geradas versoes `32 x 32` e `128 x 128`.

### SlaughterDemon.png

- Status: [done]
- Tipo: carta
- Arquivos gerados:
  - `src/main/resources/jujutsumod/images/cards/attack/SlaughterDemon.png`
  - `src/main/resources/jujutsumod/images/cards/attack/SlaughterDemon_p.png`
- Ajustes:
  - Geradas versoes padrao `250 x 190` e retrato `500 x 380`.
  - Convertido de JPG para PNG.

### MartialArtsMastery.png

- Status: [done]
- Tipo: carta
- Arquivos gerados:
  - `src/main/resources/jujutsumod/images/cards/attack/MartialArtsMastery.png`
  - `src/main/resources/jujutsumod/images/cards/attack/MartialArtsMastery_p.png`
- Ajustes:
  - Geradas versoes padrao `250 x 190` e retrato `500 x 380`.

## Checklist para novos ajustes

- Caminho final registrado.
- Tamanho final registrado.
- Transparencia conferida.
- Versao pequena e grande geradas quando aplicavel.
- Conferencia visual marcada como `[review]` se ainda nao foi testado em jogo.
