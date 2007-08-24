/*
 * (C) Copyright IBM Corporation 2007
 * 
 * This file is part of the Eclipse IMP.
 */
package org.eclipse.imp.xform.search;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.imp.language.LanguageRegistry;
import org.eclipse.imp.parser.IParseController;
import org.eclipse.imp.services.IASTFindReplaceTarget;
import org.eclipse.imp.utils.ExtensionPointFactory;
import org.eclipse.imp.xform.XformPlugin;
import org.eclipse.imp.xform.pattern.matching.IASTAdapter;
import org.eclipse.imp.xform.pattern.matching.MatchResult;
import org.eclipse.imp.xform.pattern.matching.Matcher;
import org.eclipse.imp.xform.pattern.parser.ASTPatternLexer;
import org.eclipse.imp.xform.pattern.parser.ASTPatternParser;
import org.eclipse.imp.xform.pattern.parser.Ast.Pattern;
import org.eclipse.jface.contentassist.SubjectControlContentAssistant;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.resource.JFaceColors;
import org.eclipse.jface.text.DefaultInformationControl;
import org.eclipse.jface.text.IFindReplaceTargetExtension;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.contentassist.ContentAssistHandler;
import org.eclipse.ui.texteditor.IAbstractTextEditorHelpContextIds;
import org.eclipse.ui.texteditor.IEditorStatusLine;
import org.eclipse.ui.texteditor.ITextEditor;

public class ASTFindReplaceDialog extends Dialog {
    /** The size of the dialogs search history. */
    private static final int HISTORY_SIZE= 5;
    private IASTFindReplaceTarget fTarget;
    private Shell fParentShell;
    private Shell fActiveShell;
    private final ActivationListener fActivationListener= new ActivationListener();
    private final ModifyListener fFindModifyListener= new FindModifyListener();
    private Point fLocation;
    private Point fIncrementalBaseLocation;
    private boolean fWrapInit, fForwardInit, fGlobalInit, fIncrementalInit;
    /**
     * Tells whether an initial find operation is needed
     * before the replace operation.
     * @since 3.0
     */
    private boolean fNeedsInitialFindBeforeReplace;
    private boolean fIsTargetEditable;
    /**
     * Tells whether fUseSelectedLines radio is checked.
     * @since 3.0
     */
    private boolean fUseSelectedLines;
    /**
     * The content assist handler for the find combo.
     * @since 3.0
     */
    private ContentAssistHandler fFindContentAssistHandler;
    /**
     * The content assist handler for the replace combo.
     * @since 3.0
     */
    private ContentAssistHandler fReplaceContentAssistHandler;
    private Rectangle fDialogPositionInit;
    private List fFindHistory;
    private List fReplaceHistory;
    private IRegion fOldScope;
    private Label fReplaceLabel, fStatusLabel;
    private Button fForwardRadioButton, fGlobalRadioButton, fSelectedRangeRadioButton;
    private Button fWrapCheckBox, fIncrementalCheckBox;
    private Button fReplaceSelectionButton, fReplaceFindButton, fFindNextButton, fReplaceAllButton;
    private Combo fFindField, fReplaceField;
    /**
     * <code>true</code> if the find field should receive focus the next time
     * the dialog is activated, <code>false</code> otherwise.
     * @since 3.0
     */
    private boolean fGiveFocusToFindField= true;
    /**
     * Content assist's proposal popup background color.
     * @since 3.0
     */
    private Color fProposalPopupBackgroundColor;
    /**
     * Content assist's proposal popup foreground color.
     * @since 3.0
     */
    private Color fProposalPopupForegroundColor;
    private IDialogSettings fDialogSettings;
    private IASTAdapter fASTAdapter;
    private Pattern fPattern;

    /**
     * Creates a new dialog with the given shell as parent.
     * @param parentShell the parent shell
     */
    public ASTFindReplaceDialog(Shell parentShell) {
	super(parentShell);
	fParentShell= null;
	fTarget= null;
	fDialogPositionInit= null;
	fFindHistory= new ArrayList(HISTORY_SIZE - 1);
	fReplaceHistory= new ArrayList(HISTORY_SIZE - 1);
	fWrapInit= false;
	// fIsRegExInit= false;
	fIncrementalInit= false;
	fGlobalInit= true;
	fForwardInit= true;
	readConfiguration();
	setShellStyle(SWT.CLOSE | SWT.MODELESS | SWT.BORDER | SWT.TITLE);
	setBlockOnOpen(false);
	ASTPatternParser.setASTAdapter(fASTAdapter);
    }

    /**
     * Returns this dialog's parent shell.
     * @return the dialog's parent shell
     */
    public Shell getParentShell() {
	return super.getParentShell();
    }

    /**
     * Updates the find replace dialog on activation changes.
     */
    class ActivationListener extends ShellAdapter {
	/*
	 * @see ShellListener#shellActivated(ShellEvent)
	 */
	public void shellActivated(ShellEvent e) {
	    fActiveShell= (Shell) e.widget;
	    updateButtonState();
	    if (fGiveFocusToFindField && getShell() == fActiveShell && okToUse(fFindField))
		fFindField.setFocus();
	}

