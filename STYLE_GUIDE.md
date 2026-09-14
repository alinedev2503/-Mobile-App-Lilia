# 🎨 STYLE_GUIDE — Diretrizes Visuais & Padrões de Código

## 1. Padrões de UI / Design System (Jetpack Compose)

### 1.1 Cores Principais
- **`LiliaPrimary` (`#15803D` / Verde Esmeralda):** Cor principal de botões de ação e destaques.
- **`LiliaSecondary` (`#16A34A` / Verde Folha):** Cor de suporte e indicadores secundários.
- **`LiliaMintLight` (`#ECFDF5` / Menta Suave):** Fundo de cartões de destaque, badges e containers suaves.
- **`LiliaBackground` (`#F8FAFC` / Off-White Suave):** Fundo geral de todas as telas.

### 1.2 Formas e Elevações
- **Botões e Badges Principais:** `PillShape` (Totalmente arredondados, 100.dp).
- **Cards e Containers Bento:** `RoundedCornerShape(18.dp)` a `24.dp` com bordas sutis (1.dp) e sombras suaves.

### 1.3 Acessibilidade e Toque
- Todos os botões e áreas clicáveis possuem tamanho mínimo de **48dp × 48dp**.
- Textos utilizam unidades relativas em `sp` e `dp` para total responsividade a escalas de fonte do sistema.

---

## 2. Padrões de Código Kotlin

- **Imutabilidade:** Preferir `val` em relação a `var` e `data class` para modelos de estado.
- **Fluxos Assíncronos:** Utilizar `StateFlow` e `collectAsStateWithLifecycle()` para consumo reativo na UI.
- **TestTags:** Todos os botões de ação e campos de texto contêm `Modifier.testTag("nome_do_elemento")` para testes automatizados com Robolectric / Roborazzi.
