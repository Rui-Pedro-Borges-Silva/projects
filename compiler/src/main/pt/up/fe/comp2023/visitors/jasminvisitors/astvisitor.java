package pt.up.fe.comp2023.visitors.jasminvisitors;

import org.antlr.v4.runtime.misc.Pair;
import pt.up.fe.comp.jmm.analysis.table.Symbol;
import pt.up.fe.comp.jmm.analysis.table.Type;
import pt.up.fe.comp.jmm.ast.AJmmVisitor;
import pt.up.fe.comp.jmm.ast.JmmNode;
import pt.up.fe.comp.jmm.report.Report;
import pt.up.fe.comp.jmm.report.ReportType;
import pt.up.fe.comp.jmm.report.Stage;
import pt.up.fe.comp2023.analysis.SymbolTableImpl;
import pt.up.fe.comp2023.utils.Utils;
import pt.up.fe.comp2023.utils.helpertojas;

import javax.swing.*;

import static java.util.stream.Collectors.joining;

import java.util.HashMap;
import java.util.stream.Collectors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.*;


public class astvisitor extends AJmmVisitor<SymbolTableImpl, Void> {

    private final Map<String, Map<String, Integer>> sav;
//começaa

    private SymbolTableImpl symbolTable;

    private Map<String, Pair<Type, Integer>> currentMethodRegisters = new HashMap<>();
    private StringBuilder currsb = null;
    private int currentStack = 0;
    private int maxStack = 0;

    //finishhhhh
    public StringBuilder sb = new StringBuilder();
    List<Report> report = new ArrayList<>();
    private int labelCounter;

    public astvisitor(Map<String, Map<String, Integer>> sav) {
        this.sav = sav;
    }

    private Integer getRegister(JmmNode id) {

        if(id.getKind() == id.get("name")){
            var name = id.get("value");
            var methName = id.getAncestor("MethodDecl").orElse(null);
            if(methName == null) {
                return null;
            }
            if(sav.get(methName) != null) {
                return sav.get(methName).get(name);
            }
        }
        return null;
    }
    private Integer getRegister(String id, String methName) {

        if(methName == null) {
            return null;
        }
        if(sav.get(methName) != null) {
            return sav.get(methName).get(id);
        }
        return null;
    }

    public List<Report> getReport(){
        return report;
    }
    public StringBuilder getSB(){
        return sb;
    }


    private void updateMaxStack(int value) {
        currentStack += value;
        if (currentStack > maxStack) {
            maxStack = currentStack;
        }
    }



    @Override
    protected void buildVisitor() {
        addVisit("prog", this::visit_prog);
        addVisit("ClassDecl", this::visit_class);
        addVisit("BinaryOp", this::visit_ast_op);
        addVisit("VarDecl", this::visit_ast_field);
        //addVisit("Boolean", this::visit_ast_Boolean);
        addVisit("Initialization", this::visit_ast_Initialization);
        addVisit("ArrayInitialization", this::visit_ast_arrayinitialization);
        addVisit("Assignment", this::visit_ast_assignment);
        addVisit("MethodCall", this::visit_ast_methodcall);
        addVisit("MainMethod", this::visit_ast_mainmethod);
        addVisit("MethodDecl", this::visit_ast_mainmethod);
        addVisit("Integer", this::visit_ast_integer);
        addVisit("Id", this::visit_ast_id);

        setDefaultVisit(this::visitDefault);
    }

