import java.util.ArrayList;
import java.util.Collections;

import static java.lang.System.out;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Created by l7861 on 2016/6/6.
 */
public class QuartileMergeTest {

    public static double[][] getError(double[] quartileList,double[] markers, ArrayList<Double> actualDataList,String id){
        assertEquals(quartileList.length,markers.length);
        double[][] result=new double[quartileList.length][2];
        int actualDataLength=actualDataList.size();
        Collections.sort(actualDataList);
        for(int i=0;i<quartileList.length;i++){
            assertEquals(quartileList[i]>=0&&quartileList[i]<=1,true,id+":"+quartileList[i]+"[quartile<0||quartile>1]");
            assertEquals(markers[i]>=0,true,id+":"+quartileList[i]+"[markers<0]");
            int m=0;
            while(actualDataList.get(m)<=markers[i]&&++m<actualDataLength){}
            assertTrue(actualDataList.get(m-1)>0,id+":"+quartileList[i]+"[there is a data less than 0]"+actualDataList.get(m-1));
            double err_x=(m*1.0/actualDataLength-quartileList[i])/2;
            double err_y=(markers[i]/actualDataList.get(m-1)-1);
            result[i][0]=err_x;
            result[i][1]=err_y;
            assertTrue(Math.abs(err_x)<=0.1,id+":"+quartileList[i]+"[err_x>10%]"+err_x);
            assertTrue(Math.abs(err_y)<=0.1,id+":"+quartileList[i]+"[err_y>10%]"+err_y);
        }
        for(int i=0;i<result.length;i++){
            out.printf("q:%6f,markers:%6f,err_x:%6f,err_y:%6f\n",quartileList[i],markers[i],result[i][0],result[i][1]);
        }
        return result;
    }
}
