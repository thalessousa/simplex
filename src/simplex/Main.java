package simplex;

import java.util.Scanner;

public class Main {
    private static double[][] matriz;
    private static double[] resultados;
    private static int[] vb;

    public static void main(String[] args) {
        int numeroVar, numeroRes;

        // Le dados basicos
        Scanner scanner = new Scanner(System.in);
        System.out.println("Sao quantas variaveis de decisao?");
        numeroVar = scanner.nextInt();
        System.out.println("Sao quantas restricoes?");
        numeroRes = scanner.nextInt();

        // Monta matriz
        matriz = new double[numeroRes + 1][numeroVar + numeroRes];
        resultados = new double[numeroRes + 1];
        vb = new int[numeroRes + 1];

        // Le equacao Z
        System.out.println("Equacao de maximizacao:");
        for (int j = 0; j < numeroVar; j++) {
            System.out.println("Qual o coeficiente de X" + (j + 1) + "?");
            matriz[0][j] = scanner.nextDouble() * -1;
        }

        // Le restricoes
        for (int i = 1; i <= numeroRes; i++) {
            vb[i] = i + numeroVar;
            System.out.println("Restricao " + i + ":");
            for (int j = 0; j < numeroVar; j++) {
                System.out.println("Qual o coeficiente de X" + (j + 1) + "?");
                matriz[i][j] = scanner.nextDouble();
            }
            matriz[i][numeroVar + i - 1] = 1;
            System.out.println("Qual o valor da desigualdade?");
            resultados[i] = scanner.nextDouble();
        }

        // Efetua calculos
        boolean continua = true;
        while (continua) {
            double valor = 0;

            // Escolhe a coluna certa
            int coluna = -1;
            for (int j = 0; j < matriz[0].length; j++) {
                if (matriz[0][j] < valor) {
                    valor = matriz[0][j];
                    coluna = j;
                }
            }

            // Escolhe a linha certa
            valor = 0;
            int linha = -1;
            for (int i = 1; i <= numeroRes; i++) {
                if ((matriz[i][coluna] / resultados[i]) > valor) {
                    valor = matriz[i][coluna] / resultados[i];
                    linha = i;
                }
            }

            vb[linha] = coluna + 1;

            // Normaliza a linha
            for (int j = 0; j < matriz[linha].length; j++){
                matriz[linha][j] = matriz[linha][j] / matriz[linha][coluna];
            }
            resultados[linha] = resultados[linha] / matriz[linha][coluna];

            // Calcula outras linhas
            for (int i = 0; i < matriz.length; i++) {
                if (i != linha) {
                    valor = matriz[i][coluna] * -1;
                    for (int j = 0; j < matriz[i].length; j++) {
                        matriz[i][j] = matriz[i][j] + (matriz[linha][j] * valor);
                    }
                    resultados[i] = resultados[i] + (resultados[linha] * valor);
                }
            }

            // Verifica condicao de parada
            continua = false;
            for (double n : matriz[0]) {
                if (n < 0) {
                    continua = true;
                    break;
                }
            }
        }

        // Mostra resultado
        System.out.println("Resultado:");
        System.out.println(String.format("Max Z = %.2f", resultados[0]));
        for (int i = 0; i < numeroVar; i++) {
            int linha = 0;
            for (int l = 1; l < vb.length; l++) {
                if (vb[l] == i + 1) {
                    linha = l;
                    break;
                }
            }
            System.out.print("X" + (i + 1) + " = ");
            if (linha > 0) {
                System.out.print(resultados[linha] + "\n");
            } else {
                System.out.print(0 + "\n");
            }
        }
    }
}