package com.martinboy.demoexoplayer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.google.android.exoplayer2.ui.PlayerView;
import com.martinboy.demoexoplayer.Presenter.VideoFilePlayerPresenter;
import com.martinboy.demoexoplayer.View.VideoPlayerView;

import androidx.appcompat.app.AppCompatActivity;

public class VideoPlayerActivity extends AppCompatActivity implements VideoPlayerView {

    private PlayerView exoPlayerView;

    private ProgressBar progressBar;

    private VideoFilePlayerPresenter videoPlayerPresenter;

    private boolean isNetworkFine;
    private int videoType = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_player);

        Intent intent = getIntent();
        if(intent != null) {
            videoType = intent.getIntExtra("videoType", -1);
        }

        exoPlayerView = findViewById(R.id.exo_player_view);
        progressBar = findViewById(R.id.proshowOfflinePopupress_bar);

        exoPlayerView.requestFocus();

        videoPlayerPresenter = new VideoFilePlayerPresenter(this);
        isNetworkFine = videoPlayerPresenter.checkNetworkStatus();

        if (isNetworkFine) {
            videoPlayerPresenter.initialize(videoType);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoPlayerPresenter.startHomeWatcher();

        if (!isNetworkFine && videoPlayerPresenter.checkNetworkStatus()) {
            isNetworkFine = true;
            videoPlayerPresenter.initialize(videoType);
        } else {
            videoPlayerPresenter.activeExoPlayer();
            videoPlayerPresenter.preparedPlayVideo();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isNetworkFine) {
            videoPlayerPresenter.memorizeVideoPosition();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        videoPlayerPresenter.stopHomeWatcher();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoPlayerPresenter.releaseExoPlayer();
    }

    @Override
    public PlayerView getExoPlayerView() {
        return exoPlayerView;
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    public void setProgressBarVisibility(int visibility) {
        progressBar.setVisibility(visibility);
    }

    @Override
    public void showOfflinePopup() {
        finish();
    }

}