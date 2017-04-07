package jfreechart;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.dial.DialBackground;
import org.jfree.chart.plot.dial.DialCap;
import org.jfree.chart.plot.dial.DialPlot;
import org.jfree.chart.plot.dial.DialTextAnnotation;
import org.jfree.chart.plot.dial.DialValueIndicator;
import org.jfree.chart.plot.dial.StandardDialFrame;
import org.jfree.chart.plot.dial.StandardDialRange;
import org.jfree.chart.plot.dial.StandardDialScale;
import org.jfree.data.general.DefaultValueDataset;
import org.jfree.data.general.ValueDataset;
import org.jfree.data.time.DynamicTimeSeriesCollection;
import org.jfree.data.time.Second;
import org.jfree.ui.GradientPaintTransformType;
import org.jfree.ui.StandardGradientPaintTransformer;

public class DesktopGUI{

    private static final String TITLE1 = "Acceleration Readings";
    private static final String TITLE2 = "Force Readings";
    private static final String RETRIEVE = "Retrieve Data";
    private static final String PAUSE = "Pause Data";
    private static final String SAVE1 = "Save Acceleration Graph";
    private static final String SAVE2 = "Save Force Graph";
    private static final int COUNT = 120;
    private static final int FAST = 100;
    private static final int SLOW = 500;
    private static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private Timer timer;

    //Constructs the GUI
    public DesktopGUI() throws SQLException, IOException {
    	
    	//Establish Intial Connection to Database, updates resultset to data in the sensor table
    	ResultSet r = connectionSensorData();
    	
    	//Creation of Accelerometer Graph and the configuration of the acceleration dataset
        DynamicTimeSeriesCollection accelset =  new DynamicTimeSeriesCollection(1, COUNT, new Second());
        accelset.setTimeBase(new Second(0, 0, 0, 1, 1, 2017));
        accelset.addSeries(readData(r, "acceleration"), 0, "Acceleration Data");
        JFreeChart chartA = createChart(TITLE1, "Time (hh:mm:ss)", "G-Forces", accelset, false, false, false);
        
        //Creation of Force Graph and the configuration of the force dataset
        DynamicTimeSeriesCollection forceset =  new DynamicTimeSeriesCollection(1, COUNT, new Second());
        forceset.setTimeBase(new Second(0, 0, 0, 1, 1, 2017));
        forceset.addSeries(readData(r, "force"), 0, "Force Data");
        JFreeChart chartF = createChart(TITLE2, "Time (hh:mm:ss)", "Force (N)", forceset, false, false, false);
                 
        //StartPause Button 
        JButton pauseStart = createPauseStart();
        
        //FastSlow Button
        JComboBox<String> fastSlow = fastSlowOption();
        
        //Save Acceleration Graph Button
        JButton saveAccel = saveAccelChart(chartA);
        
        //Save Force Graph Button
        JButton saveForce = saveForceChart(chartF);
        
        //Updatable textFields for Force and Acceleration
        JTextField forceField = createUpdateableTextField("", 400, 100);
        JTextField accelField = createUpdateableTextField("", 400, 100);
        
        //Creation of the separate panels to organize charts and other data
	//Subpanel1 is for the acceleration and force graph
	//Subpanel2 is for the speedometer and the acceleration and force textfields
      	JPanel subPanel1 = new JPanel(new BorderLayout());
      	subPanel1.setPreferredSize(new java.awt.Dimension(screenSize.width/2, screenSize.height));
	JPanel subPanel2 = new JPanel(new BorderLayout());
	subPanel2.setPreferredSize(new java.awt.Dimension(screenSize.width/2, screenSize.height));
	JFrame mainFrame = createMainWindow("Automated Injury Detection System");
		
	//Add panels to the main frame
	mainFrame.add(subPanel1, BorderLayout.WEST);
	mainFrame.add(subPanel2, BorderLayout.EAST);
	
	//Adding the graphs to subpanel1
	subPanel1.add(createGraphPanel(screenSize.width/2, 490, chartA, BorderLayout.NORTH), BorderLayout.NORTH);
	subPanel1.add(createButtonPanel(stopStart, fastSlow, saveAccel, saveForce), BorderLayout.CENTER);
	subPanel1.add(createGraphPanel(screenSize.width/2, 490, chartF, BorderLayout.SOUTH), BorderLayout.SOUTH);
	
	//Adding the speedometer and fields to subpanel2
	subPanel2.add(createGraphPanel(screenSize.width/2, screenSize.height/2, speedPanel(connectionSensorData()), BorderLayout.NORTH), BorderLayout.NORTH);
	subPanel2.add(createTextPanel(forceField, accelField, screenSize.width/2, screenSize.height/8), BorderLayout.SOUTH);		
        
	//Enable GUI
	runGUI(mainFrame);
	    
	//Updating resultset for sensor data from the sensor table
        ResultSet f = connectionSensorData();
        
        float[] newAccelData = new float[1];
	float[] newForceData = new float[1];

	//Creation of timer to updateGraph constantly
        timer = updateGraph(f, newAccelData, newForceData, accelset, forceset, accelField, forceField);
    }
    
