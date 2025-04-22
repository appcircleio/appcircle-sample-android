package com.pitv.player.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pitv.player.R;
import com.pitv.player.adapter.ChannelAdapter;
import com.pitv.player.model.Channel;
import com.pitv.player.model.Playlist;
import com.pitv.player.service.PlaylistManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment para exibição da lista de canais
 */
public class ChannelsFragment extends Fragment implements ChannelAdapter.OnChannelClickListener {
    private static final String ARG_PLAYLIST_ID = "playlist_id";
    private static final String ARG_GROUP = "group";
    
    private String playlistId;
    private String group;
    
    private RecyclerView recyclerView;
    private TextView emptyView;
    private SearchView searchView;
    private ChannelAdapter adapter;
    
    private PlaylistManager playlistManager;
    private List<Channel> channels = new ArrayList<>();
    
    public static ChannelsFragment newInstance(String playlistId, String group) {
        ChannelsFragment fragment = new ChannelsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PLAYLIST_ID, playlistId);
        args.putString(ARG_GROUP, group);
        fragment.setArguments(args);
        return fragment;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            playlistId = getArguments().getString(ARG_PLAYLIST_ID);
            group = getArguments().getString(ARG_GROUP);
        }
        
        playlistManager = PlaylistManager.getInstance(requireContext());
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_channels, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        recyclerView = view.findViewById(R.id.channels_recycler_view);
        emptyView = view.findViewById(R.id.empty_view);
        searchView = view.findViewById(R.id.search_view);
        
        // Configura o RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new ChannelAdapter(requireContext(), channels, this);
        recyclerView.setAdapter(adapter);
        
        // Configura a busca
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterChannels(query);
                return true;
            }
            
            @Override
            public boolean onQueryTextChange(String newText) {
                filterChannels(newText);
                return true;
            }
        });
        
        // Carrega os canais
        loadChannels();
    }
    
    /**
     * Carrega os canais da playlist
     */
    private void loadChannels() {
        channels.clear();
        
        if (playlistId != null) {
            // Carrega canais de uma playlist específica
            Playlist playlist = playlistManager.getPlaylistById(playlistId);
            if (playlist != null) {
                for (Channel channel : playlist.getChannels()) {
                    // Se um grupo foi especificado, filtra por ele
                    if (group == null || group.isEmpty() || channel.getGroup().equals(group)) {
                        channels.add(channel);
                    }
                }
            }
        } else {
            // Carrega canais favoritos
            channels.addAll(playlistManager.getFavorites());
        }
        
        updateUI();
    }
    
    /**
     * Filtra os canais com base na consulta de busca
     * @param query Texto de busca
     */
    private void filterChannels(String query) {
        List<Channel> filteredChannels = new ArrayList<>();
        
        if (query == null || query.isEmpty()) {
            // Se a consulta estiver vazia, mostra todos os canais
            loadChannels();
            return;
        }
        
        // Filtra os canais que contêm a consulta no nome
        String lowerCaseQuery = query.toLowerCase();
        for (Channel channel : channels) {
            if (channel.getName().toLowerCase().contains(lowerCaseQuery)) {
                filteredChannels.add(channel);
            }
        }
        
        channels.clear();
        channels.addAll(filteredChannels);
        updateUI();
    }
    
    /**
     * Atualiza a interface com base nos canais carregados
     */
    private void updateUI() {
        adapter.notifyDataSetChanged();
        
        if (channels.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }
    
    @Override
    public void onChannelClick(Channel channel) {
        // Abre o player com o canal selecionado
        PlayerFragment playerFragment = PlayerFragment.newInstance(channel);
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, playerFragment)
                .addToBackStack(null)
                .commit();
    }
}
