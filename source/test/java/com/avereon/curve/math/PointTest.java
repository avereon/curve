package com.avereon.curve.math;

import org.junit.jupiter.api.Test;

import static com.avereon.curve.match.Matchers.near;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class PointTest {

	@Test
	void testRotate() {
		assertThat( Point.rotate( Point.of( 1, 0 ), Point.of( 2, 0 ), Constants.QUARTER_CIRCLE ), is( Point.of( 1, 1 ) ) );
		assertThat( Point.rotate( Point.of( 1, 0 ), Point.of( 2, 0 ), -Constants.QUARTER_CIRCLE ), is( Point.of( 1, -1 ) ) );

		assertThat( Point.rotate( Point.of( 3, -3 ), Point.of( 4,-2 ), Constants.HALF_CIRCLE ), near( Point.of( 2, -4 ) ) );
		assertThat( Point.rotate( Point.of( 3, -3 ), Point.of( 4,-2 ), -Constants.HALF_CIRCLE ), near( Point.of( 2, -4 ) ) );

		assertThat( Point.rotate( Point.of( -2, -3 ), Point.of( -1,-2 ), Constants.THREE_QUARTER_CIRCLE ), near( Point.of( -1, -4 ) ) );
		assertThat( Point.rotate( Point.of( -2, -3 ), Point.of( -1,-2 ), -Constants.THREE_QUARTER_CIRCLE ), near( Point.of( -3, -2 ) ) );
	}

}
