package com.lazer;

import com.lazer.lab1.DynamicPerfectHashTable;
import com.lazer.lab1.ExtendibleHashing;

import java.io.IOException;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        DynamicPerfectHashTable hashTable = new DynamicPerfectHashTable(10);

        hashTable.insert("10");
        hashTable.insert("22");
        hashTable.insert("37");
        hashTable.insert("40");
        hashTable.insert("52");

        System.out.println(hashTable.contains("10"));
        System.out.println(hashTable.contains("22"));
        System.out.println(hashTable.contains("37"));
        System.out.println(hashTable.contains("40"));
        System.out.println(hashTable.contains("52"));

        System.out.println(hashTable.contains("85"));

        hashTable.remove("40");
        System.out.println(hashTable.contains("40"));

        hashTable.insert("85");
        System.out.println(hashTable.contains("85"));


//        ExtendibleHashing hashTable = new ExtendibleHashing(2);
//
//        hashTable.insert("10");
//        hashTable.insert("22");
//        hashTable.insert("37");
//        hashTable.insert("40");
//        hashTable.insert("52");
//        hashTable.insert("60");
//        hashTable.insert("75");
//        hashTable.insert("85");
//
//        hashTable.print();
//
//        System.out.println("Search 40: " + hashTable.search("40"));
//        System.out.println("Search 99: " + hashTable.search("99"));
//
//        hashTable.delete("40");
//        System.out.println("After deleting 40:");
//        hashTable.print();
    }
}