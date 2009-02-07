
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
import java.util.*;

public class ASTPatternLexer implements RuleAction
{
    private ASTPatternLexerLpgLexStream lexStream;
    
    private static ParseTable prs = new ASTPatternLexerprs();
    public ParseTable getParseTable() { return prs; }

    private LexParser lexParser = new LexParser();
    public LexParser getParser() { return lexParser; }

    public int getToken(int i) { return lexParser.getToken(i); }
    public int getRhsFirstTokenIndex(int i) { return lexParser.getFirstToken(i); }
    public int getRhsLastTokenIndex(int i) { return lexParser.getLastToken(i); }

    public int getLeftSpan() { return lexParser.getToken(1); }
    public int getRightSpan() { return lexParser.getLastToken(); }

    public void resetKeywordLexer()
    {
        if (kwLexer == null)
              this.kwLexer = new ASTPatternKWLexer(lexStream.getInputChars(), ASTPatternParsersym.TK_IDENT);
        else this.kwLexer.setInputChars(lexStream.getInputChars());
    }

    public void reset(String filename, int tab) throws java.io.IOException
    {
        lexStream = new ASTPatternLexerLpgLexStream(filename, tab);
        lexParser.reset((ILexStream) lexStream, prs, (RuleAction) this);
        resetKeywordLexer();
    }

    public void reset(char[] input_chars, String filename)
    {
        reset(input_chars, filename, 1);
    }
    
    public void reset(char[] input_chars, String filename, int tab)
    {
        lexStream = new ASTPatternLexerLpgLexStream(input_chars, filename, tab);
        lexParser.reset((ILexStream) lexStream, prs, (RuleAction) this);
        resetKeywordLexer();
    }
    
    public ASTPatternLexer(String filename, int tab) throws java.io.IOException 
    {
        reset(filename, tab);
    }

    public ASTPatternLexer(char[] input_chars, String filename, int tab)
    {
        reset(input_chars, filename, tab);
    }

    public ASTPatternLexer(char[] input_chars, String filename)
    {
        reset(input_chars, filename, 1);
    }

    public ASTPatternLexer() {}

    public ILexStream getILexStream() { return lexStream; }

    /**
     * @deprecated replaced by {@link #getILexStream()}
     */
    public ILexStream getLexStream() { return lexStream; }

    private void initializeLexer(IPrsStream prsStream, int start_offset, int end_offset)
    {
        if (lexStream.getInputChars() == null)
            throw new NullPointerException("LexStream was not initialized");
        lexStream.setPrsStream(prsStream);
        prsStream.makeToken(start_offset, end_offset, 0); // Token list must start with a bad token
    }

    private void addEOF(IPrsStream prsStream, int end_offset)
    {
        prsStream.makeToken(end_offset, end_offset, ASTPatternParsersym.TK_EOF_TOKEN); // and end with the end of file token
        prsStream.setStreamLength(prsStream.getSize());
    }

    public void lexer(IPrsStream prsStream)
    {
        lexer(null, prsStream);
    }
    
    public void lexer(Monitor monitor, IPrsStream prsStream)
    {
        initializeLexer(prsStream, 0, -1);
        lexParser.parseCharacters(monitor);  // Lex the input characters
        addEOF(prsStream, lexStream.getStreamIndex());
    }

    public void lexer(IPrsStream prsStream, int start_offset, int end_offset)
    {
        lexer(null, prsStream, start_offset, end_offset);
    }
    
    public void lexer(Monitor monitor, IPrsStream prsStream, int start_offset, int end_offset)
    {
        if (start_offset <= 1)
             initializeLexer(prsStream, 0, -1);
        else initializeLexer(prsStream, start_offset - 1, start_offset - 1);

        lexParser.parseCharacters(monitor, start_offset, end_offset);

        addEOF(prsStream, (end_offset >= lexStream.getStreamIndex() ? lexStream.getStreamIndex() : end_offset + 1));
    }

    /**
     * If a parse stream was not passed to this Lexical analyser then we
     * simply report a lexical error. Otherwise, we produce a bad token.
     */
    public void reportLexicalError(int startLoc, int endLoc) {
        IPrsStream prs_stream = lexStream.getPrsStream();
        if (prs_stream == null)
            lexStream.reportLexicalError(startLoc, endLoc);
        else {
            //
            // Remove any token that may have been processed that fall in the
            // range of the lexical error... then add one error token that spans
            // the error range.
            //
            for (int i = prs_stream.getSize() - 1; i > 0; i--) {
                if (prs_stream.getStartOffset(i) >= startLoc)
                     prs_stream.removeLastToken();
                else break;
            }
            prs_stream.makeToken(startLoc, endLoc, 0); // add an error token to the prsStream
        }        
    }

