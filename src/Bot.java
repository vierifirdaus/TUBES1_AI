import javafx.scene.control.Button;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class Bot {
    private Map map;

    public Bot(Button[][] buttons){
        map=new Map(buttons);
    }

    public int[] move(Button[][] buttons, String type,Integer depth) {
        map = new Map(buttons);
        if(type.equals("minimax")){
            Pair<Integer,Integer> res = minimax(map,depth>5 ?5 : depth,-1000,1000,true).getValue();

            return new int[]{res.getKey(), res.getValue()};
        }
        else if(type.equals("localSearch")){
            return new int[]{localSearch(buttons).getKey(),localSearch(buttons).getValue()};
        }
        return null;

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
//        return true;
        for(int rowCheck=row-1;rowCheck<=row+1;rowCheck++){
            for(int columnCheck=column-1;columnCheck<=column+1;columnCheck++){
                if(rowCheck>=0 && rowCheck<8 && columnCheck>=0 && columnCheck<8){
                    if(!map.getPosition(rowCheck,columnCheck).isEmpty()){
                        return true;
                    }
                }

            }
        }
        return false;
    }


    private int getScore(Map map){
        int score=0;
        for(int row=0;row<8;row++){
            for(int column=0;column<8;column++){
                if(map.getPosition(row,column).equals("O")){
                    score+=1;
                }
                if(map.getPosition(row,column).equals("X")){
                    score-=1;
                }
            }
        }
        return score;
    }

    private Pair<Integer,Pair<Integer,Integer>> minimax(Map map,int depth, int alpha, int beta, boolean turn){
        Pair<Integer,Integer>[] validLocation=getValidLocations(map);


        int alphaNow=alpha;
        int betaNow=beta;

        if(depth==0){
            return new Pair<>(getScore(map),null);
        }

        Pair<Integer,Integer> locationNow=validLocation[0];
        int score;
        
        if(turn){
            score=-1000;
            for(int i=0;i<validLocation.length;i++){
                Map mapCopy = new Map(map);
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
            for(int i=0;i<validLocation.length;i++){
                Map mapCopy = new Map(map);
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
    private Pair<Integer,Integer> localSearch(Button[][] buttons){
        Pair<Integer,Integer> location = null;
        int score=-10000;
        for(int row=0;row<8;row++){
            for(int column=0;column<8;column++){
                if(buttons[row][column].getText().isEmpty()){

                    Map mapCopy = new Map(buttons);
                    mapCopy.addPosition(row,column,"O");
                    if(score<getScore(mapCopy)){
                        score=getScore(mapCopy);
                        location=new Pair<>(row,column);
                    }
                }

            }
        }
        return  location;
    }
}
