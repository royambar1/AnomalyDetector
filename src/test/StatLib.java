package test;

import java.lang.Math;

public class StatLib {


	// simple average
	public static float avg(float[] x) {
		float sum = 0;
		int L = x.length;
		for(int i = 0; i < L ; i++)
		{
			sum += x[i];
		}
		return sum/L;
	}

	// returns the variance of X and Y
	public static float var(float[] x){
		float Avg = avg(x);
		float sum = 0;
		for(int i = 0; i < x.length ; i++)
		{
			sum += (x[i] - Avg) * (x[i] - Avg);
		}
		return sum/x.length;
	}

	// returns the covariance of X and Y
	public static float cov(float[] x, float[] y){
		float Avg_x = avg(x);
		float Avg_y = avg(y);
		float sum = 0;
		for(int i = 0; i < x.length ; i++)
		{
			sum += (x[i] - Avg_x)*(y[i] - Avg_y);
		}
		return sum/(x.length);
	}


	// returns the Pearson correlation coefficient of X and Y
	public static float pearson(float[] x, float[] y){
		float Numerator = cov(x,y);
		float Denominator = (float) (Math.sqrt(var(x))) * (float) (Math.sqrt(var(y)));
		return Numerator/Denominator;
	}

	// performs a linear regression and returns the line equation
	public static Line linear_reg(Point[] points){
		float[] x = new float[(int) points.length];
		float[] y = new float[(int) points.length];
		for(int i = 0; i < points.length ; i++)
		{
			x[i] = points[i].x;
			y[i] = points[i].y;
		}

		float a = cov(x,y)/var(x);
		float b = avg(y) - a*avg(x);

		return new Line(a,b);

	}

	// returns the deviation between point p and the line equation of the points
	public static float dev(Point p,Point[] points){
		Line L = linear_reg(points);
		float f_x = L.f(p.x);
		return (float) Math.sqrt((f_x - p.y)*(f_x - p.y));
	}

	// returns the deviation between point p and the line
	public static float dev(Point p,Line l)
	{
		float f_x = l.f(p.x);
		return (float) Math.sqrt((f_x - p.y)*(f_x - p.y));
	}
}
