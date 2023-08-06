package com.avereon.curve.math;

public interface Constants {

	double SQRT_ONE_HALF = Math.sqrt( 0.5 );

	double SQRT_TWO = Math.sqrt( 2 );

	double PI_OVER_2 = 0.5 * Math.PI;

	double PI_OVER_4 = 0.25 * Math.PI;

	double FULL_CIRCLE = 2.0 * Math.PI;

	double HALF_CIRCLE = Math.PI;

	double QUARTER_CIRCLE = PI_OVER_2;

	double THREE_QUARTER_CIRCLE = HALF_CIRCLE + QUARTER_CIRCLE;

	/**
	 * The value used to determine if two points in space are distinct. This needs
	 * to be small enough to be useful to the application, but large enough to
	 * stay away from round off errors. A value between 1e-4 to 1e-8 is common.
	 * <p/>
	 * This is also the same as DISTANCE_TOLERANCE and RESOLUTION_NORMAL.
	 */
	double RESOLUTION_LENGTH = 1e-6;

	/**
	 * The angle value that corresponds to RESOLUTION_LENGTH that is used to
	 * determine if to angle in space are distinct. The value is the arctan of
	 * the RESOLUTION_LENGTH along the unit circle.
	 */
	double RESOLUTION_ANGLE = Math.atan2( RESOLUTION_LENGTH, 1 );

	double RESOLUTION_SMOOTH = 1e-3;

	int MINIMUM_SEGMENTS = 64;
}
