package daumtrack.oop2016;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author mitchell.geek
 */
public class FileWatcher {
    private static final long CHECK_INTERVAL = 1000L;


    public static void main(String[] args) throws InterruptedException {
        if (args.length != 1) {
            System.out.println("Usage : fileWatcher PATH");
            System.exit(1);
        }
        File pathToWatch = new File(args[0]);
        if (!pathToWatch.exists()) {
            System.out.println(pathToWatch + " is NOT exist");
        }

        System.out.println("Watching : " + pathToWatch);

        while(true) {
            long lastChecked = System.currentTimeMillis();
            Thread.sleep(CHECK_INTERVAL);
            if (!pathToWatch.exists()) {
                System.out.println("DELETED!");
                break;
            }

            Set<File> fileSet = listFiles(pathToWatch);
            for (File file : fileSet) {
                if (file.lastModified() > lastChecked) {
                    System.out.println("MODIFIED: " + file);
                }
            }
        }
    }

    private static Set<File> listFiles(File path) {
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
