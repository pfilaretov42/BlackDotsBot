package com.pfilaretov.bdb.bot;

import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.IAudioSamples;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.ICodec.ID;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IPacket;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Produces a video
 */
public class VideoMaker {

    private static final Logger LOG = LoggerFactory.getLogger(VideoMaker.class);

    // TODO - make paths relative
    private static final String BACKGROUND_PATH = "C:/Users/Petr_Filaretov/IdeaProjects/XugglerTest/media/img/video-background-2.jpg";

    // assume the number of packets in each audio
    // TODO - adjust this once all the audio is recorded - min value of all mp3 packets
    private static final int PACKETS_COUNT = 99;

    private final Map<String, String> notePaths;

    public VideoMaker() {
        notePaths = new HashMap<>(12);
        // TODO - make paths relative
        notePaths.put(Note.C1, "C:/Users/Petr_Filaretov/IdeaProjects/XugglerTest/media/audio/c1.mp3");
        notePaths.put(Note.D1, "C:/Users/Petr_Filaretov/IdeaProjects/XugglerTest/media/audio/d1.mp3");
        notePaths.put(Note.E1, "C:/Users/Petr_Filaretov/IdeaProjects/XugglerTest/media/audio/e1.mp3");
    }

    public File generateVideo(List<Note> notes) {
        LOG.info("Got notes to generate a video: {}", notes);

        // make a IMediaWriter to write the file.
        String resultFileName = getResultFileName();
        final IMediaWriter writer = ToolFactory.makeWriter(resultFileName);

        // take the background image and convert to the right image type
        // TODO - can we make it a class field?
        BufferedImage backgroundImage = getBackgroundImage();
        backgroundImage = convertToType(backgroundImage, BufferedImage.TYPE_3BYTE_BGR);

        // We tell it we're going to add one video stream, with id 0,
        // at position 0, and that it will have a fixed frame rate of FRAME_RATE.
        int videoInputIndex = 0;
        int videoStreamId = 0;
        // these are min values that works for background image, magic numbers...
        int width = 750;
        int height = 468;
        int videoStreamIndex = writer.addVideoStream(videoInputIndex, videoStreamId, ID.CODEC_ID_MPEG4, width, height);

        int audioInputIndex = 1;
        // TODO - audioStreamId is the same as videoStreamId???
        int audioStreamId = 0;
        int channelCount = 1; //c1AudioCoder.getChannels()
        // TODO - adjust this?
        int sampleRate = 32000; //c1AudioCoder.getSampleRate();
        int videoAudioStreamIndex = writer.addAudioStream(audioInputIndex, audioStreamId, channelCount, sampleRate);

        long startTime = System.nanoTime();

        for (Note note : notes) {
            String audioFilePath = notePaths.get(note.getHeight());

            // TODO - is it fine to open several containers from the same file concurrently?
            IContainer audioContainer = IContainer.make();
            if (audioContainer.open(audioFilePath, IContainer.Type.READ, null) < 0) {
                throw new IllegalArgumentException("Cannot find " + audioFilePath);
            }

            // read audio file and create stream
            IStreamCoder audioCoder = null;
            int audioStreamIndex = 0;
            for (int i = 0; i < audioContainer.getNumStreams(); i++) {
                IStream stream = audioContainer.getStream(i);
                IStreamCoder coder = stream.getStreamCoder();

                if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_AUDIO) {
                    audioCoder = coder;
                    audioStreamIndex = i;
                }
            }

            if (audioCoder == null) {
                throw new IllegalArgumentException("Cannot find audio stream for " + audioFilePath);
            }
            if (audioCoder.open(null, null) < 0) {
                throw new IllegalArgumentException("Cannot open audio coder for " + audioFilePath);
            }

            addNote(audioContainer, audioCoder, audioStreamIndex, writer, videoStreamIndex, videoAudioStreamIndex,
                backgroundImage, startTime, note.getDuration());

            // TODO - do not close them for each iteration, reuse
            audioCoder.close();
            audioContainer.close();
        }

        // tell the writer to close and write the trailer if needed
        writer.close();

        return new File(resultFileName);
    }

    private String getResultFileName() {
        return UUID.randomUUID().toString() + ".mp4";
    }

    private void addNote(IContainer audioContainer, IStreamCoder audioCoder, int audioStreamIndex,
        IMediaWriter writer, int videoStreamIndex, int videoAudioStreamIndex, BufferedImage backgroundImage,
        long startTime, int duration) {
        // scroll to the beginning of the audio file
        int seekFrameResult = audioContainer
            .seekKeyFrame(audioStreamIndex, audioContainer.getStartTime(), IContainer.SEEK_FLAG_ANY);
        if (seekFrameResult < 0) {
            throw new RuntimeException("Cannot seek key frame");
        }

        // TODO - pass as a parameter to save some memory?
        IPacket packet = IPacket.make();

        int iteration = 0;
        while (audioContainer.readNextPacket(packet) >= 0 && iteration != PACKETS_COUNT / duration) {
            writer.encodeVideo(videoStreamIndex, backgroundImage, System.nanoTime() - startTime, TimeUnit.NANOSECONDS);

            IAudioSamples samples = IAudioSamples.make(512, audioCoder.getChannels(), IAudioSamples.Format.FMT_S32);
            audioCoder.decodeAudio(samples, packet, 0);
            if (samples.isComplete()) {
                writer.encodeAudio(videoAudioStreamIndex, samples);
            }

            iteration++;
        }
    }

    private BufferedImage convertToType(BufferedImage sourceImage, int targetType) {
        BufferedImage image;

        // if the source image is already the target type, return the source image
        if (sourceImage.getType() == targetType) {
            image = sourceImage;
        }
        // otherwise create a new image of the target type and draw the new image
        else {
            image = new BufferedImage(sourceImage.getWidth(),
                sourceImage.getHeight(), targetType);
            image.getGraphics().drawImage(sourceImage, 0, 0, null);
        }

        return image;
    }

    private BufferedImage getBackgroundImage() {
        try {
            return ImageIO.read(new File(BACKGROUND_PATH));
        } catch (IOException e) {
            throw new IllegalArgumentException("Cannot read image by path: " + BACKGROUND_PATH, e);
        }

    }


}