    private Pair<Type, Boolean> check_id(JmmNode idNode, SymbolTableImpl symbolTable) {
        JmmNode ancestor;
        Type type = new Type("", false);
        ancestor = idNode.getAncestor("MethodDecl").orElse(idNode.getAncestor("MainMethod").orElse(null));

        if (ancestor != null) {
            String methodName = ancestor.get("name");
            if (symbolTable.getLocalVariables(methodName).size() > 0) {
                for (Symbol localVar : symbolTable.getLocalVariables(methodName)) {
                    if (localVar.getName().equals(idNode.get("value"))) {
                        type = localVar.getType();
                        return new Pair<Type,Boolean>(type, false);
                    }
                }
            }
            if (symbolTable.getParameters(methodName).size() > 0) {
                for (Symbol param : symbolTable.getParameters(methodName)) {
                    if (param.getName().equals(idNode.get("value"))) {
                        type = param.getType();
                        return new Pair<Type,Boolean>(type, false);
                    }
                }
            }
        }

        if (symbolTable.getFields().size() > 0) {
            for (Symbol field : symbolTable.getFields()) {
                if (field.getName().equals(idNode.get("value"))) {
                    type = field.getType();
                    return new Pair<Type,Boolean>(type, true);
                }
            }
        }

        if (idNode.getJmmParent().getKind().equals("MethodCall")) {
            if (idNode.getJmmParent().getJmmChild(0).get("value").equals(idNode.get("value"))) {
                for (String importName : symbolTable.getImports()) {
                    if (idNode.get("value").equals(importName)) {
                        idNode.getJmmParent().put("isStatic", "true");
                        if (idNode.getKind().equals("Assignment") || idNode.getKind().equals("ArrayAssignment")) {
                            Utils utils = new Utils();
                            int lastChild = idNode.getChildren().size() - 1;
                            type = utils.getExpressionType(idNode.getJmmChild(lastChild), symbolTable);
                        }
                        if (idNode.getKind().equals("Id")) {
                            type = new Type(idNode.get("value"), false);
                        }
                        return new Pair<Type,Boolean>(type, false);
                    }
                }
                if (idNode.get("value").equals(symbolTable.getClassName()) || idNode.get("value").equals(symbolTable.getSuper())) {
                    type = new Type(idNode.get("value"), false);
                    return new Pair<Type,Boolean>(type, false);
                }
            }
        }
        return new Pair<Type,Boolean>(type, false);
    }
    private Void visit_ast_id(JmmNode inode, SymbolTableImpl symbolTable) {
        Pair<Type, Boolean> type = check_id(inode, symbolTable);

        if(type.b){
            sb.append("getfield ").append(symbolTable.getClassName()).append("/").append(inode.get("value")).append(" ").append(helpertojas.getString(type.a)).append("\n");
        }
        else {
            if (type.a.getName().equals("int")) {
                sb.append("iload ");
                check_null(inode);
            } else if(inode.get("value") != null){
                sb.append("aload_");
                check_null(inode);
            }
        }
        return null;
    }


    private Void visit_ast_integer(JmmNode jmmNode, SymbolTableImpl symbolTable) {
        if (Integer.parseInt(jmmNode.get("value")) >= -1 && Integer.parseInt(jmmNode.get("value")) <= 5) {
            sb.append("iconst_" + jmmNode.get("value") + "\n");
        } else if (Integer.parseInt(jmmNode.get("value")) >= Byte.MIN_VALUE && Integer.parseInt(jmmNode.get("value")) <= Byte.MAX_VALUE) {
            sb.append("bipush " + jmmNode.get("value") + "\n");
        } else if (Integer.parseInt(jmmNode.get("value")) >= Short.MIN_VALUE && Integer.parseInt(jmmNode.get("value")) <= Short.MAX_VALUE) {
            sb.append("sipush" + jmmNode.get("value") + "\n");
        } else {
            sb.append("ldc " + jmmNode.get("value") + "\n");
        }
        return null;
    }


    private Void visit_prog(JmmNode pnode, SymbolTableImpl symbolTable) {
        for(JmmNode child : pnode.getChildren()){
            visit(child, symbolTable);
        }
        return null;
    }