    //Creation of a graph panel, where the graph will be situated
    //Panel is intialized to dimensions entered, type of chart being plotted and the layoutType
    private JPanel createGraphPanel(int width, int height, JFreeChart graph, String layoutType){
    	JPanel panel = new JPanel(new BorderLayout());
    	panel.setPreferredSize(new java.awt.Dimension(width, height));
	ChartPanel chart = new ChartPanel(graph);
	chart.setPreferredSize(new java.awt.Dimension(width, height));
        panel.add(chart, layoutType);	
        return panel;
    }
    
    //Creation of Button panel for the fast/slow button, start/stop button and the save button for the
    //acceleration and force graphs respectively. The buttons are placed in a flow layout
    private JPanel createButtonPanel(JButton s, JComboBox<String> c, JButton sA, JButton sF){
    	JPanel btPanel = new JPanel(new FlowLayout());
    	btPanel.add(s);
    	btPanel.add(c);
    	btPanel.add(sA);
    	btPanel.add(sF);
    	return btPanel;
    }
    
    //Creation of text panel for updateable acceleration and force textfields, panel
    //is intialized with width and height
    private JPanel createTextPanel(JTextField f, JTextField a, int width, int height){
    	JPanel textPanel = new JPanel(new FlowLayout());
    	f.setFont(new Font("Dialog", 1, 25));
    	a.setFont(new Font("Dialog", 1, 25));
    	f.setEditable(false);
    	a.setEditable(false);
    	a.setHorizontalAlignment(JTextField.CENTER);
    	f.setHorizontalAlignment(JTextField.CENTER);
    	textPanel.setPreferredSize(new java.awt.Dimension(width, height));
    	textPanel.add(a);
    	textPanel.add(f);
    	return textPanel;
    }
    