	/*
	 * @see ShellListener#shellDeactivated(ShellEvent)
	 */
	public void shellDeactivated(ShellEvent e) {
	    fGiveFocusToFindField= false;
	    storeSettings();
	    fGlobalRadioButton.setSelection(true);
	    fSelectedRangeRadioButton.setSelection(false);
	    fUseSelectedLines= false;
	    //	    if (fTarget != null && (fTarget instanceof IFindReplaceTargetExtension))
	    //		((IFindReplaceTargetExtension) fTarget).setScope(null);
	    fOldScope= null;
	    fActiveShell= null;
	    updateButtonState();
	}
    }

    /**
     * Modify listener to update the search result in case of incremental search.
     * @since 2.0
     */
    private class FindModifyListener implements ModifyListener {
	/*
	 * @see ModifyListener#modifyText(ModifyEvent)
	 */
	public void modifyText(ModifyEvent e) {
	    fPattern= parsePattern(getFindString());
	    if (fPattern == null) {
		statusError("Invalid AST pattern");
		return;
	    }
	    statusMessage("");
	    if (isIncrementalSearch()) {
		if (fFindField.getText().equals("") && fTarget != null) { //$NON-NLS-1$
		    // empty selection at base location
		    int offset= fIncrementalBaseLocation.x;
		    if (isForwardSearch() && !fNeedsInitialFindBeforeReplace || !isForwardSearch() && fNeedsInitialFindBeforeReplace)
			offset= offset + fIncrementalBaseLocation.y;
		    fNeedsInitialFindBeforeReplace= false;
		    findAndSelect(offset, "", isForwardSearch()); //$NON-NLS-1$
		} else {
		    performSearch(false);
		}
	    }
	    updateButtonState(!isIncrementalSearch());
	}
    }

    /**
     * Stores the current state in the dialog settings.
     * @since 2.0
     */
    private void storeSettings() {
	fDialogPositionInit= getDialogBoundaries();
	fWrapInit= isWrapSearch();
	fIncrementalInit= isIncrementalSearch();
	fForwardInit= isForwardSearch();
	writeConfiguration();
    }

    /**
     * Attaches the given layout specification to the <code>component</code>.
     *
     * @param component the component
     * @param horizontalAlignment horizontal alignment
     * @param grabExcessHorizontalSpace grab excess horizontal space
     * @param verticalAlignment vertical alignment
     * @param grabExcessVerticalSpace grab excess vertical space
     */
    private void setGridData(Control component, int horizontalAlignment, boolean grabExcessHorizontalSpace, int verticalAlignment,
	    boolean grabExcessVerticalSpace) {
	GridData gd= new GridData();
	gd.horizontalAlignment= horizontalAlignment;
	gd.grabExcessHorizontalSpace= grabExcessHorizontalSpace;
	gd.verticalAlignment= verticalAlignment;
	gd.grabExcessVerticalSpace= grabExcessVerticalSpace;
	component.setLayoutData(gd);
    }

    /**
     * Returns the status line manager of the active editor or <code>null</code> if there is no such editor.
     * @return the status line manager of the active editor
     */
    private IEditorStatusLine getStatusLineManager() {
	IWorkbenchWindow window= PlatformUI.getWorkbench().getActiveWorkbenchWindow();
	if (window == null)
	    return null;
	IWorkbenchPage page= window.getActivePage();
	if (page == null)
	    return null;
	IEditorPart editor= page.getActiveEditor();
	if (editor == null)
	    return null;
	return (IEditorStatusLine) editor.getAdapter(IEditorStatusLine.class);
    }

    /**
     * Sets the given status message in the status line.
     *
     * @param error <code>true</code> if it is an error
     * @param message the error message
     */
    private void statusMessage(boolean error, String message) {
	fStatusLabel.setText(message);
	if (error)
	    fStatusLabel.setForeground(JFaceColors.getErrorText(fStatusLabel.getDisplay()));
	else
	    fStatusLabel.setForeground(null);
	IEditorStatusLine statusLine= getStatusLineManager();
	if (statusLine != null)
	    statusLine.setMessage(error, message, null);
	if (error)
	    getShell().getDisplay().beep();
    }

    /**
     * Sets the given error message in the status line.
     * @param message the message
     */
    private void statusError(String message) {
	statusMessage(true, message);
    }

    /**
     * Sets the given message in the status line.
     * @param message the message
     */
    private void statusMessage(String message) {
	statusMessage(false, message);
    }

    /**
     * Updates the enabled state of the buttons.
     */
    private void updateButtonState() {
	updateButtonState(false);
    }

