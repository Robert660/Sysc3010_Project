package jfreechart;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
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
import javax.swing.Timer;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.DynamicTimeSeriesCollection;
import org.jfree.data.time.Second;
import org.jfree.data.xy.XYDataset;

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

    public DesktopGUI() throws SQLException, IOException {
    	
        DynamicTimeSeriesCollection accelset =  new DynamicTimeSeriesCollection(1, COUNT, new Second());
        accelset.setTimeBase(new Second(0, 0, 0, 1, 1, 2017));
        accelset.addSeries(accelData(), 0, "Acceleration Data");
        JFreeChart chartA = createAccelChart(accelset);

        DynamicTimeSeriesCollection forceset =  new DynamicTimeSeriesCollection(1, COUNT, new Second());
        forceset.setTimeBase(new Second(0, 0, 0, 1, 1, 2017));
        forceset.addSeries(forceData(), 0, "Force Data");
        JFreeChart chartF = createForceChart(forceset);
        
        
        
        //StartStop Button
        JButton stopStart = createStopStart();
        
        //FastSlow Button
        JComboBox<String> fastSlow = fastSlowOption();
        
        //Save Acceleration Button
        JButton saveAccel = saveAccelChart(chartA);
        
        //Save Force Button
        JButton saveForce = saveForceChart(chartF);

        //Creation of the separate panels to organize charts and other data
      	JPanel subPanel1 = new JPanel(new BorderLayout());
      	subPanel1.setPreferredSize(new java.awt.Dimension(screenSize.width/2, screenSize.height));
		JPanel subPanel2 = new JPanel(new BorderLayout());
		JFrame mainFrame = createMainWindow("Automated Injury Detection System");
		mainFrame.add(subPanel1, BorderLayout.WEST);

		//Creation of left panel for graphs
		subPanel1.add(createTopGraphPanel(screenSize, chartA, BorderLayout.NORTH), BorderLayout.NORTH);
		subPanel1.add(createButtonPanel(stopStart, fastSlow, saveAccel, saveForce), BorderLayout.CENTER);
		subPanel1.add(createTopGraphPanel(screenSize, chartF, BorderLayout.SOUTH), BorderLayout.SOUTH);
				
        runGUI(mainFrame);
        
        float[] newAccelData = new float[1];
		float[] newForceData = new float[1];
    	ResultSet r = connection();
    	
        timer = updateGraph(r, newAccelData, newForceData, accelset, forceset);
    }
    
    private JPanel createTopGraphPanel(Dimension size, JFreeChart graph, String layoutType){
    	JPanel panel = new JPanel(new BorderLayout());
    	panel.setPreferredSize(new java.awt.Dimension(size.width/2, 490));
		ChartPanel chart = new ChartPanel(graph);
		chart.setPreferredSize(new java.awt.Dimension(size.width/2, 490));
        panel.add(chart, layoutType);	
        return panel;
    }
    
    private JPanel createButtonPanel(JButton s, JComboBox<String> c, JButton sA, JButton sF){
    	JPanel btPanel = new JPanel(new FlowLayout());
    	btPanel.add(s);
    	btPanel.add(c);
    	btPanel.add(sA);
    	btPanel.add(sF);
    	return btPanel;
    }
    
	private JButton createStopStart() {
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
	
	private Timer updateGraph( ResultSet r, float[] newData1, float[] newData2, DynamicTimeSeriesCollection d1, DynamicTimeSeriesCollection d2){
		Timer t = new Timer(FAST, new ActionListener() {		
            @Override
            public void actionPerformed(ActionEvent e) {
					try {
						if(r.next()){
							newData1[0] = r.getFloat("acceleration");
							newData2[0] = r.getFloat("force");
							System.out.println(r.getFloat("acceleration"));
							System.out.println(r.getFloat("force"));
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
	
	// Creation of main window frame
	private JFrame createMainWindow(String t) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		JFrame frame = new JFrame(t);
		frame.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(screenSize.width, screenSize.height);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		return frame;
	}
	
    private float[] accelData() throws SQLException{
    	ResultSet r = connection();
    	float[] a = new float[COUNT];
    	int i = 1;
    	while(r.next()){
    		a[i] = r.getFloat("acceleration");
    	}
    	return a;
    }
    
    private float[] forceData() throws SQLException{
    	ResultSet r = connection();
    	float[] b = new float[COUNT];
    	int i = 1;
    	while(r.next()){
    		b[i] = r.getFloat("force");
    	}
    	return b;
    }
    
    private JFreeChart createAccelChart(final XYDataset dataset) {
        return ChartFactory.createTimeSeriesChart(TITLE1, "Time (hh:mm:ss)", "G-Forces", dataset, false, false, false);
    }
    
    private JFreeChart createForceChart(final XYDataset dataset) {
        return ChartFactory.createTimeSeriesChart(TITLE2, "Time (hh:mm:ss)", "Force (N)", dataset, false, false, false);
    }

    private void runGUI(JFrame f){
    	f.pack();
    	f.setVisible(true);
    }
    
    //Establishes connection with MySQL Database
    private ResultSet connection() throws SQLException {
	    	//Create a connection
		Connection connect = null;
	    
	    	//Connect to the database that you specify
		try {
			String url = "jdbc:mysql://192.168.43.54:3306/projectdb";
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(url, "fooUser", "1234");
			System.out.println("Database connection established.");
		}
		catch (Exception e) {
			System.out.println("Database connection has not been established!");
			System.out.println("There has been an error.");
		}
	    
		Statement statement = connect.createStatement();
		return statement.executeQuery("select * from sensordata");
	}
    
    //Auto invoke
    public void start() {
        timer.start();
    }

    public static void main(final String[] args) throws SQLException {
    	//test2 demo = new test2();
    	
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
					DesktopGUI app = new DesktopGUI();
					app.start();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                
            }
        });
    }
}
