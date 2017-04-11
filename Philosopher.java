import common.BaseThread;

/**
 * Class Philosopher.
 * Outlines main subrutines of our virtual philosopher.
 *
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca
 */
public class Philosopher extends BaseThread
{
	/**
	 * Max time an action can take (in milliseconds)
	 */
	public static final long TIME_TO_WASTE = 1000;

	/**
	 * The act of eating.
	 * - Print the fact that a given phil (their TID) has started eating.
	 * - yield
	 * - Then sleep() for a random interval.
	 * - yield
	 * - The print that they are done eating.
	 */
	public void eat()
	{
		try
		{
			/*
			 * print that the philosopher starts to eat, sleep a random length of
			 * time between 0 and `TIME_TO_WASTE` seconds, to simulate eating time
			 * and finally, print that the philosopher has finished eating
			 *
			 * if the philosopher is interrupted while eating, he gets mad and prints
			 * why the interrupt happened, then he leaves.
			 */
			System.out.println("Philosopher " + getTID() + " has started to eat");
			sleep((long)(Math.random() * TIME_TO_WASTE));
			System.out.println("Philosopher " + getTID() + " has finished eating");
		}
		catch(InterruptedException e)
		{
			System.err.println("Philosopher.eat():");
			DiningPhilosophers.reportException(e);
			System.exit(1);
		}
	}

	/**
	 * The act of thinking.
	 * - Print the fact that a given phil (their TID) has started thinking.
	 * - yield
	 * - Then sleep() for a random interval.
	 * - yield
	 * - The print that they are done thinking.
	 */
	public void think()
	{
		try {
			/*
			 * print that the philosopher starts to think, sleep a random length of
			 * time between 0 and `TIME_TO_WASTE` seconds, to simulate thinking time
			 * and finally, print that the philosopher has finished thinking
			 *
			 * if the philosopher is interrupted while thinking, he gets mad and prints
			 * why the interrupt happened, then he leaves.
			 */
			System.out.println("Philosopher " + getTID() + " has started to think");
			yield();
			sleep((long)(Math.random() * TIME_TO_WASTE));
			System.out.println("Philosopher " + getTID() + " has perfected a thought");
		} catch (InterruptedException e) {
			System.err.println("Philosopher.think():");
			DiningPhilosophers.reportException(e);
			System.exit(1);
		}

	}

	/**
	 * The act of talking.
	 * - Print the fact that a given phil (their TID) has started talking.
	 * - yield
	 * - Say something brilliant at random
	 * - yield
	 * - The print that they are done talking.
	 */
	public void talk()
	{
		/*
		 * print that the philosopher starts to talk, then print the profound
		 * thought that the philosopher wants to share with everyone
		 * and finally print that the philosopher has done talking.
		 *
		 * between each print, yield control to another thread.
		 */
		System.out.println("Philosopher " + getTID() + " has started to speak");
		yield();
		saySomething();
		yield();
		System.out.println("Philosopher " + getTID() + " has spoken");
	}

	/**
	 * No, this is not the act of running, just the overridden Thread.run()
	 */
	public void run()
	{
		for(int i = 0; i < DiningPhilosophers.DINING_STEPS; i++)
		{
			DiningPhilosophers.soMonitor.pickUp(getTID());

			eat();

			DiningPhilosophers.soMonitor.putDown(getTID());

			think();

			/*
			 * A decision is made at random whether this particular
			 * philosopher is about to say something terribly useful.
			 */
			if(Math.random() < 0.5)
			{
				// request to talk before talking
				// talk
				// after talking, end the talk & give someone else
				// the opportunity to talk.
				DiningPhilosophers.soMonitor.requestTalk();
				talk();
				DiningPhilosophers.soMonitor.endTalk();
			}

			yield();
		}
	} // run()

	/**
	 * Prints out a phrase from the array of phrases at random.
	 * Feel free to add your own phrases.
	 */
	public void saySomething()
	{
		String[] astrPhrases =
		{
			"Eh, it's not easy to be a philosopher: eat, think, talk, eat...",
			"You know, true is false and false is true if you think of it",
			"2 + 2 = 5 for extremely large values of 2...",
			"If thee cannot speak, thee must be silent",
			"My number is " + getTID() + ""
		};

		System.out.println
		(
			"Philosopher " + getTID() + " says: " +
			astrPhrases[(int)(Math.random() * astrPhrases.length)]
		);
	}
}

// EOF
