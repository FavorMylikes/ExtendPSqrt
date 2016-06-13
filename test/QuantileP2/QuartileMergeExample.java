package QuantileP2;

import ExtendPSqrt.QuantileP2;
import ExtendPSqrt.QuartileMerge;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by l7861 on 2016/6/6.
 */
public class QuartileMergeExample {

    static double[] quartileList;
    static ArrayList<Double> actualDataListAll=new ArrayList<Double>();
    static String testDir="F:\\workspace_code\\java\\Extened P2 online\\src\\test\\resources";
    static{

        int count=6;
        quartileList = new double[count-1];
//        for(int i=1;i<count;i++) {sim
//            quartileList[i-1] = i * 1.0 / count;
//        }
        int i=0;
        quartileList[i++]=0.5;
        quartileList[i++]=0.7;
        quartileList[i++]=0.8;
        quartileList[i++]=0.9;
        quartileList[i++]=0.95;

    }
    QuartileMergeExample quartileMergeExample = null;

    public static void main() throws FileNotFoundException {
        mergeRealData("F:\\workspace_code\\java\\Extened P2 online\\src\\test\\resources");
    }
    @Test(dataProvider = "local")
    public static QuantileP2 readRealData(String fileName) throws FileNotFoundException {
        QuantileP2 qp=new QuantileP2(quartileList);
        File file=new File(fileName);
        Scanner sc = new Scanner(file);
        ArrayList<Double> actualDataList=new ArrayList<Double>();
        while(sc.hasNext()){
            double data=sc.nextDouble();
            qp.add(data);
            actualDataList.add(data);
        }

        QuartileMergeTest.getError(qp.getQuartileList(),qp.markers(),actualDataList,file.getParent());//测试函数
        actualDataListAll.addAll(actualDataList);
        return qp;
    }
    public static void mergeRealData(String dirName) throws FileNotFoundException {
        QuartileMerge qm=new QuartileMerge(quartileList);
        File dir=new File(dirName);
        String files[]=dir.list();
        for(String fileName:files){
            QuantileP2 qp=readRealData(dirName+"\\"+fileName+"\\le_data.txt");
            qm.merge(qp);
            //如果是从数据库读取数据
            //getQuartileList()数据库中每一列分位值代表的横坐标[0,0.25,0.5,0.6,0.7,0.75....1.0]
            //markers()数据库中存储的值[1280，.....11223]
            //getCount()数据中每条存储的记录的count数
            //qm.merge(qp.getQuartileList(),qp.markers(),qp.getCount());
        }
        //
//        double[] quantileList=qm.getQuartileList();
//        double[] markers=qm.markers();
//        for(int i=0;i<quantileList.length;i++){
//            out.printf("q:%6f,marker:%6f\n",quantileList[i],markers[i]);
//        }
//        out.println();
//        List<Double> simPoints=qm.getSimPoint();
//        for(double point:simPoints){
//            out.println(point);
//        }
        QuartileMergeTest.getError(qm.getQuartileList(),qm.markers(),actualDataListAll,"dir");//测试代码
    }

    @DataProvider(name="local")
    public Object[][] createData() {
        ArrayList<String> result=new ArrayList<String>();
        QuartileMerge qm=new QuartileMerge(quartileList);
        File dir=new File(testDir);
        String files[]=dir.list();
        for(String fileName:files) {
            result.add(testDir + "\\" + fileName + "\\le_data.txt");
        }
        Object[][] data=new Object[result.size()][1];
        for(int i=0;i<result.size();i++){
            data[i]=new Object[]{result.get(i)};
        }
        return data;
    }
}
