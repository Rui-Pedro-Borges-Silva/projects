package pt.up.fe.comp2023.visitors;

import pt.up.fe.comp.jmm.analysis.table.Symbol;
import pt.up.fe.comp.jmm.analysis.table.Type;
import pt.up.fe.comp.jmm.ast.AJmmVisitor;
import pt.up.fe.comp.jmm.ast.JmmNode;
import pt.up.fe.comp2023.utils.Utils;

import java.util.List;

public class FieldVisitor extends AJmmVisitor<List<Symbol>, Void> {
    @Override
    protected void buildVisitor() {
        addVisit("VarDecl", this::visitFields);
        setDefaultVisit(this::visitDefault);
    }

    private Void visitFields(JmmNode fieldNode, List<Symbol> fields) {
        if(!fieldNode.getJmmParent().getKind().equals("ClassDecl")) {
            return null;
        }
        String fieldName = fieldNode.get("name");
        Utils utils = new Utils();
        Type type = utils.getType(fieldNode.getJmmChild(0));
        Symbol field = new Symbol(type, fieldName);
        fields.add(field);
        return null;
    }

    private Void visitDefault(JmmNode node, List<Symbol> fields) {
        node.getChildren().stream().forEach(c -> visit(c, fields));
        return null;
    }
}
