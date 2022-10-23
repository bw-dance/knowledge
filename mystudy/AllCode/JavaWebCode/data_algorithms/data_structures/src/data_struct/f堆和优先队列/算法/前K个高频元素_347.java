package data_struct.f堆和优先队列.算法;


import data_struct.f堆和优先队列.base.PriorityQueue;

import java.util.Arrays;
import java.util.Comparator;
import java.util.TreeMap;

public class 前K个高频元素_347 {
    public static void main(String[] args) {
        Solution solution = new Solution();
        System.out.println(Arrays.toString(solution.topKFrequent2(new int[]{1, 1, 1, 2, 2, 3}, 2)));
    }
}

class Solution {
    private class Freq implements Comparable<Freq>{
        int e,freq;

        public Freq(int e, int freq) {
            this.e = e;
            this.freq = freq;
        }

        @Override
        public int compareTo(Freq another) {
            if (this.freq<another.freq){
                return -1;
            }else if (this.freq> another.freq){
                return 1;
            }else {
                return 0;
            }
        }
    }
    public int[] topKFrequent(int[] nums, int k) {
        int[] target = new int[k];
        TreeMap<Integer, Integer> map = new TreeMap<>();
        for (int i = 0; i < nums.length; i++) {
            map.put(nums[i], map.getOrDefault(nums[i], 0).intValue() + 1);
        }
        PriorityQueue<Freq> priorityQueue = new PriorityQueue<>();
        for (Integer key:map.keySet()) {
            if (priorityQueue.getSize()<k){
                priorityQueue.enqueue(new Freq(key,map.get(key)));
            }else if (map.get(key)>priorityQueue.getFront().freq){
                priorityQueue.dequeue();
                priorityQueue.enqueue(new Freq(key,map.get(key)));
            }
        }
        for (int i = 0; i < priorityQueue.getSize(); i++) {
            target[i]=priorityQueue.dequeue().e;
        }
        return target;
    }

    //jdk的优先队列实现
    public int[] topKFrequent2(int[] nums, int k) {
        int[] target = new int[k];
        TreeMap<Integer, Integer> map = new TreeMap<>();
        for (int i = 0; i < nums.length; i++) {
            map.put(nums[i], map.getOrDefault(nums[i], 0).intValue() + 1);
        }
        java.util.PriorityQueue<Freq> priorityQueue = new  java.util.PriorityQueue<>();
        for (Integer key:map.keySet()) {
            if (priorityQueue.size()<k){
                priorityQueue.add(new Freq(key,map.get(key)));
            }else if (map.get(key)>priorityQueue.peek().freq){
                priorityQueue.remove();
                priorityQueue.add(new Freq(key,map.get(key)));
            }
        }
        for (int i = 0; i < k; i++) {
            target[i]=priorityQueue.remove().e;
        }
        return target;
    }

    private class  FreComparator implements Comparator<Freq>{
        @Override
        public int compare(Freq a, Freq b) {
            return a.freq-b.freq;
        }
    }
    public int[] topKFrequent3(int[] nums, int k) {
        int[] target = new int[k];
        TreeMap<Integer, Integer> map = new TreeMap<>();
        for (int i = 0; i < nums.length; i++) {
            map.put(nums[i], map.getOrDefault(nums[i], 0).intValue() + 1);
        }
        java.util.PriorityQueue<Integer> priorityQueue = new  java.util.PriorityQueue<>((a,b)->{return map.get(a)-map.get(b);});
        for (Integer key:map.keySet()) {
            if (priorityQueue.size()<k){
                priorityQueue.add(key);
            }else if (map.get(key)>map.get(priorityQueue.peek())){
                priorityQueue.remove();
                priorityQueue.add(key);
            }
        }
        for (int i = 0; i < k; i++) {
            target[i]=priorityQueue.remove();
        }
        return target;
    }
}

