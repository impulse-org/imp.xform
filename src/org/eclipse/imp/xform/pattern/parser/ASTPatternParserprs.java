
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

public class ASTPatternParserprs implements lpg.runtime.ParseTable, ASTPatternParsersym {
    public final static int ERROR_SYMBOL = 32;
    public final int getErrorSymbol() { return ERROR_SYMBOL; }

    public final static int SCOPE_UBOUND = 2;
    public final int getScopeUbound() { return SCOPE_UBOUND; }

    public final static int SCOPE_SIZE = 3;
    public final int getScopeSize() { return SCOPE_SIZE; }

    public final static int MAX_NAME_LENGTH = 19;
    public final int getMaxNameLength() { return MAX_NAME_LENGTH; }

    public final static int NUM_STATES = 42;
    public final int getNumStates() { return NUM_STATES; }

    public final static int NT_OFFSET = 32;
    public final int getNtOffset() { return NT_OFFSET; }

    public final static int LA_STATE_OFFSET = 243;
    public final int getLaStateOffset() { return LA_STATE_OFFSET; }

    public final static int MAX_LA = 1;
    public final int getMaxLa() { return MAX_LA; }

    public final static int NUM_RULES = 62;
    public final int getNumRules() { return NUM_RULES; }

    public final static int NUM_NONTERMINALS = 40;
    public final int getNumNonterminals() { return NUM_NONTERMINALS; }

    public final static int NUM_SYMBOLS = 72;
    public final int getNumSymbols() { return NUM_SYMBOLS; }

    public final static int SEGMENT_SIZE = 8192;
    public final int getSegmentSize() { return SEGMENT_SIZE; }

    public final static int START_STATE = 69;
    public final int getStartState() { return START_STATE; }

    public final static int IDENTIFIER_SYMBOL = 1;
    public final int getIdentifier_SYMBOL() { return IDENTIFIER_SYMBOL; }

    public final static int EOFT_SYMBOL = 25;
    public final int getEoftSymbol() { return EOFT_SYMBOL; }

    public final static int EOLT_SYMBOL = 25;
    public final int getEoltSymbol() { return EOLT_SYMBOL; }

    public final static int ACCEPT_ACTION = 180;
    public final int getAcceptAction() { return ACCEPT_ACTION; }

    public final static int ERROR_ACTION = 181;
    public final int getErrorAction() { return ERROR_ACTION; }

    public final static boolean BACKTRACK = false;
    public final boolean getBacktrack() { return BACKTRACK; }

    public final int getStartSymbol() { return lhs(0); }
    public final boolean isValidForParser() { return ASTPatternParsersym.isValidForParser; }


    public interface IsNullable {
        public final static byte isNullable[] = {0,
            0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,1,0,0,0,
            0,0,0,0,0,0,0,0,0,0,
            0,0,1,1,1,1,1,0,0,0,
            0,0,0,0,0,0,0,0,1,1,
            0,0
        };
    };
    public final static byte isNullable[] = IsNullable.isNullable;
    public final boolean isNullable(int index) { return isNullable[index] != 0; }

    public interface ProsthesesIndex {
        public final static byte prosthesesIndex[] = {0,
            8,17,4,24,31,7,19,21,22,23,
            27,28,29,2,3,5,6,9,10,11,
            12,13,14,15,16,18,20,25,26,30,
            32,33,34,35,36,37,38,39,40,1
        };
    };
    public final static byte prosthesesIndex[] = ProsthesesIndex.prosthesesIndex;
    public final int prosthesesIndex(int index) { return prosthesesIndex[index]; }

    public interface IsKeyword {
        public final static byte isKeyword[] = {0,
            0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,
            0,0
        };
    };
    public final static byte isKeyword[] = IsKeyword.isKeyword;
    public final boolean isKeyword(int index) { return isKeyword[index] != 0; }

    public interface BaseCheck {
        public final static byte baseCheck[] = {0,
            1,1,1,8,1,3,1,3,1,2,
            3,1,2,8,1,4,1,3,1,0,
            1,1,1,0,2,0,0,3,1,3,
            1,1,3,5,1,1,1,1,1,1,
            1,2,0,3,1,1,1,1,1,1,
            1,1,1,0,2,2,1,1,1,1,
            0,3,-36,0,0,-25,0,0,-1,0,
            0,0,-33,0,0,-28,-2,-9,0,0,
            0,-3,0,0,0,-12,0,0,0,-4,
            0,0,0,-26,0,0,0,0,0,-30,
            -35,0,0,0,0,0,-6,0,0,0,
            0,0,0,0,0,-14,0,0,0,-32,
            0,-24,0,0,0,-41,0,0,-10,-11,
            0,0,0,0,0,0,0,0,0,-13,
            -16,-18,-19,-20,-22,0,-31,-5,0,0,
            0,-7,-8,-15,0,-17,0,-21,-23,-27,
            -29,-34,0,-37,-38,0,-39,-40,0,-42,
            0,0,0,0,0,0,0,0,0
        };
    };
    public final static byte baseCheck[] = BaseCheck.baseCheck;
    public final int baseCheck(int index) { return baseCheck[index]; }
    public final static byte rhs[] = baseCheck;
    public final int rhs(int index) { return rhs[index]; };

