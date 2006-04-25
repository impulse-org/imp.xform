package com.ibm.watson.safari.xform.pattern.matching.tests;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashSet;
import java.util.Set;
import java_cup.runtime.Symbol;
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
import com.ibm.watson.safari.xform.pattern.matching.PolyglotAccessorAdapter;
import com.ibm.watson.safari.xform.pattern.matching.Matcher.MatchContext;
import com.ibm.watson.safari.xform.pattern.parser.ASTPatternParser;

public class PolyglotJavaTester extends MatchTester {
    private static final TypeSystem_c fTypeSystem= new TypeSystem_c();

    protected void dumpSource(Object srcAST) {
	new PrettyPrinter().printAst((Node) srcAST, new CodeWriter(System.out, 120));
    }

    protected Object parseSourceFile(String srcFilePath) throws Exception {
        StdErrorQueue eq= new StdErrorQueue(System.err, 100, "__ERRORS__");
        File srcFile= new File(srcFilePath);
        FileSource fileSource= new FileSource(srcFile);
        Lexer_c lexer= new Lexer_c(new FileInputStream(srcFile), fileSource, eq);
        Grm parser= new Grm(lexer, fTypeSystem, new NodeFactory_c(), eq);
        Symbol sym= parser.parse();

        return (SourceFile) sym.value;
    }

    protected void setAccessorAdapter() {
	ASTPatternParser.setAccessorAdapter(new PolyglotAccessorAdapter(fTypeSystem));
    }

    public MatchContext findFirstMatch(final Matcher matcher, Object astRoot) {
        final MatchContext[] result= new MatchContext[1];
        Node root= (Node) astRoot;

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

    public Set/*<MatchContext>*/ findAllMatches(final Matcher matcher, Object astRoot) {
        final Set/*<MatchContext>*/ result= new HashSet();

        Node root= (Node) astRoot;

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

    public void test1() {
        testHelper("[MethodDecl m]", "Simple.jl");
    }

    public void test2() {
        testHelper("[Expr e]", "Simple.jl");
    }

    public void test2a() {
        testHelper("[Expr e:int]", "Simple.jl");
    }

    public void test3() {
        testHelper("[Assign a [Variable lhs:int] [Expr rhs:int]]", "Simple.jl");
    }
}
