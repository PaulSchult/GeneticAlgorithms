package com.ga.folding.population;

import com.ga.folding.protein.Molecule;
import com.ga.folding.protein.Protein;
import com.ga.folding.draw.Graphics;
import com.ga.folding.utils.RouletteWheel;
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

    avgFitness = 0;
    totalFitness = 0;

    for(Protein p : proteinList) {

      double currentFitness = p.calculateFitness();
      totalFitness += currentFitness;

      if(currentFitness > bestFitness) {
        bestFitness = currentFitness;
        bestProtein = p;
      }

    }

    avgFitness = totalFitness / proteinList.size();
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

  public void drawProtein(int generationId) {

    for(int pId = 0; pId < proteinList.size(); pId++) {
      Graphics g = new Graphics(proteinList.get(pId), generationId, pId);
    }

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

    for(int i = 0; i < proteinList.size(); i++) {
      int index = rouletteWheel.spin();
      newGenerationList.add(new Protein(proteinList.get(index)));
    }

    proteinList = newGenerationList;

    //for(Protein p : newGenerationList) {
    //  System.out.println("New Generation Fitness: " + p.getFitness());
    //}

  }

  public void mutation(int mutationRate) {

    for(Protein p : proteinList) {

      Random random = new Random();
      // 1% chance to mutate
      int r = random.nextInt(100);
      //System.out.println(r);
      if(r <= mutationRate) {
        //System.out.println("Mutated: " + p.getMolecules());
        p.mutate();
      }

    }

  }

  public void crossover() {

    Random random = new Random();
    // 25% chance for crossover
    int r = random.nextInt(4);

    if(r == 1) {

      int firstProteinId  = random.nextInt(proteinList.size());
      int secondProteinId = random.nextInt(proteinList.size());
      while(secondProteinId == firstProteinId) {
        secondProteinId = random.nextInt(proteinList.size());
      }

      Protein firstProtein  = proteinList.get(firstProteinId);
      Protein secondProtein = proteinList.get(secondProteinId);

      // create copies of protein object
      Protein firstProteinCopy  = new Protein(firstProtein);
      Protein secondProteinCopy = new Protein(secondProtein);

      int cuttingPos = random.nextInt(firstProtein.getMolecules().size());

      // get the correct section that needs to be cut
      List<Molecule> firstTmpMolecules  = firstProteinCopy.getMolecules().subList(0, cuttingPos);
      List<Molecule> secondTmpMolecules = secondProteinCopy.getMolecules().subList(0, cuttingPos);

      for(int i = 0; i < cuttingPos; i++) {
        firstProtein.setMolecule(i, secondTmpMolecules.get(i));
        secondProtein.setMolecule(i, firstTmpMolecules.get(i));
      }


    }

  }

  public void tournamentSelection(double tournamentThreshold) {

    for(Protein protein : proteinList) {
     // protein.calculateFitness();
    }

    List<Protein> nextGeneration = new ArrayList<>();

    for(int round = 0; round < proteinList.size(); round++) {

      Random random = new Random();
      // pick random number where 2 <= numberOfFighter <= proteinList.size()
      // picks number between 0 and proteinList.size()-2
      // => add 2 afterwards to make the range 2 to proteinList.size()
      int numberOfFighter = random.nextInt(proteinList.size() - 2);
      numberOfFighter += 2;

      List<Protein> tournamentFighter = new ArrayList<>();

      for (int i = 0; i < numberOfFighter; i++) {
        boolean validFighter = false;
        while (!validFighter) {
          int proteinId = random.nextInt(proteinList.size());
          // avoid identical proteins
          if (!tournamentFighter.contains(proteinList.get(proteinId))) {
            tournamentFighter.add(proteinList.get(proteinId));
            validFighter = true;
          }
        }
      }

      // select random number between 0 and 1
      double r = random.nextDouble();
      if (r < tournamentThreshold) {
        nextGeneration.add(pickBestCandidate(tournamentFighter));
      } else {
        nextGeneration.add(pickWorstCandidate(tournamentFighter));
      }
    }

    proteinList = nextGeneration;

  }

  private Protein pickBestCandidate(List<Protein> tournamentFighter) {

    double bestFitness = 0;
    Protein bestProtein = null;

    for(Protein protein : tournamentFighter) {
      double currentFitness = protein.getFitness();
      if(currentFitness > bestFitness) {
        bestFitness = currentFitness;
        bestProtein = protein;
      }
    }

    return new Protein(bestProtein);
  }

  private Protein pickWorstCandidate(List<Protein> tournamentFighter) {

    double worstFitness = 10000;
    Protein worstProtein = null;

    for(Protein protein : tournamentFighter) {
      double currentFitness = protein.getFitness();
      if(currentFitness < worstFitness) {
        worstFitness = currentFitness;
        worstProtein = protein;
      }
    }

    return new Protein(worstProtein);
  }

}
