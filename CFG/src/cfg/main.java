package cfg;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class main {
    
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
        
        String pattern = "(void|int|float|char|double)\\s+(.*)\\(.*?\\)\\s*\\{?";//function extractor regex
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(inputData);
        ArrayList<Integer> returned=new ArrayList<>();
        while (m.find()) {
            int openingIndex = m.end();
            int closingIndex = findMatching(inputData, openingIndex);
            node.put(0, "start");
           // counter++;
           returned.addAll( processing(0, inputData, openingIndex, closingIndex));
        }
        counter++;// end node
        node.put(counter, "end");
        for(Integer i:returned){
           map.add(new pair(i,counter));
        }
        
    }
    
    ArrayList<Integer> processing(int node_now,String inputData, int openingIndex, int closingIndex){
        int index=openingIndex;
        int flag=0;
        ArrayList<Integer> returning = new ArrayList<Integer>();
        int last_if=-1;
        int last_node=node_now,help_node;
        String s_now=inputData.substring(index, closingIndex);
       // System.out.println("out while "+index+" "+closingIndex);
        //System.out.println(s_now);
       
        while(index<closingIndex){
          //  System.out.println("in while "+index+" "+closingIndex);
            //String pattern = "((if|while|for|else if)\\s*(\\(.*?\\))\\s*\\{?)|((else)\\s*\\{?)";
            String pattern = "((if|while|for|else if|else|do)\\s*(\\(.*?\\))?\\s*\\{?)";
            Pattern p = Pattern.compile(pattern);
            String s_seg=inputData.substring(index, closingIndex);
            Matcher m = p.matcher(s_seg);
         //   System.out.println(s_seg+"\n");                    
            
            if(m.find()){
                
                System.out.println("found "+m.group(0));                    
                flag=1;
               
                int x=m.end();
              //    System.out.println("x= "+x+"closingIndex "+closingIndex+" string = "+inputData.substring(x, closingIndex));
                int y=findMatching(s_seg,x);
                index+=y;
                //   System.out.println("x= "+x+"y= "+y);
                
               /* for(int i=0;i<m.groupCount();i++){
                    System.out.println("i="+i+"group = "+m.group(i));  
                }*/
                   
                if(m.group(2).equals("if")){
                    counter++;
                    help_node=counter;
                    node.put(counter, m.group(0));
                    map.add(new pair(last_node,counter));
                   // System.out.println("before call counter= "+counter+" last_node= "+last_node+" last_if= "+last_if);
                    last_node=help_node;
                    if(!returning.isEmpty()){
                              for(int i=0;i<returning.size();i++)
                                 map.add(new pair(returning.get(i),counter));
                    }
                    returning.clear();
                    last_if=help_node;
                    returning.addAll(processing(counter,s_seg,x,y));
                }
                else if(m.group(2).equals("else if")){
                    //System.out.println("matched wid "+m.group(0));
                    //last_if=counter;
                   // last_node=counter;
                    counter++;
                    help_node=counter;
                    node.put(counter, m.group(0));
                    map.add(new pair(last_node,counter));
                   // System.out.println("before call counter= "+counter+" last_node= "+last_node+" last_if= "+last_if);
                    last_node=help_node;
                    last_if=help_node;
                    returning.addAll(processing(counter,s_seg,x,y));
                    
                   // System.out.println("after call counter= "+counter+" last_node= "+last_node+" last_if= "+last_if);
                   
                    
                  }
                  else if(m.group(2).equals("else")){
                    
                    //System.out.println("matched wid "+m.group(0));
                    //last_node=counter;
                    counter++;
                    help_node=counter;
                    node.put(counter, m.group(0));
                    map.add(new pair(last_if,counter));
                    //System.out.println("before call counter= "+counter+" last_node= "+last_node+" last_if= "+last_if);
                   
                    returning.addAll(processing(counter,s_seg,x,y));
                   // last_node=help_node;
                    last_node=help_node>=counter?help_node:counter;
                    last_if=-1;
                //     System.out.println("after call counter= "+counter+" last_node= "+last_node+" last_if= "+last_if);
                   
                }
                else if(m.group(2).equals("for") || m.group(2).equals("while")){
                    //System.out.println("group 0 "+m.group(0)+" group 1 "+m.group(1));
                   //System.out.println("matched wid "+m.group(0));
                  
                    //last_node=counter;
                    counter++;
                    help_node=counter;
                    node.put(counter, m.group(0));
                    map.add(new pair(last_node,counter)); 
                //      System.out.println("counter= "+counter+" last_node= "+last_node+" last_if= "+last_if);
                  
                    last_node=help_node;
                    if(last_if!=-1){
                        returning.add(last_if);
                        last_if=-1;
                    }
                    
                    if(!returning.isEmpty()){
                        for(int i=0;i<returning.size();i++)
                            map.add(new pair(returning.get(i),counter));
                    }
                    returning=processing(counter,s_seg,x,y);
                    
                    if(!returning.isEmpty()){//linked to start of the loop
                        for(int i=0;i<returning.size();i++)
                            map.add(new pair(returning.get(i),last_node));
                    }
                    returning.clear();//cleared after llinking with the start of the loop
                      
                    returning.add(last_node);//node = start of the loop 
                }
                else if(m.group(2).equals("do")){
                    counter++;
                    help_node=counter;
                    node.put(counter, m.group(0));
                    map.add(new pair(last_node,counter));
                    last_node=help_node;
                    
                     System.out.println("s_seg now "+s_seg.substring(x, y));
                    
                    if(last_if!=-1){
                        returning.add(last_if);
                        last_if=-1;
                    }
                    
                    if(!returning.isEmpty()){
                        for(int i=0;i<returning.size();i++)
                            map.add(new pair(returning.get(i),counter));
                    }
                    returning=processing(counter,s_seg,x,y);
                     
                    s_seg=inputData.substring(index, closingIndex);
                    
                    m=p.matcher(s_seg);
                    System.out.println("s_seg now "+s_seg);
                    m.find();
                    
                    counter++;
                    help_node=counter;
                    node.put(counter, m.group(0));
                    //last_node=help_node;//last node should be "do" for now 
                    System.out.println("now group==> "+m.group(0));
                    
                    if(!returning.isEmpty()){//linked to start of the loop
                        for(int i=0;i<returning.size();i++)
                            map.add(new pair(returning.get(i),help_node));
                    }
                    returning.clear();//cleared after llinking with the start of the loop
                    map.add(new pair(help_node,last_node));//help_node=>while,last_node="do"
                    last_node=help_node;//now last_node=while;
                    returning.add(last_node);//node = start of the loop
                    x=m.end();
                    index+=x;
                }
                
            }
            else if(flag==0){
                //System.out.println("flag==0\n");                    
            
                last_node=counter;
                counter++;
                node.put(counter,inputData.substring(index, closingIndex));
            //   System.out.println(inputData.substring(index, closingIndex));
                map.add(new pair(node_now,counter));
                returning.add(counter);
               // System.out.println("not finding");
                break;
            }
            else{
                // System.out.println("direct break");                    
                break;
            }
        }
        if(last_if!=-1){
            returning.add(last_if);
            last_if=-1;
        }
        return returning;
    }
    
    int findMatching(String inputData, int openingIndex) {
        int i = openingIndex;
        int count = 1;
        int matchingIndex = -1;
        while (true) {
            //System.out.print(inputData.charAt(i)+"  ");
           // System.out.print("count= "+count);
            
            if (inputData.charAt(i) == '{') {
                count++;
             // System.out.println(" ");
                
            } else if (inputData.charAt(i) == '}') {
                count--;
               // System.out.print(" ");
            
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
            System.out.println(i+"-->"+node.get(i));
        }
        for(int i=0;i<map.size();i++){
            System.out.println(map.get(i).first+"   "+map.get(i).second);
        }
    }
    
    public static void main(String[] args) {
        main mn=new main();
        mn.run();
        mn.print();
        
    }
    
}
