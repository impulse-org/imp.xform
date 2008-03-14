/*******************************************************************************
* Copyright (c) 2007 IBM Corporation.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*    Robert Fuhrer (rfuhrer@watson.ibm.com) - initial API and implementation

*******************************************************************************/

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
