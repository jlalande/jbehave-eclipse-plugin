package org.jbehave.contrib.eclipse.plugin.editor.contentassist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jbehave.contrib.eclipse.plugin.editor.KeyWords;

public class WordProvider {

  public List<String> suggest(String word) {
    ArrayList<String> wordBuffer = new ArrayList<String>();

    // Adding common BDD keywords
    for (String s : KeyWords.TAGS) {
      if (s.startsWith(word)) {
        wordBuffer.add(s);
      }
    }

    // Adding section keywords (Scenario, Meta, etc.)
    for (String s : KeyWords.SECTIONS) {
      if (s.startsWith(word)) {
        wordBuffer.add(s);
      }
    }

    // Adding narrative keywords
    for (String s : KeyWords.NARRATIVE_KEYWORDS) {
      if (s.startsWith(word)) {
        wordBuffer.add(s);
      }
    }

    Collections.sort(wordBuffer);
    return wordBuffer;
  }

}