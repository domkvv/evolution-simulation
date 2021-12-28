package agh.ics.oop;

import agh.ics.oop.gui.Statistics;

import java.io.IOException;
import java.util.*;

public class SimulationEngine implements Runnable {
    private volatile boolean running = true;
    private volatile boolean paused = true;
    private final Object pauseLock = new Object();

    private final WorldMap map;
    private final ArrayList<ISimulationEngineObserver> observers = new ArrayList<ISimulationEngineObserver>();
    private final int moveDelay;

    private final boolean isMagic;
    private final Statistics statistics;
    private int dayNumber = 0;
    private int countMagic = 0;
    private boolean magicStatement = false;

    public SimulationEngine(WorldMap map, int moveDelay, boolean isMagic) throws IOException {
        this.map = map;
        this.moveDelay = moveDelay;
        this.isMagic = isMagic;
        this.statistics = new Statistics(this);
    }

    public void toggleSimulation() {
        if (paused) {
            this.resume();
        } else {
            this.pause();
        }
    }

    @Override
    public void run() {
        while (running) {
            synchronized (pauseLock) {
                if (!running) {
                    break;
                }
                if (paused) {
                    try {
                        synchronized (pauseLock) {
                            pauseLock.wait();
                        }
                    } catch (InterruptedException ex) {
                        break;
                    }
                    if (!running) {
                        break;
                    }
                }
            }
            try {
                this.oneDay();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(this.moveDelay);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void resume() {
        synchronized (pauseLock) {
            paused = false;
            pauseLock.notifyAll();
        }
    }

    public void pause() {
        paused = true;
    }

    public void stop() {
        running = false;
        resume();
    }

    private void oneDay() throws IOException {
        this.dayNumber += 1;
        map.removeDeadAnimals(this);
        map.moveAnimals();
        map.feedAnimals();
        map.reproduceAnimals(this);
        map.placePlants();

        if (isMagic && countMagic < 3 && map.animals.size() == 5) {
            map.doMagic();
            this.magicStatement = true;
            countMagic += 1;
        } else {
            this.magicStatement = false;
        }
        mapsChanged();
        statistics.updateStatistics();
    }

    public void addObserver(ISimulationEngineObserver observer) {
        this.observers.add(observer);
    }

    public void mapsChanged() {
        for (ISimulationEngineObserver observer : observers) {
            observer.mapsChanged();
        }
    }

    public int getDayNumber() {
        return dayNumber;
    }

    public WorldMap getMap() {
        return map;
    }

    public void exportStatistics() throws IOException {
        this.statistics.exportToCSV();
    }

    public boolean isMagicStatement() {
        return magicStatement;
    }

}

