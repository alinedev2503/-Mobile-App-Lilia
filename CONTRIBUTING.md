# 🤝 CONTRIBUTING — Guia de Contribuição e Desenvolvimento

## 1. Fluxo de Trabalho e Padrões de Branching
- `main`: Código estável pronto para release e comercialização.
- `feature/*`: Novas funcionalidades e experimentos.
- `fix/*`: Correção de bugs e ajustes de layout.

---

## 2. Padrões de Commit
Utilize commits semânticos no padrão Conventional Commits:
- `feat: adiciona checkout Pix no StripeService`
- `fix: corrige alinhamento do botão de fechar no Paywall`
- `docs: adiciona guia de migração Supabase`
- `refactor: modulariza DAOs do Room Database`

---

## 3. Validação antes de Commit
Antes de submeter pull requests ou commits:
1. Compile o projeto e certifique-se de que não há erros de compilação:
   ```bash
   gradle assembleDebug
   ```
2. Execute os testes unitários e de UI:
   ```bash
   gradle :app:testDebugUnitTest
   ```
