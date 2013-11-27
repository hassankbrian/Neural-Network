import java.util.Collection;

import javax.swing.JProgressBar;


public class BackPropagationNeuralNetwork implements NeuralNetwork {
	
	public enum TransferFunction {
		none,
		sigmoid
	}
	
	private static class TransferFunctions {
		
		public double evaluate(TransferFunction transferFunction, double x) {
			
			switch (transferFunction) {
			case none:
				return 0.0;
			case sigmoid:
				return sigmoid(x);
			default:
				return 0.0;
			}

		}
		public double evaluateDerivative(TransferFunction trasnferFunction, double x) {
			
			switch(trasnferFunction) {
			case none:
				return 0.0;
			case sigmoid:
				return sigmoidDerivative(x);
			default:
				return 0.0;
			}
			
		}
		
		private double sigmoid(double x) {
			return 1.0/(1.0 + Math.exp(-x));
		}
		
		private double sigmoidDerivative(double x) {
			return sigmoid(x)*(1-sigmoid(x));
		}
		
	}

	private int layerCount;
	private int inputSize;
	private int[] layerSize;
	private TransferFunction[] transferFunction;
	
	private double[][] layerOutput;
	private double[][] layerInput;
	private double[][] bias;
	private double[][] delta;
	private double[][] previousBiasDelta;
	
	private double[][][] weight;
	private double[][][] previousWeightDelta;
	
	public void learn(Collection<Grid> list, JProgressBar progressBar) {
		// TODO Auto-generated method stub

	}

	public void recognize(Grid grid) {
		// TODO Auto-generated method stub

	}

}
