package org.jbehave.contrib.eclipse.plugin.editor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.texteditor.ContentAssistAction;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.jbehave.contrib.eclipse.plugin.editor.outline.StoryContentOutlinePage;
import org.jbehave.contrib.eclipse.plugin.editor.outline.StoryElement;
import org.jbehave.contrib.eclipse.plugin.editor.reconciler.DocumentReconciler;
import org.jbehave.contrib.eclipse.plugin.editor.tree.Node;

public class JBehaveEditor extends TextEditor {

  private ColorManager colorManager;
  private StoryContentOutlinePage outlinePage;
  private DocumentReconciler documentReconciler;

  public JBehaveEditor() {
    super();

    colorManager = new ColorManager();
    documentReconciler = new DocumentReconciler(this);
    JBehaveSourceViewerConfiguration configuration = new JBehaveSourceViewerConfiguration(this,
                                                                                          colorManager,
                                                                                          documentReconciler);
    setSourceViewerConfiguration(configuration);
    setDocumentProvider(new StoryDocumentProvider());
  }

  @Override
  protected void createActions() {
    super.createActions();

    ResourceBundle resourceBundle = null;
    try {
      resourceBundle = new PropertyResourceBundle(new ByteArrayInputStream("ContentAssistProposal.label=Content assist\nContentAssistProposal.tooltip=Content assist\nContentAssistProposal.description=Provides Content Assistance".getBytes()));
    } catch (IOException e) {
      e.printStackTrace();
    }
    ContentAssistAction action = new ContentAssistAction(resourceBundle,
                                                         "ContentAssistProposal.",
                                                         this);
    String id = ITextEditorActionDefinitionIds.CONTENT_ASSIST_PROPOSALS;
    action.setActionDefinitionId(id);
    setAction("ContentAssist", action);
  }

  public Object getAdapter(Class required) {
    if (IContentOutlinePage.class.equals(required)) {
      if (outlinePage == null) {
        outlinePage = new StoryContentOutlinePage(getDocumentProvider(), this);
        if (getEditorInput() != null)
          outlinePage.setInput(getEditorInput());
      }
      return outlinePage;
    }
    return super.getAdapter(required);
  }

  public void editorSaved() {
    if (outlinePage != null) {
      outlinePage.update();
    }
  }

  public Node<StoryElement> getRootNode() {
    return documentReconciler.getRootNode();
  }

  public void updateOutlineView() {
    if (outlinePage != null) {
      outlinePage.update();
    }
  }
}
