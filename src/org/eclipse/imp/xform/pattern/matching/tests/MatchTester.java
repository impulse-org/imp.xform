package com.ibm.watson.safari.xform.pattern.matching.tests;

import java.util.Set;
import junit.framework.TestCase;
import com.ibm.watson.safari.xform.pattern.matching.Matcher;
import com.ibm.watson.safari.xform.pattern.matching.Matcher.MatchContext;
import com.ibm.watson.safari.xform.pattern.parser.ASTPatternLexer;
import com.ibm.watson.safari.xform.pattern.parser.ASTPatternParser;
import com.ibm.watson.safari.xform.pattern.parser.Ast.Pattern;

public abstract class MatchTester extends TestCase {
    public abstract Set findAllMatches(final Matcher matcher, Object astRoot);

    public abstract MatchContext findFirstMatch(final Matcher matcher, Object astRoot);

    protected abstract void setAccessorAdapter();

    protected abstract Object parseSourceFile(String srcFilePath) throws Exception;

    protected Pattern parsePattern(String patternStr) {
        ASTPatternLexer lexer= new ASTPatternLexer(patternStr.toCharArray(), "__PATTERN__");
        ASTPatternParser parser= new ASTPatternParser(lexer.getLexStream());
    
        lexer.lexer(parser); // Why wasn't this done by the parser ctor?
        setAccessorAdapter();
    
        Pattern pattern= parser.parser();
    
        return pattern;
    }

    protected void testHelper(String patternStr, String srcFile) {
        try {
            System.out.println("\n**** " + getName() + " ****\n");
            Pattern pattern= parsePattern(patternStr);

            assertNotNull("No AST produced for AST pattern!", pattern);

            Object srcAST= parseSourceFile("resources/" + srcFile);
    
            assertNotNull("No AST produced for target source file!", srcAST);

            Matcher matcher= new Matcher(pattern);
            MatchContext m= findFirstMatch(matcher, srcAST);
    
            System.out.println("Pattern = " + pattern);
            System.out.println("Source  = ");
            dumpSource(srcAST);
            System.out.println("Result  = " + m);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected abstract void dumpSource(Object astRoot);
}
