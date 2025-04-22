# Instruções para Compilar o APK do PITV Usando Serviços Online

Este guia fornece instruções passo a passo para gerar o arquivo APK do aplicativo PITV usando o serviço online de compilação AppCircle.

## Preparação do Código-Fonte

O código-fonte já está estruturado para ser compilado. Você receberá um arquivo ZIP contendo todo o código necessário.

## Usando o AppCircle para Compilar o APK

AppCircle é um serviço online que permite compilar aplicativos Android sem precisar instalar o Android Studio.

### Passo 1: Criar uma Conta no AppCircle

1. Acesse [https://appcircle.io/](https://appcircle.io/)
2. Clique em "Sign Up" para criar uma conta gratuita
3. Complete o processo de registro

### Passo 2: Criar um Novo Projeto

1. Após fazer login, clique em "Create New Project"
2. Escolha "Android" como plataforma
3. Dê um nome ao projeto (ex: "PITV")
4. Clique em "Create Project"

### Passo 3: Fazer Upload do Código-Fonte

1. No dashboard do projeto, vá para a seção "Build"
2. Clique em "Upload Source Code"
3. Selecione a opção "Upload ZIP"
4. Faça upload do arquivo ZIP do código-fonte do PITV que você recebeu
5. Aguarde o upload e processamento do código

### Passo 4: Configurar o Build

1. Após o upload, vá para "Build Configuration"
2. Em "Build Type", selecione "Debug" (para teste) ou "Release" (para versão final)
3. Em "Android SDK Version", selecione "API Level 33" (Android 13)
4. Mantenha as demais configurações padrão
5. Clique em "Save Configuration"

### Passo 5: Iniciar o Build

1. Volte para a seção "Build"
2. Clique em "Start Build"
3. Aguarde o processo de compilação (pode levar alguns minutos)
4. Você poderá acompanhar o progresso na interface

### Passo 6: Baixar o APK

1. Quando o build for concluído com sucesso, você verá um status "Successful"
2. Clique em "Download Artifacts"
3. Selecione o arquivo APK para download
4. Salve o arquivo em seu dispositivo

## Instalando o APK em seu Dispositivo Android

1. Transfira o arquivo APK para seu dispositivo Android (via USB, email, ou serviço de armazenamento em nuvem)
2. No seu dispositivo Android, vá para Configurações > Segurança
3. Ative a opção "Fontes desconhecidas" ou "Instalar aplicativos desconhecidos"
4. Navegue até o local onde você salvou o APK e toque nele
5. Siga as instruções na tela para instalar o aplicativo
6. Após a instalação, você encontrará o ícone do PITV na tela inicial ou na gaveta de aplicativos

## Alternativas ao AppCircle

Se você encontrar dificuldades com o AppCircle, aqui estão algumas alternativas:

### Buildozer (online)
- [https://buildozer.io/](https://buildozer.io/)
- Similar ao AppCircle, mas com interface mais simples

### GitHub Actions
- Se você tiver uma conta GitHub, pode fazer upload do código para um repositório privado
- Use os workflows de GitHub Actions para compilação automática

### Serviço de Compilação Remota do Android Studio
- Se você tiver o Android Studio Cloud, pode usar o recurso de compilação remota

## Solução de Problemas

Se encontrar problemas durante a compilação:

1. **Erro de dependências**: Verifique se o serviço de compilação tem acesso à internet para baixar as dependências necessárias

2. **Erro de SDK**: Certifique-se de que a versão do SDK selecionada é compatível (API Level 33 recomendado)

3. **Erro de Gradle**: Os serviços de compilação geralmente têm versões específicas do Gradle suportadas. Se necessário, ajuste a versão no arquivo `gradle-wrapper.properties`

4. **Falha na compilação**: Verifique os logs de erro para identificar problemas específicos. A maioria dos serviços fornece logs detalhados

Para qualquer problema adicional, consulte a documentação do serviço de compilação escolhido.
