package autocomplete;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Ternary search tree (TST) implementation of the {@link Autocomplete} interface.
 *
 * @see Autocomplete
 */
public class TernarySearchTreeAutocomplete implements Autocomplete {
    /**
     * The overall root of the tree: the first character of the first autocompletion term added to this tree.
     */
    private Node overallRoot;

    /**
     * Constructs an empty instance.
     */
    public TernarySearchTreeAutocomplete() {
        overallRoot = null;
    }

    @Override
    public void addAll(Collection<? extends CharSequence> terms) {
        for (CharSequence term : terms) {
            put(term);
        }
    }

    private void put(CharSequence term) {
        if (term == null) {
            throw new IllegalArgumentException("calls put() with null term");
        }
        overallRoot = put(overallRoot, term, 0);
    }

    private Node put(Node x, CharSequence val, int d) {
        char c = val.charAt(d);

        if (x == null) {
            x = new Node(c);
        }

        if      (c < x.data)           x.left = put(x.left, val, d);
        else if (c > x.data)           x.right = put(x.right, val, d);
        else if (d < val.length() - 1) x.mid = put(x.mid, val, d + 1);
        else                           x.isTerm = true;

        return x;
    }
    @Override
    public List<CharSequence> allMatches(CharSequence prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException("calls allMatches() with null argument");
        }

        ArrayList<CharSequence> result = new ArrayList<CharSequence>();
        Node x = get(overallRoot, prefix, 0);

        if (x == null) {
            return result;
        }
        if (x.isTerm) {
            result.add(prefix);
        }

        collect(x.mid, new StringBuilder(prefix), result);
        return result;
    }

    private Node get(Node x, CharSequence prefix, int d) {
        if (prefix.length() == 0) {
            throw new IllegalArgumentException("prefix must have length >= 1");
        }
        if (x == null) {
            return null;
        }

        char c = prefix.charAt(d);

        if      (c < x.data)              return get(x.left, prefix, d);
        else if (c > x.data)              return get(x.right, prefix, d);
        else if (d < prefix.length() - 1) return get(x.mid, prefix, d + 1);
        else                              return x;

    }

    private void collect(Node x, StringBuilder prefix, ArrayList<CharSequence> result) {
        if (x == null) {
            return;
        }

        collect(x.left, prefix, result);

        if (x.isTerm) {
            result.add(prefix.toString() + x.data);
        }

        collect(x.mid, prefix.append(x.data), result);
        prefix.deleteCharAt(prefix.length() - 1);
        collect(x.right, prefix, result);
    }

    /**
     * A search tree node representing a single character in an autocompletion term.
     */
    private static class Node {
        private final char data;
        private boolean isTerm;
        private Node left;
        private Node mid;
        private Node right;

        public Node(char data) {
            this.data = data;
            this.isTerm = false;
            this.left = null;
            this.mid = null;
            this.right = null;
        }
    }
}
