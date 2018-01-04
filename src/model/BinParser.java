package model;

import java.util.LinkedList;

class BinParser {

    private static LinkedList<Character> stack;

    static int countX(char[] input) {
        int rez = 0;
        Boolean exist[] = new Boolean[10];
        for (int i = 0; i < 10; i++) {
            exist[i] = false;
        }
        for (int i = 0; i < input.length - 1; i++) {
            if (input[i] == 'x' && input[i + 1] <= '6' && input[i + 1] >= '1' && (input[i + 1] - '0') < 10 && !exist[input[i + 1] - '0']) {
                exist[input[i + 1] - '0'] = true;
                rez++;
            }
        }
        return rez;
    }

    static BinFunc parse(char[] input) {
        stack = new LinkedList<>();
        return (Boolean[] x) -> {
            Boolean result = countResult(input, x);
            x[x.length - 1] = result;
            return x;
        };
    }

    private static void parseToStack(char[] input, Boolean[] x) throws PillowException {
        for (int i = 0; i < input.length; i++) {
            switch (input[i]) {
                case '!':
                    stack.addLast('!');
                    break;
                case '+':
                    if (stack.isEmpty()) {
                        throw (new PillowException("Impossible starting letter"));
                    }
                    stack.addLast('^');
                    break;
                case '^':
                    if (stack.isEmpty()) {
                        throw (new PillowException("Impossible starting letter"));
                    }
                    stack.addLast('*');
                    break;
                case '|':
                    if (stack.isEmpty()) {
                        throw (new PillowException("Impossible starting letter"));
                    }
                    stack.addLast('+');
                    break;
                case '(':
                    stack.addLast('(');
                    break;
                case ' ':
                    break;
                case ')':
                    if (stack.isEmpty()) {
                        throw (new PillowException("Impossible starting letter"));
                    }
                    stack.addLast(')');
                    break;
                case 'x':
                    if (i < input.length - 1 && input[i + 1] <= '6' && input[i + 1] >= '1') {
                        stack.addLast(toChar(x[input[i + 1] - '0' - 1]));
                        i++;
                    } else {
                        throw (new PillowException("Wrong data in x"));
                    }
                    break;
                default:
                    throw (new PillowException("Unknown symbol found"));
            }
        }
    }

    private static Boolean countResult(char[] input, Boolean[] x) throws PillowException {
        stack.clear();
        parseToStack(input, x);

        //"!x1" -> "x2"
        while (stack.size() > 1) {
            for (int j = 0; j < stack.size(); j++) {
                //избавляемся от возможных "!"
                if (isBoolNumber(stack.get(j)) && j > 0 && stack.get(j - 1) == '!') {
                    stack.remove(j - 1);
                    stack.add(j - 1, (stack.remove(j - 1) == '1') ? '0' : '1');
                }
            }
            //x1*y1 -> x3
            for (int j = 0; j < stack.size(); j++) {
                if (j - 1 > 0 && stack.get(j - 1) == '*' && isBoolNumber(stack.get(j - 2)) && isBoolNumber(stack.get(j))) {
                    stack.remove(j - 1);
                    Boolean rez = and(toBool(stack.remove(j - 1)), toBool(stack.remove(j - 2))); //логическое умножение двух чисел
                    stack.add(j - 2, toChar(rez));
                    j--;
                }
            }
            //x1+y1 -> x3
            for (int j = 0; j < stack.size(); j++) {
                if (j - 1 > 0 && stack.get(j - 1) == '+' && isBoolNumber(stack.get(j - 2)) && isBoolNumber(stack.get(j))) {
                    stack.remove(j - 1);
                    Boolean rez = or(toBool(stack.remove(j - 1)), toBool(stack.remove(j - 2))); //логическое умножение двух чисел
                    stack.add(j - 2, toChar(rez));
                    j--;
                }
            }
            //x1^y1 -> x3
            for (int j = 0; j < stack.size(); j++) {
                if (j - 1 > 0 && stack.get(j - 1) == '^' && isBoolNumber(stack.get(j - 2)) && isBoolNumber(stack.get(j)) && isBoolNumber(stack.get(j))) {
                    stack.remove(j - 1);
                    Boolean rez = xor(toBool(stack.remove(j - 1)), toBool(stack.remove(j - 2))); //логическое умножение двух чисел
                    stack.add(j - 2, toChar(rez));
                    j--;
                }
            }
            //"()" -> ""
            Boolean done = false;
            for (int j = 0; j < stack.size(); j++) {
                if (j - 1 > 0 && stack.get(j - 2) == '(' && stack.get(j) == ')') {
                    if (isBoolNumber(stack.get(j - 1))) {
                        stack.remove(j);
                        stack.remove(j - 2);
                        done = true;
                        j -= 2;
                    } else {
                        throw (new PillowException("Parse failed"));
                    }
                }
            }
            if (!done) {
                break;
            }
        }
        if (stack.size() != 1) {
            throw (new PillowException("Parse failed!"));
        }
        return toBool(stack.get(0));
    }

    private static Boolean isBoolNumber(char s) {
        return s == '1' || s == '0';
    }

    private static Boolean toBool(char c) {
        return c == '1';
    }

    private static char toChar(Boolean c) {
        if (c) {
            return '1';
        }
        return '0';
    }

    private static Boolean and(Boolean a, Boolean b) {
        return (a && b);
    }

    private static Boolean or(Boolean a, Boolean b) {
        return (a || b);
    }

    private static Boolean xor(Boolean a, Boolean b) {
        return (a ^ b);
    }
}
