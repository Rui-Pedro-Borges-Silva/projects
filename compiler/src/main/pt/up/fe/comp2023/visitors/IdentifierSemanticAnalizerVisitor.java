package pt.up.fe.comp2023.visitors;

import pt.up.fe.comp.jmm.analysis.table.Symbol;
import pt.up.fe.comp.jmm.analysis.table.Type;
import pt.up.fe.comp.jmm.ast.AJmmVisitor;
import pt.up.fe.comp.jmm.ast.JmmNode;
import pt.up.fe.comp.jmm.report.Report;
import pt.up.fe.comp.jmm.report.ReportType;
import pt.up.fe.comp.jmm.report.Stage;
import pt.up.fe.comp2023.analysis.SymbolTableImpl;
import pt.up.fe.comp2023.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class IdentifierSemanticAnalizerVisitor extends AJmmVisitor<SymbolTableImpl, Void> {
    List<Report> reports = new ArrayList<>();
    Type type = new Type("", false);

    public Type getType(JmmNode node, SymbolTableImpl symbolTable){
        visitId(node, symbolTable);
        return type;
    }

    public List<Report> getReports() {
        return reports;
    }

    @Override
    protected void buildVisitor() {
        addVisit("Id", this::visitId);
        setDefaultVisit(this::visitDefault);
    }

    private Void visitId(JmmNode idNode, SymbolTableImpl symbolTable) {
        JmmNode ancestor;
        ancestor = idNode.getAncestor("MethodDecl").orElse(idNode.getAncestor("MainMethod").orElse(null));

        if(ancestor != null) {
            String methodName = ancestor.get("name");
            if(symbolTable.getLocalVariables(methodName).size() > 0) {
                for(Symbol localVar : symbolTable.getLocalVariables(methodName)) {
                    if(localVar.getName().equals(idNode.get("value"))) {
                        type = localVar.getType();
                        return null;
                    }
                }
            }
            if(symbolTable.getParameters(methodName).size() > 0) {
                for(Symbol param : symbolTable.getParameters(methodName)) {
                    if(param.getName().equals(idNode.get("value"))) {
                        type = param.getType();
                        return null;
                    }
                }
            }

        }

        if(symbolTable.getFields().size() > 0) {
            for(Symbol field : symbolTable.getFields()) {
                if(field.getName().equals(idNode.get("value"))) {
                    type = field.getType();
                    if(ancestor.getKind().equals("MainMethod")) {
                        reports.add(new Report(ReportType.ERROR, Stage.SEMANTIC, Integer.parseInt(idNode.get("lineStart")), Integer.parseInt(idNode.get("colStart")), idNode.get("value") + " cannot be used in a static method"));
                    }
                    return null;
                }
            }
        }

        if(idNode.getJmmParent().getKind().equals("MethodCall")) {
            if(idNode.getJmmParent().getJmmChild(0).get("value").equals(idNode.get("value"))) {
                for (String importName : symbolTable.getImports()) {
                    if(idNode.get("value").equals(importName)) {
                        idNode.getJmmParent().put("isStatic", "true");
                        if(idNode.getKind().equals("Assignment") || idNode.getKind().equals("ArrayAssignment")) {
                            Utils utils = new Utils();
                            int lastChild = idNode.getChildren().size() - 1;
                            type = utils.getExpressionType(idNode.getJmmChild(lastChild), symbolTable);
                        }
                        if(idNode.getKind().equals("Id")) {
                            type = new Type(idNode.get("value"), false);
                        }
                        return null;
                    }
                }
                if(idNode.get("value").equals(symbolTable.getClassName()) || idNode.get("value").equals(symbolTable.getSuper())) {
                    type = new Type(idNode.get("value"), false);
                    return null;
                }
            }
        }

        reports.add(new Report(ReportType.ERROR, Stage.SEMANTIC, Integer.parseInt(idNode.get("lineStart")), Integer.parseInt(idNode.get("colStart")), "Id " + idNode.get("value") + " not declared"));
        return null;
    }

    private Void visitDefault(JmmNode idNode, SymbolTableImpl symbolTable) {
        idNode.getChildren().stream().forEach(c -> visit(c, symbolTable));
        return null;
    }
}
