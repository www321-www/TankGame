package tankgameV6;

import java.util.Vector;

public class Hero extends Tank {
    Shot shot = null;
    Vector<Shot> shots = new Vector<>();

    public void shotEnemyTank() {
        if (shots.size() < 5) {

            switch (getDirect()) {
                case 0 -> shot = new Shot(this.getX() + 20, this.getY(), 0);
                case 1 -> shot = new Shot(this.getX() + 60, this.getY() + 20, 1);
                case 2 -> shot = new Shot(this.getX() + 20, this.getY() + 60, 2);
                case 3 -> shot = new Shot(this.getX(), this.getY() + 20, 3);
            }
            shots.add(shot);
            Thread thread = new Thread(shot);
            thread.start();
        }
    }

    public Hero(int x, int y) {
        super(x, y);
    }
}