    private Void visit_ast_mainmethod(JmmNode jmmNode, SymbolTableImpl symbolTable) {
        String methodName = jmmNode.get("name");

        this.generateRegisterLocalVariableAllocation(methodName ,symbolTable);

        sb.append(".method ");

        if (!jmmNode.hasAttribute("public")) {
            sb.append("public ");
        }
        if (jmmNode.get("name").equals("main")) {
            sb.append("static ");
        }

        sb.append(methodName).append("(");


        if(jmmNode.get("name").equals("main")){
            sb.append("[Ljava/lang/String;");
        }

        // Add method parameters to the method signature
        List<Symbol> parameters = symbolTable.getParameters(methodName);
        for (Symbol param : parameters) {
            sb.append(helpertojas.getString(param.getType()));
        }

        Type returnType = symbolTable.getReturnType(methodName);

        sb.append(")").append(helpertojas.getString(returnType)).append("\n");

        this.currsb = new StringBuilder();
        this.currentStack = 0;
        this.maxStack = 0;

        sb.append(".limit stack ").append("99").append("\n");
        sb.append(this.currsb);

        sb.append(".limit locals ");
        sb.append("99");
        sb.append("\n");

        for (JmmNode child : jmmNode.getChildren()) {
            visit(child, symbolTable);
        }

        if (returnType.getName().equals("void")) {
            sb.append("return\n");
        } else if (returnType.getName().equals("int") && !returnType.isArray()
                || returnType.getName().equals("Boolean")) {
            sb.append("ireturn\n");
        } else {
            sb.append("areturn\n");
        }


        sb.append(".end method\n\n");
        this.currentMethodRegisters = new HashMap<>();

        return null;
    }

    private void generateRegisterLocalVariableAllocation(String methodName, SymbolTableImpl symbolTable) {
        this.currentMethodRegisters.put("this",
                new Pair<>(new Type(symbolTable.getClassName(), false), 0));
        int registerIndex = 1;

        for (int i = 0; i < symbolTable.getParameters(methodName).size(); i++) {
            this.currentMethodRegisters.put(symbolTable.getParameters(methodName).get(i).getName(),
                    new Pair<>(symbolTable.getParameters(methodName).get(i).getType(),
                            registerIndex));
            registerIndex++;
        }

        for (int i = 0; i < symbolTable.getLocalVariables(methodName).size(); i++) {
            this.currentMethodRegisters.put(symbolTable.getLocalVariables(methodName).get(i).getName(),
                    new Pair<>(symbolTable.getLocalVariables(methodName).get(i).getType(),
                            registerIndex));
            registerIndex++;
        }
    }


    private Void visit_ast_methodcall(JmmNode methodCall, SymbolTableImpl symbolTable) {
        JmmNode m = methodCall.getAncestor("ClassDecl").orElse(null);
        String methodName = methodCall.get("name");
        JmmNode c1 = methodCall.getJmmChild(0);
        JmmNode nn = methodCall.getJmmParent();
        String objname = "";

        JmmNode scopeCall = methodCall.getJmmChild(0);
        String invokeType = "invokestatic ";

        if (methodCall.hasAttribute("isStatic") && methodCall.get("isStatic").equals("true")){
            objname = c1.get("value");
            invokeType = "invokestatic ";
        }else{
            visit(scopeCall, symbolTable);
            objname = symbolTable.getClassName();
            invokeType = "invokevirtual ";
        }
        if(methodCall.getKind().equals("Expr")){
            objname = "Expr";
            invokeType = "invokespecial ";
        }



        for (int i = 1; i < methodCall.getNumChildren(); i++) {
            JmmNode Arg = methodCall.getJmmChild(i);
            visit(Arg, symbolTable);
        }

        sb.append(invokeType);



        sb.append(objname).append("/");
        sb.append(methodName).append("(");

        // Add method parameters to the method signature
        List<Symbol> parameters = symbolTable.getParameters(methodName);
        if (parameters != null) { //null
            for (Symbol param : parameters) {
                sb.append(helpertojas.getString(param.getType()));
            }
        }else {
            for (int i = 1; i < methodCall.getNumChildren(); i++) {
                JmmNode Arg = methodCall.getJmmChild(i);
                String typearg =Arg.getKind();

                switch (typearg){
                    case ("Integer"):
                    case ("Id"):{
                        sb.append("I");
                        break;
                    }
                    case "Boolean": {
                        sb.append("B");
                        break;
                    }
                    case "String": {
                        sb.append("S");
                        break;
                    }
                    case "IntArr": {
                        //sb.append("S");
                        break;
                    }
                }
            }
        }

        Type returnType = symbolTable.getReturnType(methodName);

        sb.append(")").append(helpertojas.getString(returnType)).append("\n");

        this.maxStack += methodCall.getNumChildren() - 1;
        if (nn.getKind().equals("Expr")) {
            sb.append("aaaaaaaaaa" + methodName + "\n");
            sb.append("bbbbbbbbbb" + nn.getJmmParent().get("name") + "\n");
            if (methodName.equals(nn.getJmmParent().get("name"))) {
                if (!symbolTable.getReturnType(methodName).getName().equals("void")) {
                    sb.append("pop");
                    String storeInstruction;
                    switch (returnType.getName()) {
                        case "int":
                        case "short":
                        case "boolean":
                        case "byte":
                        case "char":
                            storeInstruction = "istore ";
                            break;
                        case "long":
                            storeInstruction = "lstore ";
                            break;
                        case "float":
                            storeInstruction = "fstore ";
                            break;
                        case "double":
                            storeInstruction = "dstore ";
                            break;
                        default:
                            storeInstruction = "istore ";
                    }
                    int localVarIndex = symbolTable.getAndIncrementLocalVarIndex(returnType);
                    sb.append(storeInstruction).append(localVarIndex).append("\n");
                }
            }
        }
        return null;
    }

