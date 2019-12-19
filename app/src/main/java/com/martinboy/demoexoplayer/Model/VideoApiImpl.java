package com.martinboy.demoexoplayer.Model;

import com.martinboy.demoexoplayer.Presenter.VideoPlayerPresenterImpl;

public interface VideoApiImpl {
    void getVideoFileURL(int videoType, VideoPlayerPresenterImpl.VideoFileURLListener videoFileURLListener);
}