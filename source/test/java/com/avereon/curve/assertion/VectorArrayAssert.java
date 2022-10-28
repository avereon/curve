package com.avereon.curve.assertion;

import com.avereon.curve.math.Vector;
import org.assertj.core.api.AbstractAssert;

import java.util.Arrays;

public class VectorArrayAssert extends AbstractAssert<VectorArrayAssert, double[][]> {

	protected VectorArrayAssert( double[][] actual ) {
		super( actual, VectorArrayAssert.class );
	}

	public static VectorArrayAssert assertThat( double[][] actual ) {
		return new VectorArrayAssert( actual );
	}

	public VectorArrayAssert areCloseTo( double[]... vectors ) {
		return areCloseTo( vectors, VectorAssert.TOLERANCE);
	}

	public VectorArrayAssert areCloseTo( double[][] vectors, double tolerance ) {
		if( vectors.length != actual.length ) {
			throw failureWithActualExpected( Arrays.deepToString( actual ), Arrays.deepToString( vectors ), "Array length mismatch: %d != %d",vectors.length, actual.length );
		}
		for( double[] expected : vectors ) {
			if( !containsCloseTo( expected, actual, tolerance ) ) {
				throw failureWithActualExpected( Arrays.deepToString( actual ), Arrays.deepToString( vectors ), "Distance is greater than %s at %s", tolerance, Arrays.toString(expected) );
			}
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
