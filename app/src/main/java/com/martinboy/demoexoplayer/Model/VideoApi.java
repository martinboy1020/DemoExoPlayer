package com.martinboy.demoexoplayer.Model;

import android.content.Context;

import com.martinboy.demoexoplayer.Presenter.VideoPlayerPresenterImpl;

public class VideoApi implements VideoApiImpl {

    private Context context;

    private String mp4Url = "https://www.sample-videos.com/video123/mp4/720/big_buck_bunny_720p_1mb.mp4";
    private String rtmpUrl = "rtmp://fms.105.net/live/rmc1";
    private String hlsUrl = "https://devstreaming-cdn.apple.com/videos/streaming/examples/img_bipbop_adv_example_ts/master.m3u8";
    private String dashUrl = "http://183.59.160.61:30001/PLTV/88888905/224/3221227518/index.m3u8";

    public VideoApi(Context context) {
        this.context = context;
    }

    @Override
    public void getVideoFileURL(int videoType, VideoPlayerPresenterImpl.VideoFileURLListener videoFileURLListener) {

        switch (videoType) {

            case 0:
                videoFileURLListener.onVideoFileURLReceived(mp4Url, videoType);
                break;
            case 1:
                videoFileURLListener.onVideoFileURLReceived(rtmpUrl, videoType);
                break;
            case 2:
                videoFileURLListener.onVideoFileURLReceived(hlsUrl, videoType);
                break;
            case 3:
                videoFileURLListener.onVideoFileURLReceived(dashUrl, videoType);
                break;

        }

    }

}