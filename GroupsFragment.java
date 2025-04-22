package com.pitv.player.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pitv.player.R;
import com.pitv.player.model.Playlist;
import com.pitv.player.service.PlaylistManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment para exibição dos grupos de canais de uma playlist
 */
public class GroupsFragment extends Fragment {
    private static final String ARG_PLAYLIST_ID = "playlist_id";
    
    private String playlistId;
    private ListView listView;
    private TextView emptyView;
    private PlaylistManager playlistManager;
    
    public static GroupsFragment newInstance(String playlistId) {
        GroupsFragment fragment = new GroupsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PLAYLIST_ID, playlistId);
        fragment.setArguments(args);
        return fragment;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            playlistId = getArguments().getString(ARG_PLAYLIST_ID);
        }
        
        playlistManager = PlaylistManager.getInstance(requireContext());
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_groups, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        listView = view.findViewById(R.id.groups_list_view);
        emptyView = view.findViewById(R.id.empty_view);
        
        // Carrega os grupos
        loadGroups();
        
        // Configura o listener de clique
        listView.setOnItemClickListener((parent, view1, position, id) -> {
            String group = (String) parent.getItemAtPosition(position);
            
            // Abre o fragmento de canais filtrado pelo grupo
            ChannelsFragment channelsFragment = ChannelsFragment.newInstance(playlistId, group);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_frame, channelsFragment)
                    .addToBackStack(null)
                    .commit();
        });
    }
    
    /**
     * Carrega os grupos da playlist
     */
    private void loadGroups() {
        List<String> groups = new ArrayList<>();
        
        if (playlistId != null) {
            Playlist playlist = playlistManager.getPlaylistById(playlistId);
            if (playlist != null) {
                groups.addAll(playlist.getGroups());
            }
        }
        
        // Configura o adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_list_item_1,
                groups
        );
        
        listView.setAdapter(adapter);
        
        // Atualiza a visibilidade
        if (groups.isEmpty()) {
            listView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            listView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }
}
