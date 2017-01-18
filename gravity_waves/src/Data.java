
/**************************************************************************
 *
 * Data: contains the LIGO data to test against. Based on an earlier
 *  codebase created for another course.
 *
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class Data {

	private double[] strainH;		// the strain recorded at Hanford
	private double[] strainL;		// the strain recorded at Livingstone

	private final double HLdelta = 52;		// the distance between H and L, in samples

	/****
	 * A helper to load datasets for us.
	 */
	public static ArrayList<Double> loadFile( String f ) {

		ArrayList<Double> retVal	= new ArrayList<Double>();
		try {
			BufferedReader inFile	= new BufferedReader(new FileReader( f ));
			String inLine		= null;
			while ((inLine = inFile.readLine()) != null)
				retVal.add( Double.valueOf(inLine) );
			}
		catch (Exception e) {

			System.err.println( "ERROR: Could not load data: " + e.getMessage() );
			}

		return retVal;

		} // loadFile

	/****
	 * Load up the datasets. Needs to be overloaded when more stations come online.
	 */
	public boolean loadData( String h, String l ) {

		ArrayList<Double> tempH	= loadFile( h );
		if ( tempH.size() < 1 )
			return false;
		
		ArrayList<Double> tempL = loadFile( l );
		if ( tempL.size() < 1 )
			return false;

		if ( tempL.size() != tempH.size() ) {

			System.err.println( "ERROR: all datasets should be the same length!" );
			return false;
			}

		strainH		= new double[ tempH.size() ];
		strainL		= new double[ tempH.size() ];

		for (int it = 0; it < tempH.size(); it++)	// this should be easier on the caches
			strainH[it]	= tempH.get( it ).doubleValue();
		for (int it = 0; it < tempH.size(); it++)	// this should be easier on the caches
			strainL[it]	= tempL.get( it ).doubleValue();

		return true;
		}

	/****
	 * How long are the datasets, in samples?
	 */
	public int getLength() { return strainH.length; }

	/****
	 * How many datasets do we have?
	 */
	public char getDatasets() { return 2; }

	/***
	 * Retrieve the data from a given detector.
	 */
	public double getData( int offset, char detector ) {

		if (offset > strainH.length || offset < 0) {

			System.err.println( "ERROR: asked for impossible sample, " + offset );
			return Double.NEGATIVE_INFINITY;
			}

		switch (detector) {

			case 0:
				return strainH[ offset ];
			case 1:
				return strainL[ offset ];
			default:

				System.err.println( "ERROR: asked for impossible detector, " + detector );
				return Double.NEGATIVE_INFINITY;
			}

		} // getData

	/****
	 * Return the sample offset between two detectors.
	 */
	public double getOffset( char first, char second ) {

		// organize these to make things easier
		if ( second < first ) {

			char temp	= first;
			first		= second;
			second		= temp;
			}

		if ( first == second )
			return 0;
		if ( first == 0 && second == 1 )
			return HLdelta;

		System.err.println( "ERROR: asked for impossible detector pair, " + first + " and " + second );
		return Double.NEGATIVE_INFINITY;

		}

	} // Data
