package com.avereon.curve.math;

import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class VectorTest {

	@Test
	void testDistance() {
		assertThat( Vector.distance( Vector.of( 0, 0 ), Vector.of( 0, 0 ) ), is( 0.0 ) );

		assertThat( Vector.distance( Vector.of( 0, 0 ), Vector.of( 1, 0 ) ), is( 1.0 ) );
		assertThat( Vector.distance( Vector.of( 0, 0 ), Vector.of( -1, 0 ) ), is( 1.0 ) );
		assertThat( Vector.distance( Vector.of( 0, 0 ), Vector.of( 0, 1 ) ), is( 1.0 ) );
		assertThat( Vector.distance( Vector.of( 0, 0 ), Vector.of( 0, -1 ) ), is( 1.0 ) );

		assertThat( Vector.distance( Vector.of( 1, 1 ), Vector.of( 5, 4 ) ), is( 5.0 ) );
		assertThat( Vector.distance( Vector.of( 1, 1 ), Vector.of( 4, 5 ) ), is( 5.0 ) );
		assertThat( Vector.distance( Vector.of( -1, 1 ), Vector.of( -5, 4 ) ), is( 5.0 ) );
		assertThat( Vector.distance( Vector.of( -1, 1 ), Vector.of( -4, 5 ) ), is( 5.0 ) );
		assertThat( Vector.distance( Vector.of( 1, -1 ), Vector.of( 5, -4 ) ), is( 5.0 ) );
		assertThat( Vector.distance( Vector.of( 1, -1 ), Vector.of( 4, -5 ) ), is( 5.0 ) );
		assertThat( Vector.distance( Vector.of( -1, -1 ), Vector.of( -5, -4 ) ), is( 5.0 ) );
		assertThat( Vector.distance( Vector.of( -1, -1 ), Vector.of( -4, -5 ) ), is( 5.0 ) );

		assertThat( Vector.distance( Vector.of( 0, 0 ), Vector.of( Double.NaN, 0 ) ), is( Double.NaN ) );
	}

}
