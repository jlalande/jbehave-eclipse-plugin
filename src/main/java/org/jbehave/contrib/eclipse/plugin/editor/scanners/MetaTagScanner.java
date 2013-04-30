package org.jbehave.contrib.eclipse.plugin.editor.scanners;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WordPatternRule;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.jbehave.contrib.eclipse.plugin.editor.ColorConstants;
import org.jbehave.contrib.eclipse.plugin.editor.ColorManager;
import org.jbehave.contrib.eclipse.plugin.editor.detectors.AnnotationDetector;

public class MetaTagScanner extends RuleBasedScanner {

  public MetaTagScanner(ColorManager colorManager) {
    super();

    Color color = colorManager.getColor(ColorConstants.META_TAG);
    TextAttribute textAttribute = new TextAttribute(color, null, SWT.BOLD);
    IToken token = new Token(textAttribute);

    WordPatternRule rule = new WordPatternRule(new AnnotationDetector(), "@", null, token);
    rule.setColumnConstraint(0);

    IRule[] rules = new IRule[1];
    rules[0] = rule;

    setRules(rules);
  }
}
