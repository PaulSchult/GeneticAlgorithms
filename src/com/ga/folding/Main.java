package com.ga.folding;

import com.ga.folding.draw.Graphics;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        String acid = "10100110100101100101"; // to be folded
        /************/

        GeneticAlgorithm ga = new GeneticAlgorithm(50, acid);

        /************/

        // 0 = white, hydrophilic
        // 1 = black, hydrophobic
        String folded_acid1 = "1r-0u-1u-0l-0d-1l-1u-0l-1u-0l-0u-1u-0l-1u-1r-0u-0l-1l-0d-1"; // without Overlapping => Expected Fitness = 4
        String folded_acid2 = "1r-0u-1u-0l-0d-1l-1u-0l-1u-0l-0d-1d-0r-1d-1l-0d-0r-1r-0u-1"; // without Overlapping => Expected Fitness = 9
        String folded_acid3 = "1r-0u-1u-0l-0d-1l-1u-0l-1u-0l-0d-1d-0r-1d-1l-0d-0r-1r-0u-1r-1"; // with Overlapping => Expected Fitness = 4


    }
}

