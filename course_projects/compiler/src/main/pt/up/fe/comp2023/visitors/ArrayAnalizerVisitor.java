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

public class ArrayAnalizerVisitor extends AJmmVisitor<SymbolTableImpl, Void> {
    List<Report> reports = new ArrayList<>();

    public List<Report> getReports() {
        return reports;
    }

    @Override
    protected void buildVisitor() {
        addVisit("ArrayInitialization", this::visitArray);
        addVisit("ArrayAccess", this::visitArray);
        setDefaultVisit(this::visitDefault);
    }

    private Void visitArray(JmmNode arrayNode, SymbolTableImpl symbolTable) {
        Utils utils = new Utils();
        Type indexType = null;
        if(arrayNode.getKind().equals("ArrayAccess")) {
            Type varType = utils.getExpressionType(arrayNode.getJmmChild(0), symbolTable);
            if (!utils.areTypesEqual(varType, new Type("int", true))) {
                reports.add(new Report(ReportType.ERROR, Stage.SEMANTIC, Integer.parseInt(arrayNode.get("lineStart")), Integer.parseInt(arrayNode.get("colStart")), "The variable " + arrayNode.getJmmChild(0).get("value") + " is not an array"));
            }
            indexType = utils.getExpressionType(arrayNode.getJmmChild(1), symbolTable);
        }
        if(arrayNode.getKind().equals("ArrayInitialization")) {
            indexType = utils.getExpressionType(arrayNode.getJmmChild(0), symbolTable);
        }
        if(!utils.areTypesEqual(indexType, new Type("int", false))) {
            reports.add(new Report(ReportType.ERROR, Stage.SEMANTIC, Integer.parseInt(arrayNode.get("lineStart")), Integer.parseInt(arrayNode.get("colStart")), "The index of the array must be of type integer"));
        }
        return null;
    }

    private Void visitDefault(JmmNode node, SymbolTableImpl symbolTable) {
        node.getChildren().stream().forEach(c -> visit(c, symbolTable));
        return null;
    }
}
