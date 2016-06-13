package Sort;

import java.util.LinkedList;

/**
 * Created by l7861 on 2016/6/9.
 */
public class TopNCache {
    private LinkedList<Integer> data=new LinkedList<>();
    private double rate;
    private boolean ascending;//如果为真（升序），则判断插入的数据是不是比最大的还大，如果是降续，则判断是不是比最小的还小
    private int count;
    TopNCache(double rate,boolean ascending){
        this.rate=rate;
        this.ascending=ascending;
        this.count=0;
    }

    public Integer add(int d){
        this.count++;
        if(!(count*rate-data.size()>=1)){
            if(data.size()!=0&&(ascending&&d>data.getLast()||!ascending&&d<data.getFirst())){
                return null;
            }
            if(ascending){
                return data.pollFirst();
            }else{
                return data.pollLast();
            }
        }
        adjust(d);
        return null;
    }

    private void adjust(int d){
        if(data.size()==0){
            data.add(d);
            return ;
        }
        int left=0;
        int right=data.size()-1;
        while(right>left){
            while(d>=data.get(left)&&right>left){
                left++;
            }
            while(d<=data.get(right)&&right>left){
                right--;
            }
        }
        if(d>data.get(right)){
            data.add(d);
        }else{
            data.add(left,d);
        }
    }

    public LinkedList<Integer> getData() {
        return data;
    }
}