package Sort;

import java.util.ArrayList;

import static java.lang.System.out;

/**
 * Created by l7861 on 2016/6/13.
 */
public class HeapSortFromAbstract extends Sort{

    static public void main(String[] args){
        HeapSortFromAbstract hs=new HeapSortFromAbstract();
        for(int i=0;i<6;i++){
            hs.push((int)(Math.random()*10));
        }
//        hs.init();
        out.println(hs);
        out.println("=================");
        hs.push(0);
        out.println(hs);
    }
    @Override
    /*
    插入式构建堆不必调用此方法，如果调用setData则需要调用此方法构建堆
     */
    public void init() {
        for(int i=size/2;i>0;i--){
            adjust(i);
        }
    }

    @Override
    protected void add(int num) {
        size++;
        data.add(num);
        readjust(data.size());
    }

    @Override
    protected boolean needAdjust(int root, int child) {
        return smallUp?root>child:root<child;
    }

    @Override
    protected void doAdjust(int num) {
        data.set(0,num);
        adjust(1);
    }

    @Override
    public String toString() {
        ArrayList<Integer> outData= (ArrayList<Integer>) data.clone();
        HeapSortFromAbstract hsa=new HeapSortFromAbstract();
        hsa.setData(outData);
        String result="";
        while(hsa.size!=0){
            result+=hsa.pop()+"\t";
        }
        return result;
    }

    public void adjust(int index) {
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

    public void readjust(int index) {
        if(index>size||index<=1){return ;}

        int root=index/2-1;
        int child=index-1;
        if(needAdjust(data.get(root),data.get(child))){
            change(root,child);
        }
        readjust(index/2);
    }

    private int pop(){
        if(size==0)
            return -1;
        int result=data.get(0);
        change(0,size-1);
        data.remove(size-1);
        size--;
        adjust(1);
        return result;
    }
}
