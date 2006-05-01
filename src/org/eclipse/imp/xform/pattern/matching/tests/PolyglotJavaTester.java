package com.ibm.watson.safari.xform.pattern.matching.tests;

import java.io.File;
import java.io.FileInputStream;
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
import polyglot.visit.PrettyPrinter;
import com.ibm.watson.safari.xform.pattern.ASTAdapter;
import com.ibm.watson.safari.xform.pattern.matching.PolyglotASTAdapter;

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

    protected ASTAdapter getASTAdapter() {
	return new PolyglotASTAdapter(fTypeSystem);
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
