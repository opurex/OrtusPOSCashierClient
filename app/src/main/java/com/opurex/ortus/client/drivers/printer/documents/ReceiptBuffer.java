package com.opurex.ortus.client.drivers.printer.documents;

import java.util.ArrayList;
import java.util.List;

public class ReceiptBuffer {

    private final List<String> lines = new ArrayList<>();

    public void add(String line) {
        lines.add(line);
    }

    public void addEmpty() {
        lines.add("");
    }

    public List<String> getLines() {
        return lines;
    }
}
