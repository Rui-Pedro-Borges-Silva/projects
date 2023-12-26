package pt.up.fe.comp2023.utils;

import pt.up.fe.comp.jmm.analysis.table.Type;
import pt.up.fe.comp.jmm.ast.JmmNode;
import pt.up.fe.comp2023.analysis.SymbolTableImpl;
import pt.up.fe.comp2023.visitors.IdentifierSemanticAnalizerVisitor;

public class Utils {
    public static char[] typeToString(Type fieldType) {
        return fieldType.getName().toCharArray();
    }

    /*
    * receives a node and returns its type
    * */
    public Type getType(JmmNode node) {
        Type type;
        switch (node.getKind()) {
            case "IntArr": {
                type = new Type("int", true);
                break;
            }
            case "Boolean": {
                type = new Type("boolean", false);
                break;
            }
            case "Int": {
                type = new Type("int", false);
                break;
            }
            case "String": {
                type = new Type("string", false);
                break;
            }
            case "IdType":
            case "Id": {
                type = new Type(node.get("value"), false);
                break;
            }
            default: {
                if (node.getChildren().size() > 0) {
                    type = getType(node.getJmmChild(0));
                } else {
                    type = new Type("", false);
                }
                break;
            }
        }
        return type;
    }



    /*
     * receives an expression node and returns its type
     * */
    public Type getExpressionType(JmmNode exprNode, SymbolTableImpl symbolTable) {
        Type type;
        switch (exprNode.getKind()) {
            case "Parentesis": {
                type = getExpressionType(exprNode.getJmmChild(0), symbolTable);
                break;
            }
            case "MethodCall": {
                if(symbolTable.getMethods().contains(exprNode.get("name"))) {
                    type = symbolTable.getReturnType(exprNode.get("name"));
                }
                else if(exprNode.getJmmChild(0).getKind().equals("This") ||
                        (symbolTable.getSuper() != null && symbolTable.getImports().contains(symbolTable.getSuper()))) {
                    if(exprNode.getJmmParent().getKind().equals("Assignment")) {
                        type = getExpressionType(exprNode.getJmmParent(),symbolTable);
                        break;
                    } else if (exprNode.getJmmParent().getKind().equals("ArrayAssignment")) {
                        type = new Type("int", false);
                        break;
                    } else if (exprNode.getJmmParent().getKind().equals("IfElseStmt") || exprNode.getJmmParent().getKind().equals("WhileStmt")) {
                        type = new Type("boolean", false);
                        break;
                    } else if (exprNode.getJmmParent().getKind().equals("BinaryOp")) {
                        switch (exprNode.getJmmParent().get("op")) {
                            case "+":
                            case "-":
                            case "*":
                            case "/":
                            case "<": {
                                type = new Type("int", false);
                                break;
                            }
                            case "&&": {
                                type = new Type("boolean", false);
                                break;
                            }
                            default: {
                                type = null;
                                break;
                            }
                        }
                        break;
                    } else {
                        type = new Type("", false);
                        break;
                    }
                }
                else if(symbolTable.getImports().contains(getExpressionType(exprNode.getJmmChild(0), symbolTable).getName()) ||
                        symbolTable.getClassName().equals(getExpressionType(exprNode.getJmmChild(0), symbolTable).getName()) ||
                        (symbolTable.getSuper() != null && getExpressionType(exprNode.getJmmChild(0), symbolTable).getName().equals(symbolTable.getSuper()))){
                    if(exprNode.getJmmParent().getKind().equals("BinaryOp")) {
                        type = new Type("int", false);
                    } else {
                        type = getExpressionType(exprNode.getJmmParent(), symbolTable);
                    }
                } else {
                    type = new Type("", false);
                }
                break;
            }
            case "ArrayAccess":
            case "Integer":
            case "Length": {
                type = new Type("int", false);
                break;
            }
            case "WhileStmt":
            case "IfElseStmt":
            case "True":
            case "False":
            case "Not": {
                type = new Type("boolean", false);
                break;
            }
            case "ArrayInitialization": {
                type = new Type("int", true);
                break;
            }
            case "Initialization": {
                type = new Type(exprNode.get("name"), false);
                break;
            }
            case "BinaryOp": {
                Type typeLeft = getExpressionType(exprNode.getJmmChild(0), symbolTable);
                Type typeRight = getExpressionType(exprNode.getJmmChild(1), symbolTable);
                if(exprNode.get("op").equals("&&") || exprNode.get("op").equals("<")) {
                    type = new Type("boolean", false);
                    break;
                }
                if(areTypesEqual(typeLeft, typeRight)) {
                    type = typeLeft;
                }
                else {
                    type = new Type("", false);
                }
                break;
            }
            case "Assignment":
            case "ArrayAssignment":
            case "Id": {
                IdentifierSemanticAnalizerVisitor identifierSemanticAnalizerVisitor = new IdentifierSemanticAnalizerVisitor();
                type = identifierSemanticAnalizerVisitor.getType(exprNode, symbolTable);
                break;
            }
            case "This": {
                // type = getExpressionType(exprNode.getJmmParent(), symbolTable);
                type = new Type(symbolTable.getClassName(), false);
                break;
            }
            default: { // fica assim ??
                type = getType(exprNode.getJmmChild(0));
                break;
            }
        }
        return type;
    }

    public Type getExpectedType(JmmNode exprNode, SymbolTableImpl symbolTable) {
        if(exprNode.getKind().equals("BinaryOp")) {
            if(exprNode.get("op").equals("<")) {
                return new Type("int", false);
            }
        }
        return getExpressionType(exprNode, symbolTable);
    }

    public boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /*
     * receives two types and determines if they are equal
     * */
    public Boolean areTypesEqual(Type type1, Type type2) {
        if(type1.getName().equals(type2.getName()) && type1.isArray() == type2.isArray()) {
            return true;
        }
        return false;
    }

    public int getTypeCode(Type type) {
        String typeName = type.getName();
        boolean isArray = type.isArray();

        if (typeName.equals("int")) {
            return isArray ? 2 : 1;
        } else if (typeName.equals("boolean")) {
            return 3;
        } else {
            return 0;
        }
    }

}
