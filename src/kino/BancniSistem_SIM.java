package kino;

import java.util.Random;

public class BancniSistem_SIM {

    public static boolean procesirajPlacilo(java.math.BigDecimal znesek, String nacinPlacila) {
        if (nacinPlacila == null) {
            return false;
        }

        if ("Gotovina".equalsIgnoreCase(nacinPlacila)) {
            return true; // Cash is always successful
        }

        System.out.println("SIM: Povezujem se z bančnim sistemom za znesek " + znesek + " EUR...");

        // Simulating a short delay for the external system
        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Simulate an 85% success rate for card payments
        Random rand = new Random();
        return rand.nextInt(100) < 85;
    }
}