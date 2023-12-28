package seamfinding;

import seamfinding.energy.EnergyFunction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Dynamic programming implementation of the {@link SeamFinder} interface.
 *
 * @see SeamFinder
 */
public class DynamicProgrammingSeamFinder implements SeamFinder {

    @Override
    public List<Integer> findHorizontal(Picture picture, EnergyFunction f) {
        List<Integer> result = new ArrayList<>(picture.height());
        Double[][] pixels_energies = new Double[picture.width()][picture.height()];

        generatePixels(pixels_energies, picture, f);
        generateShortestEnergyPath(result, pixels_energies, picture);

        return result;
    }

    private void generatePixels(Double[][] pixels_energies, Picture picture, EnergyFunction f) {
        // compute energies for each pixel for the              last column
        for (int y = 0; y < picture.height(); y++) {
            pixels_energies[picture.width() - 1][y] = f.apply(picture, picture.width() - 1, y);
        }

        // compute energies for each pixel for the              remaining columns
        for (int x = picture.width() - 2; x >= 0; x--) {
            for (int y = 0; y < picture.height(); y++) {

                // choose the best energy from neighbors
                double min_energy = Double.POSITIVE_INFINITY;
                for (int z = y - 1; z <= y + 1; z++) {
                    if (0 <= z && z < picture.height()) {

                        double curr_energy = pixels_energies[x + 1][z];
                        if (curr_energy < min_energy) {
                            min_energy = curr_energy;
                        }
                    }
                }

                pixels_energies[x][y] = f.apply(picture, x, y) + min_energy;
            }
        }
    }

    private void generateShortestEnergyPath(List<Integer> result, Double[][] pixels_energies, Picture picture) {
        // 1st column => just choose the smallest y (energy) to start from
        double min_energy = Double.POSITIVE_INFINITY;
        int min_y = -1;
        for (int y = 0; y < picture.height(); y++) {
            double curr_energy = pixels_energies[0][y];

            if (curr_energy < min_energy) {
                min_energy = curr_energy;
                min_y = y;
            }
        }

        result.add(min_y);

        // remaining columns, has to be one of previous column's neighbors
        for (int x = 1; x < picture.width(); x++) {
            min_energy = Double.POSITIVE_INFINITY;

            int y = min_y;
            for (int z = y - 1; z <= y + 1; z++) {
                if (0 <= z && z < picture.height()) {

                    double curr_energy = pixels_energies[x][z];
                    if (curr_energy < min_energy) {
                        min_energy = curr_energy;
                        min_y = z;
                    }
                }
            }

            result.add(min_y);
        }
    }
}