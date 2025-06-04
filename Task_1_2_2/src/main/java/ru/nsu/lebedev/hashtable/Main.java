package ru.nsu.lebedev.hashtable;


/**
 * Main class.
 */
public class Main {
    /**
     * Main function.
     *
     * @param args for specified parameters.
     */
    public static void main(String[] args) {
        HashTable<String, Number> hashTable = new HashTable<>();
        hashTable.put("one", 1);
        hashTable.update("one", 1.0);
        System.out.println(hashTable.get("one"));
        var removeValue = hashTable.remove("one");
        System.out.println(removeValue);
        System.out.println(hashTable.get("one"));
        System.out.println(hashTable);
        hashTable.put("one", 1);
        hashTable.put("two", 2);
        System.out.println(hashTable);
        for (HashTableEntry<String, Number> entry : hashTable) {
            System.out.println(entry);
        }
    }
}