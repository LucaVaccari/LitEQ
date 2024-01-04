package it.unibs.pajc.liteq;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class Main {

    private JFrame frame;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Main window = new Main();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * @wbp.parser.entryPoint
     */
    public Main() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        frame.getContentPane().add(panel, BorderLayout.SOUTH);
        panel.setLayout(new GridLayout(0, 3, 0, 0));

        JButton btnPlay = new JButton("Play");
        btnPlay.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(btnPlay);

        JButton btnPause = new JButton("Pause");
        btnPause.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(btnPause);

        JButton btnStop = new JButton("Stop");
        btnStop.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(btnStop);

        JSeparator separator = new JSeparator();
        separator.setOrientation(SwingConstants.VERTICAL);
        panel.add(separator);

        JSlider sliderSeek = new JSlider();
        sliderSeek.setValue(0);
        sliderSeek.setToolTipText("Seek playback");
        panel.add(sliderSeek);

        SoundPlayer player = new SoundPlayer();

        btnPlay.addActionListener(l -> player.start());
        btnPause.addActionListener(l -> player.pause());
        btnStop.addActionListener(l -> player.stop());
        sliderSeek.addMouseListener(new MouseListener() {
            @Override
            public void mouseReleased(MouseEvent e) {
                player.seek(sliderSeek.getValue());
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        frame.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
            }

            @Override
            public void windowClosing(WindowEvent e) {
            }

            @Override
            public void windowClosed(WindowEvent e) {
                player.close();
            }

            @Override
            public void windowIconified(WindowEvent e) {
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
            }

            @Override
            public void windowActivated(WindowEvent e) {
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
            }
        });
    }
}
