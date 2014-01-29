/*
 * Here we instantiate all the swing design for the UI.
 *
 * It's quite messy but it is pretty straight-forward if you know swing.
 * We have the grid which is supplied dimensions and basically determines 
 * The co-domain of the neural network. We also have textfields for attributes
 * such as tolerance that will be supplied to the neural network and we will also
 * have a sidebar with the patterns created. 
 * 
 * 
 *  
 * @author Brian Hassan 
 * @version 1.0 
 */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicProgressBarUI;


public class PatternRecognitionFrame extends JFrame {

	private Grid grid;
	private final int WIDTH;
	private int HEIGHT;
	private int buttonHeight;
	private Map<Grid,String> grids;
	private Map<MButton,Grid> patterns;
	private Color black = Color.BLACK;
	private Color green = Color.GREEN;
	private Color white = Color.WHITE;
	
	private JPanel framePanel;
	private JPanel gridPanel;
	private MButton clearButton;
	private JPanel tablePanel;
	private JPanel viewPort;
	private JScrollPane table;
	private MButton emptyTableButton;
	private JPanel optionsPanel;
	private MButton addButton;
	private JLabel addLabel;
	private JTextField addTextField;
	private JPanel addPanel;
	private JLabel hiddenLayerLabel;
	private JTextField hiddenLayerField;
	private JLabel learningRateLabel;
	private JTextField learningRateField;
	private JLabel momentumLabel;
	private JTextField momentumField;
	private JLabel errorToleranceLabel;
	private JTextField errorToleranceField;
	private JProgressBar progressBar;
	private MButton learnButton;
	private MButton recognizeButton;
	