    private Void check_null(JmmNode anode){

        if(anode.getAncestor("MethodDecl").isEmpty()){
            sb.append(getRegister(anode.get("value"), anode.getAncestor("MainMethod").get().get("name")));
            sb.append("\n");
        }else{
            sb.append(getRegister(anode.get("value"), anode.getAncestor("MethodDecl").get().get("name")));
            sb.append("\n");
        }
        return null;
    }
    private Void visit_ast_assignment(JmmNode anode, SymbolTableImpl symbolTable) {
        Pair<Type, Boolean> type = check_id(anode, symbolTable);
        Utils utils = new Utils();
        JmmNode right = anode.getJmmChild(0);
        visit(right, symbolTable);
        String varName = anode.get("value");

        //String val0 = right.hasAttribute("value") ? right.get("value") : null;
        if(type.b){
            sb.append("putfield ").append(symbolTable.getClassName()).append("/").append(anode.get("value")).append(" ").append(helpertojas.getString(type.a)).append("\n");
        }else{
            if (type.a.getName().equals("int")) {
                sb.append("istore ");
                check_null(anode);

            }else {
                sb.append("astore_");
                check_null(anode);
                sb.append("aload_");
                check_null(anode);
            }
        }


        return null;
    }
    private Void visit_ast_Initialization(JmmNode ininode, SymbolTableImpl symbolTable) {
        String aininame = ininode.get("name");

        sb.append("new " + aininame + "\n");

        sb.append("dup\n");

        sb.append("invokespecial ").append(aininame).append("/<init>()V\n");



        return null;
    }

    private Void visit_ast_arrayinitialization(JmmNode aininode, SymbolTableImpl symbolTable) {
        Utils utils = new Utils();
        //Type ainitype = utils.getExpressionType(aininode.getJmmChild(0), symbolTable);

        //so testa array de ints
        sb.append("newarray");
        sb.append("I");
        return null;
    }

    /*private Void visit_ast_Boolean(JmmNode bnode, SymbolTableImpl symbolTable) {
        String value = bnode.get("name");

        if(value.equals("true")){
            sb.append("iconst_1\n");
        }else{
            //sb.append("iconst_0\n");
        }

        return null;
    }*/


    //passing to sb the fieldname and fieldtype
    private Void visit_ast_field(JmmNode pnode, SymbolTableImpl symbolTable) {

        Utils utils = new Utils();
        //Type fieldType = utils.getExpressionType(pnode.getJmmChild(0), symbolTable);

        String fieldName = pnode.get("name");

        if(pnode.getJmmParent().getKind().equals("ClassDecl")){
            sb.append(".field ");
            sb.append("public ");
            sb.append(fieldName + " ");


            String val = pnode.getJmmChild(0).getKind();

            switch (val){
                case "Int": {
                    sb.append("I");
                    break;
                }
                case "Boolean": {
                    sb.append("B");
                    break;
                }
                case "String": {
                    sb.append("S");
                    break;
                }
                case "IdType": {
                    sb.append("L" + pnode.getJmmChild(0).get("value"));
                    break;
                }
                case "IntArr": {
                    //sb.append("S");
                    break;
                }
            }
            sb.append("\n");


        }


        return null;
    }

