package com.pitv.player;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;
import com.pitv.player.model.Playlist;
import com.pitv.player.service.PlaylistManager;
import com.pitv.player.ui.AddPlaylistFragment;
import com.pitv.player.ui.ChannelsFragment;

import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private PlaylistManager playlistManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        // Configura o toggle do drawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Configura o listener de navegação
        navigationView.setNavigationItemSelectedListener(this);

        // Inicializa o gerenciador de playlists
        playlistManager = PlaylistManager.getInstance(this);

        // Carrega o fragmento inicial
        if (savedInstanceState == null) {
            // Se não houver playlists, mostra o fragmento de adicionar playlist
            if (playlistManager.getPlaylists().isEmpty()) {
                loadFragment(new AddPlaylistFragment());
                navigationView.setCheckedItem(R.id.nav_add_playlist);
            } else {
                // Caso contrário, mostra a lista de canais
                loadFragment(ChannelsFragment.newInstance(null, null));
                navigationView.setCheckedItem(R.id.nav_live_tv);
            }
        }

        // Atualiza o menu de navegação com as playlists
        updateNavigationMenu();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Atualiza o menu de navegação quando a atividade é retomada
        updateNavigationMenu();
    }

    @Override
    public void onBackPressed() {
        // Fecha o drawer se estiver aberto
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_live_tv) {
            // Mostra todos os canais de todas as playlists
            loadFragment(ChannelsFragment.newInstance(null, null));
        } else if (id == R.id.nav_movies) {
            // TODO: Implementar visualização de filmes
        } else if (id == R.id.nav_series) {
            // TODO: Implementar visualização de séries
        } else if (id == R.id.nav_favorites) {
            // Mostra os canais favoritos
            loadFragment(ChannelsFragment.newInstance(null, null));
        } else if (id == R.id.nav_epg) {
            // TODO: Implementar guia de programação
        } else if (id == R.id.nav_settings) {
            // TODO: Implementar configurações
        } else if (id == R.id.nav_add_playlist) {
            // Mostra o fragmento para adicionar playlist
            loadFragment(new AddPlaylistFragment());
        } else {
            // Verifica se é um item de playlist dinâmico
            String tag = item.getTitle().toString();
            List<Playlist> playlists = playlistManager.getPlaylists();
            
            for (Playlist playlist : playlists) {
                if (playlist.getName().equals(tag)) {
                    // Mostra os canais da playlist selecionada
                    loadFragment(ChannelsFragment.newInstance(playlist.getId(), null));
                    break;
                }
            }
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Carrega um fragmento no container principal
     * @param fragment Fragmento a ser carregado
     */
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
    }

    /**
     * Atualiza o menu de navegação com as playlists disponíveis
     */
    private void updateNavigationMenu() {
        // Obtém o menu de navegação
        android.view.Menu menu = navigationView.getMenu();
        
        // Remove os itens de playlist existentes
        int size = menu.size();
        for (int i = 0; i < size; i++) {
            MenuItem item = menu.getItem(i);
            if (item.getItemId() > 100) { // IDs dinâmicos para playlists
                menu.removeItem(item.getItemId());
            }
        }
        
        // Adiciona as playlists como itens de menu
        List<Playlist> playlists = playlistManager.getPlaylists();
        if (!playlists.isEmpty()) {
            // Adiciona um submenu para as playlists
            android.view.SubMenu playlistsMenu = menu.addSubMenu("Minhas Playlists");
            
            int id = 101; // IDs dinâmicos começando de 101
            for (Playlist playlist : playlists) {
                playlistsMenu.add(0, id++, 0, playlist.getName())
                        .setIcon(R.drawable.ic_playlist_add);
            }
        }
    }
}
