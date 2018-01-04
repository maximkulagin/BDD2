package model;

public class Tree {
    private boolean isVisited;
    private Boolean FuncResult = null;
    private Tree Parent, LeftChild, RightChild;
    private String name, history;


    Tree() {
    }

    Tree(String history, String name) {
        isVisited = false;
        this.history = history;
        this.name = name;
    }


    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public Boolean getFuncResult() {
        return FuncResult;
    }

    public void setFuncResult(Boolean funcResult) {
        FuncResult = funcResult;
    }

    public Tree getParent() {
        return Parent;
    }

    public void setParent(Tree parent) {
        Parent = parent;
    }

    public Tree getLeftChild() {
        return LeftChild;
    }

    public void setLeftChild(Tree leftChild) {
        LeftChild = leftChild;
    }

    public Tree getRightChild() {
        return RightChild;
    }

    public void setRightChild(Tree rightChild) {
        RightChild = rightChild;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIsVisetedForAllFalse() {
        isVisited = false;
        if (LeftChild != null) LeftChild.setIsVisetedForAllFalse();
        if (RightChild != null) RightChild.setIsVisetedForAllFalse();
    }

    public String getFullDecription() {
        String st_tree = new String(name + ":" + history + ":");
        if (LeftChild.FuncResult == null) {
            st_tree += LeftChild.getHistory() + ":";
        } else {
            st_tree += (LeftChild.FuncResult ? "1r" : "0r") + ":";
        }
        if (RightChild.FuncResult == null) {
            st_tree += RightChild.getHistory();
        } else {
            st_tree += RightChild.FuncResult ? "1r" : "0r";
        }
        st_tree += ";";

        if (LeftChild.FuncResult == null && !LeftChild.isVisited) {
            LeftChild.isVisited = true;
            st_tree += LeftChild.getFullDecription();
        }
        if (RightChild.FuncResult == null && !RightChild.isVisited) {
            RightChild.isVisited = true;
            st_tree += RightChild.getFullDecription();
        }
        return st_tree;
    }

    @Override
    public String toString() {
        String st_tree = new String(name + ":" + history + ":");
        if (FuncResult == null) {
            st_tree += "null;";
        } else {
            st_tree += FuncResult ? "1;" : "0;";
        }
        if (LeftChild != null) st_tree += LeftChild.toString();
        if (RightChild != null) st_tree += RightChild.toString();
        return st_tree;
    }

    @Override
    public Tree clone() {
        Tree tree = new Tree(history, name);
        if (FuncResult != null) {
            tree.setFuncResult(FuncResult ? true : false);
        }
        if (RightChild != null) {
            tree.setRightChild(RightChild.clone());
            tree.getRightChild().setParent(tree);
        }
        if (LeftChild != null) {
            tree.setLeftChild(LeftChild.clone());
            tree.getLeftChild().setParent(tree);
        }
        return tree;
    }


}
