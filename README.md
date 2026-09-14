# 🥑 Lília — Organizador de Estilo de Vida & Nutrição Inteligente

> Produto de Software Profissional • Pronto para Comercialização (Código-Fonte, Mobile Android, SaaS & White-Label).

---

## 🌟 Visão Geral

**Lília** é uma solução completa e moderna de saúde e nutrição que alia **Inteligência Artificial Multimodal (Google Gemini 2.5 Flash)** para análise fotográfica de pratos em tempo real, **anamnese comportamental humanizada**, **assistente de receitas inteligentes com base no que você tem em casa**, **lista de compras setorizada** e **motor de monetização in-app com Stripe (Cartão + Pix)**.

Construído com **Kotlin e Jetpack Compose (100% nativo)**, o app adota o paradigma **Local Offline-First** e arquitetura desacoplada compatível tanto com **Supabase** quanto com **Firebase**.

---

## 🚀 Modelos de Comercialização

Este projeto está estruturado para ser vendido e monetizado como:
1. **Código-Fonte Completo:** Para desenvolvedores, agências e estúdios mobile.
2. **Aplicativo Mobile:** Publicação direta na **Google Play Store**.
3. **SaaS / White-Label:** Personalização visual e de marca para clínicas de nutrição, personal trainers e marcas de suplementação.
4. **Template / Boilerplate:** Base robusta para aplicações de saúde e bem-estar.

---

## 💎 Planos & Monetização Integrada (Stripe)

O app conta com fluxo de Paywall e Checkout Seguro integrado à Stripe:

| Plano | Duração | Valor | Benefícios Principais |
|---|---|---|---|
| **Plano Start** | 7 Dias | R$ 19,90 | Plano de ação prático 7 dias, até 10 análises de foto, lista básica de compras. |
| **Desafio Reset** | 15 Dias | R$ 29,90 | Plano de ação em 2 fases, até 30 análises de fotos, assistente com a despensa. |
| **Transformação 360°** | 30 Dias | R$ 39,90 | Acesso completo, fotos ILIMITADAS, gerador inteligente de receitas, suporte prioritário. |

---

## 🏗️ Stack Tecnológica

- **Frontend & UI:** Kotlin, Jetpack Compose, Material Design 3, Compose Navigation.
- **Persistência Local:** Room Database (SQLite) com Flow e Coroutines.
- **Motor de IA:** Google Gemini 2.5 Flash (multimodal para visão computacional de alimentos e geração de cardápios).
- **Pagamentos:** Stripe API (PaymentIntents + Pix instantâneo).
- **Backend Nuvem Intercambiável:** Suporte e adaptadores para **Supabase** (PostgreSQL/RLS) e **Firebase** (Firestore/Auth).

---

## ⚙️ Instalação e Execução

### Pré-requisitos
- Android Studio Ladybug (ou versão superior)
- JDK 17 ou JDK 21
- Android SDK com API 35 (Android 15)

### Passo a Passo
1. Clone o repositório:
   ```bash
   git clone <URL_DO_REPOSITORIO>
   cd lilia-app
   ```
2. Configure as variáveis de ambiente em seu painel de Secrets ou crie o `.env`:
   ```env
   GEMINI_API_KEY=sua_chave_gemini_aqui
   STRIPE_PUBLISHABLE_KEY=pk_test_sua_chave_publica_stripe
   STRIPE_SECRET_KEY=sk_test_sua_chave_secreta_stripe
   ```
3. Compile o projeto e gere o APK de depuração:
   ```bash
   gradle assembleDebug
   ```
4. O APK gerado estará disponível em:
   `./app/build/outputs/apk/debug/app-debug.apk`

---

## 📂 Estrutura de Diretórios

```
/
├── app/
│   ├── src/main/java/com/example/
│   │   ├── data/
│   │   │   ├── local/        # Room Database, DAOs e Entidades
│   │   │   ├── model/        # Modelos de Dados e Estado
│   │   │   ├── remote/       # Gemini AI Client e DTOs
│   │   │   ├── repository/   # LiliaRepository e Camada de Dados
│   │   │   └── stripe/       # StripeService, Planos e Checkout
│   │   └── ui/
│   │       ├── components/   # Componentes M3 Reutilizáveis
│   │       ├── navigation/   # Rotas e Barra de Navegação
│   │       ├── screens/      # Telas (Dashboard, Chat, Receitas, Paywall, etc.)
│   │       ├── theme/        # Cores, Tipografia e Design System M3
│   │       └── viewmodel/    # LiliaViewModel e Gerenciamento de Estado
├── docs/
│   └── adr/                  # Registros de Decisões Arquiteturais
├── ARCHITECTURE.md           # Detalhes de Arquitetura e Padrões
├── PRD.md                    # Documento de Requisitos de Produto
├── SECURITY.md               # Políticas e Auditoria de Segurança
├── DEPLOY.md                 # Guia de Publicação e Google Play Store
└── CHANGELOG.md              # Histórico de Versões
```
