/**
 * Created by l7861 on 2016/5/16.
 */
package ExtendPSqrt;

import javafx.util.Pair;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import static java.lang.System.out;


public class Example{
    static double[] quartileList;
    static String[] filter={"rs","cs","le"};
    static{
        int count=6;
        quartileList = new double[count-1];
//        for(int i=1;i<count;i++) {sim
//            quartileList[i-1] = i * 1.0 / count;
//        }
        int i=0;
        quartileList[i++]=0.5;
        quartileList[i++]=0.7;
        quartileList[i++]=0.8;
        quartileList[i++]=0.9;
        quartileList[i++]=0.95;
    }
    public static void main(String[] args) {
//        ArrayList<Double> datas=new ArrayList<>();
//        for(double i=0;i<100;i++){
//            datas.add(i);
//        }
//        Hist h=new Hist(datas);
//        for(double[] d:h.getResult()){
//            System.out.printf("%f,%f\n",d[0],d[1]);
//        }

//        hist(new ArrayList<>());
//        randomReadFile();
//        readRealData("F:\\workspace_code\\java\\Test\\src\\ExtendPSqrt\\resource\\realData\\-DYWYWA_1C8\\cs_data.txt");
        readRealData("C:\\Users\\l7861\\Desktop\\7-le_data.txt");
//        readRealDataDir("F:\\workspace_code\\java\\Test\\src\\ExtendPSqrt\\resource\\realData_1_1000000000");
    }

