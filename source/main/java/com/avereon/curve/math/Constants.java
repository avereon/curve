package com.avereon.curve.math;

public interface Constants {

	double SQRT_ONE_HALF = Math.sqrt( 0.5 );

	double SQRT_TWO = Math.sqrt( 2 );

	double PI_OVER_2 = 0.5 * Math.PI;

	double PI_OVER_4 = 0.25 * Math.PI;

	double DISTANCE_TOLERANCE = 1e-12;

	double FULL_CIRCLE = 2.0 * Math.PI;

	double HALF_CIRCLE = Math.PI;

	double QUARTER_CIRCLE = PI_OVER_2;

	double RESOLUTION_LENGTH = 1e-6;

	double RESOLUTION_NORMAL = 1e-10;

	double RESOLUTION_ANGLE = Math.atan( RESOLUTION_NORMAL );

	double RESOLUTION_SMOOTH = 1e-3;

}
