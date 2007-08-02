package org.eclipse.imp.xform.search;

import java.util.ResourceBundle;

import org.eclipse.imp.editor.IASTFindReplaceTarget;
import org.eclipse.imp.editor.UniversalEditor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IPartService;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.texteditor.ResourceAction;

/**
 * An action which opens an AST-based Find/Replace dialog.
 * The dialog while open, tracks the active workbench part
 * and retargets itself to the active find/replace target.
 * <p>
 * This class may be instantiated; it is not intended to be subclassed.
 * </p>
 *
 * @see IASTFindReplaceTarget
 */
public class ASTFindReplaceAction implements IWorkbenchWindowActionDelegate /* extends ResourceAction implements IUpdate */ {
    /**
     * Represents the "global" find/replace dialog. It tracks the active
     * part and retargets the find/replace dialog accordingly. The find/replace
     * target is retrieved from the active part using
     * <code>getAdapter(IASTFindReplaceTarget.class)</code>.
     * <p>
     * The stub has the same life cycle as the find/replace dialog.</p>
     */
    static class FindReplaceDialogStub implements IPartListener, DisposeListener {
	/** The workbench part */
	private IWorkbenchPart fPart;
	/** The previous workbench part */
	private IWorkbenchPart fPreviousPart;
	/** The previous find/replace target */
	private IASTFindReplaceTarget fPreviousTarget;
	/** The workbench window */
	private IWorkbenchWindow fWindow;
	/** The find/replace dialog */
	private ASTFindReplaceDialog fDialog;

	/**
	 * Creates a new find/replace dialog accessor anchored at the given part site.
	 * @param site the part site
	 */
	public FindReplaceDialogStub(IWorkbenchPartSite site) {
	    fWindow= site.getWorkbenchWindow();
	    fDialog= new ASTFindReplaceDialog(site.getShell());
	    fDialog.create();
	    fDialog.getShell().addDisposeListener(this);
	    IPartService service= fWindow.getPartService();
	    service.addPartListener(this);
	    partActivated(service.getActivePart());
	}

	/**
	 * Returns the find/replace dialog.
	 * @return the find/replace dialog
	 */
	public ASTFindReplaceDialog getDialog() {
	    return fDialog;
	}

	/*
	 * @see IPartListener#partActivated(IWorkbenchPart)
	 */
	public void partActivated(IWorkbenchPart part) {
	    IASTFindReplaceTarget target= (part == null) ? null : (IASTFindReplaceTarget) part.getAdapter(IASTFindReplaceTarget.class);
	    fPreviousPart= fPart;
	    fPart= (target == null) ? null : part;
	    if (fPreviousTarget != target) {
		fPreviousTarget= target;
		if (target == null)
		    fDialog.updateTarget(null, false, false);
		else if (fDialog != null) {
		    boolean isEditable= false;
		    if (fPart instanceof UniversalEditor)
			isEditable= true;
//		    if (fPart instanceof ITextEditorExtension2) {
//			ITextEditorExtension2 extension= (ITextEditorExtension2) fPart;
//			isEditable= extension.isEditorInputModifiable();
//		    }
		    fDialog.updateTarget(target, isEditable, false);
		}
	    }
	}

	/*
	 * @see IPartListener#partClosed(IWorkbenchPart)
	 */
	public void partClosed(IWorkbenchPart part) {
	    if (part == fPreviousPart) {
		fPreviousPart= null;
		fPreviousTarget= null;
	    }
	    if (part == fPart)
		partActivated(null);
	}

	/*
	 * @see DisposeListener#widgetDisposed(DisposeEvent)
	 */
	public void widgetDisposed(DisposeEvent event) {
	    if (fgFindReplaceDialogStub == this)
		fgFindReplaceDialogStub= null;
	    if (fWindow != null) {
		fWindow.getPartService().removePartListener(this);
		fWindow= null;
	    }
	    fDialog= null;
	    fPart= null;
	    fPreviousPart= null;
	    fPreviousTarget= null;
	}

	/*
	 * @see IPartListener#partOpened(IWorkbenchPart)
	 */
	public void partOpened(IWorkbenchPart part) {}

	/*
	 * @see IPartListener#partDeactivated(IWorkbenchPart)
	 */
	public void partDeactivated(IWorkbenchPart part) {}

	/*
	 * @see IPartListener#partBroughtToTop(IWorkbenchPart)
	 */
	public void partBroughtToTop(IWorkbenchPart part) {}
    }

