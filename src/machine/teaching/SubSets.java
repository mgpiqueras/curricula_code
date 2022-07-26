/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package machine.teaching;

import java.util.List;
import java.util.ArrayList;

public class SubSets {
    public List<List<Integer>> allSubSets(List<Integer> list) {
        List<List<Integer>> out = new ArrayList<List<Integer>>();
        for(int i=1; i<=list.size(); i++) {
            List<List<Integer>> outAux = this.subSets(list, i);
            out.addAll(outAux);
        }
        return out;
    }

    private List<List<Integer>> subSets(List<Integer> list, int size) {
        List<List<Integer>> out = new ArrayList<List<Integer>>();
        for(int i=0; i<list.size()-size+1;i++) {
            List<Integer> subset = new ArrayList<Integer>();
            for (int j=i;j<i+size-1;j++) {
                subset.add(list.get(j));
            }
            if (!(size==1 && i>0)) {
                for (int j=i+size-1;j<list.size();j++) {
                    List<Integer> newsubset = new ArrayList<Integer>(subset);
                    newsubset.add(list.get(j));
                    out.add(newsubset);
                }
            }
        }
        return out;
    }
    
     public static void main(String args[]){
        SubSets aux = new SubSets();
        List<Integer> list = new ArrayList<Integer>();
        
        list.add(1);
//        list.add(2);
        //list.add(3);
        List<List<Integer>> out = aux.allSubSets(list);
        System.out.println("# subsets: "+out.size());
        
        System.out.println(out);
         
     }
}
