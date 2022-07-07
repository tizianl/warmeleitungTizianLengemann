package waermeleitung;

import org.yaml.snakeyaml.Yaml;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;
import java.io.FileNotFoundException;


public class Cuboid {

	private static int X;
	static {
		try {
			X = (int) parseYamlFile("Axes", 0);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	private static int Y;
	static {
		try {
			Y = (int) parseYamlFile("Axes", 1);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	private static int Z;
	static {
		try {
			Z = (int) parseYamlFile("Axes", 2);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	private static double [] [] [] threeDimArray ;
	private double [] [] [] copyThreeDimArray;
	private final double temperatureConstant = (double) parseYamlFile("Constant_Temperature", 0);
	private final int caseCuboid =  (int) parseYamlFile("Cuboid_Parameters", 0);
	private final double edgeLength = (double) parseYamlFile("Cuboid_Parameters",  1);
	private static double lowestValue;
	static {
		try {
			lowestValue = (double) parseYamlFile("Cuboid_Parameters", 2);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	private final double temperatureConductivity = (double) parseYamlFile("Cuboid_Parameters", 3);
	private final double timeDifferential = (double) parseYamlFile("Cuboid_Parameters", 4);
	private final double edgeTemperature= (double) parseYamlFile("Edge_Middle_Temperature", 0);
	private final double middleTemperature = (double) parseYamlFile("Edge_Middle_Temperature", 1);
	private final int height = (int) parseYamlFile("Gui_Measurements", 0);
	private final int width = (int) parseYamlFile("Gui_Measurements", 1);
	private final int iterations = (int) parseYamlFile("Simulation_Parameters", 0);
	private final int sleepTime = (int) parseYamlFile("Simulation_Parameters", 1);
	private final int numberOfThreads = (int) parseYamlFile("Simulation_Parameters", 2);

	private final int ThreadSelection = (int) parseYamlFile("Simulation_Parameters", 3);
	private final double sinusBasis = (double) parseYamlFile("Sinus_Temperature", 0);
	private final double sinusVariance = (double) parseYamlFile("Sinus_Temperature", 1);
	private static double highestValue;
	private Gui_Frame gui;


	public Cuboid() throws FileNotFoundException {
	}
	public void startQuader() throws FileNotFoundException {
		if (ThreadSelection == 0 ) {
			mainCycle();
		} if (ThreadSelection == 1 )  {
			runthreads();
		}
		if (ThreadSelection == 2 )  {
			sequentialRun();
		}
	}
	public void setCuboidAxes() {
		this.X= X + 2;
		this.Y = Y + 2;
		this.Z = Z + 2;

		if (caseCuboid == 1) {
			highestValue = temperatureConstant;
		}
		if (caseCuboid == 2) {
			highestValue = middleTemperature;
		}
		if (caseCuboid == 3) {
			highestValue = sinusBasis + sinusVariance;
		}
	}

	public void copyCreateQuader() {
		double [] [] [] temp = threeDimArray ;
		threeDimArray = copyThreeDimArray;
		copyThreeDimArray = temp;
	}
	
	public void createQuader() {
		threeDimArray = new double [X] [Z] [Y] ;
		copyThreeDimArray = new double [X] [Z] [Y] ;
		
		for(int x = 0 ; x < X; x++) {
			for(int z = 0; z < Z ; z++) {
				for(int y = 0; y < Y; y++ ) {
					threeDimArray[x][z][y] = lowestValue;
					copyThreeDimArray[x][z][y] = lowestValue;
				}
			}
		}
	}
	
	public void mainCycle() throws FileNotFoundException {
		//this.gui = new Gui_Frame(width, height);
		long start = System.nanoTime();
		Thread[] threads = new Thread [numberOfThreads];
		if (caseCuboid == 1) {
			heatSourceConstant();
		}
		else if (caseCuboid == 2) {
			heatSourceHotCenter();
		}


		for(int x = 0 ; x < iterations; x++) {
			//System.out.println("Iteration: " + (x + 1));

			if (caseCuboid == 3) {
				heatSourceSinus(x);
			}

			//sequentialRun();
			assignThreads(threads);
			copyCreateQuader();
//			printCuboid(threeDimArray);
//			printCrossSectionCuboid();
//			printHeatSource();
			/*gui.repaint(width, height);
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException ignored) { }*/
		}
//		printCuboid(threeDimArray);
		long end = System.nanoTime();
		long time= end - start;
		System.out.println("Time: "+ (TimeUnit.MILLISECONDS.convert(time, TimeUnit.NANOSECONDS))+"ms");
		//System.exit(0);
	}

	public void runthreads() throws FileNotFoundException  {
		this.gui = new Gui_Frame(width, height);
		long start = System.nanoTime();
		long end = 0;
		if (caseCuboid == 1) {
			heatSourceConstant();
		}
		else if (caseCuboid == 2) {
			heatSourceHotCenter();
		}


		ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(numberOfThreads);
		Future[] futures = new Future[numberOfThreads];
		for (int w = 0; w < iterations; w++) {
			if (caseCuboid == 3) {
				heatSourceSinus(w);
			}
			for (int l = 0; l < numberOfThreads; l++) {
				Runnable task = new ExecuterCuboidCalculationThreadpool(X,Y, Z, l,  threeDimArray, this.copyThreeDimArray, numberOfThreads, this.edgeLength, this.temperatureConductivity, this.timeDifferential);
				futures[l] = executor.submit(task);
			}

			for (int l = 0; l < numberOfThreads; l++) {
				try {
					futures[l].get();
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				} catch (ExecutionException e) {
					throw new RuntimeException(e);
				}
			}
			this.copyCreateQuader();
			gui.repaint(width, height);
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException ignored) { }
		}
		executor.shutdown();
		//printCuboid(threeDimArray);
		//this.ausgabeQuader();
		end = System.nanoTime();
		long time= end -start;
		System.out.println("Time: "+ (TimeUnit.MILLISECONDS.convert(time, TimeUnit.NANOSECONDS))+"ms");
		//System.exit(0);
	}

	public void heatSourceConstant() {
		for (int x = 1 ; x < X - 1 ; x++) {
			for(int z = 1 ; z < Z - 1; z++) {
				threeDimArray[x][z][0] = temperatureConstant;
				copyThreeDimArray[x][z][0] = temperatureConstant;
			}
		}
	}

	public void heatSourceHotCenter() {
		int width= this.X - 2;
		int height = this.Z- 2;
		boolean even_even = false;
		boolean even_odd = false;
		boolean odd_even = false;
		double deviationSteps = 0;

//		System.out.println("Width: " + width + "; Height: " + height);
		if (width % 2 == 0) {
			// even + even
			if (height % 2 == 0) {
//				System.out.println("Calculate: even_even");
				deviationSteps = (middleTemperature-edgeTemperature) / ((width/2-1) * (height/2-1));
				even_even = true;
			}

			// even + odd
			else {
//				System.out.println("Calculate: even_odd");
				deviationSteps = (middleTemperature-edgeTemperature) / ((width/2-1)*(height/2));
				even_odd = true;
			}
		} else {
			// odd + even
			if (height % 2 == 0) {
//				System.out.println("Calculate: odd_even");
				deviationSteps = (middleTemperature-edgeTemperature) / ((width/2)*(height/2-1));
				odd_even = true;
			}

			// odd + odd
			else {
//				System.out.println("Calculate: odd_odd");
				deviationSteps = (middleTemperature - edgeTemperature) / ((height/2) * (width/2));
			}
		}
//		System.out.println("deviationSteps: " + deviationSteps);
		for (int x = 0; x < width ; x++) {
			for (int z = 0; z < height ; z++) {
				int indexWidth = x;
				int indexHeight = z;
				//x = i
				// j == z
				while (indexWidth > width/2) {
					indexWidth = width-indexWidth-1;
				}

				while (indexHeight > height/2) {
					indexHeight = height-indexHeight-1;
				}

				if (even_even) {
					if (z == height / 2 && x == width / 2) {
						threeDimArray[x+1][z+1][0] = deviationSteps * (indexWidth-1) * (indexHeight-1) + edgeTemperature;
					} else if (z == height / 2) {
						threeDimArray[x+1][z+1][0] = deviationSteps * indexWidth * (indexHeight-1) + edgeTemperature;
					} else if (x == width / 2) {
						threeDimArray[x+1][z+1][0] = deviationSteps * (indexWidth-1) * indexHeight + edgeTemperature;
					} else {
						threeDimArray[x+1][z+1][0] = deviationSteps * indexWidth * indexHeight + edgeTemperature;
					}
				}

				else if (even_odd) {
					if (x == width / 2) {
						threeDimArray[x+1][z+1][0] = deviationSteps * (indexWidth-1) * indexHeight + edgeTemperature;
					} else {
						threeDimArray[x+1][z+1][0] = deviationSteps * indexWidth * indexHeight + edgeTemperature;
					}
				}

				else if (odd_even) {
					if (z == height / 2) {
						threeDimArray[x+1][z+1][0] = deviationSteps * indexWidth * (indexHeight-1) + edgeTemperature;
					} else {
						threeDimArray[x+1][z+1][0] = deviationSteps * indexWidth * indexHeight + edgeTemperature;
					}
				}

				else {
					threeDimArray[x+1][z+1][0]= deviationSteps * indexWidth * indexHeight + edgeTemperature;
				}
//				System.out.print(threeDimArray[x+1][z+1][0] + " ");
			}
//			System.out.println("");
		}
//		System.out.println("_______");
	}

	public void heatSourceSinus(int currentIteration) {
		double sin = Math.sin (currentIteration);
		double temperature = sinusBasis + sinusVariance * sin ;

		for (int x = 1; x < X -1 ; x++) {
			for(int z = 1 ; z < Z -1; z++) {
				threeDimArray[x][z][0] = temperature;
				copyThreeDimArray[x][z][0] = temperature;
			}
		}
	}

	public void assignThreads(Thread[] threads) {
		int start = 1;
		int end =  0;

		int value = (this.X-2)/numberOfThreads ;
		int modulur = (this.X-2) % numberOfThreads;
		boolean checkIfStart = false ;

		for(int i = 0 ; i < numberOfThreads ; i++) {
			if (modulur== 0) {
				if (!checkIfStart) {
					checkIfStart= true;
					end += value ;
				}
				else {
					start += value;
					end += value ;
				}
			}
			else {
				if( 0 == i ) {
					end = value *(i+1)+1;
				}
				else {
					if (modulur >= i+1) {
						start = start + value +1;
						end = end + value +1;
					} else  {
						if (!checkIfStart) {
							start= start +1;
							checkIfStart= true;
						}
						start = start + value;
						end = end + value;
					}
				}
			}
			//System.out.println("id: "+ i+" |start: "+ start+ "| end: "+end );
			threads[i] = new Thread(new ExecutorCuboidCalculation(threeDimArray, copyThreeDimArray, start, end , Z, Y, edgeLength, temperatureConductivity, timeDifferential));
			threads[i].start();
		}

		for (int i = 0; i < numberOfThreads; i++) {
			try {
				threads[i].join();
			}
			catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}



	public static double[][][] getArray() { return threeDimArray;}

	public static double getHighestValue() { return highestValue;}

	public static double getLowestValue() { return lowestValue;}

	public static int getXValue() {return X;}

	public static int getZValue() {return Z;}

	public static int getYValue() {return Y;}

	public static Object parseYamlFile(String key, int index) throws FileNotFoundException {
		InputStream inputStream = new FileInputStream("config.yaml");
		Yaml yaml = new Yaml();
		Map<String, Object> data = yaml.load(inputStream);
		final LinkedHashMap<String, Object> testMap = (LinkedHashMap<String, Object>) data.get(key);
		Set<String> keySet = testMap.keySet();
		Object[] keyArray = keySet.toArray(new Object[keySet.size()]);
		Object keyResult = keyArray[index];
		Object result = testMap.get(keyResult);
		return result;
	}


	/**
	 * Unused (print)-functions
	 */
	public void printCuboid(double [][][] arrayToPrint) {
		for(int x = 0 ; x < X; x++) {
			for(int z = 0; z < Z ; z++) {
				for(int y = 0; y < Y; y++ ) {
					System.out.print(arrayToPrint[x][z][y] + "  ");
				}
				System.out.println("-");
			}
			System.out.println("x");
		}
	}

	public void printCrossSectionCuboid() {
		int z = Z / 2 ;
		for(int x = 0; x < X  ; x++) {
			for(int y = 0; y < Y ; y++ ) {
				System.out.print(threeDimArray[x][z][y] + "  ");
			}
			System.out.println("");
		}
	}

	public void printHeatSource() {
		for (int x = 1 ; x < X - 1 ; x++) {
			for(int z = 1 ; z < Z - 1; z++) {
				System.out.print(threeDimArray[x][z][0] + "  ");
			}
			System.out.println("");
		}
		System.out.println("--------------");
	}

	public void sequentialRun() {
			for(int x = 1 ; x < X - 1; x++) {
				for(int z = 1; z < Z- 1 ; z++) {
					for(int y = 1; y < Y - 1; y++ ) {
						threeDimArray[x][z][y] = (copyThreeDimArray[x][z][y] + ((timeDifferential * timeDifferential)/(edgeLength * edgeLength))*
								(copyThreeDimArray[x-1][z][y] + copyThreeDimArray[x+1][z][y]+ copyThreeDimArray[x][z-1][y]+ copyThreeDimArray[x][z+1][y]+
										copyThreeDimArray[x][z][y-1]+ copyThreeDimArray[x][z][y+1] - (6*copyThreeDimArray[x][z][y] )));
					}
				}
			}
	}
}