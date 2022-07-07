package waermeleitung;

public class ExecuterCuboidCalculationThreadpool implements Runnable  {
    private int numberOfThreads ;
    public double [] [] [] threeDimArray ;
    public double [] [] [] copyThreeDimArray ;

    private int xAxes;
    private int yAxes;
    private int zAxes;
    private int id;

    private final double edgeLength;
    private final double temperatureConductivity;
    private final double timeDifferential;

    private int counter=0;

    public ExecuterCuboidCalculationThreadpool(int x, int y , int z, int id, double[][][] threeDimArray, double[][][] copyThreeDimArray,int  numberOfThreads, double edgeLength, double temperatureConductivity, double timeDifferential) {
        this.xAxes= x;
        this.yAxes=y;
        this.zAxes=z;
        this.id=id;
        this.threeDimArray=threeDimArray;
        this.copyThreeDimArray=copyThreeDimArray;
        this.numberOfThreads= numberOfThreads;
        this.counter=id+1;
        this.edgeLength=edgeLength;
        this.temperatureConductivity= temperatureConductivity;
        this.timeDifferential=timeDifferential;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub

        while (counter < xAxes -1) {
            for(int z = 1; z < this.zAxes -1 ; z++) {
                for(int y = 1; y < this.yAxes -1; y++ ) {
                    threeDimArray[counter][z][y] = (copyThreeDimArray[counter][z][y] + ((this.temperatureConductivity *this.timeDifferential)/(this.edgeLength* this.edgeLength))*
                            (copyThreeDimArray[counter-1][z][y] + copyThreeDimArray[counter+1][z][y]+ copyThreeDimArray[counter][z-1][y]+ copyThreeDimArray[counter][z+1][y]+
                                    copyThreeDimArray[counter][z][y-1]+ copyThreeDimArray[counter][z][y+1] - (6*copyThreeDimArray[counter][z][y] )));
                }
            }
            counter = counter + numberOfThreads;
        }
    }
}
