package com.pitv.player.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.pitv.player.ui.M3UInputFragment;
import com.pitv.player.ui.XtreamInputFragment;

/**
 * Adapter para as abas de adição de playlist
 */
public class PlaylistTabAdapter extends FragmentStateAdapter {

    public PlaylistTabAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new M3UInputFragment();
        } else {
            return new XtreamInputFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2; // Duas abas: M3U e Xtream Codes
    }
}
