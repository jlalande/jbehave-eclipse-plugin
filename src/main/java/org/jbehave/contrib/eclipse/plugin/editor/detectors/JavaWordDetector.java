package org.jbehave.contrib.eclipse.plugin.editor.detectors;

import org.eclipse.jface.text.rules.IWordDetector;

public class JavaWordDetector implements IWordDetector {

  @Override
  public boolean isWordPart(char c) {
    return Character.isUnicodeIdentifierPart(c);
    // return Character.isJavaIdentifierPart(c) || c == '@';
  }

  @Override
  public boolean isWordStart(char c) {
    // return Character.isUnicodeIdentifierPart(c);
    return Character.isJavaIdentifierStart(c);
  }
}
