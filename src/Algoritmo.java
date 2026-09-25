import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Algoritmo {

    static String[] HEURISTICAS = {"Manhattan", "Euclidiana", "Chebyshev"};

    ArrayList<No> caminho = new ArrayList<>();
    HashMap<No, Double> g = new HashMap<>();
    ArrayList<Aresta> testadas = new ArrayList<>();
    HashSet<No> fechados = new HashSet<>();
    StringBuilder log = new StringBuilder();

    static double h(No n, No fim, int tipo) {
        int dx = Math.abs(n.x - fim.x);
        int dy = Math.abs(n.y - fim.y);
        if (tipo == 0) {
            return dx + dy;
        } else if (tipo == 1) {
            return Math.sqrt(dx * dx + dy * dy);
        } else {
            return Math.max(dx, dy);
        }
    }

    public void buscar(Grafo grafo, No inicio, No fim, int heur) {
        ArrayList<No> aberta = new ArrayList<>();
        HashMap<No, No> pai = new HashMap<>();

        g.put(inicio, 0.0);
        aberta.add(inicio);
        int it = 0;

        while (!aberta.isEmpty()) {
            // procura o de menor f na lista aberta (se empatar fica o de menor h)
            No atual = aberta.get(0);
            for (No n : aberta) {
                double f = g.get(n) + h(n, fim, heur);
                double fAtual = g.get(atual) + h(atual, fim, heur);
                if (f < fAtual || (f == fAtual && h(n, fim, heur) < h(atual, fim, heur))) {
                    atual = n;
                }
            }
            aberta.remove(atual);
            fechados.add(atual);
            it++;

            double hAtual = h(atual, fim, heur);
            log.append("\n" + it + ") expande " + atual + "   g=" + num(g.get(atual)) + "  h=" + num(hAtual)
                    + "  f=" + num(g.get(atual) + hAtual) + "\n");

            if (atual == fim) {
                log.append("   chegou no destino\n");
                break;
            }

            for (Aresta a : grafo.adj.get(atual)) {
                testadas.add(a);
                No viz = a.para;
                if (fechados.contains(viz)) {
                    continue;
                }
                double novoG = g.get(atual) + a.custo;
                if (!g.containsKey(viz) || novoG < g.get(viz)) {
                    g.put(viz, novoG);
                    pai.put(viz, atual);
                    if (!aberta.contains(viz)) {
                        aberta.add(viz);
                    }
                    log.append("   " + atual + "->" + viz + "  g=" + num(novoG) + "  f=" + num(novoG + h(viz, fim, heur)) + "\n");
                }
            }

            String s = "";
            for (No n : aberta) {
                s += n + "(" + num(g.get(n) + h(n, fim, heur)) + ") ";
            }
            log.append("   aberta: " + s + "\n");
        }

        if (fechados.contains(fim)) {
            No n = fim;
            while (n != null) {
                caminho.add(0, n);
                n = pai.get(n);
            }
        } else {
            log.append("\nnao tem caminho!\n");
        }
    }

    // A -> B(1) -> C(4) ...
    public String trajeto() {
        if (caminho.isEmpty()) return "sem caminho";
        String s = caminho.get(0).nome;
        for (int i = 1; i < caminho.size(); i++) {
            No n = caminho.get(i);
            s += " → " + n.nome + "(" + num(g.get(n)) + ")";
        }
        return s;
    }

    static String num(double d) {
        if (d == (int) d) return "" + (int) d;
        return String.format("%.2f", d);
    }
}
