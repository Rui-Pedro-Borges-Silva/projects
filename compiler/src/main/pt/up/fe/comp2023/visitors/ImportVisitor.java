package pt.up.fe.comp2023.visitors;

import pt.up.fe.comp.jmm.ast.JmmNode;
import pt.up.fe.comp.jmm.ast.PreorderJmmVisitor;

import java.util.List;
import java.util.stream.Collectors;

public class ImportVisitor extends PreorderJmmVisitor<List<String>, Boolean> {

    @Override
    protected void buildVisitor() {
        addVisit("Import", this::visitImport);
        setDefaultVisit(this::visitDefault);
    }

    private Boolean visitImport(JmmNode importNode, List<String> imports) {
        String importName = importNode.getChildren().stream().map(i -> i.get("name")).collect(Collectors.joining("."));

        imports.add(importName);

        return true;
    }

    private Boolean visitDefault(JmmNode node, List<String> list) {
        return true;
    }
}
