package org.jbehave.contrib.eclipse.plugin.editor.scanners;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.Token;
import org.jbehave.contrib.eclipse.plugin.editor.KeyWords;
import org.jbehave.contrib.eclipse.plugin.editor.rules.NonMatchingRule;

public class StoryPartitionScanner extends RuleBasedPartitionScanner {
  public final static String STORY_SECTION = "__story_section";
  public final static String STORY_COMMENT = "__story_comment";
  public final static String STORY_META_TAG = "__story_meta_tag";

  public StoryPartitionScanner() {

    IToken storyComment = new Token(STORY_COMMENT);
    IToken storySection = new Token(STORY_SECTION);
    IToken storyMetaTag = new Token(STORY_META_TAG);

    List<IPredicateRule> rules = new ArrayList<IPredicateRule>();

    rules.add(new NonMatchingRule());
    rules.add(new EndOfLineRule("!-- ", storyComment));
    EndOfLineRule metaTagRule = new EndOfLineRule("@", storyMetaTag);
    metaTagRule.setColumnConstraint(0);
    rules.add(metaTagRule);

    for (String section : KeyWords.SECTIONS) {
      rules.add(new EndOfLineRule(section, storySection));
    }

    IPredicateRule[] predicateRules = rules.toArray(new IPredicateRule[rules.size()]);
    setPredicateRules(predicateRules);
  }
}
