package frontend.AST.Var;

import frontend.AST.Exp.Exp;
import frontend.AST.Node;
import frontend.AST.SyntaxType;
import frontend.AST.TokenNode;
import llvm_ir.Constant;
import llvm_ir.Value;

import java.util.ArrayList;
import java.util.HashMap;

public class InitVal extends Node {
    //InitVal → Exp | '{' [ Exp { ',' Exp } ] '}' | StringConst
    public InitVal(ArrayList<Node> children, SyntaxType type) {
        super(children, type);
    }

    public Value genIR() {
        //对于非数组来说
        return children.get(0).genIR();
    }

    public ArrayList<Value> genIRValList(int elemNum, boolean isCharArray) {
        //针对数组
        ArrayList<Value> res = new ArrayList<>();
        for (Node child : children) {
            if (child instanceof Exp) {
                res.add(child.genIR());
            }
        }
        if (children.size() == 1 && children.get(0) instanceof TokenNode) {
            String str = ((TokenNode) children.get(0)).getToken().getValue();
            str = str.substring(1, str.length() - 1);
            for (int i = 0; i < str.length(); i++) {
                res.add(new Constant("\"" + str.charAt(i) + "\""));
            }
        }
        if (res.size() < elemNum) {
            int left = elemNum - res.size();
            for (int i = 0; i < left; i++) {
                if (isCharArray) {
                    res.add(new Constant("\"\0\""));
                } else {
                    res.add(new Constant(0));
                }

            }
        }
        return res;
    }

    public ArrayList<Integer> executeForCharArray(int elemNum) {
        HashMap<Character, Integer> special = new HashMap<>();
        special.put('n', 10);
        special.put('t', 9);
        special.put('a', 7);
        special.put('b', 8);
        special.put('v', 11);
        special.put('f', 12);
        special.put('\"', 34);
        special.put('\'', 39);
        special.put('\\', 92);
        special.put('0', 0);

        ArrayList<Integer> res = new ArrayList<>();
        if (children.size() == 1) {
            if (children.get(0) instanceof TokenNode) {
                TokenNode string = (TokenNode) children.get(0);
                String str = string.getToken().getValue();
                str = str.substring(1, str.length() - 1);
                for (int i = 0; i < str.length(); i++) {
                    if (str.charAt(i) == '\\') {
                        if (i + 1 < str.length() && special.containsKey(str.charAt(i + 1))) {
                            res.add(special.get(str.charAt(i + 1)));
                            i++;
                        }
                    } else {
                        res.add((int) str.charAt(i));
                    }
                }
            } else {
                res.add(children.get(0).execute());
            }
        } else {
            for (Node child : children) {
                if (child instanceof Exp) {
                    res.add(child.execute());
                }
            }
        }
        if (res.size() < elemNum) {
            int left = elemNum - res.size();
            for (int i = 0; i < left; i++) {
                res.add(0);
            }
        }
        return res;
    }

    public ArrayList<Integer> executeForIntArray(int elemNum) {
        ArrayList<Integer> res = new ArrayList<>();
        for (Node child : children) {
            if (child instanceof Exp) {
                res.add(child.execute());
            }
        }
        if (res.size() < elemNum) {
            int left = elemNum - res.size();
            for (int i = 0; i < left; i++) {
                res.add(0);
            }
        }
        return res;
    } //如果只有一个值的话也放在数组里就行
    //以上execute针对于全局变量中可以计算的地方,比如，维度和初始值
}
