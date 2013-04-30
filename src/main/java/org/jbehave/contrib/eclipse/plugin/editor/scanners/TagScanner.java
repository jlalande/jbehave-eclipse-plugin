package org.jbehave.contrib.eclipse.plugin.editor.scanners;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WordRule;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.jbehave.contrib.eclipse.plugin.editor.ColorConstants;
import org.jbehave.contrib.eclipse.plugin.editor.ColorManager;
import org.jbehave.contrib.eclipse.plugin.editor.KeyWords;
import org.jbehave.contrib.eclipse.plugin.editor.detectors.KeyWordDetector;

public class TagScanner extends RuleBasedScanner {

  public TagScanner(ColorManager manager) {
    super();

    Color keywordColor = manager.getColor(ColorConstants.KEYWORD);
    TextAttribute keywordAttribute = new TextAttribute(keywordColor, null, SWT.BOLD);
    IToken keywordToken = new Token(keywordAttribute);

    WordRule keywordRule = new WordRule(new KeyWordDetector(), Token.UNDEFINED);
    keywordRule.setColumnConstraint(0);
    for (String keyword : KeyWords.SECTIONS) {
      keywordRule.addWord(keyword + ":", keywordToken);
    }

    IRule[] scannerRules = new IRule[1];
    scannerRules[0] = keywordRule;

    setRules(scannerRules);
  }
}
