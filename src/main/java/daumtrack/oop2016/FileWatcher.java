package daumtrack.oop2016;

import com.sun.tools.javac.code.Attribute;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

/**
 * @author mitchell.geek
 */
public class FileWatcher {
    private static final long CHECK_INTERVAL = 1000L;
    private static final Logger logger = new Logger(System.out);
    private File pathToWatch;
    private OnDeletedEventListener deletedEventListener = null;
    private OnModifiedEventListener modifiedEventListener = null;
    private Thread watchThread = null;

    interface OnDeletedEventListener{
        void onDeleted();
    }
    interface OnModifiedEventListener{
        void onModified(File f);
    }

    public static void main(String[] args) throws InterruptedException {
        if (args.length != 1) {
            logger.log("Usage : fileWatcher PATH");
            System.exit(1);
        }
        (new FileWatcher())
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

    private void watch(final String path) throws InterruptedException {
        watchThread = new Thread(new Runnable() {
            public void run() {
                pathToWatch = new File(path);
                if (!pathToWatch.exists()) {
                    logger.log(pathToWatch + " is NOT exist");
                }
                logger.log("Watching : " + pathToWatch);
                while(true) {
                    long lastChecked = System.currentTimeMillis();
                    try {
                        Thread.sleep(CHECK_INTERVAL);
                    } catch (InterruptedException e){
                    }
                    if (!pathToWatch.exists()) {
                        if (FileWatcher.this.deletedEventListener != null){
                            deletedEventListener.onDeleted();
                        }
                        break;
                    }

                    Set<File> fileSet = FileUtil.listFiles(pathToWatch);
                    for (File file : fileSet) {
                        if (file.lastModified() > lastChecked) {
                            if (FileWatcher.this.modifiedEventListener != null){
                                modifiedEventListener.onModified(file);
                            }

                        }
                    }
                }
            }
        });
    }

    FileWatcher deleted(OnDeletedEventListener listener){
        this.deletedEventListener = listener;
        return this;
    }
    FileWatcher modified(OnModifiedEventListener listener){
        this.modifiedEventListener = listener;
        return this;
    }
}
