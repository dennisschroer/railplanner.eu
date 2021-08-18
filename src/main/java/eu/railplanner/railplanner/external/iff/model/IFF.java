package eu.railplanner.railplanner.external.iff.model;

import eu.railplanner.railplanner.external.iff.parser.FootnoteParser;
import eu.railplanner.railplanner.external.iff.parser.IFFParser;
import eu.railplanner.railplanner.external.iff.parser.TimetableParser;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.util.Assert;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

@Data
@RequiredArgsConstructor
public class IFF {
    public static final Charset IFF_CHARSET = StandardCharsets.ISO_8859_1;
    public static final String LINE_TERMINATION = "\n\r";

    private final Path path;

    private Delivery delivery;

    private Timetable timetable;

    private Footnote footnote;
}
