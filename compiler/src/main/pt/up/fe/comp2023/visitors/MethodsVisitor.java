package pt.up.fe.comp2023.visitors;

import pt.up.fe.comp.jmm.analysis.table.Symbol;
import pt.up.fe.comp.jmm.analysis.table.Type;
import pt.up.fe.comp.jmm.ast.AJmmVisitor;
import pt.up.fe.comp.jmm.ast.JmmNode;
import pt.up.fe.comp2023.analysis.JmmMethod;
import pt.up.fe.comp2023.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class MethodsVisitor extends AJmmVisitor<List<JmmMethod>, Void> {

    JmmNode methodParams;

    @Override
    protected void buildVisitor() {
        addVisit("MethodDecl", this::visitMethods);
        addVisit("MainMethod", this::visitMethods);
        setDefaultVisit(this::visitDefault);
    }

    private Void visitMethods(JmmNode methodNode, List<JmmMethod> methods) {
        JmmMethod method = new JmmMethod();

        String methodName = methodNode.get("name");

        Utils utils = new Utils();
        Type type;
        setMethodParams(methodNode);
        int numParam = methodParams.getChildren().size();
        List<Symbol> parameters = new ArrayList<>();
        if(methodNode.getKind().equals("MainMethod")) {
            type = new Type("void", false);
        } else {
            type = utils.getType(methodNode.getJmmChild(0));
            List<String> paramNames = (List<String>) methodParams.getObject("paramNames");
            for(int i=0; i<numParam; i++) {
                Type paramType = utils.getType(methodParams.getJmmChild(i));
                Symbol param = new Symbol(paramType, paramNames.get(i));
                parameters.add(param);
            }
        }

        List<Symbol> localVariables = new ArrayList<>();
        if(methodNode.getChildren().size() > 2) {
            for(int i = 2; i < methodNode.getChildren().size(); i++) {  // 0 é o tipo de retorno e 1 são os params
                if(methodNode.getJmmChild(i).getKind().equals("VarDecl")) {
                    Type localVarType = utils.getType(methodNode.getJmmChild(i));
                    Symbol localVar = new Symbol(localVarType, methodNode.getJmmChild(i).get("name"));
                    localVariables.add(localVar);
                }
            }
        }

        method.setName(methodName);
        method.setType(type);
        method.setParameters(parameters);
        method.setLocalVariables(localVariables);
        methods.add(method);
        return null;
    }

    private Void visitDefault(JmmNode node, List<JmmMethod> methods) {
        node.getChildren().stream().forEach(c -> visit(c, methods));
        return null;
    }

    private void setMethodParams(JmmNode methodNode) {
        methodNode.getChildren().forEach(c -> {
            if(c.getKind().equals("Params")) {
                methodParams = c;
            }
        });
    }
}
