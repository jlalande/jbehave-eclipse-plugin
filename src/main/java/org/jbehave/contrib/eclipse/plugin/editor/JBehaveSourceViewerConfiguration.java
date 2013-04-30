package org.jbehave.contrib.eclipse.plugin.editor;

import org.eclipse.jface.text.DefaultIndentLineAutoEditStrategy;
import org.eclipse.jface.text.IAutoEditStrategy;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.reconciler.IReconciler;
import org.eclipse.jface.text.reconciler.MonoReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.editors.text.TextSourceViewerConfiguration;
import org.eclipse.ui.texteditor.ITextEditor;
import org.jbehave.contrib.eclipse.plugin.editor.autoedit.StoryAutoEditStrategy;
import org.jbehave.contrib.eclipse.plugin.editor.contentassist.StoryContentAssistProcessor;
import org.jbehave.contrib.eclipse.plugin.editor.reconciler.DocumentReconciler;
import org.jbehave.contrib.eclipse.plugin.editor.scanners.MetaTagScanner;
import org.jbehave.contrib.eclipse.plugin.editor.scanners.StoryPartitionScanner;
import org.jbehave.contrib.eclipse.plugin.editor.scanners.TagScanner;
import org.jbehave.contrib.eclipse.plugin.editor.scanners.TextScanner;

public class JBehaveSourceViewerConfiguration extends TextSourceViewerConfiguration {

  private ITextEditor editor = null;
  private TagScanner tagScanner = null;
  private TextScanner textScanner = null;
  private MetaTagScanner metaTagScanner = null;
  private ContentAssistant assistant = null;
  private ColorManager colorManager = null;
  private DocumentReconciler documentReconciler = null;

  public JBehaveSourceViewerConfiguration(ITextEditor editor,
                                          ColorManager colorManager,
                                          DocumentReconciler reconciler)
  {
    super();
    this.editor = editor;
    this.colorManager = colorManager;
    this.documentReconciler = reconciler;
  }

  @Override
  public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
    PresentationReconciler reconciler = new PresentationReconciler();

    DefaultDamagerRepairer dr = new DefaultDamagerRepairer(getTextScanner());
    reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
    reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

    dr = new DefaultDamagerRepairer(getTagScanner());
    reconciler.setDamager(dr, StoryPartitionScanner.STORY_SECTION);
    reconciler.setRepairer(dr, StoryPartitionScanner.STORY_SECTION);

    dr = new DefaultDamagerRepairer(getMetaTagScanner());
    reconciler.setDamager(dr, StoryPartitionScanner.STORY_META_TAG);
    reconciler.setRepairer(dr, StoryPartitionScanner.STORY_META_TAG);

    TextAttribute textAttribute = new TextAttribute(colorManager.getColor(ColorConstants.COMMENT));
    NonRuleBasedDamagerRepairer ndr = new NonRuleBasedDamagerRepairer(textAttribute);
    reconciler.setDamager(ndr, StoryPartitionScanner.STORY_COMMENT);
    reconciler.setRepairer(ndr, StoryPartitionScanner.STORY_COMMENT);

    return reconciler;
  }

  @Override
  public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
    if (assistant == null) {
      assistant = new ContentAssistant();
      assistant.setContentAssistProcessor(new StoryContentAssistProcessor(),
                                          IDocument.DEFAULT_CONTENT_TYPE);
      assistant.setInformationControlCreator(getInformationControlCreator(sourceViewer));
    }
    return assistant;
  }

  public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
    return new String[] { IDocument.DEFAULT_CONTENT_TYPE, StoryPartitionScanner.STORY_COMMENT,
                         StoryPartitionScanner.STORY_SECTION, StoryPartitionScanner.STORY_META_TAG };
  }

  /**
   * @see org.eclipse.ui.editors.text.TextSourceViewerConfiguration#getReconciler(org.eclipse.jface.text.source.ISourceViewer)
   */
  public IReconciler getReconciler(ISourceViewer sourceViewer) {
    return new MonoReconciler(documentReconciler, false);
  }

  public IAutoEditStrategy[] getAutoEditStrategies(ISourceViewer sourceViewer, String contentType) {
    IAutoEditStrategy strategy = (IDocument.DEFAULT_CONTENT_TYPE.equals(contentType) ? new StoryAutoEditStrategy()
                                                                                    : new DefaultIndentLineAutoEditStrategy());
    return new IAutoEditStrategy[] { strategy };
  }

  private TagScanner getTagScanner() {
    if (tagScanner == null) {
      tagScanner = new TagScanner(colorManager);
    }
    return tagScanner;
  }

  private TextScanner getTextScanner() {
    if (textScanner == null) {
      textScanner = new TextScanner(colorManager);
    }
    return textScanner;
  }

  private MetaTagScanner getMetaTagScanner() {
    if (metaTagScanner == null) {
      metaTagScanner = new MetaTagScanner(colorManager);
    }
    return metaTagScanner;
  }
}
