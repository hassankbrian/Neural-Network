/*
 * This was one of the three group projects that was selected to be presented in
 * the 200 people lecture for 1332. Pretty sweet.(bragging rights)
 * 
 * in this file we have the backbone of the neural network. Here we do all the calculus 
 * and heavy lifting.  
 *  
 *  
 * @author Brian Hassan 
 * @version 1.0 
 */
import java.util.List;
import java.util.Random;

import javax.swing.JProgressBar;


public class BackPropagationNeuralNetwork {
	
	public enum TransferFunction {
		none,
		sigmoid,
		linear
	}
	
	private static class TransferFunctions {
		
		public static double evaluate(TransferFunction transferFunction, double x) {
			
			switch (transferFunction) {
			case none:
				return 0.0;
			case sigmoid:
				return sigmoid(x);
			case linear:
				return linear(x);
			default:
				return 0.0;
			}

		}
		public static double evaluateDerivative(TransferFunction transferFunction, double x) {
			
			switch(transferFunction) {
			case none:
				return 0.0;
			case sigmoid:
				return sigmoidDerivative(x);
			case linear:
				return linearDerivative(x);
			default:
				return 0.0;
			}
			
		}
		
		private static double sigmoid(double x) {
			return 1.0/(1.0 + Math.exp(-x));
		}
		
		private static double sigmoidDerivative(double x) {
			return sigmoid(x)*(1-sigmoid(x));
		}
		
		private static double linear(double x) {
			return x;
		}
		
		private static double linearDerivative(double x) {
			return 1;
		}
		
	}

	private static class Gaussian {
		
		private static Random random = new Random();
		
		public static double getRandomGaussian() {
			return getRandomGaussian(0.0,1.0);
		}
		
		public static double getRandomGaussian(double mean, double stdDev) {
			double u, v, s, t;
			
			do {
				u = 2*random.nextDouble() - 1;
				v = 2*random.nextDouble() - 1;
			} while (u*u + v*v > 1 || (u == 0 && v == 0));
			s = u*u + v*v;
			t = Math.sqrt(-2.0*Math.log(s)/s);
			
			return stdDev*u*t + mean;
		}
		
	}
	
	private int layerCount;
	private int inputSize;
	private int[] layerSizes;
	private TransferFunction[] transferFunctions;
	
	private double[][] layerOutputs;
	private double[][] layerInputs;
	private double[][] biases;
	private double[][] deltas;
	private double[][] previousBiasDeltas;
	
	private double[][][] weights;
	private double[][][] previousWeightDeltas;
	
	public BackPropagationNeuralNetwork(int[] layerSizes, TransferFunction[] transferFunctions) {
		
		if (transferFunctions.length != layerSizes.length || transferFunctions[0] != TransferFunction.none) {
			throw new IllegalArgumentException();
		}
		
		layerCount = layerSizes.length - 1;
		inputSize = layerSizes[0];
		this.layerSizes = new int[layerCount];
		this.transferFunctions = new TransferFunction[layerCount];
		
		for (int i = 0; i < layerCount; i++) {
			this.layerSizes[i] = layerSizes[i+1];
			this.transferFunctions[i] = transferFunctions[i+1];
		}
		
		biases = new double[layerCount][];
		previousBiasDeltas = new double[layerCount][];
		deltas = new double[layerCount][];
		layerOutputs = new double[layerCount][];
		layerInputs = new double[layerCount][];
		
		weights = new double[layerCount][][];
		previousWeightDeltas = new double[layerCount][][];
		
		int layerSize;
		for (int i = 0; i < layerCount; i++) {
			layerSize = this.layerSizes[i];
			biases[i] = new double[layerSize];
			previousBiasDeltas[i] = new double[layerSize];
			deltas[i] = new double[layerSize];
			layerOutputs[i] = new double[layerSize];
			layerInputs[i] = new double[layerSize];
			
			weights[i] = new double[i == 0 ? inputSize:this.layerSizes[i-1]][];
			previousWeightDeltas[i] = new double[i == 0 ? inputSize:this.layerSizes[i-1]][];
			
			for (int j = 0; j <  (i == 0 ? inputSize:this.layerSizes[i-1]); j++) {
				weights[i][j] = new double[this.layerSizes[i]];
				previousWeightDeltas[i][j] = new double[this.layerSizes[i]];
			}
		}
		
		for (int i = 0; i < layerCount; i++) {
			for (int j = 0; j < this.layerSizes[i]; j++) {
				biases[i][j] = Gaussian.getRandomGaussian();
			}
			
			for (int j = 0; j < (i == 0 ? inputSize:this.layerSizes[i-1]); j++) {
				for (int k = 0; k < this.layerSizes[i]; k++) {
					weights[i][j][k] = Gaussian.getRandomGaussian();
				}
			}
		}
	}