    /**
     * Updates the enabled state of the buttons.
     *
     * @param disableReplace <code>true</code> if replace button must be disabled
     * @since 3.0
     */
    private void updateButtonState(boolean disableReplace) {
	if (okToUse(getShell()) && okToUse(fFindNextButton)) {
	    boolean selection= false;
	    if (fTarget != null) {
		String selectedText= fTarget.getSelectionText();
		selection= (selectedText != null && selectedText.length() > 0);
	    }
	    boolean enable= fTarget != null && (fActiveShell == fParentShell || fActiveShell == getShell());
	    String str= getFindString();
	    boolean findString= str != null && str.length() > 0;

	    fFindNextButton.setEnabled(enable && findString);
	    fReplaceSelectionButton.setEnabled(!disableReplace && enable && isEditable() && selection
		    && (!fNeedsInitialFindBeforeReplace));
	    fReplaceFindButton.setEnabled(!disableReplace && enable && isEditable() && findString && selection
		    && (!fNeedsInitialFindBeforeReplace));
	    fReplaceAllButton.setEnabled(enable && isEditable() && findString);
	}
    }

    /**
     * Returns <code>true</code> if control can be used.
     *
     * @param control the control to be checked
     * @return <code>true</code> if control can be used
     */
    private boolean okToUse(Control control) {
	return control != null && !control.isDisposed();
    }

    /**
     * Creates the options configuration section of the find replace dialog.
     *
     * @param parent the parent composite
     * @return the options configuration section
     */
    private Composite createConfigPanel(Composite parent) {
	Composite panel= new Composite(parent, SWT.NULL);
	GridLayout layout= new GridLayout();
	layout.numColumns= 2;
	layout.makeColumnsEqualWidth= true;
	panel.setLayout(layout);
	Composite directionGroup= createDirectionGroup(panel);
	setGridData(directionGroup, GridData.FILL, true, GridData.FILL, false);
	Composite scopeGroup= createScopeGroup(panel);
	setGridData(scopeGroup, GridData.FILL, true, GridData.FILL, false);
	Composite optionsGroup= createOptionsGroup(panel);
	setGridData(optionsGroup, GridData.FILL, true, GridData.FILL, false);
	GridData data= (GridData) optionsGroup.getLayoutData();
	data.horizontalSpan= 2;
	optionsGroup.setLayoutData(data);
	return panel;
    }

    /**
     * Creates the functional options part of the options defining
     * section of the find replace dialog.
     *
     * @param parent the parent composite
     * @return the options group
     */
    private Composite createOptionsGroup(Composite parent) {
	Composite panel= new Composite(parent, SWT.NULL);
	GridLayout layout= new GridLayout();
	layout.marginWidth= 0;
	layout.marginHeight= 0;
	panel.setLayout(layout);
	Group group= new Group(panel, SWT.SHADOW_NONE);
	group.setText("Options");
	GridLayout groupLayout= new GridLayout();
	groupLayout.numColumns= 2;
	groupLayout.makeColumnsEqualWidth= true;
	group.setLayout(groupLayout);
	group.setLayoutData(new GridData(GridData.FILL_BOTH));

	SelectionListener selectionListener= new SelectionListener() {
	    public void widgetSelected(SelectionEvent e) {
		storeSettings();
	    }

	    public void widgetDefaultSelected(SelectionEvent e) {}
	};

	fWrapCheckBox= new Button(group, SWT.CHECK | SWT.LEFT);
	fWrapCheckBox.setText("Wr&ap Search");
	setGridData(fWrapCheckBox, GridData.BEGINNING, false, GridData.CENTER, false);
	fWrapCheckBox.setSelection(fWrapInit);
	fWrapCheckBox.addSelectionListener(selectionListener);

	fIncrementalCheckBox= new Button(group, SWT.CHECK | SWT.LEFT);
	fIncrementalCheckBox.setText("&Incremental");
	setGridData(fIncrementalCheckBox, GridData.BEGINNING, false, GridData.CENTER, false);
	fIncrementalCheckBox.setSelection(fIncrementalInit);
	fIncrementalCheckBox.addSelectionListener(new SelectionListener() {
	    public void widgetSelected(SelectionEvent e) {
		if (isIncrementalSearch())
		    initIncrementalBaseLocation();
		storeSettings();
	    }

	    public void widgetDefaultSelected(SelectionEvent e) {}
	});
	fIncrementalCheckBox.setSelection(false);
	fIncrementalCheckBox.setEnabled(false);
	return panel;
    }

    /**
     * Initializes the anchor used as starting point for incremental searching.
     * @since 2.0
     */
    private void initIncrementalBaseLocation() {
	if (fTarget != null && isIncrementalSearch()) {
	    fIncrementalBaseLocation= fTarget.getSelection();
	} else {
	    fIncrementalBaseLocation= new Point(0, 0);
	}
    }

