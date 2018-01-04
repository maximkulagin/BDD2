package model;

import java.util.ArrayList;

public class Controller {
    private TruthTable truthTable;
    private Tree tree, orderedTree;
    private int depth; //глубина дерева или тоже самое что кол-во переменных
    private String st_tree, short_tree, ordered_tree, short_ordered_tree; //представление дерева в виде строки. По этой строке на стороне клиента будет рисоваться дерево
    private String st_func; //функция которую ввел клиент
    private ArrayList<Tree> x1, x2, x3, x4, x5, x6, x7, garbage;

    public Controller() {
        truthTable = new TruthTable();
        tree = new Tree("", "");
        x1 = new ArrayList<>();
        x2 = new ArrayList<>();
        x3 = new ArrayList<>();
        x4 = new ArrayList<>();
        x5 = new ArrayList<>();
        x6 = new ArrayList<>();
        x7 = new ArrayList<>();
        garbage = new ArrayList<>();
    }

    public String getShort_ordered_tree() {
        return short_ordered_tree;
    }

    public void setShort_ordered_tree(String short_ordered_tree) {
        this.short_ordered_tree = short_ordered_tree;
    }

    public String getSt_func() {
        return st_func;
    }

    public void setSt_func(String st_func) {
        this.st_func = st_func;
    }

    public String getOrdered_tree() {
        if (orderedTree != null) orderedTree.getFullDecription();
        return ordered_tree;
    }

    public void setOrdered_tree(String ordered_tree) {
        this.ordered_tree = ordered_tree;
    }

    public TruthTable getTruthTable() {
        return truthTable;
    }

    public void setTruthTable(TruthTable truthTable) {
        this.truthTable = truthTable;
    }

    public String getSt_tree() {
        if (tree != null) st_tree = tree.toString();
        return st_tree;
    }

    public void setSt_tree(String st_tree) {
        this.st_tree = st_tree;
    }

    public void makeTable() {
        truthTable.setSt_func(st_func);
        depth = truthTable.fillUp();
    }

    public void startMakingTree() {
        makeTree(tree, 0);
    }

    private void makeTree(Tree tree, int step) {
        if (step < depth) {
            ++step;
            Tree LeftChild = new Tree(tree.getHistory() + "1", "x" + step);
            Tree RightChild = new Tree(tree.getHistory() + "0", "x" + step);

            tree.setRightChild(RightChild);
            tree.setLeftChild(LeftChild);
            LeftChild.setParent(tree);
            RightChild.setParent(tree);

            makeTree(LeftChild, step);
            makeTree(RightChild, step);
        } else {
            int res = Integer.parseInt(tree.getHistory(), 2);
            tree.setFuncResult(truthTable.getRows().get(res)[depth]);
        }
    }

    private void clearLists() {
        x1.clear();
        x2.clear();
        x3.clear();
        x4.clear();
        x5.clear();
        x6.clear();
        x7.clear();
    }

    public void makeOrderedTree() {
        clearLists();
        orderedTree = tree.clone();
        orderedTree.setName("x0");
        rename(orderedTree);
        x1.add(orderedTree);
        treeToLists(orderedTree);
        switch (depth) {
            case 2:
                mergeLeaves(x3);
                break;
            case 3:
                mergeLeaves(x4);
                break;
            case 4:
                mergeLeaves(x5);
                break;
            case 5:
                mergeLeaves(x6);
                break;
            case 6:
                mergeLeaves(x7);
                break;
        }
        switch (depth) {
            case 6:
                mergeNodes(x6);
            case 5:
                mergeNodes(x5);
            case 4:
                mergeNodes(x4);
            case 3:
                mergeNodes(x3);
            case 2:
                mergeNodes(x2);
        }
        ordered_tree = orderedTree.getFullDecription();
        orderedTree.setIsVisetedForAllFalse();
        switch (depth){
            case 6:
                makeOrderedTreeBetter(x5);
            case 5:
                makeOrderedTreeBetter(x4);
            case 4:
                makeOrderedTreeBetter(x3);
            case 3:
                makeOrderedTreeBetter(x2);
            case 2:
                makeOrderedTreeBetter(x1);
        }
        short_ordered_tree = orderedTree.getFullDecription();
    }

