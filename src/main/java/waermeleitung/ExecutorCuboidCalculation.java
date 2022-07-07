package waermeleitung;


public class ExecutorCuboidCalculation implements Runnable {
    public double [] [] [] threeDimArray ;
    public double [] [] [] copyThreeDimArray ;
    private final int start;
    private final int end;
    private final int Z;
    private final int Y;
    private final double edgeLength;
    private final double temperatureConductivity;
    private final double timeDifferential;


    public ExecutorCuboidCalculation(double[][][] threeDimArray, double[][][] copyThreeDimArray, int start, int end , int Z, int Y, double edgeLength, double temperatureConductivity, double timeDifferential) {
        this.threeDimArray = threeDimArray;
        this.copyThreeDimArray = copyThreeDimArray;
        this.start = start;
        this.end = end;
        this.Z= Z;
        this.Y= Y;
        this.edgeLength= edgeLength;
        this.temperatureConductivity= temperatureConductivity;
        this.timeDifferential= timeDifferential;
    }

    @Override
    public void run() {

        for(int x = start ; x <= end ; x++) {
            for(int z = 1; z < Z -1 ; z++) {
                for(int y = 1; y < Y -1; y++ ) {
                    threeDimArray[x][z][y] = (copyThreeDimArray[x][z][y] + ((temperatureConductivity * timeDifferential)/(edgeLength * edgeLength))*
                            (copyThreeDimArray[x-1][z][y] + copyThreeDimArray[x+1][z][y]+ copyThreeDimArray[x][z-1][y]+ copyThreeDimArray[x][z+1][y]+
                                    copyThreeDimArray[x][z][y-1]+ copyThreeDimArray[x][z][y+1] - (6*copyThreeDimArray[x][z][y] )));
                }
            }
        }
    }
}
