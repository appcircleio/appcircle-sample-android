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
 * Fragment para entrada de credenciais Xtream Codes
 */
public class XtreamInputFragment extends Fragment {
    
    private EditText editServerUrl;
    private EditText editUsername;
    private EditText editPassword;
    private EditText editPlaylistName;
    private Button btnAddXtream;
    
    public interface OnXtreamAddListener {
        void onXtreamPlaylistAdd(String name, String url, String username, String password);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_xtream_input, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        editServerUrl = view.findViewById(R.id.edit_server_url);
        editUsername = view.findViewById(R.id.edit_username);
        editPassword = view.findViewById(R.id.edit_password);
        editPlaylistName = view.findViewById(R.id.edit_xtream_playlist_name);
        btnAddXtream = view.findViewById(R.id.btn_add_xtream);
        
        btnAddXtream.setOnClickListener(v -> addPlaylist());
    }
    
    /**
     * Adiciona uma nova playlist Xtream Codes
     */
    private void addPlaylist() {
        String url = editServerUrl.getText().toString().trim();
        String username = editUsername.getText().toString().trim();
        String password = editPassword.getText().toString().trim();
        String name = editPlaylistName.getText().toString().trim();
        
        if (url.isEmpty()) {
            Toast.makeText(requireContext(), R.string.invalid_url, Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(requireContext(), R.string.invalid_credentials, Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Se o nome estiver vazio, usa a URL como nome
        if (name.isEmpty()) {
            name = url;
        }
        
        // Notifica o listener
        if (getParentFragment() instanceof OnXtreamAddListener) {
            ((OnXtreamAddListener) getParentFragment()).onXtreamPlaylistAdd(name, url, username, password);
        }
    }
}