    /**
     * Creates the direction defining part of the options defining section
     * of the find replace dialog.
     *
     * @param parent the parent composite
     * @return the direction defining part
     */
    private Composite createDirectionGroup(Composite parent) {
	Composite panel= new Composite(parent, SWT.NONE);
	GridLayout layout= new GridLayout();
	layout.marginWidth= 0;
	layout.marginHeight= 0;
	panel.setLayout(layout);
	Group group= new Group(panel, SWT.SHADOW_ETCHED_IN);
	group.setText("Direction");
	GridLayout groupLayout= new GridLayout();
	group.setLayout(groupLayout);
	group.setLayoutData(new GridData(GridData.FILL_BOTH));
	SelectionListener selectionListener= new SelectionListener() {
	    public void widgetSelected(SelectionEvent e) {
		if (isIncrementalSearch())
		    initIncrementalBaseLocation();
	    }

	    public void widgetDefaultSelected(SelectionEvent e) {}
	};
	fForwardRadioButton= new Button(group, SWT.RADIO | SWT.LEFT);
	fForwardRadioButton.setText("F&orward");
	setGridData(fForwardRadioButton, GridData.BEGINNING, false, GridData.CENTER, false);
	fForwardRadioButton.addSelectionListener(selectionListener);
	Button backwardRadioButton= new Button(group, SWT.RADIO | SWT.LEFT);
	backwardRadioButton.setText("&Backward");
	setGridData(backwardRadioButton, GridData.BEGINNING, false, GridData.CENTER, false);
	backwardRadioButton.addSelectionListener(selectionListener);
	backwardRadioButton.setSelection(!fForwardInit);
	fForwardRadioButton.setSelection(fForwardInit);
	return panel;
    }

    /**
     * Creates the scope defining part of the find replace dialog.
     *
     * @param parent the parent composite
     * @return the scope defining part
     * @since 2.0
     */
    private Composite createScopeGroup(Composite parent) {
	Composite panel= new Composite(parent, SWT.NONE);
	GridLayout layout= new GridLayout();
	layout.marginWidth= 0;
	layout.marginHeight= 0;
	panel.setLayout(layout);
	Group group= new Group(panel, SWT.SHADOW_ETCHED_IN);
	group.setText("Scope");
	GridLayout groupLayout= new GridLayout();
	group.setLayout(groupLayout);
	group.setLayoutData(new GridData(GridData.FILL_BOTH));
	fGlobalRadioButton= new Button(group, SWT.RADIO | SWT.LEFT);
	fGlobalRadioButton.setText("All");
	setGridData(fGlobalRadioButton, GridData.BEGINNING, false, GridData.CENTER, false);
	fGlobalRadioButton.setSelection(fGlobalInit);
	fGlobalRadioButton.addSelectionListener(new SelectionListener() {
	    public void widgetSelected(SelectionEvent e) {
		if (!fGlobalRadioButton.getSelection() || !fUseSelectedLines)
		    return;
		fUseSelectedLines= false;
		//		useSelectedLines(false);
	    }

	    public void widgetDefaultSelected(SelectionEvent e) {}
	});
	fSelectedRangeRadioButton= new Button(group, SWT.RADIO | SWT.LEFT);
	fSelectedRangeRadioButton.setText("Selected lines");
	setGridData(fSelectedRangeRadioButton, GridData.BEGINNING, false, GridData.CENTER, false);
	fSelectedRangeRadioButton.setSelection(!fGlobalInit);
	fUseSelectedLines= !fGlobalInit;
	fSelectedRangeRadioButton.addSelectionListener(new SelectionListener() {
	    public void widgetSelected(SelectionEvent e) {
		if (!fSelectedRangeRadioButton.getSelection() || fUseSelectedLines)
		    return;
		fUseSelectedLines= true;
		useSelectedLines(true);
	    }

	    public void widgetDefaultSelected(SelectionEvent e) {}
	});
	return panel;
    }

    /*
     * @see org.eclipse.jface.window.Window#create()
     */
    public void create() {
	super.create();
	Shell shell= getShell();
	shell.addShellListener(fActivationListener);
	if (fLocation != null)
	    shell.setLocation(fLocation);
	// set help context
	PlatformUI.getWorkbench().getHelpSystem().setHelp(shell, IAbstractTextEditorHelpContextIds.FIND_REPLACE_DIALOG);
	// fill in combo contents
	fFindField.removeModifyListener(fFindModifyListener);
	updateCombo(fFindField, fFindHistory);
	fFindField.addModifyListener(fFindModifyListener);
	updateCombo(fReplaceField, fReplaceHistory);
	// get find string
	initFindString();
	// set dialog position
	if (fDialogPositionInit != null)
	    shell.setBounds(fDialogPositionInit);
	shell.setText("AST Find/Replace");
	// shell.setImage(null);
    }

    /**
     * Initializes the string to search for and the appropriate
     * text in the Find field based on the selection found in the
     * action's target.
     */
    private void initFindString() {
	if (fTarget != null && okToUse(fFindField)) {
	    fFindField.removeModifyListener(fFindModifyListener);
	    if ("".equals(fFindField.getText())) { //$NON-NLS-1$
		if (fFindHistory.size() > 0)
		    fFindField.setText((String) fFindHistory.get(0));
		else
		    fFindField.setText(""); //$NON-NLS-1$
	    }
	    fFindField.setSelection(new Point(0, fFindField.getText().length()));
	    fFindField.addModifyListener(fFindModifyListener);
	    fPattern= parsePattern(getFindString());
	}
    }

