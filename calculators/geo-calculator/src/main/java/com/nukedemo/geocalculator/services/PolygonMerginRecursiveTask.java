package com.nukedemo.geocalculator.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;

public class PolygonMerginRecursiveTask extends RecursiveTask<List<List<List<List<double[]>>>>> {
    private List<List<List<List<double[]>>>> features;

    private static final int THRESHOLD = 1000;

    public PolygonMerginRecursiveTask(List<List<List<List<double[]>>>> features) {
        this.features = features;
    }

    @Override
    protected List<List<List<List<double[]>>>> compute() {
        if (features.size() > THRESHOLD) {
            return ForkJoinTask.invokeAll(createSubtasks())
                    .stream()
                    .map(ForkJoinTask::join)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());
        } else {
            return process(features);
        }
    }

    private Collection<PolygonMerginRecursiveTask> createSubtasks() {
        List<PolygonMerginRecursiveTask> dividedTasks = new ArrayList<>();
        dividedTasks.add(new PolygonMerginRecursiveTask(features.subList(0, features.size() / 2)));
        dividedTasks.add(new PolygonMerginRecursiveTask(features.subList(features.size() / 2, features.size())));
        return dividedTasks;
    }

    private List<List<List<List<double[]>>>> process(List<List<List<List<double[]>>>> features) {
        return features;
    }
}
