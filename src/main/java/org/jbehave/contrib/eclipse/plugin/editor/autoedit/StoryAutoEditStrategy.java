package org.jbehave.contrib.eclipse.plugin.editor.autoedit;

import org.eclipse.jface.text.DocumentCommand;
import org.eclipse.jface.text.IAutoEditStrategy;
import org.eclipse.jface.text.IDocument;

public class StoryAutoEditStrategy implements IAutoEditStrategy {

  /**
   * @see org.eclipse.jface.text.IAutoEditStrategy#customizeDocumentCommand(org.eclipse.jface.text.IDocument,
   *      org.eclipse.jface.text.DocumentCommand)
   * 
   *      TODO Implement a better solution and more features
   */
  @Override
  public void customizeDocumentCommand(IDocument document, DocumentCommand command) {
    // if (command.text.equals("\"")) {
    // command.text = "\"\"";
    // configureCommand(command);
    // } else if (command.text.equals("'")) {
    // command.text = "''";
    // configureCommand(command);
    // }
  }

  private void configureCommand(DocumentCommand command) {
    // puts the caret between both the quotes
    command.caretOffset = command.offset + 1;
    command.shiftsCaret = false;
  }
}
