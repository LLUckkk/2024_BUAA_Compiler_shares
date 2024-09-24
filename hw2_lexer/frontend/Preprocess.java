package frontend;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Preprocess {
    private ArrayList<String> lines; //将处理过的每一行程序储存在arraylist中

    public Preprocess() {
        lines = new ArrayList<>();
    }

    public ArrayList<String> getLines() {
        readLines();
//        for (String line : lines) {
//            System.out.println(line);
//        }
        return lines;
    }

    //此处的读入和识别应该去掉所有注释行
    public void readLines() {
        String path = "testfile.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            boolean flag = false; //标记是否处于多行注释
            while ((line = reader.readLine()) != null) {
                //识别是否有无多行注释，如果有则一直跳过
                if (!flag) { //之前没有多行注释，需要判断该句的成分
                    if (line.contains("/*")) {
                        flag = !line.contains("*/");
                        if(!flag){ //同行之间注释已经结束
                            line = removeDoubleComment(line);
                            if(!isBlank(line)){ //去掉空行
                                lines.add(line);
                            }
                        } else { //处理/*在句子末尾的情况
                            line = removeOnceComment(line);
                            if(!isBlank(line)){
                                lines.add(line);
                            }
                        }
                    } else {
                        if (line.contains("//")) {
                            line = removeSingleComment(line);
                        }
                        if (!isBlank(line)) { //去掉空行
                            lines.add(line);
                        }
                    }
                } else if (line.contains("*/")) {
                    flag = false;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String removeSingleComment(String line) {
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == '/' && line.charAt(i + 1) == '/') {
                line = line.substring(0, i);
                break;
            }
        }
        return line;
    }

    public String removeDoubleComment(String line) {
        int commentStart = line.indexOf("/*");
        int commentEnd = line.indexOf("*/");
        return line.substring(0, commentStart) + line.substring(commentEnd + 2);
    }

    public String removeOnceComment(String line) {
        int commentStart = line.indexOf("/*");
        return line.substring(0, commentStart);
    }

    public boolean isBlank(String line) {
        return line.trim().isEmpty();
    }
}
