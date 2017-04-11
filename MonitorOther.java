import java.util.Arrays;

/**
 * In this implementation, we don't keep track of the chopsticks,
 * we simply look at the philosophers' states & their neighbors'
 * states
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca
 */
public class MonitorOther
{
	/*
	 * ------------
	 * Data members
	 * ------------
	 */
	private PhilosopherState[] states;
	private boolean someoneTalking;
	private int piNumberOfPhilosophers;

	/**
	 * Constructor
	 */
	public MonitorOther(int piNumberOfPhilosophers)
	{

		states = new PhilosopherState[piNumberOfPhilosophers];
		Arrays.fill(states, PhilosopherState.THINKING);
		this.piNumberOfPhilosophers = piNumberOfPhilosophers;
		someoneTalking = false;
	}

	/*
	 * -------------------------------
	 * User-defined monitor procedures
	 * -------------------------------
	 */


	/**
	 * Grants request (returns) to eat when both chopsticks/forks are available.
	 * Else forces the philosopher to wait()
	 */
	public synchronized void pickUp(final int piTID)
	{
		int n = piTID - 1;
		int n1 = (n + 1) % piNumberOfPhilosophers;
		int n_1 = (piNumberOfPhilosophers + n - 1) % piNumberOfPhilosophers;
		while(states[n1].equals(PhilosopherState.EATING) || states[n_1].equals(PhilosopherState.EATING)) {
			try {
				wait();
			} catch (InterruptedException e) {}
		}
		states[n] = PhilosopherState.EATING;
	}

	/**
	 * When a given philosopher's done eating, they put the chopstiks/forks down
	 * and let others know they are available.
	 */
	public synchronized void putDown(final int piTID)
	{
		int n = piTID - 1;
		states[n] = PhilosopherState.THINKING;
		notifyAll();
	}

	/**
	 * Only one philopher at a time is allowed to philosophy
	 * (while she is not eating).
	 */
	public synchronized void requestTalk()
	{
		while(someoneTalking) {
			try {
				wait();
			} catch (InterruptedException e) {}
		}
		someoneTalking = true;
	}

	/**
	 * When one philosopher is done talking stuff, others
	 * can feel free to start talking.
	 */
	public synchronized void endTalk()
	{
		someoneTalking = false;
		notifyAll();
	}
}

// EOF
