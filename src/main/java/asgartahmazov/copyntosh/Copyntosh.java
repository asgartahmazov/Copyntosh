package asgartahmazov.copyntosh;

import asgartahmazov.copyntosh.component.ClipboardReader;
import asgartahmazov.copyntosh.component.ClipboardWriter;
import asgartahmazov.copyntosh.controller.ClipboardController;
import asgartahmazov.copyntosh.service.ClipboardService;
import asgartahmazov.copyntosh.view.CopyntoshView;

import java.awt.*;
import java.awt.datatransfer.Clipboard;

public class Copyntosh {

    public static void main(String[] args) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        ClipboardReader clipboardReader = new ClipboardReader(clipboard);
        ClipboardWriter clipboardWriter = new ClipboardWriter(clipboard);
        ClipboardService clipboardService = new ClipboardService(clipboardReader, clipboardWriter);
        ClipboardController clipboardController = new ClipboardController(clipboardService);
        new CopyntoshView(clipboardController);
    }
}
