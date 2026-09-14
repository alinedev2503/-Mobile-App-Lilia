# 💳 Guia de Configuração dos Produtos no Catálogo da Stripe

Este documento fornece as instruções e dados para cadastrar ou sincronizar os 3 planos no seu painel da **Stripe** (`dashboard.stripe.com/products`).

---

## 📋 Tabela de Produtos para Cadastro

### 1. App Lilia - Plano Start (7 Dias)
- **Nome do Produto:** `App Lilia - Plano Start (7 Dias)`
- **Descrição:** `Experimentação rápida da rotina e foco imediato. Inclui plano prático de 7 dias, até 10 análises de foto com IA, lista de compras básica e chat para dúvidas pontuais.`
- **ID do Produto (Stripe):** `prod_lilia_start_7d`
- **Preço:** `R$ 19,90 BRL`
- **ID do Preço:** `price_start_1990`
- **Tipo de Cobrança:** Pagamento único (ou recorrente semanal, se preferir modelo de assinatura contínua)

---

### 2. Desafio Reset (15 Dias)
- **Nome do Produto:** `Desafio Reset (15 Dias)`
- **Descrição:** `Equilíbrio, criação de hábitos e constância. Inclui plano prático de 15 dias em 2 fases, até 30 análises de fotos, assistente de receitas com a despensa, lista de compras com conservação e suporte contínuo da Lília.`
- **ID do Produto (Stripe):** `prod_lilia_reset_15d`
- **Preço:** `R$ 29,90 BRL`
- **ID do Preço:** `price_reset_2990`
- **Tipo de Cobrança:** Pagamento único (ou recorrente a cada 15 dias)

---

### 3. Transformação 360° (30 Dias) ⭐ *(Mais Popular / Melhor Custo-Benefício)*
- **Nome do Produto:** `Transformação 360° (30 Dias)`
- **Descrição:** `Experiência completa e consolidação de estilo de vida saudável. Plano prático de 30 dias com metas semanais, análises nutricionais por foto ilimitadas, gerador ilimitado de receitas com IA, lista dinâmica setorizada, relatórios de hábitos e suporte prioritário 24/7.`
- **ID do Produto (Stripe):** `prod_lilia_transform_30d`
- **Preço:** `R$ 39,90 BRL`
- **ID do Preço:** `price_transform_3990`
- **Tipo de Cobrança:** Pagamento único (ou mensal recorrente)

---

## 🖥️ Como Cadastrar pelo Painel Web da Stripe (Passo a Passo)

1. No seu navegador, acesse o botão **+ Criar produto** (no canto superior direito do seu painel aberto na imagem).
2. Preencha os campos para cada um dos 3 produtos:
   - **Nome:** Cole o nome do produto correspondente.
   - **Descrição:** Cole a descrição resumida.
   - **Preço:** Digite o valor em BRL (`19,90`, `29,90` ou `39,90`).
   - Em **Informações Adicionais / ID**, se desejar especificar o ID customizado, utilize os IDs fornecidos (`prod_lilia_start_7d`, `prod_lilia_reset_15d`, `prod_lilia_transform_30d`).
3. Clique em **Salvar produto**.

---

## ⚡ Criação Automática via Terminal / cURL

Você também pode criar os 3 produtos de uma só vez executando o script incluído no projeto:

```bash
export STRIPE_SECRET_KEY="sk_live_sua_chave_secreta_aqui"
chmod +x ./scripts/setup_stripe_products.sh
./scripts/setup_stripe_products.sh
```
