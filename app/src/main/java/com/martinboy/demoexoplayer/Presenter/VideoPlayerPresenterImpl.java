package com.martinboy.demoexoplayer.Presenter;

public interface VideoPlayerPresenterImpl {
    boolean checkNetworkStatus();
    void initialize(int videoType);
    void activeExoPlayer();
    void preparedPlayVideo();
    void memorizeVideoPosition();
    void releaseExoPlayer();
    void startHomeWatcher();
    void stopHomeWatcher();

    interface VideoFileURLListener {
        void onVideoFileURLReceived(String videoUrl, int videoType);
    }
}