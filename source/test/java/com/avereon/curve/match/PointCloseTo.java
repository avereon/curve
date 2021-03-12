package com.avereon.curve.match;

import com.avereon.curve.math.Vector;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class PointCloseTo extends TypeSafeMatcher<double[]> {

	private final double[] expected;

	private final double delta;

	public PointCloseTo( double[] expected, double error ) {
		this.expected = expected;
		this.delta = error;
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
				.appendValue( this.delta );
	}

	@Override
	public void describeTo( Description description ) {
		description.appendText( "a point within " ).appendValue( this.delta ).appendText( " of " ).appendValue( this.expected );
	}

	private double actualDelta( double[] item ) {
		return Vector.distance( item, expected ) - this.delta;
	}

	public static Matcher<double[]> closeTo( double[] operand, double error ) {
		return new PointCloseTo( operand, error );
	}

}
