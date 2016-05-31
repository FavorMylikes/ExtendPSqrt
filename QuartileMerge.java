/**
 * Created by l7861 on 2016/5/16.
 */
package ExtendPSqrt;

import javafx.util.Pair;

import java.util.*;


class QuartileMerge{
    private double[] quartileList;
    private double[] dn;//每个marker所代表的分位置
    private int maxCount = 0;
    private int k = 1000;//取样参数，最长数列的抽样数目
    public ArrayList<Double> simPoint = null;//拟合的数据
    private ArrayList<QuantileP2> MergeList = new ArrayList<QuantileP2>();//分位置的横纵坐标及count数

    QuartileMerge(double[] quartileList){
        this.quartileList=quartileList;
        initMarkers();
    }

    public ArrayList<Double> getSimPoint() {
        if(simPoint==null){
            dataGen();//设定取样参数，值越大越精确
        }
        return simPoint;
    }

    public void setSampleValue(int k) {
        this.k = k;
    }
    public void merge(QuantileP2 q) {
        int count = q.getCount();
        if (maxCount < count) {
            maxCount = count;
        }
        MergeList.add(q);
    }

    public void merge(double[] quantiles, double[] p2_q, int count) {
        QuantileP2 q=new QuantileP2(quantiles,p2_q,count);
        if (maxCount < count) {
            maxCount = count;
        }
        MergeList.add(q);
    }

    private void dataGen() {
        double inarr = 1.0 * (maxCount) / this.k;
        simPoint = new ArrayList<Double>();
        for (QuantileP2 q : MergeList) {
            double[] quartileList=q.getQuartileList();
            double[] markers=q.markers();
            int curveLength = q.getCount();
            int length = markers.length;
            double j = 0;
            for (int i = 3; i <= length; i++) {
                double [] markerx= Arrays.copyOfRange(quartileList,i-3,i);
                double [] markery= Arrays.copyOfRange(markers,i-3,i);
                double[] params = lagrange(markerx, markery);
                for (; j < markerx[2] * curveLength; j += inarr) {
                    double data = curve(params, j/curveLength);
                    simPoint.add(data);
                }
            }
        }
    }

    private double[] lagrange(double[] pointx, double[] pointy) {
        double x1 = pointx[0];
        double x2 = pointx[1];
        double x3 = pointx[2];
        double y1 = pointy[0];
        double y2 = pointy[1];
        double y3 = pointy[2];
        double p = y1 / ((x1 - x2) * (x1 - x3));
        double q = y2 / ((x2 - x1) * (x2 - x3));
        double r = y3 / ((x3 - x1) * (x3 - x2));
        double a = p + q + r;
        double b = -p * (x2 + x3) - q * (x1 + x3) - r * (x1 + x2);
        double c = p * x2 * x3 + q * x1 * x3 + r * x1 * x2;
        double[] result = {a, b, c};
        return result;
    }
    private double curve(double[] params,double x){
        return params[0]*x*x+params[1]*x+params[2];
    }
    public double getQuartile(double quartile){
        int count=simPoint.size()-2;
        int position=(int)(quartile*count);
        double a=simPoint.get(position);
        double b=simPoint.get(position+1);
        return (b-a)*(quartile*(count-1)-position)+a;
    }

    public ArrayList<Pair<Double,Double>> getPoints(){
        ArrayList<Pair<Double,Double>> result=new ArrayList<Pair<Double,Double>>();
        if(this.simPoint==null){
            dataGen();
        }
        Collections.sort(simPoint);
        for(double quartile:dn){
            result.add(new Pair(quartile,getQuartile(quartile)));
        }
        return result;
    }

    private void initMarkers(){
        int quartile_count=this.quartileList.length;
        int marker_count=quartile_count*2+3;
        this.dn=new double[marker_count];
        this.dn[0]=.0;
        for(int i=0;i<quartile_count;i++){
            double marker=this.quartileList[i];
            dn[i*2+1]=(marker+dn[i*2])/2;
            dn[i*2+2]=marker;
        }
        dn[marker_count-2]=(1+this.quartileList[quartile_count-1])/2;
        this.dn[marker_count-1]=1.0;
    }
    /*
    计算拟合直方图[(left_bin,count),(left_bin,count)..]
     */
    public double[][] getHist(){
        Hist h=new Hist(simPoint);
        double inarr=1.0*maxCount/this.k;
        double [][] result=h.getResult();
        for(double[] d:result){
            d[1]*=inarr;
        }
        return result;
    }
}