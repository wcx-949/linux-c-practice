
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class hello_java extends JPanel implements ActionListener {
    private static final int GRID_SIZE = 20;
    private static final int CELL_SIZE = 20;
    private static final int WIDTH = GRID_SIZE * CELL_SIZE;
    private static final int HEIGHT = GRID_SIZE * CELL_SIZE;
    private static final int EASY_DELAY = 170;
    private static final int MEDIUM_DELAY = 130;
    private static final int HARD_DELAY = 90;
    private static final int EASY_LENGTH = 3;
    private static final int MEDIUM_LENGTH = 4;
    private static final int HARD_LENGTH = 5;

    private final LinkedList<Point> snake = new LinkedList<>();
    private Timer timer;
    private Point food;
    private char direction = 'R';
    private char nextDirection = 'R';
    private boolean running = true;
    private int score = 0;
    private int initialLength = MEDIUM_LENGTH;
    private int delay = MEDIUM_DELAY;
    private String difficultyName = "中等";

    public hello_java() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKey(e.getKeyCode());
            }
        });
        registerKeyBindings();
        chooseDifficulty();
        startGame();
    }

    private void registerKeyBindings() {
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "up");
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0), "up");
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "down");
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), "down");
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "left");
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), "left");
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "right");
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0), "right");

        getActionMap().put("up", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleKey(KeyEvent.VK_UP);
            }
        });
        getActionMap().put("down", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleKey(KeyEvent.VK_DOWN);
            }
        });
        getActionMap().put("left", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleKey(KeyEvent.VK_LEFT);
            }
        });
        getActionMap().put("right", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleKey(KeyEvent.VK_RIGHT);
            }
        });
    }

    public void grabFocusForGame() {
        requestFocusInWindow();
        requestFocus();
    }

    private void handleKey(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                setDirection('U');
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                setDirection('D');
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                setDirection('L');
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                setDirection('R');
                break;
            case KeyEvent.VK_ENTER:
                if (!running) {
                    initGame();
                    timer.start();
                }
                break;
            default:
                break;
        }
    }

    private void chooseDifficulty() {
        String[] options = {"简单", "中等", "困难"};
        int choice = JOptionPane.showOptionDialog(this,
                "请选择游戏难度：",
                "难度选择",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[1]);

        switch (choice) {
            case 0:
                difficultyName = "简单";
                delay = EASY_DELAY;
                initialLength = EASY_LENGTH;
                break;
            case 2:
                difficultyName = "困难";
                delay = HARD_DELAY;
                initialLength = HARD_LENGTH;
                break;
            default:
                difficultyName = "中等";
                delay = MEDIUM_DELAY;
                initialLength = MEDIUM_LENGTH;
                break;
        }
    }

    private void startGame() {
        timer = new Timer(delay, this);
        initGame();
        timer.start();
    }

    private void initGame() {
        snake.clear();
        snake.add(new Point(5, 5));
        for (int i = 1; i < initialLength; i++) {
            snake.add(new Point(5 - i, 5));
        }
        direction = 'R';
        nextDirection = 'R';
        score = 0;
        running = true;
        spawnFood();
        repaint();
    }

    private void spawnFood() {
        do {
            int x = (int) (Math.random() * GRID_SIZE);
            int y = (int) (Math.random() * GRID_SIZE);
            food = new Point(x, y);
        } while (snake.contains(food));
    }

    private void move() {
        direction = nextDirection;
        Point head = snake.getFirst();
        Point next = new Point(head);

        switch (direction) {
            case 'U':
                next.y--;
                break;
            case 'D':
                next.y++;
                break;
            case 'L':
                next.x--;
                break;
            case 'R':
                next.x++;
                break;
            default:
                return;
        }

        if (next.x < 0 || next.x >= GRID_SIZE || next.y < 0 || next.y >= GRID_SIZE) {
            gameOver();
            return;
        }

        if (snake.contains(next)) {
            gameOver();
            return;
        }

        snake.addFirst(next);

        if (next.equals(food)) {
            score += 10;
            spawnFood();
        } else {
            snake.removeLast();
        }

        repaint();
    }

    private void gameOver() {
        running = false;
        timer.stop();
        repaint();
        int result = JOptionPane.showConfirmDialog(this,
                "Game Over! 你的分数是: " + score + "\n是否重新开始？",
                "贪吃蛇",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.YES_OPTION) {
            initGame();
            timer.start();
        }
    }

    private void setDirection(char newDirection) {
        if (!running) {
            if (newDirection == 'R' || newDirection == 'L' || newDirection == 'U' || newDirection == 'D') {
                initGame();
                timer.start();
            }
            return;
        }

        char current = direction;
        if (nextDirection != 0) {
            current = nextDirection;
        }

        if ((current == 'U' && newDirection == 'D') ||
                (current == 'D' && newDirection == 'U') ||
                (current == 'L' && newDirection == 'R') ||
                (current == 'R' && newDirection == 'L')) {
            return;
        }

        nextDirection = newDirection;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int x = 0; x <= WIDTH; x += CELL_SIZE) {
            g.drawLine(x, 0, x, HEIGHT);
        }
        for (int y = 0; y <= HEIGHT; y += CELL_SIZE) {
            g.drawLine(0, y, WIDTH, y);
        }

        g.setColor(Color.RED);
        g.fillRect(food.x * CELL_SIZE, food.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);

        g.setColor(Color.GREEN);
        for (Point p : snake) {
            g.fillRect(p.x * CELL_SIZE + 1, p.y * CELL_SIZE + 1, CELL_SIZE - 2, CELL_SIZE - 2);
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("宋体", Font.BOLD, 18));
        g.drawString("分数: " + score, 10, 20);
        g.drawString("难度: " + difficultyName, 10, 40);

        if (!running) {
            g.setColor(new Color(255, 255, 255, 200));
            g.fillRect(0, 0, WIDTH, HEIGHT);
            g.setColor(Color.BLACK);
            g.setFont(new Font("宋体", Font.BOLD, 36));
            g.drawString("Game Over", 170, 250);
            g.setFont(new Font("宋体", Font.PLAIN, 18));
            g.drawString("按方向键继续/重新开始", 165, 290);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            hello_java game = new hello_java();
            JFrame frame = new JFrame("贪吃蛇");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(game);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setResizable(false);
            frame.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    game.handleKey(e.getKeyCode());
                }
            });
            frame.setVisible(true);
            game.grabFocusForGame();
            frame.getRootPane().requestFocusInWindow();
        });
    }
}
