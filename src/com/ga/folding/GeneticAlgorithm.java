package com.ga.folding;

import com.ga.folding.protein.Protein;
import com.ga.folding.population.Population;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GeneticAlgorithm {

  private int maxGeneration;
  private int popSize;
  private int currentGeneration;
  private int mutationRate;
  private double populationFitness;

  private Protein overallBestProtein;

  private String acidString;
  private Population population;
  private List<String[]> allData = new ArrayList<>();

  public GeneticAlgorithm(int maxGeneration, String acidString) {
    this.maxGeneration = maxGeneration;
    this.currentGeneration = 0;
    this.popSize = 50;
    // in %
    this.mutationRate = 1;
    this.acidString = acidString;

    start();
  }

  private void start() {
    createRandomPopulation();
    populationFitness = population.evaluateFitness();

    overallBestProtein = population.getBestProtein();
    population.drawProtein(currentGeneration);

    createCSV();

    int sameFitnessCnt = 0;
    double prevFitness = 0;

    while(currentGeneration < maxGeneration) {
      currentGeneration++;

      System.out.println("--------------------------------------");
      System.out.println("----------- New Population -----------");
      System.out.println("------------ Generation " + currentGeneration + " ------------");
      System.out.println("--------------------------------------");


      population.selection();
      population.crossover();
      population.mutation(mutationRate);
      //population.tournamentSelection(0.75);

      prevFitness = populationFitness;
      populationFitness = population.evaluateFitness();

      // if the fitness is the same as the previous generation increase counter
      // otherwise reset the counter and the mutation rate
      if(prevFitness == populationFitness) {
        sameFitnessCnt++;
      } else {
        sameFitnessCnt = 0;
        mutationRate = 1;
      }

      // if the fitness doesn't change for 5 continuous generations, we assume that a local maximum is reached
      // => increase the mutation rate to 50%
      if(sameFitnessCnt == 3) {
        mutationRate = 50;
      }

      if(population.getBestProtein().getFitness() > overallBestProtein.getFitness()) {
        overallBestProtein = population.getBestProtein();
      }

      //overallBestProtein = population.getBestProtein();

      //population.drawProtein(currentGeneration);
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
    allData.get(currentGeneration)[3] = overallBestProtein.getFitness()+"";
    allData.get(currentGeneration)[4] = overallBestProtein.getNoOfHHBonds()+"";
    allData.get(currentGeneration)[5] = overallBestProtein.getNoOfOverlappings()+"";


    try {
      FileWriter csvWriter = new FileWriter("/tmp/paul/ga/log.csv");

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
      //protein.useTestFolding("1r-0u-1u-0l-0d-1l-1u-0l-1u-0l-0d-1d-0r-1d-1l-0d-0r-1r-0u-1r-1r");
      population.addProtein(protein);
    }

  }

}
