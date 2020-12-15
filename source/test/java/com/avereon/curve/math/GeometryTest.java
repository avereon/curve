package com.avereon.curve.math;

import org.junit.jupiter.api.Test;

import static com.avereon.curve.match.Near.near;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class GeometryTest {

	@Test
	public void testGetSquare() {
		assertThat( Geometry.square( 0.5 ), is( 0.25 ) );
		assertThat( Geometry.square( 5 ), is( 25.0 ) );
		assertThat( Geometry.square( 2 ), is( 4.0 ) );
	}

	@Test
	void testGetAngleInXYPlaneAndVector() {
		//		double tolerance = 1e-12;
		//		Vector normal = new Vector( 0, 0, 1 );
		//		assertEquals( 0.0, Geometry.getAngle( new Vector(), normal, new Vector( 1, 0, 0 ), new Vector( 1, 0, 0 ) ), tolerance );
		//
		//		try {
		//			Log.setLevel( Log.DEBUG );
		//			assertEquals( Math.PI * 0.25, Geometry.getAngle( new Vector(), normal, new Vector( 1, 0, 0 ), new Vector( 1, 1, 0 ) ), tolerance );
		//		} finally {
		//			Log.setLevel( Log.NONE );
		//		}
		//		assertEquals( Math.PI * 0.5, Geometry.getAngle( new Vector(), normal, new Vector( 1, 0, 0 ), new Vector( 0, 1, 0 ) ), tolerance );
		//		assertEquals( Math.PI * 0.75, Geometry.getAngle( new Vector(), normal, new Vector( 1, 0, 0 ), new Vector( -1, 1, 0 ) ), tolerance );
		//
		//		assertEquals( Math.PI, Geometry.getAngle( new Vector(), normal, new Vector( 1, 0, 0 ), new Vector( -1, 0, 0 ) ), tolerance );
		//
		//		assertEquals( -Math.PI * 0.75, Geometry.getAngle( new Vector(), normal, new Vector( 1, 0, 0 ), new Vector( -1, -1, 0 ) ), tolerance );
		//		assertEquals( -Math.PI * 0.5, Geometry.getAngle( new Vector(), normal, new Vector( 1, 0, 0 ), new Vector( 0, -1, 0 ) ), tolerance );
		//		assertEquals( -Math.PI * 0.25, Geometry.getAngle( new Vector(), normal, new Vector( 1, 0, 0 ), new Vector( 1, -1, 0 ) ), tolerance );
		//
		//		assertEquals( -0.9272952180016123, Geometry.getAngle( new Vector( -2, 3, 0 ), normal, new Vector( -0.75, 3, 0 ), new Vector( -1.625, 3 - 0.5, 0 ) ), tolerance );
	}

	@Test
	void testMidpoint() {
		assertThat( Geometry.midpoint( Point.of( -1, -1, -1 ), Point.of( 1, 1, 1 ) ), near( Point.of( 0, 0, 0 ) ) );
		assertThat( Geometry.midpoint( Point.of( 0, 0, 0 ), Point.of( 1, 1, 1 ) ), near( Point.of( 0.5, 0.5, 0.5 ) ) );
		assertThat( Geometry.midpoint( Point.of( 1, 1, 0 ), Point.of( 3, 5, 0 ) ), near( Point.of( 2, 3, 0 ) ) );
	}

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
