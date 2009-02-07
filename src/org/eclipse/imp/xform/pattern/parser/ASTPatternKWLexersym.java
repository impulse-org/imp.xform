
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

public interface ASTPatternKWLexersym {
    public final static int
      Char_DollarSign = 15,
      Char__ = 16,
      Char_a = 7,
      Char_b = 17,
      Char_c = 18,
      Char_d = 8,
      Char_e = 1,
      Char_f = 2,
      Char_g = 9,
      Char_h = 19,
      Char_i = 10,
      Char_j = 20,
      Char_k = 21,
      Char_l = 22,
      Char_m = 23,
      Char_n = 11,
      Char_o = 24,
      Char_p = 3,
      Char_q = 25,
      Char_r = 12,
      Char_s = 26,
      Char_t = 4,
      Char_u = 27,
      Char_v = 28,
      Char_w = 29,
      Char_x = 30,
      Char_y = 5,
      Char_z = 31,
      Char_O = 6,
      Char_T = 13,
      Char_EOF = 14;

    public final static String orderedTerminalSymbols[] = {
                 "",
                 "e",
                 "f",
                 "p",
                 "t",
                 "y",
                 "O",
                 "a",
                 "d",
                 "g",
                 "i",
                 "n",
                 "r",
                 "T",
                 "EOF",
                 "DollarSign",
                 "_",
                 "b",
                 "c",
                 "h",
                 "j",
                 "k",
                 "l",
                 "m",
                 "o",
                 "q",
                 "s",
                 "u",
                 "v",
                 "w",
                 "x",
                 "z"
             };

    public final static int numTokenKinds = orderedTerminalSymbols.length;
    public final static boolean isValidForParser = true;
}
