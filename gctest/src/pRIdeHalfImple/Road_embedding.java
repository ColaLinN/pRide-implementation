package pRIdeHalfImple;
/**
 * Java: 集成混淆电路、路网嵌入图的乘客、司机距离比较
 * @author lfl
 * @date 2020/2/18
 *
 */

import jxl.Sheet;
import jxl.Workbook;

import java.io.File;
import java.util.Arrays;


public class Road_embedding {
    private char[] mVexs;       // 顶点集合
    public Road_embedding(){}
    public static void main(String[] args) {
        final int INF = Integer.MAX_VALUE;   // 最大值
        //经纬度数组vexs_coordinate
        int[][] vexs_coordinate=new int[54][2];
        int[] vexs=new int[54];
        //领接矩阵matrix
        int[][] matrix=new int[54][54];
        for (int i = 0; i < 54; i++)  vexs[i]=i;//初始化vexs
        try{
            File file=new File("allgraph.xls");
            Workbook workbook = Workbook.getWorkbook(file);// 获得工作簿对象
            Sheet[] sheets = workbook.getSheets();// 获得所有工作表
            int rows = sheets[0].getRows();// 获得行数
            int cols = sheets[0].getColumns();// 获得列数
//            System.out.println("行数："+rows+" 列数："+cols);// 读取数据

            //遍历每一个点的坐标
            for(int row=1;row<55;row++){
                //维度
                vexs_coordinate[row-1][0]=Integer.parseInt(sheets[0].getCell(0,row).getContents());
                //经度
                vexs_coordinate[row-1][1]=Integer.parseInt(sheets[0].getCell(1,row).getContents());
//                System.out.println(vexs_coordinate[row-1][0]+"   "+vexs_coordinate[row-1][1]);//get it!
            }
            //遍历sheet构造领接矩阵matrix
            for (int row = 1; row < 55; row++)
            {
                for (int col = 3; col < row+3; col++)
                {
                    String A=sheets[0].getCell(col,row).getContents();
//                    System.out.printf("%6s",A);
                    String N="N";
                    if(A.equals(N)==true){
                        matrix[row-1][col-3]=INF;
                        matrix[col-3][row-1]=INF;
//                        System.out.print("yes");
                    }else if(A.equals("0")){
                        matrix[row-1][col-3]=0;
                        matrix[col-3][row-1]=0;
//                        System.out.print("zero");
                    }else {
                        matrix[row-1][col-3]=Integer.parseInt(A);
                        matrix[col-3][row-1]=Integer.parseInt(A);
//                        System.out.print("ohhh  ");
                    }
//                    System.out.print("    ");
                }
//                System.out.println();
            }
            // 遍历工作表
//            if (sheets != null)
//            {
//                System.out.println("dawd");
//                for (Sheet sheet : sheets)
//                {
//                    // 获得行数
//                    rows = sheet.getRows();
//                    // 获得列数
//                    cols = sheet.getColumns();
//                    // 读取数据
//                    System.out.println("行数："+rows+" 列数："+cols);
//                    for (int row = 0; row < rows; row++)
//                    {
//                        for (int col = 0; col < cols; col++)
//                        {
//                            System.out.printf("%6s",sheet.getCell(col,row).getContents());
//                            System.out.print("    ");
//                        }
//                        System.out.println();
//                    }
//                }
//            }
            workbook.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        //以上为都vexcel
        //输出excel所读数据
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
//                if(matrix[i][j]!=2147483647)
//                System.out.print(matrix[i][j]+"  ");
            }
//            System.out.println();
        }
        MatrixUDG pG= new MatrixUDG(vexs, matrix);//MatrixUDG_一个图类,里面封装了图的最短路径以及路网嵌入的计算等
        int[][] path = new int[pG.mVexs.length][pG.mVexs.length];//这是最短路径上所经点的记录
        int[][] floyd = new int[pG.mVexs.length][pG.mVexs.length];//这是floyd所计算出来的点间距离矩阵
        // floyd算法获取各个顶点之间的最短距离
        pG.floyd(path, floyd);//调用floyd算法，这时，floyd点距离矩阵已经被计算并且返回了
//        pG.floyd_path(path);//可视化打印floyd计算的最短路径
        //路网嵌入计算！计算完之后，Omega将存于pG对象中，之后调用calc_point即可计算某点的路网嵌入
        int[][] Omega=pG.road_embedding(floyd);
        int[] Ride=pG.calc_point(26,27,1000);//计算乘客的路网嵌入
        int[] driver1=pG.calc_point(12,14,1300);//next_a是邻接的第一个点,next_b是邻接的第二个点,point2a_len是点到a的距离
        int[] driver2=pG.calc_point(20,21,1301);

