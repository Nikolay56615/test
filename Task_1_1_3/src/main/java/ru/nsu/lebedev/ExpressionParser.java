package ru.nsu.lebedev;

import java.util.Stack;

/**
 * An auxiliary class for processing expressions.
 */
class ExpressionParser {

    /**
     * A method for parsing a string and creating an expression.
     */
    public static Expression parse(String input) {
        Stack<Expression> operands = new Stack<>();
        Stack<Character> operators = new Stack<>();

        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);
            if (Character.isWhitespace(ch)) {
                continue;
            }
            if (Character.isDigit(ch)) {
                int num = 0;
                while (i < input.length() && Character.isDigit(input.charAt(i))) {
                    num = num * 10 + Character.getNumericValue(input.charAt(i));
                    i++;
                }
                i--;
                operands.push(new Number(num));
            } else if (Character.isLetter(ch)) {
                StringBuilder variableName = new StringBuilder();
                while (i < input.length() && Character.isLetter(input.charAt(i))) {
                    variableName.append(input.charAt(i));
                    i++;
                }
                i--;
                operands.push(new Variable(variableName.toString()));
            } else if (ch == '(') {
                operators.push(ch);
            } else if (ch == ')') {
                while (!operators.empty() && operators.peek() != '(') {
                    processOperator(operands, operators.pop());
                }
                operators.pop();
            } else if (isOperator(ch)) {
                while (!operators.empty() && precedence(operators.peek()) >= precedence(ch)) {
                    processOperator(operands, operators.pop());
                }
                operators.push(ch);
            }
        }

        while (!operators.empty()) {
            processOperator(operands, operators.pop());
        }

        return operands.pop(); // Возвращаем итоговое выражение
    }

    /**
     * A method for processing an operator and creating an appropriate expression.
     */
    private static void processOperator(Stack<Expression> operands, char operator) {
        Expression right = operands.pop();
        Expression left = operands.pop();
        switch (operator) {
            case '+':
                operands.push(new Add(left, right));
                break;
            case '-':
                operands.push(new Sub(left, right));
                break;
            case '*':
                operands.push(new Mul(left, right));
                break;
            case '/':
                operands.push(new Div(left, right));
                break;
            default:
                break;
        }
    }

    /**
     * A method for checking whether a character is an operator.
     */
    private static boolean isOperator(char ch) {
        return ch == '+' || ch == '-' || ch == '*' || ch == '/';
    }

    /**
     * Method for determining operator priority.
     */
    private static int precedence(char operator) {
        switch (operator) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            default:
                return 0;
        }
    }
}
