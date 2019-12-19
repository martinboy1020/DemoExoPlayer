package com.martinboy.demoexoplayer.View;

import android.content.Context;

import com.google.android.exoplayer2.ui.PlayerView;

public interface VideoPlayerView {
    PlayerView getExoPlayerView();
    Context getContext();
    void setProgressBarVisibility(int visibility);
    void showOfflinePopup();
}