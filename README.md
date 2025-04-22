# Documentação do Aplicativo PITV

## Visão Geral
PITV é um aplicativo Android para reprodução de conteúdo IPTV, similar ao XCIPTV Player. O aplicativo suporta tanto playlists no formato M3U quanto servidores Xtream Codes, permitindo aos usuários assistir a canais de TV ao vivo em seus dispositivos Android.

## Funcionalidades Principais

### Gerenciamento de Playlists
- Adição de playlists via URL M3U
- Suporte a servidores Xtream Codes (com autenticação de usuário/senha)
- Gerenciamento de múltiplas playlists

### Navegação de Conteúdo
- Visualização de canais por categoria/grupo
- Busca de canais por nome
- Lista de favoritos para acesso rápido

### Reprodução de Mídia
- Player de vídeo integrado com controles de reprodução
- Suporte a streaming HLS e outros formatos
- Opção de tela cheia
- Navegação entre canais

### Interface do Usuário
- Design moderno e intuitivo
- Menu de navegação lateral
- Visualização de informações do canal
- Suporte a temas escuros

## Requisitos Técnicos
- Android 5.0 (Lollipop) ou superior
- Conexão com a internet
- Permissões: Internet, Acesso ao Estado da Rede, Armazenamento

## Instruções de Instalação
1. Baixe o arquivo PITV.apk
2. Habilite a instalação de aplicativos de fontes desconhecidas nas configurações do seu dispositivo
3. Abra o arquivo APK e siga as instruções de instalação
4. Após a instalação, abra o aplicativo PITV

## Instruções de Uso

### Adicionar uma Playlist M3U
1. Abra o menu lateral e selecione "Adicionar Playlist"
2. Selecione a aba "URL M3U"
3. Digite um nome para a playlist e a URL do arquivo M3U
4. Toque em "Adicionar"

### Adicionar um Servidor Xtream Codes
1. Abra o menu lateral e selecione "Adicionar Playlist"
2. Selecione a aba "Xtream Codes"
3. Digite um nome para a playlist, a URL do servidor, nome de usuário e senha
4. Toque em "Adicionar"

### Assistir a um Canal
1. Selecione uma playlist no menu lateral
2. Navegue pelos grupos de canais ou use a busca para encontrar um canal específico
3. Toque no canal desejado para iniciar a reprodução
4. Use os controles de reprodução para pausar/reproduzir, avançar/retroceder canais ou alternar para tela cheia

### Adicionar um Canal aos Favoritos
1. Durante a reprodução de um canal, toque no ícone de coração
2. Ou na lista de canais, toque no ícone de coração ao lado do nome do canal
3. Acesse seus canais favoritos através da opção "Favoritos" no menu lateral

## Solução de Problemas
- Se um canal não reproduzir, verifique sua conexão com a internet
- Certifique-se de que a URL da playlist ou as credenciais do servidor Xtream Codes estão corretas
- Alguns canais podem estar temporariamente indisponíveis devido a problemas no servidor de origem

## Compilação do Código-Fonte
O código-fonte está organizado seguindo as melhores práticas de desenvolvimento Android:

- `model`: Classes de modelo de dados (Channel, Playlist)
- `parser`: Parser para arquivos M3U
- `api`: Cliente para API Xtream Codes
- `service`: Gerenciador de playlists e favoritos
- `ui`: Fragmentos e atividades da interface do usuário
- `adapter`: Adaptadores para RecyclerView e ViewPager

Para compilar o projeto:
1. Importe o código-fonte em Android Studio
2. Sincronize o projeto com os arquivos Gradle
3. Execute a tarefa `assembleDebug` ou `assembleRelease` para gerar o APK
