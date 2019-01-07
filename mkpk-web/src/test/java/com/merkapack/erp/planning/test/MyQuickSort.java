package com.merkapack.erp.planning.test;

import java.util.Date;
import java.util.Random;

public class MyQuickSort {
     
    private int array[];
    private int length;
 
    public void sort(int[] inputArr) {
         
        if (inputArr == null || inputArr.length == 0) {
            return;
        }
        this.array = inputArr;
        length = inputArr.length;
        
        Date start = new Date();
        quickSort1( 0, length - 1);
        Date end = new Date();
        System.out.println();
        System.out.println(end.getTime() - start.getTime() + "ms.");

        start = new Date();
        quickSort(inputArr, 0, length - 1);
        end = new Date();
        System.out.println();
        System.out.println(end.getTime() - start.getTime() + "ms.");
}
 
    private void quickSort1(int lowerIndex, int higherIndex) {
         
        int i = lowerIndex;
        int j = higherIndex;
        // calculate pivot number, I am taking pivot as middle index number
        int pivot = array[lowerIndex+(higherIndex-lowerIndex)/2];
        // Divide into two arrays
        while (i <= j) {
            /**
             * In each iteration, we will identify a number from left side which 
             * is greater then the pivot value, and also we will identify a number 
             * from right side which is less then the pivot value. Once the search 
             * is done, then we exchange both numbers.
             */
            while (array[i] < pivot) {
                i++;
            }
            while (array[j] > pivot) {
                j--;
            }
            if (i <= j) {
                exchangeNumbers(i, j);
                //move index to next position on both sides
                i++;
                j--;
            }
        }
        // call quickSort() method recursively
        if (lowerIndex < j)
            quickSort1(lowerIndex, j);
        if (i < higherIndex)
            quickSort1(i, higherIndex);
    }
 
    private void exchangeNumbers(int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
     
    public void quickSort(int arr[], int begin, int end) {
        if (begin < end) {
            int partitionIndex = partition(arr, begin, end);
     
            quickSort(arr, begin, partitionIndex-1);
            quickSort(arr, partitionIndex+1, end);
        }
    }
    private int partition(int arr[], int begin, int end) {
        int pivot = arr[end];
        int i = (begin-1);
     
        for (int j = begin; j < end; j++) {
            if (arr[j] <= pivot) {
                i++;
     
                int swapTemp = arr[i];
                arr[i] = arr[j];
                arr[j] = swapTemp;
            }
        }
     
        int swapTemp = arr[i+1];
        arr[i+1] = arr[end];
        arr[end] = swapTemp;
     
        return i+1;
    }    
    public static void main(String a[]){
         
        MyQuickSort sorter = new MyQuickSort();
        Random r = new Random();
        //int[] input = {24,2,45,20,56,75,2,56,99,53,12};
        int[] input = r.ints(100000).toArray();
        sorter.sort(input);
    }
}