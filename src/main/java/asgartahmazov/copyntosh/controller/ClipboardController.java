package asgartahmazov.copyntosh.controller;

import asgartahmazov.copyntosh.service.ClipboardService;

public class ClipboardController {

    private final ClipboardService clipboardService;

    public ClipboardController(ClipboardService clipboardService) {
        this.clipboardService = clipboardService;
    }

    public String readFromClipboard() {
        return clipboardService.readFromClipboard();
    }

    public void writeToClipboard(String string) {
        clipboardService.writeToClipboard(string);
    }
}
