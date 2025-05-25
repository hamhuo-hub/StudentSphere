package com.hamhuo.massey.slapocalypse.core;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Stack;

public abstract class GameEngine implements KeyListener, MouseListener, MouseMotionListener {
    //-------------------------------------------------------
    // Game Engine Frame and Panel
    //-------------------------------------------------------
    JFrame mFrame;
    GamePanel mPanel;
    int mWidth, mHeight;
    public Graphics2D mGraphics;
    boolean initialised = false;

    //-------------------------------------------------------
    // Time-Related functions
    //-------------------------------------------------------

    public long getTime() {
        return System.currentTimeMillis();
    }

    public void sleep(double ms) {
        try {
            Thread.sleep((long)ms);
        } catch(Exception e) {
            // Do Nothing
        }
    }

    //-------------------------------------------------------
    // Functions to control the framerate
    //-------------------------------------------------------
    long time = 0, oldTime = 0;

    public long measureTime() {
        time = getTime();
        if(oldTime == 0) {
            oldTime = time;
        }
        long passed = time - oldTime;
        oldTime = time;
        return passed;
    }

    //-------------------------------------------------------
    // Functions for setting up the window
    //-------------------------------------------------------
    public void setupWindow(int width, int height) {
        mFrame = new JFrame();
        mPanel = new GamePanel();

        // Set fullscreen mode
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        if (gd.isFullScreenSupported()) {
            mFrame.setUndecorated(true);
            gd.setFullScreenWindow(mFrame);
            DisplayMode dm = gd.getDisplayMode();
            mWidth = dm.getWidth();
            mHeight = dm.getHeight();
        } else {
            // Fallback to windowed mode
            mWidth = width;
            mHeight = height;
            mFrame.setSize(width, height);
            mFrame.setLocationRelativeTo(null);
        }

        mFrame.setTitle("Slapocalypse");
        mFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mFrame.add(mPanel);
        mFrame.setVisible(true);

        mPanel.setDoubleBuffered(true);
        mPanel.addMouseListener(this);
        mPanel.addMouseMotionListener(this);

        KeyboardFocusManager.getCurrentKeyboardFocusManager()
                .addKeyEventDispatcher(new KeyEventDispatcher() {
                    @Override
                    public boolean dispatchKeyEvent(KeyEvent e) {
                        switch (e.getID()) {
                            case KeyEvent.KEY_PRESSED:
                                GameEngine.this.keyPressed(e);
                                return false;
                            case KeyEvent.KEY_RELEASED:
                                GameEngine.this.keyReleased(e);
                                return false;
                            case KeyEvent.KEY_TYPED:
                                GameEngine.this.keyTyped(e);
                                return false;
                            default:
                                return false;
                        }
                    }
                });

        // Adjust panel size
        mPanel.setSize(mWidth, mHeight);
    }

