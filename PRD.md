# 📋 PRD — Documento de Requisitos de Produto (Lília)

## 1. Visão do Produto
A **Lília** é uma assistente pessoal e organizadora de estilo de vida alimentar que transforma o processo de reeducação nutricional em uma jornada prazerosa, sustentável e livre de julgamentos, utilizando IA multimodal para identificar refeições por foto e fornecer insights práticos.

---

## 2. Personas e Público-Alvo
1. **Profissionais com Rotina Acelerada:** Pessoas com pouco tempo para cozinhar que precisam de ideias práticas com os ingredientes disponíveis na despensa.
2. **Praticantes de Atividade Física:** Indivíduos focados em atingir metas diárias de calorias, proteínas e hidratação.
3. **Pessoas em Reeducação Alimentar:** Usuários que desejam entender melhor o que comem sem a rigidez obsessiva de tabelas complexas.

---

## 3. Matriz de Funcionalidades do Produto

| Módulo | Funcionalidade | Descrição | Status |
|---|---|---|---|
| **Onboarding** | Anamnese Humanizada | Questionário empático em 3 fases para calibrar metas de água, calorias e preferências. | ✅ Verificado |
| **Dashboard** | Visão 360° do Dia | Gráficos de macros (Proteína, Carbo, Gordura), meta de água interativa e linha do tempo de refeições. | ✅ Verificado |
| **Visão Computacional** | Reconhecimento de Prato por Foto | Análise instantânea com Google Gemini com estimativa de calorias, composição e dicas práticas. | ✅ Verificado |
| **Chat & Assistente** | Conversa Empática com Lília | IA conversacional com memória de contexto, sugestões rápidas e áudio. | ✅ Verificado |
| **Receitas Inteligentes** | Gerador com base na Despensa | Sugestão de refeições balanceadas a partir dos ingredientes que o usuário já possui. | ✅ Verificado |
| **Lista de Compras** | Organização Setorizada | Lista com categorização inteligente por setores do supermercado e dicas de conservação. | ✅ Verificado |
| **Monetização** | Paywall & Checkout Stripe | 3 planos estruturados (Start 7d, Reset 15d, Transformação 30d) com Cartão e Pix. | ✅ Verificado |

---

## 4. Requisitos Não Funcionais
- **Disponibilidade:** Operação Offline-First sem travar na ausência de rede.
- **Latência:** Renderização de UI em 60/120 FPS e resposta local em 0ms.
- **Segurança:** Nenhum segredo hardcoded no código-fonte; uso estrito de `BuildConfig` e HTTPS.
- **Acessibilidade:** Alvos de toque mínimos de 48dp, contrastes WCAG AA e navegação fluida.
