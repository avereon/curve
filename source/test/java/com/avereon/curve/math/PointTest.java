package com.avereon.curve.math;

import com.avereon.curve.assertion.VectorAssert;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PointTest {

	@Test
	void testRotate() {
		assertThat( Point.rotate( Point.of( 1, 0 ), Point.of( 2, 0 ), Constants.QUARTER_CIRCLE ) ).isEqualTo( Point.of( 1, 1 ) );
		assertThat( Point.rotate( Point.of( 1, 0 ), Point.of( 2, 0 ), -Constants.QUARTER_CIRCLE ) ).isEqualTo( Point.of( 1, -1 ) );

		VectorAssert.assertThat( Point.rotate( Point.of( 3, -3 ), Point.of( 4, -2 ), Constants.HALF_CIRCLE ) ).isCloseTo( Point.of( 2, -4 ) );
		VectorAssert.assertThat( Point.rotate( Point.of( 3, -3 ), Point.of( 4, -2 ), -Constants.HALF_CIRCLE ) ).isCloseTo( Point.of( 2, -4 ) );

		VectorAssert.assertThat( Point.rotate( Point.of( -2, -3 ), Point.of( -1, -2 ), Constants.THREE_QUARTER_CIRCLE ) ).isCloseTo( Point.of( -1, -4 ) );
		VectorAssert.assertThat( Point.rotate( Point.of( -2, -3 ), Point.of( -1, -2 ), -Constants.THREE_QUARTER_CIRCLE ) ).isCloseTo( Point.of( -3, -2 ) );
	}

}
