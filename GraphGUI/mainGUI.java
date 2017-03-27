package jFreeChart;
import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.dial.ArcDialFrame;
import org.jfree.chart.plot.dial.DialBackground;
import org.jfree.chart.plot.dial.DialCap;
import org.jfree.chart.plot.dial.DialPlot;
import org.jfree.chart.plot.dial.DialTextAnnotation;
import org.jfree.chart.plot.dial.DialValueIndicator;
import org.jfree.chart.plot.dial.StandardDialRange;
import org.jfree.chart.plot.dial.StandardDialScale;
import org.jfree.data.general.DefaultValueDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.GradientPaintTransformType;
import org.jfree.ui.StandardGradientPaintTransformer;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;


public class mainGUI extends JFrame implements ChangeListener{
	
	//Sliders are used for test purposes only until data is read
	JSlider slider1, slider2;
	DefaultValueDataset dataset3, dataset4;
	
	public mainGUI(){
		
	}
	
	//Temporary Method to change the value on the dial, later implementation will have data
	//read from the sensors
	public void stateChanged(ChangeEvent cE){
		dataset3.setValue(new Integer(slider1.getValue()));
		dataset4.setValue(new Integer(slider2.getValue()));
	}
	
	
	//Creates base frame for the application
	private void createFrame(){
		
		//Creation of main window frame
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		JFrame frame = new JFrame("Automated Injury Detection System");
		frame.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(screenSize.width, screenSize.height);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		//Creation of the separate panels to organize charts and other data
		JPanel subPanel1 = new JPanel(new BorderLayout());
		subPanel1.setPreferredSize(new Dimension(screenSize.width/2,screenSize.height));
		JPanel subPanel2 = new JPanel(new BorderLayout());
		subPanel2.setPreferredSize(new Dimension(screenSize.height/2,screenSize.height/2));
		JPanel subPanel3 = new JPanel(new GridLayout(2, 2));
		subPanel3.add(new JLabel("Outer Needle (rpm)"));
		subPanel3.add(new JLabel("Inner Needle (rps)"));
		frame.add(subPanel1, BorderLayout.WEST);
		frame.add(subPanel2, BorderLayout.EAST);
		
		//Accelerometer Line Graph
		TimeSeries accelR = new TimeSeries("Accelerometer");
		TimeSeriesCollection dataset1 = new TimeSeriesCollection(accelR);
		//dataset1.addSeries();
		JFreeChart accelGraph = ChartFactory.createTimeSeriesChart("Accelerometer Reading", "Time (ms)", "G-Forces", dataset1, false, false, false);	
		ChartPanel panel1 = new ChartPanel(accelGraph);
		panel1.setMouseWheelEnabled(true);
		panel1.setMouseZoomable(true);
		subPanel1.add(panel1, BorderLayout.NORTH);
		
		//Force Line Graph
		TimeSeries forceData = new TimeSeries("Force Sensor");
		TimeSeriesCollection dataset2 = new TimeSeriesCollection(forceData);
		//dataset2.addSeries();
		JFreeChart forceGraph = ChartFactory.createTimeSeriesChart("Force Sensor Reading", "Time (ms)", "Force (N)", dataset2, false, false, false);
		ChartPanel panel2 = new ChartPanel(forceGraph);
		panel2.setMouseWheelEnabled(true);
		panel2.setMouseZoomable(true);
		subPanel1.add(panel2, BorderLayout.SOUTH);
		
		//Revolution per Seconds/Minutes (RPS/RPM) Graph
		
		JFreeChart dialPlot =  buildDialPlot(dataset3, dataset4);
		ChartPanel panel3  = new ChartPanel(dialPlot);
		panel3.setPreferredSize(new Dimension(400, 400));
		subPanel2.add(panel3, BorderLayout.NORTH);
		
		//Temporary Slider for the RPM
		slider1 = new JSlider(0, 100);
		slider1.setMajorTickSpacing(20);
		slider1.setPaintLabels(true);
		slider1.setPaintTicks(true);
		slider1.addChangeListener(this);
		subPanel3.add(slider1);
		
		//Temporary Slider for the RPS
		slider2 = new JSlider(0, 200);
		slider2.setMajorTickSpacing(10);
		slider2.setPaintLabels(true);
		slider2.setPaintTicks(true);
		slider2.addChangeListener(this);
		subPanel3.add(slider2);

		subPanel2.add(subPanel3, BorderLayout.SOUTH);
	
		frame.pack();
		frame.setVisible(true);
	}
	
