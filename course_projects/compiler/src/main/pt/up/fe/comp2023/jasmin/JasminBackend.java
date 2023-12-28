package pt.up.fe.comp2023.jasmin;


import pt.up.fe.comp.jmm.analysis.JmmSemanticsResult;
import pt.up.fe.comp.jmm.analysis.table.SymbolTable;
import pt.up.fe.comp.jmm.ast2jasmin.AstToJasmin;
import pt.up.fe.comp.jmm.jasmin.JasminResult;
import pt.up.fe.comp2023.analysis.SymbolTableImpl;
import pt.up.fe.comp2023.visitors.jasminvisitors.astvisitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class JasminBackend implements AstToJasmin {

    public JasminResult toJasmin(JmmSemanticsResult astresult){
        Map<String, Map<String, Integer>> sav = new HashMap<>();

        SymbolTableImpl symbolTable = (SymbolTableImpl) astresult.getSymbolTable();
        for ( var meth : symbolTable.getMethods()){
            int c = 0;

            Map<String, Integer> nsav = new HashMap<>();
            sav.put(meth, nsav);


            c = 1;
            for (var param : symbolTable.getParameters(meth)){
                nsav.put(param.getName(), c);
                c++;
            }
            for (var lcl : symbolTable.getLocalVariables(meth)){
                nsav.put(lcl.getName(), c);
                c++;
            }
            if(!meth.equals("main")){
                nsav.put("this", 0);
            }
        }
        System.out.println(sav);

        astvisitor astvisit = new astvisitor(sav);
        astvisit.visit(astresult.getRootNode(), symbolTable);String jasminCode = astvisit.getSB().toString();
        System.out.println(jasminCode); // Print the Jasmin code

        System.out.println("HEREEE");
        return new JasminResult(jasminCode, astresult.getConfig());
    }
}
