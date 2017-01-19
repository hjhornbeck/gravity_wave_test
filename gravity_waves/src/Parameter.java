
/**************************************************************************
 *
 * Parameter: represent a specific parameter in the space. Based on a codebase
 *  created for another course.
 *
 */

public class Parameter implements Comparable<Parameter> {

	private Data data	= null;		// the dataset this parameter set applies to
	private Waveform template = null;	// the gravity wave template to compare to
	private int time = 0;			// the specific sample where the gravity wave starts
	private double amp = 0.0;		// the amplitude of the gravity wave
	private int[] delta;			// the difference between detectors
	private double cached;			// a cache of the result, with a validity flag
	private boolean cacheValid	= false;


	/****
	 * A helper to adjust our parameter set for us.
	 */
	private void adjustParams() {

		if ( data != null ) {	// can't do much without a dataset

			if ( template != null ) {

				if ( time < -template.getLength() )
					time = -template.getLength();
				else if ( time > data.getLength() + template.getLength() )
					time = data.getLength() + template.getLength();
				}

			for (int it = 0; it < delta.length; it++)
				if ( delta[it] > (int)data.maxOffset() )
					delta[it] = (int)data.maxOffset();
				else if ( delta[it] < -(int)data.maxOffset() ) // round to zero?
					delta[it] = -(int)data.maxOffset();
			}

		} // adjustParams

	/***
	 * Assign a dataset.
	 */
	public boolean setData( Data d ) {

		if ( d == null )
			return false;

		cacheValid = false;

		data = d;
		delta	= new int[ data.getDetectors() ];
		adjustParams();

		return true;
		}

	/***
	 * Assign a template.
	 */
	public boolean setWaveform( Waveform w ) {

		if ( w == null )
			return false;

		cacheValid = false;

		template = w;
		adjustParams();

		return true;
		}


	/***
	 * Assign a time
	 */
	public boolean setTime( int t ) {

		// only need to do half the checks
		if ( data != null && template != null )
			if ( t < template.getLength() || t > data.getLength() + template.getLength() )
				return false;

		cacheValid = false;

		time = t;
		return true;

		}


	/***
	 * Assign an amplitude. All values are valid.
	 */
	public void setAmplitude( double a ) { amp = a; cacheValid = false; }


	/***
	 * Assign a delta.
	 */
	public boolean setDelta( int[] d ) {

		// only need to do half the checks
		if ( data != null )

			if (d.length != delta.length)
				return false;

			for (int it = 0; it < delta.length; it++) {
				if ( d[it] > (int)data.maxOffset() )
					return false;
				else if ( d[it] < -(int)data.maxOffset() ) // round to zero?
					return false;
				}

		cacheValid = false;

		delta = d;
		return true;
		}

	/****
	 * Calculate the log prior probability of this parameter set.
	 **/
	public double logPrior() {

		// We're agnostic about time, and amplitude is tricky to calculate so we won't.
		//  Delta relies on the dataset, so we defer there.
		return data.logPriorDelta( delta );

		} // prior

	/****
	 * Calculate the log likelihood of this dataset.
	 **/
	public double logLikeli() {

		// error checking
		if ( data == null || template == null )
			return Double.NEGATIVE_INFINITY;

		double OR	= 0.0;		// the resulting odds ratio 

		// for each template location
		for (int location = 0; location < template.getLength(); location++) {

			char dataset	= 0;	// keep track of how many data locations are valid
			double tempOR	= 0.0;

			for (; dataset < delta.length; dataset++) {

				// figure out the location in the data, exiting if invalid
				int dLocation	= time + delta[dataset];
				if ( dLocation < 0 || dLocation >= data.getLength() )
					break;

				// otherwise, continue calculating
				double adjTemp	= template.getData( location );
				tempOR	+= adjTemp * adjTemp;

				adjTemp		= data.getData( dLocation, dataset ) - amp*adjTemp;
				tempOR	-= adjTemp * adjTemp;
				}

			// only add our data if all datasets comply (necessary?)
			if (dataset == delta.length)
				OR	+= tempOR;

			} // for (each location)

		return OR;

		} // logLikeli

	/****
	 * Calculate the log probability of this dataset.
	 **/
	public double logProb() {

		if ( !cacheValid ) {

			cached = logLikeli() * logPrior();
			cacheValid = true;
			}

		return cached;

		} // logProb

	/****
	 * Allow this Parameter to be compared to others.
	 **/
	public int compareTo( Parameter p ) {

		double temp = logProb() - p.logProb();
		if ( temp < 0 )
			return -1;
		else if ( temp > 0 )
			return 1;
		else
			return 0;

		} // compareTo

	} // Parameter
