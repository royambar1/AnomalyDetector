package test;

import java.util.ArrayList;
import java.util.List;

public class SimpleAnomalyDetector implements TimeSeriesAnomalyDetector {

	public float getThreshold() {
		return threshold;
	}

	public void setThreshold(float threshold) {
		this.threshold = threshold;
	}

	float threshold = (float) 0.90;


	List<CorrelatedFeatures> anomalyThresholdList = new ArrayList<>();

	public float[] objectToFloat(ArrayList<Float> M){
		int size = M.size();
		float[] floatList = new float[size];
		for(int i = 0; i < size; i++){
			floatList[i] = M.get(i);
		}
		return floatList;
	}

	public Point[] pointsFromColumns(TimeSeries ts,String a, String b){
		int size = ts.csvTable.get(a).size();
		Point[] points = new Point[size];
		for (int i = 0; i < size; i++){
			points[i] = new Point(ts.csvTable.get(a).get(i),ts.csvTable.get(b).get(i));
		}
		return points;
	}

	public float maxDeviation(Point[] points, Line line){
		float max = 0;
		for(int i = 0; i < points.length; i++)
		{
			float x = StatLib.dev(points[i],line);
			if(x > max)
				max = x;
		}
		return max*1.1f;
	}

	@Override
	public void learnNormal(TimeSeries ts) {
		float maxPearson  = 0;
		String firstColumn = null;
		String secondColumn = null;
		String maxFirstColumn = null;
		String maxSecondColumn = null;
		float temp = 0;
		int sizeTs = ts.csvTable.size();
		for (int i = 0; i < sizeTs; i++) {
			for (int j = i+1; j < sizeTs; j++) {
				firstColumn = ts.headers[i];
				secondColumn = ts.headers[j];
				float p = Math.abs(StatLib.pearson(objectToFloat(ts.csvTable.get(firstColumn)), objectToFloat(ts.csvTable.get(secondColumn))));
				if (p >= this.threshold && p > temp) {
					maxPearson = p;
					temp = p;
					maxFirstColumn = ts.headers[i];
					maxSecondColumn = ts.headers[j];
				}
			}
			if(temp != 0) {
				Point[] points = pointsFromColumns(ts, maxFirstColumn, maxSecondColumn);
				Line line = StatLib.linear_reg(points);
				float maxDeviation = maxDeviation(points, line);
				CorrelatedFeatures A = new CorrelatedFeatures(maxFirstColumn, maxSecondColumn, maxPearson, line, maxDeviation);
				anomalyThresholdList.add(A);
			}
			temp = 0;
		}
	}

	@Override
	public List<AnomalyReport> detect(TimeSeries ts) {
		List<AnomalyReport> anomalyReport = new ArrayList<>();
		String firstColumn = null;
		String secondColumn = null;
		CorrelatedFeatures corrFet;
		Point point_TestForAnomaly;
		double dev_TestForAnomaly;
		for (int i = 0; i < anomalyThresholdList.size();i++)
		{
			int sizeOfFirstColumn = ts.csvTable.get(anomalyThresholdList.get(i).feature1).size();
			corrFet = anomalyThresholdList.get(i);

			for (int j = 0; j < sizeOfFirstColumn ; j++) {
				firstColumn = corrFet.feature1;
				secondColumn = corrFet.feature2;
				point_TestForAnomaly = new Point(ts.csvTable.get(firstColumn).get(j),ts.csvTable.get(secondColumn).get(j));
				dev_TestForAnomaly = StatLib.dev(point_TestForAnomaly,corrFet.lin_reg);
				if(dev_TestForAnomaly > corrFet.threshold){
					String temp = (firstColumn+"-"+secondColumn);
					AnomalyReport anomalyRep1 = new AnomalyReport(temp,j+1);
					anomalyReport.add(anomalyRep1);
				}
			}
		}
		return anomalyReport;
	}


	public List<CorrelatedFeatures> getNormalModel(){
		return this.anomalyThresholdList;
	}
}