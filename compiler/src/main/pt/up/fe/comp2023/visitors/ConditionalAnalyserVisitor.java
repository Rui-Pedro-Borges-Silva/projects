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

public class ConditionalAnalyserVisitor extends AJmmVisitor<SymbolTableImpl, Void> {
    List<Report> reports = new ArrayList<>();

    public List<Report> getReports() {
        return reports;
    }

    @Override
    protected void buildVisitor() {
        addVisit("IfElseStmt", this::visitConditional);
        addVisit("WhileStmt", this::visitConditional);
        setDefaultVisit(this::visitDefault);
    }

    private Void visitConditional(JmmNode condNode, SymbolTableImpl symbolTable) {
        Utils utils = new Utils();
        Type condType = utils.getExpressionType(condNode.getJmmChild(0), symbolTable);
        if(!utils.areTypesEqual(condType, new Type("boolean", false))) {
            reports.add(new Report(ReportType.ERROR, Stage.SEMANTIC, Integer.parseInt(condNode.get("lineStart")), Integer.parseInt(condNode.get("colStart")), condNode.getKind() + " is expecting a boolean type expression"));
        }
        if(condNode.getJmmChild(1) != null) {
            if(condNode.getJmmChild(1).getChildren().size() > 0) {
                if(condNode.getJmmChild(1).getJmmChild(0).getKind().equals("WhileStmt") || condNode.getJmmChild(1).getJmmChild(0).getKind().equals("IfElseStmt")) {
                    visitConditional(condNode.getJmmChild(1).getJmmChild(0), symbolTable);
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
