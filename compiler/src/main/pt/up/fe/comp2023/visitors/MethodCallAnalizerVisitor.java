package pt.up.fe.comp2023.visitors;

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

public class MethodCallAnalizerVisitor extends AJmmVisitor<SymbolTableImpl, Void> {
    List<Report> reports = new ArrayList<>();

    public List<Report> getReports() {
        return reports;
    }

    @Override
    protected void buildVisitor() {
        addVisit("MethodCall", this::visitMethodCall);
        setDefaultVisit(this::visitDefault);
    }

    private Void visitMethodCall(JmmNode methodCallNode, SymbolTableImpl symbolTable) {
        Utils utils = new Utils();
        Type idType = utils.getExpressionType(methodCallNode.getJmmChild(0), symbolTable);
        if (idType == null ) {
            idType = new Type(methodCallNode.getJmmChild(0).get("value"), false);
        }
        if(!symbolTable.getMethods().contains(methodCallNode.get("name")) &&
                !symbolTable.getImports().contains(idType.getName()) &&
                !(symbolTable.getClassName().equals(idType.getName()) && symbolTable.getSuper() != null) &&
                !(idType.getName().equals(symbolTable.getSuper()) && symbolTable.getImports().contains(idType.getName()))) {
            reports.add(new Report(ReportType.ERROR, Stage.SEMANTIC, Integer.parseInt(methodCallNode.get("lineStart")), Integer.parseInt(methodCallNode.get("colStart")), "Call to an undeclared method: " + methodCallNode.get("name")));
        }

        if(symbolTable.getMethods().contains(methodCallNode.get("name")) &&
                !symbolTable.getImports().contains(utils.getExpressionType(methodCallNode.getJmmChild(0), symbolTable).getName())) {
            Type returnType = symbolTable.getReturnType(methodCallNode.get("name"));
            Type expectedReturnType = utils.getExpectedType(methodCallNode.getJmmParent(), symbolTable);
            if(!utils.areTypesEqual(returnType, expectedReturnType) && !methodCallNode.getJmmParent().getKind().equals("Expr") &&
                    !(expectedReturnType.getName().equals(symbolTable.getSuper()) && returnType.getName().equals(symbolTable.getClassName())) &&
                    !symbolTable.getImports().contains(returnType.getName())) {
                reports.add(new Report(ReportType.ERROR, Stage.SEMANTIC, Integer.parseInt(methodCallNode.get("lineStart")), Integer.parseInt(methodCallNode.get("colStart")), "The method's return type is incompatible with the expected return type"));
            }
            if(methodCallNode.getChildren().size() > 1) { // if the called method has arguments
                if(methodCallNode.getChildren().size() - 1 != symbolTable.getParameters(methodCallNode.get("name")).size()) {
                    reports.add(new Report(ReportType.ERROR, Stage.SEMANTIC, Integer.parseInt(methodCallNode.get("lineStart")), Integer.parseInt(methodCallNode.get("colStart")),  methodCallNode.get("name") + " is being called with an incorrect number of arguments"));
                    return null;
                }
                for (int i = 1; i < methodCallNode.getChildren().size(); i++) {
                    if(!utils.areTypesEqual(
                                utils.getExpressionType(methodCallNode.getJmmChild(i), symbolTable),
                                symbolTable.getParameters(methodCallNode.get("name")).get(i - 1).getType()) &&
                            !(utils.getExpressionType(methodCallNode.getJmmChild(i), symbolTable).getName().equals(symbolTable.getClassName()) &&
                                    symbolTable.getParameters(methodCallNode.get("name")).get(i - 1).getType().getName().equals(symbolTable.getSuper())) &&
                            !utils.getExpressionType(methodCallNode.getJmmChild(i), symbolTable).getName().equals("")) {
                        reports.add(new Report(ReportType.ERROR, Stage.SEMANTIC, Integer.parseInt(methodCallNode.get("lineStart")), Integer.parseInt(methodCallNode.get("colStart")), "The types of the arguments of the method " + methodCallNode.get("name") + " are incompatible with the types expected"));
                        return null;
                    }
                }
            } else { // has no arguments
                if(symbolTable.getParameters(methodCallNode.get("name")).size() > 0) {
                    reports.add(new Report(ReportType.ERROR, Stage.SEMANTIC, Integer.parseInt(methodCallNode.get("lineStart")), Integer.parseInt(methodCallNode.get("colStart")), "Invalid call to the method " + methodCallNode.get("name")));
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
