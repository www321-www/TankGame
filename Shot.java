package tankgameV6;


class Shot implements Runnable{
    int x;
    int y;
    int direct;
    int speed = 5;
    boolean isLive = true;

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public Shot(int x, int y, int direct) {
        this.x = x;
        this.y = y;
        this.direct = direct;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            switch (direct) {
                case 0 -> // up
                        y -= speed;
                case 1 -> // right
                        x += speed;
                case 2 -> // down
                        y += speed;
                case 3 -> //left
                        x -= speed;
            }
            if (!(x >= 0 && x <= 500 && y >= 0 && y <= 500 && isLive)) {
                isLive = false;
                break;
            }
        }
    }
}
