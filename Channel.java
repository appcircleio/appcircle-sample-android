package com.pitv.player.model;

import java.io.Serializable;

/**
 * Classe que representa um canal de IPTV
 */
public class Channel implements Serializable {
    private String id;
    private String name;
    private String url;
    private String logoUrl;
    private String group;
    private boolean isFavorite;
    
    public Channel() {
        // Construtor vazio necessário para algumas operações
    }
    
    public Channel(String id, String name, String url, String logoUrl, String group) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.logoUrl = logoUrl;
        this.group = group;
        this.isFavorite = false;
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
    
    public String getLogoUrl() {
        return logoUrl;
    }
    
    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }
    
    public String getGroup() {
        return group;
    }
    
    public void setGroup(String group) {
        this.group = group;
    }
    
    public boolean isFavorite() {
        return isFavorite;
    }
    
    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Channel channel = (Channel) o;
        
        return id != null ? id.equals(channel.id) : channel.id == null;
    }
    
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
