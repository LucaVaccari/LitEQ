package it.unibs.pajc.liteq;

import javax.sound.sampled.*;
import java.io.File;

public class SoundPlayer {
    private static final String AUDIO_FILE_PATH = "C:\\Users\\asus\\Desktop\\SuckYourLollipop.wav";

    // TODO: make buffer size selectable
    private static final int BUFFER_SIZE = 256;
    private SourceDataLine dataLine;

    public SoundPlayer() {
        try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(AUDIO_FILE_PATH))) {
            byte[] bytes = audioInputStream.readAllBytes();
            AudioFormat format = audioInputStream.getFormat();

            int duration =
                    (int) (bytes.length * 8. / format.getSampleRate() / format.getSampleSizeInBits() / format.getChannels());
            System.out.println("DURATION in secs: " + duration);

            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
            dataLine = (SourceDataLine) AudioSystem.getLine(info);
            dataLine.open(format);
            dataLine.start();

            //dataLine.write(bytes, 0, bytes.length);
        } catch (UnsupportedAudioFileException e) {
            System.out.println("Unsupported audio file");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() {
        dataLine.start();
    }

    public void stop() {
        dataLine.stop();
    }

    public void seek(int percentage) {

    }
}
