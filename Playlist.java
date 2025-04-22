package com.pitv.player.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa uma playlist de IPTV
 */
public class Playlist implements Serializable {
    private String id;
    private String name;
    private String url;
    private String username;
    private String password;
    private PlaylistType type;
    private List<Channel> channels;
    private List<String> groups;
    
    public enum PlaylistType {
        M3U,
        XTREAM_CODES
    }
    
    public Playlist() {
        channels = new ArrayList<>();
        groups = new ArrayList<>();
    }
    
    public Playlist(String id, String name, String url, PlaylistType type) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.type = type;
        this.channels = new ArrayList<>();
        this.groups = new ArrayList<>();
    }
    
    public Playlist(String id, String name, String url, String username, String password, PlaylistType type) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.username = username;
        this.password = password;
        this.type = type;
        this.channels = new ArrayList<>();
        this.groups = new ArrayList<>();
    }
    
    // Getters e Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public PlaylistType getType() {
        return type;
    }
    
    public void setType(PlaylistType type) {
        this.type = type;
    }
    
    public List<Channel> getChannels() {
        return channels;
    }
    
    public void setChannels(List<Channel> channels) {
        this.channels = channels;
    }
    
    public void addChannel(Channel channel) {
        this.channels.add(channel);
        if (!groups.contains(channel.getGroup())) {
            groups.add(channel.getGroup());
        }
    }
    
    public List<String> getGroups() {
        return groups;
    }
    
    public void setGroups(List<String> groups) {
        this.groups = groups;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Playlist playlist = (Playlist) o;
        
        return id != null ? id.equals(playlist.id) : playlist.id == null;
    }
    
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
