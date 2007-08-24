/*
 * (C) Copyright IBM Corporation 2007
 * 
 * This file is part of the Eclipse IMP.
 */
package org.eclipse.imp.xform.search;

import org.eclipse.core.resources.IFile;
import org.eclipse.imp.xform.XformPlugin;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.search.ui.text.AbstractTextSearchViewPage;
import org.eclipse.search.ui.text.Match;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

public class ASTSearchResultPage extends AbstractTextSearchViewPage {
    private static final Image sSearchHitImage= XformPlugin.getImageDescriptor("icons/astSearchHit.gif").createImage();

    private final class ASTLabelProvider implements ILabelProvider {
        public void addListener(ILabelProviderListener listener) { }

        public void dispose() { }

        public boolean isLabelProperty(Object element, String property) {
            return false;
        }

        public void removeListener(ILabelProviderListener listener) { }

        public Image getImage(Object element) {
            return sSearchHitImage;
        }

        public String getText(Object element) {
            if (element instanceof IFile) {
        	IFile file= (IFile) element;

        	return "Matches in '" + file.getFullPath() + "'";
            } else if (element instanceof Match) {
        	Match m= (Match) element;

        	return "match at " + m.getOffset() + ":" + m.getLength();
            }
            return "???";
        }
    }

    private ASTSearchContentProvider fContentProvider;

    public ASTSearchResultPage(int supportedLayouts) {
        super(supportedLayouts);
    }

    public ASTSearchResultPage() {
        super();
    }

    protected void elementsChanged(Object[] objects) {
        if (fContentProvider != null)
            fContentProvider.elementsChanged(objects);
    }

    protected void clear() {
        if (fContentProvider != null)
            fContentProvider.clear();
    }

    protected void configureTreeViewer(TreeViewer viewer) {
//        PostfixLabelProvider postfixLabelProvider= new PostfixLabelProvider(this);
        viewer.setUseHashlookup(true);
//        viewer.setSorter(new DecoratorIgnoringViewerSorter(postfixLabelProvider));
//        viewer.setLabelProvider(new ColorDecoratingLabelProvider(postfixLabelProvider, PlatformUI.getWorkbench().getDecoratorManager().getLabelDecorator()));
        viewer.setLabelProvider(new ASTLabelProvider());
        fContentProvider= new ASTSearchTreeContentProvider(this);
        viewer.setContentProvider(fContentProvider);
//        addDragAdapters(viewer);
    }

    protected void configureTableViewer(TableViewer viewer) {
        viewer.setUseHashlookup(true);
        // SortingLabelProvider sortingLabelProvider= new SortingLabelProvider(this);
        // viewer.setLabelProvider(new ColorDecoratingLabelProvider(sortingLabelProvider, PlatformUI.getWorkbench().getDecoratorManager().getLabelDecorator()));
        viewer.setLabelProvider(new ASTLabelProvider());
        fContentProvider= new ASTSearchTableContentProvider(this);
        viewer.setContentProvider(fContentProvider);
        // viewer.setSorter(new DecoratorIgnoringViewerSorter(sortingLabelProvider));
        // setSortOrder(fCurrentSortOrder);
        // addDragAdapters(viewer);
    }

    protected StructuredViewer getViewer() {
        // override so that it's visible in the package.
        return super.getViewer();
    }

    protected void showMatch(Match match, int currentOffset, int currentLength, boolean activate) throws PartInitException {
	IEditorPart editorPart= IDE.openEditor(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage(), (IFile) match.getElement());

	editorPart.getEditorSite().getSelectionProvider().setSelection(new TextSelection(match.getOffset(), match.getLength()));
    }
}
