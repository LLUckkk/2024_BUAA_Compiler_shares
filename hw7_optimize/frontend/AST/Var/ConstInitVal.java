package frontend.AST.Var;

import frontend.AST.Exp.ConstExp;
import frontend.AST.Node;
import frontend.AST.SyntaxType;
import frontend.AST.TokenNode;

import java.util.ArrayList;
import java.util.HashMap;

public class ConstInitVal extends Node {
    public ConstInitVal(ArrayList<Node> children, SyntaxType type) {
        super(children, type);
    }

    //ConstInitVal → ConstExp | '{' [ ConstExp { ',' ConstExp } ] '}' |
    //StringConst
    public ArrayList<Integer> executeForConst(int eleNum) {
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
                //StringConst
                String str = ((TokenNode) children.get(0)).getToken().getValue();
                str = str.substring(1, str.length() - 1);
                for (int i = 0; i < str.length(); i++) {
                    if (str.charAt(i) == '\\') {
                        if (i + 1 < str.length() && special.containsKey(str.charAt(i + 1))) {
                            res.add(special.get(str.charAt(i + 1)));
                            i++;
                        } else {
                            res.add((int) str.charAt(i));
                        }
                    } else {
                        res.add((int) str.charAt(i));
                    }
                }
                if (res.size() < eleNum) {
                    int left = eleNum - res.size();
                    for (int i = 0; i < left; i++) {
                        res.add(0);
                    }
                }
            } else {
                //ConstExp
                res.add(children.get(0).execute());
            }
        } else {
            for (Node child : children) {
                if (child instanceof ConstExp) {
                    res.add(child.execute());
                }
            }
            if (res.size() < eleNum) {
                int left = eleNum - res.size();
                for (int i = 0; i < left; i++) {
                    res.add(0);
                }
            }
        }
        return res;
    }
}
