package tankgameV6;

import java.util.Vector;

public class Enemy extends Tank implements Runnable {
    Vector<Shot> shotVector = new Vector<>();
    // 增加成员，EnemyTank 可以得到敌人坦克的Vector
    Vector<Enemy> enemyTanks = new Vector<>();

    boolean isLive = true;

    public Enemy(int x, int y) {
        super(x, y);
    }

    @Override
    public void run() {
        while (true) {
            if (isLive && shotVector.size() < 5){
                Shot s = switch (getDirect()) {
                    case 0 -> new Shot(getX() + 20, getY(), 0);
                    case 1 -> new Shot(getX() + 60, getY() + 20, 1);
                    case 2 -> new Shot(getX() + 20, getY() + 60, 2);
                    case 3 -> new Shot(getX(), getY() + 20, 3);
                    default -> null;
                };
                shotVector.add(s);
                new Thread(s).start();
            }

            setSpeed(3);
            switch (getDirect()) {
                case 0 -> {
                    for (int i = 0; i < 30; i++) {
                        // 增加条件
                        // 原来条件：只要没到达上下左右的边界，就可以移动
                        // 新增条件：没有和其他敌人坦克发生触碰
                        if (getX() > 0 && getY() > 0 && !isTouchEnemyTank()) {
                            moveUp();
                            try {
                                Thread.sleep(50);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                case 1 -> {
                    for (int i = 0; i < 30; i++) {
                        if (getY() > 0 && getX() + 60 < 500 && !isTouchEnemyTank()) {
                            moveRight();
                            try {
                                Thread.sleep(50);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                case 2 -> {
                    for (int i = 0; i < 30; i++) {
                        if (getX() > 0 && getY() + 60 < 500 && !isTouchEnemyTank()) {
                            moveDown();
                            try {
                                Thread.sleep(50);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                case 3 -> {
                    for (int i = 0; i < 30; i++) {
                        if (getY() > 0 && getX() > 0 && !isTouchEnemyTank()) {
                            moveLeft();
                            try {
                                Thread.sleep(50);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
            setDirect((int) (Math.random() * 4));
            if (!isLive) {
                break;
            }
        }
    }

    // 这里提供一个方法，可以将mypanel的成员 Vector<Enemy> enemies = new Vector<>();
    // 设置给enemyTanks
    public void setEnemyTanks(Vector<Enemy> enemyTanks){
        this.enemyTanks = enemyTanks;
    }

    // 编写方法，判断当前的敌人坦克，是否和Vector<Enemy> enemies = new Vector<>();
    // 中的其他坦克发生了重叠或者碰撞
    public boolean isTouchEnemyTank(){
        // 判断当前敌人坦克 this 的方向
        switch(this.getDirect()){
            case 0:
                // 让当前敌人坦克和其他所有的敌人坦克比较
                for (int i = 0; i < enemyTanks.size(); i++) {
                    // 从Vector中取出一个敌人坦克
                    Enemy enemy = enemyTanks.get(i);
                    // 不和自己比较
                    if (this != enemy){
                        // 如果敌人坦克是上或者下
                        // 分析
                        // 1 如果敌人坦克是上下 x的范围是[enemy.getX(), enemy.getX() + 40]
                        //                   y的范围是[enemy.getY(), enemy.getY() + 60]
                        if (enemy.getDirect() == 0 || enemy.getDirect() == 2){
                            // 2 当前坦克左上角的坐标 [this.getX(), this.getY()]
                            if (this.getX() >= enemy.getX()
                                    && this.getX() <= enemy.getX() + 40
                                    && this.getY() >= enemy.getY()
                                    && this.getY() <= enemy.getY() + 60){
                                return true;
                            }
                            // 3 当前坦克右上角的坐标 [this.getX(0 + 40, this.getY()]
                            if (this.getX() + 40 >= enemy.getX()
                                    && this.getX() + 40 <= enemy.getX() + 40
                                    && this.getY() >= enemy.getY()
                                    && this.getY() <= enemy.getY() + 60){
                                return true;
                            }
                        }
                        // 如果敌人坦克是左或者右
                        if (enemy.getDirect() == 1 || enemy.getDirect() == 3){
                            // 2 当前坦克左上角的坐标 [this.getX(), this.getY()]
                            if (this.getX() >= enemy.getX()
                                    && this.getX() <= enemy.getX() + 60
                                    && this.getY() >= enemy.getY()
                                    && this.getY() <= enemy.getY() + 40){
                                return true;
                            }
                            // 3 当前坦克右上角的坐标 [this.getX(0 + 40, this.getY()]
                            if (this.getX() + 40 >= enemy.getX()
                                    && this.getX() + 40 <= enemy.getX() + 60
                                    && this.getY() >= enemy.getY()
                                    && this.getY() <= enemy.getY() + 40){
                                return true;
                            }
                        }
                    }
                }
                break;
            case 1:
                for (int i = 0; i < enemyTanks.size(); i++) {
                    Enemy enemy = enemyTanks.get(i);
                    if (this != enemy){
                        if (enemy.getDirect() == 0 || enemy.getDirect() == 2){
                            if (this.getX() + 60 >= enemy.getX()
                                    && this.getX() + 60 <= enemy.getX() + 40
                                    && this.getY() >= enemy.getY()
                                    && this.getY() <= enemy.getY() + 60){
                                return true;
                            }
                            if (this.getX() + 60 >= enemy.getX()
                                    && this.getX() + 60 <= enemy.getX() + 40
                                    && this.getY() + 40 >= enemy.getY()
                                    && this.getY() + 40 <= enemy.getY() + 60){
                                return true;
                            }
                        }
                        if (enemy.getDirect() == 1 || enemy.getDirect() == 3){
                            if (this.getX() + 60 >= enemy.getX()
                                    && this.getX() + 60 <= enemy.getX() + 60
                                    && this.getY() >= enemy.getY()
                                    && this.getY() <= enemy.getY() + 40){
                                return true;
                            }
                            if (this.getX() + 60 >= enemy.getX()
                                    && this.getX() + 60 <= enemy.getX() + 60
                                    && this.getY()+ 40 >= enemy.getY()
                                    && this.getY() + 40 <= enemy.getY() + 40){
                                return true;
                            }
                        }
                    }
                }
                break;
            case 2:
                for (int i = 0; i < enemyTanks.size(); i++) {
                    Enemy enemy = enemyTanks.get(i);
                    if (this != enemy){
                        if (enemy.getDirect() == 0 || enemy.getDirect() == 2){
                            if (this.getX() >= enemy.getX()
                                    && this.getX() <= enemy.getX() + 40
                                    && this.getY() + 60 >= enemy.getY()
                                    && this.getY() + 60 <= enemy.getY() + 60){
                                return true;
                            }
                            if (this.getX() + 40 >= enemy.getX()
                                    && this.getX() + 40 <= enemy.getX() + 40
                                    && this.getY() + 60 >= enemy.getY()
                                    && this.getY() + 60 <= enemy.getY() + 60){
                                return true;
                            }
                        }
                        if (enemy.getDirect() == 1 || enemy.getDirect() == 3){
                            if (this.getX() >= enemy.getX()
                                    && this.getX() <= enemy.getX() + 60
                                    && this.getY() + 60 >= enemy.getY()
                                    && this.getY() + 60 <= enemy.getY() + 40){
                                return true;
                            }
                            if (this.getX() + 40 >= enemy.getX()
                                    && this.getX() + 40 <= enemy.getX() + 60
                                    && this.getY()+ 60 >= enemy.getY()
                                    && this.getY() + 60 <= enemy.getY() + 40){
                                return true;
                            }
                        }
                    }
                }
                break;
            case 3:
                for (int i = 0; i < enemyTanks.size(); i++) {
                    Enemy enemy = enemyTanks.get(i);
                    if (this != enemy){
                        if (enemy.getDirect() == 0 || enemy.getDirect() == 2){
                            if (this.getX() >= enemy.getX()
                                    && this.getX() <= enemy.getX() + 40
                                    && this.getY() >= enemy.getY()
                                    && this.getY() <= enemy.getY() + 60){
                                return true;
                            }
                            if (this.getX() >= enemy.getX()
                                    && this.getX()<= enemy.getX() + 40
                                    && this.getY() + 40 >= enemy.getY()
                                    && this.getY() + 40 <= enemy.getY() + 60){
                                return true;
                            }
                        }
                        if (enemy.getDirect() == 1 || enemy.getDirect() == 3){
                            if (this.getX() >= enemy.getX()
                                    && this.getX() <= enemy.getX() + 60
                                    && this.getY() >= enemy.getY()
                                    && this.getY() <= enemy.getY() + 40){
                                return true;
                            }
                            if (this.getX() >= enemy.getX()
                                    && this.getX() <= enemy.getX() + 60
                                    && this.getY()+ 40 >= enemy.getY()
                                    && this.getY() + 40 <= enemy.getY() + 40){
                                return true;
                            }
                        }
                    }
                }
                break;
        }
        return false;
    }
}
