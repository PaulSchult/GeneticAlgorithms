package com.ga.folding.protein;

public class Molecule {

  private char direction;
  private char charge;
  private Molecule prevMolecule;
  private int x_pos;
  private int y_pos;

  private int x_direction;
  private int y_direction;

  // 0 = white, hydrophilic
  // 1 = black, hydrophobic

  public Molecule(char direction, char charge) {
    this.direction = direction;
    this.charge = charge;

    this.x_pos = 0;
    this.y_pos = 0;

    switch(direction) {
      case 'l':
        x_direction = -1;
        y_direction =  0;
        break;
      case 'r':
        x_direction = +1;
        y_direction =  0;
        break;
      case 'u':
        x_direction =  0;
        y_direction = +1;
        break;
      case 'd':
        x_direction =  0;
        y_direction = -1;
        break;
    }
  }

  public Molecule(Molecule molecule) {
    this.direction = molecule.direction;
    this.charge = molecule.charge;

    if(molecule.prevMolecule != null) {
      this.prevMolecule = new Molecule(molecule.prevMolecule);
    }

    this.x_pos = molecule.x_pos;
    this.y_pos = molecule.y_pos;
    this.x_direction = molecule.x_direction;
    this.y_direction = molecule.y_direction;
  }

  public char getDirection() {
    return this.direction;
  }

  public void setDirection(char newDirection) {
    this.direction = newDirection;
  }

  public int getX_direction() {
    return this.x_direction;
  }

  public int getY_direction() {
    return this.y_direction;
  }

  public char getCharge() {
    return this.charge;
  }

  public void setPos(int x, int y) {
    this.x_pos = x;
    this.y_pos = y;
  }

  public int getX_Pos() {
    return this.x_pos;
  }

  public int getY_Pos() {
    return this.y_pos;
  }

  public void setPrevMolecule(Molecule prevM) {
    this.prevMolecule = prevM;
  }

  public Molecule getPrevMolecule() {
    return this.prevMolecule;
  }
}
