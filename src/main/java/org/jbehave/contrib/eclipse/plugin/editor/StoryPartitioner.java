package org.jbehave.contrib.eclipse.plugin.editor;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.jface.text.rules.IPartitionTokenScanner;

public class StoryPartitioner extends FastPartitioner {
  /**
   * Creates a <code>StoryPartitioner</code>.
   * 
   * @param scanner
   * @param legalContentTypes
   */
  public StoryPartitioner(IPartitionTokenScanner scanner, String[] legalContentTypes) {
    super(scanner, legalContentTypes);
  }

  public void connect(IDocument document, boolean delayInitialise) {
    super.connect(document, delayInitialise);
    printPartitions(document);
  }

  public void printPartitions(IDocument document) {
    StringBuffer buffer = new StringBuffer();

    ITypedRegion[] partitions = computePartitioning(0, document.getLength());
    for (int i = 0; i < partitions.length; i++) {
      try {
        buffer.append("Partition type: " + partitions[i].getType() + ", offset: "
                      + partitions[i].getOffset() + ", length: " + partitions[i].getLength());
        buffer.append("\n");
        buffer.append("Text:\n");
        buffer.append(document.get(partitions[i].getOffset(), partitions[i].getLength()));
        buffer.append("\n---------------------------\n\n\n");
      } catch (BadLocationException e) {
        e.printStackTrace();
      }
    }
    System.out.print(buffer);
  }
}
