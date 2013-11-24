import java.util.Collection;

import javax.swing.JProgressBar;


public interface NeuralNetwork {

	public void learn(Collection<Grid> list, JProgressBar progressBar);
	
	public void recognize(Grid grid);
		
}
