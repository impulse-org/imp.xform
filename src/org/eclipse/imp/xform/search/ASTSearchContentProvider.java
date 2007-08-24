/*
 * (C) Copyright IBM Corporation 2007
 * 
 * This file is part of the Eclipse IMP.
 */
package org.eclipse.imp.xform.search;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public abstract class ASTSearchContentProvider implements IStructuredContentProvider {
    protected static final Object[] EMPTY_ARR= new Object[0];

    protected ASTSearchResult fResult;

    protected ASTSearchResultPage fPage;

    public ASTSearchContentProvider(ASTSearchResultPage page) {
        fPage= page;
    }

    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        initialize((ASTSearchResult) newInput);
    }

    protected void initialize(ASTSearchResult result) {
        fResult= result;
    }

    public abstract void elementsChanged(Object[] updatedElements);

    public abstract void clear();

    public void dispose() {
        // nothing to do
    }

    protected ASTSearchResultPage getPage() {
        return fPage;
    }
}
