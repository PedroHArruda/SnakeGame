package snakegame;

import java.awt.Color;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class Tabuleiro extends JFrame {

    private JPanel painel; // Painel
    private JPanel menu; // Menu do jogo
    private JButton resetButton; // Botão de reset 
    private JButton pauseButton; // Botão de pause
    private JTextField placarField; // Placar de Pontos
    private int x, y; // Coordenadas X e Y
    private String direcao = "parado"; // Direção atual da cobra
    private long tempoAtualizacao = 100; // Tempo de Atualização dos Frames
    private int incremento = 20; // Incremento
    private Quadrado maca, macaEnvenenada; // Quadrado referente a maçã e maçã envenenada
    private Snake cobra; // Instância da classe Snake
    private boolean jogoAtivo; // Variável de controle para o estado do jogo
    private int larguraTabuleiro, alturaTabuleiro; // Altura e Largura do Tabuleiro
    private int placar = 0; // Variável referente ao placar do jogo
    private Random random = new Random(); // Intância randômica 
    private int modoDeJogo; // Variável de controle para o modo de Jogo
    TelaInicial telaInicial = new TelaInicial(); //Instancia um objeto da classe TelaInicial

    public int getModoDeJogo() {
        return modoDeJogo;
    }

    public void setModoDeJogo(int modoDeJogo) {
        this.modoDeJogo = modoDeJogo;
    }

    public Tabuleiro() {

        cobra = new Snake();

        larguraTabuleiro = alturaTabuleiro = 420;

        Color corMaca = new Color(150, 2, 0);
        Color corMacaEnvenenada = new Color(121, 30, 148);

        maca = new Quadrado(20, 20, corMaca);
        macaEnvenenada = new Quadrado(20, 20, corMacaEnvenenada);

        setTitle("Jogo da Cobrinha");
        setSize(alturaTabuleiro, larguraTabuleiro + 30);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        menu = new JPanel();
        menu.setLayout(new FlowLayout());

        //Estilização dos botões e placar
        resetButton = new JButton("Reiniciar");
        pauseButton = new JButton("Pausar");
        placarField = new JTextField("Placar: 0");
        placarField.setEditable(false);

        menu.add(resetButton);
        menu.add(placarField);
        menu.add(pauseButton);

        Color menuBackground = new Color(116, 229, 78);
        Color buttonBackground = new Color(48, 242, 242);

        menu.setBackground(menuBackground);
        pauseButton.setBackground(buttonBackground);

        Font f = new Font("Poppins", 10, 18);
        placarField.setFont(f);
        resetButton.setFont(f);
        pauseButton.setFont(f);
        resetButton.setBackground(buttonBackground);
        placarField.setBackground(menuBackground);
        placarField.setBorder(null);

        painel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {

                super.paintComponent(g);

                // Dimensões de cada quadrado no tabuleiro
                int quadradoLargura = 20;
                int quadradoAltura = 20;

                // Cores para o padrão quadriculado
                Color cor1 = new Color(51, 204, 0);
                Color cor2 = new Color(116, 229, 78);

                // Desenha o fundo quadriculado
                for (int y = 0; y < alturaTabuleiro; y += quadradoAltura) {
                    for (int x = 0; x < larguraTabuleiro; x += quadradoLargura) {
                        // Alterna entre as cores para cada célula
                        if ((x / quadradoLargura + y / quadradoAltura) % 2 == 0) {
                            g.setColor(cor1);
                        } else {
                            g.setColor(cor2);
                        }
                        g.fillRect(x, y, quadradoLargura, quadradoAltura);
                    }
                }

                // Desenha cada nó do corpo da cobra
                Node nodeCorpo = cobra.getCorpo().getFirstNode(); // Acessa o primeiro nó da cobra
                while (nodeCorpo != null) {
                    Quadrado quadrado = nodeCorpo.getQuadrado(); // Obtém o quadrado de cada segmento
                    g.setColor(quadrado.cor); // Acessa a cor do quadrado
                    g.fillRect(quadrado.x, quadrado.y, quadrado.largura, quadrado.altura);
                    nodeCorpo = nodeCorpo.getNextNode(); // Avança para o próximo nó
                }

                // Desenha a maçã
                g.setColor(maca.cor);
                g.fillOval(maca.x, maca.y, maca.largura, maca.altura);
                //Desenha a maçã envenenada 
                g.setColor(macaEnvenenada.cor);
                g.fillOval(macaEnvenenada.x, macaEnvenenada.y, macaEnvenenada.largura, macaEnvenenada.altura);

            }
        };
        add(menu, BorderLayout.NORTH);
        add(painel, BorderLayout.CENTER);

        setVisible(true);

        resetButton.addActionListener(e -> {
            Reiniciar();

        });

        // ActionListener para o botão Pausar
        pauseButton.addActionListener(e -> {
            Pausar();

        });

        gerarMaca();
        gerarMacaEnvenenada();

        painel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {

                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        if (!direcao.equals("direita")) {
                            direcao = "esquerda";
                        }
                        break;
                    case KeyEvent.VK_RIGHT:
                        if (!direcao.equals("esquerda")) {
                            direcao = "direita";
                        }
                        break;
                    case KeyEvent.VK_UP:
                        if (!direcao.equals("baixo")) {
                            direcao = "cima";
                        }
                        break;
                    case KeyEvent.VK_DOWN:
                        if (!direcao.equals("cima")) {
                            direcao = "baixo";
                        }
                        break;

                }
            }
        });

        painel.setFocusable(true);
        painel.requestFocusInWindow();

    }

    public void verificarColisoes() {
        switch (this.modoDeJogo) {
            case 0:
                colisaoBorda();
                break;
            case 1:
                colisaoBordaOposta();
                break;
        }
        if (cobra.detectarColisaoComCorpo()) {
            encerrarJogo("Colisão com o próprio corpo!");
            jogoAtivo = false;

        }
        colisaoMaca();
        colisaoMacaEnvenenada();

    }

    public void colisaoBorda() {
        //Método responsável para verificar as colisões com as bordas da janela e com o corpo da cobra
        if (!jogoAtivo) {
            return;
        }

        // Obtém a cabeça da cobra
        Node cabeca = cobra.getCorpo().getLastNode();

        Quadrado quadradoCabeca = cabeca.getQuadrado();

        // Verifica colisão com a borda da janela
        if (quadradoCabeca.x < 0 || quadradoCabeca.x >= larguraTabuleiro
                || quadradoCabeca.y < 0 || quadradoCabeca.y >= alturaTabuleiro) {

            encerrarJogo("Colisão com a borda! Jogo encerrado.");
            jogoAtivo = false;

        }

    }

    public void colisaoBordaOposta() {
        //Método para quando a cobra colidir com a borda ela aparece do outro lado 
        Node cabeca = cobra.getCorpo().getLastNode();
        Quadrado quadradoCabeca = cabeca.getQuadrado();
        Quadrado novoQuadrado = cabeca.getQuadrado();

        if (quadradoCabeca.x < 0) {
            novoQuadrado.x = larguraTabuleiro - 20;
            novoQuadrado.y = quadradoCabeca.y;
            cabeca.setQuadrado(novoQuadrado);
            cobra.mover("esquerda");
        }
        if (quadradoCabeca.x > larguraTabuleiro) {
            novoQuadrado.x = 20;
            novoQuadrado.y = quadradoCabeca.y;
            cabeca.setQuadrado(novoQuadrado);
            cobra.mover("direita");
        }
        if (quadradoCabeca.y > alturaTabuleiro) {
            novoQuadrado.x = quadradoCabeca.x;
            novoQuadrado.y = 20;
            cabeca.setQuadrado(novoQuadrado);
            cobra.mover("baixo");
        }
        if (quadradoCabeca.y < 0) {
            novoQuadrado.x = 20;
            novoQuadrado.y = alturaTabuleiro - 20;
            cabeca.setQuadrado(novoQuadrado);
            cobra.mover("cima");
        }

    }

    //Método para gerar a maçã aleatoriamente
    private void gerarMaca() {
        int maxPosicaoX = (350 - maca.largura) / maca.largura;
        int maxPosicaoY = (350 - maca.altura) / maca.altura;

        int posicaoX, posicaoY;

        posicaoX = random.nextInt(maxPosicaoX) * maca.largura;
        posicaoY = random.nextInt(maxPosicaoY) * maca.altura;

        maca.x = posicaoX;
        maca.y = posicaoY;
    }

    // Método de encerrar o jogo e exibir uma mensagem de Game Over
    private void encerrarJogo(String mensagem) {

        jogoAtivo = false; // Interrompe o loop principal do jogo
        JOptionPane.showMessageDialog(this, mensagem, "Game Over", JOptionPane.OK_OPTION);
        this.setVisible(false);
        telaInicial.setVisible(true);

    }

    //Método para gerar a maçã envenenada aleatoriamente
    private void gerarMacaEnvenenada() {
        int maxPosicaoX = (350 - macaEnvenenada.largura) / macaEnvenenada.largura;
        int maxPosicaoY = (350 - macaEnvenenada.altura) / macaEnvenenada.altura;

        int posicaoX, posicaoY;

        posicaoX = random.nextInt(maxPosicaoX) * macaEnvenenada.largura;
        posicaoY = random.nextInt(maxPosicaoY) * macaEnvenenada.altura;

        macaEnvenenada.x = posicaoX;
        macaEnvenenada.y = posicaoY;
    }

    // Método para resetar a cobra
    private void resetarCobra() {

        cobra = new Snake();
        direcao = "parado";
        placar = 0;
        placarField.setText("Placar: " + placar);
        gerarMaca();
        this.tempoAtualizacao = 100;
        definirDificuldade(TelaInicial.dificuldade);
        Iniciar();

    }

    public void definirDificuldade(int dificuldade) {
        //Método responsável por definir a dificuldade do jogo. 
        //O método subtrai o valor da dificuldade do tempo de atualização. 
        tempoAtualizacao -= dificuldade;
    }

    public void colisaoMaca() {
        Quadrado cabecaCobra = cobra.getCorpo().getLastNode().getQuadrado();
        //Estrutura de condicional responsável pela colisão da cobra com a maçã
        if (cabecaCobra.x == maca.x && cabecaCobra.y == maca.y) {
            cobra.crescer(direcao);
            gerarMaca();
            gerarMacaEnvenenada();
            cabecaCobra = cobra.getCorpo().getLastNode().getQuadrado();
            placar += 1;
            this.tempoAtualizacao -= 5;
            placarField.setText("Placar: " + placar);
        }
    }

    public void colisaoMacaEnvenenada() {
        Quadrado cabecaCobra = cobra.getCorpo().getLastNode().getQuadrado();
        //Estrutura de condicional responsável pela colisão da cobra com a maçã envenenada 
        if (cabecaCobra.x == macaEnvenenada.x && cabecaCobra.y == macaEnvenenada.y) {

            if (cobra.getCorpo().getSize() == 1) {
                cobra.diminuir();
                placarField.setText("Placar: " + placar);
                encerrarJogo("Você comeu uma maçã envenenada e morreu!");

            } else {
                cobra.diminuir();
                gerarMacaEnvenenada();
                placar -= 1;
                placarField.setText("Placar: " + placar);
            }
        }
    }

    public void Iniciar() {
        new Thread(() -> {

            jogoAtivo = true;
            while (jogoAtivo) {
                try {
                    Thread.sleep(tempoAtualizacao);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                cobra.mover(direcao);
                verificarColisoes();
                painel.repaint();

            }
        }).start();
    }

    private void Reiniciar() {
        //Método responsável por reiniciar o jogo. 

        direcao = "parado";

        String[] opcoes = {"Sim", "Não", "Sair do Jogo"};

        // Exibe o JOptionPane com a pergunta
        int escolha = JOptionPane.showOptionDialog(
                null,
                "Deseja Reiniciar o Jogo?",
                "Reiniciar Jogo",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcoes,
                opcoes[0]
        );

        // Trata a escolha do usuário
        switch (escolha) {
            case 0: // Sim
                resetarCobra();
                tempoAtualizacao = 100;
                definirDificuldade(TelaInicial.dificuldade);
                Iniciar();
                break;
            case 1: // Não

                break;
            case 2: // Sair do Jogo
                System.exit(0); // Encerra a aplicação
                break;
            default:
                break;
        }
    }

    private void Pausar() {
        //Método responsável por pausar o jogo. 
        direcao = "parado";

        JOptionPane.showMessageDialog(this, "Jogo Pausado!", "Pause", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        new Tabuleiro();
    }
}
