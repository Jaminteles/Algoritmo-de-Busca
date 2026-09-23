import java.awt.*;
import javax.swing.*;

public class Main extends JFrame {

    Grafo grafo = new Grafo();
    PainelMapa mapa;
    JComboBox<String> comboHeur = new JComboBox<>(AStar.HEURISTICAS);
    JLabel lblOrigem = new JLabel("Origem: -");
    JLabel lblDestino = new JLabel("Destino: -");
    JLabel lblRota = new JLabel(" ");
    JTextArea txt = new JTextArea();

    No origem, destino;

    public Main() {
        setTitle("A* - Mapa da cidade");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        mapa = new PainelMapa(grafo, this);
        add(mapa, BorderLayout.CENTER);

        lblRota.setFont(new Font("Arial", Font.BOLD, 16));
        lblRota.setForeground(Color.BLUE);
        lblRota.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        add(lblRota, BorderLayout.NORTH);

        JPanel lado = new JPanel(new BorderLayout());
        lado.setPreferredSize(new Dimension(420, 0));

        JPanel topo = new JPanel(new GridLayout(4, 1, 5, 5));
        topo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p1.add(new JLabel("Heuristica:"));
        p1.add(comboHeur);
        topo.add(p1);
        topo.add(lblOrigem);
        topo.add(lblDestino);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnBuscar = new JButton("Buscar");
        JButton btnLimpar = new JButton("Limpar");
        botoes.add(btnBuscar);
        botoes.add(btnLimpar);
        topo.add(botoes);
        lado.add(topo, BorderLayout.NORTH);

        txt.setEditable(false);
        txt.setFont(new Font("Monospaced", Font.PLAIN, 12));
        txt.setText("Clique no ponto de origem e depois no destino");
        lado.add(new JScrollPane(txt), BorderLayout.CENTER);
        add(lado, BorderLayout.EAST);

        btnBuscar.addActionListener(e -> buscar());
        btnLimpar.addActionListener(e -> limpar());
        comboHeur.addActionListener(e -> buscar());

        pack();
        setLocationRelativeTo(null);
    }

    public void clicou(No n) {
        if (origem == null || destino != null) {
            // comeca de novo
            origem = n;
            destino = null;
            mapa.resultado = null;
            lblRota.setText(" ");
            txt.setText("Origem: " + n + "\nAgora clique no destino");
        } else if (n != origem) {
            destino = n;
        }
        lblOrigem.setText("Origem: " + (origem == null ? "-" : origem.nome));
        lblDestino.setText("Destino: " + (destino == null ? "-" : destino.nome));
        mapa.origem = origem;
        mapa.destino = destino;
        mapa.repaint();

        if (destino != null) buscar();
    }

    void buscar() {
        if (origem == null || destino == null) return;

        AStar a = new AStar();
        a.buscar(grafo, origem, destino, comboHeur.getSelectedIndex());
        mapa.resultado = a;
        mapa.repaint();
        lblRota.setText(a.trajeto());

        String s = "Heuristica: " + comboHeur.getSelectedItem() + "\n";
        s += "De " + origem + " para " + destino + "\n\n";
        s += "Trajeto: " + a.trajeto() + "\n";
        if (!a.caminho.isEmpty())
            s += "Distancia total: " + AStar.num(a.g.get(destino)) + " U\n";
        s += "Nos expandidos: " + a.fechados.size() + "\n";
        s += "\n--- passos ---\n" + a.log;
        txt.setText(s);
        txt.setCaretPosition(0);
    }

    void limpar() {
        origem = null;
        destino = null;
        mapa.origem = null;
        mapa.destino = null;
        mapa.resultado = null;
        mapa.repaint();
        lblOrigem.setText("Origem: -");
        lblDestino.setText("Destino: -");
        lblRota.setText(" ");
        txt.setText("Clique no ponto de origem e depois no destino");
    }

    public static void main(String[] args) {
        new Main().setVisible(true);
    }
}
