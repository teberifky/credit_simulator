package com.credit.simulator.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayDeque;
import java.util.Deque;

public class LineSource {
    private record Source(BufferedReader reader, boolean echo) {}

    private final Deque<Source> sources = new ArrayDeque<>();
    private boolean lastLineEchoed;

    public LineSource addSource(BufferedReader reader, boolean echo) {
        sources.addLast(new Source(reader, echo));
        return this;
    }

    public String readLine() {
        while (!sources.isEmpty()) {
            Source source = sources.peekFirst();
            try {
                String line = source.reader().readLine();
                if (line != null) {
                    lastLineEchoed = source.echo();
                    return line;
                }
                sources.removeFirst();
                if (source.echo()) {
                    source.reader().close();
                }
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
        return null;
    }

    public boolean lastLineEchoed() {
        return lastLineEchoed;
    }
}
