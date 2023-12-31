import javafx.scene.control.Button;

public class Map {
    private String[][] map= new String[8][8];

    public Map(){
        for(int row=0;row<8;row++){
            for(int column=0;column<8;column++){
                map[row][column]="";
            }
        }
    }

    public Map(Button[][] buttons){
        for(int row=0;row<8;row++){
            for(int column=0;column<8;column++){
                map[row][column]=buttons[row][column].getText();
            }
        }
    }

    public Map(Map map){
        for(int row=0;row<8;row++){
            for(int column=0;column<8;column++){
                this.map[row][column]=map.getPosition(row,column);
            }
        }
    }

    public String getPosition(int row,int column){
        return map[row][column];
    }

    public void addPosition(int row,int column,String player){
        map[row][column]=player;
        for(int i=row-1;i<=row+1;i++){
            if(i>=0 && i<8 ){
                if(!map[i][column].isEmpty() && !map[i][column].equals(player)){
                    map[i][column]=player;
                }
            }
        }
        for(int j=column-1;j<=column+1;j++){
            if(j>=0 && j<8){
                if(!map[row][j].isEmpty() && !map[row][j].equals(player)){
                    map[row][j]=player;
                }
            }
        }
    }

    public int getScore(String firstPlayer,String secondPlayer){
        int score=64;
        for(int row=0;row<8;row++){
            for(int column=0;column<8;column++){
                if(map[row][column].equals(firstPlayer)){
                    score++;
                }else if(map[row][column].equals(secondPlayer)){
                    score--;
                }
            }
        }
        return score;
    }

}
