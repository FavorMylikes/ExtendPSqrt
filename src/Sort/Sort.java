package Sort;

import java.util.ArrayList;

import static java.lang.System.out;

/**
 * Created by l7861 on 2016/6/13.
 */
public abstract class Sort {
    protected int size=0;
    protected ArrayList<Integer> data=new ArrayList<>();
    protected boolean smallUp=true;

    public abstract void init();
    protected void change(int a,int b){
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
    protected abstract void add(int num);

    public void push(int num){
        add(num);
    }
    /*
    和堆顶比较，如果不满足条件则不更新且size不变
     */
    public void update(int num){
        if(size==0) return;
        if(needAdjust(num,data.get(0))){
            doAdjust(num);
        }
    }

    protected abstract boolean needAdjust(int root,int child);

    protected abstract void doAdjust(int num);

    public int getSize() {
        return size;
    }

    public ArrayList<Integer> getData() {
        return data;
    }

    public void setData(ArrayList<Integer> data) {
        this.size=data.size();
        this.data = data;
    }
}
