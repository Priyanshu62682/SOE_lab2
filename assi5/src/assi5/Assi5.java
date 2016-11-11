package assi5;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Assi5 {

    void run(String filename) {
        String inputData = Helper.getInputFromFile(filename);
        inputData = Helper.removeQuotes(inputData);
        inputData = Helper.removeSinglelineComments(inputData);
        inputData = Helper.removeMultilineComments(inputData);

        cyclo(inputData);
    }

    void cyclo(String inputData) {
        String pattern = "(void|int|float|char|double)\\s+(.*)\\(.*?\\)\\s*\\{";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(inputData);
        int i = 1;
        int total = 0;
        while (m.find()) {
            int openingIndex = m.end();
         //   System.out.println(openingIndex);
            int closingIndex = findMatching(inputData, openingIndex);
         //   System.out.println(closingIndex);
            int noDecisionPoints = findDecisionPoints(inputData.substring(openingIndex, closingIndex));
            System.out.println("Function " + m.group(2) + "(C" + i + "): " + noDecisionPoints + " + 1 = " + (noDecisionPoints + 1));
            total += noDecisionPoints + 1;
            i++;
          // System.out.println(m.group(2));
        }
        System.out.println("TC = " + total);
    }

    int findDecisionPoints(String data) {
        int count = 0;
        String pattern = "(if|while|for)\\s*\\(.*?\\)";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(data);
        while (m.find()) {
           // System.out.println(m.group(1));
            count++;
        }
        return count;
    }

    int findMatching(String inputData, int openingIndex) {
        int i = openingIndex;
        int count = 1;
        int matchingIndex = -1;
        while (true) {
            if (inputData.charAt(i) == '{') {
                count++;
            } else if (inputData.charAt(i) == '}') {
                count--;
            }
            if (count == 0) {
                matchingIndex = i;
                break;
            }
            i++;
        }
        return matchingIndex;
    }

    public static void main(String[] args) {
            new Assi5().run("input.c");
    }
}