    /** Lister for disabling the dialog on editor close */
    private static FindReplaceDialogStub fgFindReplaceDialogStub;
    /** The action's target */
    private IASTFindReplaceTarget fTarget;
    /** The part the action is bound to */
    private IWorkbenchPart fWorkbenchPart;
    /** The workbench window */
    private IWorkbenchWindow fWorkbenchWindow;

    public ASTFindReplaceAction() { }

    public void init(IWorkbenchWindow window) {
	fWorkbenchWindow= window;
    }

    public void run(IAction action) {
	if (fgFindReplaceDialogStub != null) {
	    Shell shell= fWorkbenchPart.getSite().getShell();
	    ASTFindReplaceDialog dialog= fgFindReplaceDialogStub.getDialog();
	    if (dialog != null && shell != dialog.getParentShell()) {
		fgFindReplaceDialogStub= null; // here to avoid timing issues
		dialog.close();
	    }
	}
	if (fgFindReplaceDialogStub == null)
	    fgFindReplaceDialogStub= new FindReplaceDialogStub(fWorkbenchPart.getSite());
	boolean isEditable= false;
	fTarget= fgFindReplaceDialogStub.fPreviousTarget;
	if (fTarget == null) return; // Gracefully decline if not an IASTFindReplaceTarget
	isEditable= fTarget.isEditable();
	ASTFindReplaceDialog dialog= fgFindReplaceDialogStub.getDialog();
	dialog.updateTarget(fTarget, isEditable, true);
	dialog.open();
    }

    public void selectionChanged(IAction action, ISelection selection) {
	if (fWorkbenchPart == null && fWorkbenchWindow != null)
	    fWorkbenchPart= fWorkbenchWindow.getPartService().getActivePart();
	if (fWorkbenchPart != null && fWorkbenchPart instanceof IASTFindReplaceTarget)
	    fTarget= (IASTFindReplaceTarget) fWorkbenchPart.getAdapter(IASTFindReplaceTarget.class);
	else
	    fTarget= null;
    }

    // ====================================================================================
    // From here on down is code taken from FindReplaceAction that supports using the
    // action as a "contributed" action by the UniversalEditor, but that requires the
    // UniversalEditor to know about this stuff, which would introduce a cyclic dependency,
    // until such time as we're ready to move this code into org.eclipse.uide...
    // ====================================================================================

    /**
     * Creates a new find/replace action for the given workbench part.
     * The action configures its visual representation from the given
     * resource bundle.
     *
     * @param bundle the resource bundle
     * @param prefix a prefix to be prepended to the various resource keys
     *   (described in <code>ResourceAction</code> constructor), or
     *   <code>null</code> if none
     * @param workbenchPart	 the workbench part
     * @see ResourceAction#ResourceAction(ResourceBundle, String)
     */
    public ASTFindReplaceAction(ResourceBundle bundle, String prefix, IWorkbenchPart workbenchPart) {
//	super(bundle, prefix);
	fWorkbenchPart= workbenchPart;
	update();
    }

    /*
     *	@see IAction#run()
     */
    public void run() {
	if (fTarget == null)
	    return;
	if (fgFindReplaceDialogStub != null) {
	    Shell shell= fWorkbenchPart.getSite().getShell();
	    ASTFindReplaceDialog dialog= fgFindReplaceDialogStub.getDialog();
	    if (dialog != null && shell != dialog.getParentShell()) {
		fgFindReplaceDialogStub= null; // here to avoid timing issues
		dialog.close();
	    }
	}
	if (fgFindReplaceDialogStub == null)
	    fgFindReplaceDialogStub= new FindReplaceDialogStub(fWorkbenchPart.getSite());
	boolean isEditable= false;
	fTarget= fgFindReplaceDialogStub.fPreviousTarget;
	isEditable= fTarget.isEditable();
	ASTFindReplaceDialog dialog= fgFindReplaceDialogStub.getDialog();
	dialog.updateTarget(fTarget, isEditable, true);
	dialog.open();
    }

    /*
     * @see IUpdate#update()
     */
    public void update() {
	if (fWorkbenchPart == null && fWorkbenchWindow != null)
	    fWorkbenchPart= fWorkbenchWindow.getPartService().getActivePart();
	if (fWorkbenchPart != null)
	    fTarget= (IASTFindReplaceTarget) fWorkbenchPart.getAdapter(IASTFindReplaceTarget.class);
	else
	    fTarget= null;
//	setEnabled(fTarget != null && fTarget.canPerformFind());
    }

    public void dispose() {
	// TODO Auto-generated method stub
    }
}
