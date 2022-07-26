/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package machine.teaching;



import java.util.*;

/**
 *
 * @author Manuel.GPiqueras
 */
public class Library {

    private static final String SYMBOL_LIBRARY_CALL = "@";
    private static final int BASE_NUMBERING_SYSTEM = 2;

    
    // A library of primitives which reflect background knowledge
    private List<String> primitives;
    
    
    public Library(List<String> primitives) {
        this.primitives = primitives;
    }
    
    public String get(int i){
        return this.primitives.get(i);
    }
    
    public void setLibrary(List<String> newLibrary) {
        this.primitives = newLibrary;
    }
    
    public void addPrimitive(String newPrimitive){
        if (this.primitives==null || this.primitives.isEmpty()){
            this.primitives = new ArrayList<>();
            this.primitives.add(newPrimitive);
        }else{
            this.primitives.add(newPrimitive);
        }
    }

    public int getSize(){
        if(this.primitives==null || this.primitives.isEmpty()) return 0;
        else return this.primitives.size();
    }
    
    public int getNumberOfBits(){
        return (int) Math.ceil( (Math.log(getSize()) / Math.log(2)) );
    }
    
    public String getIdPrimitive(int positionPrimitive, int sizeLibrary){
        String result = "";
        
        if (sizeLibrary == 1){
            return SYMBOL_LIBRARY_CALL;
        } else if (positionPrimitive < sizeLibrary){
            
            result = Integer.toString(positionPrimitive, BASE_NUMBERING_SYSTEM);
            
            while (result.length() < getNumberOfBits()){
                result = "0"+result;
            }
            
            return SYMBOL_LIBRARY_CALL + result;
        } 
        
        return result;
    }
    
    public static int BinaryToDecimal(int binaryNumber){
    int decimal = 0;
    int p = 0;
    while(true){
      if(binaryNumber == 0){
        break;
      } else {
          int temp = binaryNumber%10;
          decimal += temp*Math.pow(2, p);
          binaryNumber = binaryNumber/10;
          p++;
       }
    }
    return decimal;
  }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ArrayList<String> ps = new ArrayList<>();
        ps.add("primitive_1");
        ps.add("primitive_2");
        ps.add("primitive_3");
        ps.add("primitive_4");
        ps.add("primitive_5");
        ps.add("primitive_6");
        ps.add("primitive_7");
        ps.add("primitive_8");
        ps.add("primitive_9");
        ps.add("primitive_10");
                
        
        Library library = new Library(ps);
        for (int i=0; i<ps.size(); i++) {
            String id = library.getIdPrimitive(i, ps.size());
            System.out.println("Primitive id: "+id);
            int binary = Integer.valueOf(id.substring(1));
            System.out.println("Binary value:"+binary);
            System.out.println("Conversion to decimal:"+Library.BinaryToDecimal(binary));
        }
        
    }
    
}
