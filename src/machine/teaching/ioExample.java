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
public class ioExample {
   private String path;
   private boolean positive;

    public void setPositive(boolean positive) {
        this.positive = positive;
    }

    public ioExample(String path, boolean positive) {
        this.path = path;
        this.positive = positive;
    }
   
   public String getPath(){
       return this.path;
   }
   
   public boolean getPositive(){
       return this.positive;
   }

    @Override
    public String toString() {
        
        if(this.positive) return "<" + path + "+>";
        else return "<" + path + "->";
    }
   
}
