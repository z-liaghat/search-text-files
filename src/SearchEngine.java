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

    private QueryGenerator queryGenerator;

    public SearchEngine(List<File> files, QueryGenerator queryGenerator) {
        satisfiedFileNames = new ArrayList<>();
        fileList = files;
        this.queryGenerator = queryGenerator;
    }

    public void search(String query) {
        if (fileList != null) {
            String regexQuery = queryGenerator.convertQueryToRegex(query);
            for (int i = 0; i < fileList.size(); i++) {
                if (searchByRegex(regexQuery, fileList.get(i).getPath())) {
                    satisfiedFileNames.add(fileList.get(i).getName());
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

    private boolean searchByRegex(String regexQuery, String filePath) {
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

}
