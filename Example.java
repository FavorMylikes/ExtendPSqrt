/**
 * Created by l7861 on 2016/5/16.
 */
package ExtendPSqrt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;


public class Example{
    static ArrayList<Double> quartileList;
    static{
        quartileList = new ArrayList<>();
        quartileList.add(0.5);
        quartileList.add(0.7);
        quartileList.add(0.8);
        quartileList.add(0.9);
        quartileList.add(0.95);
    }
    public static void main(String[] args) {
        ArrayList<Integer> data=new ArrayList<>();
        for(int i = 0;i<10;i++){
            data.add((int)(Math.random()*100));
        }
        ArrayList<Integer> data2 = (ArrayList<Integer>) data.clone();
        Collections.sort(data2);
        for(int i = 0;i<10;i++){
            System.out.println(data2.get(i));
        }
//        hist(new ArrayList<>());
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
        for(int n=0;n<60;n++)
        try {
            Scanner sc=new Scanner(new FileInputStream(new File(String.format("F:\\workspace_code\\java\\Test\\resource\\burrData\\%d.txt",n))));
            int start_position=(int)(Math.random()*20000);
            int i=0;
            for(;sc.hasNext()&&i<start_position;i++){
                sc.next();
            }
            int index=0;
            while(sc.hasNext()&&index<20000){
                eps1.pushData(sc.nextDouble());
                index++;
            }
            for(;sc.hasNext()&&i+50000<start_position;i++){
                sc.next();
            }
            index=0;
            while(sc.hasNext()&&index<20000){
                eps2.pushData(sc.nextDouble());
            }
            System.out.println(eps1.getPoints());
            System.out.println(eps2.getPoints());
            QuartileMerge qm=new QuartileMerge(quartileList);
            qm.add(eps1.getPoints(),2000);
            qm.add(eps2.getPoints(),2000);
            qm.setSampleValue(1000);
            System.out.println(qm.getPoints());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}