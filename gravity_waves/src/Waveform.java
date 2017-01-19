
/**************************************************************************
 *
 * Waveform: the gravity wave template to test against. Based on an earlier
 *  codebase created for another course.
 *
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class Waveform {

	private double[] template = new double[0];

	/****
	 * Load up the template.
	 */
	public boolean loadWave( String t ) {

		ArrayList<Double> temp	= Data.loadFile( t );
		if ( temp.size() < 1 )
			return false;
		
		template	= new double[ temp.size() ];
		for (int it = 0; it < temp.size(); it++)
			template[it]	= temp.get( it ).doubleValue();

		return true;
		}

	/****
	 * How many samples are there in the template?
	 */
	public int getLength() { return template.length; }

	/***
	 * Retrieve a sample from the template
	 */
	public double getData( int offset ) {

		if (offset > template.length || offset < 0) {

			System.err.println( "ERROR: asked for impossible sample, " + offset );
			return Double.NEGATIVE_INFINITY;
			}

		return template[ offset ];

		} // getData

	} // Waveform
