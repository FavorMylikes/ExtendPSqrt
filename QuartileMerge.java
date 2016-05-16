/**
 * Created by l7861 on 2016/5/16.
 */
package ExtendPSqrt;

import javafx.util.Pair;

import java.util.*;


class QuartileMerge{
    private HashMap<ArrayList<Pair>,Integer> data=new HashMap<>();//分位置的横纵坐标及count数
    public ArrayList<Double> pointy=null;//拟合的数据
    private int maxCount=0;
    private ArrayList<Double> quartileList;
    private ArrayList<Double> dn;//每个marker所代表的分位置
    private int marker_count;
    private int k=1000;//取样参数，最长数列的抽样数目
    private ArrayList<Integer> buckets=null;//直方图桶的宽
    QuartileMerge(ArrayList<Double> quartileList){
        this.quartileList=quartileList;
        initMarkers();
    }
    public void setSampleValue(int k) {
        this.k = k;
    }
    public void add(ArrayList<Pair> quartilePoints,int count){
        if(maxCount<count){
            maxCount=count;
        }
        this.data.put(quartilePoints,count);
    }

    private void dataGen(){
        double inarr=1.0*maxCount/this.k;
        pointy=new ArrayList<>();
        for(Map.Entry<ArrayList<Pair>,Integer> d:this.data.entrySet()) {
            LinkedList<Double> markerx=new LinkedList<>();
            LinkedList<Double> markery=new LinkedList<>();
            Pair p0=d.getKey().get(0);
            Pair p1=d.getKey().get(1);
            int curveLength=d.getValue();
            markerx.add((Double) p0.getKey()*curveLength);
            markerx.add((Double) p1.getKey()*curveLength);
            markery.add((Double) p0.getValue());
            markery.add((Double) p1.getValue());
            int length=d.getKey().size();
            for(int i=2,j=0;i<length;i++){
                double x=(Double) d.getKey().get(i).getKey();
                double y=(Double) d.getKey().get(i).getValue();
                markerx.add(x*curveLength);
                markery.add(y);
                double[] params=lagrange(markerx,markery);

                for(;j<x*curveLength;j+=inarr){
                    pointy.add(curve(params,j));
                }
                markerx.poll();
                markery.poll();
            }
        }
    }

    private double[] lagrange(List<Double> pointx, List<Double> pointy){
        double x1=pointx.get(0);
        double x2=pointx.get(1);
        double x3=pointx.get(2);
        double y1=pointy.get(0);
        double y2=pointy.get(1);
        double y3=pointy.get(2);
        double p=y1/((x1-x2)*(x1-x3));
        double q=y2/((x2-x1)*(x2-x3));
        double r=y3/((x3-x1)*(x3-x2));
        double a=p+q+r;
        double b=-p*(x2+x3)-q*(x1+x3)-r*(x1+x2);
        double c=p*x2*x3+q*x1*x3+r*x1*x2;
        double []result={a,b,c};
        return result;
    }
    private double curve(double[] params,double x){
        return params[0]*x*x+params[1]*x+params[2];
    }
    private double getQuartile(double quartile){
        int count=pointy.size()-2;
        int position=(int)(quartile*count);
        double a=pointy.get(position);
        double b=pointy.get(position+1);
        return (b-a)*(quartile*(count-1)-position)+a;
    }

    public TreeMap<Double,Double> getPoints(){
        TreeMap<Double,Double> result=new TreeMap<Double, Double>();
        if(this.pointy==null){
            dataGen();//设定取样参数，值越大越精确
        }
        Collections.sort(pointy);
        for(double quartile:dn){
            result.put(quartile,getQuartile(quartile));
        }
        return result;
    }

    public ArrayList<Pair<Integer,Double>> hist(ArrayList<Integer> buckets){
        setHistBuckets(buckets);
        return null;
    }

    public ArrayList<Pair<Integer,Double>> hist(){
        return null;
    }

    public void setHistBuckets(ArrayList<Integer> buckets){
        this.buckets=(ArrayList<Integer>)buckets.clone();
        Collections.sort(this.buckets);
        if(this.buckets.get(0)!=0){
            this.buckets.add(0,0);
        }

    }

    private void initMarkers(){
        this.dn=new ArrayList<>();
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
    }
}