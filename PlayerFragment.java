package com.pitv.player.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.pitv.player.R;
import com.pitv.player.model.Channel;
import com.pitv.player.service.PlaylistManager;

/**
 * Fragment para reprodução de vídeo
 */
public class PlayerFragment extends Fragment {
    private static final String ARG_CHANNEL = "channel";
    
    private Channel channel;
    private PlayerView playerView;
    private SimpleExoPlayer player;
    private ImageView channelLogo;
    private TextView channelName;
    private TextView programTitle;
    private TextView programTime;
    private ProgressBar loadingIndicator;
    private ImageButton btnPlayPause;
    private ImageButton btnFavorite;
    
    private PlaylistManager playlistManager;
    
    public static PlayerFragment newInstance(Channel channel) {
        PlayerFragment fragment = new PlayerFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CHANNEL, channel);
        fragment.setArguments(args);
        return fragment;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            channel = (Channel) getArguments().getSerializable(ARG_CHANNEL);
        }
        
        playlistManager = PlaylistManager.getInstance(requireContext());
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_player, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        playerView = view.findViewById(R.id.player_view);
        channelLogo = view.findViewById(R.id.channel_logo);
        channelName = view.findViewById(R.id.channel_name);
        programTitle = view.findViewById(R.id.program_title);
        programTime = view.findViewById(R.id.program_time);
        loadingIndicator = view.findViewById(R.id.loading_indicator);
        btnPlayPause = view.findViewById(R.id.btn_play_pause);
        btnFavorite = view.findViewById(R.id.btn_favorite);
        
        ImageButton btnPrevious = view.findViewById(R.id.btn_previous);
        ImageButton btnNext = view.findViewById(R.id.btn_next);
        ImageButton btnFullscreen = view.findViewById(R.id.btn_fullscreen);
        
        // Configura os botões
        btnPlayPause.setOnClickListener(v -> togglePlayPause());
        btnFavorite.setOnClickListener(v -> toggleFavorite());
        btnPrevious.setOnClickListener(v -> playPreviousChannel());
        btnNext.setOnClickListener(v -> playNextChannel());
        btnFullscreen.setOnClickListener(v -> toggleFullscreen());
        
        // Configura as informações do canal
        updateChannelInfo();
    }
    
    @Override
    public void onStart() {
        super.onStart();
        initializePlayer();
    }
    
    @Override
    public void onResume() {
        super.onResume();
        if (player == null) {
            initializePlayer();
        }
    }
    
    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }
    
    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }
    
    /**
     * Inicializa o player de vídeo
     */
    private void initializePlayer() {
        if (channel == null) return;
        
        player = new SimpleExoPlayer.Builder(requireContext()).build();
        playerView.setPlayer(player);
        
        // Configura o listener de eventos do player
        player.addListener(new Player.EventListener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playbackState == Player.STATE_BUFFERING) {
                    loadingIndicator.setVisibility(View.VISIBLE);
                } else if (playbackState == Player.STATE_READY) {
                    loadingIndicator.setVisibility(View.GONE);
                }
            }
            
            @Override
            public void onPlayerError(ExoPlaybackException error) {
                loadingIndicator.setVisibility(View.GONE);
                // TODO: Mostrar mensagem de erro
            }
        });
        
        // Prepara a fonte de mídia
        prepareMediaSource();
    }
    
    /**
     * Prepara a fonte de mídia para reprodução
     */
    private void prepareMediaSource() {
        Uri uri = Uri.parse(channel.getUrl());
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(requireContext(),
                Util.getUserAgent(requireContext(), "PITV"));
        
        MediaSource mediaSource;
        if (channel.getUrl().contains(".m3u8")) {
            // Fonte HLS
            mediaSource = new HlsMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(uri);
        } else {
            // Fonte progressiva
            mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(uri);
        }
        
        player.prepare(mediaSource);
        player.setPlayWhenReady(true);
    }
    
    /**
     * Libera os recursos do player
     */
    private void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }
    
    /**
     * Atualiza as informações do canal na interface
     */
    private void updateChannelInfo() {
        if (channel == null) return;
        
        channelName.setText(channel.getName());
        
        // Carrega o logo do canal se disponível
        if (channel.getLogoUrl() != null && !channel.getLogoUrl().isEmpty()) {
            Glide.with(this)
                    .load(channel.getLogoUrl())
                    .placeholder(R.drawable.default_channel_logo)
                    .error(R.drawable.default_channel_logo)
                    .into(channelLogo);
        } else {
            channelLogo.setImageResource(R.drawable.default_channel_logo);
        }
        
        // Atualiza o ícone de favorito
        updateFavoriteIcon();
        
        // TODO: Implementar EPG para obter informações do programa atual
        programTitle.setText("Informação não disponível");
        programTime.setText("");
    }
    
    /**
     * Atualiza o ícone de favorito com base no status do canal
     */
    private void updateFavoriteIcon() {
        if (playlistManager.isFavorite(channel)) {
            btnFavorite.setImageResource(R.drawable.ic_favorite);
        } else {
            btnFavorite.setImageResource(R.drawable.ic_favorite_border);
        }
    }
    
    /**
     * Alterna entre reproduzir e pausar o vídeo
     */
    private void togglePlayPause() {
        if (player != null) {
            player.setPlayWhenReady(!player.getPlayWhenReady());
            btnPlayPause.setImageResource(player.getPlayWhenReady() ? 
                    R.drawable.ic_pause : R.drawable.ic_play);
        }
    }
    
    /**
     * Alterna o status de favorito do canal
     */
    private void toggleFavorite() {
        if (playlistManager.isFavorite(channel)) {
            playlistManager.removeFromFavorites(channel);
        } else {
            playlistManager.addToFavorites(channel);
        }
        updateFavoriteIcon();
    }
    
    /**
     * Reproduz o canal anterior na lista
     */
    private void playPreviousChannel() {
        // TODO: Implementar navegação entre canais
    }
    
    /**
     * Reproduz o próximo canal na lista
     */
    private void playNextChannel() {
        // TODO: Implementar navegação entre canais
    }
    
    /**
     * Alterna entre modo normal e tela cheia
     */
    private void toggleFullscreen() {
        // TODO: Implementar modo tela cheia
    }
}
