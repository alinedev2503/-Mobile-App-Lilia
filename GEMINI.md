# 🤖 GEMINI — Diretrizes de Inteligência Artificial e Modelos

## 1. Modelo em Uso
- **Modelo:** `gemini-2.5-flash`
- **Capacidades:**
  - Visão Computacional Multimodal: Análise de imagens de alimentos (JPEG/PNG) em Base64 para identificação de calorias, macronutrientes e sugestões.
  - Processamento de Linguagem Natural: Respostas estruturadas via JSON schema para geração de cardápios e mensagens conversacionais no chat.

---

## 2. Segurança e Boas Práticas
- Nenhuma chave de API deve ser exposta no frontend; sempre utilizar injeção via `BuildConfig.GEMINI_API_KEY`.
- Prompts com restrições explícitas de microcópia humanizada e sem julgamentos (filosofia Lília).
