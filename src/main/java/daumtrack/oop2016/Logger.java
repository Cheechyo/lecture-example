package daumtrack.oop2016;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Created by Cheechyo on 2016. 9. 30..
 */
public class Logger {
    private static PrintStream stream = null;
    Logger(PrintStream stream){
        this.stream = stream;
    }
    public void log(CharSequence str){
        if (stream != null){
            stream.println(str);
        }
    }
}
