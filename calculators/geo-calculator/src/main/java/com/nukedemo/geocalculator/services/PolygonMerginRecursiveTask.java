package com.nukedemo.geocalculator.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;

public class PolygonMerginRecursiveTask extends RecursiveTask<List<List<List<List<double[]>>>>> {
    private List<List<List<List<double[]>>>> features;

    private final int threshold;

    public PolygonMerginRecursiveTask(List<List<List<List<double[]>>>> features, int threshold) {
        this.features = features;
        this.threshold = threshold;
    }

    @Override
    protected List<List<List<List<double[]>>>> compute() {
        if (features.size() > threshold) {
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
        dividedTasks.add(new PolygonMerginRecursiveTask(features.subList(0, features.size() / 2), threshold));
        dividedTasks.add(new PolygonMerginRecursiveTask(features.subList(features.size() / 2, features.size()), threshold));
        return dividedTasks;
    }

    private List<List<List<List<double[]>>>> process(List<List<List<List<double[]>>>> features) {
        return PolygonClippingService.merge(features);
    }
}
