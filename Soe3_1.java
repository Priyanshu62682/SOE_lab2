/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package soe3_1;

import java.util.regex.Pattern;
import java.io.*;
/**
 *
 * @author placements2017
 */
public class Soe3_1 {

    /**
     * @param args the command line arguments
     */
    public int find_matchfunc(String cur_line,String regex){
                  if(Pattern.matches(regex,cur_line)) {
                   return 1;
                  }
                   else
                   return 0;
       }
    public static void main(String[] args) throws FileNotFoundException, IOException {
        FileInputStream F=null;
        BufferedReader rd=null;
        F=new FileInputStream("example.c");
        rd=new BufferedReader(new InputStreamReader (F));
        String line;
        Soe3_1 s=new Soe3_1();
        int loopc=0,condc=0,func=0,f=0,ans=0;
        line=rd.readLine();
        int k=0;
        int a[]=new int[20];
       // System.out.println(line);
        if(s.find_matchfunc(line,"[\\s]*[/*].*")==1)
        {
            while(true)
            {
                line=rd.readLine();
                if(s.find_matchfunc(line,"[\\s]*.*[*/]")==1)
                {
                    line=rd.readLine();
                    break;
                }
                    
            }
        }
        while((line)!=null)
           {
                f=0;
                int h=0;
                if(s.find_matchfunc(line,"[\\s]*[/*].*")==1)
                {
               while(true)
               {
                     line=rd.readLine();
                     if(s.find_matchfunc(line,"[\\s]*.*[*/]")==1)
                     {
                         line=rd.readLine();
                         break;
                     }
               }
                }
                if(line!=null&&s.find_matchfunc(line,"[\\s]*void [^;]*")==1||s.find_matchfunc(line,"[\\s]*int .*[(].*[)][^;]*")==1)
                {
                    k++;
                    System.out.print("Function ");
                    for(int i=0;i<line.length();i++)
                    {
                        if(line.charAt(i)==' ')
                        {
                            h=i;
                            break;
                        }
                            
                    }
                    for(int i=h+1;i<line.length();i++)
                    {
                        if(line.charAt(i)=='(')
                            break;
                        System.out.print(line.charAt(i));
                    }
                    System.out.print("(C"+k+"):");
                    line=rd.readLine();
                    while((line)!=null&&s.find_matchfunc(line,"[\\s]*void [^;]*")==0&&s.find_matchfunc(line,"[\\s]*int .*[(].*[)][^;]*")==0)
                    {
                        if(s.find_matchfunc(line,"[\\s]*if[(].*[)]")==1||s.find_matchfunc(line,"[\\s]*else if[(].*[)]")==1||s.find_matchfunc(line,"[\\s]*switch[(].*[)]")==1||s.find_matchfunc(line, "[\\s]*for[(].*;.*;.*[)]")==1||s.find_matchfunc(line,"[\\s]*while[(].*[)]")==1||s.find_matchfunc(line,"[\\s]*do[{]*.*")==1)
                        {
                           f++;
                           
                        }
                        line=rd.readLine();
                    }
                    System.out.println(" "+f+"+ 1 = " +(f+1));
                    ans+=(f+1);
                    a[k-1]=f+1;
                }
                else
                {
                    line=rd.readLine();
                }
           }
        System.out.print("TC=");
        for(int i=0;i<k-1;i++)
        {
            System.out.print(a[i]+"+");
        }
        System.out.print(a[k-1]+"=");
        System.out.println(ans);
   //     System.out.println(func);
    }
    
}
