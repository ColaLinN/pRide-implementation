import jxl.Sheet;
import jxl.Workbook;
import pRIdeHalfImple.MatrixUDG;
import pRIdeHalfImple.garbled_circuit;
import pRideREALWORLD.*;

import java.io.File;
import java.math.BigInteger;
import java.util.Arrays;

import static pRIdeHalfImple.Road_embedding_Origin.*;

public class pRideREALWORLDTEST {
    public static void main(String[] args) {
//      ################################ 下面开始初始化地图 #################################################

        initial init_o=new initial();
        int[][] vexs_coordinate=init_o.getVexs_coordinate();//经纬度数组vexs_coordinate,new int[54][2];
        int[] vexs=init_o.getVexs();//new int[54];
        int[][] matrix=init_o.getMatrix();//领接矩阵matrix
//        init_o.output_xls();

        MatrixUDG pG= new MatrixUDG(vexs, matrix);//MatrixUDG_一个图类,里面封装了图的最短路径以及路网嵌入的计算等
        int[][] path = new int[pG.mVexs.length][pG.mVexs.length];//这是最短路径上所经点的记录
        int[][] floyd = new int[pG.mVexs.length][pG.mVexs.length];//这是floyd所计算出来的点间距离矩阵
        pG.floyd(path, floyd);//调用floyd算法,获取各个顶点之间的最短距离
//        pG.floyd_path(path);//可视化打印floyd计算的最短路径
        pG.road_embedding(floyd);//在floyd的基础上得到路网嵌入图

//      ################################ 下面开始初始化四方 #################################################
        //计算第三方
        CP cp=new CP();
        //服务商
        ORH orh=new ORH(cp.getPublicKey());
        //乘客  //PointA是邻接的第一个点,PointB是邻接的第二个点,PointNow2A_len是乘客到第一个点的距离
        Rider rider=new Rider(33,33,1,pG,26,27,1000,cp.getPublicKey());
        //司机
        Driver driver1=new Driver(44,44,pG,12,14,1300,cp.getPublicKey());
        Driver driver2=new Driver(55,55,pG,20,21,1301,cp.getPublicKey());

        //路网比较，这是直接比较。没有混淆电路、同态加密、没有全流程的
        Road_map_compare(pG,rider.getRE_Graph_int(),driver1.getRE_Graph_int(),driver2.getRE_Graph_int());

//      ################################ 以下为加入了混淆电路的比较全流程！！ #####################################

        //首先，ORH计算pb
        BigInteger[] pb1=orh.calc_Pd(cp,rider.getRE_Graph_Cipher(),driver1.getRE_Graph_Cipher(),driver1.getMu_Cipher());
        BigInteger[] pb2=orh.calc_Pd(cp,rider.getRE_Graph_Cipher(),driver2.getRE_Graph_Cipher(),driver2.getMu_Cipher());
        //ORH将pb发给cp解密
        cp.get_garbled_value(pb1,driver1.getMu_Cipher(),pb2,driver2.getMu_Cipher());
        //ORH从cp处获得混淆值到混淆pb判断两个司机哪个离乘客近
        orh.gc_compare_2(cp,pb1,driver1.getMu_Cipher(),pb2,driver2.getMu_Cipher());
    }
}
