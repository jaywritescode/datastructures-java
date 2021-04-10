package datastructures;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TrieTest {

    @Nested
    @DisplayName("#add")
    class Add {
        @Test
        @DisplayName("words with no common prefix")
        void noPrefix() throws Exception {
            var trie = new Trie();
            var words = List.of("their", "follow", "car");

            words.forEach(word -> assertThat(trie.add(word)).as("add \"%s\"", word).isTrue());

            var expectedSize = words.stream().mapToInt(String::length).sum() + 1;
            assertThat(size(trie)).isEqualTo(expectedSize);
        }

        @Test
        @DisplayName("common prefix in trie")
        void commonPrefix() throws Exception {
            var trie = new Trie();

            var node = getRoot(trie);
            node.children.put('d', node = trie.new Node());
            node.children.put('a', node = trie.new Node());
            node.children.put('r', node = trie.new Node());
            node.children.put('k', node = trie.new Node());
            node.isTerminus = true;

            var words = List.of("darkness", "darken", "darker", "darkened");

            words.forEach(word -> assertThat(trie.add(word)).as("add \"%s\"", word).isTrue());

            var expectedSize = 14;  // number of unique (letter, index) pairs in word list, including one for (𝜀, 0)
            assertThat(size(trie)).isEqualTo(expectedSize);
        }

        @Test
        @DisplayName("add word that is a prefix of a word in trie")
        void isPrefix() throws Exception {
            var trie = new Trie();

            var node = getRoot(trie);
            node.children.put('m', node = trie.new Node());
            node.children.put('e', node = trie.new Node());
            node.children.put('t', node = trie.new Node());
            node.children.put('a', node = trie.new Node());
            node.children.put('l', node = trie.new Node());
            node.isTerminus = true;

            var words = List.of("me", "met", "meta");

            words.forEach(word -> assertThat(trie.add(word)).as("add \"%s\"", word).isTrue());
            assertThat(size(trie)).isEqualTo("metal".length() + 1);
        }
    }

    @Test
    @DisplayName("#contains")
    void contains() throws Exception {
        Trie trie = new Trie();
        var words = List.of("cat", "freedom", "catchy", "free");

        words.forEach(trie::add);

        words.forEach(word -> assertThat(trie.contains(word)).as("contains \"%s\"", word).isTrue());
        assertThat(trie.contains("catch")).as("does not contain \"%s\"", "catch").isFalse();
        assertThat(trie.contains("freed")).as("does not contain \"%s\"", "freed").isFalse();
    }

    static Trie.Node getRoot(Trie trie) throws Exception {
        Field field = Trie.class.getDeclaredField("root");
        return (Trie.Node) field.get(trie);
    }

    static int size(Trie trie) throws Exception {
        return size(getRoot(trie));
    }

    private static int size(Trie.Node node) {
        var children = node.children.values();
        var descendantCount = children.stream().mapToInt(TrieTest::size).sum();

        return 1 + descendantCount;
    }
}