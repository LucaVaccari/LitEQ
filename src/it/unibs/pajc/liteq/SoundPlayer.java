package it.unibs.pajc.liteq;

import javax.sound.sampled.*;
import java.io.Closeable;
import java.io.File;

public class SoundPlayer implements LineListener, Closeable {
    private static final String AUDIO_FILE_PATH = "C:\\Users\\asus\\Desktop\\SuckYourLollipop.wav";

    // TODO: make buffer size selectable
    private final int bufferSize = 256000;
    private int playCursor = 0;
    private byte[] bytes;
    private boolean playing;
    private SourceDataLine dataLine;
    private Runnable playbackProcess;
    private Thread currentThread;

    public SoundPlayer() {
        try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(AUDIO_FILE_PATH))) {
            bytes = audioInputStream.readAllBytes();
            AudioFormat format = audioInputStream.getFormat();

            int duration =
                    (int) (bytes.length * 8. / format.getSampleRate() / format.getSampleSizeInBits() / format.getChannels());
            System.out.println("DURATION in secs: " + duration);

            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
            dataLine = (SourceDataLine) AudioSystem.getLine(info);
            dataLine.addLineListener(this);
            dataLine.open(format, bufferSize);

            //int dt = (int) (bufferSize * 000. / format.getSampleRate() / format.getSampleSizeInBits() / format
            // .getChannels());
            playbackProcess = () -> {
                if (playCursor < 0) playCursor = 0;

                while (playing) {
                    if (playCursor < bytes.length) {
                        int remainingBytes = bytes.length - playCursor;
                        int currentBufferSize = Math.min(remainingBytes, bufferSize);
                        currentBufferSize -= currentBufferSize % format.getFrameSize();

                        byte[] buffer = new byte[currentBufferSize];
                        System.arraycopy(bytes, playCursor, buffer, 0, currentBufferSize);
                        dataLine.write(buffer, 0, currentBufferSize);
                        if (playing) playCursor += bufferSize;
                    }
                }

                playCursor -= dataLine.available();
                dataLine.flush();
            };

            start();

        } catch (UnsupportedAudioFileException e) {
            System.out.println("Unsupported audio file");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(LineEvent event) {
        LineEvent.Type type = event.getType();
        if (type.equals(LineEvent.Type.CLOSE)) {
            System.out.println("CLOSE");
        } else if (type.equals(LineEvent.Type.OPEN)) {
            System.out.println("OPEN");
        } else if (type.equals(LineEvent.Type.START)) {
            System.out.println("START");
        } else if (type.equals(LineEvent.Type.STOP)) {
            System.out.println("STOP");
        }
    }

    public void start() {
        playing = false;
        if (currentThread != null) {
            try {
                currentThread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        playing = true;
        currentThread = new Thread(playbackProcess);
        dataLine.start();
        currentThread.start();

        System.out.println(playCursor);
    }

    public void pause() {
        playing = false;
        dataLine.stop();

        System.out.println(playCursor);
    }

    public void stop() {
        pause();
        seek(0);
    }

    public void seek(int percentage) {
        pause();
        playCursor = (int) (percentage / 100. * bytes.length);
        start();
    }

    @Override
    public void close() {
        dataLine.flush();
        stop();
        dataLine.close();
    }
}
