
package cfg;

/**
 *
 * @author Nishank Priya Jain RIT2014073
 */
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class home {
    
    HashMap node;
    ArrayList<pair> map; 
    int counter=0;
    

    void run() {
        String inputData = Helper.getInputFromFile("input.c");
        inputData = Helper.removeQuotes(inputData);
        inputData = Helper.removeSinglelineComments(inputData);
        inputData = Helper.removeMultilineComments(inputData);
        CFG(inputData);
    }
    
    void CFG(String inputData){
        node=new HashMap();
        map=new ArrayList<pair>();
        
        String pattern = "(void|int|float|char|double)\\s+(.*)\\(.*?\\)\\s*\\{?";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(inputData);
        while (m.find()) {
            int openingIndex = m.end();
            int closingIndex = findMatching(inputData, openingIndex);
            node.put(0, "start");
            processing(0, inputData, openingIndex, closingIndex);
        }
        
    }
    
    ArrayList<Integer> processing(int v,String inputData, int openingIndex, int closingIndex){
        int index=openingIndex;
        int flag=0;
        ArrayList<Integer> returning = new ArrayList<Integer>();
        int latest=v;
        int previous=v;
        String sst=inputData.substring(index, closingIndex);
        System.out.println("out while "+index+" "+closingIndex);
        System.out.println(sst);
       
        while(index<closingIndex){
            System.out.println("in while "+index+" "+closingIndex);
          String pattern = "(if|while|for|else|else if)\\s*(\\(.*?\\))\\s*\\{?";
            Pattern p = Pattern.compile(pattern);
            String ss=inputData.substring(index, closingIndex);
            Matcher m = p.matcher(ss);
            System.out.println(ss+"\n");                    
            
            if(m.find()){
               System.out.println("found");                    
                flag=1;
                int x=m.end();
                int y=findMatching(ss,x);
                index+=y;
                   System.out.println("group 0 => "+m.group(0)+" group 1 => "+m.group(1)+"x= "+x+"y= "+y);
                
                if(m.group(1).equals("if") || m.group(1).equals("else if")){
                    System.out.println("matched wid "+m.group(0));
                    previous=counter;
                    counter++;
                    node.put(counter, m.group(0));
                    map.add(new pair(latest,counter));
                    latest=counter;
                   /* if(!returning.isEmpty()){
                        for(int i=0;i<returning.size();i++)
                            map.add(new pair(returning.get(i),counter));
                    }*/
                    returning.addAll(processing(counter,ss,x,y));
                    
                     String pattern2="else\\s*\\{";
                    Pattern p2 = Pattern.compile(pattern2);
                    String ss2=inputData.substring(index, closingIndex);
                    Matcher m2 = p2.matcher(ss2);
                    
                    String pattern3="else if";
                    Pattern p3 = Pattern.compile(pattern3);
                    String ss3=inputData.substring(index, closingIndex);
                    Matcher m3 = p3.matcher(ss3);
                  boolean f1=m2.find();
                  boolean f2=m3.find();
                    System.out.println("after if part "+ss3+" f1= "+f1+" f2= "+f2);
               
                    if(f2){
                    }
                    else if(f1){
                       System.out.println("in with else");
                 
                        flag=1;
                        x=m2.end();
                        y=findMatching(ss2,x);
                        index+=y;
                        returning.addAll(processing(latest,ss2,x,y));
                    }
                    else {
                        System.out.println("!m3 "+index+"  "+closingIndex);
                 
                        returning.add(latest);
                    }
                    
                }
                else if(m.group(1).equals("for") || m.group(1).equals("while")){
                    //System.out.println("group 0 "+m.group(0)+" group 1 "+m.group(1));
                    previous=latest;
                    counter++;
                    node.put(counter, m.group(0));
                    map.add(new pair(latest,counter)); 
                    latest=counter;
                    if(!returning.isEmpty()){
                        for(int i=0;i<returning.size();i++)
                            map.add(new pair(returning.get(i),counter));
                    }
                    returning=processing(counter,ss,x,y);
                    
                    if(!returning.isEmpty()){
                        for(int i=0;i<returning.size();i++)
                            map.add(new pair(returning.get(i),latest));
                    }
                    
                    counter++;
                    node.put(counter, "end");
                    map.add(new pair(latest,counter));
                    returning.add(latest);
                    returning.add(counter);
                }
            }
            else if(flag==0){
                System.out.println("flag==0\n");                    
            
                latest=counter;
                counter++;
                node.put(counter,inputData.substring(index, closingIndex));
              //  System.out.println(inputData.substring(index, closingIndex));
                map.add(new pair(v,counter));
                returning.add(counter);
              //  System.out.println("not finding");
                break;
            }
            else{
                System.out.println("direct break");                    
                //index++;
                break;
            }
        }
        return returning;
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
    
    void print(){
        System.out.println(counter);
        for(int i=0;i<=counter;i++){
            System.out.println(i+"             "+node.get(i));
        }
        for(int i=0;i<map.size();i++){
            System.out.println(map.get(i).first+"   "+map.get(i).second);
        }
    }
    
    public static void main(String[] args) {
        home h=new home();
        h.run();
        h.print();
        
    }
    
    

}
