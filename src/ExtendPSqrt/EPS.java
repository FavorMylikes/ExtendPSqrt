package ExtendPSqrt;

import javafx.util.Pair;

import java.util.ArrayList;

/**
 * Created by l7861 on 2016/6/1.
 */
public class EPS {
    public QuantileP2 q;

    EPS(double[] quantiles){
        q=new QuantileP2(quantiles);
        this.q=q;
    }

    EPS(double[] quantiles, double[] p2_q, int count){
        q=new QuantileP2(quantiles, p2_q, count);
        this.q=q;
    }

    EPS(QuantileP2 q){
        this.q=q;
}

    public ArrayList<Pair> getPoints() {
        ArrayList<Pair> result = new ArrayList<Pair>();
        double[] markers = q.markers();
        double[] quattileList = q.getQuartileList();
        int count = q.getCount();
        for (int i = 0; i < markers.length; i++) {
            result.add(new Pair(quattileList[i], markers[i]));
        }
        return result;
    }

    public void add(double data) {
        q.add(data);
    }
}