	private BackPropagationNeuralNetwork bpnn;
	private List<String> patternStringList;
	private List<Grid> patternGridList;
	private int[] layerSizes;
	BackPropagationNeuralNetwork.TransferFunction[] transferFunctions;

	
	public PatternRecognitionFrame(int width, int height) {
		WIDTH = width;
		this.HEIGHT = height;
		this.buttonHeight = 5*height/9-2*WIDTH/3;
		
		this.setTitle("Back Propagation Neural Network - Pattern Recognition Demo");
		
		patternStringList = new ArrayList<String>();
		patternGridList = new ArrayList<Grid>();
		
		grids = new HashMap<Grid,String>();
		patterns = new HashMap<MButton,Grid>();
		
		framePanel = new JPanel();
		framePanel.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
		framePanel.setPreferredSize(new Dimension(WIDTH,HEIGHT-2*width/3+5*buttonHeight - height/100));
		add(framePanel);
		
		tablePanel = new JPanel(new FlowLayout(FlowLayout.CENTER,0,0));
		tablePanel.setPreferredSize(new Dimension(WIDTH/3,(5*HEIGHT)/9));
		viewPort = new JPanel();
		viewPort.setBackground(black);
		viewPort.setLayout(new BoxLayout(viewPort,BoxLayout.Y_AXIS));
		table = new JScrollPane(viewPort);
		table.setPreferredSize(new Dimension(WIDTH/3,2*WIDTH/3));
		tablePanel.add(table);
		emptyTableButton = new MButton("Empty Table");
		emptyTableButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				viewPort.removeAll();
				viewPort.revalidate();
				viewPort.repaint();
				patternStringList.clear();
				patternGridList.clear();
			}
		});
		emptyTableButton.setPreferredSize(new Dimension(width/3,buttonHeight));
		tablePanel.add(emptyTableButton);
		framePanel.add(tablePanel);
		
		gridPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,0,0));
		gridPanel.setPreferredSize(new Dimension((int)(2*WIDTH/3),5*HEIGHT/9));
		grid = new Grid(2*WIDTH/3,1,2);
		gridPanel.add(grid);
		clearButton = new MButton("Clear");
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				grid.clear();
			}
		});
		clearButton.setPreferredSize(new Dimension(2*WIDTH/3,buttonHeight));
		gridPanel.add(clearButton);
		framePanel.add(gridPanel);
		
		optionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,0,0));
		optionsPanel.setBackground(black);
		optionsPanel.setPreferredSize(new Dimension(WIDTH,4*buttonHeight));
		addButton = new MButton("Add");
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Grid tempGrid = new Grid(grid);
				grids.put(tempGrid,addTextField.getText());
				MButton tableButton = new MButton(addTextField.getText());
				patternStringList.add(addTextField.getText());
				patternGridList.add(tempGrid);
				patterns.put(tableButton,tempGrid);
				tableButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						grid.update(patterns.get(e.getSource()));
					}
				});
				tableButton.setMaximumSize(new Dimension(WIDTH/3-3,buttonHeight/2));
				viewPort.add(tableButton);
				viewPort.updateUI();
			}
		});
		addButton.setPreferredSize(new Dimension(WIDTH/3,buttonHeight));
		optionsPanel.add(addButton);
		addPanel = new JPanel();
		addPanel.setPreferredSize(new Dimension(2*WIDTH/3,buttonHeight));
		addLabel = new JLabel("Pattern ID:");
		addPanel.setBackground(black);
		addLabel.setForeground(green);
		addLabel.setPreferredSize(new Dimension(2*WIDTH/3,buttonHeight/3));
		addPanel.add(addLabel);
		addTextField = new JTextField();
		addTextField.setPreferredSize(new Dimension(2*WIDTH/3,buttonHeight/2));
		addPanel.add(addTextField);
		optionsPanel.add(addPanel);
		hiddenLayerLabel = new JLabel("Hidden Layer #:");
		hiddenLayerLabel.setForeground(green);
		hiddenLayerLabel.setPreferredSize(new Dimension(width/4,buttonHeight/3));
		optionsPanel.add(hiddenLayerLabel);
		learningRateLabel = new JLabel("Learning Rate:");
		learningRateLabel.setForeground(green);
		learningRateLabel.setPreferredSize(new Dimension(width/4,buttonHeight/3));
		optionsPanel.add(learningRateLabel);
		momentumLabel = new JLabel("Momentum:");
		momentumLabel.setForeground(green);
		momentumLabel.setPreferredSize(new Dimension(width/4,buttonHeight/3));
		optionsPanel.add(momentumLabel);
		errorToleranceLabel = new JLabel("Error Tolerance:");
		errorToleranceLabel.setForeground(green);
		errorToleranceLabel.setPreferredSize(new Dimension(width/4,buttonHeight/3));
		optionsPanel.add(errorToleranceLabel);
		hiddenLayerField = new JTextField("4");
		hiddenLayerField.setPreferredSize(new Dimension(width/4,buttonHeight/2));
		optionsPanel.add(hiddenLayerField);
		learningRateField = new JTextField("0.1");
		learningRateField.setPreferredSize(new Dimension(width/4,buttonHeight/2));
		optionsPanel.add(learningRateField);
		momentumField = new JTextField("0.15");
		momentumField.setPreferredSize(new Dimension(width/4,buttonHeight/2));
		optionsPanel.add(momentumField);
		errorToleranceField = new JTextField("0.00001");
		errorToleranceField.setPreferredSize(new Dimension(width/4,buttonHeight/2));
		optionsPanel.add(errorToleranceField);
		progressBar = new JProgressBar();
		progressBar.setBackground(black);
		progressBar.setForeground(green);
		progressBar.setPreferredSize(new Dimension(width,buttonHeight));
		progressBar.setString("Progress...");
		progressBar.setStringPainted(true);
		progressBar.setUI(new BasicProgressBarUI() {
			protected Color getSelectionBackground() {
				return green;
			}
			protected Color getSelectionForeground() {
				return black;
			}
		});
		optionsPanel.add(progressBar);
		learnButton = new MButton("Learn");
		learnButton.setPreferredSize(new Dimension(width/2,buttonHeight));
		learnButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				layerSizes = new int[Integer.parseInt(hiddenLayerField.getText())+2];
				layerSizes[0] = grid.getRows()*grid.getCols();
				for (int i = 1; i < layerSizes.length-1; i++) {
					layerSizes[i] = 10;
				}
				layerSizes[layerSizes.length-1] = 1;
				transferFunctions = new BackPropagationNeuralNetwork.TransferFunction[layerSizes.length];
				transferFunctions[0] = BackPropagationNeuralNetwork.TransferFunction.none;
				for (int i = 1; i < transferFunctions.length-1; i++) {
					transferFunctions[i] = BackPropagationNeuralNetwork.TransferFunction.sigmoid;
				}
				transferFunctions[transferFunctions.length-1] = BackPropagationNeuralNetwork.TransferFunction.linear;
				bpnn = new BackPropagationNeuralNetwork(layerSizes, transferFunctions);
				progressBar.setValue(0);
				bpnn.learn(patternGridList,Double.parseDouble(learningRateField.getText()),Double.parseDouble(momentumField.getText()),Double.parseDouble(errorToleranceField.getText()), progressBar);
			}
		});
		optionsPanel.add(learnButton);
		recognizeButton = new MButton("Recognize");
		recognizeButton.setPreferredSize(new Dimension(width/2,buttonHeight));
		recognizeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null,"Pattern Recognized: " + patternStringList.get(bpnn.recognize(grid)));
			}
		});
		optionsPanel.add(recognizeButton);
		framePanel.add(optionsPanel);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		pack();
	}
	
	private class MButton extends JButton {
		public MButton(String text) {
			super(text);
			setBackground(Color.BLACK);
			setForeground(Color.GREEN);
			setBorder(new LineBorder(Color.WHITE));
		}
	}
	
	public static void main(String[] args) {
		PatternRecognitionFrame frame = new PatternRecognitionFrame(600,800);
		frame.setVisible(true);
	}
	
}