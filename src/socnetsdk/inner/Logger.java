package socnetsdk.inner;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import socnetsdk.Application;

/**
 *
 * @author asus
 */
public class Logger {

    static String filename = "error.log";

    private static String generateDateNow() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String tanggal = formatter.format(date);
        return tanggal;
    }

    private static void createAppPath() {
        File lokasi = new File(Application.Path());
        if (!lokasi.exists()) {
            lokasi.mkdirs();
        }
    }

    public static void writeError(String message) {
        try {
            // check whether the path is exist or not?
            // if so then create first
            createAppPath();
            
            // then we do the work for
            // appending new data
            BufferedWriter penulis = new BufferedWriter(new FileWriter(Application.Path(filename), true));

            penulis.write(generateDateNow() + " - " + message);
            penulis.newLine();

            penulis.close();
        } catch (Exception ex) {
            // we will leave it here empty
        }
    }
}
