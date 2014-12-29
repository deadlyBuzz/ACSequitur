/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package acsequitur;

import java.util.ArrayList;

/**
 *
 * @author Alan Curley
 */
public class ACSequitur {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {        
        // TODO code application logic here
        ArrayList<String> S = new ArrayList<>();
        ArrayList<diagram> d = new ArrayList<>();
        ArrayList<Rule> R = new ArrayList<>();
        Integer charCount = new Integer(0);
        boolean complete = false;
        String[] entries = args[0].split(",");
        for(String c:entries){ // iterate through each string.
            if(S.size()==0){ // for the first character, load it into "S" and leave it be
                S.add(c);
                d.add(new diagram(c,1));
                charCount = 1;
            }
            else{                
                if(!d.get(d.size()-1).isComplete) // check if the last diagram is incomplete
                    d.get(d.size()-1).add(c);                
                if(charCount!=entries.length-1) //if this is not the last character
                    d.add(new diagram(c,++charCount));                                
            }
        }
            // now have a list of diagrams.
            // check if any of these are supposed to contain Rules.
            do{
            ArrayList<Integer> forDeleting = new ArrayList();
            for(Rule r:R){
                d = d;
                for (diagram diag:d){
                    if(r.matchesDiagram(diag)){
                        Integer dLoc = diag.startChar-1;
                        if(dLoc!=0) // 1st item - cannot update previous
                            d.get(dLoc-1).updateCharacter(1, r.name); // update the previous diagram
                        if(dLoc!=d.size()-1) // last item - cannot update next
                            d.get(dLoc+1).updateCharacter(0, r.name); // update the following diagram
                        forDeleting.add(dLoc); // Mark this diagram for deletion.
                    }
                }
            }
            // now, remove the diagrams in the list <<<< Here Alan
            int delCount = 0;
            for(int i=0; i<d.size();i++)
            for(Integer D:forDeleting){                
                if(i==D-delCount)
                {
                    d.remove(i);
                    delCount++;                           
                }
            }
            // now check each diagram to see if it creates any more new rules.
            int i = d.size();
            boolean newRule = false;
            for(int j=0; j<i; j++){ // Go through each item.
                for(int k=j+1; k<i; k++){ // Go through each item again.
                    if(d.get(j).matchDiagram(d.get(k))){ // if the 2 diagrams are the same.
                        Rule rule = new Rule("R"+String.valueOf(R.size()+1),new diagram(d.get(j))); // create a rule
                        // check if the rule already exists.
//                        if(R.isEmpty()){
//                            R.add(rule);
//                            newRule = true;
//                        }
                        ArrayList<Rule> oldRules = new ArrayList<>(R);
                        boolean ruleExists = false;
                        for(Rule rules:oldRules)
                            if(rules.d.matchDiagram(rule.d))
                                ruleExists = true;                            
                            if(!ruleExists){                                
                                R.add(rule);
                                newRule = true;
                            }
                        break;
                    }                        
                }                
            }
            complete = !newRule; // mark complete if there are no new rules.
            }while(!complete);
        
        
        // build "S"
        S = new ArrayList<>();
        for(diagram diag:d)
            S.add(diag.characters[0]+",");
        S.add(d.get(d.size()-1).characters[1]);
        
        System.out.println("Input: "+args[0]);
        // Print "S"
        System.out.print("S-->");
        for(String s:S)
            System.out.print(s);
        
        // Followed by printing subsequent rules.
        System.out.println();
        for(Rule r:R)
            System.out.println(r.name+"->"+r.d.characters[0]+r.d.characters[1]);
            
        
    }
    
    public static class diagram{
        boolean isComplete = false;
        String[] characters = new String[2];
        Integer startChar;
        public diagram(String s, Integer i){
            characters[0] = s;
            startChar = i;
        }
        
        public diagram(diagram d){
            characters[0] = d.characters[0];
            characters[1] = d.characters[1];
            isComplete = d.isComplete;
            startChar = d.startChar;
        }
        
        public void setStartChar(Integer i){
            startChar = i;
        }
        public Integer getStartChar(){return startChar;}
        
        public void add(String s){
            if(characters[0]==null)
                characters[0] = s;
            else
            {
                characters[1] = s;
                isComplete = true;
            }
        }
        
        public boolean matchDiagram(diagram d){
            return (characters[0].equalsIgnoreCase(d.getCharacters()[0])
                    &
                    characters[1].equalsIgnoreCase(d.getCharacters()[1]));
        }
        
        public String[] getCharacters(){
            return characters;
        }
        
        public void updateCharacter(Integer index, String s){characters[index] = s;}
        
        public boolean isDiagramComplete(){return isComplete;}
    }
            
    public static class Rule{
            String name;
            diagram d;
            public Rule(String name){
               this.name = name;                
            }
            public Rule(String name, diagram d){
                this.name = name;
                this.d = new diagram(d);
            }
            public boolean matchesDiagram(diagram d){
                return this.d.matchDiagram(d);
            }
            
            
        }

}
