package com.pitv.player.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pitv.player.R;
import com.pitv.player.model.Channel;
import com.pitv.player.service.PlaylistManager;

import java.util.List;

/**
 * Adapter para exibição de canais em um RecyclerView
 */
public class ChannelAdapter extends RecyclerView.Adapter<ChannelAdapter.ChannelViewHolder> {
    
    private final Context context;
    private final List<Channel> channels;
    private final OnChannelClickListener listener;
    private final PlaylistManager playlistManager;
    
    public interface OnChannelClickListener {
        void onChannelClick(Channel channel);
    }
    
    public ChannelAdapter(Context context, List<Channel> channels, OnChannelClickListener listener) {
        this.context = context;
        this.channels = channels;
        this.listener = listener;
        this.playlistManager = PlaylistManager.getInstance(context);
    }
    
    @NonNull
    @Override
    public ChannelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_channel, parent, false);
        return new ChannelViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ChannelViewHolder holder, int position) {
        Channel channel = channels.get(position);
        holder.bind(channel);
    }
    
    @Override
    public int getItemCount() {
        return channels.size();
    }
    
    class ChannelViewHolder extends RecyclerView.ViewHolder {
        private final ImageView logoImageView;
        private final TextView nameTextView;
        private final TextView groupTextView;
        private final ImageView favoriteImageView;
        
        public ChannelViewHolder(@NonNull View itemView) {
            super(itemView);
            logoImageView = itemView.findViewById(R.id.channel_logo);
            nameTextView = itemView.findViewById(R.id.channel_name);
            groupTextView = itemView.findViewById(R.id.channel_group);
            favoriteImageView = itemView.findViewById(R.id.favorite_icon);
            
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onChannelClick(channels.get(position));
                }
            });
            
            favoriteImageView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Channel channel = channels.get(position);
                    toggleFavorite(channel);
                    notifyItemChanged(position);
                }
            });
        }
        
        public void bind(Channel channel) {
            nameTextView.setText(channel.getName());
            groupTextView.setText(channel.getGroup());
            
            // Carrega o logo do canal
            if (channel.getLogoUrl() != null && !channel.getLogoUrl().isEmpty()) {
                Glide.with(context)
                        .load(channel.getLogoUrl())
                        .placeholder(R.drawable.default_channel_logo)
                        .error(R.drawable.default_channel_logo)
                        .into(logoImageView);
            } else {
                logoImageView.setImageResource(R.drawable.default_channel_logo);
            }
            
            // Atualiza o ícone de favorito
            updateFavoriteIcon(channel);
        }
        
        private void updateFavoriteIcon(Channel channel) {
            if (playlistManager.isFavorite(channel)) {
                favoriteImageView.setImageResource(R.drawable.ic_favorite);
            } else {
                favoriteImageView.setImageResource(R.drawable.ic_favorite_border);
            }
        }
        
        private void toggleFavorite(Channel channel) {
            if (playlistManager.isFavorite(channel)) {
                playlistManager.removeFromFavorites(channel);
            } else {
                playlistManager.addToFavorites(channel);
            }
            updateFavoriteIcon(channel);
        }
    }
}
