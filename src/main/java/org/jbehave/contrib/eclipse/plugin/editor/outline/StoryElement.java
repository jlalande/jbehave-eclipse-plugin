package org.jbehave.contrib.eclipse.plugin.editor.outline;

import org.eclipse.jface.text.Position;

public class StoryElement {
  public enum StoryElementType {
    STORY, SCENARIO, NARRATIVE, EXAMPLES, META, GIVENSTORIES, UNDEFINED;
  };

  private StoryElementType elementType;
  private String label;
  private Position position;

  public StoryElement(StoryElementType elementType, String label, Position position) {
    this.elementType = elementType;
    this.label = label;
    this.position = position;
  }

  public String toString() {
    return this.label;
  }

  public StoryElementType getElementType() {
    return this.elementType;
  }

  public void setElementType(StoryElementType elementType) {
    this.elementType = elementType;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public Position getPosition() {
    return position;
  }

  public void setPosition(Position position) {
    this.position = position;
  }

  public boolean equals(Object object) {
    StoryElement element = (StoryElement) object;

    return label.equals(element.getLabel()) && elementType.equals(element.getElementType())
           && position.equals(element.getPosition());
  }
};