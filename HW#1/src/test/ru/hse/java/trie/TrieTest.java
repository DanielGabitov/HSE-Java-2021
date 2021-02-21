package ru.hse.java.trie;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Random;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

class TrieTest {

    @Test
    void testAddStress() {
        TrieImpl testTrie = new TrieImpl();

        for (int i = 0; i < 25_000; i++){
            assertTrue(testTrie.add("a".repeat(i)));
        }

        assertTrue(testTrie.add("A".repeat(100_000)));
    }

    @Test
    void testAdd() {
        TrieImpl testTrie = new TrieImpl();

        assertTrue(testTrie.add("abaa"));
        assertFalse(testTrie.add("abaa"));

        assertTrue(testTrie.add(""));

        assertTrue(testTrie.add("Hse"));
        assertTrue(testTrie.add("hse"));
    }

    @Test
    void testException() {
        TrieImpl testTrie = new TrieImpl();

        assertThrows(
                IllegalArgumentException.class,
                () -> testTrie.add("Ошибка")
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> testTrie.add(" ")
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> testTrie.add("There is a whitespace!")
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> testTrie.add("@")
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> testTrie.add("H_S_E")
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> testTrie.add("HSE;")
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> testTrie.add("Кириллица")
        );
    }

    @Test
    void testContainsStress() {
        TrieImpl testTrie = new TrieImpl();

        StringBuilder word = new StringBuilder();
        boolean is_lower = true;
        for (int i = 0; i < 25_000; i++){
            if (is_lower){
                word.append((char)('a' + (i % 26)));
                is_lower = false;
            } else {
                word.append((char)('A' + (i % 26)));
                is_lower = true;
            }
            assertFalse(testTrie.contains(word.toString()));
            testTrie.add(word.toString());
            assertTrue(testTrie.contains(word.toString()));
        }
    }

    @Test
    void testContains() {
        TrieImpl testTrie = new TrieImpl();

        assertFalse(testTrie.contains("abracadabra"));
        testTrie.add("abracadabra");
        assertTrue(testTrie.contains("abracadabra"));
        assertFalse(testTrie.contains("ABRACADABRA"));
        assertFalse(testTrie.contains("aBRACADABRA"));
        assertFalse(testTrie.contains("Abracadabra"));
        assertFalse(testTrie.contains("AbRaCaDaBrA"));
        testTrie.add("AbRaCaDaBrA");
        assertTrue(testTrie.contains("AbRaCaDaBrA"));
        assertFalse(testTrie.contains("aBrAcAdAbRa"));

        assertFalse(testTrie.contains(""));
        testTrie.add("");
        assertTrue(testTrie.contains(""));
    }

    @Test
    void testRemoveStress() {
        TrieImpl testTrie = new TrieImpl();
        StringBuilder word = new StringBuilder();

        boolean is_lower = true;
        for (int i = 0; i < 25_000; i++){
            if (is_lower){
                word.append((char)('a' + (i % 26)));
                is_lower = false;
            } else {
                word.append((char)('A' + (i % 26)));
                is_lower = true;
            }
            testTrie.add(word.toString());
        }

        word = new StringBuilder();
        is_lower = true;
        for (int i = 0; i < 25_000; i++){
            if (is_lower){
                word.append((char)('a' + (i % 26)));
                is_lower = false;
            } else {
                word.append((char)('A' + (i % 26)));
                is_lower = true;
            }
            assertTrue(testTrie.contains(word.toString()));
            assertTrue(testTrie.remove(word.toString()));
            assertFalse(testTrie.contains(word.toString()));
        }
    }

    @Test
    void testRemove() {
        TrieImpl testTrie = new TrieImpl();

        assertFalse(testTrie.remove(""));
        testTrie.add("");
        assertTrue(testTrie.remove(""));
        assertFalse(testTrie.contains(""));
        assertFalse(testTrie.remove(""));

        testTrie.add("JavaIsAwesome");
        assertTrue(testTrie.remove("JavaIsAwesome"));
        assertFalse(testTrie.remove("JavaIsAwesome"));
        assertFalse(testTrie.contains("JavaIsAwesome"));
    }

    @Test
    void testSizeStress() {
        TrieImpl testTrie = new TrieImpl();
        StringBuilder word = new StringBuilder();

        boolean is_lower = true;
        for (int i = 0; i < 25_000; i++){
            if (is_lower){
                word.append((char)('a' + (i % 26)));
                is_lower = false;
            } else {
                word.append((char)('A' + (i % 26)));
                is_lower = true;
            }
            testTrie.add(word.toString());
            assertEquals(i + 1, testTrie.size());
        }
    }

    @Test
    void testSize()  {
        TrieImpl testTrie = new TrieImpl();
        assertEquals(0, testTrie.size());
        testTrie.add("");
        assertEquals(1, testTrie.size());
        testTrie.remove("");
        assertEquals(0, testTrie.size());
        testTrie.add("a");
        testTrie.add("aa");
        testTrie.add("aaa");
        testTrie.add("aaa");
        assertEquals(3, testTrie.size());
        testTrie.remove("a");
        testTrie.remove("aa");
        testTrie.remove("aaa");
        assertEquals(0, testTrie.size());
    }

