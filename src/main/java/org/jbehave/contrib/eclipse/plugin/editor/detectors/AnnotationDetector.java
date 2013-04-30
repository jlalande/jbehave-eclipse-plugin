package org.jbehave.contrib.eclipse.plugin.editor.detectors;

import org.eclipse.jface.text.rules.IWordDetector;

public class AnnotationDetector implements IWordDetector {

  /**
   * @see org.eclipse.jface.text.rules.IWordDetector#isWordPart(char)
   */
  @Override
  public boolean isWordPart(char c) {
    return Character.isJavaIdentifierPart(c);
  }

  /**
   * @see org.eclipse.jface.text.rules.IWordDetector#isWordStart(char)
   */
  @Override
  public boolean isWordStart(char c) {
    return c == '@';
  }

}
