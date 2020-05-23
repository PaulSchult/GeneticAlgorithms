package com.ga.folding;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Protein {

  private String acidString;
  private String directions = "lrud"; //left, right, up, down
  private List<String> foldedAcid = new ArrayList<>();

  private double noOfOverlappings;
  private double noOfHHBonds;
  private double fitness;

  private double probability;

  public Protein(String acidString) {
    this.acidString = acidString;
  }

  public void createRandomFolding() {

    for(int i = 0; i < acidString.length(); i++) {

      // last element does not have a direction parameter
      if(i == acidString.length()-1) {
        foldedAcid.add(acidString.charAt(i) + "" );
      } else {
        Random random = new Random();
        int index = random.nextInt(directions.length());

        //System.out.println("ACID: " + acidString.charAt(i));
        //System.out.println("Direction: " + directions.charAt(index));

        foldedAcid.add(acidString.charAt(i) + "" + directions.charAt(index));
      }

    }

  }

  public void printFolding() {
    for(String acid : foldedAcid) {
      System.out.print(acid + " ");
    }
  }

  private ArrayList<Pair<Integer, Character>> getFoldingData() {
    ArrayList<Pair<Integer, Character>> acid_data = new ArrayList<Pair<Integer, Character>>();

    for(String acid : foldedAcid) {
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

  public double calculateFitness() {

    ArrayList<Pair<Integer, Character>> acid_data = getFoldingData() ;

    System.out.println("");
    System.out.println("------ Protein ------");

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
      //System.out.println(current_x + ", " + current_y + " => " + acid_data.get(i).getKey());






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
              //System.out.println("Overlapping found: " + el.getKey() + "|" + el.getValue());
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
                  //System.out.println("H/H Bond found: " + el.getKey() + "|" + el.getValue() + " and " + current_x + "|" + current_y);
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

    fitness = bonds / (overlappings + 1);

    noOfHHBonds = bonds;
    noOfOverlappings = overlappings;

    return fitness;

  }

  private char oppositeOf(char direction) {
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

  public double getNoOfHHBonds() {
    return this.noOfHHBonds;
  }

  public double getNoOfOverlappings() {
    return this.noOfOverlappings;
  }

  public double getFitness() {
    return this.fitness;
  }

  public double getProbability() {
    return this.probability;
  }

  public void calculateProbability(double totalFitness) {
    probability = (this.fitness / totalFitness) * 100;
  }

  public void mutate() {

    Random random = new Random();
    int indexToMutate = random.nextInt(foldedAcid.size());

    System.out.println("index: " + indexToMutate);

    char newDirection = directions.charAt(random.nextInt(4));
    char oldDirection = foldedAcid.get(indexToMutate).charAt(1);

    foldedAcid.set(indexToMutate, foldedAcid.get(indexToMutate).replace(oldDirection, newDirection));
  }
}
