/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package machine.teaching;

/**
 *
 * @author Manuel.GPiqueras
 */
public class Pencil {

    /** Every instruction of a program created by the learner is translated into a direction to create a path.
     */
    public char getStroke(char stroke){
        switch (stroke) {
            case 'U':
                return 'N';
            case 'D':
                return 'S';
            case 'R':
                return 'E';
            default:
                return 'W';
        }
    } 

    /**
     * If the example given in the argument is positive and the program of the argument is able to describe that path, then the program returns true; if the example is negative and the program does describe the path given, then it returns false (true otherwise).
     * 
     * @param example An example which is a path using the four cardinal directions and the output is either positive or negative.
     * @param program A program that might be compatible with the path given.
     * @return It returns true if the program given in the argument is compatible with the example given. 
     */
    public boolean isExampleCompatible(ioExample example, String program){
        //System.out.println("Example: "+example.getPath()+"->"+example.getPositive());
        //System.out.println("Program: "+program);
        
        char[] p = program.toCharArray();
                
        char[] exampleChars = example.getPath().toCharArray();
        boolean coincide = false;
        boolean execute_p = true;
        int index_p = 0; 
        int index_ex = 0;
        
        while (execute_p == true){
            //We check whether the path is not completed and the program already finished
            if (index_ex < example.getPath().length() && index_p == p.length){
                //System.out.println("The example was not completed.");
                coincide = false;
                break;
                
            }

            // We check whether the program starts with an infinite loop )
            if(p[index_p]==')' && index_p==0) {
                //System.out.println("The program starts with an infinite loop.");
                coincide = false;
                break;
            } 
            
            
            // We define an auxiliary variable to store the index of program p
            int index_p_aux = index_p;
            boolean parenthesesFound = false;
            // Close brackets may redirect the program to the first position
            if(p[index_p]==')') {
                parenthesesFound = true;
                //System.out.println("Close brackets redirects the program to the first position.");
                index_p = 0;
                //System.out.println("index_p:"+index_p);
                //System.out.println("index_p_aux:"+index_p_aux);
            }
            
            // We retrieve the instruction of the program
            char instruction = p[index_p];
            
            // If the instruction retrieved does not coincide with the stroke then
            // the program is not compatible with the example given. 
            // Otherwise, we continue.
            if ( this.getStroke(instruction) != exampleChars[index_ex]){
                boolean makeBreak = true;
                //System.out.println("Stroke"+index_ex+":"+this.getStroke(instruction));
                //System.out.println("exampleChars"+index_ex+":"+exampleChars[index_ex]);
                // If we found parentheses just the instruction before and the beginning of
                // the program does not match properly with the example, then
                // we have to continue after that last parentheses
                if(parenthesesFound==true && (index_p_aux+1)<p.length ) {
                    index_p = index_p_aux + 1;
                    instruction = p[index_p];
                    if (this.getStroke(instruction) == exampleChars[index_ex]) {
                        makeBreak = false;
                    }
                }
                if(makeBreak) break;
            }
            
            // We increase the index of the example 
            index_ex++;
            index_p++;
            
            // If we get to the end of the problem and there are no differences between
            // the path of the example and the one defined by the program, then the example
            // is compatible with the concept
            if (index_ex == example.getPath().length()) {
                execute_p = false;
                coincide = true;
            }
        }//while     
                
        //System.out.println("Coincide?: "+coincide);
        boolean compatible = false;
        if(coincide == example.getPositive()) {
            compatible = true;
        }
        return compatible;
    }
    
    public static void main (String[] args){
        
        Pencil pencil = new Pencil();
//        boolean isCompatible = pencil.isExampleCompatible(new ioExample("W", false), "L)L)L)");
//        System.out.println("Result: "+isCompatible);
        //{<W->, <WW+>} ==> L)L)L)
        
        String p1 = "R)URD)";
        //String p1 = "RURDR)R)";
        String p2 = "RUWD";
        
        ioExample exa = new ioExample("ENESEEE", true);
        
        boolean p1_compatible = pencil.isExampleCompatible(exa, p1);
        boolean p2_compatible = pencil.isExampleCompatible(exa, p2);
        
        
        System.out.println("Is program p1="+p1+" compatible with example"+exa+"?: "+p1_compatible);
        System.out.println("Is program p2="+p2+" compatible with example"+exa+"?: "+p2_compatible);
        
        
        String p3 = "UURDDLUR";
        String p4 = "UURDLDRU";
        
        ioExample exb = new ioExample("NNESSWNE", true);
        
        boolean p3_compatible = pencil.isExampleCompatible(exb, p3);
        boolean p4_compatible = pencil.isExampleCompatible(exb, p4);

        System.out.println("Is program p3="+p3+" compatible with example"+exb+"?: "+p3_compatible);
        System.out.println("Is program p4="+p4+" compatible with example"+exb+"?: "+p4_compatible);
        
        
        ioExample exc = new ioExample("ENESEEE", true);
        String p5 ="RURD)RRURD)";
        String p6 ="RURD)RRRURD)";
        boolean p5_compatible = pencil.isExampleCompatible(exc, p5);
        boolean p6_compatible = pencil.isExampleCompatible(exc, p6);
        System.out.println("Is program p5="+p5+" compatible with example"+exc+"?: "+p5_compatible);
        System.out.println("Is program p6="+p6+" compatible with example"+exc+"?: "+p6_compatible);
        
    }
    
}