	private JFreeChart buildDialPlot(DefaultValueDataset d1, DefaultValueDataset d2){
		DialPlot plot = new DialPlot();
		d1 = new DefaultValueDataset(10D);
		d2 = new DefaultValueDataset(50D);
		plot.setView(0.0D, 0.0D, 1.0D, 1.0D);
		plot.setDataset(0, d1);
		plot.setDataset(1, d2);
		
		//Set Background 
		GradientPaint gradient = new GradientPaint(new Point(), new Color(255, 255, 255), new Point(), new Color(170, 170, 220));
		DialBackground background = new DialBackground(gradient);
		background.setGradientPaintTransformer(new StandardGradientPaintTransformer(GradientPaintTransformType.VERTICAL));
		plot.setBackground(background);
		
		//Dial Info
		DialTextAnnotation dta = new DialTextAnnotation("Speedometer");
		dta.setFont(new Font("Dialog", 1, 14));
		dta.setRadius(0.69999999999999996D);
		plot.addLayer(dta);
		
		//Outer Dial (rpm)
		DialValueIndicator dvi = new DialValueIndicator(0);
		dvi.setFont(new Font("Dialog", 0, 10));
		dvi.setOutlinePaint(Color.darkGray);
		dvi.setRadius(0.59999999999999998D);
		dvi.setAngle(-103D);
        plot.addLayer(dvi);
        
        //Inner Dial (rps)
        DialValueIndicator dialvalueindicator1 = new DialValueIndicator(1);
        dialvalueindicator1.setFont(new Font("Dialog", 0, 10));
        dialvalueindicator1.setOutlinePaint(Color.red);
        dialvalueindicator1.setRadius(0.59999999999999998D);
        dialvalueindicator1.setAngle(-77D);
        plot.addLayer(dialvalueindicator1);
		
        //Outer Dial Scale
        StandardDialScale sds = new StandardDialScale(0D, 100D, -120D, -300D, 10D, 4);
        sds.setTickRadius(0.88D);
        sds.setTickLabelOffset(0.14999999999999999D);
        sds.setTickLabelFont(new Font("Dialog", 0, 14));
        plot.addScale(0, sds);
        
        //Inner Dial Scale
        StandardDialScale sds1 = new StandardDialScale(0.0D, 200D, -120D, -300D, 10D, 4);
        sds1.setTickRadius(0.5D);
        sds1.setTickLabelOffset(0.14999999999999999D);
        sds1.setTickLabelFont(new Font("Dialog", 0, 10));
        sds1.setMajorTickPaint(Color.red);
        sds1.setMinorTickPaint(Color.red);
        plot.addScale(1, sds1);
        
        plot.mapDatasetToScale(1, 1);
        
        StandardDialRange sdr = new StandardDialRange(90D, 100D, Color.blue);
        sdr.setScaleIndex(1);
        sdr.setInnerRadius(0.58999999999999997D);
        sdr.setOuterRadius(0.58999999999999997D);
        plot.addLayer(sdr);
        
        org.jfree.chart.plot.dial.DialPointer.Pin pin = new org.jfree.chart.plot.dial.DialPointer.Pin(1);
        pin.setRadius(0.55000000000000004D);
        plot.addPointer(pin);
        
        DialCap dc= new DialCap();
        dc.setRadius(0.10000000000000001D);
        plot.setCap(dc);
		return new JFreeChart(plot);
	}
		
	public static void main(String[] args){
		mainGUI main = new mainGUI();
		main.createFrame();	
	}
}
