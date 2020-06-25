package com.ga.folding.protein;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Protein {

  private String acidString;
  private String directions = "lrud"; //left, right, up, down
  //private List<String> foldedAcid = new ArrayList<>();

  private List<Molecule> foldedAcid = new ArrayList<>();
  private List<Molecule> foldedAcidWithPos = new ArrayList<>();


  private double noOfOverlappings;
  private double noOfHHBonds;
  private double fitness;

  private double probability;

  public Protein(String acidString) {
    this.acidString = acidString;
  }

  public Protein(Protein protein) {
    this.acidString = new String(protein.acidString);

    for(Molecule molecule : protein.foldedAcid) {
      this.foldedAcid.add(new Molecule(molecule));
    }

    for(Molecule molecule : protein.foldedAcidWithPos) {
      this.foldedAcidWithPos.add(new Molecule(molecule));
    }

    this.noOfOverlappings = protein.noOfOverlappings;
    this.noOfHHBonds = protein.noOfHHBonds;
    this.fitness = protein.fitness;
    this.probability = protein.probability;
  }

  public void createRandomFolding() {

    char direction = ' ';

    for(int i = 0; i < acidString.length(); i++) {

      boolean validDirection = false;

      while(!validDirection) {
        Random random = new Random();
        int tmp_index = random.nextInt(directions.length());
        char tmp_direction = directions.charAt(tmp_index);

        // example: first goes to the right => make sure it can't go left instantly
        if(tmp_direction != oppositeOf(direction)) {
          validDirection = true;
          direction = tmp_direction;
        }
      }
      Molecule molecule = new Molecule(direction, acidString.charAt(i));
      foldedAcid.add(molecule);

      if(i == 0) {
        molecule.setPrevMolecule(null);
      } else {
        molecule.setPrevMolecule(foldedAcid.get(i - 1));
      }

      //foldedAcid.add(acidString.charAt(i) + "" + directions.charAt(index));

    }

    //calculatePosition();
  }

  private char oppositeOf(int direction) {
    switch(direction) {
      case 'l':
        return 'r';
      case 'r':
        return 'l';
      case 'd':
        return 'u';
      case 'u':
        return 'd';
    }

    return ' ';
  }

  public void useTestFolding(String foldedTestAcid) {

    // split contains pairs of charge and direction
    String[] split = foldedTestAcid.split("-");

    for(int i = 0; i < split.length; i++) {
      // direction, charge
      Molecule molecule = new Molecule(split[i].charAt(1), split[i].charAt(0));
      foldedAcid.add(molecule);

      if(i == 0) {
        molecule.setPrevMolecule(null);
      } else {
        molecule.setPrevMolecule(foldedAcid.get(i - 1));
      }
    }

    calculateFitness();
  }

  private void calculatePosition() {

    // initialize first molecule with position 0|0
    foldedAcid.get(0).setPos(0, 0);

    int prevX_Pos = 0;
    int prevY_Pos = 0;

    // iterate over every molecule starting at the second
    for(int i = 1; i < foldedAcid.size(); i++) {

      Molecule m = foldedAcid.get(i);

      // add the X-Direction value and the Y-Direction value (-1 || +1 || 0) onto the previous position
      m.setPos(prevX_Pos + m.getX_direction(), prevY_Pos + m.getY_direction());

      prevX_Pos += m.getX_direction();
      prevY_Pos += m.getY_direction();

    }

    calculateFitness();
  }

  private void calculatePosition(Molecule molecule, Molecule prevMolecule) {

    int newX_pos = prevMolecule.getX_Pos() + prevMolecule.getX_direction();
    int newY_pos = prevMolecule.getY_Pos() + prevMolecule.getY_direction();

    molecule.setPos(newX_pos, newY_pos);
    foldedAcidWithPos.add(molecule);
  }

  public double calculateFitness() {

    int x_pos = 0;
    int y_pos = 0;

    // reset all values
    noOfOverlappings = 0;
    noOfHHBonds = 0;
    foldedAcidWithPos.clear();


    foldedAcid.get(0).setPos(0, 0);
    foldedAcidWithPos.add(foldedAcid.get(0));

    // iterate over every molecule one by one
    for(int i = 1; i < foldedAcid.size(); i++) {

      Molecule molecule = foldedAcid.get(i);
      Molecule prevMolecule = molecule.getPrevMolecule();

      // calculate relative position for every new molecule
      calculatePosition(molecule, prevMolecule);

      x_pos = molecule.getX_Pos();
      y_pos = molecule.getY_Pos();

      boolean overlappingFound = false;

      // iterate over every molecule which already got an relative position
      // and check all four direction for H/H Bonds or Overlappings
      for(int j = 0; j < foldedAcidWithPos.size(); j++) {

        Molecule m = foldedAcidWithPos.get(j);

        // equality check && check if not the prev && charge must be hydrophobic for H/H Bond
        if(molecule != m && molecule.getPrevMolecule() != m) {

          if(x_pos == m.getX_Pos() && y_pos == m.getY_Pos()) {
            noOfOverlappings += 1;
            overlappingFound = true;
          }

          // don't check the surroundings if an overlapping is found
          // it may find another H/H Bond which we don't want to count
          if(!overlappingFound) {

            if(m.getCharge() == '1' && molecule.getCharge() == '1') {
              // check right side
              if (x_pos + 1 == m.getX_Pos() && y_pos == m.getY_Pos()) {
                noOfHHBonds += 1;
              }

              // check left side
              if (x_pos - 1 == m.getX_Pos() && y_pos == m.getY_Pos()) {
                noOfHHBonds += 1;

              }

              // check top side
              if (x_pos == m.getX_Pos() && y_pos + 1 == m.getY_Pos()) {
                noOfHHBonds += 1;

              }

              // check bottom side
              if (x_pos == m.getX_Pos() && y_pos - 1 == m.getY_Pos()) {
                noOfHHBonds += 1;
              }
            }

          }

        }

      }

    }

    fitness = (noOfHHBonds) / (noOfOverlappings + 1);

    System.out.println("H/H Bonds: " + noOfHHBonds);
    System.out.println("Overlappings: " + noOfOverlappings);
    System.out.println("Fitness: " + fitness);
    for(Molecule m : foldedAcid) {
      System.out.print(m.getCharge() + "" + m.getDirection() + " ");
    }
    System.out.println("");
    System.out.println("---------------");

    return fitness;
  }

  public void printFolding() {
    /*for(String acid : foldedAcid) {
      System.out.print(acid + " ");
      System.out.println("");
    }*/
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

  public List<Molecule> getMolecules() {
    return this.foldedAcid;
  }

  public void setMolecule(int position, Molecule molecule) {
    this.foldedAcid.set(position, molecule);
  }

  public void calculateProbability(double totalFitness) {
    probability = (this.fitness / totalFitness) * 100;
  }

  public void mutate() {

    Random random = new Random();
    int indexToMutate = random.nextInt(foldedAcid.size());

    //System.out.println("index: " + indexToMutate);

    char newDirection = directions.charAt(random.nextInt(directions.length()));
    //System.out.println("Index: " + indexToMutate);
    //System.out.println("Old Direction: " + foldedAcid.get(indexToMutate).getDirection());
    for(Molecule m : this.foldedAcid) {
      //System.out.print(m.getCharge() + "" + m.getDirection() + " ");
    }

    foldedAcid.get(indexToMutate).setDirection(newDirection);
    //System.out.println("New Direction: " + newDirection);
    for(Molecule m : this.foldedAcid) {
      //System.out.print(m.getCharge() + "" + m.getDirection() + " ");
    }

    //char oldDirection = foldedAcid.get(indexToMutate).getDirection();

    //foldedAcid.set(indexToMutate, foldedAcid.get(indexToMutate).replace(oldDirection, newDirection));
  }


}
