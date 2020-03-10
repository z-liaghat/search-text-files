package util;

import java.util.Arrays;
import java.util.List;

public class ShowResult {

    public static void ShowListOfFileName(List inputList) {
        if (inputList == null)
            System.out.println("The result list is null");
        else if (inputList.size() == 0)
            System.out.println("There isn't any file in the directory with your query");
        else
            System.out.println(Arrays.toString(inputList.toArray()));
    }
}
