package asgartahmazov.copyntosh.view;

import asgartahmazov.copyntosh.controller.ClipboardController;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.Deque;
import java.util.LinkedList;

public class CopyntoshView {

    public static final String TRAY_ICON_IMAGE_PATH = "icon.png";
    public static final String TITLE = "Copyntosh";
    public static final int MAIN_FRAME_WIDTH = 512;
    public static final int MAIN_FRAME_HEIGHT = 768;
    public static final int COPY_LIMIT = 32;
    public static final String OPEN_MENU_ITEM_TEXT = "Open";
    public static final String HIDE_MENU_ITEM_TEXT = "Hide";
    public static final String EXIT_MENU_ITEM_TEXT = "Exit";

    private final ClipboardController clipboardController;
    private final Deque<String> strings = new LinkedList<>();

    private TrayIcon trayIcon;
    private JFrame mainFrame;
    private JPanel mainPanel;
    private JPanel clipboardPanel;
    private JScrollPane clipboardScrollPane;

    public CopyntoshView(ClipboardController clipboardController) {
        this.clipboardController = clipboardController;
        initializeView();
        initializeActionListeners();
        startClipboardReader();
    }

    private void initializeView() {
        if (SystemTray.isSupported()) {
            initializeTrayIcon();
        }
        initializeClipboardPanel();
        initializeClipboardScrollPane();
        initializeMainPanel();
        initializeMainFrame();
    }

    private void initializeActionListeners() {
        if (SystemTray.isSupported()) {
            initializeTrayIconActionListener();
        }
        initializeMainFrameActionListener();
    }

    private void startClipboardReader() {
        while (true) {
            try {
                String string = clipboardController.readFromClipboard();
                if (string == null || string.isEmpty() || strings.contains(string)) {
                    continue;
                }
                if (strings.size() > COPY_LIMIT) {
                    strings.removeLast();
                }

                strings.push(string);

                clipboardPanel.removeAll();
                strings.forEach(s -> {
                    JTextArea jTextArea = new JTextArea(s);
                    jTextArea.setEditable(false);
                    jTextArea.setLineWrap(true);
                    jTextArea.setWrapStyleWord(true);
                    jTextArea.setBackground(new Color(200, 200, 200));
                    jTextArea.setBorder(BorderFactory.createDashedBorder(null));
                    jTextArea.setMaximumSize(new Dimension(MAIN_FRAME_WIDTH, MAIN_FRAME_HEIGHT / 4));
                    jTextArea.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            jTextArea.select(0, jTextArea.getSelectionEnd());
                            jTextArea.setSelectionColor(Color.black);
                            jTextArea.setSelectedTextColor(Color.white);
                            clipboardController.writeToClipboard(jTextArea.getText());
                        }
                    });
                    clipboardPanel.add(jTextArea);
                    clipboardPanel.add(Box.createRigidArea(new Dimension(0, 8)));
                });
                clipboardScrollPane.revalidate();

                Thread.sleep(500);
            } catch (InterruptedException ignored) {
            }
        }
    }

    private void initializeTrayIcon() {
        SystemTray systemTray = SystemTray.getSystemTray();
        InputStream trayIconImageStream = getClass()
                .getClassLoader()
                .getResourceAsStream(TRAY_ICON_IMAGE_PATH);
        if (trayIconImageStream == null) {
            return;
        }
        try {
            Image trayIconImage = ImageIO.read(trayIconImageStream);
            trayIcon = new TrayIcon(trayIconImage, TITLE);
            trayIcon.setImageAutoSize(true);
            MenuItem openMenuItem = new MenuItem(OPEN_MENU_ITEM_TEXT);
            MenuItem hideMenuItem = new MenuItem(HIDE_MENU_ITEM_TEXT);
            MenuItem exitMenuItem = new MenuItem(EXIT_MENU_ITEM_TEXT);
            PopupMenu popupMenu = new PopupMenu();
            popupMenu.add(openMenuItem);
            popupMenu.add(hideMenuItem);
            popupMenu.add(exitMenuItem);
            trayIcon.setPopupMenu(popupMenu);
            systemTray.add(trayIcon);
        } catch (AWTException | IOException ignored) {
        }
    }

    private void initializeClipboardPanel() {
        clipboardPanel = new JPanel();
        clipboardPanel.setLayout(new BoxLayout(clipboardPanel, BoxLayout.Y_AXIS));
    }

    private void initializeClipboardScrollPane() {
        clipboardScrollPane = new JScrollPane(clipboardPanel);
    }

    private void initializeMainPanel() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(clipboardScrollPane, BorderLayout.CENTER);
    }

    private void initializeMainFrame() {
        mainFrame = new JFrame(TITLE);
        mainFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        mainFrame.setSize(MAIN_FRAME_WIDTH, MAIN_FRAME_HEIGHT);
        mainFrame.setResizable(false);
        mainFrame.setAlwaysOnTop(true);
        mainFrame.add(mainPanel);
        mainFrame.setVisible(true);
    }

    private void initializeTrayIconActionListener() {
        trayIcon.getPopupMenu().getItem(0).addActionListener(e -> {
            mainFrame.requestFocus();
            mainFrame.setVisible(true);
        });
        trayIcon.getPopupMenu().getItem(1).addActionListener(e -> mainFrame.setVisible(false));
        trayIcon.getPopupMenu().getItem(2).addActionListener(e -> System.exit(0));
        trayIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!mainFrame.isVisible()) {
                    mainFrame.requestFocus();
                    mainFrame.setVisible(true);
                } else {
                    mainFrame.setVisible(false);
                }
            }
        });
    }

    private void initializeMainFrameActionListener() {
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowDeactivated(WindowEvent e) {
                mainFrame.setVisible(false);
            }
        });
    }
}
