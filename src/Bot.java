import javafx.scene.control.Button;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Bot {
    private Map map;

    public Bot(Button[][] buttons){
        map=new Map(buttons);
    }

    public int[] move(Button[][] buttons) {
        map = new Map(buttons);
        Pair<Integer,Integer> res = minimax(map,5,-1000,1000,true).getValue();
        System.out.printf("%s: %s%n", res.getKey(), res.getValue());
        // create random move
//        map.print();
        return new int[]{res.getKey(), res.getValue()};

//        return new int[]{(int), column};
    }

    private Pair<Integer, Integer>[] getValidLocations(Map map) {
        // Define a list to store valid locations
        List<Pair<Integer, Integer>> validLocations = new ArrayList<>();

        // Loop through the 2D array of buttons to find valid locations
        for (int row = 0; row < 8; row++) {
            for (int column = 0; column < 8; column++) {

                // Check if the button represents a valid location (you should define the condition)
                if (isValidLocation(map,row,column) && map.getPosition(row,column).isEmpty()) {
                    // Add the valid location to the list
                    validLocations.add(new Pair<>(row, column));
                }
            }
        }

        // Convert the list to an array and return it
        return validLocations.toArray(new Pair[0]);
    }

    // Define the isValidLocation method based on your requirements
    private boolean isValidLocation(Map map,int row,int column) {
        for(int rowCheck=row-2;rowCheck<=row+2;rowCheck++){
            for(int columnCheck=column-2;columnCheck<=column+2;columnCheck++){
                if(rowCheck>=0 && rowCheck<8 && columnCheck>=0 && columnCheck<8){
                    if(!map.getPosition(rowCheck,columnCheck).isEmpty()){
                        return true;
                    }
                }

            }
        }
        return false;
    }

    private int getScore(int row,int column){
        int score=1;
        for(int i=row-1;i<row+2;i++){
            for(int j=column-1;j<column+2;j++){
                if(i>=0 && i<8 && j>=0 && j<8){
                    if(map.getPosition(i,j).equals("x")){
                        score+=1;
                    }
                }

            }
        }
        return score;
    }
    private int getScoreAll(Map map){
        int score=0;
        for(int row=0;row<8;row++){
            for(int column=0;column<8;column++){
                if(map.getPosition(row,column).equals("O")){
                    score++;
                }
            }
        }
        return score;
    }

    private Pair<Integer,Pair<Integer,Integer>> minimax(Map map,int depth, int alpha, int beta, boolean turn){
        Pair<Integer,Integer>[] validLocation=getValidLocations(map);
//        for(int i=0;i<validLocation.length;i++){
//            System.out.printf("%d %d\n",validLocation[i].getKey(),validLocation[i].getValue());
//        }
        Pair<Integer,Integer> locationNow;
        int alphaNow=alpha;
        int betaNow=beta;
        if(depth==0){
            return new Pair<>(getScoreAll(map),null);
        }
        int score;
        if(turn){
            score=-1000;
            locationNow=validLocation[1];
            for(int i=1;i<validLocation.length;i++){
                Map mapCopy = new Map(this.map);
                mapCopy.addPosition(validLocation[i].getKey(),validLocation[i].getValue(),"O");
                int newScore = minimax(mapCopy,depth-1,alphaNow,betaNow,false).getKey();
                if(newScore>score){
                    score=newScore;
                    locationNow=validLocation[i];
                }
                if(alpha<score){
                    alphaNow=score;
                }

                if(alpha>=beta){
                    break;
                }
            }
            return new Pair(score,locationNow);
        }else{
            score=1000;
            locationNow=validLocation[1];
            for(int i=1;i<validLocation.length;i++){
                Map mapCopy = new Map(this.map);
                mapCopy.addPosition(validLocation[i].getKey(),validLocation[i].getValue(),"X");
                int newScore = minimax(mapCopy,depth-1,alphaNow,betaNow,true).getKey();
                if(newScore<score){
                    score=newScore;
                    locationNow=validLocation[i];
                }
                if(beta>score){
                    betaNow=score;
                }

                if(alpha>=beta){
                    break;
                }
            }
            return new Pair(score,locationNow);
        }

    }
}
