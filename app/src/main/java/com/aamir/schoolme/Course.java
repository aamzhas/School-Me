package com.aamir.schoolme;

import java.util.HashMap;

/**
 * Created by Aamir on 25/02/17.
 */

public class Course {
	private String name;
	private String code;
	private int rating;
	private HashMap< String, String > reviews;
	private double averageGPA;

	public Course( String name, String code, int rating, HashMap< String, String > reviews, double averageGPA ) {
		this.name = name;
		this.code = code;
		this.rating = rating;
		this.reviews = reviews;
		this.averageGPA = averageGPA;
	}

	@Override
	public int hashCode() {
		int result;
		long temp;
		result = getName().hashCode();
		result = 31 * result + getCode().hashCode();
		result = 31 * result + getRating();
		result = 31 * result + ( getReviews() != null ? getReviews().hashCode() : 0 );
		temp = Double.doubleToLongBits( getAverageGPA() );
		result = 31 * result + (int) ( temp ^ ( temp >>> 32 ) );
		return result;
	}

	@Override
	public boolean equals( Object o ) {
		if ( this == o ) {
			return true;
		}
		if ( o == null || getClass() != o.getClass() ) {
			return false;
		}

		Course course = (Course) o;

		if ( getRating() != course.getRating() ) {
			return false;
		}
		if ( Double.compare( course.getAverageGPA(), getAverageGPA() ) != 0 ) {
			return false;
		}
		if ( !getName().equals( course.getName() ) ) {
			return false;
		}
		if ( !getCode().equals( course.getCode() ) ) {
			return false;
		}

		return getReviews() != null ? getReviews().equals( course.getReviews() ) : course.getReviews() == null;

	}

	public String getName() {
		return name;
	}

	public String getCode() {
		return code;
	}

	public int getRating() {
		return rating;
	}

	public HashMap< String, String > getReviews() {
		return reviews;
	}

	public double getAverageGPA() {
		return averageGPA;
	}
}
