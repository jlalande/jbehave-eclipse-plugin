package org.jbehave.contrib.eclipse.plugin.editor.outline;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.jbehave.contrib.eclipse.plugin.editor.outline.StoryElement.StoryElementType;
import org.jbehave.contrib.eclipse.plugin.editor.tree.Node;

public class OutlineLabelProvider implements ILabelProvider {

  private List<ILabelProviderListener> listeners;
  private Image elementImage;
  private Image storyImage;

  public OutlineLabelProvider() {
    listeners = new ArrayList<ILabelProviderListener>();
    elementImage = PlatformUI.getWorkbench()
                             .getSharedImages()
                             .getImageDescriptor(ISharedImages.IMG_OBJ_ELEMENT)
                             .createImage();
    storyImage = PlatformUI.getWorkbench()
                           .getSharedImages()
                           .getImageDescriptor(ISharedImages.IMG_DEF_VIEW)
                           .createImage();
  }

  /**
   * @see org.eclipse.jface.viewers.IBaseLabelProvider#addListener(org.eclipse.jface.viewers.ILabelProviderListener)
   */
  @Override
  public void addListener(ILabelProviderListener listener) {
    listeners.add(listener);
  }

  /**
   * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose()
   */
  @Override
  public void dispose() {
  }

  /**
   * @see org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(java.lang.Object,
   *      java.lang.String)
   */
  @Override
  public boolean isLabelProperty(Object arg0, String arg1) {
    return false;
  }

  /**
   * @see org.eclipse.jface.viewers.IBaseLabelProvider#removeListener(org.eclipse.jface.viewers.ILabelProviderListener)
   */
  @Override
  public void removeListener(ILabelProviderListener listener) {
    listeners.remove(listener);
  }

  /**
   * @see org.eclipse.jface.viewers.ILabelProvider#getImage(java.lang.Object)
   */
  @SuppressWarnings("unchecked")
  @Override
  public Image getImage(Object object) {
    Image image = elementImage;

    if (object instanceof Node<?>) {
      Node<StoryElement> node = (Node<StoryElement>) object;
      if (node.getData().getElementType().equals(StoryElementType.STORY)) {
        image = storyImage;
      } else {
        image = elementImage;
      }
    }

    return image;
  }

  /**
   * @see org.eclipse.jface.viewers.ILabelProvider#getText(java.lang.Object)
   */
  @Override
  public String getText(Object object) {
    if (object instanceof Node<?>) {
      Node<StoryElement> node = (Node<StoryElement>) object;
      return node.getData().getLabel();
    } else {
      return object.toString();
    }
  }

}
