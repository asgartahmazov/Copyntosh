package asgartahmazov.copyntosh.component;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class ClipboardWriter {

    private final Clipboard clipboard;

    public ClipboardWriter(Clipboard clipboard) {
        this.clipboard = clipboard;
    }

    public void writeToClipboard(String string) {
        StringSelection stringSelection = new StringSelection(string);
        clipboard.setContents(stringSelection, null);
    }
}
