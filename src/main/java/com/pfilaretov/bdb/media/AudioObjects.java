package com.pfilaretov.bdb.media;

import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IStreamCoder;

/**
 * Objects that is used in generating audio for a video
 */
public class AudioObjects {

    private final IContainer audioContainer;
    private final IStreamCoder audioCoder;
    private final int audioStreamIndex;

    public AudioObjects(IContainer audioContainer, IStreamCoder audioCoder, int audioStreamIndex) {
        this.audioContainer = audioContainer;
        this.audioCoder = audioCoder;
        this.audioStreamIndex = audioStreamIndex;
    }

    public IContainer getAudioContainer() {
        return audioContainer;
    }

    public IStreamCoder getAudioCoder() {
        return audioCoder;
    }

    public int getAudioStreamIndex() {
        return audioStreamIndex;
    }
}
