package io.reqover;

import io.reqover.core.ReqoverResultsWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;

public class Reqover {

    public static final String REQOVER_ROOT = ".reqover";

    public static void dumpSpec(String url) {
        try {
            ReqoverResultsWriter.createDirectories(Path.of(REQOVER_ROOT));
            download(url, REQOVER_ROOT + File.separator + "spec.yml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void download(String urlStr, String file) throws IOException {
        URL url = new URL(urlStr);
        ReadableByteChannel rbc = Channels.newChannel(url.openStream());
        FileOutputStream fos = new FileOutputStream(file);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        fos.close();
        rbc.close();
    }

}