    private Void visit_class(JmmNode classnode, SymbolTableImpl symbolTable) {
        sb.append(".class public ");
        String className = classnode.get("name");
        sb.append(className + "\n");

        var superName = "";

        if (symbolTable.getSuper() != null) {
            sb.append(".super ");
            if(!classnode.get("superName").isEmpty()) {
                superName = classnode.get("superName");
            }
            sb.append(superName + "\n");
        } else {
            sb.append(".super ");
            superName = "java/lang/Object";
            sb.append(superName + "\n\n");
        }

        if(symbolTable.getFields() != null){
            for(JmmNode child : classnode.getChildren()){
                if(child.getKind().equals("VarDecl")){
                    visit(child, symbolTable);
                }
            }
        }

        sb.append(".method public <init>()V\n");
        sb.append("aload_0\n");
        sb.append("invokespecial ").append(superName).append("/<init>()V\n");
        sb.append("return\n");
        sb.append(".end method\n\n");

        for(JmmNode child : classnode.getChildren()){
            visit(child, symbolTable);
        }
        return null;
    }

    /*private static String inttojasmin(int value){
        String instruction;
        if (value >= -1 && value <= 5) {
            instruction = "iconst_" + value + "\n";
        } else if (value >= Byte.MIN_VALUE && value <= Byte.MAX_VALUE) {
            instruction = "bipush " + value + "\n";
        } else if (value >= Short.MIN_VALUE && value <= Short.MAX_VALUE) {
            instruction = "sipush " + value + "\n";
        } else {
            instruction = "ldc " + value + "\n";
        }
        return instruction;
    }*/
    private Void visit_ast_op(JmmNode pnode, SymbolTableImpl symbolTable) {
        Utils utils = new Utils();

        JmmNode child0 = pnode.getJmmChild(0);
        JmmNode child1 = pnode.getJmmChild(1);

        /*String val0 = child0.hasAttribute("value") ? child0.get("value") : null;
        String val1 = child1.hasAttribute("value") ? child1.get("value") : null;*/

        visit(child0, symbolTable);
        visit(child1, symbolTable);

        /*if (val0 != null && utils.isInteger(val0)) {
            sb.append(inttojasmin(Integer.parseInt(val0)));
        }

        if (val1 != null && utils.isInteger(val1)) {
            sb.append(inttojasmin(Integer.parseInt(val1)));
        }*/

        switch (pnode.get("op")){
            case "+": {
                sb.append("iadd\n");
                updateMaxStack(-1);
                break;
            }
            case "-": {
                sb.append("isub\n");
                updateMaxStack(-1);
                break;
            }
            case "*": {
                sb.append("imul\n");
                updateMaxStack(-1);
                break;
            }
            case "/": {
                sb.append("idiv\n");
                updateMaxStack(-1);
                break;
            }
            case "&&": {
                sb.append("iand\n");
                updateMaxStack(-1);
                break;
            }
            case "<": {
                String trueLabel = generateUniqueLabel("true");
                String endLabel = generateUniqueLabel("end");

                sb.append("if_icmplt\n").append(trueLabel).append("\n");
                sb.append("iconst_0\n");
                sb.append("goto ").append(endLabel).append("\n");

                sb.append(trueLabel).append(":\n");
                sb.append("iconst_1\n");

                sb.append(endLabel).append(":\n");
                updateMaxStack(-1);
                break;
            }
        }
        return null;
    }

    private String generateUniqueLabel(String labelPrefix) {
        int currentLabelNumber = labelCounter++;
        return labelPrefix + "_" + currentLabelNumber;
    }


    private Void visitDefault(JmmNode node, SymbolTableImpl symbolTable) {
        node.getChildren().stream().forEach(c -> visit(c, symbolTable));
        return null;
    }

    public String build() {
        return sb.toString();
    }
}
