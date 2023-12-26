package pt.up.fe.comp2023.analysis;

import pt.up.fe.comp.jmm.analysis.JmmAnalysis;
import pt.up.fe.comp.jmm.analysis.JmmSemanticsResult;
import pt.up.fe.comp.jmm.analysis.table.Symbol;
import pt.up.fe.comp.jmm.analysis.table.SymbolTable;
import pt.up.fe.comp.jmm.parser.JmmParserResult;
import pt.up.fe.comp.jmm.report.Report;
import pt.up.fe.comp2023.visitors.*;

import java.util.ArrayList;
import java.util.List;

public class JmmAnalyser implements JmmAnalysis {
    @Override
    public JmmSemanticsResult semanticAnalysis(JmmParserResult jmmParserResult) {
        SymbolTableImpl symbolTable = new SymbolTableImpl(jmmParserResult);
        List<Report> reports = new ArrayList<>();

        IdentifierSemanticAnalizerVisitor identifierSemanticAnalizerVisitor = new IdentifierSemanticAnalizerVisitor();
        identifierSemanticAnalizerVisitor.visit(jmmParserResult.getRootNode(),symbolTable);
        reports.addAll(identifierSemanticAnalizerVisitor.getReports());

        BinaryOpAnalizerVisitor binaryOpAnalizerVisitor = new BinaryOpAnalizerVisitor();
        binaryOpAnalizerVisitor.visit(jmmParserResult.getRootNode(), symbolTable);
        reports.addAll(binaryOpAnalizerVisitor.getReports());

        ArrayAnalizerVisitor arrayAnalizerVisitor = new ArrayAnalizerVisitor();
        arrayAnalizerVisitor.visit(jmmParserResult.getRootNode(), symbolTable);
        reports.addAll(arrayAnalizerVisitor.getReports());

        ThisAnalizerVisitor thisAnalizerVisitor = new ThisAnalizerVisitor();
        thisAnalizerVisitor.visit(jmmParserResult.getRootNode(), symbolTable);
        reports.addAll(thisAnalizerVisitor.getReports());

        AssignmentAnalizerVisitor assignmentAnalizerVisitor = new AssignmentAnalizerVisitor();
        assignmentAnalizerVisitor.visit(jmmParserResult.getRootNode(), symbolTable);
        reports.addAll(assignmentAnalizerVisitor.getReports());

        ConditionalAnalyserVisitor conditionalAnalyserVisitor = new ConditionalAnalyserVisitor();
        conditionalAnalyserVisitor.visit(jmmParserResult.getRootNode(), symbolTable);
        reports.addAll(conditionalAnalyserVisitor.getReports());

        MethodCallAnalizerVisitor methodCallAnalizerVisitor = new MethodCallAnalizerVisitor();
        methodCallAnalizerVisitor.visit(jmmParserResult.getRootNode(), symbolTable);
        reports.addAll(methodCallAnalizerVisitor.getReports());

        JmmSemanticsResult jmmSemanticsResult = new JmmSemanticsResult(jmmParserResult, symbolTable, reports);

        printSymbolTable(jmmSemanticsResult.getSymbolTable());
        return jmmSemanticsResult;
    }

    private void printSymbolTable(SymbolTable symbolTable) {
        System.out.println("Symbol Table:\n\n");
        if(symbolTable.getImports().size() > 0) {
            System.out.println("Imports:\n" + symbolTable.getImports().toString() + "\n");
        }
        if(symbolTable.getClassName() != null) {
            System.out.println("Class:\n" + symbolTable.getClassName());
        }
        if(symbolTable.getSuper() != null) {
            System.out.println("Extends:\n" + symbolTable.getSuper());
        }
        System.out.println("\n\n\n");
        if(symbolTable.getMethods().size() > 0) {
            System.out.println("Methods:");
            for (String method : symbolTable.getMethods()) {
                System.out.println("Name: " + method);
                String isArray = symbolTable.getReturnType(method).isArray() ? "[]" : "";
                System.out.println("Return Type: " + symbolTable.getReturnType(method).getName() + isArray);
                if(symbolTable.getParameters(method).size() > 0) {
                    System.out.println("Parameters:");
                    for(Symbol param : symbolTable.getParameters(method)) {
                        String arrayP = param.getType().isArray() ? "[]" : "";
                        System.out.println(param.getType().getName() + arrayP + " " + param.getName());
                    }
                }
                if(symbolTable.getLocalVariables(method).size() > 0) {
                    System.out.println("Local Variables:");
                    for(Symbol localVar : symbolTable.getLocalVariables(method)) {
                        String arrayLV = localVar.getType().isArray() ? "[]" : "";
                        System.out.println(localVar.getType().getName() + arrayLV + " " + localVar.getName());
                    }
                }
                System.out.println("\n");
            }
        }
    }
}
