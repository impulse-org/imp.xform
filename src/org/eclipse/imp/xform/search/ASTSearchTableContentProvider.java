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

import java.util.HashSet;
import java.util.Set;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.search.ui.text.Match;

public class ASTSearchTableContentProvider extends ASTSearchContentProvider {
    public ASTSearchTableContentProvider(ASTSearchResultPage page) {
        super(page);
    }

    public Object[] getElements(Object inputElement) {
        if (inputElement instanceof ASTSearchResult) {
            ASTSearchResult result= (ASTSearchResult) inputElement;
            Set filteredElements= new HashSet();
            Object[] fileElements= result.getElements();

            for(int i= 0; i < fileElements.length; i++) {
                // if (getPage().getDisplayedMatchCount(rawElements[i]) > 0)
        	Match[] matches= result.getMatches(fileElements[i]);

        	for(int j= 0; j < matches.length; j++) {
        	    filteredElements.add(matches[j]);
		}
            }
            return filteredElements.toArray();
        }
        return EMPTY_ARR;
    }

    public void elementsChanged(Object[] updatedElements) {
        if (fResult == null)
            return;
        int addCount= 0;
        int removeCount= 0;
        TableViewer viewer= (TableViewer) getPage().getViewer();
        Set updated= new HashSet();
        Set added= new HashSet();
        Set removed= new HashSet();
        for(int i= 0; i < updatedElements.length; i++) {
            if (getPage().getDisplayedMatchCount(updatedElements[i]) > 0) {
                if (viewer.testFindItem(updatedElements[i]) != null)
                    updated.add(updatedElements[i]);
                else
                    added.add(updatedElements[i]);
                addCount++;
            } else {
                removed.add(updatedElements[i]);
                removeCount++;
            }
        }

        viewer.add(added.toArray());
        viewer.update(updated.toArray(), null /*new String[] { SearchLabelProvider.PROPERTY_MATCH_COUNT } */);
        viewer.remove(removed.toArray());
    }

    public void clear() {
        getPage().getViewer().refresh();
    }
}
