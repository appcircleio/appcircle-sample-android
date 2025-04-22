package com.pitv.player.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pitv.player.R;

/**
 * Fragment para entrada de URL M3U
 */
public class M3UInputFragment extends Fragment {
    
    private EditText editM3uUrl;
    private EditText editPlaylistName;
    private Button btnAddPlaylist;
    
    public interface OnM3UAddListener {
        void onM3UPlaylistAdd(String name, String url);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_m3u_input, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        editM3uUrl = view.findViewById(R.id.edit_m3u_url);
        editPlaylistName = view.findViewById(R.id.edit_playlist_name);
        btnAddPlaylist = view.findViewById(R.id.btn_add_playlist);
        
        btnAddPlaylist.setOnClickListener(v -> addPlaylist());
    }
    
    /**
     * Adiciona uma nova playlist M3U
     */
    private void addPlaylist() {
        String url = editM3uUrl.getText().toString().trim();
        String name = editPlaylistName.getText().toString().trim();
        
        if (url.isEmpty()) {
            Toast.makeText(requireContext(), R.string.invalid_url, Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Se o nome estiver vazio, usa a URL como nome
        if (name.isEmpty()) {
            name = url;
        }
        
        // Notifica o listener
        if (getParentFragment() instanceof OnM3UAddListener) {
            ((OnM3UAddListener) getParentFragment()).onM3UPlaylistAdd(name, url);
        }
    }
}
