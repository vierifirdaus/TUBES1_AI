import javafx.scene.control.Button;

import java.util.Arrays;
import java.util.Random;
import java.util.ArrayList;

public class GeneticAlgorithm {
    private int POPULATION_SIZE;
    private int CHROMOSOME_LENGTH;
    private int MAX_GENERATIONS ;
    private double MUTATION_RATE;
    private ArrayList<Integer> pointMap = new ArrayList<>(); // Use ArrayList instead of array
    private String[] playerMap = new String[65];
    private String firstPlayer;
    private String secondPlayer;

    public GeneticAlgorithm(int population, int chromosomeLength, int max_generations, double mutationRate, Button[][] buttons, String firstPlayer, String secondPlayer) {
        this.POPULATION_SIZE = population;
        this.CHROMOSOME_LENGTH = chromosomeLength;
        this.MAX_GENERATIONS = max_generations;
        this.MUTATION_RATE = mutationRate;
        Map tempMap = new Map(buttons);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (!tempMap.getPosition(i, j).isEmpty()) {
                    pointMap.add(i * 8 + j);
                    playerMap[i * 8 + j] = tempMap.getPosition(i, j);
                }
            }
        }
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
    }

    public int execute() {
        int[][] population = initializePopulation();
        int generation = 1;
        int[] fitnessScores;
        int fitnessMax=-9999999;
        int idxMax=0;
        while (generation <= MAX_GENERATIONS) {
            fitnessScores = calculateFitness(population);

            int[][] newPopulation = new int[POPULATION_SIZE][CHROMOSOME_LENGTH];

            for (int i = 0; i < POPULATION_SIZE; i++) {
                int[] parent1 = selectParent(population, fitnessScores);
                int[] parent2 = selectParent(population, fitnessScores);
                int[] offspring = crossover(parent1, parent2);
                offspring = mutate(offspring);
                newPopulation[i] = offspring;
            }

            population = newPopulation;
            generation++;
            if(generation==MAX_GENERATIONS){
                for(int i=0;i<POPULATION_SIZE;i++){
                    if(fitnessScores[i]>fitnessMax){
                        fitnessMax=fitnessScores[i];
                        idxMax=i;
                    }
                }

            }
        }


        return population[idxMax][0];
    }

    private int[][] initializePopulation() {
        int[][] population = new int[POPULATION_SIZE][CHROMOSOME_LENGTH];
        Random random = new Random();

        for (int i = 0; i < POPULATION_SIZE; i++) {
            for (int j = 0; j < CHROMOSOME_LENGTH; j++) {
                ArrayList<Integer> randomize= new ArrayList<>();
                for(int idxRandom=0;idxRandom<64;idxRandom++){
                    randomize.add(idxRandom);
                }
                randomize.removeAll(pointMap);
                //amann
                for(int idx=0;idx<j;idx++){
                    randomize.remove(randomize.indexOf(population[i][idx]));
                }
                population[i][j]=randomize.get(random.nextInt(randomize.size()));
            }
        }

        return population;
    }

    // menghitung nilai fitness berdasarkan nilai heuristic
    // aman
    private int[] calculateFitness(int[][] population) {
        int[] fitnessScores = new int[POPULATION_SIZE];

        for (int i = 0; i < POPULATION_SIZE; i++) {
            int chromosomeSum = 0;

            Map tempMap = new Map();
            for(int idx=0;idx<pointMap.size();idx++){
                tempMap.addPosition(pointMap.get(idx)/8,pointMap.get(idx)%8,playerMap[pointMap.get(idx)]);
            }
            for(int idx=0;idx<CHROMOSOME_LENGTH;idx++){
                if(idx%2==0){
                    tempMap.addPosition(population[i][idx]/8,population[i][idx]%8,firstPlayer);
                }
                else{
                    tempMap.addPosition(population[i][idx]/8,population[i][idx]%8,secondPlayer);
                }
            }

            fitnessScores[i] = tempMap.getScore(firstPlayer,secondPlayer);
        }

        return fitnessScores;
    }

    // ga perlu diganti
    private int[] selectParent(int[][] population, int[] fitnessScores) {
        Random random = new Random();
        int totalFitness = Arrays.stream(fitnessScores).sum();
        double rouletteWheelPosition = random.nextDouble() * totalFitness;
        double spinWheel = 0;

        for (int i = 0; i < POPULATION_SIZE; i++) {
            spinWheel += fitnessScores[i];

            if (spinWheel >= rouletteWheelPosition) {
                return population[i];
            }
        }

        return population[POPULATION_SIZE - 1];
    }


    // elemen dalam offspring ga boleh sama
    private int[] crossover(int[] parent1, int[] parent2) {
        int[] offspring = new int[CHROMOSOME_LENGTH];
        Random random = new Random();
        int crossoverPoint = random.nextInt(CHROMOSOME_LENGTH);

        for (int i = 0; i < CHROMOSOME_LENGTH; i++) {

            if (i < crossoverPoint) {
                offspring[i] = parent1[i];
            } else {
                offspring[i] = parent2[i];
            }

        }

        for(int i=0;i<CHROMOSOME_LENGTH;i++){
            for(int j=i+1;j<CHROMOSOME_LENGTH;j++){
                if(offspring[i]==offspring[j]){
                    offspring=parent1;
                    break;
                }
            }
        }

        return offspring;
    }

    // mutasi ga boleh sembarangan, kalo belum ada isinya boleh, kalo ada isinya gaboleh
    private int[] mutate(int[] chromosome) {
        Random random = new Random();

        for (int i = 0; i < CHROMOSOME_LENGTH; i++) {
            if (random.nextDouble() <= MUTATION_RATE) {
                ArrayList<Integer> randomize= new ArrayList<>();
                for(int idxRandom=0;idxRandom<64;idxRandom++){
                    randomize.add(idxRandom);
                }
                randomize.removeAll(pointMap);
                for(int idx=0;idx<i;idx++){
                   for(int idxRandom=0;idxRandom<randomize.size();idxRandom++){
                       if(randomize.get(idxRandom)==chromosome[idx]){
                           randomize.remove(idxRandom);
                           break;
                       }
                   }
                }
                chromosome[i]=randomize.get(random.nextInt(randomize.size()));
                break;
            }

        }

        return chromosome;
    }
}