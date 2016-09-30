package daumtrack.oop2016;

import daumtrack.oop2016.Watcher.OnDeletedEventListener;
import daumtrack.oop2016.Watcher.OnModifiedEventListener;

import java.io.File;

/**
 * @author mitchell.geek
 */
public class FileWatcher {
    private static final Logger logger = new Logger(System.out);
    public static void main(String[] args) throws InterruptedException {
        if (args.length != 1) {
            logger.log("Usage : fileWatcher PATH");
            System.exit(1);
        }
        (new Watcher())
                .deleted(new OnDeletedEventListener() {
                    public void onDeleted() {
                        logger.log("DELETED!");
                    }
                })
                .modified(new OnModifiedEventListener() {
                    public void onModified(File f) {
                        logger.log("MODIFIED: " + f);
                    }
                })
                .watch(args[0]);
        while(true);
    }
}
