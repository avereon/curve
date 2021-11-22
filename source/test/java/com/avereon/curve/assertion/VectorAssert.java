package com.avereon.curve.assertion;

import com.avereon.curve.math.Vector;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import org.assertj.core.data.Offset;

public class VectorAssert extends AbstractAssert<VectorAssert, double[]> {

	// This value is a bit roomier than required. Most tests have deltas smaller
	// than 1e-14. This is set just a bit larger to give some wiggle room.
	public static final double TOLERANCE = 1e-12;

	private VectorAssert( double[] actual ) {
		super( actual, VectorAssert.class );
	}

	public static VectorAssert assertThat( double[] actual ) {
		return new VectorAssert( actual );
	}

	public VectorAssert isEqualTo( double[] expected ) {
		return this;
	}

	public VectorAssert isCloseTo( double[] expected ) {
		return isCloseTo( expected, TOLERANCE );
	}

	public VectorAssert isCloseTo( double[] expected, double tolerance ) {
		Assertions.assertThat( Vector.distance( actual, expected ) ).isCloseTo( 0.0, Offset.offset( tolerance ) );
		return this;
	}

	public VectorAssert isSameDirection( double[] expected ) {
		return this;
	}

}
