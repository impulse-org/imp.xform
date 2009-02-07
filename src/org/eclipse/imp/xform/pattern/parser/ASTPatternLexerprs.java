
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

public class ASTPatternLexerprs implements lpg.runtime.ParseTable, ASTPatternLexersym {
    public final static int ERROR_SYMBOL = 0;
    public final int getErrorSymbol() { return ERROR_SYMBOL; }

    public final static int SCOPE_UBOUND = 0;
    public final int getScopeUbound() { return SCOPE_UBOUND; }

    public final static int SCOPE_SIZE = 0;
    public final int getScopeSize() { return SCOPE_SIZE; }

    public final static int MAX_NAME_LENGTH = 0;
    public final int getMaxNameLength() { return MAX_NAME_LENGTH; }

    public final static int NUM_STATES = 14;
    public final int getNumStates() { return NUM_STATES; }

    public final static int NT_OFFSET = 103;
    public final int getNtOffset() { return NT_OFFSET; }

    public final static int LA_STATE_OFFSET = 586;
    public final int getLaStateOffset() { return LA_STATE_OFFSET; }

    public final static int MAX_LA = 1;
    public final int getMaxLa() { return MAX_LA; }

    public final static int NUM_RULES = 203;
    public final int getNumRules() { return NUM_RULES; }

    public final static int NUM_NONTERMINALS = 41;
    public final int getNumNonterminals() { return NUM_NONTERMINALS; }

    public final static int NUM_SYMBOLS = 144;
    public final int getNumSymbols() { return NUM_SYMBOLS; }

    public final static int SEGMENT_SIZE = 8192;
    public final int getSegmentSize() { return SEGMENT_SIZE; }

    public final static int START_STATE = 204;
    public final int getStartState() { return START_STATE; }

    public final static int IDENTIFIER_SYMBOL = 103;
    public final int getIdentifier_SYMBOL() { return IDENTIFIER_SYMBOL; }

    public final static int EOFT_SYMBOL = 100;
    public final int getEoftSymbol() { return EOFT_SYMBOL; }

    public final static int EOLT_SYMBOL = 104;
    public final int getEoltSymbol() { return EOLT_SYMBOL; }

    public final static int ACCEPT_ACTION = 382;
    public final int getAcceptAction() { return ACCEPT_ACTION; }

    public final static int ERROR_ACTION = 383;
    public final int getErrorAction() { return ERROR_ACTION; }

    public final static boolean BACKTRACK = false;
    public final boolean getBacktrack() { return BACKTRACK; }

    public final int getStartSymbol() { return lhs(0); }
    public final boolean isValidForParser() { return ASTPatternLexersym.isValidForParser; }


    public interface IsNullable {
        public final static byte isNullable[] = {0,
            0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,
            0,0,0,0
        };
    };
    public final static byte isNullable[] = IsNullable.isNullable;
    public final boolean isNullable(int index) { return isNullable[index] != 0; }

    public interface ProsthesesIndex {
        public final static byte prosthesesIndex[] = {0,
            9,8,14,15,16,17,18,19,20,21,
            22,23,24,25,26,27,28,29,30,31,
            32,33,34,35,36,37,38,39,12,2,
            3,4,5,6,7,10,11,13,40,41,
            1
        };
    };
    public final static byte prosthesesIndex[] = ProsthesesIndex.prosthesesIndex;
    public final int prosthesesIndex(int index) { return prosthesesIndex[index]; }

    public interface IsKeyword {
        public final static byte isKeyword[] = {0,
            0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,
            0,0,0
        };
    };
    public final static byte isKeyword[] = IsKeyword.isKeyword;
    public final boolean isKeyword(int index) { return isKeyword[index] != 0; }

    public interface BaseCheck {
        public final static byte baseCheck[] = {0,
            1,1,1,1,1,1,1,1,1,1,
            1,1,1,1,1,1,1,2,2,2,
            2,3,1,1,2,2,2,1,2,2,
            1,2,1,2,2,2,1,1,1,1,
            1,1,1,1,1,1,1,1,1,1,
            1,1,1,1,1,1,1,1,1,1,
            1,1,1,1,1,1,1,1,1,1,
            1,1,1,1,1,1,1,1,1,1,
            1,1,1,1,1,1,1,1,1,1,
            1,1,1,1,1,1,1,1,1,1,
            1,1,1,1,1,1,1,1,1,1,
            1,1,1,1,1,1,1,1,1,1,
            1,1,1,1,1,1,1,1,1,1,
            1,1,1,1,1,1,1,1,1,1,
            1,1,1,1,1,1,1,1,1,1,
            1,1,1,1,1,1,1,1,1,1,
            1,1,1,1,1,1,1,1,1,1,
            1,1,1,1,1,1,1,1,1,1,
            1,1,1,1,1,1,1,1,1,1,
            1,1,1,1,1,1,1,1,1,1,
            1,1,1
        };
    };
    public final static byte baseCheck[] = BaseCheck.baseCheck;
    public final int baseCheck(int index) { return baseCheck[index]; }
    public final static byte rhs[] = baseCheck;
    public final int rhs(int index) { return rhs[index]; };

