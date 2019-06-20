package com.example.lucas.audiosample.audio.core;

public interface Callback {
    void onBufferAvailable(byte[] buffer);
}