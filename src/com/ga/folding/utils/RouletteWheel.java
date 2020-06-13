package com.ga.folding.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RouletteWheel {

  private List<Double> probabilities = new ArrayList<Double>();

  public RouletteWheel() {

  }

  public void addProbability(double value) {
    probabilities.add(value);
  }

  public int spin() {

    Random random = new Random();
    double p = 100 * random.nextDouble();
    double sum = 0.0;
    int i = 0;

    while(sum < p && i < probabilities.size()) {

      sum += probabilities.get(i);

      i++;
    }


    return (i - 1);

  }

}
