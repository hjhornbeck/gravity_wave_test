
/**************************************************************************
 *
 * SearchBlock: Scan over the given parameter space, feeding results back
 *  into Results.
 *
 */

import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

public class SearchBlock implements Runnable {

	private char method = 0;		// 0 = brute force, currently the only option
	private int storage = -1;		// store the top X values. -1 = store them all
	private SortedSet<Parameter> results;	// the top X results

	private int minTime = 0;		// the min/max of the time parameter to search
	private int maxTime = 0;

	private double minAmp = -3.0;		// the min/max amplitude of the gravity wave
	private double maxAmp =  3.0;
	private double stepAmp = 1.0/100.0;	// how many steps to divide the range into

	private Data data = null;		// the Data object shared among all Parameters
	private Waveform template = null;	// the Waveform

	public SearchBlock() {

		results = Collections.synchronizedSortedSet( new TreeSet<Parameter>() );

		}

	/****
	 * associate a dataset
	 */
	public boolean loadData( Data d ) {

		if ( d == null )
			return false;

		data = d;

		// adjust the given range to fit
		if ( template != null && maxTime >= template.getLength() + data.getLength() )
			maxTime = data.getLength() + template.getLength() - 1;
		else if ( maxTime >= data.getLength() )
			maxTime = data.getLength() - 1;

		return true;

		} // loadData
	
	/****
	 * associate a template
	 */
	public boolean loadWaveform( Waveform w ) {

		if ( w == null )
			return false;

		template = w;

		// adjust the given range to fit
		if ( data != null && maxTime >= template.getLength() + data.getLength() )
			maxTime = data.getLength() + template.getLength() - 1;
		else if ( maxTime >= template.getLength() )
			maxTime = template.getLength() - 1;

		return true;

		} // loadWaveform

	/****
	 * set the time range
	 */
	public boolean setTimeRange( int start, int end ) {

		if ( start > end )
			return false;

		if ( data != null ) {

			if ( template != null ) {

				if ( start < -template.getLength() )
					return false;
				else if ( end >= template.getLength() + data.getLength() )
					return false;

				}
			else {
				if ( start < 0 )
					return false;
				else if ( end >= data.getLength() )
					return false;
				}
			}

		minTime = start;
		maxTime = end;

		return true;
		}

	/****
	 * set the amplitude range
	 */
	public boolean setAmplitudeRange( double start, double end ) {

		if ( start > end )
			return false;

		minAmp	= start;
		maxAmp 	= end;
		return true;
		}

	/****
	 * set the amplitude step
	 */
	public boolean setAmplitudeDivisions( double steps ) {

		if ( steps < 0 )
			return false;

		stepAmp = 1.0 / steps;
		return true;
		}

	/****
	 * set how much storage to keep (no really possibility for error)
	 */
	public void setStorage( int count ) {	storage = count; }

	/****
	 * allow retreiving the stored totals
	 */
	public SortedSet<Parameter> getStorage() {	return results; }

	/****
	 * brute force scan the parameter space
	 */
	public void run() {

		// create a mapping between the ints and the Parameter space we'll be scanning
		// fill an array with those ints
		// shuffle it
		// for each element of that array
		//  convert it to a Parameter
		//  evaluate it
		//  not enough members in results? Or unlimited storage? tack it on
		//  too many? check if the result even makes the top X
		//   if so, add it and trim off the bottom

		} // run

	} // SearchBlock
