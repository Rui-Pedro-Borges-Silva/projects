package pt.up.fe.comp2023.visitors.optimizations;

import pt.up.fe.comp.jmm.ast.AJmmVisitor;
import pt.up.fe.comp.jmm.ast.JmmNode;
import pt.up.fe.comp.jmm.ast.JmmNodeImpl;
import pt.up.fe.comp2023.analysis.SymbolTableImpl;


public class ConstantFoldingVisitor extends AJmmVisitor<SymbolTableImpl, Void> {
    private Boolean hasChanges = false;

    public Boolean getHasChanges() {
        return hasChanges;
    }

    @Override
    protected void buildVisitor() {
        addVisit("BinaryOp", this::visitBinaryOp);
        setDefaultVisit(this::visitDefault);
    }

    private Void visitBinaryOp(JmmNode binOpNode, SymbolTableImpl symbolTable) {
        String op = binOpNode.get("op");
        if(binOpNode.getJmmChild(0).getKind().equals("Integer") && binOpNode.getJmmChild(1).getKind().equals("Integer")) {
            Integer int1 = Integer.parseInt(binOpNode.getJmmChild(0).get("value"));
            Integer int2 = Integer.parseInt(binOpNode.getJmmChild(1).get("value"));

            switch (op) {
                case "+":
                case "-":
                case "*":
                case "/": {
                    Integer result = executeIntegerOp(int1, int2, op);
                    JmmNode newNode = new JmmNodeImpl("Integer");
                    newNode.put("value", result.toString());
                    binOpNode.replace(newNode);
                    hasChanges = true;
                    break;
                }
                case "<": {
                    if(int1 < int2) {
                        // true
                        JmmNode newNode = new JmmNodeImpl("True");
                        newNode.put("value", "true");
                        binOpNode.replace(newNode);
                    } else {
                        // false
                        JmmNode newNode = new JmmNodeImpl("False");
                        newNode.put("value", "false");
                        binOpNode.replace(newNode);
                    }
                    hasChanges = true;
                    break;
                }
                default:
                    break;

            }
        } else if((binOpNode.getJmmChild(0).getKind().equals("True") || binOpNode.getJmmChild(0).getKind().equals("False"))
                && (binOpNode.getJmmChild(1).getKind().equals("True") || binOpNode.getJmmChild(1).getKind().equals("False"))
                && op.equals("&&")) {
            Boolean bool1 = Boolean.parseBoolean(binOpNode.getJmmChild(0).get("value"));
            Boolean bool2 = Boolean.parseBoolean(binOpNode.getJmmChild(1).get("value"));

            if(bool1 && bool2) {
                // true
                JmmNode newNode = new JmmNodeImpl("True");
                newNode.put("value", "true");
                binOpNode.replace(newNode);
            } else {
                // false
                JmmNode newNode = new JmmNodeImpl("False");
                newNode.put("value", "false");
                binOpNode.replace(newNode);
            }
            hasChanges = true;
        }
        return null;
    }

    private Integer executeIntegerOp(Integer int1, Integer int2, String op) {
        Integer result = null;
        switch (op) {
            case "+": {
                result = int1 + int2;
                break;
            }
            case "-": {
                result = int1 - int2;
                break;
            }
            case "*": {
                result = int1 * int2;
                break;
            }
            case "/": {
                result = int1 / int2;
                break;
            }
            default: {
                break;
            }
        }
        return result;
    }


    private Void visitDefault(JmmNode node, SymbolTableImpl symbolTable) {
        node.getChildren().stream().forEach(c -> visit(c, symbolTable));
        return null;
    }
}