    /**
     * Tells the dialog to perform searches only in the scope given by the actually selected lines.
     * @param selectedLines <code>true</code> if selected lines should be used
     * @since 2.0
     */
    private void useSelectedLines(boolean selectedLines) {
	if (isIncrementalSearch())
	    initIncrementalBaseLocation();
	if (fTarget == null || !(fTarget instanceof IFindReplaceTargetExtension))
	    return;
	IFindReplaceTargetExtension extensionTarget= (IFindReplaceTargetExtension) fTarget;
	if (selectedLines) {
	    IRegion scope;
	    if (fOldScope == null) {
		Point lineSelection= extensionTarget.getLineSelection();
		scope= new Region(lineSelection.x, lineSelection.y);
	    } else {
		scope= fOldScope;
		fOldScope= null;
	    }
	    int offset= isForwardSearch() ? scope.getOffset() : scope.getOffset() + scope.getLength();
	    extensionTarget.setSelection(offset, 0);
	    extensionTarget.setScope(scope);
	} else {
	    fOldScope= extensionTarget.getScope();
	    extensionTarget.setScope(null);
	}
    }

    /**
     * Updates the given combo with the given content.
     * @param combo combo to be updated
     * @param content to be put into the combo
     */
    private void updateCombo(Combo combo, List content) {
	combo.removeAll();
	for(int i= 0; i < content.size(); i++) {
	    combo.add(content.get(i).toString());
	}
    }

    protected Control createContents(Composite parent) {
	Composite panel= new Composite(parent, SWT.NULL);
	GridLayout layout= new GridLayout();
	layout.numColumns= 1;
	layout.makeColumnsEqualWidth= true;
	panel.setLayout(layout);
	panel.setLayoutData(new GridData(GridData.FILL_BOTH));
	Composite inputPanel= createInputPanel(panel);
	setGridData(inputPanel, GridData.FILL, true, GridData.CENTER, false);
	Composite configPanel= createConfigPanel(panel);
	setGridData(configPanel, GridData.FILL, true, GridData.CENTER, true);
	Composite buttonPanelB= createButtonSection(panel);
	setGridData(buttonPanelB, GridData.FILL, true, GridData.CENTER, false);
	Composite statusBar= createStatusAndCloseButton(panel);
	setGridData(statusBar, GridData.FILL, true, GridData.CENTER, false);
	updateButtonState();
	applyDialogFont(panel);
	return panel;
    }

    /**
     * Creates a button.
     * @param parent the parent control
     * @param label the button label
     * @param id the button id
     * @param dfltButton is this button the default button
     * @param listener a button pressed listener
     * @return the new button
     */
    private Button makeButton(Composite parent, String label, int id, boolean dfltButton, SelectionListener listener) {
	Button b= createButton(parent, id, label, dfltButton);
	b.addSelectionListener(listener);
	return b;
    }

    /**
     * Create the button section of the find/replace dialog.
     *
     * @param parent the parent composite
     * @return the button section
     */
    private Composite createButtonSection(Composite parent) {
	Composite panel= new Composite(parent, SWT.NULL);
	GridLayout layout= new GridLayout();
	layout.numColumns= -2;
	layout.makeColumnsEqualWidth= true;
	panel.setLayout(layout);
	fFindNextButton= makeButton(panel, "Find &Next", 102, true, new SelectionAdapter() {
	    public void widgetSelected(SelectionEvent e) {
		if (isIncrementalSearch())
		    initIncrementalBaseLocation();
		fNeedsInitialFindBeforeReplace= false;
		performSearch();
		updateFindHistory();
		fFindNextButton.setFocus();
	    }
	});
	setGridData(fFindNextButton, GridData.FILL, true, GridData.FILL, false);
	fReplaceFindButton= makeButton(panel, "Replace/Fin&d", 103, false, new SelectionAdapter() {
	    public void widgetSelected(SelectionEvent e) {
		if (fNeedsInitialFindBeforeReplace)
		    performSearch();
		if (performReplaceSelection())
		    performSearch();
		updateFindAndReplaceHistory();
		fReplaceFindButton.setFocus();
	    }
	});
	setGridData(fReplaceFindButton, GridData.FILL, true, GridData.FILL, false);
	fReplaceSelectionButton= makeButton(panel, "&Replace", 104, false, new SelectionAdapter() {
	    public void widgetSelected(SelectionEvent e) {
		if (fNeedsInitialFindBeforeReplace)
		    performSearch();
		performReplaceSelection();
		updateFindAndReplaceHistory();
		fFindNextButton.setFocus();
	    }
	});
	setGridData(fReplaceSelectionButton, GridData.FILL, true, GridData.FILL, false);
	fReplaceAllButton= makeButton(panel, "Replace &All", 105, false, new SelectionAdapter() {
	    public void widgetSelected(SelectionEvent e) {
		performReplaceAll();
		updateFindAndReplaceHistory();
		fFindNextButton.setFocus();
	    }
	});
	setGridData(fReplaceAllButton, GridData.FILL, true, GridData.FILL, false);
	// Make the all the buttons the same size as the Remove Selection button.
	fReplaceAllButton.setEnabled(isEditable());
	return panel;
    }

