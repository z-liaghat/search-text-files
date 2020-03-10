import util.FileUtil;

import java.io.File;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        String folderPath = getUserFolderPath();
        List<File> textFiles = FileUtil.getTextFilesInDirectory(folderPath);
        while (textFiles.size() == 0) {
            System.out.println("Try again.");
            folderPath = getUserFolderPath();
            textFiles = FileUtil.getTextFilesInDirectory(folderPath);
        }

        QueryGenerator queryGenerator = new QueryGenerator();
        SearchEngine searchEngine = new SearchEngine(textFiles, FileUtil.isAnyLargeFileInDirectory(folderPath), queryGenerator);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your query, please:");
        String inputQuery = scanner.nextLine();

        while (!inputQuery.equals("-1")) {
            searchEngine.search(inputQuery);
            System.out.println("Enter your query, please:");
            inputQuery = scanner.nextLine();
        }
    }

    private static String getUserFolderPath() {
        System.out.println("Enter the path of folder, please:");
        Scanner scanner = new Scanner(System.in);
        String folderPath = scanner.nextLine();
        while (!FileUtil.isFolderPathValid(folderPath)) {
            System.out.println("Enter the path of folder, please:");
            folderPath = scanner.nextLine();
        }
        return folderPath;
    }
}
