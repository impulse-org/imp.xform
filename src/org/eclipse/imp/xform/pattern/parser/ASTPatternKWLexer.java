
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2007 IBM Corporation.
// All rights reserved. This program and the accompanying materials
// are made available under the terms of the Eclipse Public License v1.0
// which accompanies this distribution, and is available at
// http://www.eclipse.org/legal/epl-v10.html
//
//Contributors:
//    Philippe Charles (pcharles@us.ibm.com) - initial API and implementation

////////////////////////////////////////////////////////////////////////////////

package org.eclipse.imp.xform.pattern.parser;

import lpg.runtime.*;

public class ASTPatternKWLexer extends ASTPatternKWLexerprs
{
    private char[] inputChars;
    private final int keywordKind[] = new int[3 + 1];

    public int[] getKeywordKinds() { return keywordKind; }

    public int lexer(int curtok, int lasttok)
    {
        int current_kind = getKind(inputChars[curtok]),
            act;

        for (act = tAction(START_STATE, current_kind);
             act > NUM_RULES && act < ACCEPT_ACTION;
             act = tAction(act, current_kind))
        {
            curtok++;
            current_kind = (curtok > lasttok
                                   ? ASTPatternKWLexersym.Char_EOF
                                   : getKind(inputChars[curtok]));
        }

        if (act > ERROR_ACTION)
        {
            curtok++;
            act -= ERROR_ACTION;
        }

        return keywordKind[act == ERROR_ACTION  || curtok <= lasttok ? 0 : act];
    }

    public void setInputChars(char[] inputChars) { this.inputChars = inputChars; }


    final static int tokenKind[] = new int[128];
    static
    {
        tokenKind['$'] = ASTPatternKWLexersym.Char_DollarSign;
        tokenKind['_'] = ASTPatternKWLexersym.Char__;

        tokenKind['a'] = ASTPatternKWLexersym.Char_a;
        tokenKind['b'] = ASTPatternKWLexersym.Char_b;
        tokenKind['c'] = ASTPatternKWLexersym.Char_c;
        tokenKind['d'] = ASTPatternKWLexersym.Char_d;
        tokenKind['e'] = ASTPatternKWLexersym.Char_e;
        tokenKind['f'] = ASTPatternKWLexersym.Char_f;
        tokenKind['g'] = ASTPatternKWLexersym.Char_g;
        tokenKind['h'] = ASTPatternKWLexersym.Char_h;
        tokenKind['i'] = ASTPatternKWLexersym.Char_i;
        tokenKind['j'] = ASTPatternKWLexersym.Char_j;
        tokenKind['k'] = ASTPatternKWLexersym.Char_k;
        tokenKind['l'] = ASTPatternKWLexersym.Char_l;
        tokenKind['m'] = ASTPatternKWLexersym.Char_m;
        tokenKind['n'] = ASTPatternKWLexersym.Char_n;
        tokenKind['o'] = ASTPatternKWLexersym.Char_o;
        tokenKind['p'] = ASTPatternKWLexersym.Char_p;
        tokenKind['q'] = ASTPatternKWLexersym.Char_q;
        tokenKind['r'] = ASTPatternKWLexersym.Char_r;
        tokenKind['s'] = ASTPatternKWLexersym.Char_s;
        tokenKind['t'] = ASTPatternKWLexersym.Char_t;
        tokenKind['u'] = ASTPatternKWLexersym.Char_u;
        tokenKind['v'] = ASTPatternKWLexersym.Char_v;
        tokenKind['w'] = ASTPatternKWLexersym.Char_w;
        tokenKind['x'] = ASTPatternKWLexersym.Char_x;
        tokenKind['y'] = ASTPatternKWLexersym.Char_y;
        tokenKind['z'] = ASTPatternKWLexersym.Char_z;
    };

    final int getKind(int c)
    {
        return ((c & 0xFFFFFF80) == 0 /* 0 <= c < 128? */ ? tokenKind[c] : 0);
    }


    public ASTPatternKWLexer(char[] inputChars, int identifierKind)
    {
        this.inputChars = inputChars;
        keywordKind[0] = identifierKind;

        //
        // Rule 1:  Keyword ::= d e f i n e
        //
        
        keywordKind[1] = (ASTPatternParsersym.TK_DEFINE);
      
    
        //
        // Rule 2:  Keyword ::= t y p e O f
        //
        
        keywordKind[2] = (ASTPatternParsersym.TK_typeOf);
      
    
        //
        // Rule 3:  Keyword ::= t a r g e t T y p e O f
        //
        
        keywordKind[3] = (ASTPatternParsersym.TK_targetTypeOf);
      
    
        for (int i = 0; i < keywordKind.length; i++)
        {
            if (keywordKind[i] == 0)
                keywordKind[i] = identifierKind;
        }
    }
}

