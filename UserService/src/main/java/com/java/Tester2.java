package com.java;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class Tester2 {

	public static void main(String[] args) {

		
	}

	
	public void countPrimeNumber(int n){
		
/*
 * 
 * final class
 * private final primitive fields
 * no setters
 * getters for non-primitive objects 
 * 
 * 
 */
		
	}
	
}


class Result {


    public static void customSort(List<Integer> arr) {

    class Entry {
        int val;
        int cnt;

        public Entry(int val, int cnt) {
            this.val = val;
            this.cnt = cnt;
        }
    }

    class Comp implements Comparator<Entry> {
        @Override
        public int compare(Entry e1, Entry e2) {
            if (e1.cnt == e2.cnt) {
                return e1.val - e2.val;
            }
            return e1.cnt - e2.cnt;
            
        }
    }
    
        PriorityQueue<Entry> pq = new PriorityQueue<Entry>(new Comp());

        Map<Integer, Integer> map = new HashMap<>();

        for (int i = 0; i < arr.size(); i++) {
            if (map.containsKey(arr.get(i))) {
                map.put(arr.get(i), map.get(arr.get(i)) + 1);
            }
            else {
                map.put(arr.get(i), 1);
            }
        }

        Iterator<Integer> iterator = map.keySet().iterator();
        while(iterator.hasNext()) {
            int key = iterator.next();
            Entry entry = new Entry(key, map.get(key));
            pq.offer(entry);
        }

        

    }

}