import util.FileUtil;

import java.io.File;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {

        System.out.println("Enter the path of folder, please:");
        Scanner scanner = new Scanner(System.in);
        String folderPath = scanner.nextLine();
        while (!FileUtil.isFolderPathValid(folderPath)) {
            System.out.println("Enter the path of folder, please:");
            folderPath = scanner.nextLine();
        }
        List<File> textFiles = FileUtil.getTextFilesInDirectory(folderPath);
        SearchEngine searchEngine = new SearchEngine(textFiles, FileUtil.isAnyLargeFileInDirectory(folderPath));

        System.out.println("Enter your query, please:");
        String inputQuery = scanner.nextLine();

        while (!inputQuery.equals("-1")) {
            searchEngine.search(inputQuery);
            System.out.println("Enter your query, please:");
            inputQuery = scanner.nextLine();
        }

    }
}
