package tankgameV6;

import java.io.*;
import java.util.Vector;

public class Recorder {
    // 该类记录相关信息和文件交互
    private static int allEnemyTanks = 0;
    private static BufferedReader br = null;
    private static BufferedWriter bw = null;
    private static String recordPath = "src/hspEdu/tankgameV6/record.txt";
    private static Vector<Enemy> enemyVector = null;
    private static Vector<Node> nodes = new Vector<>();

    // 增加一个方法，用于读取文件，恢复相关信息
    public static Vector<Node> getNodesAndEnemyTanRec(){
        try {
            br = new BufferedReader(new FileReader(recordPath));
            allEnemyTanks = Integer.parseInt(br.readLine());
            String line = "";
            while((line = br.readLine()) != null){
                String[] xyd = line.split(" ");
                Node node = new Node(Integer.parseInt(xyd[0]),
                        Integer.parseInt(xyd[1]),
                        Integer.parseInt(xyd[2]));
                nodes.add(node);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null){
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return nodes;
    }

    public static int getAllEnemyTanks() {
        return allEnemyTanks;
    }

    public static void setAllEnemyTanks(int allEnemyTanks) {
        Recorder.allEnemyTanks = allEnemyTanks;
    }

    // 当我方坦克击毁一辆敌人坦克，对击毁数量++
    public static void addAllEnemy(){
        allEnemyTanks++;
    }

    public static void setEnemyVector(Vector<Enemy> enemyVector) {
        Recorder.enemyVector = enemyVector;
    }

    // 增加一个方法，当游戏退出的时候，我们将allElementTanks保存到recordFIle
    public static void keepRecord() {
        try {
            bw = new BufferedWriter(new FileWriter(recordPath));
            bw.write(allEnemyTanks + "\r\n");
            // 遍历敌人坦克的Vector，然后根据情况保存即可
            // 从OOp的角度来说，定义一个属性，通过setXXX得到
            for (int i = 0; i < enemyVector.size(); i++) {
                Enemy enemy = enemyVector.get(i);
                if (enemy.isLive){
                    // 保存该enemy坦克的信息
                    String record = enemy.getX() + " "+ enemy.getY() + " " + enemy.getDirect();
                    // 写入到文件
                    bw.write(record + "\r\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (bw != null){
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

class Node{
    private int x;
    private int y;
    private int direct;

    public Node(int x, int y, int direct) {
        this.x = x;
        this.y = y;
        this.direct = direct;
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

    public int getDirect() {
        return direct;
    }

    public void setDirect(int direct) {
        this.direct = direct;
    }
}
