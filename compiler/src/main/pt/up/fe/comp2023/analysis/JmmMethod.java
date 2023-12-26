package pt.up.fe.comp2023.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pt.up.fe.comp.jmm.analysis.table.Symbol;
import pt.up.fe.comp.jmm.analysis.table.Type;

public class JmmMethod {
    private String name;
    private Type returnType;
    private List<Symbol> parameters;
    private List<Symbol> localVariables;

    public JmmMethod(){
    }

    public JmmMethod(String name, Type returnType, List<Symbol> parameters) {
        this.name = name;
        this.returnType = returnType;
        this.parameters = parameters;
        /*for(Symbol param: parameters){
            localVariables.put(param.getName(), param);
        }*/
    }

    /*public boolean addLocalVariable(Type type, String name)
    {
        if(localVariables.containsKey(name))
            return false;

        localVariables.put(name, new Symbol(type, name));
        return true;
    }*/

    public String getName()
    {
        return name;
    }

    public void setName(String name) {this.name = name;}

    public Type getReturnType()
    {
        return returnType;
    }

    public void setType(Type returnType) {this.returnType = returnType;}

    public List<Symbol> getParameters()
    {
        return parameters;
    }

    public void setParameters(List<Symbol> parameters) {this.parameters = parameters;}

    public List<Symbol> getLocalVariables()
    {
        return localVariables;
    }

    public void setLocalVariables(List<Symbol> localVariables) {this.localVariables = localVariables;}

    /*public Symbol getLocalVariable(String name)
    {
        return localVariables.getOrDefault(name, null);
    }*/

}