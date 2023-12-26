package pt.up.fe.comp2023.analysis;

import pt.up.fe.comp.jmm.analysis.table.Symbol;
import pt.up.fe.comp.jmm.analysis.table.SymbolTable;
import pt.up.fe.comp.jmm.analysis.table.Type;
import pt.up.fe.comp.jmm.parser.JmmParserResult;
import pt.up.fe.comp2023.visitors.ClassVisitor;
import pt.up.fe.comp2023.visitors.FieldVisitor;
import pt.up.fe.comp2023.visitors.ImportVisitor;
import pt.up.fe.comp2023.visitors.MethodsVisitor;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class SymbolTableImpl implements SymbolTable {

    private JmmParserResult parserResult;

    private List<String> imports;
    private String className;
    private String superName;

    private Map<String, String> classMap;

    private List<Symbol> fields;

    private List<JmmMethod> methods;
    private int nextLocalVarIndex = 0;


    public SymbolTableImpl(JmmParserResult parserResult) {
        this.parserResult = parserResult;
        imports = new ArrayList<>();
        ImportVisitor importVisitor = new ImportVisitor();
        importVisitor.visit(this.parserResult.getRootNode(), imports);

        ClassVisitor classVisitor = new ClassVisitor();
        classMap = new HashMap<>();
        classVisitor.visit(this.parserResult.getRootNode(), classMap);
        className = classMap.get("className");
        superName = classMap.get("superName");

        FieldVisitor fieldVisitor = new FieldVisitor();
        fields = new ArrayList<>();
        fieldVisitor.visit(this.parserResult.getRootNode(), fields);

        MethodsVisitor methodsVisitor = new MethodsVisitor();
        methods = new ArrayList<>();
        methodsVisitor.visit(this.parserResult.getRootNode(), methods);
    }


    @Override
    public List<String> getImports() {
        return imports;
    }

    @Override
    public String getClassName() {
        return className;
    }

    @Override
    public String getSuper() {
        return superName;
    }

    @Override
    public List<Symbol> getFields() {
        return fields;
    }

    @Override
    public List<String> getMethods() {
        List<String> names = new ArrayList<>();
        methods.stream().forEach(m -> names.add(m.getName()));
        return names;
    }

    @Override
    public Type getReturnType(String s) {
        AtomicReference<Type> type = new AtomicReference<>();
        methods.stream().forEach(m -> {
            if(m.getName().equals(s)) {
                if(m.getReturnType() == null) {
                    System.out.println("Return type of the method " + s + " is null");
                } else {
                    type.set(m.getReturnType());
                }
            }
        });
        return type.get();
    }

    @Override
    public List<Symbol> getParameters(String s) {
        AtomicReference<List<Symbol>> parameters = new AtomicReference<>();
        methods.stream().forEach(m -> {
            if(m.getName().equals(s)) {
                parameters.set(m.getParameters());
            }
        });
        return parameters.get();
    }

    @Override
    public List<Symbol> getLocalVariables(String s) {
        AtomicReference<List<Symbol>> localVars = new AtomicReference<>();
        methods.stream().forEach(m -> {
            if(m.getName().equals(s)) {
                localVars.set(m.getLocalVariables());
            }
        });
        return localVars.get();
    }

    public Type getVarType(String varName) {
        for (JmmMethod method : methods) {
            Optional<Symbol> localVar = method.getLocalVariables().stream()
                    .filter(symbol -> symbol.getName().equals(varName))
                    .findFirst();

            if (localVar.isPresent()) {
                return localVar.get().getType();
            }
        }

        Optional<Symbol> field = fields.stream()
                .filter(symbol -> symbol.getName().equals(varName))
                .findFirst();

        if (field.isPresent()) {
            return field.get().getType();
        }

        return null;
    }

    public boolean hasLocalVar(String varName) {
        for (JmmMethod method : methods) {
            Optional<Symbol> localVar = method.getLocalVariables().stream()
                    .filter(symbol -> symbol.getName().equals(varName))
                    .findFirst();

            if (localVar.isPresent()) {
                return true;
            }
        }

        return false;
    }

    public int getLocalVarIndex(String varName) {
        for (JmmMethod method : methods) {
            List<Symbol> localVars = method.getLocalVariables();
            for (int i = 0; i < localVars.size(); i++) {
                if (localVars.get(i).getName().equals(varName)) {
                    return i;
                }
            }
        }

        return -1;
    }

    public boolean hasField(String varName) {
        return fields.stream().anyMatch(symbol -> symbol.getName().equals(varName));
    }

    public Type getFieldType(String varName) {
        Optional<Symbol> field = fields.stream().filter(symbol -> symbol.getName().equals(varName)).findFirst();

        if (field.isPresent()) {
            return field.get().getType();
        }

        return null;
    }

    public boolean containsMethod(String scopeName) {
        return methods.stream().anyMatch(method -> method.getName().equals(scopeName));
    }

    public JmmMethod getMethod(String scopeName) {
        Optional<JmmMethod> methodOpt = methods.stream()
                .filter(method -> method.getName().equals(scopeName))
                .findFirst();

        return methodOpt.orElse(null);
    }
    public int getAndIncrementLocalVarIndex(Type type) {
        int currentIndex = nextLocalVarIndex;
        if (type.getName().equals("long") || type.getName().equals("double")) {
            nextLocalVarIndex += 2;
        } else {
            nextLocalVarIndex += 1;
        }

        return currentIndex;
    }

}
