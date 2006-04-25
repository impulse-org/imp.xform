package com.ibm.watson.safari.xform.pattern.matching.tests;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import org.eclipse.uide.utils.StreamUtils;
import org.jikespg.uide.parser.JikesPGLexer;
import org.jikespg.uide.parser.JikesPGParser;
import org.jikespg.uide.parser.JikesPGParser.ASTNode;
import com.ibm.watson.safari.xform.pattern.matching.JikesPGAccessorAdapter;
import com.ibm.watson.safari.xform.pattern.matching.Matcher;
import com.ibm.watson.safari.xform.pattern.matching.Matcher.MatchContext;
import com.ibm.watson.safari.xform.pattern.parser.ASTPatternParser;

public class JikesPGTester extends MatchTester {
    protected Object parseSourceFile(String srcFilePath) throws Exception {
        JikesPGLexer lexer= new JikesPGLexer(); // Create the lexer
        JikesPGParser parser= new JikesPGParser(lexer.getLexStream()); // Create the parser
        File file= new File(srcFilePath);
        InputStream is= new FileInputStream(file);

        lexer.initialize(StreamUtils.readStreamContents(is, "US-ASCII").toCharArray(), srcFilePath);
	parser.getParseStream().resetTokenStream();
	parser.setMessageHandler(new SystemOutMessageHandler());
	lexer.lexer(null, parser.getParseStream()); // Lex the char stream to produce the token stream

        ASTNode ast= (ASTNode) parser.parser();

	return ast;
    }

    protected void setAccessorAdapter() {
	ASTPatternParser.setAccessorAdapter(new JikesPGAccessorAdapter());
    }

    public Set findAllMatches(final Matcher matcher, Object astRoot) {
        final Set/*<MatchContext>*/ result= new HashSet();

        ASTNode root= (ASTNode) astRoot;

        root.accept(new JikesPGParser.AbstractVisitor() {
            public void preVisit(ASTNode n) {
                MatchContext m= matcher.match(n);

                if (m != null)
                    result.add(m);
            }

	    public void unimplementedVisitor(String s) { }
        });
        return result;
    }

    public MatchContext findFirstMatch(final Matcher matcher, Object astRoot) {
        final MatchContext[] result= new MatchContext[1];
        ASTNode root= (ASTNode) astRoot;

        root.accept(new JikesPGParser.AbstractVisitor() {
            public void preVisit(ASTNode n) {
                if (result[0] != null)
//                    bypass(n)
                    ;
                else {
                    MatchContext m= matcher.match(n);

                    if (m != null) {
                        result[0]= m;
//                        bypass(n);
                    }
                }
//                return this;
            }

	    public void unimplementedVisitor(String s) { }
        });
        return result[0];
    }

    protected void dumpSource(Object astRoot) {
	ASTNode node= (ASTNode) astRoot;
	System.out.println(node.toString());
    }

    public void test1() {
	testHelper("[nonTerm n]", "leg.g");
    }
}
