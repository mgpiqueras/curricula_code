/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package machine.teaching;


//import java.util.ArrayList;

/**
 *
 * @author Manuel.GPiqueras
 */
public class WitnessSet {

	// We consider that the size in bits of every command is 3
	// Also every example has another character, either positive '+' or negative '-'
	public static int sizeBitsCommand = 3;


    private ioExample[] examples;

    public WitnessSet(ioExample[] examples) {
        this.examples = examples;
    }

 
    public ioExample[] getExamples() {
        return examples;
    }
    
    public int getSizeBits() {
    	int sizeBits = 0;
    	if (this.examples == null ) return sizeBits;
        
        for(int i=0; i<this.examples.length; i++){
//        	System.out.println("sizeBits: "+sizeBits);
            sizeBits = sizeBits + ((this.examples[i].getPath().length())+1)*3;
        }
        
        return sizeBits;
    }

    @Override
    public String toString() {
        String info = new String();
        if (examples == null ) return info;
        for(int i=0; i<examples.length; i++){
            info = info + examples[i].toString();
            
            if( i<(examples.length-1) ) info = info + ", ";
        }
        
        return "{" + info + "}";
    }
    
    public static void main(String[] args) {
    	ioExample[] ioexs3 = new ioExample[2];        

    	ioexs3[0] = new ioExample("WW", true);
    	ioexs3[1] = new ioExample("N", true);
    	
    	WitnessSet w3 = new WitnessSet(ioexs3);
    	System.out.println("Size of "+w3+" in bits is: "+w3.getSizeBits());
    	
    }
    
}
