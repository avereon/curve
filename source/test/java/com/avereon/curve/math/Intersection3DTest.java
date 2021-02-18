package com.avereon.curve.math;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNull;

public class Intersection3DTest {

	@Test
	public void testGetIntersectionWithPlaneAndLine() {
		// Null checks.
		assertNull( Intersection3D.intersectionLinePlane( new double[]{ 0, 0, 0 }, new double[]{ 0, 0, 0 }, new double[]{ 0, 0, 0 }, new double[]{ 0, 0, 1 } ) );
		assertNull( Intersection3D.intersectionLinePlane( new double[]{ -1, 0, 0 }, new double[]{ 1, 0, 0 }, new double[]{ 0, 0, 0 }, new double[]{ 0, 0, 1 } ) );
		assertNull( Intersection3D.intersectionLinePlane( new double[]{ -1, 2, 0 }, new double[]{ 1, 2, 0 }, new double[]{ 0, 0, 0 }, new double[]{ 0, 0, 1 } ) );
		assertNull( Intersection3D.intersectionLinePlane( new double[]{ -1, 2, 3 }, new double[]{ 1, 2, 3 }, new double[]{ 0, 0, 0 }, new double[]{ 0, 0, 1 } ) );

		// Trivial plane with non-trivial line.
		assertThat( Intersection3D.intersectionLinePlane( new double[]{ 0, 0, -1 }, new double[]{ 0, 0, 1 }, new double[]{ 0, 0, 0 }, new double[]{ 0, 0, 1 } ), is( new double[]{ 0, 0, 0 } ) );
		assertThat( Intersection3D.intersectionLinePlane( new double[]{ -1, -1, -1 }, new double[]{ 1, 1, 1 }, new double[]{ 0, 0, 0 }, new double[]{ 0, 0, 1 } ), is( new double[]{ 0, 0, 0 } ) );
		assertThat( Intersection3D.intersectionLinePlane( new double[]{ 0, 0, 2 }, new double[]{ 0, 0, 1 }, new double[]{ 0, 0, 0 }, new double[]{ 0, 0, 1 } ), is( new double[]{ 0, 0, 0 } ) );

		// Non-trivial plane with trivial line.
		assertThat( Intersection3D.intersectionLinePlane( new double[]{ 1, 2, -1 }, new double[]{ 1, 2, 1 }, new double[]{ 1, 2, 3 }, new double[]{ 1, 1, 1 } ), is( new double[]{ 1, 2, 3 } ) );
	}

}