    //Start and pause button, essentially the graphs pause as the pause button is pressed and 
    //the graphs start as the start button is pressed
    private JButton createPauseStart() {
	JButton run = new JButton(PAUSE);
	run.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();
			if (PAUSE.equals(command)) {
				timer.stop();
				run.setText(RETRIEVE);
			} 
			else {
				timer.start();
				run.setText(PAUSE);
			}
			}
	});
	return run;
    }

    //Creates a dial chart with appropriate labels, dataset, the start range, end range, major tick increment, and number
    //of ticks between each major tick increment.
	private static JFreeChart createStandardDialChart(String title, String textAnnotation, ValueDataset dataset, double start, double end, double mti, int numTicks)
	{
		DialPlot dialplot = new DialPlot();
		dialplot.setDataset(dataset);
		dialplot.setDialFrame(new StandardDialFrame());
		dialplot.setBackground(new DialBackground());
		DialTextAnnotation dialtextannotation = new DialTextAnnotation(textAnnotation);
		dialtextannotation.setFont(new Font("Dialog", 1, 14));
		dialtextannotation.setRadius(0.69999999999999996D);
		dialplot.addLayer(dialtextannotation);
		DialValueIndicator dialvalueindicator = new DialValueIndicator(0);
		dialplot.addLayer(dialvalueindicator);
		StandardDialScale standarddialscale = new StandardDialScale(start, end, -120D, -300D, 10D, 4);
		standarddialscale.setMajorTickIncrement(mti);
		standarddialscale.setMinorTickCount(numTicks);
		standarddialscale.setTickRadius(0.88D);
		standarddialscale.setTickLabelOffset(0.14999999999999999D);
		standarddialscale.setTickLabelFont(new Font("Dialog", 0, 14));
		dialplot.addScale(0, standarddialscale);
		dialplot.addPointer(new org.jfree.chart.plot.dial.DialPointer.Pin());
		DialCap dialcap = new DialCap();
		dialplot.setCap(dialcap);
		return new JFreeChart(title, dialplot);
	}
	
	//Creates a speedometer dialplot
	public JFreeChart speedPanel(ResultSet r) throws SQLException{
		JFreeChart jfreechart = createStandardDialChart("Speedometer", "RPS", new DefaultValueDataset(findMaxSpeed(r)), 0D, 30D, 2D, 3);
		DialPlot dialplot = (DialPlot)jfreechart.getPlot();
		
		//Outer Dial Range
		StandardDialRange standarddialrange = new StandardDialRange(25D, 30D, Color.red);
		standarddialrange.setInnerRadius(0.52000000000000002D);
		standarddialrange.setOuterRadius(0.55000000000000004D);
		dialplot.addLayer(standarddialrange);
		
		//Middle Dial Range
		StandardDialRange standarddialrange1 = new StandardDialRange(15D, 25D, Color.orange);
		standarddialrange1.setInnerRadius(0.52000000000000002D);
		standarddialrange1.setOuterRadius(0.55000000000000004D);
		dialplot.addLayer(standarddialrange1);
		
		//Inner Dial Range
		StandardDialRange standarddialrange2 = new StandardDialRange(0D, 15D, Color.green);
		standarddialrange2.setInnerRadius(0.52000000000000002D);
		standarddialrange2.setOuterRadius(0.55000000000000004D);
		dialplot.addLayer(standarddialrange2);
		GradientPaint gradientpaint = new GradientPaint(new Point(), new Color(255, 255, 255), new Point(), new Color(170, 170, 220));
		DialBackground dialbackground = new DialBackground(gradientpaint);
		dialbackground.setGradientPaintTransformer(new StandardGradientPaintTransformer(GradientPaintTransformType.VERTICAL));
		dialplot.setBackground(dialbackground);
		dialplot.removePointer(0);
		org.jfree.chart.plot.dial.DialPointer.Pointer pointer = new org.jfree.chart.plot.dial.DialPointer.Pointer();
		pointer.setFillPaint(Color.yellow);
		dialplot.addPointer(pointer);
		return jfreechart;
	}
	
	//Finds the max Speed in the rps column of the sensor table, and returns the max speed
	private float findMaxSpeed(ResultSet r) throws SQLException{
		float max = 0;
		float temp = 0;
		while(r.next()){
			temp = r.getFloat("rps");
			if(temp > max){
				max = temp;
			}
		}
		return max;                                                                                                                                                                                                                                     
	}
	
	//Updates the graph and textfields based on new data read from the sensor table, adds it to the dataset and and advances the time
	private Timer updateGraph(ResultSet r, float[] newData1, float[] newData2, DynamicTimeSeriesCollection d1, DynamicTimeSeriesCollection d2, JTextField a, JTextField f){
		Timer t = new Timer(FAST, new ActionListener() {		
            @Override
            public void actionPerformed(ActionEvent e) {
					try {
						if(r.next()){
							newData1[0] = r.getFloat("acceleration");
							newData2[0] = r.getFloat("force");
							f.setText("Force: " + r.getString("force"));
							a.setText("Acceleration: " + r.getString("acceleration"));
							d1.appendData(newData1); 
							d1.advanceTime();
							d2.appendData(newData2); 
							d2.advanceTime();
						}
					} catch (SQLException e1) {
						System.out.println("Can't connect to database.");
					}
            }
        });
		return t;
	}
	
	//Dropdown menu to increase or decrease speed of graph plotting
	private JComboBox<String> fastSlowOption(){
		JComboBox<String> combo = new JComboBox<String>();
        combo.addItem("Fast");
        combo.addItem("Slow");
        combo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ("Fast".equals(combo.getSelectedItem())) {
                    timer.setDelay(FAST);
                } 
                else {
                    timer.setDelay(SLOW);
                }
            }
        });
        return combo;
	}
	
	//Creation of updateable text field with text text passed in
	//Text field is intialized with width and height
	private JTextField createUpdateableTextField(String text, int width, int height){
		JTextField tF = new JTextField();
		tF.setPreferredSize(new java.awt.Dimension(width, height));
		tF.setText(text);
		return tF;
	}
	
	//Saves the acceleration graph at position chosen into eclipse directory where the project 
	//has been created
	private JButton saveAccelChart(JFreeChart c){
		JButton save = new JButton(SAVE1);
		save.setActionCommand("Save Acceleration Chart");
		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if ("Save Acceleration Chart".equals(e.getActionCommand())) {
					File accelChart = new File("Acceleration Reading.jpeg");
					try {
						ChartUtilities.saveChartAsJPEG(accelChart, c, 680, 400);
					}
					catch (IOException e1) {
						System.out.println("Can't save the chart that you have requested.");
					}
				}
			}
		});
		return save;
	}

	//Saves the force graph at position chosen into eclipse directory where the project 
	//has been created
	private JButton saveForceChart(JFreeChart c) throws IOException{
		JButton save = new JButton(SAVE2);
		save.setActionCommand("Save Force Chart");
		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if ("Save Force Chart".equals(e.getActionCommand())) {
					File fChart = new File("Force Reading.jpeg");
					try {
						ChartUtilities.saveChartAsJPEG(fChart, c, 680, 400);
					}
					catch (IOException e1) {
						System.out.println("Can't save the chart that you have requested.");
					}
				}
			}
		});
		return save;
	}
	
	
	// Creation of main window frame with the entire screensize
	private JFrame createMainWindow(String t) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		JFrame frame = new JFrame(t);
		frame.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(screenSize.width, screenSize.height);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		return frame;
	}

    //Reads data from database with column name specified and stores into an array of floats
    private float[] readData(ResultSet r, String columnName) throws SQLException{
    	float[] a = new float[COUNT];
    	int i = 1;
    	while(r.next()){
    		a[i] = r.getFloat(columnName);
    	}
    	return a;
    }
    
    //Creates a graph based on the DynamicTimeSeriesCollection
    private JFreeChart createChart(String title, String xaxis, String yaxis, DynamicTimeSeriesCollection dataset, boolean legend, boolean tooltips, boolean url) {
        return ChartFactory.createTimeSeriesChart(title, xaxis, yaxis, dataset, legend, tooltips, url);
    }

    //Enables gui and makes gui visible
    private void runGUI(JFrame f){
    	f.pack();
    	f.setVisible(true);
    }
    
    //Connects to the database and returns a resultset with all of the data from the sensor table
    private ResultSet connectionSensorData() throws SQLException {
		Connection connect = null;
		try {

			String url = "jdbc:mysql://192.168.43.54:3306/projectdb";
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(url, "fooUser", "1234");
			System.out.println("Database connection to Sensor Table established");
		}
		catch (Exception e) {
			System.out.println("Can't connect to the database.");
		}
		Statement statement = connect.createStatement();
		return statement.executeQuery("select * from sensordata");
	}
    
    //Starts the timer
    public void start() {
        timer.start();
    }

    //Runs the GUI
    public static void main(final String[] args) throws SQLException {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
					DesktopGUI app = new DesktopGUI();
					app.start();
				} catch (SQLException e) {
					System.out.println("Database connection failed!");
				}
                catch (IOException ce) {
					System.out.println("Can't save Graphs!");
				}
                
            }
        });
    }
}
