/**
 * Created by l7861 on 2016/5/16.
 */
package ExtendPSqrt;

import javafx.util.Pair;

import java.util.ArrayList;


class ExtenedPSqrt {
    private ArrayList<Double> quartileList;
    private ArrayList<Double> q;//marker纵坐标
    private ArrayList<Double> dn;//每个marker所代表的分位置
    private ArrayList<Double> np;//marker参与计算的横坐标
    private  ArrayList<Integer> n;//真实横坐标
    private int marker_count;
    private int count;
    ExtenedPSqrt(ArrayList<Double> quartileList){
        this.quartileList=quartileList;
        initMarkers();
    }
    public ArrayList<Pair> getPoints(){
        ArrayList<Pair> result=new ArrayList<Pair>();
        for(int i=0;i<marker_count;i++){
            result.add(new Pair(dn.get(i),q.get(i)));
        }
        return result;
    }
    private void initMarkers(){
        this.dn=new ArrayList<>();
        this.q=new ArrayList<>();
        this.np=new ArrayList<>();
        this.n=new ArrayList<>();
        int quartile_count=this.quartileList.size();
        marker_count=quartile_count*2+3;
        this.dn.add(0.0);
        for(int i=0;i<quartile_count;i++){
            double marker=this.quartileList.get(i);
            double pro_marker=(marker+dn.get(i*2))/2;
            dn.add(pro_marker);
            dn.add(marker);
        }
        dn.add((1+this.quartileList.get(quartile_count-1))/2);
        this.dn.add(1.0);
        for( int i = 0; i < marker_count; i ++ ) {
            this.np.add((marker_count - 1) * dn.get(i) + 1);
            this.n.add(0);
            this.q.add(0.0);
        }
    }
    private int sign(double d){ return d>=0 ?  1: 0;}
    private double parabolic(int i,int d){
        return q.get(i) + d / (double)(n.get(i + 1 ) - n.get(i - 1 )) * ((n.get(i) - n.get(i - 1 ) + d) * (q.get(i + 1 ) - q.get(i) ) / (n.get( i + 1) - n.get(i) ) + (n.get(i + 1 ) - n.get(i) - d) * (q.get(i) - q.get(i - 1 )) / (n.get(i) - n.get(i - 1 )) );
    }
    private double linear(int i,int d){
        return q.get(i) + d * (q.get(i+d) - q.get(i) ) / (n.get(i+d) - n.get(i) );
    }
    private void p2_sort(ArrayList<Double> q,int count){
        double k;
        int i, j;
        for( j = 1; j < count; j ++ ) {
            k = q.get(j);
            i = j - 1;

            while( i >= 0 && q.get(i) > k ) {
                q.set(i+1,q.get(i));
                i --;
            }
            q.set(i+1,k);
        }
    }
    public void pushData(double data){
        int i,k=0;
        double d,newq;
        if(count>=marker_count){
            count++;
            if(data<q.get(0)){
                q.set(0,data);
                k=1;
            }else if(data>q.get(marker_count-1)){
                q.set(marker_count-1,data);
                k=marker_count-1;
            }else{
                for(i=1;i<marker_count;i++){
                    if(data<q.get(i)){
                        k=i;
                        break;
                    }
                }
            }
            //
            for(i=k;i<marker_count;i++){
                n.set(i,n.get(i)+1);
                np.set(i,np.get(i)+dn.get(i));
            }
            for(i=0;i<k;i++){
                np.set(i,np.get(i)+dn.get(i));
            }

            for(i=1;i<marker_count-1;i++){
                d=np.get(i)-n.get(i);
                if((d>=1.0&&n.get(i+1)-n.get(i)>1)||(d<=-1.0&&n.get(i-1)-n.get(i)<-1.0)){
                    newq=parabolic(i,sign(d));
                    if(q.get(i-1)<newq&&newq<q.get(i+1)){
                        q.set(i,newq);
                    }else{
                        q.set(i,linear(i,sign(d)));
                    }
                    n.set(i,n.get(i)+sign(d));
                }
            }
        }else{
            q.set(count,data);
            count++;
            if(count==marker_count){
                p2_sort(q,marker_count);
                for(i=0;i<marker_count;i++){
                    n.set(i,i+1);
                }
            }
        }
    }
    public double result() {
        if(marker_count!=5){
            System.out.println("Multiple quantiles not return result by defult,please add quantiles params");
        }
        return result(dn.get(2));
    }
    public double result(double quantile){
        if(count<marker_count){
            int closest=1;
            p2_sort(q,count);
            for(int i=2;i<count;i++){
                if((Math.abs((double)i)/count-quantile)<Math.abs(((double)closest)/marker_count-quantile))
                    closest=i;
            }
            return q.get(closest);
        }else{
            int closest=1;
            for(int i=2;i<marker_count-1;i++){
                if((Math.abs(dn.get(i))-quantile)<Math.abs(dn.get(closest)-quantile)){
                    closest=i;
                }
            }
            return q.get(closest);
        }
    }
}