    @Test
    void testHowManyStartsWithPrefix() {
        TrieImpl testTrie = new TrieImpl();
        assertEquals(0, testTrie.howManyStartsWithPrefix(""));
        testTrie.add("");
        assertEquals(1, testTrie.howManyStartsWithPrefix(""));
        testTrie.remove("");
        assertEquals(0, testTrie.howManyStartsWithPrefix(""));

        testTrie.add("a");
        testTrie.add("a");
        assertEquals(1, testTrie.howManyStartsWithPrefix("a"));
        testTrie.add("aa");
        testTrie.add("aaa");
        testTrie.add("abba");
        testTrie.add("SaintP");
        assertEquals(4, testTrie.howManyStartsWithPrefix("a"));
        assertEquals(2, testTrie.howManyStartsWithPrefix("aa"));
        assertEquals(5, testTrie.howManyStartsWithPrefix(""));
        assertEquals(0, testTrie.howManyStartsWithPrefix("A"));
    }


    @Test
    void testNextStringIfStringExists() {
        TrieImpl testTrie = new TrieImpl();
        testTrie.add("");
        testTrie.add("ab");
        testTrie.add("abaa");
        testTrie.add("abb");
        testTrie.add("c");
        testTrie.add("ca");
        testTrie.add("cb");
        testTrie.add("ccc");

        assertEquals("", testTrie.nextString("", 0));
        assertEquals("ab", testTrie.nextString("ab", 0));
        assertEquals("abaa", testTrie.nextString("abaa", 0));
        assertEquals("abb", testTrie.nextString("abb", 0));
        assertEquals("c", testTrie.nextString("c", 0));
        assertEquals("ca", testTrie.nextString("ca", 0));
        assertEquals("cb", testTrie.nextString("cb", 0));
        assertEquals("ccc", testTrie.nextString("ccc", 0));

        assertEquals("ab", testTrie.nextString("", 1));
        assertEquals("abaa", testTrie.nextString("", 2));
        assertEquals("abb", testTrie.nextString("", 3));
        assertEquals("c", testTrie.nextString("", 4));
        assertEquals("ca", testTrie.nextString("", 5));
        assertEquals("cb", testTrie.nextString("", 6));
        assertEquals( "ccc", testTrie.nextString("", 7));
        assertNull(testTrie.nextString("", 8));
        assertNull(testTrie.nextString("ccc", 1));

        assertEquals("cb", testTrie.nextString("abaa", 4));
        assertEquals("c", testTrie.nextString("abb", 1));
        assertEquals("cb", testTrie.nextString("ca", 1));
    }


    @Test
    void testNextStringIfStringDoNotExists() {
        TrieImpl testTrie = new TrieImpl();
        testTrie.add("");
        testTrie.add("ab");
        testTrie.add("abaa");
        testTrie.add("abb");
        testTrie.add("c");
        testTrie.add("ca");
        testTrie.add("cb");
        testTrie.add("ccc");

        assertNull(testTrie.nextString("abaaa", 0));
        assertFalse(testTrie.contains("abaaa"));

        assertEquals("ca", testTrie.nextString("abaaa", 3));
        assertFalse(testTrie.contains("abaaa"));

        assertNull(testTrie.nextString("cccc", 1));
        assertFalse(testTrie.contains("cccc"));

        testTrie.remove("");
        assertEquals("ccc", testTrie.nextString("", 7));
        assertEquals("ab", testTrie.nextString("", 1));
        assertEquals("abb", testTrie.nextString("", 3));
        assertEquals("ca", testTrie.nextString("", 5));

        testTrie = new TrieImpl();
        testTrie.add("A");
        testTrie.add("a");
        assertEquals("A", testTrie.nextString("a", 1));
        assertNull(testTrie.nextString("A", 1));
    }

    @Test
    void testNextStringCase3() {
        TrieImpl testTrie = new TrieImpl();

        testTrie.add("abb");
        assertEquals("abb", testTrie.nextString("ab", 1));
        assertNull(testTrie.nextString("ZZZ", 0));
        assertNull(testTrie.nextString("ZZZ", 1));

        testTrie.add("aA");
        testTrie.add("aB");
        assertEquals("aB", testTrie.nextString("abb", 2));
        assertEquals("aA", testTrie.nextString("abb", 1));

        testTrie.add("");
        testTrie.add("aaa");
        testTrie.add("AAA");
        assertEquals("AAA", testTrie.nextString("aaa", 4));
        assertEquals("aaa", testTrie.nextString("a", 1));
        assertEquals("abb", testTrie.nextString("a", 2));
        assertEquals("aA", testTrie.nextString("a", 3));
        assertEquals("aB", testTrie.nextString("a", 4));
        assertEquals("AAA", testTrie.nextString("a", 5));
    }

    @Test
    void testTrieWithSet() {
        TrieImpl testTrie = new TrieImpl();
        TreeSet<String> set = new TreeSet<>();
        String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        Random rnd = new Random();
        for (int i = 0; i < 10_000_000; i++){
            StringBuilder testWord = new StringBuilder();
            for (int j = 0; j < rnd.nextInt(5); j++){
                testWord.append(letters.charAt(rnd.nextInt(52)));
            }
            String word = testWord.toString();
            assertEquals(set.contains(word), testTrie.contains(word));
            if (i % 5 == 0){
                assertEquals(set.remove(word), testTrie.remove(word));
            } else {
                assertEquals(set.add(word), testTrie.add(word));
            }
            assertEquals(set.size(), testTrie.size());
            assertEquals(set.contains(word), testTrie.contains(word));
        }
    }
}