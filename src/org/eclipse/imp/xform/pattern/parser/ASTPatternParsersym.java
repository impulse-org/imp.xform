
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

public interface ASTPatternParsersym {
    public final static int
      TK_IDENT = 1,
      TK_NUMBER = 6,
      TK_STRING = 11,
      TK_DEFINE = 12,
      TK_SEMICOLON = 26,
      TK_COLON = 7,
      TK_COMMA = 3,
      TK_DOT = 8,
      TK_ELLIPSIS = 13,
      TK_PLUS = 27,
      TK_MINUS = 14,
      TK_TIMES = 15,
      TK_EQUALS = 16,
      TK_ARROW = 17,
      TK_NOTEQUALS = 18,
      TK_LEFTPAREN = 9,
      TK_RIGHTPAREN = 10,
      TK_LEFTBRACE = 4,
      TK_RIGHTBRACE = 5,
      TK_LEFTBRACKET = 2,
      TK_RIGHTBRACKET = 19,
      TK_DIRECT = 20,
      TK_DIRECTEND = 21,
      TK_LESSTHAN = 22,
      TK_GREATERTHAN = 23,
      TK_SHARP = 24,
      TK_UNDERSCORE = 28,
      TK_typeOf = 29,
      TK_targetTypeOf = 30,
      TK_EOF_TOKEN = 25,
      TK_SINGLE_LINE_COMMENT = 31,
      TK_ERROR_TOKEN = 32;

    public final static String orderedTerminalSymbols[] = {
                 "",
                 "IDENT",
                 "LEFTBRACKET",
                 "COMMA",
                 "LEFTBRACE",
                 "RIGHTBRACE",
                 "NUMBER",
                 "COLON",
                 "DOT",
                 "LEFTPAREN",
                 "RIGHTPAREN",
                 "STRING",
                 "DEFINE",
                 "ELLIPSIS",
                 "MINUS",
                 "TIMES",
                 "EQUALS",
                 "ARROW",
                 "NOTEQUALS",
                 "RIGHTBRACKET",
                 "DIRECT",
                 "DIRECTEND",
                 "LESSTHAN",
                 "GREATERTHAN",
                 "SHARP",
                 "EOF_TOKEN",
                 "SEMICOLON",
                 "PLUS",
                 "UNDERSCORE",
                 "typeOf",
                 "targetTypeOf",
                 "SINGLE_LINE_COMMENT",
                 "ERROR_TOKEN"
             };

    public final static int numTokenKinds = orderedTerminalSymbols.length;
    public final static boolean isValidForParser = true;
}
