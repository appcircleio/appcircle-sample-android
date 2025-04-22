package com.pitv.player.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.pitv.player.R;
import com.pitv.player.adapter.PlaylistTabAdapter;
import com.pitv.player.model.Playlist;
import com.pitv.player.service.PlaylistManager;

/**
 * Fragment para adicionar uma nova playlist
 */
public class AddPlaylistFragment extends Fragment implements 
        M3UInputFragment.OnM3UAddListener, 
        XtreamInputFragment.OnXtreamAddListener {
    
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private PlaylistTabAdapter tabAdapter;
    private PlaylistManager playlistManager;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        playlistManager = PlaylistManager.getInstance(requireContext());
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_playlist, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.view_pager);
        
        setupViewPager();
    }
    
    /**
     * Configura o ViewPager com os fragmentos de entrada M3U e Xtream Codes
     */
    private void setupViewPager() {
        tabAdapter = new PlaylistTabAdapter(this);
        viewPager.setAdapter(tabAdapter);
        
        // Conecta o TabLayout com o ViewPager2
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if (position == 0) {
                tab.setText(R.string.m3u_url);
            } else {
                tab.setText(R.string.xtream_codes);
            }
        }).attach();
    }
    
    @Override
    public void onM3UPlaylistAdd(String name, String url) {
        playlistManager.addM3UPlaylist(name, url, new PlaylistManager.PlaylistLoadListener() {
            @Override
            public void onPlaylistLoaded(Playlist playlist) {
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(requireContext(), R.string.playlist_added, Toast.LENGTH_SHORT).show();
                    // Volta para a lista de canais
                    requireActivity().getSupportFragmentManager().popBackStack();
                });
            }
            
            @Override
            public void onPlaylistLoadFailed(Exception e) {
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(requireContext(), R.string.error_loading, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
    
    @Override
    public void onXtreamPlaylistAdd(String name, String url, String username, String password) {
        playlistManager.addXtreamCodesPlaylist(name, url, username, password, new PlaylistManager.PlaylistLoadListener() {
            @Override
            public void onPlaylistLoaded(Playlist playlist) {
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(requireContext(), R.string.playlist_added, Toast.LENGTH_SHORT).show();
                    // Volta para a lista de canais
                    requireActivity().getSupportFragmentManager().popBackStack();
                });
            }
            
            @Override
            public void onPlaylistLoadFailed(Exception e) {
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(requireContext(), R.string.error_loading, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}