    public interface BaseAction {
        public final static char baseAction[] = {
            14,14,14,14,16,17,17,6,15,3,
            3,18,19,19,1,1,2,26,26,7,
            22,22,20,21,21,23,23,24,24,27,
            27,8,8,9,10,11,11,12,13,29,
            29,29,4,5,5,31,30,30,32,33,
            28,28,34,35,25,25,36,37,37,38,
            38,38,39,1,41,15,25,39,147,4,
            90,15,152,17,56,15,11,22,17,90,
            15,12,41,77,1,3,17,90,15,8,
            44,76,33,40,62,46,47,116,100,147,
            21,62,29,31,32,100,147,49,10,30,
            31,32,55,73,162,58,8,90,15,13,
            14,161,17,90,15,160,14,130,63,51,
            53,167,35,36,50,51,17,170,35,36,
            56,58,51,59,56,63,5,71,36,18,
            6,141,43,46,23,154,24,156,61,68,
            70,35,15,143,73,77,145,76,64,66,
            74,181,181,181,181,181,181,181,164,181,
            181
        };
    };
    public final static char baseAction[] = BaseAction.baseAction;
    public final int baseAction(int index) { return baseAction[index]; }
    public final static char lhs[] = baseAction;
    public final int lhs(int index) { return lhs[index]; };

    public interface TermCheck {
        public final static byte termCheck[] = {0,
            0,1,2,0,1,2,6,0,1,2,
            0,11,5,0,0,12,0,1,2,6,
            0,0,0,0,0,3,3,13,15,19,
            20,21,10,10,0,0,16,3,18,5,
            0,1,0,0,9,0,25,4,0,1,
            0,1,0,1,9,0,1,0,0,17,
            0,0,0,0,4,7,4,0,1,0,
            0,1,0,0,5,0,0,14,0,0,
            8,24,7,22,8,0,0,0,0,0,
            0,0,0,0,0,0,23,0,0,0,
            0,0,0,0,0,0,0,0,0
        };
    };
    public final static byte termCheck[] = TermCheck.termCheck;
    public final int termCheck(int index) { return termCheck[index]; }

    public interface TermAction {
        public final static char termAction[] = {0,
            181,148,107,181,148,107,229,181,148,107,
            61,230,192,181,57,82,181,148,107,218,
            181,181,181,181,54,142,144,168,219,195,
            240,241,197,158,181,181,233,101,234,209,
            181,153,2,9,129,181,180,78,181,203,
            181,200,24,204,140,181,188,20,26,86,
            181,43,27,181,122,159,94,181,206,181,
            181,165,181,181,185,181,42,243,181,181,
            225,202,126,120,45,181,181,181,181,181,
            181,181,181,181,181,181,215
        };
    };
    public final static char termAction[] = TermAction.termAction;
    public final int termAction(int index) { return termAction[index]; }

    public interface Asb {
        public final static byte asb[] = {0,
            6,36,31,10,33,31,35,33,30,31,
            23,30,31,13,38,23,38,31,24,31,
            41,25,31,30,26,17,15,26,43,51,
            31,20,30,29,17,1,46,48,54,56,
            20,58
        };
    };
    public final static byte asb[] = Asb.asb;
    public final int asb(int index) { return asb[index]; }

    public interface Asr {
        public final static byte asr[] = {0,
            11,6,2,1,0,2,1,12,0,17,
            25,4,2,1,5,0,1,22,0,6,
            15,0,24,7,4,19,20,21,13,2,
            1,0,9,0,17,25,0,10,3,0,
            4,0,5,3,0,8,0,8,3,5,
            16,18,0,7,0,14,0,23,0
        };
    };
    public final static byte asr[] = Asr.asr;
    public final int asr(int index) { return asr[index]; }

    public interface Nasb {
        public final static byte nasb[] = {0,
            6,22,22,11,22,23,22,22,6,27,
            29,6,31,6,22,33,22,35,37,39,
            22,41,22,6,4,13,22,8,22,17,
            43,25,6,22,19,1,22,22,22,22,
            25,22
        };
    };
    public final static byte nasb[] = Nasb.nasb;
    public final int nasb(int index) { return nasb[index]; }

    public interface Nasr {
        public final static byte nasr[] = {0,
            5,29,0,25,0,1,0,38,36,0,
            18,0,5,4,27,0,28,0,4,5,
            8,0,20,0,11,0,26,0,21,0,
            17,0,22,0,7,0,23,0,6,0,
            24,0,31,0
        };
    };
    public final static byte nasr[] = Nasr.nasr;
    public final int nasr(int index) { return nasr[index]; }

