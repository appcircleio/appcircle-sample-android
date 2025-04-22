package com.pitv.player.api;

import com.pitv.player.model.Channel;
import com.pitv.player.model.Playlist;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

/**
 * Cliente para API Xtream Codes
 */
public class XtreamCodesClient {

    private static final String API_LIVE = "player_api.php?username=%s&password=%s&action=get_live_categories";
    private static final String API_LIVE_STREAMS = "player_api.php?username=%s&password=%s&action=get_live_streams&category_id=%s";
    
    /**
     * Carrega os canais de uma playlist Xtream Codes
     * @param playlist Playlist a ser carregada
     * @return Playlist com os canais carregados
     */
    public static Playlist loadChannels(Playlist playlist) throws IOException, JSONException {
        // Primeiro carrega as categorias
        String categoriesUrl = String.format(
                "%s/%s", 
                playlist.getUrl(), 
                String.format(API_LIVE, playlist.getUsername(), playlist.getPassword())
        );
        
        String categoriesJson = makeRequest(categoriesUrl);
        JSONArray categories = new JSONArray(categoriesJson);
        
        // Para cada categoria, carrega os canais
        for (int i = 0; i < categories.length(); i++) {
            JSONObject category = categories.getJSONObject(i);
            String categoryId = category.getString("category_id");
            String categoryName = category.getString("category_name");
            
            // Carrega os canais da categoria
            String streamsUrl = String.format(
                    "%s/%s", 
                    playlist.getUrl(), 
                    String.format(API_LIVE_STREAMS, playlist.getUsername(), playlist.getPassword(), categoryId)
            );
            
            String streamsJson = makeRequest(streamsUrl);
            JSONArray streams = new JSONArray(streamsJson);
            
            // Adiciona cada canal à playlist
            for (int j = 0; j < streams.length(); j++) {
                JSONObject stream = streams.getJSONObject(j);
                
                String id = UUID.randomUUID().toString();
                String name = stream.getString("name");
                String streamType = stream.getString("stream_type");
                
                // Constrói a URL do stream
                String streamUrl = String.format(
                        "%s/%s/%s/%s", 
                        playlist.getUrl(), 
                        streamType, 
                        playlist.getUsername(), 
                        playlist.getPassword()
                );
                
                if (stream.has("stream_id")) {
                    streamUrl += "/" + stream.getString("stream_id");
                }
                
                // Logo do canal
                String logoUrl = "";
                if (stream.has("stream_icon") && !stream.isNull("stream_icon")) {
                    logoUrl = stream.getString("stream_icon");
                }
                
                Channel channel = new Channel(id, name, streamUrl, logoUrl, categoryName);
                playlist.addChannel(channel);
            }
        }
        
        return playlist;
    }
    
    /**
     * Faz uma requisição HTTP GET
     * @param urlString URL da requisição
     * @return Resposta da requisição
     */
    private static String makeRequest(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;
            
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            
            return response.toString();
        } finally {
            connection.disconnect();
        }
    }
}
