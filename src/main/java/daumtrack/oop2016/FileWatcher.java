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
    private OnModefiedEventListener modefiedEventListener = null;
    interface OnDeletedEventListener{
        void onDeleted();
    }
    interface OnModefiedEventListener{
        void onModefied(File f);
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
                .modeifed(new OnModefiedEventListener() {
                    public void onModefied(File f) {
                        logger.log("MODIFIED: " + f);
                    }
                })
                .watch(args[0]);
    }

    private void watch(String path) throws InterruptedException {
        pathToWatch = new File(path);
        if (!pathToWatch.exists()) {
            logger.log(pathToWatch + " is NOT exist");
        }
        logger.log("Watching : " + pathToWatch);
        while(true) {
            long lastChecked = System.currentTimeMillis();
            Thread.sleep(CHECK_INTERVAL);
            if (!pathToWatch.exists()) {
                if (this.deletedEventListener != null){
                    deletedEventListener.onDeleted();
                }
                break;
            }

            Set<File> fileSet = listFiles(pathToWatch);
            for (File file : fileSet) {
                if (file.lastModified() > lastChecked) {
                    if (this.modefiedEventListener != null){
                        modefiedEventListener.onModefied(file);
                    }

                }
            }
        }
    }

    FileWatcher deleted(OnDeletedEventListener listener){
        this.deletedEventListener = listener;
        return this;
    }
    FileWatcher modeifed(OnModefiedEventListener listener){
        this.modefiedEventListener = listener;
        return this;
    }
    private Set<File> listFiles(File path) {
        Set<File> files = new HashSet<File>();
        files.add(path);
        if (path.isDirectory()) {
            for (File aFile : path.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return !name.endsWith(File.separator + "..") && !name.endsWith(File.separator + ".");
                }
            })) {
                files.addAll(listFiles(aFile));
            }
        }
        return files;
    }
}