    public static void readRealDataDir(String root){
        File file=new File(root);
        String files[]=file.list();
        for(String filedir:files){
            boolean [] skip=new boolean[3];
            try {
                Scanner sc=new Scanner(new FileInputStream(new File(root+"\\"+filedir+"\\err_count")));
                int i=0;
                while(sc.hasNext()){
                    String line=sc.nextLine();
                    String[] lines=line.split("\t");
                    if(Integer.parseInt(lines[3])-Integer.parseInt(lines[1])<200){
                        skip[i]=true;
                    }
                    i++;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            int i=0;
            for(String fileName:filter){
                if(skip[i++]) continue;
                readRealData(root+"\\"+filedir+"\\"+fileName+"_data.txt");
            }
        }
    }

    private static void readRealData(String fileName){
//        File file=new File(fileName);
//        if(file.isFile()){
//            try{
//                Scanner sc = new Scanner(new FileInputStream(new File(fileName)));
//                QuartileMerge qm=new QuartileMerge(quartileList);
//                ExtenedPSqrt eps=new ExtenedPSqrt(quartileList);
//                ArrayList<Double> actualDataList=new ArrayList<>();
//                int i=0;
//                for(;sc.hasNext();i++){
//                    double data=sc.nextDouble();
//                    eps.pushData(data);
//                    actualDataList.add(data);
//                }
//                ArrayList<Pair> points=eps.getPoints();
//                PrintStream fileOut=new PrintStream(new File(file.getParentFile()+"\\psqrt_error_"+file.getName()));
//                Collections.sort(actualDataList);
//                double avg=getAverager(actualDataList);
//                for(Pair<Double,Double> point:points){
//                    double[] err=getError(point,actualDataList);
//                    err[1]/=avg;
//                    out.printf("%6f,%6f,%6f\n",point.getKey(),err[0],err[1]);
//                    fileOut.printf("%6f,%6f,%6f\n",point.getKey(),err[0],err[1]);
//                }
//                fileOut.flush();
//                fileOut.close();
//
//                fileOut=new PrintStream(new File(file.getParentFile()+"\\psqrt_markers_"+file.getName()));
//                qm.add(eps.getPoints(),i);
//                for(Pair<Double,Double> point:qm.getPoints()){
//                    out.printf("%6f,%6f\n",point.getKey(),point.getValue());
//                    fileOut.printf("%6f,%6f\n",point.getKey(),point.getValue());
//                }
//                fileOut.flush();
//                fileOut.close();
//
//                fileOut=new PrintStream(new File(file.getParentFile()+"\\psqrt_simPoint_"+file.getName()));
//                for(double d:qm.getSimPoint()){
//                    out.println(d);
//                    fileOut.println(d);
//                }
//                fileOut.flush();
//                fileOut.close();
//
//            }catch (Exception e){
//
//            }
//        }
    }

    public static double getAverager(ArrayList<Double> list){
        return list.stream().mapToDouble(i -> i).average().getAsDouble();
    }

    public static void hist(ArrayList<Integer> bins){
//        File file=new File("F:\\workspace_code\\java\\Test\\resource\\burrData\\");
//        String files[];
//        files=file.list();
//        QuartileMerge qm=new QuartileMerge(quartileList);
//        qm.setSampleValue(1000);
//        ArrayList<Double> datas=new ArrayList<>();
//        for(String fileName:files){
//            ExtenedPSqrt eps=new ExtenedPSqrt(quartileList);
//            fileName=file.getPath()+"\\"+fileName;
//            try {
//                Scanner sc = new Scanner(new FileInputStream(new File(fileName)));
//                int i;
//                for(i=0;sc.hasNext();i++){
//                    double data=sc.nextDouble()*10;
//                    datas.add(data);
//                    eps.pushData(data);
//                }
//                qm.add(eps.getPoints(),i);
//            }catch (Exception e){
//
//            }
//        }
//        Hist h=new Hist(datas);
//        for(double[] d:h.getResult()){
//            out.printf("%f,%f\n",d[0],d[1]);
//        }
//        out.println(qm.getPoints());
//        for(double[] d:qm.getHist()){
//            out.printf("%f,%f\n",d[0],d[1]);
//        }
    }
    public static void randomReadFile(){
//        ExtenedPSqrt eps1=new ExtenedPSqrt(quartileList);
//        ExtenedPSqrt eps2=new ExtenedPSqrt(quartileList);
//        ArrayList<Double> actualDataList=new ArrayList<>();
//        try {
//            Scanner sc=new Scanner(new FileInputStream(new File("F:\\workspace_code\\java\\Test\\resource\\2.txt")));
//            int start_position=(int)(Math.random()*20000);
//            int i=0;
//            for(;sc.hasNext()&&i<start_position;i++){
//                sc.next();
//            }
//            int index=0;
//            while(sc.hasNext()&&index<20000){
//                double data=sc.nextDouble();
//                actualDataList.add(data*10);
//                eps1.pushData(data*10);
//                index++;
//            }
//            for(;sc.hasNext()&&i+50000<start_position;i++){
//                sc.next();
//            }
//            index=0;
//            while(sc.hasNext()&&index<20000){
//                double data=sc.nextDouble();
//                actualDataList.add(data*10);
//                eps2.pushData(data*10);
//            }
//            out.println(eps1.getPoints());
//            out.println(eps2.getPoints());
//            QuartileMerge qm=new QuartileMerge(quartileList);
//            qm.add(eps1.getPoints(),2000);
//            qm.add(eps2.getPoints(),2000);
//            qm.setSampleValue(1000);
//
//            Collections.sort(actualDataList);//查询误差前必须进行排序
//            ArrayList<Pair<Double,Double>> points=qm.getPoints();
//            for(Pair<Double,Double> point:points){
//                double[] err=getError(point,actualDataList);
//                out.printf("%6f,%6f,%6f\n",point.getKey(),err[0],err[1]);
//            }
//            out.println(points);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
    }

    /*
    **计算合并之后的横坐标与真是横坐标之间的误差
     */
    public static double[] getError(Pair<Double,Double> point,ArrayList<Double> actualDataList){
        int m=0;
         double [] result=new double[2];
        for(;m<actualDataList.size();m++){
            if(actualDataList.get(m)>=point.getValue()){
                break;
            }
        }
        result[0]=(m*1.0/actualDataList.size()-point.getKey())/2;
        result[1]=(point.getValue()-actualDataList.get((int)Math.round(point.getKey()*(actualDataList.size()-1))));
        return result;
    }
}