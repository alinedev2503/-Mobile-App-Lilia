#!/bin/bash
# ==============================================================================
# Script de Criação Automática de Produtos e Preços no Stripe
# Lília Personal Diet
# ==============================================================================

# Defina sua chave secreta da Stripe (sk_test_... ou sk_live_...)
# Exemplo de uso: STRIPE_SECRET_KEY="sk_live_..." ./setup_stripe_products.sh

if [ -z "$STRIPE_SECRET_KEY" ]; then
  echo "⚠️  Por favor, defina a variável STRIPE_SECRET_KEY antes de executar."
  echo "Exemplo: export STRIPE_SECRET_KEY='sk_test_...'"
  exit 1
fi

echo "🚀 Iniciando criação dos produtos no Stripe..."

# 1. App Lilia - Plano Start (7 Dias)
echo "📦 Criando Produto 1: App Lilia - Plano Start (7 Dias)..."
curl -s -X POST https://api.stripe.com/v1/products \
  -u "$STRIPE_SECRET_KEY:" \
  -d "id=prod_lilia_start_7d" \
  -d "name=App Lilia - Plano Start (7 Dias)" \
  -d "description=Experimentação rápida da rotina e foco imediato. Inclui plano prático de 7 dias, até 10 análises de foto com IA, lista de compras básica e chat para dúvidas." \
  -d "metadata[duration_days]=7" \
  -d "metadata[photos_limit]=10"

echo ""
echo "💰 Criando Preço para o Plano Start (R$ 19,90)..."
curl -s -X POST https://api.stripe.com/v1/prices \
  -u "$STRIPE_SECRET_KEY:" \
  -d "id=price_start_1990" \
  -d "product=prod_lilia_start_7d" \
  -d "unit_amount=1990" \
  -d "currency=brl"

echo ""
echo "------------------------------------------------------------"

# 2. Desafio Reset (15 Dias)
echo "📦 Criando Produto 2: Desafio Reset (15 Dias)..."
curl -s -X POST https://api.stripe.com/v1/products \
  -u "$STRIPE_SECRET_KEY:" \
  -d "id=prod_lilia_reset_15d" \
  -d "name=Desafio Reset (15 Dias)" \
  -d "description=Equilíbrio, criação de hábitos e constância. Inclui plano prático de 15 dias em 2 fases, até 30 análises de fotos, assistente com despensa e lista de compras com conservação." \
  -d "metadata[duration_days]=15" \
  -d "metadata[photos_limit]=30"

echo ""
echo "💰 Criando Preço para o Desafio Reset (R$ 29,90)..."
curl -s -X POST https://api.stripe.com/v1/prices \
  -u "$STRIPE_SECRET_KEY:" \
  -d "id=price_reset_2990" \
  -d "product=prod_lilia_reset_15d" \
  -d "unit_amount=2990" \
  -d "currency=brl"

echo ""
echo "------------------------------------------------------------"

# 3. Transformação 360° (30 Dias)
echo "📦 Criando Produto 3: Transformação 360° (30 Dias)..."
curl -s -X POST https://api.stripe.com/v1/products \
  -u "$STRIPE_SECRET_KEY:" \
  -d "id=prod_lilia_transform_30d" \
  -d "name=Transformação 360° (30 Dias) ⭐" \
  -d "description=Experiência completa e consolidação de estilo de vida saudável. Plano prático de 30 dias com metas semanais, fotos ilimitadas, receitas por IA e suporte prioritário 24/7." \
  -d "metadata[duration_days]=30" \
  -d "metadata[photos_limit]=unlimited"

echo ""
echo "💰 Criando Preço para a Transformação 360° (R$ 39,90)..."
curl -s -X POST https://api.stripe.com/v1/prices \
  -u "$STRIPE_SECRET_KEY:" \
  -d "id=price_transform_3990" \
  -d "product=prod_lilia_transform_30d" \
  -d "unit_amount=3990" \
  -d "currency=brl"

echo ""
echo "🎉 Todos os 3 produtos e preços foram criados com sucesso no Stripe!"
