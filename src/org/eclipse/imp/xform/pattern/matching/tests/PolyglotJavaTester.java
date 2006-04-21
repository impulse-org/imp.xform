package com.ibm.watson.safari.xform.pattern.matching.tests;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashSet;
import java.util.Set;

import java_cup.runtime.Symbol;
import junit.framework.TestCase;
import polyglot.ast.Node;
import polyglot.ast.SourceFile;
import polyglot.ext.jl.ast.NodeFactory_c;
import polyglot.ext.jl.parse.Grm;
import polyglot.ext.jl.parse.Lexer_c;
import polyglot.ext.jl.types.TypeSystem_c;
import polyglot.frontend.FileSource;
import polyglot.util.CodeWriter;
import polyglot.util.StdErrorQueue;
import polyglot.visit.HaltingVisitor;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;

import com.ibm.watson.safari.xform.pattern.matching.Matcher;
import com.ibm.watson.safari.xform.pattern.matching.Matcher.MatchContext;
import com.ibm.watson.safari.xform.pattern.parser.ASTPatternLexer;
import com.ibm.watson.safari.xform.pattern.parser.ASTPatternParser;
import com.ibm.watson.safari.xform.pattern.parser.Ast.Pattern;

public class PolyglotJavaTester extends TestCase {
    private static SourceFile parseSourceFile(String srcFilePath) throws Exception {
        StdErrorQueue eq= new StdErrorQueue(System.err, 100, "__ERRORS__");
        File srcFile= new File(srcFilePath);
        FileSource fileSource= new FileSource(srcFile);
        Lexer_c lexer= new Lexer_c(new FileInputStream(srcFile), fileSource, eq);
        Grm parser= new Grm(lexer, new TypeSystem_c(), new NodeFactory_c(), eq);
        Symbol sym= parser.parse();

        return (SourceFile) sym.value;
    }

    private static Pattern parsePattern(String patternStr) {
        ASTPatternLexer lexer= new ASTPatternLexer(patternStr.toCharArray(), "__PATTERN__");
        ASTPatternParser parser= new ASTPatternParser(lexer.getLexStream());

        lexer.lexer(parser); // Why wasn't this done by the parser ctor?

        Pattern pattern= parser.parser();

        return pattern;
    }

    public MatchContext findFirstMatch(final Matcher matcher, Node root) {
        final MatchContext[] result= new MatchContext[1];

        root.visit(new HaltingVisitor() {
            /* (non-Javadoc)
             * @see polyglot.visit.NodeVisitor#enter(polyglot.ast.Node)
             */
            public NodeVisitor enter(Node n) {
                if (result[0] != null)
                    bypass(n);
                else {
                    MatchContext m= matcher.match(n);

                    if (m != null) {
                        result[0]= m;
                        bypass(n);
                    }
                }
                return this;
            }
        });
        return result[0];
    }

    public Set/*<MatchContext>*/ findAllMatches(final Matcher matcher, Node root) {
        final Set/*<MatchContext>*/ result= new HashSet();

        root.visit(new NodeVisitor() {
            /* (non-Javadoc)
             * @see polyglot.visit.NodeVisitor#enter(polyglot.ast.Node)
             */
            public NodeVisitor enter(Node n) {
                MatchContext m= matcher.match(n);

                if (m != null)
                    result.add(m);
                return this;
            }
        });
        return result;
    }

    protected void testHelper(String patternStr, String srcFile) {
        try {
            System.out.println("\n**** " + getName() + " ****\n");
            Pattern pattern= parsePattern(patternStr);
            SourceFile javaAST= parseSourceFile("resources/" + srcFile);

            Matcher matcher= new Matcher(pattern);
            MatchContext m= findFirstMatch(matcher, javaAST);

            System.out.println("Pattern = " + pattern);
            System.out.println("Source  = ");
            new PrettyPrinter().printAst(javaAST, new CodeWriter(System.out, 120));
            System.out.println("Result  = " + m);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void test1() {
        testHelper("[MethodDecl m]", "Simple.jl");
    }

    public void test2() {
        testHelper("[Expr e:int]", "Simple.jl");
    }

    public void test3() {
        testHelper("[Assign a [Variable lhs:int] [Expr rhs:int]]", "Simple.jl");
    }
}