    /**
     * Creates the panel where the user specifies the text to search
     * for and the optional replacement text.
     *
     * @param parent the parent composite
     * @return the input panel
     */
    private Composite createInputPanel(Composite parent) {
	ModifyListener listener= new ModifyListener() {
	    public void modifyText(ModifyEvent e) {
		updateButtonState();
	    }
	};
	Composite panel= new Composite(parent, SWT.NULL);
	GridLayout layout= new GridLayout();
	layout.numColumns= 2;
	panel.setLayout(layout);
	Label findLabel= new Label(panel, SWT.LEFT);
	findLabel.setText("&Find:");
	setGridData(findLabel, GridData.BEGINNING, false, GridData.CENTER, false);
	fFindField= new Combo(panel, SWT.DROP_DOWN | SWT.BORDER);
	setGridData(fFindField, GridData.FILL, true, GridData.CENTER, false);
	fFindField.addModifyListener(fFindModifyListener);
	fReplaceLabel= new Label(panel, SWT.LEFT);
	fReplaceLabel.setText("Replace &with:");
	setGridData(fReplaceLabel, GridData.BEGINNING, false, GridData.CENTER, false);
	fReplaceField= new Combo(panel, SWT.DROP_DOWN | SWT.BORDER);
	setGridData(fReplaceField, GridData.FILL, true, GridData.CENTER, false);
	fReplaceField.addModifyListener(listener);
	return panel;
    }

    /**
     * Creates the status and close section of the dialog.
     *
     * @param parent the parent composite
     * @return the status and close button
     */
    private Composite createStatusAndCloseButton(Composite parent) {
	Composite panel= new Composite(parent, SWT.NULL);
	GridLayout layout= new GridLayout();
	layout.numColumns= 2;
	layout.marginWidth= 0;
	layout.marginHeight= 0;
	panel.setLayout(layout);
	fStatusLabel= new Label(panel, SWT.LEFT);
	setGridData(fStatusLabel, GridData.FILL, true, GridData.CENTER, false);
	String label= "Close";
	Button closeButton= createButton(panel, 101, label, false);
	setGridData(closeButton, GridData.END, false, GridData.END, false);
	return panel;
    }

    protected void buttonPressed(int buttonID) {
	if (buttonID == 101)
	    close();
    }

    /**
     * Called after executed find/replace action to update the history.
     */
    private void updateFindAndReplaceHistory() {
	updateFindHistory();
	if (okToUse(fReplaceField)) {
	    updateHistory(fReplaceField, fReplaceHistory);
	}
    }

    /**
     * Called after executed find action to update the history.
     */
    private void updateFindHistory() {
	if (okToUse(fFindField)) {
	    fFindField.removeModifyListener(fFindModifyListener);
	    updateHistory(fFindField, fFindHistory);
	    fFindField.addModifyListener(fFindModifyListener);
	}
    }

    /**
     * Updates the combo with the history.
     * @param combo to be updated
     * @param history to be put into the combo
     */
    private void updateHistory(Combo combo, List history) {
	String findString= combo.getText();
	int index= history.indexOf(findString);
	if (index != 0) {
	    if (index != -1) {
		history.remove(index);
	    }
	    history.add(0, findString);
	    updateCombo(combo, history);
	    combo.setText(findString);
	}
    }

    /**
     * Returns whether the target is editable.
     * @return <code>true</code> if target is editable
     */
    private boolean isEditable() {
	boolean isEditable= (fTarget == null ? false : fTarget.isEditable());
	return fIsTargetEditable && isEditable;
    }

    /**
     * Retrieves and returns the option search direction from the appropriate check box.
     * @return <code>true</code> if searching forward
     */
    private boolean isForwardSearch() {
	if (okToUse(fForwardRadioButton)) {
	    return fForwardRadioButton.getSelection();
	}
	return fForwardInit;
    }

    /**
     * Retrieves and returns the option wrap search from the appropriate check box.
     * @return <code>true</code> if wrapping while searching
     */
    private boolean isWrapSearch() {
	if (okToUse(fWrapCheckBox)) {
	    return fWrapCheckBox.getSelection();
	}
	return fWrapInit;
    }

    /**
     * Retrieves and returns the option incremental search from the appropriate check box.
     * @return <code>true</code> if incremental search
     * @since 2.0
     */
    private boolean isIncrementalSearch() {
	if (okToUse(fIncrementalCheckBox)) {
	    return fIncrementalCheckBox.getSelection();
	}
	return fIncrementalInit;
    }

    /**
     * Returns the dialog's boundaries.
     * @return the dialog's boundaries
     */
    private Rectangle getDialogBoundaries() {
	if (okToUse(getShell()))
	    return getShell().getBounds();
	return fDialogPositionInit;
    }

