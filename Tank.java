package tankgameV6;

public class Tank {
    private int x;
    private int y;
    private int speed = 5;
    int direct;
    boolean isLive = true;

    public int getDirect() {
        return direct;
    }

    public void setDirect(int direct) {
        this.direct = direct;
    }

    public Tank(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void moveUp() {
        setY(getY() - getSpeed());
    }

    public void moveDown() {
        setY(getY() + getSpeed());
    }

    public void moveLeft() {
        setX(getX() - getSpeed());
    }

    public void moveRight() {
        setX(getX() + getSpeed());
    }
}