        //这是路网嵌入对象，里面封装了各种路网嵌入的方法
        Road_embedding RE=new Road_embedding();
        //路网距离比较，这是直接比较
        Road_map_compare(RE,pG,Ride,driver1,driver2);

        //以下为加入了混淆电路的比较

        int[] mu1=new int[9];//μ1
        int[] mu2=new int[9];//μ2
        Arrays.fill(mu1, 3000);//初始化，一个比较大的u值
        Arrays.fill(mu2, 3000);
//        System.out.println("RRRRRRRRRRRRRRRRRRRR");
//        for (int i = 0; i < Ride.length; i++) {
//            System.out.println(Ride[i]);
//        }
//        System.out.println("dddddddddddddddddddddddddd");
//        for (int i = 0; i < Ride.length; i++) {
//            System.out.println(driver2[i]);
//        }
        int[] Driver1=RE.Ride_Driver_mu(Ride,driver1,mu1);//
        int[] Driver2=RE.Ride_Driver_mu(Ride,driver2,mu2);
        RE.two_point_gc_test(RE,Driver1,Driver2,mu1,mu2);
        garbled_circuit gc=new garbled_circuit();
        if(gc.gc_compare_2(2,5,0,0)) System.out.println("yes");
    }
    //直接比较距离
    public static void Road_map_compare(Road_embedding RE, MatrixUDG pG, int[] Ride, int[] driver1, int[] driver2){
        int Min_len1=RE.two_point_len(Ride,driver1);//乘客与司机1的距离
        int Min_len2=RE.two_point_len(Ride,driver2);//乘客与司机2的距离
        if(Min_len1<Min_len2){
            System.out.println("路网嵌入算得：乘客离司机1最近");
        }else{
            System.out.println("路网嵌入算得：乘客离司机2最近");
        }
    }
    //计算两点之间最短距离
    public  int two_point_len(int[] A,int[] B){
        int[] pointA=A;
        int[] pointB=B;
        int Min_len=0;
        Min_len=Math.abs(pointA[0]-pointB[0]);
        for (int i = 1; i < pointA.length; i++) {
            Min_len=Math.max(Math.abs(pointA[i]-pointB[i]),Min_len);
        }
        System.out.print("ride与driver之间距离是：");
        System.out.println(Min_len);
        return Min_len;
    }
    //这里跳过rider和driver的初始化，直接计算p【d】,而且没有同态加密，已经假设CP有了同态加密
    //2020/4/26  加一个同态加密
    public  int[] Ride_Driver_mu(int[] ride,int[] driver,int[] mu){
        int[] result=new int[9];//如果 加点 要改大小
        for (int i = 0; i < ride.length; i++) {
//            System.out.print("   r"+ride[i]);
//            System.out.println("   d"+driver[i]);
//            System.out.println("hi"+(ride[i]-driver[i]));
            result[i]=ride[i]-driver[i]+mu[i];
        }
        return result;
    }
    //模拟cp与服务器之前的操作，暂时先放在一起
    public void two_point_gc_test(Road_embedding RE, int[] driver1, int[] driver2, int[] mu1, int[] mu2){
        //以下为绝对值的判断
        for (int i = 0; i < driver1.length; i++) {
            //比较大小并且交换
            int[] D_m=RE.Driver_mu_compare_swap(RE,driver1[i],mu1[i]);
            driver1[i]=D_m[0];
            mu1[i]=D_m[1];
//            System.out.println(Driver1[i]-mu1[i]);
        }
        for (int i = 0; i < driver2.length; i++) {
            int[] D_m=RE.Driver_mu_compare_swap(RE,driver2[i],mu2[i]);
            driver2[i]=D_m[0];
            mu2[i]=D_m[1];
//            System.out.println(Driver2[i]-mu2[i]);
        }
        garbled_circuit gc=new garbled_circuit();
        int driver1_min=driver1[0];
        int mu1_min=mu1[0];
//        if(!RE.Driver_mu_compare(driver1_min,mu1_min)){
//            driver1_min=-driver1_min;
//            mu1_min=-mu1_min;
//        }
        //从头开始比较
        if(driver1_min<mu1_min){
            driver1_min=-driver1_min;
            mu1_min=-mu1_min;
        }
        for (int i = 1; i < driver1.length; i++) {
            if(driver1[i]<mu1[i]){
                driver1[i]=-driver1[i];
                mu1[i]=-mu1[i];
//                System.out.println("hello");
            }
//            System.out.println((driver1_min-mu1_min)+"    "+(driver1[i]-mu1[i]));
            //使用混淆电路比较大小
            boolean result=RE.Driver_mu_compare((driver1_min-mu1_min),(driver1[i]-mu1[i]));
//            boolean result=gc.gc_compare_2(driver1_min,mu1_min,driver1[i],mu1[i]);
            //result为true代表：driver2_min比driver2[i]小
            //最小的就是结果了
            if(!result){
                driver1_min=driver1[i];
                mu1_min=mu1[i];
            }
        }
//        System.out.println("hhhhhh");
        int driver2_min=driver2[0];
        int mu2_min=mu1[0];
        //比最小值，若driver小于mu，取绝对值
        if(driver2_min<mu2_min){
            driver2_min=-driver2_min;
            mu2_min=-mu2_min;
        }
        for (int i = 1; i < driver2.length; i++) {
            if(driver2[i]<mu2[i]){
                driver2[i]=-driver2[i];
                mu2[i]=-mu2[i];
            }
//            System.out.println((driver2_min-mu2_min)+"    "+(driver2[i]-mu2[i]));
            //比较两个值的大小
            boolean result=RE.Driver_mu_compare((driver2_min-mu2_min),(driver2[i]-mu2[i]));
            //result为true代表：driver2_min比driver2[i]小
            if(!result){
                driver2_min=driver2[i];
                mu2_min=mu2[i];
            }
        }
        if(driver1_min-mu1_min<driver2_min-mu2_min){
            System.out.println("混淆电路计算得：乘客离司机1最近");
        }else{
            System.out.println("混淆电路计算得：乘客离司机2最近");
        }
    }
    //用于比较计算Driver_mu的绝对值
    public boolean Driver_mu_compare(int Driver,int mu){
        //使用混淆电路比较大小
        garbled_circuit gc=new garbled_circuit();
//        System.out.println(Driver+"    "+mu);
        if(gc.gc_compare_2(Driver,mu,0,0)){
//            System.out.println(Driver);
            return true;
        }
        else{
//            System.out.println(mu);
            return false;
        }
    }
    //比较大小并且交换
    public int[] Driver_mu_compare_swap(Road_embedding RE, int Driver, int mu){
        int[] result=new int[2];
        if(!RE.Driver_mu_compare(Driver,mu)){
            Driver=-Driver;
            mu=-mu;
        }
        result[0]=Driver;
        result[1]=mu;
        return result;
    }
}
