import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Grafo {
    HashMap<String, No> nos = new LinkedHashMap<>();
    HashMap<No, ArrayList<Aresta>> adj = new HashMap<>();
    ArrayList<Aresta> ruas = new ArrayList<>(); // pro desenho

    public Grafo() {
        // x, y na grade (cada quadrado = 1U)
        addNo("A", 0, 0);
        addNo("B", 1, 0);
        addNo("C", 4, 0);
        addNo("D", 5, 0);
        addNo("E", 7, 0);
        addNo("F", 0, 2);
        addNo("G", 1, 2);
        addNo("H", 4, 2);
        addNo("I", 7, 2);
        addNo("J", 1, 3);
        addNo("K", 3, 3);
        addNo("L", 4, 3);
        addNo("M", 5, 3);
        addNo("N", 7, 3);
        addNo("O", 4, 4);
        addNo("P", 7, 4);
        addNo("Q", 0, 5);
        addNo("R", 1, 5);
        addNo("S", 3, 5);
        addNo("T", 4, 5);
        addNo("U", 7, 5);

        // mao dupla
        rua("A", "B", true);
        rua("B", "C", true);
        rua("C", "D", true);
        rua("D", "E", true);
        rua("A", "F", true);
        rua("F", "Q", true);
        rua("F", "G", true);
        rua("G", "H", true);
        rua("D", "M", true);
        rua("K", "S", true);
        rua("T", "U", true);

        // mao unica (de -> para)
        rua("B", "G", false);
        rua("G", "J", false);
        rua("J", "R", false);
        rua("T", "O", false);
        rua("O", "L", false);
        rua("L", "H", false);
        rua("H", "C", false);
        rua("E", "I", false);
        rua("I", "N", false);
        rua("N", "P", false);
        rua("P", "U", false);
        rua("N", "M", false);
        rua("M", "L", false);
        rua("L", "K", false);
        rua("K", "J", false);
        rua("P", "O", false);
        rua("Q", "R", false);
        rua("R", "S", false);
        rua("S", "T", false);
    }

    void addNo(String nome, int x, int y) {
        No n = new No(nome, x, y);
        nos.put(nome, n);
        adj.put(n, new ArrayList<>());
    }

    void rua(String a, String b, boolean dupla) {
        No n1 = nos.get(a);
        No n2 = nos.get(b);
        Aresta ar = new Aresta(n1, n2, dupla);
        adj.get(n1).add(ar);
        ruas.add(ar);
        if (dupla) {
            adj.get(n2).add(new Aresta(n2, n1, true));
        }
    }

    No get(String nome) {
        return nos.get(nome);
    }
}