    public interface TerminalIndex {
        public final static byte terminalIndex[] = {0,
            25,16,3,14,15,26,2,4,12,13,
            27,28,5,7,8,9,10,11,17,18,
            19,20,21,22,31,1,6,23,29,30,
            32,33
        };
    };
    public final static byte terminalIndex[] = TerminalIndex.terminalIndex;
    public final int terminalIndex(int index) { return terminalIndex[index]; }

    public interface NonterminalIndex {
        public final static byte nonterminalIndex[] = {0,
            38,0,35,46,0,37,43,45,0,0,
            49,0,0,34,0,0,36,39,40,41,
            0,0,0,0,0,42,44,47,48,0,
            50,0,0,0,0,51,0,0,0,0
        };
    };
    public final static byte nonterminalIndex[] = NonterminalIndex.nonterminalIndex;
    public final int nonterminalIndex(int index) { return nonterminalIndex[index]; }

    public interface ScopePrefix {
        public final static byte scopePrefix[] = {
            6,1,16
        };
    };
    public final static byte scopePrefix[] = ScopePrefix.scopePrefix;
    public final int scopePrefix(int index) { return scopePrefix[index]; }

    public interface ScopeSuffix {
        public final static byte scopeSuffix[] = {
            14,4,4
        };
    };
    public final static byte scopeSuffix[] = ScopeSuffix.scopeSuffix;
    public final int scopeSuffix(int index) { return scopeSuffix[index]; }

    public interface ScopeLhs {
        public final static byte scopeLhs[] = {
            1,24,18
        };
    };
    public final static byte scopeLhs[] = ScopeLhs.scopeLhs;
    public final int scopeLhs(int index) { return scopeLhs[index]; }

    public interface ScopeLa {
        public final static byte scopeLa[] = {
            19,5,5
        };
    };
    public final static byte scopeLa[] = ScopeLa.scopeLa;
    public final int scopeLa(int index) { return scopeLa[index]; }

    public interface ScopeStateSet {
        public final static byte scopeStateSet[] = {
            5,1,3
        };
    };
    public final static byte scopeStateSet[] = ScopeStateSet.scopeStateSet;
    public final int scopeStateSet(int index) { return scopeStateSet[index]; }

    public interface ScopeRhs {
        public final static byte scopeRhs[] = {0,
            59,4,0,5,0,57,56,55,54,53,
            52,2,0,19,0,51,4,0
        };
    };
    public final static byte scopeRhs[] = ScopeRhs.scopeRhs;
    public final int scopeRhs(int index) { return scopeRhs[index]; }

    public interface ScopeState {
        public final static char scopeState[] = {0,
            145,0,90,0,63,73,122,116,86,78,
            69,0
        };
    };
    public final static char scopeState[] = ScopeState.scopeState;
    public final int scopeState(int index) { return scopeState[index]; }

    public interface InSymb {
        public final static byte inSymb[] = {0,
            0,46,12,33,1,2,35,1,4,9,
            52,17,9,51,58,53,49,3,54,3,
            10,55,7,4,56,4,35,57,59,36,
            37,22,69,70,3,60,63,1,43,13,
            7,43
        };
    };
    public final static byte inSymb[] = InSymb.inSymb;
    public final int inSymb(int index) { return inSymb[index]; }

    public interface Name {
        public final static String name[] = {
            "",
            ";",
            ":",
            ",",
            ".",
            "...",
            "+",
            "-",
            "*",
            "==",
            "=>",
            "!=",
            "(",
            ")",
            "{",
            "}",
            "[",
            "]",
            "|-",
            "\\-",
            "<",
            ">",
            "#",
            "_",
            "$empty",
            "IDENT",
            "NUMBER",
            "STRING",
            "DEFINE",
            "typeOf",
            "targetTypeOf",
            "EOF_TOKEN",
            "SINGLE_LINE_COMMENT",
            "ERROR_TOKEN",
            "TopLevel",
            "Pattern",
            "FormalArgList",
            "FormalArg",
            "Node",
            "ScopeBlock",
            "PatternList",
            "NodeType",
            "ActualArgList",
            "ActualArg",
            "ConstraintList",
            "Constraint",
            "NodeAttribute",
            "Operator",
            "Value",
            "Bound",
            "ident",
            "Child"
        };
    };
    public final static String name[] = Name.name;
    public final String name(int index) { return name[index]; }

    public final int originalState(int state) {
        return -baseCheck[state];
    }
    public final int asi(int state) {
        return asb[originalState(state)];
    }
    public final int nasi(int state) {
        return nasb[originalState(state)];
    }
    public final int inSymbol(int state) {
        return inSymb[originalState(state)];
    }

    /**
     * assert(! goto_default);
     */
    public final int ntAction(int state, int sym) {
        return baseAction[state + sym];
    }

    /**
     * assert(! shift_default);
     */
    public final int tAction(int state, int sym) {
        int i = baseAction[state],
            k = i + sym;
        return termAction[termCheck[k] == sym ? k : i];
    }
    public final int lookAhead(int la_state, int sym) {
        int k = la_state + sym;
        return termAction[termCheck[k] == sym ? k : la_state];
    }
}
