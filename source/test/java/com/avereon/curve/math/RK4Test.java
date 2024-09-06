package com.avereon.curve.math;

import org.junit.jupiter.api.Test;

import java.util.function.BiFunction;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

public class RK4Test {

	@Test
	void testNextWithFunction2t() {
		// y(t)=y^2; dydt = 2t
		Function<Double, Double> dydt = t -> 2 * t;

		// Initial state
		double t = 0;
		double y = 0;

		y = RK4.next( t++, y, 1, dydt );
		assertThat( y ).isEqualTo( 1 );
		y = RK4.next( t++, y, 1, dydt );
		assertThat( y ).isEqualTo( 4 );
		y = RK4.next( t++, y, 1, dydt );
		assertThat( y ).isEqualTo( 9 );
		y = RK4.next( t, y, 1, dydt );
		assertThat( y ).isEqualTo( 16 );
	}

	@Test
	void testNextWithBiFunction2t() {
		// y(t)=y^2; dydt = 2t
		BiFunction<Double, Double, Double> dydt = ( t, y ) -> 2 * t + y;

		// Initial state
		double t = 0;
		double y = 0;

		y = RK4.next( t++, y, 1, dydt );
		assertThat( y ).isEqualTo( 1.25 );
		y = RK4.next( t++, y, 1, dydt );
		assertThat( y ).isEqualTo( 7.78125 );
		y = RK4.next( t++, y, 1, dydt );
		assertThat( y ).isEqualTo( 28.17578125 );
		y = RK4.next( t, y, 1, dydt );
		assertThat( y ).isEqualTo( 84.96142578125 );
	}

	@Test
	void testNextWithVectorBiFunctions1tAnd2t() {
		// x(t)=t; dxdt = 1
		Function<Double, Double> dxdt = ( t ) -> 1.0;
		// y(t)=t^2; dydt = 2t
		Function<Double, Double> dydt = ( t ) -> 2 * t;

		// Initial state
		double t = 0;
		double dt = 1;
		double[] u = new double[]{ 0, 0 };

		u = RK4.next( t, u, dt, dxdt, dydt );
		assertThat( u ).isEqualTo( new double[]{ 1.0, 1.0 } );
		u = RK4.next( t += dt, u, dt, dxdt, dydt );
		assertThat( u ).isEqualTo( new double[]{ 2.0, 4.0 } );
		u = RK4.next( t += dt, u, dt, dxdt, dydt );
		assertThat( u ).isEqualTo( new double[]{ 3.0, 9.0 } );
		u = RK4.next( t += dt, u, dt, dxdt, dydt );
		assertThat( u ).isEqualTo( new double[]{ 4.0, 16.0 } );
	}

}
