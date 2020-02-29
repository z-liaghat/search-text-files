import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchEngine {
    //This list is used to store the name of file which contains the searched query
    List<String> satisfiedFileNames;

    //The list of all files in the directory
    List<File> allFiles;

    private boolean containLargeFile;

    public SearchEngine(List<File> files, boolean isContainLargeFile) {
        satisfiedFileNames = new ArrayList<>();
        allFiles = files;
        containLargeFile = isContainLargeFile;
    }

    public void search(String query) {
        if (allFiles != null) {
            String regexQuery = convertQueryToRegex(query);
            if (!containLargeFile) {
                for (int i = 0; i < allFiles.size(); i++) {
                    if (searchByRegex(regexQuery, allFiles.get(i).getPath())) {
                        satisfiedFileNames.add(allFiles.get(i).getName());
                    }
                }
            } else {
                for (int i = 0; i < allFiles.size(); i++) {
                    if(allFiles.get(i).length()<=2000000000) {
                        if (searchByRegex(regexQuery, allFiles.get(i).getPath())) {
                            satisfiedFileNames.add(allFiles.get(i).getName());
                        }
                    }else {
                        if (searchQueryLineByLine(query, allFiles.get(i))) {
                            satisfiedFileNames.add(allFiles.get(i).getName());
                        }
                    }
                }
            }
        }
        printFileNamesOfResult();
        resetSatisfiedFileNameList();
    }

    private void resetSatisfiedFileNameList() {
        satisfiedFileNames.clear();
    }

    public void printFileNamesOfResult() {
        if (satisfiedFileNames.size() == 0)
            System.out.println("there isn't any file in the directory");
        else
            System.out.println(Arrays.toString(satisfiedFileNames.toArray()));
    }

    public boolean searchByRegex(String regexQuery, String filePath) {
        boolean isFileContainsQuery = false;
        // It contains all of the lines in the file
        String line;
        StringBuilder fileContent = new StringBuilder();
        Pattern pattern = Pattern.compile(regexQuery);

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
            while ((line = bufferedReader.readLine()) != null) {
                fileContent.append(line.toLowerCase()); // Append each line to the "file" string
            }
            //Inorder to be case sensitive,  all text is converted to lowercase
            Matcher matcher = pattern.matcher(fileContent);
            isFileContainsQuery = matcher.matches();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return isFileContainsQuery;
    }

    public String convertQueryToRegex(String query) {
        StringBuilder regexQuery = new StringBuilder();
        String[] operandBetweenORList;
        if (query.contains(Operators.OR.name())) {
            operandBetweenORList = query.split(Operators.OR.name());
            for (int i = 0; i < operandBetweenORList.length; i++) {
                StringBuilder andExpression = convertAndExpressionToRegex(operandBetweenORList[i]);
                regexQuery.append(andExpression);
                regexQuery.append("|");
                if (i == operandBetweenORList.length - 1)
                    regexQuery.deleteCharAt(regexQuery.length() - 1);
            }
        } else {
            regexQuery = convertAndExpressionToRegex(query);
        }
        System.out.println(regexQuery);
        return regexQuery.toString().toLowerCase();
    }

    private StringBuilder convertAndExpressionToRegex(String andExpression) {
        String[] operand = andExpression.split(Operators.AND.name());
        StringBuilder regex = new StringBuilder();
        for (int j = 0; j < operand.length; j++) {
            regex.append("(?=.*" + operand[j].toLowerCase().trim() + ")");
        }
        regex.append(".*");
        return regex;
    }

    //This method is use for huge files (more than 2 Gb). It reads files line by line
    public boolean searchQueryLineByLine(String query, File file) {
        String regexQuery;
        Pattern pattern = null;
        Matcher matcher;
        boolean isFileContainsQuery = false;
        AndPhrase andPhrase = null;
        OrPhrase orPhrase = null;
//        Path path = Paths.get(fileName);
        Scanner scanner = null;

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
                while_label:
                while (scanner.hasNextLine()) {
                    //process each line
                    line = scanner.nextLine().toLowerCase();
                    matcher = pattern.matcher(line);
                    if (matcher.find()) {
                        isFileContainsQuery = true;
                        break while_label;
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


    public boolean isContainLargeFile() {
        return containLargeFile;
    }

    public void setContainLargeFile(boolean containLargeFile) {
        this.containLargeFile = containLargeFile;
    }
}
