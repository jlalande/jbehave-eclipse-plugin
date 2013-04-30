package org.jbehave.contrib.eclipse.plugin.editor;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.ui.editors.text.FileDocumentProvider;
import org.jbehave.contrib.eclipse.plugin.editor.scanners.StoryPartitionScanner;


public class StoryDocumentProvider extends FileDocumentProvider {

  protected IDocument createDocument(Object element) throws CoreException {
    IDocument document = super.createDocument(element);
    if (document != null) {
      IDocumentPartitioner partitioner = new StoryPartitioner(new StoryPartitionScanner(),
                                                              new String[] {
                                                                            StoryPartitionScanner.STORY_COMMENT,
                                                                            StoryPartitionScanner.STORY_META_TAG,
                                                                            StoryPartitionScanner.STORY_SECTION, });
      partitioner.connect(document);
      document.setDocumentPartitioner(partitioner);
    }
    return document;
  }
}