    public interface BaseAction {
        public final static char baseAction[] = {
            30,30,30,30,30,30,30,30,30,30,
            30,30,30,30,30,30,30,30,30,30,
            30,30,30,30,31,31,31,31,32,32,
            35,36,36,33,33,34,34,1,1,1,
            1,1,1,1,1,1,1,3,3,4,
            4,5,5,6,6,7,7,8,8,9,
            9,10,10,11,11,12,12,13,13,14,
            14,15,15,16,16,17,17,18,18,19,
            19,20,20,21,21,22,22,23,23,24,
            24,25,25,26,26,27,27,28,28,2,
            2,2,2,2,2,2,2,2,2,2,
            2,2,2,2,2,2,2,2,2,2,
            2,2,2,2,2,29,29,29,29,29,
            39,39,39,39,39,39,39,39,39,39,
            39,39,39,39,39,39,39,39,39,39,
            39,39,39,39,39,39,39,39,39,39,
            39,39,38,38,38,38,38,38,40,40,
            40,40,40,40,40,40,40,40,40,40,
            40,40,40,40,40,40,40,40,40,40,
            40,40,40,40,40,40,40,40,40,37,
            37,37,37,37,201,28,24,99,100,101,
            102,103,104,105,106,107,108,109,110,111,
            112,113,114,115,116,117,118,119,120,121,
            122,123,124,33,272,322,270,351,282,23,
            241,101,200,199,99,100,101,102,103,104,
            105,106,107,108,109,110,111,112,113,114,
            115,116,117,118,119,120,121,122,123,124,
            361,29,88,373,271,374,356,301,32,375,
            376,201,1,163,162,99,100,101,102,103,
            104,105,106,107,108,109,110,111,112,113,
            114,115,116,117,118,119,120,121,122,123,
            124,383,383,383,383,383,383,383,383,383,
            36,164,290,26,25,99,100,101,102,103,
            104,105,106,107,108,109,110,111,112,113,
            114,115,116,117,118,119,120,121,122,123,
            124,359,383,383,383,383,383,383,383,383,
            383,383,383,383,383,383,383,383,383,383,
            383,383,383,383,383,383,383,383,383,383,
            34,383,383
        };
    };
    public final static char baseAction[] = BaseAction.baseAction;
    public final int baseAction(int index) { return baseAction[index]; }
    public final static char lhs[] = baseAction;
    public final int lhs(int index) { return lhs[index]; };

    public interface TermCheck {
        public final static byte termCheck[] = {0,
            0,1,2,3,4,5,6,7,8,9,
            10,11,12,13,14,15,16,17,18,19,
            20,21,22,23,24,25,26,27,28,29,
            30,31,32,33,34,35,36,37,38,39,
            40,41,42,43,44,45,46,47,48,49,
            50,51,52,53,54,55,56,57,58,59,
            60,61,62,63,64,65,66,67,68,69,
            70,71,72,73,74,75,76,77,78,79,
            80,81,82,83,84,85,86,0,0,89,
            90,91,92,93,94,95,96,97,98,99,
            0,1,2,3,4,5,6,7,8,9,
            10,11,12,13,14,15,16,17,18,19,
            20,21,22,23,24,25,26,27,28,29,
            30,31,32,33,34,35,36,37,38,39,
            40,41,42,43,44,45,46,47,48,49,
            50,51,52,53,54,55,56,57,58,59,
            60,61,62,63,64,65,66,67,68,0,
            70,71,72,73,74,75,76,77,78,79,
            80,81,82,83,84,85,86,100,0,89,
            90,91,92,93,94,95,96,97,98,99,
            0,1,2,3,4,5,6,7,8,9,
            10,11,12,13,14,15,16,17,18,19,
            20,21,22,23,24,25,26,27,28,29,
            30,31,32,33,34,35,36,37,38,39,
            40,41,42,43,44,45,46,47,48,49,
            50,51,52,53,54,55,56,57,58,59,
            60,61,62,63,64,65,66,67,68,69,
            0,71,72,73,74,75,76,77,78,79,
            80,81,12,83,84,85,86,87,88,0,
            1,2,3,4,5,6,7,8,9,10,
            0,0,0,14,15,16,17,18,19,20,
            21,22,23,24,25,26,27,28,29,30,
            31,32,33,34,35,36,37,38,39,40,
            41,42,43,44,45,46,47,48,49,50,
            51,52,53,54,55,56,57,58,59,60,
            61,62,63,64,65,0,0,0,0,70,
            0,1,2,3,4,5,6,7,8,9,
            10,13,0,0,0,0,0,0,0,0,
            0,0,82,11,0,12,11,0,0,0,
            0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,
            0,0,0,68,66,0,0,69,0,0,
            0,0,0,0,0,0,0,0,0,0,
            0,67,0,0,0,87,88,0,0,0,
            0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0
        };
    };
    public final static byte termCheck[] = TermCheck.termCheck;
    public final int termCheck(int index) { return termCheck[index]; }

