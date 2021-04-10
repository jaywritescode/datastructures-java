package datastructures;

import org.assertj.core.api.AutoCloseableSoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;
import java.util.function.BiPredicate;

public class DisjointSetTest {

    Set<String> elements = Set.of("aardvark", "bison", "cougar", "dolphin", "eagle", "eel", "elephant", "elk");

    BiPredicate<String, String> R = (i, j) -> i.charAt(0) == j.charAt(0);

    @Test
    @DisplayName("it #unions and #finds")
    void testUnionAndFind() throws Exception {
        DisjointSet<String> disjointSet = new DisjointSet<>(elements);

        disjointSet.
                union("eagle", "eel").
                union("elephant", "elk").
                union("eagle", "elephant");

        try (AutoCloseableSoftAssertions softly = new AutoCloseableSoftAssertions()) {
            for (String i : elements) {
                for (String j : elements) {
                    softly.assertThat(disjointSet.sameEquivalenceClass(i, j))
                            .as("%s %s %s", i, R.test(i, j) ? "∼" : "≁", j)
                            .isEqualTo(i.charAt(0) == j.charAt(0));
                }
            }
        }
    }

    @Test
    @DisplayName("union by height")
    void testUnionByHeight() throws Exception {
        DisjointSet<String> disjointSet = new DisjointSet<>(elements);

        Field indices = DisjointSet.class.getDeclaredField("indices");
        indices.setAccessible(true);
        indices.set(disjointSet, Map.of(
                "aardvark", 0,
                "bison", 1,
                "cougar", 2,
                "dolphin", 3,
                "eagle", 4,
                "eel", 5,
                "elephant", 6,
                "elk", 7));

        disjointSet.
                union("eagle", "eel").
                union("elephant", "elk").
                union("eagle", "elephant").
                union("dolphin", "elephant");

        var answersInOrder = new int[] {-1, -1, -1, 4, -3, 4, 4, 6};
        try (AutoCloseableSoftAssertions softly = new AutoCloseableSoftAssertions()) {
            for (var i = 0; i < answersInOrder.length; ++i) {
                var expected = answersInOrder[i];
                softly.assertThat(disjointSet.forest[i])
                        .as("Expecting index %s to be %s.", i, expected)
                        .isEqualTo(expected);
            }
        }
    }
}
