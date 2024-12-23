package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
  public static void main(String[] args) {
    Solution.readInputAndSave("src/main/resources/input.txt");
    print2DList(Solution.unsafePrints);
    System.out.println();
    Solution.sortUnsafePrints();
    print2DList(Solution.unsafePrints);
    System.out.println(Solution.getMiddleSum(Solution.unsafePrints));
  }
  static void print2DList(List<List<Integer>> list) {
    for (List<Integer> currentList : list) {
      for (int val:currentList) {
        System.out.printf("%d, ", val);
      }
      System.out.println();
    }
  }
}

class Solution{
  static Map<Integer, List<Integer>> ruleMap = new HashMap<>();//Key is smaller than value
  static List<List<Integer>> safePrints = new ArrayList<>();
  static List<List<Integer>> unsafePrints = new ArrayList<>();
  public static void readInputAndSave(String path) {
    try(BufferedReader br = new BufferedReader(new FileReader(path))) {
      String line;
      boolean isReadingRules = true;
      while((line = br.readLine()) != null) {
        if (line.isBlank()){
          isReadingRules = false;
          continue;
        }
        if (isReadingRules) {
          //Should make a method for this:
          String[] rule = line.split("\\|");
          int small = Integer.parseInt(rule[0]);
          int big = Integer.parseInt(rule[1]);
          if(ruleMap.containsKey(small)) ruleMap.get(small).add(big);
          else {
            List<Integer> list = new ArrayList<>();
            list.add(big);
            ruleMap.put(small, list);
          }
        } else{
          //reading lists of number, should also be own method
          String[] valuesAsString = line.split(",");
          //Get all values in a integer list
          List<Integer> values = new ArrayList<>();
          for (String val:valuesAsString) {
            values.add(Integer.parseInt(val));
          }
          //Now check if the list is safe
          boolean isSafe = true;
          for(int i = 0; i < values.size(); i++) {
            List<Integer> iRules = ruleMap.get(values.get(i));
            for (int j = i+1; j < values.size(); j++) {
              if (!iRules.contains(values.get(j))) {
                isSafe = false;
                i = values.size();
                break;
              }
            }
          }
          if (isSafe) safePrints.add(values);
          else unsafePrints.add(values);
        }
      }
    }catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static int getMiddleSum(List<List<Integer>> listList) {
    int sum = 0;
    for (List<Integer> list:listList) {
      int middle = list.size()/2;
      sum+=list.get(middle);
    }
    return sum;
  }

  public static void sortUnsafePrints() {
    for (List<Integer> unsafeList : unsafePrints) {
      sortPrint(unsafeList);//mutator method makes unsafe -> safe
    }
  }

  private static void sortPrint(List<Integer> unsortedList) {
    //using a bubble sort type sort
    for (int i = 0; i < unsortedList.size()-1; i++) {
      for (int j = 0;j < unsortedList.size()-1;j++) {
        int currentVal = unsortedList.get(j);
        int nextVal = unsortedList.get(j+1);
        if(!ruleMap.get(currentVal).contains(nextVal)) {
          //Not correct order and switch
          int temp = currentVal;
          unsortedList.set(j, nextVal);
          unsortedList.set(j+1, temp);
        }
      }
    }
  }
}