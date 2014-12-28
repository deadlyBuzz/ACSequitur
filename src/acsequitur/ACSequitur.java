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
        String[] entries = args[1].split(",");
        for(String c:entries){ // iterate through each string.
            if(S.size()==0){ // for the first character, load it into "S" and leave it be
                S.add(c);
                d.add(new diagram(c,1));
                charCount = 1;
            }
            else{
                if(d.get(d.size()).isComplete){
                    if(charCount!=c.length()) //if this is not the last character.
                        d.add(new diagram(c,charCount));
                }
                else
                    d.get(d.size()).add(c);
                charCount++;
            }
            // now have a list of diagrams.
            // check if any of these are supposed to contain Rules.
            do{
            ArrayList<Integer> forDeleting = new ArrayList();
            for(diagram diag:d){
                for (Rule r:R){
                    if(r.matchesDiagram(diag)){
                        Integer dLoc = diag.startChar;
                        if(dLoc!=1)
                            d.get(dLoc-1).updateCharacter(1, r.name); // update the previous diagram
                        d.get(dLoc+1).updateCharacter(0, r.name); // update the following diagram
                        forDeleting.add(dLoc); // Mark this diagram for deletion.
                    }
                }
            }
            // now, remove the diagrams in the list
            for(Integer i:forDeleting){
                d.remove(i);
            }
            // now check each diagram to see if it creates any more new rules.
            int i = d.size();
            boolean newRule = false;
            for(int j=0; j<i; j++){ // Go through each item.
                for(int k=j; k<i; k++){ // Go through each item again.
                    if(d.get(j).matchDiagram(d.get(k))){ // if the 2 diagrams are the same.
                        Rule rule = new Rule("R"+String.valueOf(R.size()+1),d.get(j));
                        R.add(rule);
                        newRule = true;
                        break;
                    }                        
                }                
            }
            complete = !newRule; // mark complete if there are no new rules.
            }while(!complete);
        }
        
        // build "S"
        S = new ArrayList<>();
        for(diagram diag:d)
            S.add(diag.characters[0]+",");
        S.add(d.get(d.size()-1).characters[1]);
        
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
                this.d = d;
            }
            public boolean matchesDiagram(diagram d){
                return this.d.matchDiagram(d);
            }
            
            
        }

}
