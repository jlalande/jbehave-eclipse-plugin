package org.jbehave.contrib.eclipse.plugin.editor.tree;

import java.util.ArrayList;
import java.util.List;

public class Node<T> {
  private T data;
  private Node<T> parent;
  private List<Node<T>> children;

  public Node() {
    initNode(null, null);
  }

  public Node(T data) {
    initNode(data, null);
  }

  public Node(T data, Node<T> parent) {
    initNode(data, parent);
  }

  private void initNode(T data, Node<T> parent) {
    children = new ArrayList<Node<T>>();
    this.data = data;
    this.parent = parent;
  }

  public void setData(T data) {
    this.data = data;
  }

  public T getData() {
    return data;
  }

  public boolean hasChildren() {
    return children != null && children.size() > 0;
  }

  public List<Node<T>> getChildren() {
    return children;
  }

  public void setChildren(List<Node<T>> children) {
    this.children = children;
  }

  public Node<T> addChild(Node<T> child) {
    children.add(child);
    return child;
  }

  public Node<T> removeChild(T data) {
    Node<T> foundNode = null;
    for (Node<T> node : children) {
      if (node.getData().equals(data)) {
        foundNode = node;
        children.remove(node);
        break;
      }
    }
    return foundNode;
  }

  public Node<T> getChild(T data) {
    for (Node<T> node : children) {
      if (node.getData().equals(data)) {
        return node;
      }
    }
    return null;
  }

  public Node<T> getChild(int index) {
    try {
      return children.get(index);
    } catch (IndexOutOfBoundsException e) {
      return null;
    }
  }

  public void setParent(Node<T> parent) {
    this.parent = parent;
  }

  public Node<T> getParent() {
    return parent;
  }

  public Node<T> find(T data) {
    if (this.data.equals(data)) {
      return this;
    }

    Node<T> found = null;
    for (Node<T> child : children) {
      found = child.find(data);
      if (found != null) {
        return found;
      }
    }

    return found;

  }

  public void clear() {
    data = null;
    parent = null;
    for (Node<T> child : children) {
      child.clear();
    }
    children = null;
  }

  public String toString() {
    if (data != null) {
      return data.toString();
    } else {
      return "";
    }
  }

  public boolean equals(Object object) {
    if (!(object instanceof Node<?>)) {
      return false;
    }

    Node<T> node = (Node<T>) object;
    return data.equals(node.getData());
  }
}
