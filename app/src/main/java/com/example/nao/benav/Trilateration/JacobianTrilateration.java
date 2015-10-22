package com.example.nao.benav.Trilateration;

import org.apache.commons.math3.fitting.leastsquares.MultivariateJacobianFunction;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.Pair;

/**
 * Created by nao on 8/17/15.
 */
public class JacobianTrilateration implements MultivariateJacobianFunction {
    private final double positions[][];
    private final double distances[];
    JacobianTrilateration(double positions[][], double distances[]){
        this.positions = positions;
        this.distances = distances;
    }
    public RealMatrix getJacobianMat(RealVector point){
        double[] pointArray = point.toArray();

        double[][] jacobian = new double[distances.length][pointArray.length];
        for (int i = 0; i < jacobian.length; i++) {
            for (int j = 0; j < pointArray.length; j++) {
                jacobian[i][j] = 2 * pointArray[j] - 2 * positions[i][j];
            }
        }

        return new Array2DRowRealMatrix(jacobian);
    }

    public final double[] getDistances() {
        return distances;
    }

    public final double[][] getPositions() {
        return positions;
    }

    @Override
    public Pair<RealVector, RealMatrix> value(RealVector point) {
        double[] pointArray = point.toArray();
        double[] resultPoint = new double[this.distances.length];
        for (int i = 0; i < resultPoint.length; i++) {
            resultPoint[i] = 0.0;
            for (int j = 0; j < pointArray.length; j++) {
                resultPoint[i] += (pointArray[j] - this.positions[i][j]) * (pointArray[j] - this.positions[i][j]);
            }
            resultPoint[i] -= (this.distances[i]) * (this.distances[i]);
        }
        RealMatrix jacobian = getJacobianMat(point);
        return new Pair<RealVector, RealMatrix>(new ArrayRealVector(resultPoint), jacobian);
    }
}
