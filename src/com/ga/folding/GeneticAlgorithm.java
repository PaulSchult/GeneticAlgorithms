package com.ga.folding;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GeneticAlgorithm {

  private int maxGeneration;
  private int popSize;
  private int currentGeneration;
  private double populationFitness;

  private Protein bestProtein;

  private String acidString;
  private Population population;
  private List<String[]> allData = new ArrayList<>();

  public GeneticAlgorithm(int maxGeneration, String acidString) {
    this.maxGeneration = maxGeneration;
    this.currentGeneration = 0;
    this.popSize = 5;
    this.acidString = acidString;

    start();
  }

  private void start() {
    createRandomPopulation();
    populationFitness = population.evaluateFitness();

    bestProtein = population.getBestProtein();

    createCSV();

    while(currentGeneration < maxGeneration) {
      currentGeneration++;

      System.out.println("--------------------------------------");
      System.out.println("----------- New Population -----------");
      System.out.println("--------------------------------------");

      if(population.getBestProtein().getFitness() > bestProtein.getFitness()) {
        bestProtein = population.getBestProtein();
      }

      population.selection();
      population.crossover();
      population.mutation();

      populationFitness = population.evaluateFitness();
      bestProtein = population.getBestProtein();

      createCSV();
      //createRandomPopulation();
      //populationFitness = population.evaluateFitness();
      //createCSV();
    }

  }

  private void createCSV() {

    allData.add(population.getData());
    // set currentGeneration field
    allData.get(currentGeneration)[0] = currentGeneration+"";
    allData.get(currentGeneration)[3] = bestProtein.getFitness()+"";
    allData.get(currentGeneration)[4] = bestProtein.getNoOfHHBonds()+"";
    allData.get(currentGeneration)[5] = bestProtein.getNoOfOverlappings()+"";


    try {
      FileWriter csvWriter = new FileWriter("/tmp/log.csv");

      csvWriter.append("Generation Number, Average Fitness, Fitness of best candidate, Fitness of overall best candidate, Its number H/H Bonds, Its number of Overlappings \n");

      for(String[] row : allData) {
        csvWriter.append(String.join(",", row));
        csvWriter.append("\n");
      }

      csvWriter.flush();
      csvWriter.close();


    } catch (IOException e) {
      e.printStackTrace();
    }


  }

  private void createRandomPopulation() {

    population = new Population();

    for(int i = 0; i < popSize; i++) {
      Protein protein = new Protein(acidString);
      protein.createRandomFolding();
      //protein.printFolding();

      population.addProtein(protein);
    }

  }

}
