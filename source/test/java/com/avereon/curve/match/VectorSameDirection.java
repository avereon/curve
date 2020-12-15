package com.avereon.curve.match;

import com.avereon.curve.math.Vector;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.util.Arrays;

public class VectorSameDirection extends TypeSafeMatcher<double[]> {

	private final double[] expected;

	public VectorSameDirection( double[] expected ) {
		this.expected = expected;
	}

	@Override
	protected boolean matchesSafely( double[] actual ) {
		return Arrays.equals( expected, actual );
	}

	public void describeMismatchSafely( double[] item, Description mismatchDescription ) {
		mismatchDescription
				.appendValue( Vector.toString( item ) )
				.appendText( " not in same direction as " )
				.appendValue( Vector.toString( expected ) );
	}

	@Override
	public void describeTo( Description description ) {
		description.appendText( "a vector in the same direction of " ).appendValue( Vector.toString( expected ) );
	}
}
