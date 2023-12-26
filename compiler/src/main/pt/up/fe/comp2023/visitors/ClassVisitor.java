package pt.up.fe.comp2023.visitors;

import pt.up.fe.comp.jmm.ast.AJmmVisitor;
import pt.up.fe.comp.jmm.ast.JmmNode;
import pt.up.fe.comp.jmm.ast.PreorderJmmVisitor;

import java.util.Map;

public class ClassVisitor extends AJmmVisitor<Map<String, String>, Void> {
    @Override
    protected void buildVisitor() {
        addVisit("ClassDecl", this::visitClassDecl);
        setDefaultVisit(this::visitDefault);
    }

    private Void visitClassDecl(JmmNode classDeclNode, Map<String, String> classMap) {
        String className = classDeclNode.get("name");
        classMap.put("className", className);

        try {
            String superName = classDeclNode.get("superName");
            classMap.put("superName", superName);
        } catch(Exception e) {}

        return null;
    }

    private Void visitDefault(JmmNode node, Map<String, String> classMap) {
        node.getChildren().stream().forEach(c -> visit(c, classMap));
        return null;
    }
}