    //
    // The Lexer contains an array of characters as the input stream to be parsed.
    // There are methods to retrieve and classify characters.
    // The lexparser "token" is implemented simply as the index of the next character in the array.
    // The Lexer extends the abstract class LpgLexStream with an implementation of the abstract
    // method getKind.  The template defines the Lexer class and the lexer() method.
    // A driver creates the action class, "Lexer", passing an Option object to the constructor.
    //
    ASTPatternKWLexer kwLexer;
    boolean printTokens;
    private final static int ECLIPSE_TAB_VALUE = 4;

    public int [] getKeywordKinds() { return kwLexer.getKeywordKinds(); }

    public ASTPatternLexer(String filename) throws java.io.IOException
    {
        this(filename, ECLIPSE_TAB_VALUE);
        this.kwLexer = new ASTPatternKWLexer(lexStream.getInputChars(), ASTPatternParsersym.TK_IDENT);
    }

    /**
     * @deprecated function replaced by {@link #reset(char [] content, String filename)}
     */
    public void initialize(char [] content, String filename)
    {
        reset(content, filename);
    }
    
    final void makeToken(int left_token, int right_token, int kind)
    {
        lexStream.makeToken(left_token, right_token, kind);
    }
    
    final void makeToken(int kind)
    {
        int startOffset = getLeftSpan(),
            endOffset = getRightSpan();
        lexStream.makeToken(startOffset, endOffset, kind);
        if (printTokens) printValue(startOffset, endOffset);
    }

    final void makeComment(int kind)
    {
        int startOffset = getLeftSpan(),
            endOffset = getRightSpan();
        lexStream.getIPrsStream().makeAdjunct(startOffset, endOffset, kind);
    }

    final void skipToken()
    {
        if (printTokens) printValue(getLeftSpan(), getRightSpan());
    }
    
    final void checkForKeyWord()
    {
        int startOffset = getLeftSpan(),
            endOffset = getRightSpan(),
            kwKind = kwLexer.lexer(startOffset, endOffset);
        lexStream.makeToken(startOffset, endOffset, kwKind);
        if (printTokens) printValue(startOffset, endOffset);
    }
    
    //
    // This flavor of checkForKeyWord is necessary when the default kind
    // (which is returned when the keyword filter doesn't match) is something
    // other than _IDENTIFIER.
    //
    final void checkForKeyWord(int defaultKind)
    {
        int startOffset = getLeftSpan(),
            endOffset = getRightSpan(),
            kwKind = kwLexer.lexer(startOffset, endOffset);
        if (kwKind == ASTPatternParsersym.TK_IDENT)
            kwKind = defaultKind;
        lexStream.makeToken(startOffset, endOffset, kwKind);
        if (printTokens) printValue(startOffset, endOffset);
    }
    
    final void printValue(int startOffset, int endOffset)
    {
        String s = new String(lexStream.getInputChars(), startOffset, endOffset - startOffset + 1);
        System.out.print(s);
    }

