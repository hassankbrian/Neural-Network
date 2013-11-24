import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
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
	private JTextField addTextField;
	
	public PatternRecognitionFrame(int width, int height) {
		WIDTH = width;
		this.height = height;
		this.buttonHeight = 5*height/9-2*WIDTH/3;
		
		grids = new HashMap<Grid,String>();
		patterns = new HashMap<JButton,Grid>();
		
		framePanel = new JPanel();
		framePanel.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
		framePanel.setPreferredSize(new Dimension(WIDTH,height));
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
			}
		});
		emptyTableButton.setPreferredSize(new Dimension(width/3,buttonHeight));
		tablePanel.add(emptyTableButton);
		framePanel.add(tablePanel);
		
		gridPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,0,0));
		gridPanel.setPreferredSize(new Dimension((int)(2*WIDTH/3),5*height/9));
		grid = new Grid(2*WIDTH/3,10,10);
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
		optionsPanel.setPreferredSize(new Dimension(WIDTH,4*height/9));
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
		addTextField = new JTextField("Pattern...");
		addTextField.setPreferredSize(new Dimension(2*WIDTH/3,buttonHeight));
		addTextField.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				addTextField.setText("");
			}
		});
		optionsPanel.add(addTextField);
		
		framePanel.add(optionsPanel);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		//setResizable(false);
		pack();
	}
	
	public static void main(String[] args) {
		PatternRecognitionFrame frame = new PatternRecognitionFrame(750,1000);
		frame.setVisible(true);
	}
	
}
