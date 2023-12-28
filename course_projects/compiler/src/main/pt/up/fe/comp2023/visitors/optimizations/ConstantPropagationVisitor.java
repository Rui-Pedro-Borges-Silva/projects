package pt.up.fe.comp2023.visitors.optimizations;

import pt.up.fe.comp.jmm.ast.AJmmVisitor;
import pt.up.fe.comp.jmm.ast.JmmNode;
import pt.up.fe.comp.jmm.ast.JmmNodeImpl;
import pt.up.fe.comp2023.analysis.SymbolTableImpl;

public class ConstantPropagationVisitor extends AJmmVisitor<SymbolTableImpl, Void> {

    private Boolean hasChanges = false;

    public Boolean getHasChanges() {
        return hasChanges;
    }

    @Override
    protected void buildVisitor() {
        addVisit("Assignment", this::visitAssignment);
        setDefaultVisit(this::visitDefault);
    }

    private Void visitAssignment(JmmNode assignNode, SymbolTableImpl symbolTable) {
        String aVar = null;
        Integer intValue = null;
        Boolean boolValue = null;

        if(assignNode.getJmmChild(0).getKind().equals("Integer")) {
            aVar = assignNode.get("value");
            intValue = Integer.parseInt(assignNode.getJmmChild(0).get("value"));
        } else if(assignNode.getJmmChild(0).getKind().equals("True") || assignNode.getJmmChild(0).getKind().equals("False")) {
            aVar = assignNode.get("value");
            boolValue = Boolean.parseBoolean(assignNode.getJmmChild(0).get("value"));
        }
        if (aVar != null) {
            JmmNode parent = assignNode.getJmmParent();
            Integer index = assignNode.getIndexOfSelf();
            JmmNode newNode = new JmmNodeImpl("Assignment");
            newNode.put("value", aVar);

            JmmNode newNodeChild = null;
            if(intValue != null) {
                newNodeChild = new JmmNodeImpl("Integer");
                newNodeChild.put("value", intValue.toString());
            } else if (boolValue != null) {
                if(boolValue == true) {
                    newNodeChild = new JmmNodeImpl("True");
                    newNodeChild.put("value", boolValue.toString());
                } else {
                    newNodeChild = new JmmNodeImpl("False");
                    newNodeChild.put("value", boolValue.toString());
                }
            }
            if(newNodeChild != null) {
                newNode.add(newNodeChild);
            }

            for (JmmNode child : parent.getChildren()) {
                if (child.getIndexOfSelf() > index) {
                    if(child.getKind().equals("Assignment") && child.get("value").equals(aVar)) {
                        return null;
                    } else {
                        if(child.getNumChildren() > 0) {
                            for(JmmNode child2 : child.getChildren()) {
                                if (child2.getKind().equals("Id") && child2.get("value").equals(aVar)) {
                                    child2.replace(newNode);
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private Void visitDefault(JmmNode node, SymbolTableImpl symbolTable) {
        node.getChildren().stream().forEach(c -> visit(c, symbolTable));
        return null;
    }
}
