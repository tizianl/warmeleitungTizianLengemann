package waermeleitung;

import java.io.FileNotFoundException;
import java.util.concurrent.TimeUnit;

public class Main {

	public Main() throws FileNotFoundException {
		//long start = System.nanoTime();
		Cuboid cuboid = new Cuboid();
		cuboid.setCuboidAxes();
		cuboid.createQuader();

		cuboid.startQuader();
		/*long end = System.nanoTime();
		long time= end -start;
		System.out.println("Time: "+ (TimeUnit.MILLISECONDS.convert(time, TimeUnit.NANOSECONDS))+"ms");*/
	}

	public static void main(String[] args) throws FileNotFoundException {
		new Main();
	}
}