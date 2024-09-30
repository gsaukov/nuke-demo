package com.nukedemo.geocalculator.services;

import com.menecats.polybool.Epsilon;
import com.menecats.polybool.PolyBool;
import com.menecats.polybool.models.Polygon;

import java.util.*;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;

import static com.menecats.polybool.helpers.PolyBoolHelper.epsilon;

public class PolygonMerginRecursiveTask extends RecursiveTask<List<List<List<List<double[]>>>>> {

    public static final Epsilon EPSILON = epsilon(1e-12, true);

    private List<List<List<List<double[]>>>> features;
    private final int threshold;

    public PolygonMerginRecursiveTask(List<List<List<List<double[]>>>> features, int threshold) {
        this.features = features;
        this.threshold = threshold;
    }

    @Override
    protected List<List<List<List<double[]>>>> compute() {
        if (features.size() > threshold) {
            return process(ForkJoinTask.invokeAll(createSubtasks())
                    .stream()
                    .map(t -> t.join())
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList()));
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
        if (features.size() > 1) {
            return merge(features);
        } else {
            return features;
        }
    }

    public static List<List<List<List<double[]>>>> merge(List<List<List<List<double[]>>>> features) {
        LinkedList<List<List<double[]>>> cumulatives = new LinkedList<>();
        for (List<List<List<double[]>>> feature : features) {
            if(!feature.isEmpty()){
                cumulatives.add(feature.get(0));
                for (int i = 1; i < feature.size(); i++) {
                    Polygon toMerge = new Polygon(feature.get(i));
                    LinkedList<List<List<double[]>>> cumulativesNext = new LinkedList<>();
                    while (!cumulatives.isEmpty()) {
                        Polygon cumulative = new Polygon(cumulatives.pop());
                        if(hasIntersect(cumulative, toMerge)) {
                            toMerge = PolyBool.union(EPSILON, cumulative, toMerge);
                        } else {
                            cumulativesNext.add(cumulative.getRegions());
                        }
                    }
                    cumulativesNext.add(toMerge.getRegions());
                    cumulatives = cumulativesNext;
                }
            }
        }
        return Arrays.asList(cumulatives);
    }

    public static boolean hasIntersect(Polygon poly1, Polygon poly2) {
        return PolyBool.intersect(EPSILON, poly1, poly2).getRegions().size() > 0;
    }

}
