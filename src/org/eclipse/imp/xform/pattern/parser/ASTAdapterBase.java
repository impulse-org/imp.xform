/*
 * (C) Copyright IBM Corporation 2007
 * 
 * This file is part of the Eclipse IMP.
 */
/*
 * Created on Jun 6, 2006
 */
package org.eclipse.imp.xform.pattern.parser;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.eclipse.imp.xform.pattern.matching.IASTAdapter;
import org.eclipse.imp.xform.pattern.matching.MatchResult;
import org.eclipse.imp.xform.pattern.matching.Matcher;
 
public abstract class ASTAdapterBase implements IASTAdapter {
    private static Object[] EMPTY= new Object[0];

    /**
     * Never throws an exception - it is reasonable for a pattern to constrain
     * via an attribute that not all concrete nodes have.
     * @return the given attribute of the given ast node, or null if the given
     * kind of ast node has no such attribute
     */
    public final Object getValue(String attributeName, Object astNode) {
        if (attributeName.equals(KIND))
            return getKind(astNode);
        if (attributeName.equals(TARGET_TYPE))
            return getTargetType(astNode);
        if (attributeName.equals(NAME))
            return getName(astNode);
        return getChildByRole(attributeName, astNode);
    }

    /**
     * Never throws an exception - it is reasonable for a pattern to constrain
     * via an attribute that not all concrete nodes have.
     * @return the type in the target program of the given node, or null if this kind
     * of node has no "target type"
     */
    protected Object getTargetType(Object astNode) {
	return null;
    }

    /**
     * Never throws an exception - it is reasonable for a pattern to constrain
     * via an attribute that not all concrete nodes have.
     * @return the name in the target program of the given node, or null if this kind
     * of node has no "name" (e.g. literal expressions)
     */
    protected String getName(Object astNode) {
	return null;
    }

    protected Object getKind(Object astNode) {
	final String name= astNode.getClass().getName();

	return name.substring(name.lastIndexOf('.'));
    }

    /**
     * Never throws an exception - it is reasonable for a pattern to constrain
     * via an attribute that not all concrete nodes have.
     * @return the child with the given role name of the given ast node,
     * or null if the given kind of ast node has no such attribute
     */
    protected Object getChildByRole(String roleName, Object astNode) {
	Class clazz= astNode.getClass();

        try {
            while (clazz != null) {
        	try {
        	    Field field= clazz.getDeclaredField(roleName);

        	    if (field != null && (field.getModifiers() & Modifier.STATIC) == 0) {
        		boolean save= field.isAccessible();
        		field.setAccessible(true);
        		Object result= field.get(astNode);
        		field.setAccessible(save);
        		return result;
        	    }
        	} catch (NoSuchFieldException e) {
        	    // do nothing
        	}
        	clazz= clazz.getSuperclass();
            }
	} catch (IllegalArgumentException e) {
	} catch (IllegalAccessException e) {
	}
	return null;
    }

    public Object[] getChildren(Object astNode) { return EMPTY; }

    public boolean isInstanceOfType(Object astNode, String typeName) {return false; }

    public Set findAllMatches(Matcher matcher, Object astRoot) {
        return Collections.EMPTY_SET;
    }

    public MatchResult findNextMatch(Matcher matcher, Object astRoot, int offset) {
        return null;
    }

    public String getFile(Object astNode) {
	return null;
    }

    public int getOffset(Object astNode) {
	return 0;
    }

    public int getLength(Object astNode) {
	return 0;
    }

    public String lookupSimpleNodeType(String simpleName) {
        return null;
    }

    public boolean isSubTypeOf(String maybeSuper, String maybeSub) {
	try {
	    return Class.forName(maybeSuper).isAssignableFrom(Class.forName(maybeSuper));
	} catch (ClassNotFoundException e) {
	    assert(false): e.getMessage();
	    return false;
	}
    }

    public String getTypeOf(Object astNode) {
        return null;
    }

    public Object construct(String qualName, Object[] children) throws IllegalArgumentException {
        return null;
    }

    public Object construct(String qualName, Object[] children, Map attribs) throws IllegalArgumentException {
        return null;
    }

    /**
     * @param pos the 0-based field index
     */
    public Object getChildAtPosition(int pos, Object astNode) {
        Class clazz= astNode.getClass();
        Stack<Field[]> fieldStack= new Stack<Field[]>();
        Stack<Integer> instanceFieldCounts= new Stack<Integer>();

        while (clazz != null) {
            final Field[] declaredFields= clazz.getDeclaredFields();
            fieldStack.push(declaredFields);
            int count= 0;
            for(int i= 0; i < declaredFields.length; i++) {
        	if ((declaredFields[i].getModifiers() & Modifier.STATIC) != 0)
        	    count++;
            }
            instanceFieldCounts.push(count);
            clazz= clazz.getSuperclass();
        }
        Field[] fields;
        int instFieldCount;
        int idx= pos;
        for(fields= fieldStack.pop(), instFieldCount= instanceFieldCounts.pop(); idx >= instFieldCount; idx -= instFieldCount, fields= fieldStack.pop(), instFieldCount= instanceFieldCounts.pop())
            ;
        for(int i= 0; i < fields.length; i++) {
            if ((fields[i].getModifiers() & Modifier.STATIC) != 0) {
        	if (idx-- == 0)
        	    return fields[i];
            }
        }
        throw new IllegalArgumentException("Invalid child index " + pos + " for type " + astNode.getClass().getName());
    }

    public String getChildRoleAtPosition(int pos, String qualNodeType) {
        try {
            return Class.forName(qualNodeType).getFields()[pos].getName();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public int getPositionOfChildRole(String roleName, String qualNodeType) {
        try {
            Class clazz= Class.forName(qualNodeType);
            Field[] fields= clazz.getFields();

            for(int i= 0; i < fields.length; i++) {
        	if (fields[i].getName().equals(roleName))
        	    return i;
            }
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    
        if (roleName.equals(KIND) || roleName.equals(TARGET_TYPE))
            throw new IllegalArgumentException(roleName);
        return -1;
    }
    
    public boolean isMetaVariable(Object astNode) {
    	return astNode instanceof IASTPlaceholder;
    }
    
    public String getMetaVariableName(Object astNode) {
    	return ((IASTPlaceholder) astNode).getName();
    }
}