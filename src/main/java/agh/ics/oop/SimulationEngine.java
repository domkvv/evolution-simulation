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
        if (this.paused) {
            this.resume();
        } else {
            this.pause();
        }
    }

    @Override
    public void run() {
        while (this.running) {
            synchronized (this.pauseLock) {
                if (!this.running) {
                    break;
                }
                if (this.paused) {
                    try {
                        synchronized (this.pauseLock) {
                            this.pauseLock.wait();
                        }
                    } catch (InterruptedException ex) {
                        break;
                    }
                    if (!this.running) {
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
        synchronized (this.pauseLock) {
            this.paused = false;
            this.pauseLock.notifyAll();
        }
    }

    public void pause() {
        this.paused = true;
    }

    public void stop() {
        this.running = false;
        this.resume();
    }

    private void oneDay() throws IOException {
        this.dayNumber += 1;
        this.map.removeDeadAnimals(this);
        this.map.moveAnimals();
        this.map.feedAnimals();
        this.map.reproduceAnimals(this);
        this.map.placePlants();

        if (this.isMagic && this.countMagic < 3 && this.map.getAnimals().size() == 5) {
            this.map.doMagic();
            this.magicStatement = true;
            this.countMagic += 1;
        } else {
            this.magicStatement = false;
        }
        mapsChanged();
        this.statistics.updateStatistics();
    }

    public void addObserver(ISimulationEngineObserver observer) {
        this.observers.add(observer);
    }

    public void mapsChanged() {
        for (ISimulationEngineObserver observer : this.observers) {
            observer.mapsChanged();
        }
    }

    public void exportStatistics() throws IOException {
        this.statistics.exportToCSV();
    }

    public int getDayNumber() {
        return this.dayNumber;
    }

    public WorldMap getMap() {
        return this.map;
    }

    public boolean isMagicStatement() {
        return this.magicStatement;
    }

    public boolean isPaused() {
        return this.paused;
    }

}

