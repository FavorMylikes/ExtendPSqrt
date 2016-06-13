package Sort;

import java.util.Collections;
import java.util.Comparator;

/**
 * Created by l7861 on 2016/6/13.
 */
public class BinSortFromAbstract extends Sort{


    @Override
    public void init() {
        //noinspection Since15
        data.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return smallUp?o1-o2:o2-o1;
            }
        });
    }

    @Override
    protected void add(int num) {
        if(data.size()==0){size++;data.add((num));return;}
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
        size++;
    }

    @Override
    protected boolean needAdjust(int root, int child) {
        return smallUp?root>child:root<child;
    }

    @Override
    protected void doAdjust(int num) {
        data.remove(0);
        size--;
        add(num);
    }
    @Override
    public String toString() {
        String ret="";
        for(int d:data){
            ret+=d+"\t";
        }
        return ret;
    }
}
