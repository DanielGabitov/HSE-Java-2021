package ru.hse.java.trie;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TrieImpl implements Trie{

    private static class Node {
        private final Node[] next = new Node[ALPHABET_SIZE];
        private boolean isTerminal = false;
        private int size = 0;
    }

    private static final int ALPHABET_SIZE = 52;
    private final static Pattern p = Pattern.compile("[A-Za-z]*");
    private final Node root = new Node();

    private static int getNumberByCharacter(char symbol){
        return Character.isLowerCase(symbol) ? (symbol - 'a') : (symbol - 'A' + 26);
    }

    private static char getCharacterByNumber(int number){
        if (number < 26){
            return (char)((int)'a' + number);
        }
        return (char)((int)'A' + number - 26);
    }

    private static void checkString(String word){
        Matcher m = p.matcher(word);
        if (!m.matches()){
            throw new IllegalArgumentException("Illegal argument");
        }
    }

    @Override
    public boolean add(String element){
        checkString(element);
        if (contains(element)){
            return false;
        }
        Node cur = root;
        for (char symbol : element.toCharArray()){
            int symbolNumber = getNumberByCharacter(symbol);
            if (cur.next[symbolNumber] == null){
                cur.next[symbolNumber] = new Node();
            }
            cur.size += 1;
            cur = cur.next[symbolNumber];
        }
        cur.isTerminal = true;
        cur.size += 1;
        return true;
    }

    @Override
    public boolean contains(String element) {
        checkString(element);
        Node cur = root;
        for (char symbol : element.toCharArray()){
            int symbolNumber = getNumberByCharacter(symbol);
            if (cur.next[symbolNumber] == null){
                return false;
            }
            cur = cur.next[symbolNumber];
        }
        return cur.isTerminal;
    }

    @Override
    public boolean remove(String element) {
        if (!contains(element)){
            return false;
        }
        Node cur = root;
        cur.size -= 1;
        for (char symbol : element.toCharArray()){
            int symbolNumber = getNumberByCharacter(symbol);
            Node next = cur.next[symbolNumber];
            if (next.size == 1){
                cur.next[symbolNumber] = null;
                return true;
            }
            next.size -= 1;
            cur = next;
        }
        cur.isTerminal = false;
        return true;
    }

    @Override
    public int size() {
        return root.size;
    }

    @Override
    public int howManyStartsWithPrefix(String prefix) {
        checkString(prefix);
        Node cur = root;
        for (char symbol : prefix.toCharArray()){
            int symbolNumber = getNumberByCharacter(symbol);
            if (cur.next[symbolNumber] == null){
                return 0;
            }
            cur = cur.next[symbolNumber];
        }
        return cur.size;
    }

    @Override
    public String nextString(String element, int k) {
        checkString(element);
        int order = getOrderByString(element) + k;

        if (contains(element)){
            return getStringByOrder(order);
        }
        changeNodesSize(element, true);
        String res = getStringByOrder(order);
        changeNodesSize(element, false);
        return res;
    }

    private void changeNodesSize(String elem, boolean isToIncrease){
        Node cur = root;
        final int deltaSize = (isToIncrease) ? 1 : -1;

        cur.size += deltaSize;
        for (char symbol : elem.toCharArray()){
            int symbolNumber = getNumberByCharacter(symbol);
            if (cur.next[symbolNumber] == null){
                return;
            }
            cur = cur.next[symbolNumber];
            cur.size += deltaSize;
        }
        cur.isTerminal = isToIncrease;
    }

    private String getStringByOrder(int order){
        StringBuilder word = new StringBuilder();
        int cur = 0;
        Node node = root;
        boolean isLetterFound = true;

        while (isLetterFound){
            isLetterFound = false;
            if (node.isTerminal){
                cur += 1;
            }
            if (cur == order && node.isTerminal){
                return word.toString();
            }
            for (int i = 0; i < node.next.length; i++){
                Node next = node.next[i];
                if (next == null){
                    continue;
                }
                if (cur + next.size >= order){
                    word.append(getCharacterByNumber(i));
                    isLetterFound = true;
                    node = next;
                    break;
                }
                cur += next.size;
            }
        }
        return null;
    }

    private int getOrderByString(String element){
        if (element.isEmpty()){
            return 1;
        }
        Node cur = root;
        int order = 1;
        for (char symbol : element.toCharArray()){
            int symbolNumber = getNumberByCharacter(symbol);
            for (int i = 0; i < symbolNumber; i++){
                Node next = cur.next[i];
                if (next == null){
                    continue;
                }
                order += next.size;
            }
            if (cur.isTerminal){
                order += 1;
            }
            if (cur.next[symbolNumber] == null){
                return order;
            }
            cur = cur.next[symbolNumber];
        }
        return order;
    }
}
