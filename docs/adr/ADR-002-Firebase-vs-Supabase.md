# ADR-002: Arquitetura Híbrida e Intercambiável: Firebase × Supabase

## Status
Aceito ✅

## Contexto
O comprador do código-fonte ou licenciante White-Label precisa de flexibilidade para escolher seu provedor de nuvem de preferência (Backend-as-a-Service):
- **Firebase:** Ideal para quem busca integração profunda com o ecossistema Google, Cloud Firestore, Firebase Authentication, Cloud Functions e Cloud Messaging.
- **Supabase:** Ideal para quem busca PostgreSQL nativo, RLS (Row Level Security), open-source sem vendor lock-in e facilidade em queries relacionais e SQL.

## Decisão
A arquitetura do aplicativo foi desenhada com desacoplamento rigoroso através de **Camada de Repositório e Interfaces de Serviço**:
- `LiliaRepository` desacopla a camada de apresentação (`UI / ViewModel`) do provedor de infraestrutura.
- A persistência primária opera em modo **Local Offline-First (Room Database)** garantindo funcionamento 100% autônomo sem travar por indisponibilidade de rede.
- As integrações com Firebase ou Supabase funcionam como adaptadores de sincronização em nuvem (`Sync Adapters`).

## Comparativo para o Comprador

| Característica | Firebase (Google Cloud) | Supabase (PostgreSQL) |
|---|---|---|
| **Modelo de Dados** | NoSQL Documental (Firestore) | Relacional SQL (PostgreSQL 15+) |
| **Segurança** | Firebase Security Rules | Row Level Security (RLS) via SQL |
| **Autenticação** | Firebase Auth (Google, E-mail, Apple) | Supabase Auth (GoTrue + JWT) |
| **Realtime** | Firestore Listeners / RTDB | Postgres CDC (Realtime Websockets) |
| **Vendor Lock-in** | Alto (Proprietário Google) | Nulo (Open-Source / Auto-hospedável) |
| **Custo Inicial** | Generoso (Spark Free Tier) | Generoso (Free Plan com 500MB DB) |

## Consequências
O comprador pode alternar entre Firebase e Supabase apenas trocando a implementação da interface de sincronização, sem alterar nenhuma linha de UI ou regra de negócio da Lília.
