package com.socioboard.f_board_pro.fragments;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.Arrays;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.androidplot.xy.XYStepMode;
import com.socioboard.f_board_pro.R;

public class StatisticsFragment extends Fragment {

	private XYPlot xyPlot;

	final String[] mMonths = new String[] {
			"Sun","Mon","Tue", "Wed","Thur", "Fri","Sat"
	};
	
 
	View rootview;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootview = inflater.inflate(R.layout.statistic_layout, container, false);

		initView();

		return rootview;
	}

	public void initView()
	{
        // initialize our XYPlot reference:
        xyPlot = (XYPlot) rootview.findViewById(R.id.xyplot);
 
        Number[] income =  {100, 2000, 2000, 2000, 2100, 2100, 1200 };
 
        // Converting the above income array into XYSeries
        XYSeries incomeSeries = new SimpleXYSeries(Arrays.asList(income),                   // array => list
            SimpleXYSeries.ArrayFormat.Y_VALS_ONLY ,     // Y_VALS_ONLY means use the element index as the x value
            "Weeks");                                  // Title of this series
 
 
 
        LineAndPointFormatter infrr = new LineAndPointFormatter(Color.rgb(0, 0, 255),
        		Color.rgb(200, 200, 200), null, null);
        // Create a formatter to format Line and Point of expense series
        
 
        // add income series to the xyplot:
        xyPlot.addSeries(incomeSeries, infrr);
 
     // Formatting the Domain Values ( X-Axis )
        xyPlot.setDomainValueFormat(new Format() {
 
            @Override
            public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
                return new StringBuffer( mMonths[ ( (Number)obj).intValue() ]  );
            }
 
            @Override
            public Object parseObject(String source, ParsePosition pos) {
                return null;
            }
        });
 
 
        xyPlot.setDomainLabel("");
        
        xyPlot.setRangeLabel("Profile status");
 
        // Increment X-Axis by 1 value
        xyPlot.setDomainStep(XYStepMode.INCREMENT_BY_VAL, 1);
 
        xyPlot.getGraphWidget().setRangeLabelWidth(50);
 
        // Reduce the number of range labels
        xyPlot.setTicksPerRangeLabel(1);
        
 
        // Reduce the number of domain labels
        xyPlot.setTicksPerDomainLabel(1);
 
        // Remove all the developer guides from the chart
       
      
    }
 
   
	 
}
