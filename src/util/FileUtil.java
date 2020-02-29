package util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
    public static List<File> getTextFilesInDirectory(String path) {
        List<File> files = new ArrayList<>();
        File folder;
        try {
            folder = new File(path);
            if(folder.isDirectory()) {
                File[] listOfFiles = folder.listFiles();
                for (File file : listOfFiles) {
                    if (file.isFile() && file.getName().endsWith(".txt")) {
                        files.add(file);
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        if (files.size()==0) {
            System.out.println("There is no file");
        }
        return files;
    }

    public static boolean isFolderPathValid(String path) {
        if (path == null | !new File(path).isDirectory()) {
            System.out.println("Please enter a valid path.\nYou don't enter a valid path or the path is not directory");
            return false;
        } else if(new File(path).listFiles().length==0) {
            System.out.println("There is no file in the directory");
            return false;
        }
            return true;
    }

    //This method check if there is any large file with the size of more than 2Gb
    public static boolean isAnyLargeFileInDirectory(String path) {
        boolean hasLargeFile = false;
        File folder;
        try {
            folder = new File(path);
            if(folder.isDirectory()) {
                File[] listOfFiles = folder.listFiles();
                for (File file : listOfFiles) {
                    if (file.isFile() && file.getName().endsWith(".txt") && file.length()>= 2000000000) {
                        hasLargeFile = true;
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return hasLargeFile;
    }

}
