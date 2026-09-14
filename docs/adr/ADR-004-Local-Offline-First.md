# ADR-004: Arquitetura Local-First com Room Database

## Status
Aceito ✅

## Contexto
Aplicativos de nutrição e hábitos diários sofrem com abandono quando exigem conexão estável à internet a cada interação (ex: registrar um copo de água na academia ou checar itens da lista de compras no mercado).

## Decisão
Adotar uma arquitetura **Local-First**:
- O banco de dados local **Room (SQLite)** é a única fonte da verdade para a UI.
- Todas as ações do usuário (registrar refeição, marcar copo de água, riscar item do supermercado, responder anamnese) são gravadas instantaneamente no Room.
- A UI reage via `Kotlin StateFlow` e `Room Flow queries`, garantindo resposta em 0ms (zero latência percebida).
- Chamadas de rede (como análise de imagem por IA no Gemini ou pagamento Stripe) operam como aprimoramentos assíncronos que atualizam o banco local ao concluir.

## Consequências
- A experiência do usuário é 100% fluida, sem "loaders" bloqueantes em operações rotineiras.
- Resiliência máxima para modo avião ou locais com baixa conectividade.
