package tankgameV6;

public class Bomb {
    int x;
    int y;
    int life = 9;
    boolean isLive = true;

    public Bomb(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void lifDown(){
        if (life > 0){
            life--;
        }else{
            isLive = false;
        }
    }
}
