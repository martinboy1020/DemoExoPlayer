package com.martinboy.demoexoplayer.Presenter;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ext.rtmp.RtmpDataSourceFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.martinboy.demoexoplayer.Model.VideoApi;
import com.martinboy.demoexoplayer.View.VideoPlayerView;

public class VideoFilePlayerPresenter implements VideoPlayerPresenterImpl, VideoPlayerPresenterImpl.VideoFileURLListener,
        Player.EventListener, HomeWatcher.OnHomePressedListener {

    private VideoPlayerView videoPlayerView;
    private VideoApi videoApi;
    private HomeWatcher homeWatcher;

    private SimpleExoPlayer exoPlayer;
    private DefaultTrackSelector trackSelector;
    private MediaSource mediaSource;
    private HlsMediaSource hlsMediaSource;
    private DashMediaSource dashMediaSource;

    private Context context;
    private String videoName;
    private boolean shouldAutoPlay = true;
    private long currentPosition = -1;
    private int videoType;

    public VideoFilePlayerPresenter(VideoPlayerView videoPlayerView) {
        this.videoPlayerView = videoPlayerView;
        context = videoPlayerView.getContext();
        videoApi = new VideoApi(context);
        homeWatcher = new HomeWatcher(context);
    }

    @Override
    public boolean checkNetworkStatus() {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();

        if (info == null || !info.isConnected()) {
            videoPlayerView.showOfflinePopup();
            return false;
        }

        return true;
    }

    @Override
    public void initialize(int videoType) {
        activeExoPlayer();
        this.videoType = videoType;
        if (videoApi != null)
            videoApi.getVideoFileURL(videoType, this);
    }

    @Override
    public void activeExoPlayer() {

        if (trackSelector == null) {
            trackSelector = new DefaultTrackSelector(context);
        }

        if (exoPlayer == null) {
            exoPlayer =
                    new SimpleExoPlayer.Builder(context)
                            .setTrackSelector(trackSelector)
                            .build();
            exoPlayer.addListener(this);
            exoPlayer.setPlayWhenReady(shouldAutoPlay);
            videoPlayerView.getExoPlayerView().setPlayer(exoPlayer);
        }
    }

    @Override
    public void onVideoFileURLReceived(String videoUrl, int videoType) {
        Uri videoURI = Uri.parse(videoUrl);

        switch (videoType) {

            case 0:
                mediaSource = initialVideoMediaSource(videoURI);
                break;
            case 1:
                mediaSource = initialRtmpMediaSource(videoURI);
                break;
            case 2:
                hlsMediaSource = initalHlsMediaSource(videoURI);
                break;
            case 3:
                dashMediaSource = initalDashMediaSource(videoURI);
                break;

        }
        preparedPlayVideo();
    }

    @Override
    public void preparedPlayVideo() {

        if(videoType == 0 || videoType == 1) {

            if (mediaSource != null) {
                exoPlayer.prepare(mediaSource);
                exoPlayer.setPlayWhenReady(true);
            } else if(videoName != null && !videoName.equals("")) {
                initialize(videoType);
            } else {
                Toast.makeText(context, "Video can not play", Toast.LENGTH_SHORT).show();
            }

        } else if (videoType == 2) {

            if (hlsMediaSource != null) {
                exoPlayer.prepare(hlsMediaSource);
                exoPlayer.setPlayWhenReady(true);
            } else if(videoName != null && !videoName.equals("")) {
                initialize(videoType);
            } else {
                Toast.makeText(context, "Video can not play", Toast.LENGTH_SHORT).show();
            }

        } else {

            if (dashMediaSource != null) {
                exoPlayer.prepare(dashMediaSource);
                exoPlayer.setPlayWhenReady(true);
            } else if(videoName != null && !videoName.equals("")) {
                initialize(videoType);
            } else {
                Toast.makeText(context, "Video can not play", Toast.LENGTH_SHORT).show();
            }

        }

    }

    @Override
    public void memorizeVideoPosition() {
        if (exoPlayer != null) {
            currentPosition = exoPlayer.getCurrentPosition();
            shouldAutoPlay = false;
            exoPlayer.setPlayWhenReady(false);
        }
    }

    private MediaSource initialVideoMediaSource(Uri uri) {

        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
                Util.getUserAgent(context, getApplicationName(context)));

        // This is the MediaSource representing the media to be played.
        return new ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri);

    }

    private MediaSource initialRtmpMediaSource(Uri uri) {

        RtmpDataSourceFactory rtmpDataSourceFactory = new RtmpDataSourceFactory();

        // This is the MediaSource representing the media to be played.
        return new ProgressiveMediaSource.Factory(rtmpDataSourceFactory)
                .createMediaSource(uri);

    }

    private HlsMediaSource initalHlsMediaSource(Uri uri) {
        // Create a data source factory.
        DataSource.Factory dataSourceFactory =
                new DefaultHttpDataSourceFactory(Util.getUserAgent(context, getApplicationName(context)));
        // Create a HLS media source pointing to a playlist uri.
        return new HlsMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
    }

    private DashMediaSource initalDashMediaSource(Uri uri) {
        // Create a data source factory.
        DataSource.Factory dataSourceFactory =
                new DefaultHttpDataSourceFactory(Util.getUserAgent(context, getApplicationName(context)));
        // Create a HLS media source pointing to a playlist uri.
        return new DashMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
    }

    private String getApplicationName(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : context.getString(stringId);
    }

    @Override
    public void releaseExoPlayer() {
        if (exoPlayer != null) {
            shouldAutoPlay = exoPlayer.getPlayWhenReady();
            exoPlayer.release();
            exoPlayer = null;
            trackSelector = null;
        }
    }

    @Override
    public void startHomeWatcher() {
        homeWatcher.setOnHomePressedListener(this);
        homeWatcher.startWatch();
    }

    @Override
    public void stopHomeWatcher() {
        homeWatcher.setOnHomePressedListener(null);
        homeWatcher.stopWatch();
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        switch (playbackState) {
            case Player.STATE_ENDED:
                shouldAutoPlay = false;
                exoPlayer.setPlayWhenReady(false);
                exoPlayer.seekTo(0);
                videoPlayerView.setProgressBarVisibility(View.GONE);
                break;
            case Player.STATE_READY:
                if (currentPosition != -1) {
                    exoPlayer.seekTo(currentPosition);
                    currentPosition = -1;
                } else {
                    videoPlayerView.setProgressBarVisibility(View.GONE);
                }
                break;
            case Player.STATE_BUFFERING:
                videoPlayerView.setProgressBarVisibility(View.VISIBLE);
                break;
            case Player.STATE_IDLE:
                videoPlayerView.setProgressBarVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        exoPlayer.stop();
    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onHomePressed() {
        releaseExoPlayer();
    }

    @Override
    public void onHomeLongPressed() {
        releaseExoPlayer();
    }
}