import java.io.File;
import java.io.FileReader;
import java.util.*;

public class WordAnalyze {
    private String keyWord[] = {"void","stdio","main","int","char","if","else","for","while","scanf","printf","include"};//十二个关键字，种别码为1~12, 13为标识符，14为常数
    private Map<String,Integer> keyMap = new HashMap<>();
    private String opWord[] = {"+","-","*","/","%","=","==","<",">","!=","$","&&","^",";","(",")","{","}","#"};//种别码为15~29，#的种别码为0 共15+1个运算符
    private Map<String,Integer> opMap = new HashMap<>();
    private char ch;
    //保存关键字
    void saveKey(){
        for (int i = 0;i<keyWord.length;i++){
            keyMap.put(keyWord[i],i+1);
        }
    }
    //保存运算符
    void saveOp(){
        for (int i = 0;i<opWord.length;i++){
            if ("#".equals(opWord[i]))
                opMap.put(opWord[i],0);
            else {
                opMap.put(opWord[i],keyMap.size()+2+i+1);
            }
        }
    }
    //判断是否是字母
    boolean isLetter(char letter)
    {
        if((letter >= 'a' && letter <= 'z')||(letter >= 'A' && letter <= 'Z'))
            return true;
        else
            return false;
    }
    //判断是否是数字
    boolean isDigit(char digit)
    {
        if(digit >= '0' && digit <= '9')
            return true;
        else
            return false;
    }
    //判断是否属于双位运算符
    boolean isDoubleOp(String douOp){
        if ("==".equals(douOp)||"!=".equals(douOp)||"&&".equals(douOp))
            return true;
        else
            return false;
    }
    //词法分析
    void analyze(char[] chars)
    {
        saveKey();
        saveOp();
        String arr = "";
        for(int i = 0;i< chars.length;i++) {
            ch = chars[i];
            arr = "";
            if(ch == ' '||ch == '\t'||ch == '\n'||ch == '\r'){}//空格tab换行缩进不做处理
            else if(isLetter(ch)){
                while(isLetter(ch)||isDigit(ch)){
                    arr += ch;
                    ch = chars[++i];
                }
                //回退一个字符
                i--;
                if(keyMap.containsKey(arr)){
                    //关键字
                    System.out.println(arr+"\t"+keyMap.get(arr)+"\t关键字");
                }
                else{
                    //标识符
                    System.out.println(arr+"\t"+(keyMap.size()+1)+"\t标识符");
                }
            }
            else if(isDigit(ch)||(ch == '.'))
            {
                while(isDigit(ch)||(ch == '.'&&isDigit(chars[++i])))
                {
                    if(ch == '.') i--;
                    arr = arr + ch;
                    ch = chars[++i];
                }
                //属于无符号常数
                System.out.println(arr+"\t"+(keyMap.size()+2)+"\t常数");
                i--;
            }
            else {//又不是英文又不是数字，只能是运算符了
                ch = chars[i];
                String s;
                String a="";
                if (i<chars.length-1){
                    a = String.valueOf(ch) + String.valueOf(chars[++i]);}
                if (isDoubleOp(a)){
                    s = a;
                    i++;//若是双位运算符,越格，避免第二位单独输出
                } else
                    s=String.valueOf(ch);
                if (opMap.containsKey(s)) {
                    if (opMap.get(s) >= opMap.get(";") && opMap.get(s) <= opMap.get("}")) {
                        System.out.println(s + "\t" + opMap.get(s) + "\t界符");
                        i--;
                    } else {
                        System.out.println(s + "\t" + opMap.get(s) + "\t运算符");
                        i--;
                    }
                }
                else {
                    if (i==chars.length-1)
                        System.out.println("\n\t" + "\t编译完成");
                    else {
                        System.out.println(s + "\t" + "\t无识别");
                        i--;
                    }
                }
            }
        }
    }
    public static void main(String[] args) throws Exception {
        File file = new File("E:\\data.txt");//定义一个file对象，用来初始化FileReader
        FileReader reader = new FileReader(file);//定义一个fileReader对象，用来初始化BufferedReader
        int length = (int) file.length();
        char buf[] = new char[length+1];//因为可能需要超前读取，定义长度+1防止越界
        reader.read(buf);
        reader.close();
        WordAnalyze wordAnalyze = new WordAnalyze();
        wordAnalyze.analyze(buf);
    }
}