package org.jbehave.contrib.eclipse.plugin.editor.scanners;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.PatternRule;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WordRule;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.jbehave.contrib.eclipse.plugin.editor.ColorConstants;
import org.jbehave.contrib.eclipse.plugin.editor.ColorManager;
import org.jbehave.contrib.eclipse.plugin.editor.KeyWords;
import org.jbehave.contrib.eclipse.plugin.editor.detectors.JavaWordDetector;

public class TextScanner extends RuleBasedScanner {

  /**
   * Creates a <code>TextScanner</code>.
   * 
   * @param colorManager
   */
  public TextScanner(ColorManager colorManager) {
    super();

    Color tagColor = colorManager.getColor(ColorConstants.TAG);
    TextAttribute tagAttribute = new TextAttribute(tagColor);
    IToken tagToken = new Token(tagAttribute);

    Color color = colorManager.getColor(ColorConstants.TEXT);
    TextAttribute textAttribute = new TextAttribute(color);
    IToken token = new Token(textAttribute);

    Color paramColor = colorManager.getColor(ColorConstants.PARAMETER);
    TextAttribute paramAttribute = new TextAttribute(paramColor, null, SWT.BOLD);
    IToken paramToken = new Token(paramAttribute);

    WordRule tagRule = new WordRule(new JavaWordDetector(), Token.UNDEFINED);
    tagRule.setColumnConstraint(0);
    for (String tag : KeyWords.TAGS) {
      tagRule.addWord(tag, tagToken);
    }

    IRule[] rules = new IRule[3];
    rules[0] = new MultiLineRule("\"", "\"", token);
    rules[1] = new PatternRule("<", ">", paramToken, '\\', false);
    rules[2] = tagRule;

    setRules(rules);
  }
}
