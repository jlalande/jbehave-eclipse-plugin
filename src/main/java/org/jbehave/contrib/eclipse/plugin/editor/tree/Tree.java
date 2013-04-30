package org.jbehave.contrib.eclipse.plugin.editor.tree;

public class Tree<T> {
  private Node<T> root;

  public Tree() {
    root = new Node<T>();
  }

  public Tree(T rootData) {
    root = new Node<T>(rootData);
  }

  /**
   * Recursive search from the root node.
   * 
   * @param data Data to find.
   * @return The node that contains the data. Null if it can't be found.
   */
  public Node<T> find(T data) {
    if (root == null) {
      return null;
    } else {
      return root.find(data);
    }
  }

  public Node<T> getRootNode() {
    return root;
  }

  public void setRootNode(Node<T> node) {
    root = node;
  }

  public void clear() {
    root.clear();
    root = new Node<T>();
  }

  public String toString() {
    return root.toString();
  }
}
