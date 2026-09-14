# 🏛️ ARCHITECTURE — Arquitetura de Software e Padrões

## 1. Visão Arquitetural

O projeto adota uma arquitetura em camadas baseada em **Clean Architecture / MVVM (Model-View-ViewModel)** combinada com o padrão **Offline-First**.

```
┌────────────────────────────────────────────────────────┐
│                   Apresentação (UI)                    │
│      Jetpack Compose + Material Design 3 Screens       │
└───────────────────────────▲────────────────────────────┘
                            │ StateFlow / Actions
┌───────────────────────────┴────────────────────────────┐
│                    ViewModel Layer                     │
│           LiliaViewModel (Gestão de Estado)            │
└───────────────────────────▲────────────────────────────┘
                            │
┌───────────────────────────┴────────────────────────────┐
│                   Camada de Domínio                    │
│                    LiliaRepository                     │
└───────────────▲────────────────────────▲───────────────┘
                │                        │
┌───────────────┴───────────────┐ ┌──────┴───────────────┐
│       Persistência Local      │ │    Serviços Remotos  │
│    Room Database (SQLite)     │ │ - Gemini AI Service  │
│  - MealEntryDao               │ │ - Stripe API Service │
│  - RecipeDao                  │ │ - Supabase / Firebase│
│  - ShoppingItemDao            │ │   (Sync Adapters)    │
│  - UserProfileDao             │ │                      │
└───────────────────────────────┘ └──────────────────────┘
```

---

## 2. Camadas do Sistema

### 2.1 Apresentação (`ui/`)
- **Telas (`ui/screens/`):** Dashboard, Chat, Refeições, Receitas, Lista de Compras, Perfil, Paywall e Anamnese.
- **Componentes (`ui/components/`):** Barra de navegação inferior, cartões de refeição, contadores de água e badges.
- **Design System (`ui/theme/`):** Paleta moderna em tons de verde menta e esmeralda (`LiliaPrimary`, `LiliaMintLight`), tipografia refinada e formatos pílula/arredondados.

### 2.2 Camada de Estado & ViewModel (`ui/viewmodel/`)
- `LiliaViewModel`: Concentra o estado reativo da aplicação utilizando `StateFlow`.
- Despacha coroutines via `viewModelScope` em threads de background (`Dispatchers.IO`).

### 2.3 Repositório & Dados (`data/repository/` e `data/local/`)
- `LiliaRepository`: Ponto único de acesso a dados. Garante que os dados sejam gravados no banco local antes de qualquer sincronização externa.
- `LiliaDatabase`: Banco Room versão 2 com migração segura `fallbackToDestructiveMigration` para testes ágeis e DAOs bem tipados.

### 2.4 Serviços Remotos (`data/remote/` e `data/stripe/`)
- `LiliaAiService`: Comunica-se com o endpoint `gemini-2.5-flash` para visão computacional de pratos e respostas de chat estruturadas.
- `StripeService`: Gerencia PaymentIntents e geração de Pix seguro.

---

## 3. Compatibilidade Firebase vs Supabase
Para conectar o app a um backend de sincronização em nuvem, basta instanciar o adaptador desejado no repositório:
- **Supabase Adapter:** Faz `upsert` das tabelas `meals`, `recipes`, `shopping_items` e `profiles` via REST/PostgREST.
- **Firebase Adapter:** Faz `setDoc` nas coleções correspondentes do Cloud Firestore.
