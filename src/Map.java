import javafx.scene.control.Button;

public class Map {
    private String[][] map= new String[8][8];

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
            for(int j=column-1;j<=column+1;j++){
                if(i>=0 && i<8 && j>=0 && j<8){
                    if(!map[i][j].isEmpty() && !map[i][j].equals(player)){
                        map[i][j]=player;
                    }
                }
            }
        }
    }

    public void print(){
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                if(map[i][j].isEmpty()){
                    System.out.print("K");
                }
                else{
                    System.out.print(map[i][j]);
                }

            }
            System.out.println();
        }

    }

}