    //
    //
    //
    static class ASTPatternLexerLpgLexStream extends LpgLexStream
    {
    public final static int tokenKind[] =
    {
        ASTPatternLexersym.Char_CtlCharNotWS,    // 000    0x00
        ASTPatternLexersym.Char_CtlCharNotWS,    // 001    0x01
        ASTPatternLexersym.Char_CtlCharNotWS,    // 002    0x02
        ASTPatternLexersym.Char_CtlCharNotWS,    // 003    0x03
        ASTPatternLexersym.Char_CtlCharNotWS,    // 004    0x04
        ASTPatternLexersym.Char_CtlCharNotWS,    // 005    0x05
        ASTPatternLexersym.Char_CtlCharNotWS,    // 006    0x06
        ASTPatternLexersym.Char_CtlCharNotWS,    // 007    0x07
        ASTPatternLexersym.Char_CtlCharNotWS,    // 008    0x08
        ASTPatternLexersym.Char_HT,              // 009    0x09
        ASTPatternLexersym.Char_LF,              // 010    0x0A
        ASTPatternLexersym.Char_CtlCharNotWS,    // 011    0x0B
        ASTPatternLexersym.Char_FF,              // 012    0x0C
        ASTPatternLexersym.Char_CR,              // 013    0x0D
        ASTPatternLexersym.Char_CtlCharNotWS,    // 014    0x0E
        ASTPatternLexersym.Char_CtlCharNotWS,    // 015    0x0F
        ASTPatternLexersym.Char_CtlCharNotWS,    // 016    0x10
        ASTPatternLexersym.Char_CtlCharNotWS,    // 017    0x11
        ASTPatternLexersym.Char_CtlCharNotWS,    // 018    0x12
        ASTPatternLexersym.Char_CtlCharNotWS,    // 019    0x13
        ASTPatternLexersym.Char_CtlCharNotWS,    // 020    0x14
        ASTPatternLexersym.Char_CtlCharNotWS,    // 021    0x15
        ASTPatternLexersym.Char_CtlCharNotWS,    // 022    0x16
        ASTPatternLexersym.Char_CtlCharNotWS,    // 023    0x17
        ASTPatternLexersym.Char_CtlCharNotWS,    // 024    0x18
        ASTPatternLexersym.Char_CtlCharNotWS,    // 025    0x19
        ASTPatternLexersym.Char_CtlCharNotWS,    // 026    0x1A
        ASTPatternLexersym.Char_CtlCharNotWS,    // 027    0x1B
        ASTPatternLexersym.Char_CtlCharNotWS,    // 028    0x1C
        ASTPatternLexersym.Char_CtlCharNotWS,    // 029    0x1D
        ASTPatternLexersym.Char_CtlCharNotWS,    // 030    0x1E
        ASTPatternLexersym.Char_CtlCharNotWS,    // 031    0x1F
        ASTPatternLexersym.Char_Space,           // 032    0x20
        ASTPatternLexersym.Char_Exclamation,     // 033    0x21
        ASTPatternLexersym.Char_DoubleQuote,     // 034    0x22
        ASTPatternLexersym.Char_Sharp,           // 035    0x23
        ASTPatternLexersym.Char_DollarSign,      // 036    0x24
        ASTPatternLexersym.Char_Percent,         // 037    0x25
        ASTPatternLexersym.Char_Ampersand,       // 038    0x26
        ASTPatternLexersym.Char_SingleQuote,     // 039    0x27
        ASTPatternLexersym.Char_LeftParen,       // 040    0x28
        ASTPatternLexersym.Char_RightParen,      // 041    0x29
        ASTPatternLexersym.Char_Star,            // 042    0x2A
        ASTPatternLexersym.Char_Plus,            // 043    0x2B
        ASTPatternLexersym.Char_Comma,           // 044    0x2C
        ASTPatternLexersym.Char_Minus,           // 045    0x2D
        ASTPatternLexersym.Char_Dot,             // 046    0x2E
        ASTPatternLexersym.Char_Slash,           // 047    0x2F
        ASTPatternLexersym.Char_0,               // 048    0x30
        ASTPatternLexersym.Char_1,               // 049    0x31
        ASTPatternLexersym.Char_2,               // 050    0x32
        ASTPatternLexersym.Char_3,               // 051    0x33
        ASTPatternLexersym.Char_4,               // 052    0x34
        ASTPatternLexersym.Char_5,               // 053    0x35
        ASTPatternLexersym.Char_6,               // 054    0x36
        ASTPatternLexersym.Char_7,               // 055    0x37
        ASTPatternLexersym.Char_8,               // 056    0x38
        ASTPatternLexersym.Char_9,               // 057    0x39
        ASTPatternLexersym.Char_Colon,           // 058    0x3A
        ASTPatternLexersym.Char_SemiColon,       // 059    0x3B
        ASTPatternLexersym.Char_LessThan,        // 060    0x3C
        ASTPatternLexersym.Char_Equal,           // 061    0x3D
        ASTPatternLexersym.Char_GreaterThan,     // 062    0x3E
        ASTPatternLexersym.Char_QuestionMark,    // 063    0x3F
        ASTPatternLexersym.Char_AtSign,          // 064    0x40
        ASTPatternLexersym.Char_A,               // 065    0x41
        ASTPatternLexersym.Char_B,               // 066    0x42
        ASTPatternLexersym.Char_C,               // 067    0x43
        ASTPatternLexersym.Char_D,               // 068    0x44
        ASTPatternLexersym.Char_E,               // 069    0x45
        ASTPatternLexersym.Char_F,               // 070    0x46
        ASTPatternLexersym.Char_G,               // 071    0x47
        ASTPatternLexersym.Char_H,               // 072    0x48
        ASTPatternLexersym.Char_I,               // 073    0x49
        ASTPatternLexersym.Char_J,               // 074    0x4A
        ASTPatternLexersym.Char_K,               // 075    0x4B
        ASTPatternLexersym.Char_L,               // 076    0x4C
        ASTPatternLexersym.Char_M,               // 077    0x4D
        ASTPatternLexersym.Char_N,               // 078    0x4E
        ASTPatternLexersym.Char_O,               // 079    0x4F
        ASTPatternLexersym.Char_P,               // 080    0x50
        ASTPatternLexersym.Char_Q,               // 081    0x51
        ASTPatternLexersym.Char_R,               // 082    0x52
        ASTPatternLexersym.Char_S,               // 083    0x53
        ASTPatternLexersym.Char_T,               // 084    0x54
        ASTPatternLexersym.Char_U,               // 085    0x55
        ASTPatternLexersym.Char_V,               // 086    0x56
        ASTPatternLexersym.Char_W,               // 087    0x57
        ASTPatternLexersym.Char_X,               // 088    0x58
        ASTPatternLexersym.Char_Y,               // 089    0x59
        ASTPatternLexersym.Char_Z,               // 090    0x5A
        ASTPatternLexersym.Char_LeftBracket,     // 091    0x5B
        ASTPatternLexersym.Char_BackSlash,       // 092    0x5C
        ASTPatternLexersym.Char_RightBracket,    // 093    0x5D
        ASTPatternLexersym.Char_Caret,           // 094    0x5E
        ASTPatternLexersym.Char__,               // 095    0x5F
        ASTPatternLexersym.Char_BackQuote,       // 096    0x60
        ASTPatternLexersym.Char_a,               // 097    0x61
        ASTPatternLexersym.Char_b,               // 098    0x62
        ASTPatternLexersym.Char_c,               // 099    0x63
        ASTPatternLexersym.Char_d,               // 100    0x64
        ASTPatternLexersym.Char_e,               // 101    0x65
        ASTPatternLexersym.Char_f,               // 102    0x66
        ASTPatternLexersym.Char_g,               // 103    0x67
        ASTPatternLexersym.Char_h,               // 104    0x68
        ASTPatternLexersym.Char_i,               // 105    0x69
        ASTPatternLexersym.Char_j,               // 106    0x6A
        ASTPatternLexersym.Char_k,               // 107    0x6B
        ASTPatternLexersym.Char_l,               // 108    0x6C
        ASTPatternLexersym.Char_m,               // 109    0x6D
        ASTPatternLexersym.Char_n,               // 110    0x6E
        ASTPatternLexersym.Char_o,               // 111    0x6F
        ASTPatternLexersym.Char_p,               // 112    0x70
        ASTPatternLexersym.Char_q,               // 113    0x71
        ASTPatternLexersym.Char_r,               // 114    0x72
        ASTPatternLexersym.Char_s,               // 115    0x73
        ASTPatternLexersym.Char_t,               // 116    0x74
        ASTPatternLexersym.Char_u,               // 117    0x75
        ASTPatternLexersym.Char_v,               // 118    0x76
        ASTPatternLexersym.Char_w,               // 119    0x77
        ASTPatternLexersym.Char_x,               // 120    0x78
        ASTPatternLexersym.Char_y,               // 121    0x79
        ASTPatternLexersym.Char_z,               // 122    0x7A
        ASTPatternLexersym.Char_LeftBrace,       // 123    0x7B
        ASTPatternLexersym.Char_VerticalBar,     // 124    0x7C
        ASTPatternLexersym.Char_RightBrace,      // 125    0x7D
        ASTPatternLexersym.Char_Tilde,           // 126    0x7E

        ASTPatternLexersym.Char_AfterASCII,      // for all chars in range 128..65534
        ASTPatternLexersym.Char_EOF              // for '\uffff' or 65535 
    };
            
