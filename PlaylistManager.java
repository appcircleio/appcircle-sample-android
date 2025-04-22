package com.pitv.player.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pitv.player.api.XtreamCodesClient;
import com.pitv.player.model.Channel;
import com.pitv.player.model.Playlist;
import com.pitv.player.parser.M3UParser;

import org.json.JSONException;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Gerenciador de playlists
 */
public class PlaylistManager {
    private static final String TAG = "PlaylistManager";
    private static final String PREFS_NAME = "com.pitv.player.playlists";
    private static final String KEY_PLAYLISTS = "playlists";
    private static final String KEY_FAVORITES = "favorites";
    
    private static PlaylistManager instance;
    private Context context;
    private List<Playlist> playlists;
    private List<Channel> favorites;
    private PlaylistLoadListener listener;
    
    public interface PlaylistLoadListener {
        void onPlaylistLoaded(Playlist playlist);
        void onPlaylistLoadFailed(Exception e);
    }
    
    private PlaylistManager(Context context) {
        this.context = context.getApplicationContext();
        this.playlists = loadPlaylistsFromPrefs();
        this.favorites = loadFavoritesFromPrefs();
    }
    
    public static synchronized PlaylistManager getInstance(Context context) {
        if (instance == null) {
            instance = new PlaylistManager(context);
        }
        return instance;
    }
    
    /**
     * Adiciona uma nova playlist M3U
     * @param name Nome da playlist
     * @param url URL da playlist
     * @param listener Listener para notificar quando a playlist for carregada
     */
    public void addM3UPlaylist(String name, String url, PlaylistLoadListener listener) {
        this.listener = listener;
        
        String id = UUID.randomUUID().toString();
        Playlist playlist = new Playlist(id, name, url, Playlist.PlaylistType.M3U);
        
        new LoadM3UPlaylistTask().execute(playlist);
    }
    
    /**
     * Adiciona uma nova playlist Xtream Codes
     * @param name Nome da playlist
     * @param url URL do servidor
     * @param username Nome de usuário
     * @param password Senha
     * @param listener Listener para notificar quando a playlist for carregada
     */
    public void addXtreamCodesPlaylist(String name, String url, String username, String password, PlaylistLoadListener listener) {
        this.listener = listener;
        
        String id = UUID.randomUUID().toString();
        Playlist playlist = new Playlist(id, name, url, username, password, Playlist.PlaylistType.XTREAM_CODES);
        
        new LoadXtreamCodesPlaylistTask().execute(playlist);
    }
    
    /**
     * Obtém todas as playlists
     * @return Lista de playlists
     */
    public List<Playlist> getPlaylists() {
        return playlists;
    }
    
    /**
     * Obtém uma playlist pelo ID
     * @param playlistId ID da playlist
     * @return Playlist encontrada ou null se não existir
     */
    public Playlist getPlaylistById(String playlistId) {
        for (Playlist playlist : playlists) {
            if (playlist.getId().equals(playlistId)) {
                return playlist;
            }
        }
        return null;
    }
    
    /**
     * Remove uma playlist
     * @param playlistId ID da playlist a ser removida
     */
    public void removePlaylist(String playlistId) {
        Playlist playlistToRemove = null;
        
        for (Playlist playlist : playlists) {
            if (playlist.getId().equals(playlistId)) {
                playlistToRemove = playlist;
                break;
            }
        }
        
        if (playlistToRemove != null) {
            playlists.remove(playlistToRemove);
            savePlaylistsToPrefs();
        }
    }
    
    /**
     * Adiciona um canal aos favoritos
     * @param channel Canal a ser adicionado aos favoritos
     */
    public void addToFavorites(Channel channel) {
        if (!isFavorite(channel)) {
            channel.setFavorite(true);
            favorites.add(channel);
            saveFavoritesToPrefs();
        }
    }
    
