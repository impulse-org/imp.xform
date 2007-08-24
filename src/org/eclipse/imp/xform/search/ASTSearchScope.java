/*
 * (C) Copyright IBM Corporation 2007
 * 
 * This file is part of the Eclipse IMP.
 */
package org.eclipse.imp.xform.search;

import java.util.HashSet;
import java.util.Set;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;

public class ASTSearchScope {
    Set fProjects= new HashSet();

    private ASTSearchScope() { }

    private ASTSearchScope(IProject p) {
        fProjects.add(p);
    }

    public void addProject(IProject project) {
        fProjects.add(project);
    }

    public Set/*<IProject>*/ getProjects() {
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
