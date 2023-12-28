// Generated from comp2023/grammar/Javamm.g4 by ANTLR 4.5.3

    package pt.up.fe.comp2023;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link JavammParser}.
 */
public interface JavammListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by the {@code prog}
	 * labeled alternative in {@link JavammParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProg(JavammParser.ProgContext ctx);
	/**
	 * Exit a parse tree produced by the {@code prog}
	 * labeled alternative in {@link JavammParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProg(JavammParser.ProgContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Import}
	 * labeled alternative in {@link JavammParser#importDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterImport(JavammParser.ImportContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Import}
	 * labeled alternative in {@link JavammParser#importDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitImport(JavammParser.ImportContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavammParser#identifier}.
	 * @param ctx the parse tree
	 */
	void enterIdentifier(JavammParser.IdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavammParser#identifier}.
	 * @param ctx the parse tree
	 */
	void exitIdentifier(JavammParser.IdentifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ClassDecl}
	 * labeled alternative in {@link JavammParser#classDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterClassDecl(JavammParser.ClassDeclContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ClassDecl}
	 * labeled alternative in {@link JavammParser#classDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitClassDecl(JavammParser.ClassDeclContext ctx);
	/**
	 * Enter a parse tree produced by the {@code VarDecl}
	 * labeled alternative in {@link JavammParser#varDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterVarDecl(JavammParser.VarDeclContext ctx);
	/**
	 * Exit a parse tree produced by the {@code VarDecl}
	 * labeled alternative in {@link JavammParser#varDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitVarDecl(JavammParser.VarDeclContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MethodDecl}
	 * labeled alternative in {@link JavammParser#methodDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterMethodDecl(JavammParser.MethodDeclContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MethodDecl}
	 * labeled alternative in {@link JavammParser#methodDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitMethodDecl(JavammParser.MethodDeclContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MainMethod}
	 * labeled alternative in {@link JavammParser#methodDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterMainMethod(JavammParser.MainMethodContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MainMethod}
	 * labeled alternative in {@link JavammParser#methodDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitMainMethod(JavammParser.MainMethodContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavammParser#mainType}.
	 * @param ctx the parse tree
	 */
	void enterMainType(JavammParser.MainTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavammParser#mainType}.
	 * @param ctx the parse tree
	 */
	void exitMainType(JavammParser.MainTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavammParser#params}.
	 * @param ctx the parse tree
	 */
	void enterParams(JavammParser.ParamsContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavammParser#params}.
	 * @param ctx the parse tree
	 */
	void exitParams(JavammParser.ParamsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IntArr}
	 * labeled alternative in {@link JavammParser#type}.
	 * @param ctx the parse tree
	 */
	void enterIntArr(JavammParser.IntArrContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IntArr}
	 * labeled alternative in {@link JavammParser#type}.
	 * @param ctx the parse tree
	 */
	void exitIntArr(JavammParser.IntArrContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Boolean}
	 * labeled alternative in {@link JavammParser#type}.
	 * @param ctx the parse tree
	 */
	void enterBoolean(JavammParser.BooleanContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Boolean}
	 * labeled alternative in {@link JavammParser#type}.
	 * @param ctx the parse tree
	 */
	void exitBoolean(JavammParser.BooleanContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Int}
	 * labeled alternative in {@link JavammParser#type}.
	 * @param ctx the parse tree
	 */
	void enterInt(JavammParser.IntContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Int}
	 * labeled alternative in {@link JavammParser#type}.
	 * @param ctx the parse tree
	 */
	void exitInt(JavammParser.IntContext ctx);
	/**
	 * Enter a parse tree produced by the {@code String}
	 * labeled alternative in {@link JavammParser#type}.
	 * @param ctx the parse tree
	 */
	void enterString(JavammParser.StringContext ctx);
	/**
	 * Exit a parse tree produced by the {@code String}
	 * labeled alternative in {@link JavammParser#type}.
	 * @param ctx the parse tree
	 */
	void exitString(JavammParser.StringContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IdType}
	 * labeled alternative in {@link JavammParser#type}.
	 * @param ctx the parse tree
	 */
	void enterIdType(JavammParser.IdTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IdType}
	 * labeled alternative in {@link JavammParser#type}.
	 * @param ctx the parse tree
	 */
	void exitIdType(JavammParser.IdTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Stmt}
	 * labeled alternative in {@link JavammParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStmt(JavammParser.StmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Stmt}
	 * labeled alternative in {@link JavammParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStmt(JavammParser.StmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IfElseStmt}
	 * labeled alternative in {@link JavammParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterIfElseStmt(JavammParser.IfElseStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IfElseStmt}
	 * labeled alternative in {@link JavammParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitIfElseStmt(JavammParser.IfElseStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code WhileStmt}
	 * labeled alternative in {@link JavammParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterWhileStmt(JavammParser.WhileStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code WhileStmt}
	 * labeled alternative in {@link JavammParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitWhileStmt(JavammParser.WhileStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Expr}
	 * labeled alternative in {@link JavammParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterExpr(JavammParser.ExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Expr}
	 * labeled alternative in {@link JavammParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitExpr(JavammParser.ExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Assignment}
	 * labeled alternative in {@link JavammParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterAssignment(JavammParser.AssignmentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Assignment}
	 * labeled alternative in {@link JavammParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitAssignment(JavammParser.AssignmentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ArrayAssignment}
	 * labeled alternative in {@link JavammParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterArrayAssignment(JavammParser.ArrayAssignmentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ArrayAssignment}
	 * labeled alternative in {@link JavammParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitArrayAssignment(JavammParser.ArrayAssignmentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Initialization}
	 * labeled alternative in {@link JavammParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterInitialization(JavammParser.InitializationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Initialization}
	 * labeled alternative in {@link JavammParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitInitialization(JavammParser.InitializationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ArrayInitialization}
	 * labeled alternative in {@link JavammParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterArrayInitialization(JavammParser.ArrayInitializationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ArrayInitialization}
	 * labeled alternative in {@link JavammParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitArrayInitialization(JavammParser.ArrayInitializationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Parentesis}
	 * labeled alternative in {@link JavammParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterParentesis(JavammParser.ParentesisContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Parentesis}
	 * labeled alternative in {@link JavammParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitParentesis(JavammParser.ParentesisContext ctx);
	/**
	 * Enter a parse tree produced by the {@code True}
	 * labeled alternative in {@link JavammParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterTrue(JavammParser.TrueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code True}
	 * labeled alternative in {@link JavammParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitTrue(JavammParser.TrueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code False}
	 * labeled alternative in {@link JavammParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterFalse(JavammParser.FalseContext ctx);
	/**
	 * Exit a parse tree produced by the {@code False}
	 * labeled alternative in {@link JavammParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitFalse(JavammParser.FalseContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MethodCall}
	 * labeled alternative in {@link JavammParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterMethodCall(JavammParser.MethodCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MethodCall}
	 * labeled alternative in {@link JavammParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitMethodCall(JavammParser.MethodCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Integer}
	 * labeled alternative in {@link JavammParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterInteger(JavammParser.IntegerContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Integer}
	 * labeled alternative in {@link JavammParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitInteger(JavammParser.IntegerContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Not}
	 * labeled alternative in {@link JavammParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterNot(JavammParser.NotContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Not}
	 * labeled alternative in {@link JavammParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitNot(JavammParser.NotContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ArrayAccess}
	 * labeled alternative in {@link JavammParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterArrayAccess(JavammParser.ArrayAccessContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ArrayAccess}
	 * labeled alternative in {@link JavammParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitArrayAccess(JavammParser.ArrayAccessContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Length}
	 * labeled alternative in {@link JavammParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterLength(JavammParser.LengthContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Length}
	 * labeled alternative in {@link JavammParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitLength(JavammParser.LengthContext ctx);
	/**
	 * Enter a parse tree produced by the {@code This}
	 * labeled alternative in {@link JavammParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterThis(JavammParser.ThisContext ctx);
	/**
	 * Exit a parse tree produced by the {@code This}
	 * labeled alternative in {@link JavammParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitThis(JavammParser.ThisContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Id}
	 * labeled alternative in {@link JavammParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterId(JavammParser.IdContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Id}
	 * labeled alternative in {@link JavammParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitId(JavammParser.IdContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BinaryOp}
	 * labeled alternative in {@link JavammParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterBinaryOp(JavammParser.BinaryOpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BinaryOp}
	 * labeled alternative in {@link JavammParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitBinaryOp(JavammParser.BinaryOpContext ctx);
}