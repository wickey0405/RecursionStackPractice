// ここから開発しましょう。
import java.util.HashMap;

class NodeNum{
    public Double data;
    public NodeNum next;

    public NodeNum(Double data){
        this.data = data;
        this.next = null;
    }
}
class StackNum{
    public NodeNum head;

    public StackNum(){
        this.head = null;
    }
    public void push(Double data){
        NodeNum temp = this.head;
        this.head = new NodeNum(data);
        this.head.next = temp;
    }
    public Double pop(){
        if (this.head == null) return null;
        NodeNum temp = this.head;
        this.head = this.head.next;
        return temp.data; 
    }
    public Double peek(){
        if (this.head == null) return null;
        return this.head.data;
    }
}

class NodeOperator{
    public String str;
    public NodeOperator next;
    public Operator opr;
    public Integer priority;

    public NodeOperator(String str){
        this.str = str;
        this.opr = new Operator(this.str);
        this.priority = this.opr.priority;
        this.next = null;
    }
}

class StackOperator{
    public NodeOperator head;

    public StackOperator(){
        this.head = null;
    }
    public void push(String str){
        NodeOperator temp = this.head;
        this.head = new NodeOperator(str);
        this.head.next = temp;
    }
    public String pop(){
        if (this.head == null) return null;
        NodeOperator temp = this.head;
        this.head = this.head.next;
        return temp.str; 
    }
    public String peek(){
        if (this.head == null) return null;
        return this.head.str;
    }
    public Integer peekPriority(){
        return this.head.priority;
    }
}
class Operator{
    public String opr;
    public Integer priority;

    public Operator(String opr){
        this.opr = opr;
        
        if (this.opr.equals(")")) this.priority = 0;
        else if (this.opr.equals("(")) this.priority = 4;
        else if (this.opr.equals("^")) this.priority = 3;
        else if (this.opr.equals("*") || this.opr.equals("/")) this.priority = 2;
        else if (this.opr.equals("+") || this.opr.equals("-")) this.priority = 1;
        else this.priority = -1;
    }
}

class Main{
    public static Double calculate(StackNum si, StackOperator so){
        Double oplnd2 = si.pop();
        Double oplnd1 = si.pop();
        String opr= so.pop();
        // System.out.println(oplnd1+opr+oplnd2);
        if (opr.equals("^")) return Math.pow(oplnd1, oplnd2);
        else if (opr.equals("*")) return oplnd1 * oplnd2;
        else if (opr.equals("/")) return oplnd1 / oplnd2;
        else if (opr.equals("+")) return oplnd1 + oplnd2;
        else if (opr.equals("-")) return oplnd1 - oplnd2;
        else return null;
    }
    public static Double calculationFormula(String formula){
        StackNum stackNum = new StackNum();
        StackNum tempStackNum = new StackNum();
        StackOperator stackOperator = new StackOperator();

        boolean IsOnlyNumberInParenthesis = false;
        int countParenthesis = 0;
        String numberStr = "";
        
        for (int i = 0; i < formula.length(); i++){            
            if (String.valueOf(formula.charAt(i)).matches("[0-9]+") || (stackOperator.peek() == null || stackOperator.peekPriority() == 4) && String.valueOf(formula.charAt(i)).equals("-") || String.valueOf(formula.charAt(i)).equals(".") ){
                numberStr += String.valueOf(formula.charAt(i));
                if (i == formula.length()-1){
                    stackNum.push(Double.valueOf(numberStr));
                }
            } else {
                if (numberStr != ""){
                    stackNum.push(Double.valueOf(numberStr));
                    numberStr = "";
                }                
                if (stackOperator.peek() == null || stackOperator.peekPriority() < new Operator(String.valueOf(formula.charAt(i))).priority || (stackOperator.peekPriority() == 4 && new Operator(String.valueOf(formula.charAt(i))).priority != 0)){
                    stackOperator.push(String.valueOf(formula.charAt(i)));
                    if (stackOperator.peekPriority() == 4) countParenthesis++;
                } else {
                    if (stackOperator.peekPriority() >= new Operator(String.valueOf(formula.charAt(i))).priority && new Operator(String.valueOf(formula.charAt(i))).priority != 0){
                        Double ans = calculate(stackNum, stackOperator);
                        stackNum.push(ans);
                        stackOperator.push(String.valueOf(formula.charAt(i)));
                    } else if (stackOperator.peekPriority() >= new Operator(String.valueOf(formula.charAt(i))).priority && new Operator(String.valueOf(formula.charAt(i))).priority == 0){
                        while (stackOperator.peekPriority() != 4){            
                            Double ans = calculate(stackNum, stackOperator);
                            stackNum.push(ans);
                        }
                        stackOperator.pop();
                    }                        
                }
            }
        }
        // ここまで来るとオペレータは+-なので、順番は気にしない
        while (stackOperator.peek() != null){            
            Double ans = calculate(stackNum, stackOperator);
            stackNum.push(ans);
        }
        return stackNum.peek();
    }
    public static void main(String[] args){
        String formula = "(2^9+1)+(8*9/2*6/3)/(-6+3^2)+(100)+(-8*5-2+5/6)";
        System.out.println(calculationFormula(formula));
        System.out.println((Math.pow(2,9)+1.0)+(8.0*9.0/2.0*6.0/3.0)/(-6.0+Math.pow(3,2))+(100)+(-8.0*5.0-2.0+5.0/6.0));
    }
}