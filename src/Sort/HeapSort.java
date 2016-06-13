package Sort;

import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.System.out;

/**
 * Created by l7861 on 2016/6/12.
 */

public class HeapSort {
    public int size=0;
    public ArrayList<Integer> data=new ArrayList<>();
    private boolean smallUp=true;
    static public void main(String[] args){
        HeapSort hs=new HeapSort();
        for(int i=0;i<hs.size;i++){
            hs.data.add((int)(Math.random()*10));
        }
        hs.init();
        out.println(hs);
        out.println("=================");
        hs.size=6;
        hs.add(0);
        out.println(hs);
    }

    public void init(){
        for(int i=size/2;i>0;i--){
            adjust(i);
        }
    }
    public void adjust(int index){
        if(index>size/2||index<1){return ;}
        int l_child=index*2-1;
        int r_child=index*2+1-1;
        int root=index-1;
        if(l_child<size&&needAdjust(data.get(root),data.get(l_child))){
            change(root,l_child);
        }
        if(r_child<size&&needAdjust(data.get(root),data.get(r_child))){
            change(root,r_child);
        }
        adjust(index*2);
        adjust(index*2+1);
    }

    public void readjust(int index){
        if(index>size||index<=1){return ;}
        int root=index/2-1;
        int child=index-1;
        if(needAdjust(data.get(root),data.get(child))){
            change(root,child);
        }
        readjust(index/2);
    }

    @Override
    public String toString() {
        String ret="";
        for(double d:data){
            ret+=d+"\t";
        }
        return ret;
    }

    public int pop(){
        return 1;
    }

    private void change(int a,int b){
        if(a>=size||b>=size||a<0||b<0)
            return;
        else{
            int t=data.get(a);
            data.set(a,data.get(b));
            data.set(b,t);
        }
    }
    /*
    添加之后size+1
     */
    public void add(int num){
        data.add(num);
        size++;
        readjust(data.size());
    }

    /*
    和堆顶比较，如果不满足条件则不更新且size不变
     */
    public void update(int num){
        if(size==0) return;
        if(needAdjust(num,data.get(0))){
            data.set(0,num);
            adjust(1);
        }
    }

    private boolean needAdjust(int root,int child){
        return smallUp?root>child:root<child;
    }
}
