package com.avereon.curve.match;

import org.hamcrest.Matcher;
import org.hamcrest.number.IsCloseTo;

public class Matchers {

	// This value is a bit roomier than required. Most tests have deltas smaller
	// than 1e-14. This is set just a bit larger to give some wiggle room.
	public static final double TOLERANCE = 1e-12;

	public static Matcher<Double> near( double expected ) {
		return IsCloseTo.closeTo( expected, TOLERANCE );
	}

	public static Matcher<Double> near( double expected, double error ) {
		return IsCloseTo.closeTo( expected, error );
	}

	public static Matcher<double[]> near( double[] expected ) {
		return new PointCloseTo( expected, TOLERANCE );
	}

	public static Matcher<double[]> near( double[] expected, double tolerance ) {
		return new PointCloseTo( expected, tolerance );
	}

	public static Matcher<Iterable<double[]>> nearInAnyOrder( double[]... expected ) {
		return new NearAllInAnyOrder( expected, TOLERANCE );
	}

//	public static NearAllInAnyOrder nearAll( double[][] expected, double tolerance ) {
//		return new NearAllInAnyOrder( expected, tolerance );
//	}

	public static Matcher<double[]> sameDirection( double[] expected ) {
		return new VectorSameDirection( expected );
	}

}
