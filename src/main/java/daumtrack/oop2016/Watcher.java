package daumtrack.oop2016;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Cheechyo on 2016. 9. 30..
 */
public class Watcher {
    private static final long CHECK_INTERVAL = 1000L;
    private static final Logger logger = new Logger(System.out);

    private OnDeletedEventListener deletedEventListener = null;
    private OnModifiedEventListener modifiedEventListener = null;
    private Thread watchThread = null;

    interface OnDeletedEventListener{
        void onDeleted();
    }
    interface OnModifiedEventListener{
        void onModified(File f);
    }
    public void watch(final String path) throws InterruptedException {
        watchThread = new Thread(new Runnable() {
            public void run() {
                File pathToWatch = new File(path);
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

                    if (isDeleted(pathToWatch)) {
                        if (Watcher.this.deletedEventListener != null){
                            deletedEventListener.onDeleted();
                        }
                        break;
                    }

                    Set<File> modified = getModified(pathToWatch, lastChecked);
                    for (File f : modified) {
                        if (Watcher.this.modifiedEventListener != null){
                            modifiedEventListener.onModified(f);
                        }
                    }
                }
            }
        });
    }
    private boolean isDeleted(File pathToWatch) {
        return !pathToWatch.exists();

    }
    private Set<File> getModified(File pathToWatch, long lastChecked) {
        Set<File> fileSet = FileUtil.listFiles(pathToWatch);
        Set<File> modified = new HashSet<File>();
        for (File file : fileSet) {
            if (file.lastModified() > lastChecked) {
                modified.add(file);
            }
        }
        return modified;
    }

    Watcher deleted(Watcher.OnDeletedEventListener listener){
        this.deletedEventListener = listener;
        return this;
    }
    Watcher modified(Watcher.OnModifiedEventListener listener){
        this.modifiedEventListener = listener;
        return this;
    }
}
