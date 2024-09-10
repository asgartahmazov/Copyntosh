package asgartahmazov.copyntosh.component;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class ClipboardReader {

    private final Clipboard clipboard;

    public ClipboardReader(Clipboard clipboard) {
        this.clipboard = clipboard;
    }

    public String readFromClipboard() throws UnsupportedFlavorException, IOException {
        Transferable transferable = clipboard.getContents(null);
        if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            return (String) transferable.getTransferData(DataFlavor.stringFlavor);
        }
        return null;
    }
}
