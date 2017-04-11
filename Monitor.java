import java.util.Arrays;

/**
 * Class MonitorOther
 * To synchronize dining philosophers.
 *
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca
 */
public class Monitor
    {
	/*
	 * ------------
	 * Data members
	 * ------------
	 */
        // the number of chopsticks
        private int piNumberOfChopsticks;

        // array of size ``piNumberOfChopsticks`` indicating which chopsticks are free
        // chopstick positions number from 0 .. (piNumberOfChopsticks-1) and are
        // placed between philosophers,
        // here's a possible mapping of philosopher -> adjacent chopstick
        //  0 -> {4, 0}
        //  1 -> {0, 1}
        //  2 -> {1, 2}
        //  3 -> {2, 3}
        //  4 -> {0, 4}
        private boolean[] chopsticksFree;

        // boolean to indicate if someone is talking
        private boolean someoneTalking;

        /**
         * Constructor
         */
	public Monitor(int piNumberOfPhilosophers)
        {
            piNumberOfChopsticks = piNumberOfPhilosophers;
            chopsticksFree = new boolean[piNumberOfChopsticks];
            Arrays.fill(chopsticksFree, true);
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
        // calculate the adjacent chopsticks' positions
        // from the philosopher's seat number.
        // wait for the chopsticks to be free, then set
        // their status to not free
        int n = piTID - 1;
        int n_1 = (n == 0) ? piNumberOfChopsticks - 1 : n - 1;
        while(!chopsticksFree[n] || !chopsticksFree[n_1]) {
            try {
                wait();
            } catch (InterruptedException e) {}
        }
        chopsticksFree[n] = false;
        chopsticksFree[n_1] = false;

    }

    /**
     * When a given philosopher's done eating, they put the chopstiks/forks down
     * and let others know they are available.
     */
    public synchronized void putDown(final int piTID)
    {
        // calculate the adjacent chopsticks' positions
        // from the philosopher's seat number.
        // set the chopsticks free & wake all other threads.
        int n = piTID - 1;
        int n_1 = (n == 0) ? piNumberOfChopsticks - 1 : n - 1;
        chopsticksFree[n] = true;
        chopsticksFree[n_1] = true;
        notifyAll();
    }

    /**
     * Only one philopher at a time is allowed to philosophy
     * (while she is not eating).
     */
    public synchronized void requestTalk()
    {
        // wait for the someoneTalking to be false,
        // i.e. wait for someone to finish talking
        // then set someoneTalking to true
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
        // set someoneTalking to false,
        // to give another philosopher a chance
        // to speak
        someoneTalking = false;
        notifyAll();
    }
}

// EOF
