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
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;

public class ASTSearchScope {
    Set<IProject> fProjects= new HashSet<IProject>();

    private ASTSearchScope() { }

    private ASTSearchScope(IProject p) {
        fProjects.add(p);
    }

    public void addProject(IProject project) {
        fProjects.add(project);
    }

    public Set<IProject> getProjects() {
        return fProjects;
    }

    public static ASTSearchScope createWorkspaceScope() {
        ASTSearchScope scope= new ASTSearchScope();
        IProject[] projects= ResourcesPlugin.getWorkspace().getRoot().getProjects();

        for(int i= 0; i < projects.length; i++) {
            scope.addProject(projects[i]);
        }
        return scope;
    }

    public static ASTSearchScope createProjectScope(IProject project) {
        return new ASTSearchScope(project);
    }
}
