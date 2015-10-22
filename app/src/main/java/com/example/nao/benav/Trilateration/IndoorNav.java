package com.example.nao.benav.Trilateration;

import android.renderscript.Double2;
import android.util.Log;

import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;

/**
 * Created by nao on 8/14/15.
 */
public class IndoorNav {
    public Double2 getTrilaterationNLS(double[][] positions, double[] distances){
        JacobianTrilateration JT = new JacobianTrilateration(positions,distances);
        TrilaterationSolver TS = new TrilaterationSolver(JT,new LevenbergMarquardtOptimizer());
        LeastSquaresOptimizer.Optimum optimum = TS.solve();
        double[] Pos = optimum.getPoint().toArray();
        Log.d("Trilateration","NLS , x : "+Pos[0]+" , y : "+Pos[1]);
        return new Double2(Pos[0],Pos[1]);
    }
    public Double2 getTrilaterationP(double[][] positions,double[] distances){

        double CAB,CBC,xTarget,yTarget,xFilter;
        CAB = distances[0]*distances[0] - distances[1]*distances[1] - positions[0][0]*positions[0][0] - positions[0][1]*positions[0][1] + positions[1][0]*positions[1][0] + positions[1][1]*positions[1][1];
        CBC = distances[1]*distances[1] - distances[2]*distances[2] - positions[1][0]*positions[1][0] - positions[1][1]*positions[1][1] + positions[2][0]*positions[2][0] + positions[2][1]*positions[2][1];

        yTarget = (CAB * (positions[2][0]-positions[1][0]) - CBC*(positions[1][0]-positions[0][0])) / (2 * ((positions[1][1]-positions[0][1]) * (positions[2][0]-positions[1][0]) - (positions[2][1]-positions[1][1]) * (positions[1][0]-positions[0][0])));
        xTarget = (CAB - 2 * yTarget * (positions[1][1] - positions[0][1])) / (2 * (positions[1][0]-positions[0][0]));

        xFilter = (CBC - 2 * yTarget * (positions[2][1]-positions[1][1])) / (2 * (positions[2][0]-positions[1][0]));
        xTarget = (xTarget + xFilter) / 2;

        Log.d("Trilateration", "3 Circle , x : "+xTarget+" , y : "+yTarget);
        return new Double2(xTarget,yTarget);
    }
    public Double2 getTrilaterationHeron(double S, double distances[]){
        double Sp = (S+distances[0]+distances[1])/2.0;
        double Area = Math.sqrt(Sp*(Sp-S)*(Sp-distances[0])*(Sp-distances[1]));
        double Height = Area/(S/2.0);
        double y = Math.sqrt(distances[0]*distances[0] - Height*Height);

        Sp = (S+distances[0]+distances[2])/2.0;
        Area = Math.sqrt(Sp * (Sp-S) * (Sp-distances[0]) * (Sp-distances[2]));
        Height = (Area/(S/2.0));
        double x = Math.sqrt(distances[0]*distances[0] - Height*Height);
        Log.d("Trilateration", "Heron , x : "+x+" , y : "+y);
        return new Double2(x,y);
    }

}