    public interface TermAction {
        public final static char termAction[] = {0,
            4,420,421,422,423,424,425,426,427,428,
            429,522,514,549,430,432,434,436,438,440,
            442,444,446,448,450,452,454,456,458,460,
            462,464,466,468,470,472,474,476,478,480,
            431,433,435,437,439,441,443,445,447,449,
            451,453,455,457,459,461,463,465,467,469,
            471,473,475,477,479,481,548,543,539,550,
            542,528,530,518,526,527,529,531,532,533,
            534,536,538,513,541,515,516,383,383,517,
            523,519,520,521,540,544,524,525,535,537,
            383,420,421,422,423,424,425,426,427,428,
            429,560,552,586,430,432,434,436,438,440,
            442,444,446,448,450,452,454,456,458,460,
            462,464,466,468,470,472,474,476,478,480,
            431,433,435,437,439,441,443,445,447,449,
            451,453,455,457,459,461,463,465,467,469,
            471,473,475,477,479,481,585,580,576,383,
            579,413,567,556,564,565,566,568,569,570,
            571,573,575,551,578,553,554,382,383,555,
            561,557,558,559,577,581,562,563,572,574,
            383,420,421,422,423,424,425,426,427,428,
            429,273,392,511,430,432,434,436,438,440,
            442,444,446,448,450,452,454,456,458,460,
            462,464,466,468,470,472,474,476,478,480,
            431,433,435,437,439,441,443,445,447,449,
            451,453,455,457,459,461,463,465,467,469,
            471,473,475,477,479,481,508,279,277,512,
            383,414,275,276,389,390,274,394,395,396,
            397,388,404,391,393,398,399,509,510,1,
            420,421,422,423,424,425,426,427,428,429,
            17,383,383,430,432,434,436,438,440,442,
            444,446,448,450,452,454,456,458,460,462,
            464,466,468,470,472,474,476,478,480,431,
            433,435,437,439,441,443,445,447,449,451,
            453,455,457,459,461,463,465,467,469,471,
            473,475,477,479,481,383,383,383,3,410,
            2,420,421,422,423,424,425,426,427,428,
            429,511,383,383,383,383,383,383,383,383,
            383,383,401,280,383,403,405,383,383,383,
            383,383,383,383,383,383,383,383,383,383,
            383,383,383,383,383,383,383,383,383,383,
            383,383,383,383,383,383,383,383,383,383,
            383,383,383,402,508,383,383,512,383,383,
            383,383,383,383,383,383,383,383,383,383,
            383,418,383,383,383,509,510
        };
    };
    public final static char termAction[] = TermAction.termAction;
    public final int termAction(int index) { return termAction[index]; }
    public final int asb(int index) { return 0; }
    public final int asr(int index) { return 0; }
    public final int nasb(int index) { return 0; }
    public final int nasr(int index) { return 0; }
    public final int terminalIndex(int index) { return 0; }
    public final int nonterminalIndex(int index) { return 0; }
    public final int scopePrefix(int index) { return 0;}
    public final int scopeSuffix(int index) { return 0;}
    public final int scopeLhs(int index) { return 0;}
    public final int scopeLa(int index) { return 0;}
    public final int scopeStateSet(int index) { return 0;}
    public final int scopeRhs(int index) { return 0;}
    public final int scopeState(int index) { return 0;}
    public final int inSymb(int index) { return 0;}
    public final String name(int index) { return null; }
    public final int originalState(int state) { return 0; }
    public final int asi(int state) { return 0; }
    public final int nasi(int state) { return 0; }
    public final int inSymbol(int state) { return 0; }

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
