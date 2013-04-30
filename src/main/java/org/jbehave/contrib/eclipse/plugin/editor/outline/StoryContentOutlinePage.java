package org.jbehave.contrib.eclipse.plugin.editor.outline;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;
import org.jbehave.contrib.eclipse.plugin.editor.JBehaveEditor;
import org.jbehave.contrib.eclipse.plugin.editor.tree.Node;

public class StoryContentOutlinePage extends ContentOutlinePage {
  private JBehaveEditor editor;
  private IEditorInput input;
  private IDocumentProvider documentProvider;

  public StoryContentOutlinePage(IDocumentProvider provider, JBehaveEditor editor) {
    super();
    this.editor = editor;
    this.documentProvider = provider;
  }

  public void createControl(Composite parent) {

    super.createControl(parent);

    TreeViewer viewer = getTreeViewer();
    OutlineContentProvider provider = new OutlineContentProvider(documentProvider, editor);
    viewer.setContentProvider(provider);
    viewer.setLabelProvider(new OutlineLabelProvider());
    viewer.addSelectionChangedListener(this);

    // control is created after input is set
    if (input != null) {
      viewer.setInput(input);
    }
    viewer.expandAll();
    update();
  }

  /**
   * @param editorInput
   */
  public void setInput(IEditorInput editorInput) {
    if (input != editorInput) {
      input = editorInput;
    }
    update();
  }

  /*
   * (non-Javadoc) Method declared on ContentOutlinePage
   */
  public void selectionChanged(SelectionChangedEvent event) {
    super.selectionChanged(event);

    ISelection selection = event.getSelection();
    if (selection.isEmpty())
      editor.resetHighlightRange();
    else {
      Node node = (Node) ((IStructuredSelection) selection).getFirstElement();
      StoryElement element = (StoryElement) node.getData();
      int start = element.getPosition().getOffset();
      int length = element.getPosition().getLength();
      try {
        editor.setHighlightRange(start, length, true);
      } catch (IllegalArgumentException x) {
        editor.resetHighlightRange();
      }
    }
  }

  /**
   * 
   */
  public void update() {

    TreeViewer viewer = getTreeViewer();

    if (viewer != null) {
      Control control = viewer.getControl();
      if (control != null && !control.isDisposed()) {
        Object[] expandedElements = viewer.getExpandedElements();
        TreePath[] path = viewer.getExpandedTreePaths();

        control.setRedraw(false);
        viewer.setInput(input);
        // viewer.refresh();
        viewer.setExpandedElements(expandedElements);
        viewer.setExpandedTreePaths(path);
        // viewer.expandAll();
        control.setRedraw(true);
      }
    }

  }

}
