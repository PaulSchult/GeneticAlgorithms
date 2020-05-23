package com.ga.folding;

import com.ga.folding.draw.Graphics;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        //Graphics g = new Graphics();

        String acid = "10100110100101100101"; // to be folded
        /************/

        GeneticAlgorithm ga = new GeneticAlgorithm(5, acid);

        /************/

        System.out.println("Hello World!");

        // 0 = white, hydrophilic
        // 1 = black, hydrophobic
        String folded_acid1 = "1r-0u-1u-0l-0d-1l-1u-0l-1u-0l-0u-1u-0l-1u-1r-0u-0l-1l-0d-1"; // without Overlapping => Expected Fitness = 4
        String folded_acid2 = "1r-0u-1u-0l-0d-1l-1u-0l-1u-0l-0d-1d-0r-1d-1l-0d-0r-1r-0u-1"; // without Overlapping => Expected Fitness = 9
        String folded_acid3 = "1r-0u-1u-0l-0d-1l-1u-0l-1u-0l-0d-1d-0r-1d-1l-0d-0r-1r-0u-1r-1"; // with Overlapping => Expected Fitness = 4

        //System.out.println("Fitness: " + calculateFitness(getFoldingData(folded_acid2)));

    }

    public static ArrayList<Pair<Integer, Character>> getFoldingData(String amino_acid) {
        ArrayList<Pair<Integer, Character>> acid_data = new ArrayList<Pair<Integer, Character>>();
        String[] tmp = amino_acid.split("-");

        for(String acid : tmp) {
            Pair element;
            if(acid.length() == 2) {
                element = new Pair<>(Character.getNumericValue(acid.charAt(0)), acid.charAt(1));
            } else {
                // last element does not have an direction parameter
                element = new Pair<>(Character.getNumericValue(acid.charAt(0)), ' ');
            }
            acid_data.add(element);
        }

        return acid_data;
    }

    public static double calculateFitness(ArrayList<Pair<Integer, Character>> acid_data) {

        // 1. # of Hydrophobic Bonds (more = better)
        // 2. # of Overlappings (less = better)
        // => Fitness Value = Hydrophobic Bonds / (Overlappings + 1)

        ArrayList<Triplet<Integer, Integer, Integer>> acid_positions = new ArrayList<Triplet<Integer, Integer, Integer>>();

        Triplet<Integer, Integer, Integer> pos;

        ArrayList<DirectionStruct> directions = new ArrayList<DirectionStruct>();
        directions.add(new DirectionStruct('l',-1, 0));
        directions.add(new DirectionStruct('r',+1, 0));
        directions.add(new DirectionStruct('u',0, +1));
        directions.add(new DirectionStruct('d',0, -1));

        double overlappings = 0;
        double bonds = 0;

        int current_x = 0;
        int current_y = 0;
        int id = 0;
        // iterate over the acid chain and put the position of all hydrophobic elements into an array
        for(int i = 0; i < acid_data.size(); i++) {
            System.out.println(current_x + ", " + current_y + " => " + acid_data.get(i).getKey());






            // fill the acid positions array with hydrophobic elements
            if(acid_data.get(i).getKey() == 1) {
                pos = new Triplet<>(current_x, current_y, id);
                acid_positions.add(pos);
                id++;
                //System.out.println(acid_positions.get(bla).getKey() + " " + acid_positions.get(bla).getValue());
            }

            char src = 0;
            char des = 0;
            if(i >= 1) {
                src = oppositeOf(acid_data.get(i - 1).getValue());
                des = acid_data.get(i).getValue();

                // if current element is hydrophobic
                if (acid_data.get(i).getKey() == 1) {

                    // iterate over all found hydrophobic elements
                    for (Triplet<Integer, Integer, Integer> el : acid_positions) {
                        // check if any elements are overlapping
                        if (el.getKey() == current_x && el.getValue() == current_y && el.getId() != id - 1) {
                            System.out.println("Overlapping found: " + el.getKey() + "|" + el.getValue());
                            overlappings++;
                        }
                    }

                    // iterate over all possible direction
                    for (DirectionStruct dir : directions) {
                        // exclude the source and destination direction
                        if (dir.getDirection() != src && dir.getDirection() != des) {
                            // iterate over all found hydrophobic elements
                            for (Triplet<Integer, Integer, Integer> el : acid_positions) {
                                // check if neighbouring elements are hydrophobic => H/H Bonds
                                if (el.getKey() == (current_x + dir.getX()) && el.getValue() == (current_y + dir.getY())) {
                                    System.out.println("H/H Bond found: " + el.getKey() + "|" + el.getValue() + " and " + current_x + "|" + current_y);
                                    bonds++;
                                }
                            }
                        }
                    }

                }
            }

            // increment correct coordinate in relation to the direction of the next element
            if(acid_data.get(i).getValue() == 'u') {
                current_y++;
            } else if(acid_data.get(i).getValue() == 'd') {
                current_y--;
            } else if(acid_data.get(i).getValue() == 'l') {
                current_x--;
            } else if(acid_data.get(i).getValue() == 'r') {
                current_x++;
            }
        }

        double fitness = bonds / (overlappings + 1);

        return fitness;
    }

    private static char oppositeOf(char direction) {
        switch(direction) {
            case 'l':
                return 'r';
            case 'r':
                return 'l';
            case 'u':
                return 'd';
            case 'd':
                return 'u';
            default:
                System.out.println("Invalid Direction");
                return 0;
        }
    }

}

