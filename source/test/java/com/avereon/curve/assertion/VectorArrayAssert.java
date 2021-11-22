package com.avereon.curve.assertion;

import com.avereon.curve.math.Vector;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

public class VectorArrayAssert extends AbstractAssert<VectorArrayAssert, double[][]> {

	protected VectorArrayAssert( double[][] actual ) {
		super( actual, VectorArrayAssert.class );
	}

	public static VectorArrayAssert assertThat( double[][] actual ) {
		return new VectorArrayAssert( actual );
	}

	public VectorArrayAssert areCloseTo( double[]... vectors ) {
		for( double[] expected : vectors ) {
			Assertions.assertThat( containsCloseTo( expected, actual, VectorAssert.TOLERANCE ) ).isTrue();
		}
		return this;
	}

	public VectorArrayAssert areCloseTo( double[][] vectors, double tolerance ) {
		for( double[] expected : vectors ) {
			Assertions.assertThat( containsCloseTo( expected, actual, tolerance ) ).isTrue();
		}
		return this;
	}

	static boolean containsCloseTo( double[] vector, double[][] vectors, double tolerance ) {
		for( double[] check : vectors ) {
			if( closeTo( vector, check, tolerance ) ) return true;
		}
		return false;
	}

	static boolean closeTo( double[] a, double[] b, double tolerance ) {
		return Vector.distance( a, b ) <= tolerance;
	}

}
