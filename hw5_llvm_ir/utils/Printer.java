package utils;

import frontend.AST.SyntaxType;
import frontend.lexer.Token;
import frontend.symbol.*;
import llvm_ir.Module;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Printer {
    private static HashMap<Integer, String> errorMsg = new HashMap<>();
    private static boolean isOn = true;
    private static ArrayList<Symbol> symbols = new ArrayList<>();

    public static void printToken(Token token) {
        if (!isOn) {
            return;
        }
        String outputPath = "parser.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath, true))) {
            writer.write(token.getType().toString() + " " + token.getValue() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printSyntaxType(SyntaxType type) {
        if (!isOn) {
            return;
        }
        if (type == SyntaxType.BlockItem || type == SyntaxType.Decl) {
            return;
        }
        String outputPath = "parser.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath, true))) {
            writer.write("<" + type.toString() + ">\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printSymbol(Symbol symbol, int scopeId) {
        String symbolName = symbol.getSymbolName();
        String symbolType;
        if (symbol instanceof ConstSymbol) {
            symbolType = ((ConstSymbol) symbol).getSymbolType().toString();
        } else if (symbol instanceof VarSymbol) {
            symbolType = ((VarSymbol) symbol).getSymbolType().toString();
        } else {
            symbolType = ((FuncSymbol) symbol).getSymbolType().toString();
        }
        String outputPath = "symbol.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath, true))) {
            writer.write(scopeId + " " + symbolName + " " + symbolType + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addPrintSymbol(Symbol symbol, int scopeId) {
        symbol.setScopeId(scopeId);
        symbols.add(symbol);
    }

    public static void printSymbols() {
        symbols.sort(Comparator.comparingInt(Symbol::getScopeId));
        for (Symbol symbol : symbols) {
            Printer.printSymbol(symbol, symbol.getScopeId());
        }
    }

    public static void addError(String type, int lineNum) {
        if (isOn) {
            if (errorMsg.containsKey(lineNum)) {
                return;
            }
            errorMsg.put(lineNum, type);
        }
    }

    public static void printError() {
        if (errorMsg.isEmpty()) {
            return;
        }
        //先对错误信息根据行号进行排序
        Object[] lineNumbers = errorMsg.keySet().toArray();
        Arrays.sort(lineNumbers);
        String outputPath = "error.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath, true))) {
            for (Object lineNum : lineNumbers) {
                writer.write(lineNum + " " + errorMsg.get((Integer) lineNum) + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean noError() {
        return errorMsg.isEmpty();
    }

    public static void setOn() {
        isOn = true;
    }

    public static void setOff() {
        isOn = false;
    }

    public static void printLLVM(Module module) {
        String outputPath = "llvm_ir.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath, false))) {
            writer.write(module.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}