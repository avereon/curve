package com.avereon.curve.math;

import org.junit.jupiter.api.Test;

import static com.avereon.curve.match.Near.near;
import static org.hamcrest.MatcherAssert.assertThat;

public class GeometryTest {

	@Test
	void testCartesianToPolar() {
		assertThat( Geometry.cartesianToPolarDegrees( Point.of( 1, 0, 0 ) ), near( Point.of( 1, 0, 0 ) ) );
		assertThat( Geometry.cartesianToPolarDegrees( Point.of( 0, 1, 0 ) ), near( Point.of( 1, 90, 0 ) ) );
		assertThat( Geometry.cartesianToPolarDegrees( Point.of( -1, 0, 0 ) ), near( Point.of( 1, 180, 0 ) ) );
		assertThat( Geometry.cartesianToPolarDegrees( Point.of( 0, -1, 0 ) ), near( Point.of( 1, -90, 0 ) ) );
	}

	@Test
	void testPolarToCartesian() {
		assertThat( Geometry.polarDegreesToCartesian( Point.of( 1, 0, 0 ) ), near( Point.of( 1, 0, 0 ) ) );
		assertThat( Geometry.polarDegreesToCartesian( Point.of( 1, 90, 0 ) ), near( Point.of( 0, 1, 0 ) ) );
		assertThat( Geometry.polarDegreesToCartesian( Point.of( 1, 180, 0 ) ), near( Point.of( -1, 0, 0 ) ) );
		assertThat( Geometry.polarDegreesToCartesian( Point.of( 1, 270, 0 ) ), near( Point.of( 0, -1, 0 ) ) );
		assertThat( Geometry.polarDegreesToCartesian( Point.of( 1, 360, 0 ) ), near( Point.of( 1, 0, 0 ) ) );

		assertThat( Geometry.polarDegreesToCartesian( Point.of( Constants.SQRT_TWO, 45, 0 ) ), near( Point.of( 1, 1, 0 ) ) );
		assertThat( Geometry.polarDegreesToCartesian( Point.of( Constants.SQRT_TWO, 135, 0 ) ), near( Point.of( -1, 1, 0 ) ) );
		assertThat( Geometry.polarDegreesToCartesian( Point.of( Constants.SQRT_TWO, 225, 0 ) ), near( Point.of( -1, -1, 0 ) ) );
		assertThat( Geometry.polarDegreesToCartesian( Point.of( Constants.SQRT_TWO, 315, 0 ) ), near( Point.of( 1, -1, 0 ) ) );

		assertThat( Geometry.polarDegreesToCartesian( Point.of( Constants.SQRT_TWO, -45, 0 ) ), near( Point.of( 1, -1, 0 ) ) );
		assertThat( Geometry.polarDegreesToCartesian( Point.of( Constants.SQRT_TWO, -135, 0 ) ), near( Point.of( -1, -1, 0 ) ) );
		assertThat( Geometry.polarDegreesToCartesian( Point.of( Constants.SQRT_TWO, -225, 0 ) ), near( Point.of( -1, 1, 0 ) ) );
		assertThat( Geometry.polarDegreesToCartesian( Point.of( Constants.SQRT_TWO, -315, 0 ) ), near( Point.of( 1, 1, 0 ) ) );
	}

}
