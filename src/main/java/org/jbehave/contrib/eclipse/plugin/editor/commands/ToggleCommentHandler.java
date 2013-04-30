package org.jbehave.contrib.eclipse.plugin.editor.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jbehave.contrib.eclipse.plugin.editor.JBehaveEditor;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class ToggleCommentHandler extends AbstractHandler {
  /**
   * 
   */
  private static final String COMMENT_TOKEN = "!-- ";

  /**
   * The constructor.
   */
  public ToggleCommentHandler() {
  }

  /**
   * the command has been executed, so extract extract the needed information from the application
   * context.
   */
  public Object execute(ExecutionEvent event) throws ExecutionException {
    JBehaveEditor editor = (JBehaveEditor) HandlerUtil.getActiveWorkbenchWindow(event)
                                                      .getActivePage()
                                                      .getActiveEditor();
    IDocument document = editor.getDocumentProvider().getDocument(editor.getEditorInput());
    ITextSelection selection = (ITextSelection) editor.getSelectionProvider().getSelection();

    int startLine = selection.getStartLine();
    int stopLine = selection.getEndLine();
    int selectionLength = selection.getLength();
    int offsetStart = 0;
    int offsetEnd = 0;
    StringBuilder replacementText = new StringBuilder();
    boolean addComment = false;

    try {
      offsetStart = document.getLineOffset(selection.getStartLine());
      offsetEnd = document.getLineOffset(stopLine) + document.getLineLength(stopLine);
      String beginOfSelection = document.get(offsetStart, COMMENT_TOKEN.length());
      addComment = !beginOfSelection.equals(COMMENT_TOKEN);

      String newLine;
      String currentLine;
      int lineLength;
      int lineOffset;
      int commentsCount = 0;

      for (int i = startLine; i <= stopLine; i++) {
        lineLength = document.getLineLength(i);
        lineOffset = document.getLineOffset(i);
        currentLine = document.get(lineOffset, lineLength);
        newLine = "";

        if (addComment) {
          if (!currentLine.trim().isEmpty()) {
            newLine = COMMENT_TOKEN + currentLine;
            commentsCount++;
          } else {
            newLine = currentLine;
          }
        } else {
          if (currentLine.startsWith(COMMENT_TOKEN)) {
            newLine = currentLine.substring(COMMENT_TOKEN.length());
            commentsCount--;
          } else {
            newLine = currentLine;
          }
        }

        replacementText.append(newLine);
      }

      // TODO the length used when replacing the text must be updated to reflect the real selection
      // of the user. Add some logic to check the amount of characters selected on the last line vs
      // the total length. This logic must also take into account that the number of selected
      // characters on the startline may not be the whole line.
      document.replace(offsetStart, offsetEnd - offsetStart, replacementText.toString());

      if (selection.getLength() > 0) {
        // editor.selectAndReveal(offsetStart,
        // offsetEnd - offsetStart + commentsCount * COMMENT_TOKEN.length());
        editor.selectAndReveal(selection.getOffset(), selection.getLength());
      }
    } catch (BadLocationException e) {
      e.printStackTrace();
    }

    return null;
  }
}
