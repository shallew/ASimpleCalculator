package com.example.asimplecalculator;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Stack;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private static boolean isResult;


    private Button b11;
    private Button b12;
    private Button b13;
    private Button b14;
    private Button b21;
    private Button b22;
    private Button b23;
    private Button b24;
    private Button b31;
    private Button b32;
    private Button b33;
    private Button b34;
    private Button b41;
    private Button b42;
    private Button b43;
    private Button b44;
    private Button b51;
    private Button b52;
    private Button b53;
    private Button b54;
    private EditText editText;
    private TextView tv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        editText = findViewById(R.id.edit_text);
        b11 = findViewById(R.id.b11_button);
        b11.setOnClickListener(this);
        b12 = findViewById(R.id.b12_button);
        b12.setOnClickListener(this);
        b13 = findViewById(R.id.b13_button);
        b13.setOnClickListener(this);
        b14 = findViewById(R.id.b14_button);
        b14.setOnClickListener(this);
        b21 = findViewById(R.id.b21_button);
        b22 = findViewById(R.id.b22_button);
        b23 = findViewById(R.id.b23_button);
        b24 = findViewById(R.id.b24_button);
        b31 = findViewById(R.id.b31_button);
        b32 = findViewById(R.id.b32_button);
        b33 = findViewById(R.id.b33_button);
        b34 = findViewById(R.id.b34_button);
        b41 = findViewById(R.id.b41_button);
        b42 = findViewById(R.id.b42_button);
        b43 = findViewById(R.id.b43_button);
        b44 = findViewById(R.id.b44_button);
        b51 = findViewById(R.id.b51_button);
        b52 = findViewById(R.id.b52_button);
        b53 = findViewById(R.id.b53_button);
        b54 = findViewById(R.id.b54_button);

        b21.setOnClickListener(this);
        b22.setOnClickListener(this);
        b23.setOnClickListener(this);
        b24.setOnClickListener(this);
        b31.setOnClickListener(this);
        b32.setOnClickListener(this);
        b33.setOnClickListener(this);
        b34.setOnClickListener(this);
        b41.setOnClickListener(this);
        b42.setOnClickListener(this);
        b43.setOnClickListener(this);
        b44.setOnClickListener(this);
        b51.setOnClickListener(this);
        b52.setOnClickListener(this);
        b53.setOnClickListener(this);
        b54.setOnClickListener(this);

        tv2 = findViewById(R.id.tv2);
        editText.setFocusable(false);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.b51_button) {
            //C清空
            editText.setText("");
            tv2.setText("");
            isResult = false;
        } else if (view.getId() == R.id.b14_button) {
            //DEL删除
            if (isResult) {
                editText.setText("");
                tv2.setText("");
            }
            String text = editText.getText().toString();
            if (text.equals("ERROR")) {
                editText.setText("");
                tv2.setText("");
            } else if (!text.equals("")) {
                editText.setText(text.substring(0, text.length() - 1));
                editText.setSelection(editText.getText().length());
            }
            isResult = false;
        } else if (view.getId() == R.id.b54_button) {
            //=等于

            if (editText.getText().toString().equals("ERROR")) {
                editText.setText("");
                tv2.setText("");
            }
            if (!editText.getText().toString().equals("")) {
                calculate(editText.getText().toString());
            }
            if (!editText.getText().toString().equals("ERROR")) {
                //计算结果不是ERROR就一定是一个数字
                isResult = true;
            }
        } else {
            Button bTem = findViewById(view.getId());
            if (editText.getText().toString().equals("ERROR")) {
                editText.setText("");
                tv2.setText("");
            }
            if (isResult && !isOperator(bTem.getText().charAt(0)) && bTem.getText().charAt(0) != '.') {
                tv2.setText(editText.getText().toString());
                editText.setText("");
            }
            char bText = bTem.getText().toString().charAt(0);
            String eText = editText.getText().toString();
            char last;
            StringBuilder text = new StringBuilder(editText.getText().toString());
            if (Character.isDigit(bText)){
                if(eText.length() > 0 && eText.charAt(eText.length() - 1) == ')'){
                    text.append("*");
                }
            }
            if (bText == '('){
                if (eText.length() > 0 && (Character.isDigit(eText.charAt(eText.length() - 1)) || eText.charAt(eText.length() - 1) == ')')){
                    text.append("*");
                }
            }

            editText.setText(text.append(bTem.getText()).toString());
            editText.setSelection(editText.getText().length());

            isResult = false;
        }
    }

    @SuppressLint("SetTextI18n")
    private void calculate(String str) {
        List<String> list = new ArrayList<>();
        list = transform(str);
        if (list == null) {
            editText.setText("ERROR");
            return;
        }
        Stack<BigDecimal> stack = new Stack<>();
        for (int i = 0; i < list.size(); i++) {
            if ((list.get(i).charAt(0) >= '0' && list.get(i).charAt(0) <= '9') || list.get(i).charAt(0) == '-' && list.get(i).length() > 1) {
                //如果是数字，直接入栈
                //一个字符串的第一个字符是数字  就直接将这个字符串转为数字入栈
                stack.push(new BigDecimal(list.get(i)));
            } else if (list.get(i).equals("+") || list.get(i).equals("-") || list.get(i).equals("*") || list.get(i).equals("/")) {
                //如果是运算符
                BigDecimal num1 = stack.pop();//后入栈，右
                BigDecimal num2 = stack.pop();//先入栈，左
                switch (list.get(i)) {
                    case "-":
                        num2 = num2.subtract(num1);
                        stack.push(num2);
                        break;
                    case "+":
                        num2 = num2.add(num1);
                        stack.push(num2);
                        break;
                    case "*":
                        num2 = num2.multiply(num1);
                        stack.push(num2);
                        break;
                    case "/":
                        if(num1.equals(BigDecimal.ZERO)){
                            Log.d("num1", "calculate: " + num1);
                            editText.setText("ERROR");
                            return;
                        }
                        num2 = num2.divide(num1, 13, RoundingMode.HALF_UP);
                        stack.push(num2);
                        break;
                }
            } else {
                editText.setText("ERROR");
                return;
                //过滤非法字符
            }
        }
        tv2.setText(editText.getText().toString());
        editText.setText("" + stack.pop().stripTrailingZeros());
        editText.setSelection(editText.getText().length());
    }

    @SuppressLint("SetTextI18n")
    private List<String> transform(String s) {
        Stack<Character> s1 = new Stack<>();
        Stack<String> s2 = new Stack<>();
        List<String> ls = new ArrayList<>();
        List<String> list = new ArrayList<>();


        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(') {
                s1.push(s.charAt(i));
            }
            if (s.charAt(i) == ')') {
                if (s1.isEmpty()) {
                    return null;
                }
                s1.pop();
            }
        }
        if (!s1.isEmpty()) {
            return null;
        }//判断括号是否匹配

        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '.') {
                if (i + 1 < s.length() && s.charAt(i) == '.' && s.charAt(i + 1) == '(') {
                    return null;
                }
                //如果是小数点
                if (i == 0 || (i > 0 && (s.charAt(i - 1) == '(' || isOperator(s.charAt(i - 1))))) {
                    //首位小数点
                    StringBuilder ret = new StringBuilder();
                    while (i + 1 < s.length() && Character.isDigit(s.charAt(i + 1))) {
                        ret.append(s.charAt(i + 1));
                        i++;
                    }
                    s2.push("0." + ret.toString());
                    continue;
                }
                boolean lF = false;
                boolean rF = false;
                int l = i - 1;
                int r = i + 1;
                while (l >= 0 || r < s.length()) {
                    if ((l >= 0 && s.charAt(l) == '.') || (r < s.length() && s.charAt(r) == '.')) {
                        return null;
                    }
                    if (isOperator(s.charAt(l))) {
                        lF = true;//已经遇到运算符，不用在继续
                    }
                    if (isOperator(s.charAt(r))) {
                        rF = true;//已经遇到运算符，不用在继续
                    }
                    if (lF == true && rF == true) {
                        break;
                    }
                    if (l != 0) {
                        l--;
                    }
                    if (r != s.length() - 1) {
                        r++;
                    }
                }
            }
            if (s.charAt(i) >= '0' && s.charAt(i) <= '9') {
                //如果是数字可以直接入栈
                boolean flag = false;
                StringBuilder ret = new StringBuilder();
                while (i < s.length() && (s.charAt(i) >= '0' && s.charAt(i) <= '9' || (s.charAt(i) == '.' && !flag))) {
                    if (s.charAt(i) == '.') {
                        ret.append('.');
                        i++;
                        flag = true;
                        continue;
                    }
                    ret.append(s.charAt(i) - '0');
                    i++;
                }//拼接多位数
                s2.push(ret.toString());//"" + s.charAt(i)可以将整数转化为字符串
                i -= 1;
            } else if ((s.charAt(i) == '-' && i == 0) || i > 0 && s.charAt(i) == '-' && (s.charAt(i - 1) == '(' || isOperator(s.charAt(i - 1)))) {
                i++;
                //把减号当作负号
                StringBuilder ret = new StringBuilder();
                while (i < s.length() && (s.charAt(i) >= '0' && s.charAt(i) <= '9' || s.charAt(i) == '.')) {
                    if (s.charAt(i) == '.') {
                        ret.append('.');
                        i++;
                        continue;
                    }
                    ret.append(s.charAt(i) - '0');
                    i++;
                }
                if (!"".equals(ret.toString())) {
                    s2.push("-" + ret);
                }
                i--;
            } else if (s.charAt(i) == '(' || s.charAt(i) == ')') {
                if ((i + 1 < s.length() && s.charAt(i) == '(' && s.charAt(i + 1) == ')') || (i + 1 < s.length() && s.charAt(i) == ')' && s.charAt(i + 1) == '(')) {
                    return null;
                }
                if (i + 1 < s.length() && s.charAt(i) == ')' && s.charAt(i + 1) == '.') {
                    return null;
                }

                if (s.charAt(i) == '(') {
                    //如果是左括号 直接入栈
                    s1.push(s.charAt(i));
                } else {
                    //如果是右括号
                    while (s1.peek() != '(') {
                        //将s1栈顶元素依次弹出并压入s2，直到遇到左括号 '('
                        s2.push(s1.pop() + "");
                    }
                    s1.pop();//将'('舍去
                }
            } else if (isOperator(s.charAt(i))) {
                if (i == 0 || i == s.length() - 1) {
                    //首位末尾不能是操作符
                    return null;
                }
                if ((isOperator(s.charAt(i - 1)) && s.charAt(i - 1) != '-') || (isOperator(s.charAt(i + 1)) && s.charAt(i + 1) != '-')) {
                    //操作符不能相连
                    return null;
                }
                if (s.charAt(i - 1) == '(' || s.charAt(i + 1) == ')') {
                    //操作符不能在括号的首尾
                    return null;
                }
                //如果是运算符
                if (s1.size() == 0 || s1.peek() == '(') {
                    //当栈为空，或者栈顶元素为左括号’(‘
                    s1.push(s.charAt(i));
                } else if (s1.size() != 0 && compare(s1.peek()) >= compare(s.charAt(i))) {
                    //栈不为空，该运算符优先级小于等于栈顶优先级,依次弹出并压入s2
                    while (s1.size() != 0 && compare(s1.peek()) >= compare(s.charAt(i))) {
                        s2.push(s1.pop() + "");
                    }
                    //然后将该运算符入栈s1
                    s1.push(s.charAt(i));
                } else {
                    //栈不为空，且该运算符的优先级大于栈顶运算符的优先级  ，直接压入栈s1
                    s1.push(s.charAt(i));
                }
            } else {
                return null;
                //过滤非法字符
            }
        }
        while (s1.size() != 0) {
            s2.push(s1.pop() + "");
        }//将s1栈中剩下的元素全部弹出，并压入栈s2
        //将s2中的元素弹出后反转；该操作在战中比较麻烦，但用队列和ArrayList较为简单

        while (s2.size() != 0) {
            ls.add(s2.pop());
        }
        for (int i = ls.size() - 1; i >= 0; i--) {
            list.add(ls.get(i));
        }
        if (list.size() == 0) {
            return null;
        }
        return list;
    }

    //此方法判断运算符的优先级大小关系
    public int compare(char oper1) {
        switch (oper1) {
            case '+':
                return 1;
            case '-':
                return 1;
            case '*':
                return 2;
            case '/':
                return 2;
        }
        return 0;
    }

    //判断是操作符
    public boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }
}