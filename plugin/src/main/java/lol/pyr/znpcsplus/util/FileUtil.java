package lol.pyr.znpcsplus.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
    public static String dumpReaderAsString(Reader reader) {
        BufferedReader bReader = new BufferedReader(reader);
        try {
            List<String> lines = new ArrayList<>();
            String line;
            while ((line = bReader.readLine()) != null) {
                lines.add(line);
            }
            return String.join("\n", lines);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
