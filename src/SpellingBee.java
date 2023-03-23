import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Spelling Bee
 *
 * This program accepts an input of letters. It prints to an output file
 * all English words that can be generated from those letters.
 *
 * For example: if the user inputs the letters "doggo" the program will generate:
 * do
 * dog
 * doggo
 * go
 * god
 * gog
 * gogo
 * goo
 * good
 *
 * It utilizes recursion to generate the strings, mergesort to sort them, and
 * binary search to find them in a dictionary.
 *
 * @author Zach Blick, Aarav Gupta
 *
 * Written on March 5, 2023 for CS2 @ Menlo School
 *
 * DO NOT MODIFY MAIN OR ANY OF THE METHOD HEADERS.
 */
public class SpellingBee {

    private String letters;
    private ArrayList<String> words;
    public static final int DICTIONARY_SIZE = 143091;
    public static final String[] DICTIONARY = new String[DICTIONARY_SIZE];
    public String[] merged;

    public SpellingBee(String letters) {
        this.letters = letters;
        words = new ArrayList<String>();
    }

    // TODO: generate all possible substrings and permutations of the letters.
    //  Store them all in the ArrayList words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void generate() {
        makeWords("", letters);
    }

    public void makeWords(String start, String finished)
    {
        if (start.length() > 0)
        {
            words.add(start);
        }
        if (finished.length() > 0)
        {
            for (int i = 0; i < finished.length(); i++)
            {
                String newPrefix = start + finished.charAt(i);
                String newWord = finished.substring(0, i) + finished.substring(i + 1);
                makeWords(newPrefix, newWord);
            }
        }
    }


    // TODO: Apply mergesort to sort all words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void sort()
    {
        int length = words.size();
        int med = length / 2;
        ArrayList<String> arr1 = new ArrayList<String>(words.subList(0, med));
        ArrayList<String> arr2 = new ArrayList<String>(words.subList(med, length));
        sortArray(arr1, arr2);
    }

    public String[] sortArray(ArrayList <String> arr1, ArrayList <String> arr2)
    {
        merged = new String[arr1.size() + arr2.size()];
        int i = 0, j = 0;
        while (i < arr1.size() && j < arr2.size())
        {
            if (arr1.get(i).compareTo(arr2.get(i)) < 0 )
            {
                merged[i + j] = arr1.get(i);
                i++;
            }
            else
            {
                merged[i + j] = arr2.get(j);
                j++;
            }
            while (j < arr2.size())
            {
                merged[i + j] = arr2.get(j);
                j++;
            }
            while (i < arr1.size())
            {
                merged[i + j] = arr1.get(i);
                i++;
            }
        }
        return merged;
    }

    // Removes duplicates from the sorted list.
    public void removeDuplicates() {
        int i = 0;
        while (i < words.size() - 1) {
            String word = words.get(i);
            if (word.equals(words.get(i + 1)))
                words.remove(i + 1);
            else
                i++;
        }
    }

    // TODO: For each word in words, use binary search to see if it is in the dictionary.
    //  If it is not in the dictionary, remove it from words.
    public void checkWords() {
        ArrayList<String> finalWords = new ArrayList<>();
        for (String word : words)
        {
            if (binarySearch(word, 0, DICTIONARY_SIZE - 1) != -1)
            {
                finalWords.add(word);
            }
        }
        words = finalWords;
    }

    private int binarySearch(String word, int left, int right)
    {
        if (left > right)
        {
            return -1;
        }
        int mid = (left + right) / 2;
        int comp = word.compareTo(DICTIONARY[mid]);
        if (comp == 0)
        {
            return mid;
        }
        else if (comp < 0)
        {
            return binarySearch(word, left, mid - 1);
        }
        else
        {
            return binarySearch(word, mid + 1, right);
        }
    }


    // Prints all valid words to wordList.txt
    public void printWords() throws IOException {
        File wordFile = new File("Resources/wordList.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(wordFile, false));
        for (String word : words) {
            writer.append(word);
            writer.newLine();
        }
        writer.close();
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public SpellingBee getBee() {
        return this;
    }

    public static void loadDictionary() {
        Scanner s;
        File dictionaryFile = new File("Resources/dictionary.txt");
        try {
            s = new Scanner(dictionaryFile);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open dictionary file.");
            return;
        }
        int i = 0;
        while(s.hasNextLine()) {
            DICTIONARY[i++] = s.nextLine();
        }
    }

    public static void main(String[] args) {

        // Prompt for letters until given only letters.
        Scanner s = new Scanner(System.in);
        String letters;
        do {
            System.out.print("Enter your letters: ");
            letters = s.nextLine();
        }
        while (!letters.matches("[a-zA-Z]+"));

        // Load the dictionary
        SpellingBee.loadDictionary();

        // Generate and print all valid words from those letters.
        SpellingBee sb = new SpellingBee(letters);
        sb.generate();
        sb.sort();
        sb.removeDuplicates();
        sb.checkWords();
        try {
            sb.printWords();
        } catch (IOException e) {
            System.out.println("Could not write to output file.");
        }
        s.close();
    }
}
