/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
/**
 *
 * @author Emesis
 */
public class Calcu {

    private static final Map<String, Integer> precedencia = new HashMap<>();
    static {
        precedencia.put("+", 1); 
        precedencia.put("-", 1);
        precedencia.put("*", 2);
        precedencia.put("/", 2);
        precedencia.put("%", 2);
        precedencia.put("mod", 2);
        precedencia.put("^", 3);
    }

    public static String evaluar(String expresion) {
        try {
            if (expresion == null || expresion.trim().isEmpty()) return "0";

            // Normalizar
            String expr = expresion.replace(',', '.');
            expr = expr.replaceAll("\\s+", " "); // Normalizar espacios
            expr = infixToRPN(expr);
            double resultado = evaluarRPN(expr);
            
            // Formatear resultado
            if (resultado == (long) resultado) {
                return String.valueOf((long) resultado);
            } else {
                return String.format("%.8f", resultado).replaceAll("0*$", "").replaceAll("\\.$", "");
            }
        } catch (Exception e) {
            return "Error";
        }
    }

    // Convierte infijo a notación polaca inversa (RPN)
    private static String infixToRPN(String expr) {
        StringBuilder salida = new StringBuilder();
        Deque<String> operadores = new ArrayDeque<>();
        String[] tokens = tokenize(expr);

        for (String token : tokens) {
            if (esNumero(token)) {
                salida.append(token).append(" ");
            } else if (token.equals("(")) {
                operadores.push(token);
            } else if (token.equals(")")) {
                while (!operadores.isEmpty() && !operadores.peek().equals("(")) {
                    salida.append(operadores.pop()).append(" ");
                }
                operadores.pop(); // quitar "("
            } else if (precedencia.containsKey(token)) {
                while (!operadores.isEmpty() &&
                       !operadores.peek().equals("(") &&
                       precedencia.get(operadores.peek()) >= precedencia.get(token)) {
                    salida.append(operadores.pop()).append(" ");
                }
                operadores.push(token);
            }
        }
        while (!operadores.isEmpty()) {
            salida.append(operadores.pop()).append(" ");
        }
        return salida.toString();
    }

    // Evalúa la expresión en RPN
    private static double evaluarRPN(String rpn) {
        Deque<Double> pila = new ArrayDeque<>();
        String[] tokens = rpn.split("\\s+");

        for (String token : tokens) {
            if (token.isEmpty()) continue;
            if (esNumero(token)) {
                pila.push(Double.parseDouble(token));
            } else {
                double b = pila.pop();
                double a = pila.isEmpty() ? 0 : pila.pop(); // manejo de unario
                double resultado = 0;

                switch (token) {
                    case "+": resultado = a + b; break;
                    case "-": resultado = a - b; break;
                    case "*": resultado = a * b; break;
                    case "/": 
                        if (b == 0) throw new ArithmeticException("División por cero");
                        resultado = a / b; 
                        break;
                    case "^": resultado = Math.pow(a, b); break;
                    case "%": 
                        if (rpn.contains("mod")) {
                            resultado = a % b;
                        } else {
                            // Caso: 10% → 0.1, o 50%100 → 50
                            // Aquí interpretamos % como módulo (coherente)
                            resultado = a % b;
                        }
                        break;
                    case "mod":
                        if (b == 0) throw new ArithmeticException("Mod por cero");
                        resultado = a % b;
                        break;
                    default:
                        throw new IllegalArgumentException("Operador desconocido: " + token);
                }
                pila.push(resultado);
            }
        }
        return pila.isEmpty() ? 0 : pila.pop();
    }

    // Tokeniza la expresión: separa números, operadores, paréntesis
    private static String[] tokenize(String expr) {
        expr = expr.replaceAll("mod", " mod ");
        expr = expr.replaceAll("([()])", " $1 ");
        expr = expr.replaceAll("([+\\-*/^%])", " $1 ");
        expr = expr.replaceAll("\\s+", " ").trim();
        return expr.split("\\s+");
    }

    private static boolean esNumero(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
