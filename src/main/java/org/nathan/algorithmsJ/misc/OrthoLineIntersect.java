package org.nathan.algorithmsJ.misc;

import org.jetbrains.annotations.NotNull;
import org.nathan.algorithmsJ.structures.OSTree;
import org.nathan.centralUtils.tuples.Tuple;

import java.util.*;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;

/**
 * orthogonal line segment intersection
 */
public class OrthoLineIntersect{
  public static @NotNull <L, P> Map<L, List<L>> intersects(
          @NotNull List<L> lines,
          @NotNull Function<L, Tuple<P, P>> toPoints,
          @NotNull ToDoubleFunction<P> getX,
          @NotNull ToDoubleFunction<P> getY){
    Map<P, L> pointsToLine = new HashMap<>();
    List<P> scanPoints = new ArrayList<>();
    Set<P> hPoints = new HashSet<>();
    Map<P, P> otherEndPoint = new HashMap<>();

    for(var line : lines){
      var point_pair = toPoints.apply(line);
      otherEndPoint.put(point_pair.first(), point_pair.second());
      otherEndPoint.put(point_pair.second(), point_pair.first());

      var x1 = getX.applyAsDouble(point_pair.first());
      var x2 = getX.applyAsDouble(point_pair.second());

      pointsToLine.put(point_pair.first(), line);
      scanPoints.add(point_pair.first());

      if(x1 != x2){
        pointsToLine.put(point_pair.second(), line);
        scanPoints.add(point_pair.second());

        hPoints.add(point_pair.first());
        hPoints.add(point_pair.second());
        if(getY.applyAsDouble(point_pair.first()) != getY.applyAsDouble(point_pair.second())){
          throw new IllegalArgumentException("lines are not orthogonal.");
        }
      }
    }

    scanPoints.sort(Comparator.comparing(getX::applyAsDouble));
    Set<P> inTree = new HashSet<>();
    OSTree<Double, L> HYToHL_tree = new OSTree<>(Double::compareTo);
    Map<L, List<L>> res = new HashMap<>();
    var funcAddIntersect = new Object(){
      void apply(P v_p){
        var vl = pointsToLine.get(v_p);
        var ap = otherEndPoint.get(v_p);
        var y1 = getY.applyAsDouble(ap);
        var y2 = getY.applyAsDouble(v_p);
        var hls = HYToHL_tree.keyRangeSearch(Math.min(y1, y2), Math.max(y1, y2));
        if(hls.size() > 0){
          res.put(vl, hls.stream().map(Tuple::second).collect(Collectors.toList()));
        }
      }
    };

    var funcRmFromTree = new Object(){
      void apply(P h_p){
        inTree.remove(otherEndPoint.get(h_p));
        HYToHL_tree.deleteKey(getY.applyAsDouble(h_p));
      }
    };

    for(int i = 0; i < scanPoints.size(); i++){
      var p = scanPoints.get(i);

      if(hPoints.contains(p)){
        if(inTree.contains(otherEndPoint.get(p))){
          if(i + 1 < scanPoints.size() && getX.applyAsDouble(p) == getX.applyAsDouble(scanPoints.get(i+1))){
            // lines with the same x
            var ti = i + 1;
            while(getX.applyAsDouble(p) == getX.applyAsDouble(scanPoints.get(ti)) && hPoints.contains(scanPoints.get(ti))){
              ti++; // degeneration when lots of horizon lines end in the same x value
            }
            if(getX.applyAsDouble(p) == getX.applyAsDouble(scanPoints.get(ti))){
              // exchange vertical point first encountered with current horizontal point
              var op = p;
              p = scanPoints.get(ti);
              scanPoints.set(ti, op);
              funcAddIntersect.apply(p);
            }
            else {
              // no vertical point left
              funcRmFromTree.apply(otherEndPoint.get(p));
              var np = scanPoints.get(i+1);
              do{
                funcRmFromTree.apply(np);
                np = scanPoints.get(++i);
              }
              while(getX.applyAsDouble(p) == getX.applyAsDouble(np) && hPoints.contains(np));
              --i;
            }
          }
          else{
            funcRmFromTree.apply(otherEndPoint.get(p));
          }
        }
        else{
          inTree.add(p);
          HYToHL_tree.insertKV(getY.applyAsDouble(p), pointsToLine.get(p));
        }
      }
      else{
        // TODO maybe bug
        // check first
        funcAddIntersect.apply(p);
      }
    }

    return res;
  }
}