    private void makeOrderedTreeBetter(ArrayList<Tree> nodes) {
        for (Tree node : nodes) {
            if (node.getRightChild().getLeftChild() == node.getRightChild().getRightChild()) {
                node.setRightChild(node.getRightChild().getLeftChild());
            }
            if (node.getLeftChild().getLeftChild() == node.getLeftChild().getRightChild()) {
                node.setLeftChild(node.getLeftChild().getLeftChild());
            }
        }
    }

    private void mergeLeaves(ArrayList<Tree> leaves) {
        for (int i = 0; i < leaves.size() - 1; i++) {
            for (int j = i + 1; j <= leaves.size() - 1; j++) {
                if (leaves.get(i).getFuncResult().booleanValue() == leaves.get(j).getFuncResult().booleanValue()) {
                    if (i % 2 == 0) {
                        leaves.get(i).getParent().setLeftChild(leaves.get(j));
                    } else {
                        leaves.get(i).getParent().setRightChild(leaves.get(j));
                    }
                    garbage.add(leaves.get(i));
                }
            }
        }
        clearGarbage(leaves);
    }

    private void mergeNodes(ArrayList<Tree> nodes) {
        for (int i = 0; i < nodes.size() - 1; i++) {
            for (int j = i + 1; j < nodes.size(); j++) {
                if (nodes.get(i).getLeftChild() == nodes.get(j).getLeftChild()
                        && nodes.get(i).getRightChild() == nodes.get(j).getRightChild()) {
                    if (i % 2 == 0) {
                        nodes.get(i).getParent().setLeftChild(nodes.get(j));
                    } else {
                        nodes.get(i).getParent().setRightChild(nodes.get(j));
                    }
                    garbage.add(nodes.get(i));
                }
            }
        }
        clearGarbage(nodes);
    }

    private void clearGarbage(ArrayList<Tree> nodes) {
        for (Tree tree : garbage) {
            nodes.remove(tree);
        }
        garbage.clear();
    }

    private void rename(Tree tree) {
        if (tree.getFuncResult() == null) {
            int n = Integer.parseInt(tree.getName().charAt(tree.getName().length() - 1) + "");
            ++n;
            tree.setName("x" + n);
            rename(tree.getLeftChild());
            rename(tree.getRightChild());
        } else {
            tree.setName("result");
        }

    }

    private void treeToLists(Tree tree) {
        switch (tree.getName()) {
            case "x1":
                if (tree.getLeftChild() != null) x2.add(tree.getLeftChild());
                if (tree.getRightChild() != null) x2.add(tree.getRightChild());
                break;
            case "x2":
                if (tree.getLeftChild() != null) x3.add(tree.getLeftChild());
                if (tree.getRightChild() != null) x3.add(tree.getRightChild());
                break;
            case "x3":
                if (tree.getLeftChild() != null) x4.add(tree.getLeftChild());
                if (tree.getRightChild() != null) x4.add(tree.getRightChild());
                break;
            case "x4":
                if (tree.getLeftChild() != null) x5.add(tree.getLeftChild());
                if (tree.getRightChild() != null) x5.add(tree.getRightChild());
                break;
            case "x5":
                if (tree.getLeftChild() != null) x6.add(tree.getLeftChild());
                if (tree.getRightChild() != null) x6.add(tree.getRightChild());
                break;
            case "x6":
                if (tree.getLeftChild() != null) x7.add(tree.getLeftChild());
                if (tree.getRightChild() != null) x7.add(tree.getRightChild());
                break;
        }
        if (tree.getLeftChild() != null) treeToLists(tree.getLeftChild());
        if (tree.getRightChild() != null) treeToLists(tree.getRightChild());
    }
}
