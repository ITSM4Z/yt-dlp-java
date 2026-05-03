package com.ytdlpjava.core;

public interface ProgressListener {
    void onStart();
    void onProgress(float percentage, String speed, String eta);
    void onComplete();
    void onError(String errorMessage);
}
