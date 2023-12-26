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

public class AssignmentAnalizerVisitor extends AJmmVisitor<SymbolTableImpl, Void> {

    List<Report> reports = new ArrayList<>();

    public List<Report> getReports() {
        return reports;
    }

    Utils utils = new Utils();

    @Override
    protected void buildVisitor() {
        addVisit("ArrayAssignment", this::visitArrayAssignment);
        addVisit("Assignment", this::visitAssignment);
        setDefaultVisit(this::visitDefault);
    }

    private Void visitAssignment(JmmNode assignNode, SymbolTableImpl symbolTable) {
        IdentifierSemanticAnalizerVisitor identifierSemanticAnalizerVisitor = new IdentifierSemanticAnalizerVisitor();
        Type idType = identifierSemanticAnalizerVisitor.getType(assignNode, symbolTable);
        reports.addAll(identifierSemanticAnalizerVisitor.getReports());
        Type expressionType = utils.getExpressionType(assignNode.getJmmChild(0), symbolTable);
        JmmNode ancestor = assignNode.getAncestor("MainMethod").orElse(null);
        if(assignNode.getJmmChild(0).getKind().equals("This")
                && ancestor != null
                && !idType.getName().equals(symbolTable.getClassName())
                && !idType.getName().equals(symbolTable.getSuper())) {
            reports.add(new Report(ReportType.ERROR,Stage.SEMANTIC, Integer.parseInt(assignNode.get("lineStart")), Integer.parseInt(assignNode.get("colStart")), "Cannot assign 'this' to variable " + assignNode.get("value")));
        }
        if(!utils.areTypesEqual(idType, expressionType) &&
                !(expressionType.getName().equals(symbolTable.getClassName()) && idType.getName().equals(symbolTable.getSuper())) &&
                !(symbolTable.getImports().contains(idType.getName()) || symbolTable.getImports().contains(expressionType.getName()))) {
            reports.add(new Report(ReportType.ERROR, Stage.SEMANTIC, Integer.parseInt(assignNode.get("lineStart")), Integer.parseInt(assignNode.get("colStart")), "Invalid assigment to variable " + assignNode.get("value")));
        }
        return null;
    }

    private Void visitArrayAssignment(JmmNode assignNode, SymbolTableImpl symbolTable) {
        Type arrayIndexType = utils.getExpressionType(assignNode.getJmmChild(0), symbolTable);
        Type assignExpressionType = utils.getExpressionType(assignNode.getJmmChild(1), symbolTable);
        if(!utils.areTypesEqual(arrayIndexType, new Type("int", false))) {
            reports.add(new Report(ReportType.ERROR, Stage.SEMANTIC, Integer.parseInt(assignNode.get("lineStart")), Integer.parseInt(assignNode.get("colStart")), "The index of the array has an invalid type"));
        }
        if(!utils.areTypesEqual(assignExpressionType, new Type("int", false))) {
            reports.add(new Report(ReportType.ERROR, Stage.SEMANTIC, Integer.parseInt(assignNode.get("lineStart")), Integer.parseInt(assignNode.get("colStart")), "Invalid assigment to variable " + assignNode.get("value")));
        }
        return null;
    }

    private Void visitDefault(JmmNode node, SymbolTableImpl symbolTable) {
        node.getChildren().stream().forEach(c -> visit(c, symbolTable));
        return null;
    }
}
