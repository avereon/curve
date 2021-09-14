package com.avereon.curve.match;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class NearAllInAnyOrder extends TypeSafeDiagnosingMatcher<Iterable<double[]>> {

	private final Collection<PointCloseTo> matchers;

	private final double tolerance;

	public NearAllInAnyOrder( double[][] expected, double tolerance ) {
		this.matchers = Arrays.stream( expected ).map( p -> new PointCloseTo( p, tolerance ) ).collect( Collectors.toSet() );
		this.tolerance = tolerance;
	}

	@Override
	protected boolean matchesSafely( Iterable<double[]> items, Description mismatchDescription ) {
		final NearAllInAnyOrder.Matching matching = new NearAllInAnyOrder.Matching( matchers, mismatchDescription );

		for( double[] item : items ) {
			if( !matching.matches( item ) ) {
				return false;
			}
		}

		return matching.isFinished( items );
	}

	@Override
	public void describeTo( Description description ) {
		description.appendText("iterable with items ")
			.appendList("[", ", ", "]", matchers)
			.appendText( "within " )
			.appendValue( tolerance )
			.appendText(" in any order");
	}

	private static class Matching {

		private final Collection<PointCloseTo> matchers;

		private final Description mismatchDescription;

		public Matching( Collection<PointCloseTo> matchers, Description mismatchDescription ) {
			this.matchers = new ArrayList<>( matchers );
			this.mismatchDescription = mismatchDescription;
		}

		public boolean matches( double[] item ) {
			if( matchers.isEmpty() ) {
				mismatchDescription.appendText( "no match for: " ).appendValue( item );
				return false;
			}
			return isMatched( item );
		}

		public boolean isFinished( Iterable<double[]> items ) {
			if( matchers.isEmpty() ) {
				return true;
			}
			mismatchDescription.appendText( "no item matches: " ).appendList( "", ", ", "", matchers ).appendText( " in " ).appendValueList( "[", ", ", "]", items );
			return false;
		}

		private boolean isMatched( double[] item ) {
			for( PointCloseTo matcher : matchers ) {
				if( matcher.matches( item ) ) {
					matchers.remove( matcher );
					return true;
				}
			}
			mismatchDescription.appendText( "not matched: " ).appendValue( item );
			return false;
		}
	}
}
