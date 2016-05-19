package ExtendPSqrt;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by l7861 on 2016/5/17.
 */
public class Hist {
    private ArrayList<Double> datas=null;
    private double[] buckets=null;//直方图桶的宽

    public void setBucketsCount(int bucketsCount) {
        this.bucketsCount = bucketsCount;
    }

    private int bucketsCount=20;
    Hist(ArrayList<Double> datas,double[] buckets){
        setHistBuckets(buckets);
        this.datas=datas;
    }

    Hist(ArrayList<Double> datas){
        double[] buckets=new double[bucketsCount+1];
        double max=Collections.max(datas);
        double min=Collections.min(datas);
        double inarr=(max-min)/bucketsCount;
        int index=0;
        for(double i=min-inarr/2;index<bucketsCount+1;i+=inarr){
            buckets[index++]=i;
        }
        setHistBuckets(buckets);
        this.datas=datas;
    }
    public void setHistBuckets(double[] buckets){
        this.buckets=buckets;
    }

    public double [][] getResult(){
        double [][] result=new double[bucketsCount+1][2];
        for(int i=0;i<bucketsCount+1;i++){
            result[i][0]=buckets[i];
            result[i][1]=0;
        }
        for(double d:this.datas){
            int index=0;
            while(d>buckets[++index]){
                if(index+1>bucketsCount){
                    index=bucketsCount+1;
                    break;
                }
            }
            result[--index][1]++;
        }
        return result;
    }
}