    /**
     * Returns the dialog's history.
     * @return the dialog's history
     */
    private List getFindHistory() {
	return fFindHistory;
    }

    // ------- accessors ---------------------------------------
    /**
     * Retrieves the string to search for from the appropriate text input field and returns it.
     * @return the search string
     */
    private String getFindString() {
	if (okToUse(fFindField)) {
	    return fFindField.getText();
	}
	return ""; //$NON-NLS-1$
    }

    /**
     * Returns the dialog's replace history.
     * @return the dialog's replace history
     */
    private List getReplaceHistory() {
	return fReplaceHistory;
    }

    /**
     * Retrieves the replacement string from the appropriate text input field and returns it.
     * @return the replacement string
     */
    private String getReplaceString() {
	if (okToUse(fReplaceField)) {
	    return fReplaceField.getText();
	}
	return ""; //$NON-NLS-1$
    }

    /**
     * Locates the user's findString in the text of the target.
     */
    private void performSearch() {
	performSearch(isIncrementalSearch());
    }

    private void performSearch(boolean incremental) {
        IParseController parseController= fTarget.getParseController();
        Object srcAST= parseController.getCurrentAst();

        Point curSel= fTarget.getSelection();
        int endSelPos= curSel.x + curSel.y;
        Matcher matcher= new Matcher(fPattern);
        MatchResult m= fASTAdapter.findNextMatch(matcher, srcAST, endSelPos);

        if (m == null && isWrapSearch())
            m= fASTAdapter.findNextMatch(matcher, srcAST, -1);

        if (m != null) {
            int matchPos= fASTAdapter.getOffset(m.getMatchNode());
            int matchLen= fASTAdapter.getLength(m.getMatchNode());

            fStatusLabel.setText("Found match at " + matchPos);
//            ((ITextEditor) fTarget).getSelectionProvider().setSelection(new TextSelection(matchPos, matchLen));
            ((ITextEditor) fTarget).selectAndReveal(matchPos, matchLen);
        } else
            fStatusLabel.setText("No match");
    }

    private Pattern parsePattern(String patternStr) {
	ASTPatternLexer lexer= new ASTPatternLexer(patternStr.toCharArray(), "__PATTERN__");
        ASTPatternParser parser= new ASTPatternParser(lexer.getLexStream());
    
        lexer.lexer(parser); // Why wasn't this done by the parser ctor?
	return (Pattern) parser.parser();
    }

    protected void performReplaceAll() {
	// TODO Auto-generated method stub
    }

    protected boolean performReplaceSelection() {
	// TODO Auto-generated method stub
	return false;
    }

    private void findAndSelect(int offset, String string, boolean b) {
    // TODO Auto-generated method stub
    }

    private void setContentAssistsEnablement(boolean enable) {
	if (enable) {
	    if (fFindContentAssistHandler == null) {
		fFindContentAssistHandler= ContentAssistHandler.createHandlerForCombo(fFindField, createContentAssistant(true));
		fReplaceContentAssistHandler= ContentAssistHandler.createHandlerForCombo(fReplaceField, createContentAssistant(false));
	    }
	    fFindContentAssistHandler.setEnabled(true);
	    fReplaceContentAssistHandler.setEnabled(true);
	} else {
	    if (fFindContentAssistHandler == null)
		return;
	    fFindContentAssistHandler.setEnabled(false);
	    fReplaceContentAssistHandler.setEnabled(false);
	}
    }

    /**
     * Create a new regex content assistant.
     * @param isFind <code>true</code> iff the processor is for the find field.
     *                <code>false</code> iff the processor is for the replace field.
     *
     * @return a new configured content assistant
     * @since 3.0
     */
    private SubjectControlContentAssistant createContentAssistant(boolean isFind) {
	final SubjectControlContentAssistant contentAssistant= new SubjectControlContentAssistant();
	contentAssistant.setRestoreCompletionProposalSize(getSettings("FindReplaceDialog.completion_proposal_size")); //$NON-NLS-1$
	// TODO RMF Need a content assist processor for the pattern language...
	// IContentAssistProcessor processor= new RegExContentAssistProcessor(isFind);
	// contentAssistant.setContentAssistProcessor(processor, IDocument.DEFAULT_CONTENT_TYPE);
	contentAssistant.enableAutoActivation(true);
	contentAssistant.setProposalSelectorBackground(fProposalPopupBackgroundColor);
	contentAssistant.setProposalSelectorForeground(fProposalPopupForegroundColor);
	contentAssistant.setContextInformationPopupOrientation(IContentAssistant.CONTEXT_INFO_ABOVE);
	contentAssistant.setInformationControlCreator(new IInformationControlCreator() {
	    /*
	     * @see org.eclipse.jface.text.IInformationControlCreator#createInformationControl(org.eclipse.swt.widgets.Shell)
	     */
	    public IInformationControl createInformationControl(Shell parent) {
		return new DefaultInformationControl(parent);
	    }
	});
	return contentAssistant;
    }

