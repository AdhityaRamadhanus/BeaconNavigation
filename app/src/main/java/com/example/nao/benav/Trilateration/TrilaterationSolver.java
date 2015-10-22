package com.example.nao.benav.Trilateration;

import com.example.nao.benav.Trilateration.JacobianTrilateration;

import org.apache.commons.math3.fitting.leastsquares.LeastSquaresFactory;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresProblem;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.DiagonalMatrix;

/**
 * Created by nao on 8/17/15.
 */
public class TrilaterationSolver {
    private final JacobianTrilateration function;
    private final LeastSquaresOptimizer leastSquaresOptimizer;

    private final static int MAXNUMBEROFITERATIONS = 100;

    public TrilaterationSolver(JacobianTrilateration function, LeastSquaresOptimizer leastSquaresOptimizer) {
        this.function = function;
        this.leastSquaresOptimizer = leastSquaresOptimizer;
    }

    public LeastSquaresOptimizer.Optimum solve(double[] target, double[] weights, double[] initialPoint) {

        LeastSquaresProblem leastSquaresProblem = LeastSquaresFactory.create(
                // function to be optimized
                function,
                // target values at optimal point in least square equation
                // (x0+xi)^2 + (y0+yi)^2 + ri^2 = target[i]
                new ArrayRealVector(target, false),
                new ArrayRealVector(initialPoint, false),
                new DiagonalMatrix(weights),
                null, MAXNUMBEROFITERATIONS, MAXNUMBEROFITERATIONS);

        return leastSquaresOptimizer.optimize(leastSquaresProblem);
    }

    public LeastSquaresOptimizer.Optimum solve() {
        int numberOfPositions = function.getPositions().length;
        int positionDimension = function.getPositions()[0].length;

        double[] initialPoint = new double[positionDimension];
        // initial point, use average of the vertices
        for (int i = 0; i < function.getPositions().length; i++) {
            double[] vertex = function.getPositions()[i];
            for (int j = 0; j < vertex.length; j++) {
                initialPoint[j] += vertex[j];
            }
        }
        for (int j = 0; j < initialPoint.length; j++) {
            initialPoint[j] /= numberOfPositions;
        }


        double[] target = new double[numberOfPositions];
        double[] distances = function.getDistances();
        double[] weights = new double[target.length];
        // Weights are inversely proportional to the the square of the distances I think
        for (int i = 0; i < target.length; i++) {
            target[i] = 0.0;
            //weights[i] = 1.0;
            weights[i] = (distances[i] * distances[i]);
        }

        return solve(target, weights, initialPoint);
    }
}
