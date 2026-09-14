# ADR-003: Estratégia de Monetização e Integração Stripe

## Status
Aceito ✅

## Contexto
O modelo de negócios do **Lília Personal Diet** é Freemium/In-App Paywall. O usuário pode baixar e experimentar o aplicativo gratuitamente (anamnese guiada, metas diárias, catálogo inicial de receitas e créditos iniciais de análise). Ao desejar aprofundamento, planos de ação estruturados ou análises ilimitadas, ele adquire um dos 3 planos com pagamento in-app.

## Decisão
Implementar integração direta e transparente com a Stripe:
1. **Modelos de Planos:**
   - **Plano Start (7 Dias):** R$ 19,90 (10 fotos, 7 dias de plano de ação, lista semanal de compras).
   - **Desafio Reset (15 Dias):** R$ 29,90 (30 fotos, 15 dias de plano, assistente com a despensa).
   - **Transformação 360° (30 Dias):** R$ 39,90 (Fotos ilimitadas, gerador de receitas por IA, suporte prioritário).
2. **Métodos de Pagamento:**
   - **Cartão de Crédito:** Criação de `PaymentIntent` com a API Stripe (`https://api.stripe.com/v1/payment_intents`).
   - **Pix Instantâneo Copia e Cola:** Geração de payload padronizado do Banco Central com ativação imediata após confirmação.
3. **Gerenciamento de Segredos:**
   - Chaves `STRIPE_PUBLISHABLE_KEY` e `STRIPE_SECRET_KEY` injetadas via painel de Secrets (`BuildConfig`) e documentadas no `.env.example`.

## Consequências
- Checkout ágil e de alta conversão sem taxas excessivas de lojas de aplicativos para vendas diretas ou SaaS.
- Flexibilidade total para o comprador alterar preços, moedas e períodos de validade.
