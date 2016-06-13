package Sort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.regex.Matcher;

import static java.lang.System.out;

/**
 * Created by l7861 on 2016/6/12.
 */
public class SortExamle {
    static BinSort bs=new BinSort();
    static HeapSort hs=new HeapSort();
    static{
        int size=100;
        hs.size=size;
        bs.size=size;
        for(int i=0;i<size;i++){
            hs.data.add((int)(Math.random()*100));
            bs.data.add((int)(Math.random()*100));
        }
        bs.init();
        hs.init();
    }

    public static void main(String[] args){
//        double begin=0;
//        double end=0;
//
//        begin=System.currentTimeMillis();
//        binSortTime();
//        end=System.currentTimeMillis();
//        out.println(end-begin);
//
//        begin=System.currentTimeMillis();
//        headSortTime();
//        end=System.currentTimeMillis();
//        out.println(end-begin);
        BinSortFromAbstract bsa=new BinSortFromAbstract();
        HeapSortFromAbstract hsa=new HeapSortFromAbstract();
        for(int i=0;i<5;i++){
            int data=(int)(Math.random()*10);
            bsa.push(data);
            hsa.push(data);
        }
        out.println(bsa);
        out.println(hsa);

    }
    static void binSortTime(){
        for(int i=0;i<1000000;i++){
            bs.add((int)(Math.random()*100));
        }
    }

    static void headSortTime(){
        for(int i=0;i<1000000;i++){
            hs.add((int)(Math.random()*100));
        }
    }
}
