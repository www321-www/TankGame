package hspEdu.tankgameV6;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Scanner;
import java.util.Vector;

// 版本更新内容：
// 1. 防止敌人坦克重叠运动
// 2. 记录玩家的总成绩【累计击毁敌方坦克数量】，存盘退出
// 3. 记录当时的敌人坦克坐标，存盘退出
// 4. 玩游戏时，可以选择重新一局游戏还是继续上局游戏

public class DrawCircle extends JFrame {
    MyPanel mp = null;

    public static void main(String[] args) {
        DrawCircle drawCircle = new DrawCircle();
    }

    public DrawCircle() throws HeadlessException {
        System.out.println("请输入选择：1：新游戏 2：继续上局");
        Scanner scanner = new Scanner(System.in);
        String key = scanner.next();
        mp = new MyPanel(key);

        Thread thread = new Thread(mp);
        thread.start();

        this.add(mp);
        this.setSize(900, 700);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.addKeyListener(mp);

        // 在JFrame中增加相应关闭窗口的处理
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Recorder.keepRecord();
                System.exit(0);
            }
        });
    }
}

class MyPanel extends JPanel implements KeyListener, Runnable {
    Hero hero;
    Vector<Enemy> enemies = new Vector<>();
    int enemyTanks = 3;
    Vector<Node> nodeVector = null;

    Vector<Bomb> bombs = new Vector<>();
    Image img1 = null;
    Image img2 = null;
    Image img3 = null;


    public MyPanel(String key) {
        nodeVector = Recorder.getNodesAndEnemyTanRec();
        // 将所有的坦克加入到Recorder中，才能实现记录剩余坦克的操作
        Recorder.setEnemyVector(enemies);

        this.hero = new Hero(250, 300);

        switch(key){
            case "1":
                for (int i = 0; i < enemyTanks; i++) {
                    Enemy enemy = new Enemy(100 * (i + 1), 0);
                    // 将enemyTanks设置给enemyTank!!!
                    enemy.setEnemyTanks(enemies);
                    enemy.setDirect(2);
                    new Thread(enemy).start();

                    Shot shot = new Shot(enemy.getX() + 20, enemy.getY() + 60, enemy.getDirect());
                    enemy.shotVector.add(shot);
                    // 启动shot对象线程
                    new Thread(shot).start();

                    enemies.add(enemy);
                }
                break;
            case "2":
                for (int i = 0; i < nodeVector.size(); i++) {
                    Node node = nodeVector.get(i);
                    Enemy enemy = new Enemy(node.getX(), node.getY());
                    // 将enemyTanks设置给enemyTank!!!
                    enemy.setEnemyTanks(enemies);
                    enemy.setDirect(node.getDirect());
                    new Thread(enemy).start();

                    Shot shot = new Shot(enemy.getX() + 20, enemy.getY() + 60, enemy.getDirect());
                    enemy.shotVector.add(shot);
                    // 启动shot对象线程
                    new Thread(shot).start();

                    enemies.add(enemy);
                }
                break;
            default:
                System.out.println("wrong input!");
        }
        // 初始化图片对象
//        img1 = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bomb_1.gif"));
//        img2 = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bomb_2.gif"));
//        img3 = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bomb_3.gif"));
    }

    // 编写方法，显示我方击毁坦克的信息
    public void showInfo(Graphics g){
        // 画出玩家的总成绩
        g.setColor(Color.BLACK);
        Font font = new Font("宋体", Font.BOLD, 25);
        g.setFont(font);
        g.drawString("您累计击毁敌方坦克", 520, 30);
        drawTank(520, 60, g, 0,0);
        g.setColor(Color.BLACK); // 这里需要重新设置成黑色
        g.drawString(Recorder.getAllEnemyTanks() + "", 580, 100);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        showInfo(g);
        g.fillRect(0, 0, 500, 500);
        if (hero != null && hero.isLive) {
            drawTank(hero.getX(), hero.getY(), g, hero.getDirect(), 0);
        }

        // 绘制一颗子弹的代码实现
//        if (hero.shot != null && hero.shot.isLive) {
//            g.draw3DRect(hero.shot.x, hero.shot.y, 1, 1, false);
//        }
        // 绘制所有子弹的代码实现
        for (int i = 0; i < hero.shots.size(); i++) {
            Shot shot = hero.shots.get(i);
            // 如果该shot对象有效
            if (shot != null && shot.isLive) {
                g.draw3DRect(shot.x, shot.y, 1, 1, false);
            } else { // 如果该shot对象无效
                hero.shots.remove(shot);
            }
        }

        // 如果bombs集合中有对象，就画出
//        for (int i = 0; i < bombs.size(); i++) {
//            Bomb bomb = bombs.get(i);
//            if (bomb.life > 6){
//                g.drawImage(img1, bomb.x, bomb.y, 60, 60, this);
//            }else if (bomb.life > 3){
//                g.drawImage(img2, bomb.x, bomb.y, 60, 60, this);
//            }else{
//                g.drawImage(img3, bomb.x, bomb.y, 60, 60, this);
//            }
//            bomb.lifDown(); // 让炸弹的生命值减少
//            if (bomb.life == 0){
//                bombs.remove(bomb);
//            }
//        }

        for (Enemy enemy : enemies) {
            if (enemy.isLive) {
                drawTank(enemy.getX(), enemy.getY(), g, enemy.getDirect(), 1);
                for (int i = 0; i < enemy.shotVector.size(); i++) {
                    Shot shot = enemy.shotVector.get(i);
                    if (shot.isLive) {
                        g.draw3DRect(shot.x, shot.y, 1, 1, false);
                    } else {
                        enemy.shotVector.remove(shot);
                    }
                }
            }
        }
    }

