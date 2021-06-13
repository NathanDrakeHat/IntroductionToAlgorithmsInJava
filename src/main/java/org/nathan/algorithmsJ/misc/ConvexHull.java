package org.nathan.algorithmsJ.misc;

import java.util.*;
import java.util.function.ToDoubleFunction;

public class ConvexHull{
  public static <E> List<E> GrahamScan(List<E> points, ToDoubleFunction<E> getX, ToDoubleFunction<E> getY){
    if(points.size() <= 2){
      return Collections.emptyList();
    }
    List<E> ps = new ArrayList<>(points);
    ps.sort(Comparator.comparingDouble(getY));

    var start = ps.get(0);

    List<E> others = new ArrayList<>(ps.subList(1, ps.size()));
    Map<E, Double> angles = new HashMap<>(others.size());

    var funcRelativeAngle = new Object(){
      double apply(E from, E to){
        var angle = Math.atan2(getY.applyAsDouble(to) - getY.applyAsDouble(from),
                getX.applyAsDouble(to) - getX.applyAsDouble(from));
        if(angle < 0){
          angle += Math.PI;
        }
        return angle;
      }
    };
    for(var end : others){
      angles.put(end, funcRelativeAngle.apply(start, end));
    }
    others.sort(Comparator.comparing(angles::get));

    Deque<E> points_stack = new ArrayDeque<>();
    points_stack.addLast(start);
    points_stack.addLast(others.get(0));

    var funcDist = new Object(){
      double apply(E a, E b){
        double ax = getX.applyAsDouble(a);
        double ay = getY.applyAsDouble(a);

        double bx = getX.applyAsDouble(b);
        double by = getY.applyAsDouble(b);
        return Math.sqrt(Math.pow(ax - bx, 2) + Math.pow(ay - by, 2));
      }
    };
    var funcCCW = new Object(){
      int apply(E a, E b, E c){
        double ax = getX.applyAsDouble(a);
        double ay = getY.applyAsDouble(a);

        double bx = getX.applyAsDouble(b);
        double by = getY.applyAsDouble(b);

        double cx = getX.applyAsDouble(c);
        double cy = getY.applyAsDouble(c);

        double area = (bx - ax) * (cy - ay) - (by - ay) * (cx - ax);
        if(area < 0){ return -1; }
        else if(area > 0){ return 1; }
        else{ return 0; }
      }
    };

    // longest edge
    for(int i = 1; i < others.size(); i++){
      var current = others.get(i);
      var last = points_stack.removeLast();
      var head = points_stack.getLast();
      if(funcCCW.apply(head, last, current) == 0){
        if(funcDist.apply(last, head) > funcDist.apply(head, current)){
          points_stack.addLast(last);
        }
        else{
          points_stack.addLast(current);
        }
      }
      else{
        points_stack.addLast(last);
        break;
      }
    }

    for(int i = 0; i < others.size(); i++){
      var p = others.get(i);
      var last = points_stack.removeLast();
      var ccw = funcCCW.apply(points_stack.getLast(), last, p);
      if(ccw > 0){
        points_stack.addLast(last);
        points_stack.addLast(p);
      }
      else if(ccw == 0){
        points_stack.addLast(last);
      }
      else{
        do{
          last = points_stack.removeLast();
        }
        while(funcCCW.apply(points_stack.getLast(), last, p) < 0);
        points_stack.addLast(last);
        points_stack.addLast(p);
      }
    }

    List<E> res = new ArrayList<>(points_stack.size());
    res.addAll(points_stack);
    return res;
  }
}
