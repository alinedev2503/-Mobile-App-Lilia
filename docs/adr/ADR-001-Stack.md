# ADR-001: Definição da Stack Tecnológica Principal

## Status
Aceito ✅

## Contexto
O projeto **Lília Personal Diet** é um aplicativo mobile focado em estilo de vida saudável, reeducação alimentar, registro inteligente de refeições por foto com IA multimodal (Gemini), assistência conversacional empática e monetização in-app (Stripe).

O comprador poderá comercializar o produto nos seguintes formatos:
- Aplicativo Mobile publicado na Google Play Store;
- Código-fonte completo para desenvolvedores e agências;
- SaaS / Solução White-Label para nutricionistas, clínicas e marcas de saúde e bem-estar;
- Boilerplate/Template de alta performance.

## Decisão
- **Linguagem & Framework Mobile:** Kotlin com Jetpack Compose (100% nativo moderno para Android), aproveitando aceleração de hardware, Material Design 3 e ciclo de vida reativo.
- **Persistência Local (Offline-First):** Room Database (SQLite nativo) com DAOs reativos (Flow/Coroutines) e migrações seguras.
- **Inteligência Artificial:** Google Gemini 2.5 Flash multimodal para análise nutricional de fotos de pratos e motor conversacional da Lília.
- **Monetização e Pagamentos:** Stripe PaymentIntents API + Pix Instantâneo integrado nativamente.
- **Injeção de Dependências & ViewModel:** MVVM limpo com StateFlow, Coroutines e injeção por construtor.

## Consequências
- **Positivas:** Máxima performance, zero dependência de pontes JS lentas, compilação em binário nativo APK/AAB pronto para a Google Play Store, suporte offline total com persistência local garantida.
- **Negativas:** Requer conhecimento do ecossistema Android/Kotlin para extensões nativas complexas.