    //--------------- configuration handling --------------
    /**
     * Returns the dialog settings object used to share state
     * between several find/replace dialogs.
     *
     * @return the dialog settings to be used
     */
    private IDialogSettings getDialogSettings() {
	IDialogSettings settings= XformPlugin.getDefault().getDialogSettings();
	fDialogSettings= settings.getSection(getClass().getName());
	if (fDialogSettings == null)
	    fDialogSettings= settings.addNewSection(getClass().getName());
	return fDialogSettings;
    }

    /**
     * Initializes itself from the dialog settings with the same state
     * as at the previous invocation.
     */
    private void readConfiguration() {
	IDialogSettings s= getDialogSettings();
	try {
	    int x= s.getInt("x"); //$NON-NLS-1$
	    int y= s.getInt("y"); //$NON-NLS-1$
	    fLocation= new Point(x, y);
	} catch (NumberFormatException e) {
	    fLocation= null;
	}
	fWrapInit= s.getBoolean("wrap"); //$NON-NLS-1$
	fIncrementalInit= s.getBoolean("incremental"); //$NON-NLS-1$
	String[] findHistory= s.getArray("findhistory"); //$NON-NLS-1$
	if (findHistory != null) {
	    List history= getFindHistory();
	    history.clear();
	    for(int i= 0; i < findHistory.length; i++)
		history.add(findHistory[i]);
	}
	String[] replaceHistory= s.getArray("replacehistory"); //$NON-NLS-1$
	if (replaceHistory != null) {
	    List history= getReplaceHistory();
	    history.clear();
	    for(int i= 0; i < replaceHistory.length; i++)
		history.add(replaceHistory[i]);
	}
    }

    /**
     * Stores its current configuration in the dialog store.
     */
    private void writeConfiguration() {
	IDialogSettings s= getDialogSettings();
	Point location= getShell().getLocation();
	s.put("x", location.x); //$NON-NLS-1$
	s.put("y", location.y); //$NON-NLS-1$
	s.put("wrap", fWrapInit); //$NON-NLS-1$
	s.put("incremental", fIncrementalInit); //$NON-NLS-1$
	List history= getFindHistory();
	while (history.size() > 8)
	    history.remove(8);
	String[] names= new String[history.size()];
	history.toArray(names);
	s.put("findhistory", names); //$NON-NLS-1$
	history= getReplaceHistory();
	while (history.size() > 8)
	    history.remove(8);
	names= new String[history.size()];
	history.toArray(names);
	s.put("replacehistory", names); //$NON-NLS-1$
    }

    private IDialogSettings getSettings(String sectionName) {
	IDialogSettings pluginDialogSettings= XformPlugin.getDefault().getDialogSettings();
	IDialogSettings settings= pluginDialogSettings.getSection(sectionName);
	if (settings == null)
	    settings= pluginDialogSettings.addNewSection(sectionName);
	return settings;
    }

    /**
     * Updates this dialog because of a different target.
     * @param target the new target
     * @param isTargetEditable <code>true</code> if the new target can be modified
     * @param initializeFindString <code>true</code> if the find string of this dialog should be initialized based on the viewer's selection
     * @since 2.0
     */
    public void updateTarget(IASTFindReplaceTarget target, boolean isTargetEditable, boolean initializeFindString) {
	fIsTargetEditable= isTargetEditable;
	fNeedsInitialFindBeforeReplace= true;
	if (target != fTarget) {
	    if (fTarget != null && fTarget instanceof IFindReplaceTargetExtension)
		((IFindReplaceTargetExtension) fTarget).endSession();
	    fTarget= target;
	    if (fTarget instanceof IFindReplaceTargetExtension) {
		((IFindReplaceTargetExtension) fTarget).beginSession();
		fGlobalInit= true;
		fGlobalRadioButton.setSelection(fGlobalInit);
		fSelectedRangeRadioButton.setSelection(!fGlobalInit);
		fUseSelectedLines= !fGlobalInit;
	    } else if (fTarget == null) {
		updateButtonState();
	    }
	}
	if (fTarget != null) {
	    IEditorInput editorInput= ((ITextEditor) fTarget).getEditorInput();

	    fASTAdapter= (IASTAdapter) ExtensionPointFactory.createExtensionPoint(LanguageRegistry.findLanguage(editorInput), XformPlugin.kPluginID, "astAdapter");
	    ASTPatternParser.setASTAdapter(fASTAdapter);
	}

	if (false && okToUse(fIncrementalCheckBox))
	    fIncrementalCheckBox.setEnabled(true);
	if (okToUse(fReplaceLabel)) {
	    fReplaceLabel.setEnabled(isEditable());
	    fReplaceField.setEnabled(isEditable());
	    if (initializeFindString) {
		initFindString();
		fGiveFocusToFindField= true;
	    }
	    initIncrementalBaseLocation();
	    updateButtonState();
	}
	setContentAssistsEnablement(true);
    }
}
