package com.ibm.watson.safari.xform.search;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.DialogPage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.search.ui.ISearchPage;
import org.eclipse.search.ui.ISearchPageContainer;
import org.eclipse.search.ui.NewSearchUI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class ASTSearchPage extends DialogPage implements ISearchPage {
    private ISearchPageContainer fContainer;

    private Text fPattern;

    public ASTSearchPage() {
	this("AST Search");
    }

    public ASTSearchPage(String title) {
	this(title, null);
    }

    public ASTSearchPage(String title, ImageDescriptor image) {
	super(title, image);
    }

    public boolean performAction() {
        IProject project= ResourcesPlugin.getWorkspace().getRoot().getProject("JikesPGTest");
        boolean isWorkspaceScope= fContainer.getSelectedScope() == ISearchPageContainer.WORKSPACE_SCOPE;
        ASTSearchScope scope= isWorkspaceScope ? ASTSearchScope.createWorkspaceScope() :
            ASTSearchScope.createProjectScope(project);
        ASTSearchQuery query= new ASTSearchQuery(fPattern.getText(), "jikespg", scope);

        NewSearchUI.activateSearchResultView();
        NewSearchUI.runQueryInBackground(query);
        return true;
    }

    public void setContainer(ISearchPageContainer container) {
	fContainer= container;
    }

    public void createControl(Composite parent) {
        Composite result= new Composite(parent, SWT.NONE);

        GridLayout layout= new GridLayout(2, false);
        layout.horizontalSpacing= 10;
        result.setLayout(layout);

        Label label= new Label(result, SWT.LEFT);
        label.setText("AST pattern:"); 
        label.setLayoutData(new GridData(GridData.FILL, GridData.FILL, false, false, 2, 1));

        fPattern= new Text(result, SWT.LEFT | SWT.BORDER);
        fPattern.setText("");

        setControl(result);
    }
}
