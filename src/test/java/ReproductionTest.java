import agh.ics.oop.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReproductionTest {

    @Test
    void ReproduceTest1() throws IOException {
        WorldMap map = new WorldMap(10, 10, 10, 0.5, 10, 10, 10);
        SimulationEngine engine = new SimulationEngine(map, 200, false);
        Animal parentOne = new Animal(map, new Vector2d(1, 1));
        Animal parentTwo = new Animal(map, new Vector2d(1, 1));
        int[] genesOneArray = {0, 0, 0, 0, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 4, 4, 4, 4, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5};
        int[] genesTwoArray = {0, 1, 2, 3, 3, 4, 4, 4, 4, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6};
        ArrayList<Integer> genesOne = new ArrayList<Integer>();
        ArrayList<Integer> genesTwo = new ArrayList<Integer>();
        for (int i = 0; i < 32; i++) {
            genesOne.add(genesOneArray[i]);
            genesTwo.add(genesTwoArray[i]);
        }
        parentOne.setGenes(genesOne);
        parentTwo.setGenes(genesTwo);
        parentOne.setEnergy(50);
        parentTwo.setEnergy(10);
        Animal baby = parentOne.reproduce(parentTwo);

        int[] babyScenarioOne = {0, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5};
        int[] babyScenarioTwo = {0, 0, 0, 0, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 4, 4, 4, 4, 5, 5, 5, 5, 5, 6, 6, 6, 6, 6, 6};
        ArrayList<Integer> scenarioOne = new ArrayList<Integer>();
        ArrayList<Integer> scenarioTwo = new ArrayList<Integer>();
        for (int i = 0; i < 32; i++) {
            scenarioOne.add(babyScenarioOne[i]);
            scenarioTwo.add(babyScenarioTwo[i]);
        }
        assertTrue(baby.getGenes().equals(scenarioOne) || baby.getGenes().equals(scenarioTwo));
        assertEquals(new Vector2d(1, 1), baby.getPosition());
        assertEquals(15.0, baby.getEnergy());
    }

    @Test
    void ReproduceTest2() throws IOException {

        WorldMap map = new WorldMap(10, 10, 10, 0.5, 10, 10, 10);
        SimulationEngine engine = new SimulationEngine(map, 200, false);
        Vector2d position = new Vector2d(1, 1);
        if (map.getAnimalLists().containsKey(position)) {
            map.getAnimalLists().remove(position);
        }
        Animal parentOne = new Animal(map, position);
        Animal parentTwo = new Animal(map, position);
        Animal parentCandidateOne = new Animal(map, position);
        Animal parentCandidateTwo = new Animal(map, position);
        Animal parentCandidateThree = new Animal(map, position);
        map.getAnimalLists().put(position, new ArrayList<>());
        map.getAnimalLists().get(position).add(parentCandidateThree);
        map.getAnimalLists().get(position).add(parentCandidateOne);
        map.getAnimalLists().get(position).add(parentTwo);
        map.getAnimalLists().get(position).add(parentCandidateTwo);
        map.getAnimalLists().get(position).add(parentOne);

        int[] genesOneArray = {0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 2, 2, 2, 2, 2, 2, 3, 3, 4, 4, 4, 4, 4, 4, 5, 5, 6, 6, 7, 7, 7, 7};
        int[] genesTwoArray = {4, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 7, 7, 7, 7, 7, 7, 7, 7};
        ArrayList<Integer> genesOne = new ArrayList<Integer>();
        ArrayList<Integer> genesTwo = new ArrayList<Integer>();
        for (int i = 0; i < 32; i++) {
            genesOne.add(genesOneArray[i]);
            genesTwo.add(genesTwoArray[i]);
        }
        parentOne.setGenes(genesOne);
        parentTwo.setGenes(genesTwo);
        parentOne.setEnergy(50);
        parentTwo.setEnergy(150);
        map.reproduceAnimals(engine);
        Animal baby = map.getAnimalLists().get(position).get(5);

        int[] babyScenarioOne = {0, 0, 0, 0, 0, 0, 0, 0, 5, 5, 5, 5, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 7, 7, 7, 7, 7, 7, 7, 7};
        int[] babyScenarioTwo = {4, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 7, 7, 7, 7};
        ArrayList<Integer> scenarioOne = new ArrayList<Integer>();
        ArrayList<Integer> scenarioTwo = new ArrayList<Integer>();
        for (int i = 0; i < 32; i++) {
            scenarioOne.add(babyScenarioOne[i]);
            scenarioTwo.add(babyScenarioTwo[i]);
        }
        assertTrue(baby.getGenes().equals(scenarioOne) || baby.getGenes().equals(scenarioTwo));
        assertEquals(new Vector2d(1, 1), baby.getPosition());
        assertEquals(50.0, baby.getEnergy());
    }


}