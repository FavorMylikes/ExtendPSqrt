/**
 * Created by l7861 on 2016/5/16.
 */
package ExtendPSqrt;

import javafx.util.Pair;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;


public class Example{
    static double[] quartileList;
    static{
        quartileList = new double[5];
        int i=0;
        quartileList[i++]=0.5;
        quartileList[i++]=0.7;
        quartileList[i++]=0.8;
        quartileList[i++]=0.9;
        quartileList[i++]=0.95;
    }
    public static void main(String[] args) {
//        hist(new ArrayList<>());
        randomReadFile();
    }
    public static void hist(ArrayList<Integer> bins){
        File file=new File("F:\\workspace_code\\java\\Test\\resource\\burrData\\");
        String files[];
        files=file.list();
        QuartileMerge qm=new QuartileMerge(quartileList);
        qm.setSampleValue(1000);
        for(String fileName:files){
            ExtenedPSqrt eps=new ExtenedPSqrt(quartileList);
            fileName=file.getPath()+"\\"+fileName;
            try {
                Scanner sc = new Scanner(new FileInputStream(new File(fileName)));
                int i;
                for(i=0;sc.hasNext();i++){
                    eps.pushData(sc.nextDouble());
                }
                qm.add(eps.getPoints(),i);
            }catch (Exception e){

            }
        }
        System.out.println(qm.getPoints());
    }
    public static void randomReadFile(){
        ExtenedPSqrt eps1=new ExtenedPSqrt(quartileList);
        ExtenedPSqrt eps2=new ExtenedPSqrt(quartileList);
        ArrayList<Double> actualDataList=new ArrayList<>();
        try {
            Scanner sc=new Scanner(new FileInputStream(new File("F:\\workspace_code\\java\\Test\\resource\\2.txt")));
            int start_position=(int)(Math.random()*20000);
            int i=0;
            for(;sc.hasNext()&&i<start_position;i++){
                sc.next();
            }
            int index=0;
            while(sc.hasNext()&&index<20000){
                double data=sc.nextDouble();
                actualDataList.add(data);
                eps1.pushData(data);
                index++;
            }
            for(;sc.hasNext()&&i+50000<start_position;i++){
                sc.next();
            }
            index=0;
            while(sc.hasNext()&&index<20000){
                double data=sc.nextDouble();
                actualDataList.add(data);
                eps2.pushData(data);
            }
            System.out.println(eps1.getPoints());
            System.out.println(eps2.getPoints());
            QuartileMerge qm=new QuartileMerge(quartileList);
            qm.add(eps1.getPoints(),2000);
            qm.add(eps2.getPoints(),2000);
            qm.setSampleValue(1000);

            Collections.sort(actualDataList);
            ArrayList<Pair<Double,Double>> points=qm.getPoints();
            for(Pair<Double,Double> point:points){
                int m=0;
                for(;m<actualDataList.size();m++){
                    if(actualDataList.get(m)>=point.getValue()){
                        break;
                    }
                }
                System.out.printf("q:%6f,err:%6f\n",point.getKey(),(m*1.0/actualDataList.size()-point.getKey())/2);//计算合并之后的横坐标与真是横坐标之间的误差
            }
            System.out.println(points);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}