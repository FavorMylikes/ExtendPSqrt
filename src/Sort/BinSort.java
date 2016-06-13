package Sort;

import java.util.Collections;
import java.util.LinkedList;
import java.util.regex.Matcher;

import static java.lang.System.out;

/**
 * Created by l7861 on 2016/6/12.
 */
public class BinSort {
    public int size=20;
    public LinkedList<Integer> data=new LinkedList<>();
    public static void main(String[] args){
        BinSort bs=new BinSort();
        for(int i=0;i<bs.size;i++){
            bs.data.add((int)(Math.random()*10));
        }
        Collections.sort(bs.data);
        out.println(bs);
        bs.add(10);
        out.println(bs);
        bs.add(0);
        out.println(bs);
        bs.add(5);
        out.println(bs);
    }
    public void init(){
        Collections.sort(data);
    }
    public void add(int num){
        int left=0;
        int right=size-1;
        while(left!=right){
            int mid=(right-left+1)/2;
            if(data.get(left+mid)<=num){
                left=left+mid;
            }else if(data.get(right)>num){
                right=right-mid;
            }
        }
        if(num>data.get(left)){
            data.add(left+1,num);
        }else{
            data.add(left,num);
        }
    }

    @Override
    public String toString() {
        String ret="";
        for(double d:data){
            ret+=d+"\t";
        }
        return ret;
    }
}