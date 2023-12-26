package pt.up.fe.comp2023.visitors;

import pt.up.fe.comp.jmm.analysis.table.Type;
import pt.up.fe.comp.jmm.ast.AJmmVisitor;
import pt.up.fe.comp.jmm.ast.JmmNode;
import pt.up.fe.comp.jmm.report.Report;
import pt.up.fe.comp.jmm.report.ReportType;
import pt.up.fe.comp.jmm.report.Stage;
import pt.up.fe.comp2023.analysis.SymbolTableImpl;

import java.util.ArrayList;
import java.util.List;

public class ThisAnalizerVisitor extends AJmmVisitor<SymbolTableImpl, Void> {

    List<Report> reports = new ArrayList<>();

    public List<Report> getReports() {
        return reports;
    }

    @Override
    protected void buildVisitor() {
        addVisit("This", this::visitThis);
        setDefaultVisit(this::visitDefault);
    }

    private Void visitThis(JmmNode thisNode, SymbolTableImpl symbolTable) {
        JmmNode ancestor = thisNode.getAncestor("MainMethod").orElse(null);
        if (ancestor != null) {
            reports.add(new Report(ReportType.ERROR,Stage.SEMANTIC, Integer.parseInt(thisNode.get("lineStart")), Integer.parseInt(thisNode.get("colStart")), "'this' cannot be used in a static method"));
        }
        return null;
    }

    private Void visitDefault(JmmNode node, SymbolTableImpl symbolTable) {
        node.getChildren().stream().forEach(c -> visit(c, symbolTable));
        return null;
    }
}