    public void setWindowSize(final int width, final int height) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Insets insets = mFrame.getInsets();
                mWidth = width;
                mHeight = height;
                mFrame.setSize(width + insets.left + insets.right, height + insets.top + insets.bottom);
                mPanel.setSize(width, height);
            }
        });
    }

    public int width() {
        return mWidth;
    }

    public int height() {
        return mHeight;
    }

    //-------------------------------------------------------
    // Main Game function
    //-------------------------------------------------------

    public GameEngine() {
        mTransforms = new Stack<AffineTransform>();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                setupWindow(1000, 1000);
            }
        });
    }

    public static void createGame(GameEngine game, int framerate) {
        game.init();
        game.gameLoop(framerate);
    }

    public static void createGame(GameEngine game) {
        createGame(game, 30);
    }


    protected class GameTimer extends Timer {
        private static final long serialVersionUID = 1L;
        private int framerate;

        protected GameTimer(int framerate, ActionListener listener) {
            super(1000/framerate, listener);
            this.framerate = framerate;
        }


        protected void setFramerate(int framerate) {
            if (framerate < 1) framerate = 1;
            this.framerate = framerate;
            int delay = 1000 / framerate;
            setInitialDelay(0);
            setDelay(delay);
        }

        protected int getFramerate() {
            return framerate;
        }
    }

    protected class GamePanel extends JPanel {
        private static final long serialVersionUID = 1L;

        public void paintComponent(Graphics graphics) {
            mGraphics = (Graphics2D)graphics;
            mTransforms.clear();
            mTransforms.push(mGraphics.getTransform());
            mGraphics.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
            if (initialised) {
                GameEngine.this.paintComponent();
            }
        }
    }

    GameTimer timer = new GameTimer(180, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            double passedTime = measureTime();
            double dt = passedTime / 1000.;
            try {
                update(dt);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            mPanel.repaint();
        }
    });

    public void gameLoop(int framerate) {
        initialised = true;
        timer.setFramerate(framerate);
        timer.setRepeats(true);
        timer.start();
    }

    public void init() {}

    public abstract void update(double dt) throws InterruptedException;

    public abstract void paintComponent();

    public void keyPressed(KeyEvent event) {}

    public void keyReleased(KeyEvent event) {}

    public void keyTyped(KeyEvent event) {}

    public void mouseClicked(MouseEvent event) {}

    public void mousePressed(MouseEvent event) {}

    public void mouseReleased(MouseEvent event) {}

    public void mouseEntered(MouseEvent event) {}

    public void mouseExited(MouseEvent event) {}

    public void mouseMoved(MouseEvent event) {}

    public void mouseDragged(MouseEvent event) {}

    protected Color black = Color.BLACK;
    protected Color orange = Color.ORANGE;
    protected Color pink = Color.PINK;
    protected Color red = Color.RED;
    protected Color purple = new Color(128, 0, 128);
    protected Color blue = Color.BLUE;
    protected Color green = Color.GREEN;
    protected Color yellow = Color.YELLOW;
    protected Color white = Color.WHITE;

    public void changeBackgroundColor(Color c) {
        mGraphics.setBackground(c);
    }

    public void changeBackgroundColor(int red, int green, int blue) {
        if(red < 0)   {red = 0;}
        if(red > 255) {red = 255;}
        if(green < 0)   {green = 0;}
        if(green > 255) {green = 255;}
        if(blue < 0)   {blue = 0;}
        if(blue > 255) {blue = 255;}
        mGraphics.setBackground(new Color(red,green,blue));
    }

    public void clearBackground(int width, int height) {
        mGraphics.clearRect(0, 0, width, height);
    }

    public void changeColor(Color c) {
        mGraphics.setColor(c);
    }

    public void changeColor(int red, int green, int blue) {
        if(red < 0)   {red = 0;}
        if(red > 255) {red = 255;}
        if(green < 0)   {green = 0;}
        if(green > 255) {green = 255;}
        if(blue < 0)   {blue = 0;}
        if(blue > 255) {blue = 255;}
        mGraphics.setColor(new Color(red,green,blue));
    }

    void drawLine(double x1, double y1, double x2, double y2) {
        mGraphics.draw(new Line2D.Double(x1, y1, x2, y2));
    }

    void drawLine(double x1, double y1, double x2, double y2, double l) {
        mGraphics.setStroke(new BasicStroke((float)l));
        mGraphics.draw(new Line2D.Double(x1, y1, x2, y2));
        mGraphics.setStroke(new BasicStroke(1.0f));
    }

    void drawRectangle(double x, double y, double w, double h) {
        mGraphics.draw(new Rectangle2D.Double(x, y, w, h));
    }

    void drawRectangle(double x, double y, double w, double h, double l) {
        mGraphics.setStroke(new BasicStroke((float)l));
        mGraphics.draw(new Rectangle2D.Double(x, y, w, h));
        mGraphics.setStroke(new BasicStroke(1.0f));
    }

    protected void drawSolidRectangle(double x, double y, double w, double h) {
        mGraphics.fill(new Rectangle2D.Double(x, y, w, h));
    }

    void drawCircle(double x, double y, double radius) {
        mGraphics.draw(new Ellipse2D.Double(x-radius, y-radius, radius*2, radius*2));
    }

    void drawCircle(double x, double y, double radius, double l) {
        mGraphics.setStroke(new BasicStroke((float)l));
        mGraphics.draw(new Ellipse2D.Double(x-radius, y-radius, radius*2, radius*2));
        mGraphics.setStroke(new BasicStroke(1.0f));
    }

    void drawSolidCircle(double x, double y, double radius) {
        mGraphics.fill(new Ellipse2D.Double(x-radius, y-radius, radius*2, radius*2));
    }

    public void drawText(double x, double y, String s) {
        mGraphics.setFont(new Font("Arial", Font.PLAIN, 40));
        mGraphics.drawString(s, (int)x, (int)y);
    }

    public void drawBoldText(double x, double y, String s) {
        mGraphics.setFont(new Font("Arial", Font.BOLD, 40));
        mGraphics.drawString(s, (int)x, (int)y);
    }

    public void drawText(double x, double y, String s, String font, int size) {
        mGraphics.setFont(new Font(font, Font.PLAIN, size));
        mGraphics.drawString(s, (int)x, (int)y);
    }

    public void drawBoldText(double x, double y, String s, String font, int size) {
        mGraphics.setFont(new Font(font, Font.BOLD, size));
        mGraphics.drawString(s, (int)x, (int)y);
    }

    public static Image loadImage(String filename) {
        try {
            Image image = ImageIO.read(new File(filename));
            return image;
        } catch (IOException e) {
            System.out.println("Error: could not load image " + filename);
            System.exit(1);
        }
        return null;
    }

    public Image loadRotatedImage(String filename, double angleDegrees) {
        try {
            BufferedImage source = ImageIO.read(new File(filename));
            if (source == null) {
                System.out.println("Error: could not load image " + filename);
                return null;
            }

            // 计算旋转后图像的尺寸
            double radians = Math.toRadians(angleDegrees);
            double sin = Math.abs(Math.sin(radians));
            double cos = Math.abs(Math.cos(radians));
            int newWidth = (int) Math.floor(source.getWidth() * cos + source.getHeight() * sin);
            int newHeight = (int) Math.floor(source.getHeight() * cos + source.getWidth() * sin);

            // 创建新的 BufferedImage 用于存储旋转后的图片
            BufferedImage rotated = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = rotated.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


            AffineTransform transform = new AffineTransform();
            transform.translate(newWidth / 2.0, newHeight / 2.0);
            transform.rotate(radians);
            transform.translate(-source.getWidth() / 2.0, -source.getHeight() / 2.0);


            g2d.setTransform(transform);
            g2d.drawImage(source, 0, 0, null);
            g2d.dispose();

            return rotated;
        } catch (IOException e) {
            System.out.println("Error: could not load image " + filename);
            e.printStackTrace();
            return null;
        }
    }

    public Image subImage(Image source, int x, int y, int w, int h) {
        if(source == null) {
            System.out.println("Error: cannot extract a subImage from a null image.\n");
            return null;
        }
        BufferedImage buffered = (BufferedImage)source;
        Image image = buffered.getSubimage(x, y, w, h);
        return image;
    }

    public void drawImage(Image image, double x, double y) {
        if(image == null) {
            System.out.println("Error: cannot draw null image.\n");
            return;
        }
        mGraphics.drawImage(image, (int)x, (int)y, null);
    }

    public void drawImage(Image image, double x, double y, double w, double h) {
        if(image == null) {
            System.out.println("Error: cannot draw null image.\n");
            return;
        }
        mGraphics.drawImage(image, (int)x, (int)y, (int)w, (int)h, null);
    }

    Stack<AffineTransform> mTransforms;

    public void saveCurrentTransform() {
        mTransforms.push(mGraphics.getTransform());
    }

    public void restoreLastTransform() {
        mGraphics.setTransform(mTransforms.peek());
        if(mTransforms.size() > 1) {
            mTransforms.pop();
        }
    }

    public void translate(double x, double y) {
        mGraphics.translate(x,y);
    }

    public void rotate(double a) {
        mGraphics.rotate(Math.toRadians(a));
    }

    public void scale(double x, double y) {
        mGraphics.scale(x, y);
    }

    void shear(double x, double y) {
        mGraphics.shear(x, y);
    }

    public class AudioClip {
        AudioFormat mFormat;
        byte[] mData;
        long mLength;
        Clip mLoopClip;

        public Clip getLoopClip() {
            return mLoopClip;
        }

        public void setLoopClip(Clip clip) {
            mLoopClip = clip;
        }

        public AudioFormat getAudioFormat() {
            return mFormat;
        }

        public byte[] getData() {
            return mData;
        }

        public long getBufferSize() {
            return mLength;
        }

        public AudioClip(AudioInputStream stream) {
            mFormat = stream.getFormat();
            mLength = stream.getFrameLength() * mFormat.getFrameSize();
            mData = new byte[(int)mLength];
            try {
                stream.read(mData);
            } catch(Exception exception) {
                System.out.println("Error reading Audio File\n");
                System.exit(1);
            }
            mLoopClip = null;
        }
    }

    public AudioClip loadAudio(String filename) {
        try {
            File file = new File(filename);
            AudioInputStream in = AudioSystem.getAudioInputStream(file);
            AudioFormat baseFormat = in.getFormat();
            AudioFormat decodedFormat = new AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED,
                    baseFormat.getSampleRate(),
                    16,
                    baseFormat.getChannels(),
                    baseFormat.getChannels() * 2,
                    baseFormat.getSampleRate(),
                    false
            );
            AudioInputStream pcmStream = AudioSystem.getAudioInputStream(decodedFormat, in);
            return new AudioClip(pcmStream);
        } catch (Exception e) {
            System.err.println("Failed to load audio file: " + filename);
            e.printStackTrace();
            return null;
        }
    }

    public void playAudio(AudioClip audioClip) {
        if(audioClip == null) {
            System.out.println("Error: audioClip is null\n");
            return;
        }
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(audioClip.getAudioFormat(), audioClip.getData(), 0, (int)audioClip.getBufferSize());
            clip.start();
        } catch(Exception exception) {
            System.out.println("Error playing Audio Clip\n");
        }
    }

    public void playAudio(AudioClip audioClip, float volume) {
        if(audioClip == null) {
            System.out.println("Error: audioClip is null\n");
            return;
        }
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(audioClip.getAudioFormat(), audioClip.getData(), 0, (int)audioClip.getBufferSize());
            FloatControl control = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
            control.setValue(volume);
            clip.start();
        } catch(Exception exception) {
            System.out.println("Error: could not play Audio Clip\n");
        }
    }

    public void startAudioLoop(AudioClip audioClip) {
        if(audioClip == null) {
            System.out.println("Error: audioClip is null\n");
            return;
        }
        Clip clip = audioClip.getLoopClip();
        if(clip == null) {
            try {
                clip = AudioSystem.getClip();
                clip.open(audioClip.getAudioFormat(), audioClip.getData(), 0, (int)audioClip.getBufferSize());
                clip.loop(Clip.LOOP_CONTINUOUSLY);
                audioClip.setLoopClip(clip);
            } catch(Exception exception) {
                System.out.println("Error: could not play Audio Clip\n");
            }
        }
        clip.setFramePosition(0);
        clip.start();
    }

    public void startAudioLoop(AudioClip audioClip, float volume) {
        if(audioClip == null) {
            System.out.println("Error: audioClip is null\n");
            return;
        }
        Clip clip = audioClip.getLoopClip();
        if(clip == null) {
            try {
                clip = AudioSystem.getClip();
                clip.open(audioClip.getAudioFormat(), audioClip.getData(), 0, (int)audioClip.getBufferSize());
                FloatControl control = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
                control.setValue(volume);
                clip.loop(Clip.LOOP_CONTINUOUSLY);
                audioClip.setLoopClip(clip);
            } catch(Exception exception) {
                System.out.println("Error: could not play Audio Clip\n");
            }
        }
        clip.setFramePosition(0);
        clip.start();
    }

    public void stopAudioLoop(AudioClip audioClip) {
        Clip clip = audioClip.getLoopClip();
        if(clip != null){
            clip.stop();
        }
    }

    Random mRandom = null;

    public int rand(int max) {
        if(mRandom == null) {
            mRandom = new Random();
        }
        double d = mRandom.nextDouble();
        return (int)(d*max);
    }

    public float rand(float max) {
        if(mRandom == null) {
            mRandom = new Random();
        }
        float d = mRandom.nextFloat();
        return d*max;
    }

    public double rand(double max) {
        if(mRandom == null) {
            mRandom = new Random();
        }
        double value = mRandom.nextDouble();
        return value*max;
    }

    public Image flipImage(Image original, boolean horizontal) {
        if (!(original instanceof BufferedImage)) {
            throw new IllegalArgumentException("Image must be a BufferedImage");
        }

        BufferedImage bufOrig = (BufferedImage) original;
        int width = bufOrig.getWidth();
        int height = bufOrig.getHeight();

        AffineTransform tx = new AffineTransform();
        if (horizontal) {

            tx.translate(width, 0);
            tx.scale(-1, 1);
        } else {

            tx.translate(0, height);
            tx.scale(1, -1);
        }

        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
        return op.filter(bufOrig, null);
    }

    public int floor(double value) {
        return (int)Math.floor(value);
    }

    public int ceil(double value){
        return (int)Math.ceil(value);
    }

    public int round(double value) {
        return (int)Math.round(value);
    }

    public double sqrt(double value) {
        return Math.sqrt(value);
    }

    public double length(double x, double y) {
        return Math.sqrt(x*x + y*y);
    }

    public double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2-x1, 2) + Math.pow(y2-y1, 2));
    }

    public double toDegrees(double radians) {
        return Math.toDegrees(radians);
    }

    public double toRadians(double degrees) {
        return Math.toRadians(degrees);
    }

    public int abs(int value) {
        return Math.abs(value);
    }

    public float abs(float value) {
        return Math.abs(value);
    }

    public double abs(double value) {
        return Math.abs(value);
    }

    public double cos(double value) {
        return Math.cos(Math.toRadians(value));
    }

    public double acos(double value) {
        return Math.toDegrees(Math.acos(value));
    }

    public double sin(double value) {
        return Math.sin(Math.toRadians(value));
    }

    public double asin(double value) {
        return Math.toDegrees(Math.asin(value));
    }

    public double tan(double value) {
        return Math.tan(Math.toRadians(value));
    }

    public double atan(double value) {
        return Math.toDegrees(Math.atan(value));
    }

    public double atan2(double x, double y) {
        return Math.toDegrees(Math.atan2(x,y));
    }
}