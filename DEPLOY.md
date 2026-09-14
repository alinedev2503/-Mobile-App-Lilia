# 🚀 DEPLOY — Guia de Build e Publicação na Google Play Store

## 1. Geração de Builds Android

### 1.1 Gerar APK de Depuração (Debug APK)
Execute no terminal:
```bash
gradle assembleDebug
```
Local do arquivo gerado:
`app/build/outputs/apk/debug/app-debug.apk`

### 1.2 Gerar Android App Bundle para a Play Store (Release AAB)
Execute no terminal:
```bash
gradle bundleRelease
```
Local do arquivo gerado:
`app/build/outputs/bundle/release/app-release.aab`

---

## 2. Checklist para Publicação na Google Play Store

1. **Application ID Único:** Certifique-se de definir um `applicationId` único em `app/build.gradle.kts`.
2. **Ícone Adaptativo e Splash:** O app possui ícone adaptativo e splash configurados no padrão Material 3.
3. **Target SDK 35 (Android 15):** O projeto já está configurado com `compileSdk = 35` e `targetSdk = 35`, atendendo aos requisitos vigentes da Google Play.
4. **Política de Privacidade e Termos de Uso:** O app possui links acessíveis e telas dedicadas de Termos de Uso e Política de Privacidade na tela de Perfil e no Paywall.
5. **Declarações de Permissões:** O app utiliza permissões mínimas (`INTERNET`, `CAMERA`, `RECORD_AUDIO`) devidamente declaradas no `AndroidManifest.xml` e solicitadas sob demanda em tempo de execução via Jetpack Compose.
