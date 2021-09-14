package com.avereon.curve.match;

import com.avereon.curve.math.Vector;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class PointCloseTo extends TypeSafeMatcher<double[]> {

	private final double[] expected;

	private final double tolerance;

	public PointCloseTo( double[] expected, double tolerance ) {
		this.expected = expected;
		this.tolerance = tolerance;
	}

	@Override
	protected boolean matchesSafely( double[] item ) {
		return this.actualDelta( item ) <= 0.0D;
	}

	public void describeMismatchSafely( double[] item, Description mismatchDescription ) {
		mismatchDescription
				.appendValue( item )
				.appendText( " differed by " )
				.appendValue( this.actualDelta( item ) )
				.appendText( " more than delta " )
				.appendValue( this.tolerance );
	}

	@Override
	public void describeTo( Description description ) {
		description.appendText( "a point within " ).appendValue( this.tolerance ).appendText( " of " ).appendValue( this.expected );
	}

	private double actualDelta( double[] item ) {
		return Vector.distance( item, expected ) - this.tolerance;
	}

	public static Matcher<double[]> closeTo( double[] operand, double error ) {
		return new PointCloseTo( operand, error );
	}

}
