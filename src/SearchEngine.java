import util.ShowResult;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchEngine {
    //This list is used to store the name of file which contains the searched query
    private List<String> satisfiedFileNames;

    //The list of all files in the directory
    private List<File> fileList;

    private boolean containLargeFile;

    private QueryGenerator queryGenerator;

    public SearchEngine(List<File> files, boolean isContainLargeFile, QueryGenerator queryGenerator) {
        satisfiedFileNames = new ArrayList<>();
        fileList = files;
        containLargeFile = isContainLargeFile;
        this.queryGenerator = queryGenerator;
    }

    public void search(String query) {
        if (fileList != null) {
            String regexQuery = queryGenerator.convertQueryToRegex(query);
            if (!containLargeFile) {
                for (int i = 0; i < fileList.size(); i++) {
                    if (searchByRegex(regexQuery, fileList.get(i).getPath())) {
                        satisfiedFileNames.add(fileList.get(i).getName());
                    }
                }
            } else {
                for (int i = 0; i < fileList.size(); i++) {
                    if (fileList.get(i).length() <= 2000000000) {
                        if (searchByRegex(regexQuery, fileList.get(i).getPath())) {
                            satisfiedFileNames.add(fileList.get(i).getName());
                        }
                    } else {
                        if (searchQueryLineByLine(query, fileList.get(i))) {
                            satisfiedFileNames.add(fileList.get(i).getName());
                        }
                    }
                }
            }
        }
        //Print file names of result
        ShowResult.ShowListOfFileName(satisfiedFileNames);
        resetSatisfiedFileNameList();
    }

    private void resetSatisfiedFileNameList() {
        satisfiedFileNames.clear();
    }

    public boolean searchByRegex(String regexQuery, String filePath) {
        boolean isFileContainsQuery = false;
        // It contains all of the lines in the file
        String line;
        StringBuilder fileContent = new StringBuilder();
        Pattern pattern = Pattern.compile(regexQuery);

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
            // This loop append each line to the "fileContent" string
            //Inorder to be case sensitive,  all text is converted to lowercase
            while ((line = bufferedReader.readLine()) != null) {
                fileContent.append(line.toLowerCase());
            }
            Matcher matcher = pattern.matcher(fileContent);
            isFileContainsQuery = matcher.matches();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return isFileContainsQuery;
    }

    //This method is use for huge files (more than 2 Gb). It reads files line by line
    private boolean searchQueryLineByLine(String query, File file) {
        String regexQuery;
        Pattern pattern = null;
        Matcher matcher;
        boolean isFileContainsQuery = false;
        AndPhrase andPhrase = null;
        OrPhrase orPhrase = null;

        //preparing query to check the file
        if (query.contains(Operators.AND.name()) && query.contains(Operators.OR.name())) {
            orPhrase = new OrPhrase(query);
        } else if (query.contains(Operators.AND.name())) {
            andPhrase = new AndPhrase(query);
        } else if (query.contains(Operators.OR.name())) {
            regexQuery = query.replaceAll(Operators.OR.name(), "|")
                    .replaceAll(" ", ".*");
            regexQuery = regexQuery.toLowerCase();
            pattern = Pattern.compile(regexQuery);
        }
        Scanner scanner = null;
        try {
             scanner = new Scanner(file);
            String line;
            //reads each line of the input file and check if the query is satisfied or not
            if (query.contains(Operators.AND.name()) && query.contains(Operators.OR.name())) {
                while (scanner.hasNextLine()) {
                    line = scanner.nextLine().toLowerCase();
                    //If the value of this or phrase set to true, reading from file will stop.
                    if (orPhrase.setPhraseValues(line))
                        break;
                }
                isFileContainsQuery = orPhrase.evaluate();

            } else if (query.contains(Operators.AND.name())) {
                while (scanner.hasNextLine()) {
                    line = scanner.nextLine().toLowerCase();
                    //If the value of this and phrase set to true, reading from file will stop.
                    if (andPhrase.setPhraseValues(line))
                        break;
                }
                isFileContainsQuery = andPhrase.evaluate();

            } else if (query.contains(Operators.OR.name())) {

                while (scanner.hasNextLine()) {
                    //process each line
                    line = scanner.nextLine().toLowerCase();
                    matcher = pattern.matcher(line);
                    if (matcher.find()) {
                        isFileContainsQuery = true;
                        break;
                    }
                }
            } else {
                while (scanner.hasNextLine()) {
                    //process each line
                    line = scanner.nextLine().toLowerCase();
                    if (line.contains(query.toLowerCase())) {
                        isFileContainsQuery = true;
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }

        return isFileContainsQuery;
    }

    public void setContainLargeFile(boolean containLargeFile) {
        this.containLargeFile = containLargeFile;
    }
}
