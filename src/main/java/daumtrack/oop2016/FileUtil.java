package daumtrack.oop2016;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Cheechyo on 2016. 9. 30..
 */
public class FileUtil {
    public static Set<File> listFiles(File path) {
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
