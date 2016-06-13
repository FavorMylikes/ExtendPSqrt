package Sort;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Scanner;

import static java.lang.System.out;

/**
 * Created by l7861 on 2016/6/10.
 */
class TopNCacheTest {
    static public void main(String[] args) throws FileNotFoundException {
        readRealData("F:\\workspace_code\\java\\CodeLab\\algorithm\\resources\\1MFbIUQGGr0\\le_data.txt");
    }
    static public void readRealData(String fileName) throws FileNotFoundException {
        double rate=0.05;
        Scanner sc=new Scanner(new File(fileName));
        ArrayList<Sort> tc=new ArrayList<>();
        tc.add(new BinSortFromAbstract());
        tc.add(new HeapSortFromAbstract());
        ArrayList<Integer> realData=new ArrayList<>();
        int count=0;
        while(sc.hasNext()){
            count++;
            int data=sc.nextInt();
            for(Sort ts:tc){
                if(count*rate-ts.size>=0)
                    ts.add(data);
                else{
                    ts.update(data);
                }
            }
            realData.add(data);
        }
        for(Sort ts:tc){
            out.println(ts);
        }
        //noinspection Since15
        realData.sort(null);
        for(int i=(int)(realData.size()*(1-rate));i<realData.size();i++){
            out.printf("%d\t",realData.get(i));
        }
        out.println();
    }
}