    /**
     * Remove um canal dos favoritos
     * @param channel Canal a ser removido dos favoritos
     */
    public void removeFromFavorites(Channel channel) {
        Channel channelToRemove = null;
        
        for (Channel favoriteChannel : favorites) {
            if (favoriteChannel.getId().equals(channel.getId())) {
                channelToRemove = favoriteChannel;
                break;
            }
        }
        
        if (channelToRemove != null) {
            channelToRemove.setFavorite(false);
            favorites.remove(channelToRemove);
            saveFavoritesToPrefs();
        }
    }
    
    /**
     * Verifica se um canal está nos favoritos
     * @param channel Canal a ser verificado
     * @return true se o canal estiver nos favoritos, false caso contrário
     */
    public boolean isFavorite(Channel channel) {
        for (Channel favoriteChannel : favorites) {
            if (favoriteChannel.getId().equals(channel.getId())) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Obtém todos os canais favoritos
     * @return Lista de canais favoritos
     */
    public List<Channel> getFavorites() {
        return favorites;
    }
    
    /**
     * Carrega as playlists salvas nas preferências
     * @return Lista de playlists
     */
    private List<Playlist> loadPlaylistsFromPrefs() {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String playlistsJson = prefs.getString(KEY_PLAYLISTS, null);
        
        if (playlistsJson != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<Playlist>>(){}.getType();
            return gson.fromJson(playlistsJson, type);
        }
        
        return new ArrayList<>();
    }
    
    /**
     * Salva as playlists nas preferências
     */
    private void savePlaylistsToPrefs() {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        
        Gson gson = new Gson();
        String playlistsJson = gson.toJson(playlists);
        
        editor.putString(KEY_PLAYLISTS, playlistsJson);
        editor.apply();
    }
    
    /**
     * Carrega os favoritos salvos nas preferências
     * @return Lista de canais favoritos
     */
    private List<Channel> loadFavoritesFromPrefs() {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String favoritesJson = prefs.getString(KEY_FAVORITES, null);
        
        if (favoritesJson != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<Channel>>(){}.getType();
            return gson.fromJson(favoritesJson, type);
        }
        
        return new ArrayList<>();
    }
    
    /**
     * Salva os favoritos nas preferências
     */
    private void saveFavoritesToPrefs() {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        
        Gson gson = new Gson();
        String favoritesJson = gson.toJson(favorites);
        
        editor.putString(KEY_FAVORITES, favoritesJson);
        editor.apply();
    }
    
    /**
     * AsyncTask para carregar uma playlist M3U
     */
    private class LoadM3UPlaylistTask extends AsyncTask<Playlist, Void, Playlist> {
        private Exception exception;
        
        @Override
        protected Playlist doInBackground(Playlist... playlists) {
            Playlist playlist = playlists[0];
            
            try {
                return M3UParser.parseFromUrl(playlist);
            } catch (IOException e) {
                Log.e(TAG, "Erro ao carregar playlist M3U", e);
                exception = e;
                return null;
            }
        }
        
        @Override
        protected void onPostExecute(Playlist playlist) {
            if (playlist != null) {
                playlists.add(playlist);
                savePlaylistsToPrefs();
                
                if (listener != null) {
                    listener.onPlaylistLoaded(playlist);
                }
            } else if (exception != null && listener != null) {
                listener.onPlaylistLoadFailed(exception);
            }
        }
    }
    
    /**
     * AsyncTask para carregar uma playlist Xtream Codes
     */
    private class LoadXtreamCodesPlaylistTask extends AsyncTask<Playlist, Void, Playlist> {
        private Exception exception;
        
        @Override
        protected Playlist doInBackground(Playlist... playlists) {
            Playlist playlist = playlists[0];
            
            try {
                return XtreamCodesClient.loadChannels(playlist);
            } catch (IOException | JSONException e) {
                Log.e(TAG, "Erro ao carregar playlist Xtream Codes", e);
                exception = e;
                return null;
            }
        }
        
        @Override
        protected void onPostExecute(Playlist playlist) {
            if (playlist != null) {
                playlists.add(playlist);
                savePlaylistsToPrefs();
                
                if (listener != null) {
                    listener.onPlaylistLoaded(playlist);
                }
            } else if (exception != null && listener != null) {
                listener.onPlaylistLoadFailed(exception);
            }
        }
    }
}
