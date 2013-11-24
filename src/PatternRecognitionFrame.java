import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JTextField;


public class PatternRecognitionFrame extends JFrame {

	private Grid grid;
	private final int WIDTH;
	private int height;
	private int buttonHeight;
	private Map<Grid,String> grids;
	private Map<JButton,Grid> patterns;
	
	private JPanel framePanel;
	private JPanel gridPanel;
	private JButton clearButton;
	private JPanel tablePanel;
	private JPanel viewPort;
	private JScrollPane table;
	private JButton emptyTableButton;
	private JPanel optionsPanel;
	private JButton addButton;
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
	private JButton learnButton;
	private JButton recognizeButton;

	
	public PatternRecognitionFrame(int width, int height) {
		WIDTH = width;
		this.height = height;
		this.buttonHeight = 5*height/9-2*WIDTH/3;
		
		grids = new HashMap<Grid,String>();
		patterns = new HashMap<JButton,Grid>();
		
		framePanel = new JPanel();
		framePanel.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
		framePanel.setPreferredSize(new Dimension(WIDTH,height-2*width/3+5*buttonHeight - height/100));
		add(framePanel);
		
		tablePanel = new JPanel(new FlowLayout(FlowLayout.CENTER,0,0));
		tablePanel.setPreferredSize(new Dimension(WIDTH/3,(5*height)/9));
		viewPort = new JPanel();
		viewPort.setLayout(new BoxLayout(viewPort,BoxLayout.Y_AXIS));
		table = new JScrollPane(viewPort);
		table.setPreferredSize(new Dimension(WIDTH/3,2*WIDTH/3));
		tablePanel.add(table);
		emptyTableButton = new JButton("Empty Table");
		emptyTableButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				viewPort.removeAll();
				viewPort.revalidate();
				viewPort.repaint();
			}
		});
		emptyTableButton.setPreferredSize(new Dimension(width/3,buttonHeight));
		tablePanel.add(emptyTableButton);
		framePanel.add(tablePanel);
		
		gridPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,0,0));
		gridPanel.setPreferredSize(new Dimension((int)(2*WIDTH/3),5*height/9));
		grid = new Grid(2*WIDTH/3,7,5);
		gridPanel.add(grid);
		clearButton = new JButton("Clear");
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				grid.clear();
			}
		});
		clearButton.setPreferredSize(new Dimension(2*WIDTH/3,buttonHeight));
		gridPanel.add(clearButton);
		framePanel.add(gridPanel);
		
		optionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,0,0));
		optionsPanel.setPreferredSize(new Dimension(WIDTH,4*buttonHeight));
		addButton = new JButton("Add");
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Grid tempGrid = new Grid(grid);
				grids.put(tempGrid,addTextField.getText());
				JButton tableButton = new JButton(addTextField.getText());
				patterns.put(tableButton,tempGrid);
				tableButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						grid.update(patterns.get(e.getSource()));
					}
				});
				tableButton.setMaximumSize(new Dimension(WIDTH/3-3,buttonHeight/2));
				tableButton.setMinimumSize(new Dimension(WIDTH/3-3,buttonHeight/2));
				viewPort.add(tableButton);
				viewPort.updateUI();
			}
		});
		addButton.setPreferredSize(new Dimension(WIDTH/3,buttonHeight));
		optionsPanel.add(addButton);
		addPanel = new JPanel();
		addPanel.setPreferredSize(new Dimension(2*WIDTH/3,buttonHeight));
		addLabel = new JLabel("Pattern ID:");
		addLabel.setPreferredSize(new Dimension(2*WIDTH/3,buttonHeight/3));
		addPanel.add(addLabel);
		addTextField = new JTextField();
		addTextField.setPreferredSize(new Dimension(2*WIDTH/3,buttonHeight/2));
		addPanel.add(addTextField);
		optionsPanel.add(addPanel);
		hiddenLayerLabel = new JLabel("Hidden Layer #:");
		hiddenLayerLabel.setPreferredSize(new Dimension(width/4,buttonHeight/3));
		optionsPanel.add(hiddenLayerLabel);
		learningRateLabel = new JLabel("Learning Rate:");
		learningRateLabel.setPreferredSize(new Dimension(width/4,buttonHeight/3));
		optionsPanel.add(learningRateLabel);
		momentumLabel = new JLabel("Momentum:");
		momentumLabel.setPreferredSize(new Dimension(width/4,buttonHeight/3));
		optionsPanel.add(momentumLabel);
		errorToleranceLabel = new JLabel("Error Tolerance:");
		errorToleranceLabel.setPreferredSize(new Dimension(width/4,buttonHeight/3));
		optionsPanel.add(errorToleranceLabel);
		hiddenLayerField = new JTextField();
		hiddenLayerField.setPreferredSize(new Dimension(width/4,buttonHeight/2));
		optionsPanel.add(hiddenLayerField);
		learningRateField = new JTextField();
		learningRateField.setPreferredSize(new Dimension(width/4,buttonHeight/2));
		optionsPanel.add(learningRateField);
		momentumField = new JTextField();
		momentumField.setPreferredSize(new Dimension(width/4,buttonHeight/2));
		optionsPanel.add(momentumField);
		errorToleranceField = new JTextField();
		errorToleranceField.setPreferredSize(new Dimension(width/4,buttonHeight/2));
		optionsPanel.add(errorToleranceField);
		progressBar = new JProgressBar();
		progressBar.setPreferredSize(new Dimension(width,buttonHeight));
		progressBar.setString("Progress...");
		progressBar.setStringPainted(true);
		optionsPanel.add(progressBar);
		learnButton = new JButton("Learn");
		learnButton.setPreferredSize(new Dimension(width/2,buttonHeight));
		learnButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//TODO
			}
		});
		optionsPanel.add(learnButton);
		recognizeButton = new JButton("Recognize");
		recognizeButton.setPreferredSize(new Dimension(width/2,buttonHeight));
		recognizeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//TODO
			}
		});
		optionsPanel.add(recognizeButton);
		framePanel.add(optionsPanel);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		pack();
	}
	
	public static void main(String[] args) {
		PatternRecognitionFrame frame = new PatternRecognitionFrame(600,800);
		frame.setVisible(true);
	}
	
}

