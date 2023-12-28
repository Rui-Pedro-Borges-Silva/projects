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

public class BinaryOpAnalizerVisitor extends AJmmVisitor<SymbolTableImpl, Void> {

    List<Report> reports = new ArrayList<>();

    public List<Report> getReports() {
        return reports;
    }

    @Override
    protected void buildVisitor() {
        addVisit("BinaryOp", this::visitBinaryOp);
        setDefaultVisit(this::visitDefault);
    }

    private Void visitBinaryOp(JmmNode binOpNode, SymbolTableImpl symbolTable) {
        Utils utils = new Utils();
        Type typeLeft = utils.getExpressionType(binOpNode.getJmmChild(0), symbolTable);
        Type typeRight = utils.getExpressionType(binOpNode.getJmmChild(1), symbolTable);
        switch (binOpNode.get("op")) {
            case "<":
            case "+":
            case "-":
            case "*":
            case "/": {
                Type intType = new Type("int", false);
                if(!utils.areTypesEqual(typeLeft, typeRight) ||
                        !utils.areTypesEqual(typeLeft, intType) ||
                        !utils.areTypesEqual(typeRight, intType)) {
                    reports.add(new Report(ReportType.ERROR, Stage.SEMANTIC, Integer.parseInt(binOpNode.get("lineStart")), Integer.parseInt(binOpNode.get("colStart")), "Operation " + binOpNode.get("op") + " expects two integers"));
                }
                break;
            }
            case "&&": {
                Type boolType = new Type("boolean", false);
                if(!utils.areTypesEqual(typeLeft, typeRight) ||
                        !utils.areTypesEqual(typeLeft, boolType) ||
                        !utils.areTypesEqual(typeRight, boolType)) {
                    reports.add(new Report(ReportType.ERROR, Stage.SEMANTIC, Integer.parseInt(binOpNode.get("lineStart")), Integer.parseInt(binOpNode.get("colStart")), "Operation " + binOpNode.get("op") + " expects two booleans"));
                }
                break;
            }
            default:
                break;

        }
        return null;
    }

    private Void visitDefault(JmmNode node, SymbolTableImpl symbolTable) {
        node.getChildren().stream().forEach(c -> visit(c, symbolTable));
        return null;
    }
}
