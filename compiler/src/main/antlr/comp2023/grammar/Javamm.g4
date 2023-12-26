grammar Javamm;

@header {
    package pt.up.fe.comp2023;
}

INTEGER : ([0] | [1-9][0-9]*);
ID : [a-zA-Z_$][a-zA-Z_$0-9]* ;
INTARR : 'int' '[' ']';

WS : [ \t\n\r\f]+ -> skip ;
COMMENT : '/*' .*? '*/' -> skip ;
LINE_COMMENT : '//' ~[\r\n]* -> skip ;

program
    : (importDeclaration)* classDeclaration EOF #prog
    ;

importDeclaration
    : 'import' identifier ('.' identifier )* ';' #Import
    ;

identifier
    : name=ID
    ;

classDeclaration
    : 'class' name=ID ('extends' superName=ID )? '{' (varDeclaration)* (methodDeclaration)* '}' #ClassDecl
    ;

varDeclaration
    : type name=ID ';' #VarDecl
    ;

methodDeclaration
    : ('public')? type name=ID params '{' (varDeclaration)* (statement)* 'return' expression ';' '}' #MethodDecl
    | ('public')? 'static' mainType name='main' params '{' (varDeclaration)* (statement)* '}' #MainMethod
    ;

mainType
    : 'void'
    ;

params locals[List<Token> paramNames=new ArrayList<>()]
    : '(' (type ID {$paramNames.add($ID);}(',' type ID {$paramNames.add($ID);})*)? ')'
    | '(' 'String' '[' ']' ID {$paramNames.add($ID);}')'
    ;

type
    : name=INTARR #IntArr
    | name='boolean' #Boolean
    | name='int' #Int
    | name='String' #String
    | value=ID #IdType
    ;

statement
    : '{' (statement)* '}' #Stmt
    | 'if' '(' expression ')' statement 'else' statement #IfElseStmt
    | 'while' '(' expression ')' statement #WhileStmt
    | expression ';' #Expr
    | value=ID '=' expression ';' #Assignment
    | value=ID '[' expression ']' '=' expression ';' #ArrayAssignment
    ;

expression
    : '(' expression ')' #Parentesis
    | expression '[' expression ']' #ArrayAccess
    | expression '.' name=ID '(' (expression (',' expression)*)? ')' #MethodCall
    | expression '.' 'length' #Length
    | '!' expression #Not
    | 'new' name=ID '(' ')' #Initialization
    | 'new' 'int' '[' expression ']' #ArrayInitialization
    | expression op=( '*' | '/' ) expression #BinaryOp
    | expression op=( '+' | '-' ) expression #BinaryOp
    | expression op=( '<' | '&&' ) expression #BinaryOp
    | value=INTEGER #Integer
    | value='true' #True
    | value='false' #False
    | value=ID #Id
    | 'this' #This
    ;
