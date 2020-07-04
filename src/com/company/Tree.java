package com.company;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayDeque;

public class Tree<T> {
    private int height = 0;
    private TreeNode<T> root;
    private final Comparator<T> comparator;

    Tree(Comparator<T> comparator) {
        root = new TreeNode<>(null, null, null);
        this.comparator = comparator;
    }

    public int height() {
        return height;
    }

    public void add(T newElement) {
        if (root.value == null) {
            root.value = newElement;
        } else if (height == 0) {
            if (comparator.compare(newElement, root.value)) {
                root.left = new TreeNode<>(newElement, null, null);
            } else {
                root.right = new TreeNode<>(newElement, null, null);
            }
            height++;
        } else {
            TreeNode<T> i = root;
            int j = 0;
            while (true) {
                j++;
                if (i.left != null && comparator.compare(newElement, i.value)) {
                    i = i.left;
                } else if (i.left == null && comparator.compare(newElement, i.value)) {
                    i.left = new TreeNode<>(newElement, null, null);
                    if (j > height) {
                        height = j;
                    }
                    break;
                } else if (i.right != null) {
                    i = i.right;
                } else {
                    i.right = new TreeNode<>(newElement, null, null);
                    if (j > height) {
                        height = j;
                    }
                    break;
                }
            }
        }
    }

    public String print() {
        StringBuilder builder = new StringBuilder();
        builder.append(root.value).append(" (");
        if (root.left != null) {
            builder.append(root.left.value);
            print(root.left, builder);
        }
        builder.append(", ");
        if (root.right != null) {
            builder.append(root.right.value);
            print(root.right, builder);
        }
        builder.append(")");
        return builder.toString();
    }

    private void print(TreeNode<T> link, StringBuilder builder) {
        if (link.left != null || link.right != null) {
            builder.append(" (");
            if (link.left != null) {
                builder.append(link.left.value);
                print(link.left, builder);
            }
            builder.append(", ");
            if (link.right != null) {
                builder.append(link.right.value);
                print(link.right, builder);
            }
            builder.append(")");
        }
    }

    public static Tree<Integer> intTree(int N) {
        ArrayDeque<TreeNode<Integer>> deque = new ArrayDeque<>();
        for (int i = N; i >= 1; i--) {
            for (int j = 0; j < Math.pow(2, i - 1); j++) {
                if (i == N) {
                    deque.add(new TreeNode<>(i, null, null));
                } else {
                    TreeNode<Integer> left = deque.pollFirst();
                    TreeNode<Integer> right = deque.pollFirst();
                    deque.add(new TreeNode<>(i, left, right));
                }
            }
        }
        Tree<Integer> tree = new Tree<>((first, second) -> first < second);
        tree.root = deque.pollFirst();
        tree.height = N - 1;
        return tree;
    }

    interface Comparator<T> {
        boolean compare(T first, T second);
    }

    private static class TreeNode<T> {
        T value;
        TreeNode<T> left;
        TreeNode<T> right;

        TreeNode(T value, TreeNode<T> left, TreeNode<T> right) {
            this.left = left;
            this.right = right;
            this.value = value;
        }
    }

    public boolean contains(T element) {
        TreeNode<T> node = root;
        boolean i = true;
        while (i) {
            i = false;
            if (node != null && !comparator.compare(element, node.value)) {
                node = node.right;
                i = true;
            } else if (node != null && comparator.compare(element, node.value)) {
                node = node.left;
                i = true;
            } else if (node != null && node.value.equals(element)) {
                return true;
            }
        }
        return false;
    }

    private static class TreeNodePaint extends JComponent {
        String value;

        TreeNodePaint(int x, int y, String value) {
            this.value = value;
            setLocation(x, y);
            setSize(50, 50);
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            g.setColor(new Color(255, 255, 255));
            g.fillRect(0, 0, 50, 50);
            g.setColor(new Color(0, 0, 0));
            g.setFont(new Font("Lol", Font.BOLD, 20));
            g.drawString(value, 25 - value.length() * 5, 30);
            g.drawLine(0, 0, 0, 50);
            g.drawLine(49, 0, 49, 50);
            g.drawLine(0, 0, 49, 0);
            g.drawLine(0, 49, 49, 49);
        }
    }

    public static class TreePaint<T> extends JComponent {
        private final int height;

        TreePaint(Tree<T> tree) {
            this.height = tree.height;
            int[] coordinates = new int[tree.height + 1];
            for (int v : coordinates) {
                v = 0;
            }
            paint(tree.root, 0, coordinates);
        }

        int[][][] coordinates() {
            int[][][] coordinates = new int[height + 1][][];
            int[][] coo = new int[(int) Math.pow(2, height)][2];
            for (int i = 0; i < Math.pow(2, height); i++) {
                coo[i][0] = 10 + i * 60;
                coo[i][1] = 60 * height + 10;
            }
            coordinates[height] = coo;
            for (int j = height - 1; j >= 0; j--) {
                int[][] co = new int[(int) Math.pow(2, j)][2];
                for (int i = 0; i < Math.pow(2, j); i++) {
                    co[i][0] = (coordinates[j + 1][2 * i + 1][0] + 50 - coordinates[j + 1][2 * i][0]) / 2 - 25 + coordinates[j + 1][2 * i][0];
                    co[i][1] = 60 * j + 10;
                }
                coordinates[j] = co;
            }
            return coordinates;
        }

        public void paint(TreeNode<T> link, int level, int[] coordinates) {
            TreeNodePaint nodePaint = new TreeNodePaint(
                    coordinates()[level][coordinates[level]][0],
                    coordinates()[level][coordinates[level]++][1],
                    String.valueOf(link.value)
            );
            add(nodePaint);
            if (link.left != null) {
                paint(link.left, level + 1, coordinates);
            } else {
                for (int i = 0; i < height - level; i++) {
                    for (int j = 0; j < Math.pow(2, i); j++) {
                        coordinates[level + 1 + i]++;
                    }
                }
            }
            if (link.right != null) {
                paint(link.right, level + 1, coordinates);
            } else {
                for (int i = 0; i < height - level; i++) {
                    for (int j = 0; j < Math.pow(2, i); j++) {
                        coordinates[level + 1 + i]++;
                    }
                }
            }
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            int level = 0;
            while (level < height) {
                for (int i = 0; i < Math.pow(2, level); i++) {
                    if (getComponentAt(coordinates()[level][i][0], coordinates()[level][i][1]) != null) {
                        if (getComponentAt(coordinates()[level + 1][2 * i][0], coordinates()[level + 1][2 * i][1]) != null &&
                                getComponentAt(coordinates()[level + 1][2 * i][0], coordinates()[level + 1][2 * i][1]) instanceof TreeNodePaint
                        ) {

                            g.drawLine(
                                    coordinates()[level][i][0] + 25,
                                    coordinates()[level][i][1] + 50,
                                    coordinates()[level + 1][2 * i][0] + 25,
                                    coordinates()[level + 1][2 * i][1]
                            );
                        }
                        if (getComponentAt(coordinates()[level + 1][2 * i + 1][0], coordinates()[level + 1][2 * i + 1][1]) != null &&
                                getComponentAt(coordinates()[level + 1][2 * i + 1][0], coordinates()[level + 1][2 * i + 1][1]) instanceof TreeNodePaint
                        ) {
                            g.drawLine(
                                    coordinates()[level][i][0] + 25,
                                    coordinates()[level][i][1] + 50,
                                    coordinates()[level + 1][2 * i + 1][0] + 25,
                                    coordinates()[level + 1][2 * i + 1][1]
                            );
                        }
                    }
                }
                level++;
            }
        }
    }
}
