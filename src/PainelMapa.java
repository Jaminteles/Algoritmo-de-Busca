import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

public class PainelMapa extends JPanel {

    int TAM = 85;   // tamanho do quadrado
    int MARGEM = 50;

    Grafo grafo;
    Main tela;
    No origem, destino;
    Algoritmo resultado;

    public PainelMapa(Grafo grafo, Main tela) {
        this.grafo = grafo;
        this.tela = tela;
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(7 * TAM + 2 * MARGEM, 5 * TAM + 2 * MARGEM));

        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                for (No n : grafo.nos.values()) {
                    int dx = e.getX() - px(n);
                    int dy = e.getY() - py(n);
                    if (dx * dx + dy * dy < 17 * 17) {
                        tela.clicou(n);
                        break;
                    }
                }
            }
        });
    }

    int px(No n) {
        return MARGEM + n.x * TAM;
    }

    int py(No n) {
        return MARGEM + n.y * TAM;
    }

    void linha(Graphics2D g, No a, No b) {
        g.drawLine(px(a), py(a), px(b), py(b));
    }

    protected void paintComponent(Graphics g1) {
        super.paintComponent(g1);
        Graphics2D g = (Graphics2D) g1;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // grade cinza
        g.setColor(new Color(215, 215, 215));
        for (int i = 0; i <= 7; i++)
            g.drawLine(MARGEM + i * TAM, MARGEM, MARGEM + i * TAM, MARGEM + 5 * TAM);
        for (int i = 0; i <= 5; i++)
            g.drawLine(MARGEM, MARGEM + i * TAM, MARGEM + 7 * TAM, MARGEM + i * TAM);

        // ruas
        g.setColor(Color.RED);
        g.setStroke(new BasicStroke(2));
        for (Aresta a : grafo.ruas) {
            linha(g, a.de, a.para);
        }

        if (resultado != null) {
            g.setStroke(new BasicStroke(7, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g.setColor(new Color(255, 205, 0));
            for (Aresta a : resultado.testadas) {
                linha(g, a.de, a.para);
            }
            // o azul por cima do amarelo
            g.setColor(Color.BLUE);
            for (int i = 1; i < resultado.caminho.size(); i++) {
                linha(g, resultado.caminho.get(i - 1), resultado.caminho.get(i));
            }
        }

        // setinhas nas ruas de mao unica
        g.setColor(Color.BLACK);
        for (Aresta a : grafo.ruas) {
            if (a.dupla) continue;
            int mx = (px(a.de) + px(a.para)) / 2;
            int my = (py(a.de) + py(a.para)) / 2;
            double ang = Math.atan2(py(a.para) - py(a.de), px(a.para) - px(a.de));
            int[] xs = new int[3];
            int[] ys = new int[3];
            xs[0] = (int) (mx + 9 * Math.cos(ang));
            ys[0] = (int) (my + 9 * Math.sin(ang));
            xs[1] = (int) (mx + 9 * Math.cos(ang + 2.5));
            ys[1] = (int) (my + 9 * Math.sin(ang + 2.5));
            xs[2] = (int) (mx + 9 * Math.cos(ang - 2.5));
            ys[2] = (int) (my + 9 * Math.sin(ang - 2.5));
            g.fillPolygon(xs, ys, 3);
        }

        // pontos
        g.setStroke(new BasicStroke(2));
        g.setFont(new Font("Arial", Font.BOLD, 14));
        for (No n : grafo.nos.values()) {
            int x = px(n), y = py(n);
            boolean noCaminho = resultado != null && resultado.caminho.contains(n);

            if (n == origem) g.setColor(new Color(0, 160, 50));
            else if (n == destino) g.setColor(new Color(210, 0, 0));
            else if (noCaminho) g.setColor(new Color(190, 210, 255));
            else g.setColor(Color.WHITE);
            g.fillOval(x - 12, y - 12, 24, 24);
            g.setColor(Color.DARK_GRAY);
            g.drawOval(x - 12, y - 12, 24, 24);

            if (n == origem || n == destino) g.setColor(Color.WHITE);
            else g.setColor(new Color(40, 40, 140));
            g.drawString(n.nome, x - 5, y + 5);

            if (noCaminho) {
                g.setFont(new Font("Arial", Font.PLAIN, 11));
                g.setColor(Color.BLUE);
                g.drawString("" + Algoritmo.num(resultado.g.get(n)), x + 14, y - 12);
                g.setFont(new Font("Arial", Font.BOLD, 14));
            }
        }
    }
}
