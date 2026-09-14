# 🔒 SECURITY — Políticas de Segurança e Gerenciamento de Segredos

## 1. Gestão de Chaves e Credenciais

- **Nunca versionar chaves no Git:** Nenhuma chave de API (Gemini, Stripe, Firebase, Supabase) deve ser mantida em arquivos de código-fonte (`.kt`, `.xml` ou `.json`).
- **Injeção Segura:** O projeto utiliza o plugin `com.google.android.libraries.mapsplatform.secrets-gradle-plugin` que lê variáveis do `.env` local ou das variáveis de ambiente de CI/CD e as injeta no `BuildConfig`.
- **Exemplo de Configuração:** Consulte o arquivo `.env.example` para obter os nomes exatos das chaves esperadas.

---

## 2. Comunicação e Criptografia em Trânsito

- **HTTPS Obrigatório:** Todas as chamadas para Google Gemini (`generativelanguage.googleapis.com`) e Stripe API (`api.stripe.com`) são realizadas exclusivamente sobre TLS 1.3/HTTPS.
- **Validação de Payload:** Respostas da API de IA e da Stripe são desserializadas com tipagem estrita e validação de erros para evitar injeções ou quebras de execução.

---

## 3. Segurança dos Dados do Usuário (LGPD / GDPR)

- **Persistência Local Segura:** O banco de dados Room reside no diretório privado da aplicação no Android (`/data/data/com.example/databases/`), inacessível para outros aplicativos instalados no dispositivo sem permissões de root.
- **Controle de Permissões:** O app solicita apenas permissões estritamente necessárias (`INTERNET` para APIs, `CAMERA` para foto de refeições e `RECORD_AUDIO` para mensagens de voz).
