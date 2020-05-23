package com.ga.folding;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Population {

  private List<Protein> proteinList = new ArrayList<>();
  private double avgFitness;
  private double totalFitness;
  private double bestFitness = 0;
  private Protein bestProtein;

  public Population() {
  }

  public void addProtein(Protein protein) {
    proteinList.add(protein);
  }

  public double evaluateFitness() {

    for(Protein p : proteinList) {

      double fitness = p.calculateFitness();
      avgFitness += fitness;

      if(fitness > bestFitness) {
        bestFitness = fitness;
        bestProtein = p;
      }
      System.out.println("Fitness: " + fitness);

    }
    // used only for calculation of probability
    totalFitness = avgFitness;

    avgFitness /= proteinList.size();
    System.out.println("Average Fitness: " + avgFitness);

    return avgFitness;
  }

  public String[] getData() {

    /*
    (1) generation number, (2) avg fitness, (3) fitness of best candidate,
    (4) fitness of overall best candidate (5) its number of H/H bonds,
    (6) its number of overlappings
     */
    return (
        new String[] {"", avgFitness+"", bestFitness+"", "", "", ""}
    );

  }

  public Protein getBestProtein() {
    return bestProtein;
  }

  public void selection() {
    RouletteWheel rouletteWheel = new RouletteWheel();

    for(Protein p : proteinList) {
      p.calculateProbability(totalFitness);
      double probability = p.getProbability();
      rouletteWheel.addProbability(probability);

      //System.out.println("Probability: " + p.getProbability() + " Fitness: " + p.getFitness());
    }

    List<Protein> newGenerationList = new ArrayList<Protein>();

    for(int i = 0; i < 5; i++) {
      int index = rouletteWheel.spin();
      newGenerationList.add(proteinList.get(index));
    }

    proteinList = newGenerationList;

    //for(Protein p : newGenerationList) {
    //  System.out.println("New Generation Fitness: " + p.getFitness());
    //}

  }

  public void mutation() {

    for(Protein p : proteinList) {

      Random random = new Random();
      // 1% chance to mutate
      int r = random.nextInt(100);
      //System.out.println(r);
      if(r == 1) {
        System.out.println("Mutated: ");
        p.mutate();
      }

    }

  }

  public void crossover() {

    Random random = new Random();
    // 25% chance for crossover
    int r = random.nextInt(4);

    if(r == 1) {

      

    }

  }

}