	public double[] recognize(double[] inputs) {
		if (inputs.length != inputSize) {
			throw new IllegalArgumentException();
		}

		double sum;
		for (int l = 0; l < layerCount; l++) {
			for (int j = 0; j < layerSizes[l]; j++) {
				sum = 0.0;
				for (int i = 0; i < (l == 0 ? inputSize:layerSizes[l-1]); i++) {
					sum += weights[l][i][j]*(l == 0 ? inputs[i]:layerOutputs[l-1][i]);
				}
				sum += biases[l][j];
				layerInputs[l][j] = sum;
				
				layerOutputs[l][j] = TransferFunctions.evaluate(transferFunctions[l], sum);
			}
		}
		
		return layerOutputs[layerCount-1];
	}

	public double learn(double[] input, double[] desired, double learningRate, double momentum) {
		if (input.length != inputSize) {
			throw new IllegalArgumentException();
		}
		if (desired.length != layerSizes[layerCount-1]) {
			throw new IllegalArgumentException();
		}
		
		double error = 0.0, sum = 0.0, weightDelta = 0.0, biasDelta = 0.0;
		double[] outputs;
		
		outputs = recognize(input);
		
		for (int l = layerCount-1; l >= 0; l--) {
			if (l == layerCount-1) {
				for (int k = 0; k < layerSizes[l]; k++) {
					deltas[l][k] = outputs[k] - desired[k];
					error += Math.pow(deltas[l][k], 2);
					deltas[l][k] *= TransferFunctions.evaluateDerivative(transferFunctions[l], layerInputs[l][k]);
				}
			} else {
				for (int i = 0; i < layerSizes[l]; i++) {
					sum = 0.0;
					for (int j = 0; j < layerSizes[l+1]; j++) {
						sum += weights[l+1][i][j]*deltas[l+1][j];
					}
					sum *= TransferFunctions.evaluateDerivative(transferFunctions[l], layerInputs[l][i]);
					deltas[l][i] = sum;
				}
			}
		}
		
		for (int l = 0; l < layerCount; l++) {
			for (int i = 0; i < (l == 0 ? inputSize:layerSizes[l-1]); i++) {
				for (int j = 0; j < layerSizes[l]; j++) {
					weightDelta = learningRate*deltas[l][j]*(l == 0 ? input[i]:layerOutputs[l-1][i]) + momentum*previousWeightDeltas[l][i][j];
					weights[l][i][j] -= weightDelta;
					previousWeightDeltas[l][i][j] = weightDelta;
				}
			}
		}
		
		for (int l = 0; l < layerCount; l++) {
			for (int i = 0; i < layerSizes[l]; i++) {
				biasDelta = learningRate*deltas[l][i] + momentum*previousBiasDeltas[l][i];
				biases[l][i] -= biasDelta;
				previousBiasDeltas[l][i] = biasDelta;
			}
		}
		
		return error;
	}
	
	public void learn(List<Grid> learningSet,double learningRate,double momentum, double errorTolerance, JProgressBar progressBar) {
		double[][] inputs = new double[learningSet.size()][];
		double[][] desired = new double[learningSet.size()][];
		double error = 0.0;
		error = 0.0;
		int maxCount = 100000, count = 0;
		
		for (int i = 0; i < learningSet.size(); i ++) {
			inputs[i] = gridToArray(learningSet.get(i));
			desired[i] = new double[1];
			desired[i][0] = i;
		}
		
		progressBar.setValue(0);
		progressBar.repaint();
		do{
			count++;
			error = 0.0;
			for (int i = 0; i < learningSet.size(); i++) {
				error += learn(inputs[i], desired[i], learningRate,momentum);
			}
			if (count%maxCount == 100) {
				progressBar.setValue(progressBar.getValue() + progressBar.getMaximum()/100);
				progressBar.repaint();
			}
		} while (error > errorTolerance && count <= maxCount);
		progressBar.setValue(progressBar.getMaximum());
	}

	public int recognize(Grid grid) {
		double[] inputs = gridToArray(grid);
		double[] output = recognize(inputs);
		return (int) Math.round(output[0]);
	}
	
	private double[] gridToArray(Grid grid) {
		double[] array = new double[grid.getRows()*grid.getCols()];
		for (int i = 0; i < grid.getRows(); i++) {
			for (int j = 0; j < grid.getCols(); j++) {
				array[grid.getCols()*i + j] = grid.isPressedAt(i, j) ? 1.0:0.0;
			}
		}
		return array;
	}

}
