package com.avereon.curve.assertion;

import org.assertj.core.api.AbstractAssert;

public class VectorAssert extends AbstractAssert<VectorAssert, double[]> {

	private VectorAssert( double[] actual ) {
		super( actual, VectorAssert.class );
	}

	public static VectorAssert assertThat( double[] actual ) {
		return new VectorAssert( actual );
	}

	public VectorAssert isEqualTo( double[] expected ) {
		return this;
	}

}