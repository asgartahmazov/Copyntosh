package asgartahmazov.copyntosh.service;

import asgartahmazov.copyntosh.component.ClipboardReader;
import asgartahmazov.copyntosh.component.ClipboardWriter;

public class ClipboardService {

    private final ClipboardReader clipboardReader;
    private final ClipboardWriter clipboardWriter;

    public ClipboardService(ClipboardReader clipboardReader, ClipboardWriter clipboardWriter) {
        this.clipboardReader = clipboardReader;
        this.clipboardWriter = clipboardWriter;
    }

    public String readFromClipboard() {
        String string = null;
        try {
            string = clipboardReader.readFromClipboard();
        } catch (Exception ignored) {
        }
        return string;
    }

    public void writeToClipboard(String string) {
        clipboardWriter.writeToClipboard(string);
    }
}
