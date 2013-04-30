package org.jbehave.contrib.eclipse.plugin.editor.reconciler;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.reconciler.DirtyRegion;
import org.eclipse.jface.text.reconciler.IReconcilingStrategy;
import org.eclipse.jface.text.reconciler.IReconcilingStrategyExtension;
import org.eclipse.swt.widgets.Display;
import org.jbehave.contrib.eclipse.plugin.editor.JBehaveEditor;
import org.jbehave.contrib.eclipse.plugin.editor.KeyWords;
import org.jbehave.contrib.eclipse.plugin.editor.outline.StoryElement;
import org.jbehave.contrib.eclipse.plugin.editor.outline.StoryElement.StoryElementType;
import org.jbehave.contrib.eclipse.plugin.editor.tree.Node;
import org.jbehave.contrib.eclipse.plugin.editor.tree.Tree;

public class DocumentReconciler implements IReconcilingStrategyExtension, IReconcilingStrategy {
  private JBehaveEditor editor = null;
  protected final static String DEFAULT_ROOT_LABEL = "Story";
  protected Tree<StoryElement> storyStructure = new Tree<StoryElement>();

  public DocumentReconciler() {

  }

  public DocumentReconciler(JBehaveEditor editor) {
    this.editor = editor;
    initStoryStructure();
  }

  /**
   * 
   * @param document
   */
  protected void parse() {
    IDocument document = editor.getDocumentProvider().getDocument(editor.getEditorInput());
    int lines = document.getNumberOfLines();

    Node<StoryElement> storyNode = new Node<StoryElement>(new StoryElement(StoryElementType.STORY,
                                                                           DEFAULT_ROOT_LABEL,
                                                                           new Position(0)));
    Node<StoryElement> currentScenario = null;
    boolean isStoryNodeAlreadySet = false;

    for (int line = 0; line < lines; line++) {
      try {
        int offset = document.getLineOffset(line);
        int length = document.getLineLength(line);

        Position position = new Position(offset, length);
        // document.addPosition(OutlineContentProvider.SEGMENTS, position);

        String lineText = document.get(offset, length);
        String label = "";
        StoryElementType elementType = StoryElementType.UNDEFINED;

        // TODO refactor this loop
        for (String section : KeyWords.SECTIONS) {
          if (lineText.startsWith(section + ":")) {
            elementType = StoryElementType.valueOf(section.toUpperCase());
            label = section;
            if (lineText.length() > (section.length() + 1)) {
              label = lineText.substring(section.length() + 1);
              if (label.trim().isEmpty()) {
                label = section;
              }
            }

            StoryElement element = new StoryElement(elementType, label, position);
            if (element.getElementType().equals(StoryElementType.STORY)) {
              // If the root node hasn't been initialized yet, meaning it contains only the default
              // value, we replace its content with this new element.
              // NOTE: Keep in mind that other story nodes won't appear in the tree as it is not
              // valid to have multiple stories within a single story file.
              if (!isStoryNodeAlreadySet) {
                storyNode.setData(element);
                isStoryNodeAlreadySet = true;
              }
            } else {

              if (element.getElementType().equals(StoryElementType.SCENARIO)) {
                // Adding a new scenario node and setting it as the current scenario so we can
                // attach other elements underneath it.
                Node<StoryElement> scenarioNode = new Node<StoryElement>(element, storyNode);
                storyNode.addChild(scenarioNode);
                currentScenario = scenarioNode;

              } else {
                // Adding the element as a child to the current scenario or at the root level if no
                // scenario is currently available.
                if (currentScenario != null) {
                  currentScenario.addChild(new Node<StoryElement>(element, currentScenario));
                } else {
                  storyNode.addChild(new Node<StoryElement>(element, storyNode));
                }

              }
            }
          }
        }

      } catch (BadLocationException e) {
        e.printStackTrace();
        // } catch (BadPositionCategoryException e) {
        // e.printStackTrace();
      }
    }

    synchronized (this) {
      // storyStructure.setRootNode(storyNode);
      updateStoryStructure(storyNode);
    }

    Display.getDefault().syncExec(new Runnable() {
      public void run() {
        editor.updateOutlineView();
      }
    });
  }

  public void updateStoryStructure(Node<StoryElement> rootNode) {
    updateNode(storyStructure.getRootNode(), rootNode);
  }

  public void updateNode(Node<StoryElement> currentNode, Node<StoryElement> newNode) {
    if (currentNode == null || !currentNode.getData().equals(newNode.getData())
        || currentNode.getChildren().size() != newNode.getChildren().size())
    {
      // currentNode = newNode;
      currentNode.setData(newNode.getData());
      currentNode.setParent(newNode.getParent());
      currentNode.setChildren(newNode.getChildren());
    } else {
      int nbChildren = currentNode.getChildren().size();
      for (int i = 0; i < nbChildren; i++) {
        updateNode(currentNode.getChild(i), newNode.getChild(i));
      }
    }
  }

  /**
   * 
   */
  private void initStoryStructure() {
    if (storyStructure != null) {
      storyStructure.clear();
    }

    StoryElement rootElement = new StoryElement(StoryElementType.STORY,
                                                DEFAULT_ROOT_LABEL,
                                                new Position(0));
    storyStructure = new Tree<StoryElement>(rootElement);
  }

  /**
   * @see org.eclipse.jface.text.reconciler.IReconcilingStrategyExtension#initialReconcile()
   */
  @Override
  public void initialReconcile() {
    parse();
  }

  /**
   * @see org.eclipse.jface.text.reconciler.IReconcilingStrategyExtension#setProgressMonitor(org.eclipse.core.runtime.IProgressMonitor)
   */
  @Override
  public void setProgressMonitor(IProgressMonitor monitor) {
  }

  /**
   * @see org.eclipse.jface.text.reconciler.IReconcilingStrategy#reconcile(org.eclipse.jface.text.IRegion)
   */
  @Override
  public void reconcile(IRegion arg0) {
    parse();
  }

  /**
   * @see org.eclipse.jface.text.reconciler.IReconcilingStrategy#reconcile(org.eclipse.jface.text.reconciler.DirtyRegion,
   *      org.eclipse.jface.text.IRegion)
   */
  @Override
  public void reconcile(DirtyRegion arg0, IRegion arg1) {
    // parse();
  }

  /**
   * @see org.eclipse.jface.text.reconciler.IReconcilingStrategy#setDocument(org.eclipse.jface.text.IDocument)
   */
  @Override
  public void setDocument(IDocument document) {
  }

  public Node<StoryElement> getRootNode() {
    return storyStructure.getRootNode();
  }

}
