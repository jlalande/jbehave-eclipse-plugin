package org.jbehave.contrib.eclipse.plugin.editor.outline;

import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.DefaultPositionUpdater;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IPositionUpdater;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.jbehave.contrib.eclipse.plugin.editor.JBehaveEditor;
import org.jbehave.contrib.eclipse.plugin.editor.tree.Node;

@SuppressWarnings("rawtypes")
public class OutlineContentProvider implements ITreeContentProvider {
  public final static String SEGMENTS = "__java_segments";
  protected IPositionUpdater fPositionUpdater = new DefaultPositionUpdater(SEGMENTS);

  protected Object fInput;
  protected IDocumentProvider documentProvider;
  protected JBehaveEditor textEditor;

  public OutlineContentProvider(IDocumentProvider documentProvider, JBehaveEditor editor) {
    super();
    this.documentProvider = documentProvider;
    this.textEditor = editor;
    fInput = editor.getEditorInput();
  }

  @Override
  public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    if (oldInput != null) {
      IDocument document = documentProvider.getDocument(oldInput);
      if (document != null) {
        try {
          document.removePositionCategory(SEGMENTS);
        } catch (BadPositionCategoryException x) {
        }
        document.removePositionUpdater(fPositionUpdater);
      }
    }

    if (newInput != null) {
      IDocument document = documentProvider.getDocument(newInput);
      if (document != null) {
        document.addPositionCategory(SEGMENTS);
        document.addPositionUpdater(fPositionUpdater);
      }
    }
  }

  public Object[] getChildren(Object element) {
    return ((Node) element).getChildren().toArray();
  }

  @Override
  public void dispose() {

  }

  @Override
  public Object[] getElements(Object inputElement) {
    if (inputElement == fInput) {
      return new Object[] { textEditor.getRootNode() };
    } else {
      return textEditor.getRootNode().getChildren().toArray();
    }
  }

  @Override
  public Object getParent(Object element) {
    if (element instanceof Node) {
      return ((Node) element).getParent();
    } else {
      return null;
    }
  }

  @Override
  public boolean hasChildren(Object element) {
    if (element == fInput) {
      return true;
    } else {
      return ((Node) element).hasChildren();
    }
  }
}
