/**
 * Created by l7861 on 2016/5/16.
 */
package ExtendPSqrt;

import javafx.util.Pair;

import java.util.ArrayList;


class ExtenedPSqrt {
    private double[] quartileList;
    private double[] q;//marker纵坐标
    private double[] dn;//每个marker所代表的分位置
    private double[] np;//marker参与计算的横坐标
    private  int[] n;//真实横坐标
    private int marker_count;
    private int count;
    ExtenedPSqrt(double[] quartileList){
        this.quartileList=quartileList;
        initMarkers();
    }
    public ArrayList<Pair> getPoints(){
        ArrayList<Pair> result=new ArrayList<Pair>();
        for(int i=0;i<marker_count;i++){
            result.add(new Pair(dn[i],q[i]));
        }
        return result;
    }
    private void initMarkers(){
        int quartile_count=this.quartileList.length;
        marker_count=quartile_count*2+3;
        this.dn=new double[marker_count];
        this.q=new double[marker_count];
        this.np=new double[marker_count];
        this.n=new int[marker_count];

        this.dn[0]=0.0;
        for(int i=0;i<quartile_count;i++){
            double marker=this.quartileList[i];
            dn[i*2+1]=(marker+dn[i*2])/2;
            dn[i*2+2]=marker;
        }
        dn[marker_count-2]=(1+this.quartileList[quartile_count-1])/2;
        this.dn[marker_count-1]=1.0;
        for( int i = 0; i < marker_count; i ++ ) {
            this.np[i]=((marker_count - 1) * dn[i] + 1);
            this.n[i]=0;
            this.q[i]=.0;
        }
    }
    private int sign(double d){ return d>=0 ?  1: 0;}
    private double parabolic(int i,int d){
        return q[i] + d / (double)(n[i+1] - n[i-1]) * ((n[i] - n[i-1] + d) * (q[i+1] - q[i] ) / (n[i+1] - n[i] ) + (n[i+1] - n[i] - d) * (q[i] - q[i-1]) / (n[i] - n[i-1]) );
    }
    private double linear(int i,int d){
        return q[i] + d * (q[i+d] - q[i] ) / (n[i+d] - n[i] );
    }
    private void p2_sort(double[] q,int count){
        double k;
        int i, j;
        for( j = 1; j < count; j ++ ) {
            k = q[j];
            i = j - 1;

            while( i >= 0 && q[i] > k ) {
                q[i+1]=q[i];
                i --;
            }
            q[i+1]=k;
        }
    }
    public void pushData(double data){
        int i,k=0;
        double d,newq;
        if(count>=marker_count){
            count++;
            if(data<q[0]){
                q[0]=data;
                k=1;
            }else if(data>q[marker_count-1]){
                q[marker_count-1]=data;
                k=marker_count-1;
            }else{
                for(i=1;i<marker_count;i++){
                    if(data<q[i]){
                        k=i;
                        break;
                    }
                }
            }
            //
            for(i=k;i<marker_count;i++){
                n[i]=n[i]+1;
                np[i]=np[i]+dn[i];
            }
            for(i=0;i<k;i++){
                np[i]=np[i]+dn[i];
            }

            for(i=1;i<marker_count-1;i++){
                d=np[i]-n[i];
                if((d>=1.0&&n[i+1]-n[i]>1)||(d<=-1.0&&n[i-1]-n[i]<-1.0)){
                    newq=parabolic(i,sign(d));
                    if(q[i-1]<newq&&newq<q[i+1]){
                        q[i]=newq;
                    }else{
                        q[i]=linear(i,sign(d));
                    }
                    n[i]=n[i]+sign(d);
                }
            }
        }else{
            q[count]=data;
            count++;
            if(count==marker_count){
                p2_sort(q,marker_count);
                for(i=0;i<marker_count;i++){
                    n[i]=i+1;
                }
            }
        }
    }
    public double result() {
        if(marker_count!=5){
            System.out.println("Multiple quantiles not return result by defult,please add quantiles params");
        }
        return result(dn[2]);
    }
    public double result(double quantile){
        if(count<marker_count){
            int closest=1;
            p2_sort(q,count);
            for(int i=2;i<count;i++){
                if((Math.abs((double)i)/count-quantile)<Math.abs(((double)closest)/marker_count-quantile))
                    closest=i;
            }
            return q[closest];
        }else{
            int closest=1;
            for(int i=2;i<marker_count-1;i++){
                if((Math.abs(dn[i])-quantile)<Math.abs(dn[closest]-quantile)){
                    closest=i;
                }
            }
            return q[closest];
        }
    }
}