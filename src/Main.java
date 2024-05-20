import java.io.*;
import java.util.Scanner;


public class HashMap {
    public static void main(String[] args) {
        TabelaHash tabela = new TabelaHash(30000); // Tamanho da tabela de hash
        Scanner scanner = new Scanner(System.in);

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
                    long inicio = System.nanoTime();
                    lerFicheiro("C:\\Users\\diogo\\eclipse-workspace\\AEDTP2\\src\\palavras.txt", tabela);
                    long fim = System.nanoTime();
                    System.out.println("Tempo de inserção: " + (fim - inicio) + " nanossegundos");
                    break;
                case 2:
                    System.out.print("Digite a palavra a ser procurada: ");
                    String palavraProcurada = scanner.next();
                    long inicio2 = System.nanoTime();
                    Object resultado = tabela.procura(hashing(palavraProcurada), palavraProcurada);
                    long fim2 = System.nanoTime();
                    if (resultado != null) {
                        System.out.println("Palavra encontrada: " + resultado);
                    } else {
                        System.out.println("Palavra não encontrada.");
                    }
                    System.out.println("Tempo de procura: " + (fim2 - inicio2) + " nanossegundos"); // Imprimir a duração da operação de procura
                    break;
                case 3:
                    System.out.print("Digite a palavra a ser inserida: ");
                    String novaPalavra = scanner.next();
                    long inicio3 = System.nanoTime();
                    tabela.inserir(hashing(novaPalavra), novaPalavra);
                    long fim3 = System.nanoTime();
                    System.out.println("Tempo de inserção: " + (fim3 - inicio3) + " nanossegundos"); // Imprimir a duração da operação de inserção
                    break;
                case 4:
                    System.out.print("Digite a palavra a ser removida: ");
                    String palavraRemover = scanner.next();
                    long inicio4 = System.nanoTime(); // Iniciar o cronômetro antes da operação de remoção
                    tabela.elimina(hashing(palavraRemover), palavraRemover);
                    long fim4 = System.nanoTime(); // Parar o cronômetro após a operação de remoção
                    System.out.println("Tempo de remoção: " + (fim4 - inicio4) + " nanossegundos"); // Imprimir a duração da operação de remoção
                    break;
                case 5:
                    long inicio5 = System.nanoTime(); // Iniciar o cronômetro antes da operação de gravação
                    gravarFicheiro("palavras.txt", tabela);
                    long fim5 = System.nanoTime(); // Parar o cronômetro após a operação de gravação
                    System.out.println("Tempo de gravação: " + (fim5 - inicio5) + " nanossegundos"); // Imprimir a duração da operação de gravação
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

    // Método de hashing original
        /*static long hashing(String str) {
            long resultado = 0;
            for (int i = 0; i < str.length(); i++)
                resultado = resultado * 16 + str.charAt(i);
            return Math.abs(resultado) % 31; // Aplicar o método numérico
        }*/

    // Método de hashing optimizado
    static long hashing(String str) {
        // Este método implementa uma função de hashing para strings.
        // Inicializa o valor do hash como 5381, um número primo.
        long hash = 5381;
        // Itera sobre cada caractere da string.
        for (int i = 0; i < str.length(); i++) {
            // Atualiza o valor do hash multiplicando o valor atual por 33 (equivalente a um shift left de 5 bits e uma adição)
            // e adicionando o valor ASCII do caractere atual.
            hash = ((hash << 5) + hash) + str.charAt(i);
        }
        // Retorna o valor absoluto do hash módulo 31.
        // O uso do módulo garante que o valor do hash caia dentro de um intervalo específico (neste caso, de 0 a 30).
        return Math.abs(hash) % 31;
    }

    static int lerFicheiro(String nomefich, TabelaHash tabela) {
        // Este método lê um ficheiro e insere cada linha na tabela de hash.
        int nPalavras = 0;
        // Contador para o número de palavras inseridas na tabela de hash.
        File file = new File(nomefich);
        // Cria um objeto File para o ficheiro a ser lido.
        Scanner leitor = null;
        // Inicializa um objeto Scanner para ler o ficheiro.
        try {
            leitor = new Scanner(file);
            // Tenta abrir o ficheiro.
        } catch (FileNotFoundException e) {
            System.out.println("Ficheiro não encontrado");
            // Imprime uma mensagem de erro se o ficheiro não for encontrado.
            e.printStackTrace();
            // Imprime a pilha de chamadas do erro.
        }
        while (leitor.hasNextLine()) { // Enquanto houver mais linhas no ficheiro...
            String tmp = leitor.nextLine(); // Lê a próxima linha.
            tabela.inserir(hashing(tmp), tmp); // Insere a linha na tabela de hash.
            nPalavras++; // Incrementa o contador de palavras.
        }
        leitor.close();
        return nPalavras;
        // Retorna o número de palavras inseridas na tabela de hash.
    }

    private static void gravarFicheiro(String nomeFicheiro, TabelaHash tabela) {
        try {
            PrintWriter ficheiro = new PrintWriter(new FileWriter(nomeFicheiro, false));
            // Cria um objeto PrintWriter para escrever no ficheiro.
            // O segundo argumento como false indica que o ficheiro será sobrescrito se já existir.
            // Itera sobre cada lista ligada na tabela de hash.
            for (ListaLigada lista : tabela.aTabela) {
                // Chama o método imprimir para escrever os valores da lista ligada no ficheiro.
                imprimir(ficheiro, lista.inicio);
            }
            ficheiro.close();
        } catch (IOException e) {
            System.out.println("Erro ao gravar no ficheiro"); // Imprime uma mensagem de erro se ocorrer um erro de I/O.
            e.printStackTrace(); // Imprime a pilha de chamadas do erro.
        }
    }

    public static void imprimir(PrintWriter ficheiro, ListaLigada.No current) {
        while (current != null) {
            ficheiro.println(current.valor); // Imprime o valor do nó atual
            current = current.proximo; // Passa para o próximo nó
        }
    }


    static class TabelaHash {
        ListaLigada[] aTabela;
        // Array de listas ligadas que representa a tabela de hash

        public TabelaHash(int nPos) {
            // Construtor da classe TabelaHash.
            // Recebe um inteiro que representa o número de posições na tabela de hash.
            aTabela = new ListaLigada[nPos];
            for (int i = 0; i < nPos; i++) {
                aTabela[i] = new ListaLigada();
            }
        }

        boolean inserir(long chave, Object oElem) {
            // Este método insere um elemento na tabela de hash.
            aTabela[(int) chave].adicionar(oElem);
            // Retorna true para indicar que a inserção foi bem-sucedida.
            return true;
        }

        Object procura(long chave, Object oElem) {
            // Este método procura um elemento na tabela de hash.
            return aTabela[(int) chave].procurar(oElem);
            // Retorna o objeto se ele for encontrado, ou null se não for.
        }

        boolean elimina(long chave, Object oElem) {
            // Este método remove um elemento da tabela de hash.
            return aTabela[(int) chave].remover(oElem);
            // Retorna true se o objeto foi removido com sucesso, ou false se o objeto não foi encontrado.
        }
    }

    static class ListaLigada {
        No inicio;
        // O início da lista ligada

        private static class No {
            Object valor; // O valor armazenado neste nó
            No proximo; // O próximo nó na lista ligada

            public No(Object valor) {
                this.valor = valor; // Inicializa o valor deste nó
                this.proximo = null; // Inicialmente, este nó não tem um próximo nó
            }
        }

        public void adicionar(Object valor) {
            // Se a lista estiver vazia, o novo nó se torna o início da lista
            if (inicio == null) {
                inicio = new No(valor);
            } else {
                // Se a lista não estiver vazia, percorre a lista até o último nó
                No temp = inicio;
                while (temp.proximo != null) {
                    temp = temp.proximo;
                }
                // Adiciona o novo nó no final da lista
                temp.proximo = new No(valor);
            }
        }

        public Object procurar(Object valor) {
            // Começa a procurar a partir do início da lista
            No temp = inicio;
            while (temp != null) {
                // Se o valor do nó atual é igual ao valor procurado, retorna o valor
                if (temp.valor.equals(valor)) {
                    return temp.valor;
                }
                // Se não, passa para o próximo nó
                temp = temp.proximo;
            }
            // Se o valor não foi encontrado em nenhum nó, retorna null
            return null;
        }

        public boolean remover(Object valor) {
            // Verifica se a lista está vazia. Se estiver, retorna false, pois não há nada para remover.
            if (inicio == null) {
                return false;
            }
            // Verifica se o valor do primeiro nó é igual ao valor que queremos remover.
            // Se for, faz o início apontar para o próximo nó, efetivamente removendo o primeiro nó.
            if (inicio.valor.equals(valor)) {
                inicio = inicio.proximo;
                return true;
                // Retorna true, indicando que o valor foi removido com sucesso.
            }
            // Se o valor não foi encontrado no primeiro nó, começa a procurar a partir do segundo nó.
            No temp = inicio;
            while (temp.proximo != null) {
                // Verifica se o valor do próximo nó é igual ao valor que queremos remover.
                if (temp.proximo.valor.equals(valor)) {
                    // Se for, faz o nó atual apontar para o nó após o próximo, efetivamente removendo o próximo nó.
                    temp.proximo = temp.proximo.proximo;
                    return true;
                    // Retorna true, indicando que o valor foi removido com sucesso.
                }
                // Se o valor não foi encontrado no nó atual, passa para o próximo nó.
                temp = temp.proximo;
            }
            // Se o valor não foi encontrado em nenhum nó, retorna false.
            return false;
        }
    }
}
