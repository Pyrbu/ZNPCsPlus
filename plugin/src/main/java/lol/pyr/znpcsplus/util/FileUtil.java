package lol.pyr.znpcsplus.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class FileUtil {
    public static String dumpReaderAsString(Reader reader) {
        BufferedReader bReader = new BufferedReader(reader);
        try {
            StringBuilder sb = new StringBuilder();
            String line = bReader.readLine();
            while (true) {
                sb.append(line);
                line = bReader.readLine();
                if (line == null) break;
                sb.append("\n");
            }
            return sb.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
