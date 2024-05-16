
import java.io.*;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        TabelaHash tabela = new TabelaHash(30000); // Tamanho da tabela de hash
        Scanner scanner = new Scanner(System.in);
        long startTime, endTime;

        int opcao;
        do {
            System.out.println("\nMenu:");
            System.out.println("1 – Ler Ficheiro");
            System.out.println("2 – Procurar Palavra");
            System.out.println("3 – Inserir Palavra");
            System.out.println("4 – Eliminar Palavra");
            System.out.println("5 – Gravar Ficheiro");
            System.out.println("0 – Sair");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();

            switch (opcao) {
                case 1:
                    startTime = System.nanoTime();
                    lerFicheiro("palavras.txt", tabela);
                    endTime = System.nanoTime();
                    System.out.println("Tempo de inserção: " + (endTime - startTime) + " nanossegundos");
                    break;
                case 2:
                    System.out.print("Digite a palavra a ser procurada: ");
                    String palavraProcurada = scanner.next();
                    startTime = System.nanoTime(); // Iniciar o cronômetro antes da operação de procura
                    Object resultado = tabela.procura(hashing(palavraProcurada), palavraProcurada);
                    endTime = System.nanoTime(); // Parar o cronômetro após a operação de procura
                    if (resultado != null) {
                        System.out.println("Palavra encontrada: " + resultado);
                    } else {
                        System.out.println("Palavra não encontrada.");
                    }
                    System.out.println("Tempo de procura: " + (endTime - startTime) + " nanossegundos"); // Imprimir a duração da operação de procura
                    break;
                case 3:
                    System.out.print("Digite a palavra a ser inserida: ");
                    String novaPalavra = scanner.next();
                    long startTimeInsert = System.nanoTime(); // Iniciar o cronômetro antes da operação de inserção
                    tabela.inserir(hashing(novaPalavra), novaPalavra);
                    long endTimeInsert = System.nanoTime(); // Parar o cronômetro após a operação de inserção
                    System.out.println("Tempo de inserção: " + (endTimeInsert - startTimeInsert) + " nanossegundos"); // Imprimir a duração da operação de inserção
                    break;
                case 4:
                    System.out.print("Digite a palavra a ser removida: ");
                    String palavraRemover = scanner.next();
                    long startTimeRemove = System.nanoTime(); // Iniciar o cronômetro antes da operação de remoção
                    tabela.elimina(hashing(palavraRemover), palavraRemover);
                    long endTimeRemove = System.nanoTime(); // Parar o cronômetro após a operação de remoção
                    System.out.println("Tempo de remoção: " + (endTimeRemove - startTimeRemove) + " nanossegundos"); // Imprimir a duração da operação de remoção
                    break;
                case 5:
                    gravarFicheiro("palavras.txt", tabela);
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
        scanner.close();
    }

        /*static long hashing(String str) {
            long resultado = 0;
            for (int i = 0; i < str.length(); i++)
                resultado = resultado * 16 + str.charAt(i);
            return Math.abs(resultado) % 31; // Aplicar o método numérico
        }*/

    static long hashing(String str) {
        long hash = 5381;
        for (int i = 0; i < str.length(); i++) {
            hash = ((hash << 5) + hash) + str.charAt(i); /* hash * 33 + c */
        }
        return Math.abs(hash) % 31; // Aplicar o método numérico
    }

    static int lerFicheiro(String nomefich, TabelaHash tabela) {
        int nPalavras = 0;
        File file = new File(nomefich);
        Scanner leitor = null;
        try {
            leitor = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.out.println("Ficheiro não encontrado");
            e.printStackTrace();
        }
        while (leitor.hasNextLine()) {
            String tmp = leitor.nextLine();
            tabela.inserir(hashing(tmp), tmp);
            nPalavras++;
        }
        leitor.close();
        return nPalavras;
    }

    private static void gravarFicheiro(String nomeFicheiro, TabelaHash tabela) {
        try {
            PrintWriter ficheiro = new PrintWriter(new FileWriter(nomeFicheiro, false));
            for (ListaLigada lista : tabela.aTabela) {
                imprimir(ficheiro, lista.inicio);
            }
            ficheiro.close();
        } catch (IOException e) {
            System.out.println("Erro ao gravar no ficheiro");
            e.printStackTrace();
        }
    }

    public static void imprimir(PrintWriter ficheiro, ListaLigada.No current) {
        while (current != null) {
            ficheiro.println(current.valor);
            current = current.proximo;
        }
    }


    static class TabelaHash {
        ListaLigada[] aTabela;

        public TabelaHash(int nPos) {
            aTabela = new ListaLigada[nPos];
            for (int i = 0; i < nPos; i++) {
                aTabela[i] = new ListaLigada();
            }
        }

        boolean inserir(long chave, Object oElem) {
            aTabela[(int) chave].adicionar(oElem);
            return true;
        }

        Object procura(long chave, Object oElem) {
            return aTabela[(int) chave].procurar(oElem);
        }

        boolean elimina(long chave, Object oElem) {
            return aTabela[(int) chave].remover(oElem);
        }
    }

    static class ListaLigada {
        No inicio;

        private static class No {
            Object valor;
            No proximo;

            public No(Object valor) {
                this.valor = valor;
                this.proximo = null;
            }
        }

        public void adicionar(Object valor) {
            if (inicio == null) {
                inicio = new No(valor);
            } else {
                No temp = inicio;
                while (temp.proximo != null) {
                    temp = temp.proximo;
                }
                temp.proximo = new No(valor);
            }
        }

        public Object procurar(Object valor) {
            No temp = inicio;
            while (temp != null) {
                if (temp.valor.equals(valor)) {
                    return temp.valor;
                }
                temp = temp.proximo;
            }
            return null;
        }

        public boolean remover(Object valor) {
            if (inicio == null) {
                return false;
            }
            if (inicio.valor.equals(valor)) {
                inicio = inicio.proximo;
                return true;
            }
            No temp = inicio;
            while (temp.proximo != null) {
                if (temp.proximo.valor.equals(valor)) {
                    temp.proximo = temp.proximo.proximo;
                    return true;
                }
                temp = temp.proximo;
            }
            return false;
        }
    }
}