    public void drawTank(int x, int y, Graphics g, int direct, int type) {
        switch (type) {
            case 0 -> g.setColor(Color.CYAN);
            case 1 -> g.setColor(Color.yellow);
        }
        switch (direct) {
            case 0: // up
                g.fill3DRect(x, y, 10, 60, false);
                g.fill3DRect(x + 30, y, 10, 60, false);
                g.fill3DRect(x + 10, y + 10, 20, 40, false);
                g.drawOval(x + 12, y + 20, 16, 16);
                g.drawLine(x + 20, y + 30, x + 20, y);
                break;
            case 3: // left
                g.fill3DRect(x, y, 60, 10, false);
                g.fill3DRect(x, y + 30, 60, 10, false);
                g.fill3DRect(x + 10, y + 10, 40, 20, false);
                g.fillOval(x + 22, y + 12, 16, 16);
                g.drawLine(x + 30, y + 20, x, y + 20);
                break;
            case 2: // down
                g.fill3DRect(x, y, 10, 60, false);
                g.fill3DRect(x + 30, y, 10, 60, false);
                g.fill3DRect(x + 10, y + 10, 20, 40, false);
                g.drawOval(x + 12, y + 20, 16, 16);
                g.drawLine(x + 20, y + 30, x + 20, y + 60);
                break;
            case 1: // right
                g.fill3DRect(x, y, 60, 10, false);
                g.fill3DRect(x, y + 30, 60, 10, false);
                g.fill3DRect(x + 10, y + 10, 40, 20, false);
                g.fillOval(x + 22, y + 12, 16, 16);
                g.drawLine(x + 30, y + 20, x + 60, y + 20);
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            if (hero.getX() > 0 && hero.getY() + 60 < 500) {
                hero.moveDown();
                hero.setDirect(2);
            }
        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
            if (hero.getX() > 0 && hero.getY() > 0) {
                hero.moveUp();
                hero.setDirect(0);
            }
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (hero.getX() > 0 && hero.getY() > 0) {
                hero.moveLeft();
                hero.setDirect(3);
            }
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (hero.getX() + 60 < 500 && hero.getY() > 0) {
                hero.moveRight();
                hero.setDirect(1);
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_J) {
            // 发射多颗子弹的代码
            hero.shotEnemyTank();
        }
        this.repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 判断hero是否击中了enemies
            shotsHitEnemies();
            // 判断enemies是否击中了hero
            hitHero();
            this.repaint();
        }
    }

    public void shotsHitEnemies() {
        for (int i = 0; i < hero.shots.size(); i++) {
            Shot shot = hero.shots.get(i);
            if (shot != null && shot.isLive) {
                for (int j = 0; j < enemies.size(); j++) {
                    Enemy enemy = enemies.get(j);
                    if (enemy != null && enemy.isLive) {
                        hitTank(shot, enemy);
                    }
                }
            }
        }
    }

    public void hitTank(Shot shot, Tank enemy) {
        switch (enemy.getDirect()) {
            case 0:
            case 2:
                if (shot.x > enemy.getX() && shot.x < enemy.getX() + 40
                        && shot.y > enemy.getY() && shot.y < enemy.getY() + 60) {
                    shot.isLive = false;
                    enemy.isLive = false;
                    enemies.remove(enemy);
                    // 当我方击毁一个敌方坦克，就把杀敌数++
                    if (enemy instanceof Enemy){
                        Recorder.addAllEnemy();
                    }
                    Bomb bomb = new Bomb(enemy.getX(), enemy.getY());
                    bombs.add(bomb);
                }
                break;
            case 1:
            case 3:
                if (shot.x > enemy.getX() && shot.x < enemy.getX() + 60
                        && shot.y > enemy.getY() && shot.y < enemy.getY() + 40) {
                    shot.isLive = false;
                    enemy.isLive = false;
                    enemies.remove(enemy);
                    // 当我方击毁一个敌方坦克，就把杀敌数++
                    if (enemy instanceof Enemy){
                        Recorder.addAllEnemy();
                    }
                    Bomb bomb = new Bomb(enemy.getX(), enemy.getY());
                    bombs.add(bomb);
                }
                break;
        }
    }

    public void hitHero() {
        for (int i = 0; i < enemies.size(); i++) {
            Enemy enemy = enemies.get(i);
            for (int j = 0; j < enemy.shotVector.size(); j++) {
                Shot shot = enemy.shotVector.get(j);
                if (hero.isLive && shot.isLive) {
                    hitTank(shot, hero);
                }
            }
        }
    }
}

