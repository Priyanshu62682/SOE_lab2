       /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package soe7;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
public class Soe7 {
    static int findMatching(String inputData, int openingIndex) {
        int i = openingIndex;
        int count = 1;
        boolean flag = true;
        int matchingIndex = -1;
        while (true) {
            if (inputData.charAt(i) == '{') {
                count++;
                if (count == 1) {
                    flag = true;
                }
            } else if (inputData.charAt(i) == '}') {
                count--;
            }
            if (count == 0 && flag == true) {
                matchingIndex = i;
                break;
            }
            i++;
        }
        return matchingIndex;
    }
    ArrayList<String> findMethodsInvoked(String data) {
        Pattern detectMethodsInvoked = Pattern.compile("\\b([A-Za-z]\\w*)\\b\\s*\\(");
        Matcher matcher = detectMethodsInvoked.matcher(data);
        ArrayList<String> methodsInvoked = new ArrayList<String>();
        while (matcher.find()) {
            //System.out.println("> " + matcher.group(1));
            if(filter2(matcher.group(1)))
            methodsInvoked.add(matcher.group(1));
        }
        return methodsInvoked;
    }
    boolean filter(String name) {
        Pattern keyword = Pattern.compile("(int|float|void|double|public|private|protected|boolean)");
        Matcher m = keyword.matcher(name);
        if (m.find()) {
            return false;
        } else {
            return true;
        }
    }
    boolean filter2(String name) {
        Pattern keyword = Pattern.compile("(for|if|while)");
        Matcher m = keyword.matcher(name);
        if (m.find()) {
            return false;
        } else {
            return true;
        }
    }
    ArrayList<String> findVariables(String data) {
        Pattern detectVariables = Pattern.compile("\\b[A-Za-z]\\w*\\b(?!\\s*\\()");
        Matcher matcher = detectVariables.matcher(data);
        ArrayList<String> variables = new ArrayList<String>();
        while (matcher.find()) {
        //    System.out.println(matcher.group(0));
            if (filter(matcher.group(0))) {
                variables.add(matcher.group(0));
            }
        }
        return variables;
    }
    String removeiden(String x)
    {
        String pattern = "int|float|char";
       Pattern p = Pattern.compile(pattern);
       Matcher m = p.matcher(x);
       x = m.replaceAll("");
       return x;
    }
    void findCohesion(String data)
    {
        ArrayList<ArrayList<String>> variablesInMethods = new ArrayList<ArrayList<String>>();
           ArrayList<String> methods = new ArrayList<String>();
           Pattern detectMethod = Pattern.compile("\\w+\\s+((\\w+)\\((.*?)\\))\\s*\\{");
           ArrayList<String> attributes = new ArrayList<String>();
           Matcher matcher = detectMethod.matcher(data);
           boolean found = false;
           System.out.println("Methods:");
           while (matcher.find()) {
            if (!found) {
                attributes = findVariables(data.substring(0, matcher.start()));
                found = true;
            }
            int openingIndex = matcher.end();
            int closingIndex = findMatching(data, openingIndex);
            System.out.print(matcher.group(2));
            String x=matcher.group(3);
            int t=0;
            for(int i=0;i<x.length();i++)
            {
                if(x.charAt(i)==' ')
                {
                    t=i;
                    break;
                }
            }
            x=x.substring(t);
            x=removeiden(x);
            if(!x.equals(""))
            {
    //            if(filter(x))
            System.out.print(" argument list:- " + x);
            }
            else
            System.out.print(" argument list:- null");
            variablesInMethods.add(findVariables(data.substring(openingIndex, closingIndex)));
            ArrayList<String> dd=findMethodsInvoked(data.substring(openingIndex, closingIndex));
           // k.add("[]");
       //     System.out.println(k);
            int u=dd.size();
            if(u>0)
            {
                System.out.print(" methods invocation:- ");
                for(int i=0;i<u;i++)
                {
                    System.out.print(dd.get(i)+" ");
                }
                System.out.println("\n");
            }
            else
                System.out.println(" methods invocation:- null");
            methods.add(matcher.group(1));
        }
    }
    public static void main(String[] args) throws FileNotFoundException, IOException {
       Soe7 ss=new Soe7();
       FileInputStream F=null;
       BufferedReader rd=null;
       F=new FileInputStream("input.java");
       rd=new BufferedReader(new InputStreamReader (F));
       String line,file1="";
       while((line=rd.readLine())!=null)
       {
           file1+=line+"\n";
       }
       String pattern = "//.*?\\n";
       Pattern p = Pattern.compile(pattern);
       Matcher m = p.matcher(file1);
       file1 = m.replaceAll("\n");
       Pattern detectClass = Pattern.compile("class\\s+(\\w+)\\s*\\{");
       Matcher matcher = detectClass.matcher(file1);
       while (matcher.find()) 
       {
           int openingIndex = matcher.end();
           int closingIndex = findMatching(file1, openingIndex);
           System.out.println(matcher.group(1));
           ss.findCohesion(file1.substring(openingIndex, closingIndex));
        //   System.out.println("*******");
       //    System.out.println(file1.substring(openingIndex,closingIndex));
        //   System.out.println("*******");
       }
    
}
}