    public final int getKind(int i)  // Classify character at ith location
    {
        int c = (i >= getStreamLength() ? '\uffff' : getCharValue(i));
        return (c < 128 // ASCII Character
                  ? tokenKind[c]
                  : c == '\uffff'
                       ? ASTPatternLexersym.Char_EOF
                       : ASTPatternLexersym.Char_AfterASCII);
    }

    public String[] orderedExportedSymbols() { return ASTPatternParsersym.orderedTerminalSymbols; }

    public ASTPatternLexerLpgLexStream(String filename, int tab) throws java.io.IOException
    {
        super(filename, tab);
    }

    public ASTPatternLexerLpgLexStream(char[] input_chars, String filename, int tab)
    {
        super(input_chars, filename, tab);
    }

    public ASTPatternLexerLpgLexStream(char[] input_chars, String filename)
    {
        super(input_chars, filename, 1);
    }
    }

    public void ruleAction(int ruleNumber)
    {
        switch(ruleNumber)
        {

            //
            // Rule 1:  Token ::= identifier
            //
            case 1: { 
            
                checkForKeyWord();
                  break;
            }
    
            //
            // Rule 2:  Token ::= number
            //
            case 2: { 
            
                makeToken(ASTPatternParsersym.TK_NUMBER);
                  break;
            }
    
            //
            // Rule 3:  Token ::= white
            //
            case 3: { 
            
                skipToken();
                  break;
            }
    
            //
            // Rule 4:  Token ::= slc
            //
            case 4: { 
            
                makeComment(ASTPatternParsersym.TK_SINGLE_LINE_COMMENT);
                  break;
            }
    
            //
            // Rule 5:  Token ::= ,
            //
            case 5: { 
            
                makeToken(ASTPatternParsersym.TK_COMMA);
                  break;
            }
    
            //
            // Rule 6:  Token ::= :
            //
            case 6: { 
            
                makeToken(ASTPatternParsersym.TK_COLON);
                  break;
            }
    
            //
            // Rule 7:  Token ::= ;
            //
            case 7: { 
            
                makeToken(ASTPatternParsersym.TK_SEMICOLON);
                  break;
            }
    
            //
            // Rule 8:  Token ::= +
            //
            case 8: { 
            
                makeToken(ASTPatternParsersym.TK_PLUS);
                  break;
            }
    
            //
            // Rule 9:  Token ::= -
            //
            case 9: { 
            
                makeToken(ASTPatternParsersym.TK_MINUS);
                  break;
            }
    
            //
            // Rule 10:  Token ::= *
            //
            case 10: { 
            
                makeToken(ASTPatternParsersym.TK_TIMES);
                  break;
            }
    
            //
            // Rule 11:  Token ::= {
            //
            case 11: { 
            
                makeToken(ASTPatternParsersym.TK_LEFTBRACE);
                  break;
            }
    
            //
            // Rule 12:  Token ::= }
            //
            case 12: { 
            
                makeToken(ASTPatternParsersym.TK_RIGHTBRACE);
                  break;
            }
    
            //
            // Rule 13:  Token ::= [
            //
            case 13: { 
            
                makeToken(ASTPatternParsersym.TK_LEFTBRACKET);
                  break;
            }
    
            //
            // Rule 14:  Token ::= ]
            //
            case 14: { 
            
                makeToken(ASTPatternParsersym.TK_RIGHTBRACKET);
                  break;
            }
    
            //
            // Rule 15:  Token ::= (
            //
            case 15: { 
            
                makeToken(ASTPatternParsersym.TK_LEFTPAREN);
                  break;
            }
    
            //
            // Rule 16:  Token ::= )
            //
            case 16: { 
            
                makeToken(ASTPatternParsersym.TK_RIGHTPAREN);
                  break;
            }
    
            //
            // Rule 17:  Token ::= =
            //
            case 17: { 
            
                makeToken(ASTPatternParsersym.TK_EQUALS);
                  break;
            }
    
            //
            // Rule 18:  Token ::= = >
            //
            case 18: { 
            
                makeToken(ASTPatternParsersym.TK_ARROW);
                  break;
            }
    
            //
            // Rule 19:  Token ::= ! =
            //
            case 19: { 
            
                makeToken(ASTPatternParsersym.TK_NOTEQUALS);
                  break;
            }
    
            //
            // Rule 20:  Token ::= | -
            //
            case 20: { 
            
                makeToken(ASTPatternParsersym.TK_DIRECT);
                  break;
            }
    
            //
            // Rule 21:  Token ::= \ -
            //
            case 21: { 
            
                makeToken(ASTPatternParsersym.TK_DIRECTEND);
                  break;
            }
    
            //
            // Rule 22:  Token ::= . . .
            //
            case 22: { 
            
                makeToken(ASTPatternParsersym.TK_ELLIPSIS);
                  break;
            }
    
            //
            // Rule 23:  Token ::= string
            //
            case 23: { 
            
                makeToken(ASTPatternParsersym.TK_STRING);
                  break;
            }
     
    
            default:
                break;
        }
        return;
    }
}

