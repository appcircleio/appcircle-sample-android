package com.pitv.player.parser;

import com.pitv.player.model.Channel;
import com.pitv.player.model.Playlist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parser para arquivos M3U
 */
public class M3UParser {
    
    private static final String EXTM3U = "#EXTM3U";
    private static final String EXTINF = "#EXTINF:";
    private static final String TVG_NAME = "tvg-name=\"";
    private static final String TVG_LOGO = "tvg-logo=\"";
    private static final String GROUP_TITLE = "group-title=\"";
    
    /**
     * Faz o parse de uma playlist M3U a partir de uma URL
     * @param playlist Objeto Playlist a ser preenchido
     * @return Playlist preenchida com os canais
     */
    public static Playlist parseFromUrl(Playlist playlist) throws IOException {
        URL url = new URL(playlist.getUrl());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        
        try (InputStream inputStream = connection.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            
            return parseFromReader(reader, playlist);
        } finally {
            connection.disconnect();
        }
    }
    
    /**
     * Faz o parse de uma playlist M3U a partir de um BufferedReader
     * @param reader BufferedReader com o conteúdo da playlist
     * @param playlist Objeto Playlist a ser preenchido
     * @return Playlist preenchida com os canais
     */
    public static Playlist parseFromReader(BufferedReader reader, Playlist playlist) throws IOException {
        String line;
        boolean isFirstLine = true;
        String channelInfo = "";
        
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            
            // Verifica se é a primeira linha e se começa com #EXTM3U
            if (isFirstLine) {
                isFirstLine = false;
                if (!line.startsWith(EXTM3U)) {
                    throw new IOException("Formato de arquivo inválido. Não é um arquivo M3U válido.");
                }
                continue;
            }
            
            // Ignora linhas vazias
            if (line.isEmpty()) {
                continue;
            }
            
            // Se a linha começa com #EXTINF, é uma informação de canal
            if (line.startsWith(EXTINF)) {
                channelInfo = line;
            } 
            // Se não começa com # e temos informações de canal, é a URL do canal
            else if (!line.startsWith("#") && !channelInfo.isEmpty()) {
                Channel channel = parseChannel(channelInfo, line);
                playlist.addChannel(channel);
                channelInfo = "";
            }
        }
        
        return playlist;
    }
    
    /**
     * Faz o parse das informações de um canal
     * @param channelInfo Linha com as informações do canal (#EXTINF)
     * @param channelUrl URL do canal
     * @return Objeto Channel preenchido
     */
    private static Channel parseChannel(String channelInfo, String channelUrl) {
        String id = UUID.randomUUID().toString();
        String name = extractChannelName(channelInfo);
        String logoUrl = extractAttribute(channelInfo, TVG_LOGO);
        String group = extractAttribute(channelInfo, GROUP_TITLE);
        
        // Se não encontrou um grupo, usa "Sem Categoria"
        if (group.isEmpty()) {
            group = "Sem Categoria";
        }
        
        return new Channel(id, name, channelUrl, logoUrl, group);
    }
    
    /**
     * Extrai o nome do canal da linha de informações
     * @param channelInfo Linha com as informações do canal
     * @return Nome do canal
     */
    private static String extractChannelName(String channelInfo) {
        // Tenta extrair do atributo tvg-name primeiro
        String name = extractAttribute(channelInfo, TVG_NAME);
        
        // Se não encontrou, extrai do final da linha
        if (name.isEmpty()) {
            int commaIndex = channelInfo.lastIndexOf(',');
            if (commaIndex != -1 && commaIndex < channelInfo.length() - 1) {
                name = channelInfo.substring(commaIndex + 1).trim();
            } else {
                name = "Canal Sem Nome";
            }
        }
        
        return name;
    }
    
    /**
     * Extrai um atributo da linha de informações do canal
     * @param channelInfo Linha com as informações do canal
     * @param attribute Atributo a ser extraído (ex: tvg-logo=")
     * @return Valor do atributo
     */
    private static String extractAttribute(String channelInfo, String attribute) {
        int startIndex = channelInfo.indexOf(attribute);
        if (startIndex != -1) {
            startIndex += attribute.length();
            int endIndex = channelInfo.indexOf("\"", startIndex);
            if (endIndex != -1) {
                return channelInfo.substring(startIndex, endIndex);
            }
        }
        return "";
    }
}
