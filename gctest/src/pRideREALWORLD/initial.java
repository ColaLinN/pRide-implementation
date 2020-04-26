package pRideREALWORLD;

import jxl.Sheet;
import jxl.Workbook;

import java.io.File;

public class initial {
    private final int INF = Integer.MAX_VALUE;   // 最大值
    private int[][] vexs_coordinate=new int[54][2];  //经纬度数组vexs_coordinate
    private int[] vexs=new int[54];        //领接矩阵matrix
    private int[][] matrix=new int[54][54];
    public  void init_xls(){
        for (int i = 0; i < 54; i++)  this.vexs[i]=i;//初始化vexs
        try{
            File file=new File("allgraph.xls");
            Workbook workbook = Workbook.getWorkbook(file);// 获得工作簿对象
            Sheet[] sheets = workbook.getSheets();// 获得所有工作表
            //遍历每一个点的坐标经纬度
            for(int row=1;row<55;row++){
                this.vexs_coordinate[row-1][0]=Integer.parseInt(sheets[0].getCell(0,row).getContents());//维度
                this.vexs_coordinate[row-1][1]=Integer.parseInt(sheets[0].getCell(1,row).getContents());//经度
            }
            for (int row = 1; row < 55; row++)
            {
                for (int col = 3; col < row+3; col++)
                {
                    String A=sheets[0].getCell(col,row).getContents();
                    String N="N";
                    if(A.equals(N)==true){
                        this.matrix[row-1][col-3]=INF;
                        this.matrix[col-3][row-1]=INF;
                    }else if(A.equals("0")){
                        this.matrix[row-1][col-3]=0;
                        this.matrix[col-3][row-1]=0;
                    }else {
                        this.matrix[row-1][col-3]=Integer.parseInt(A);
                        this.matrix[col-3][row-1]=Integer.parseInt(A);
                    }
                }
            }
            workbook.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void output_xls(){
//        输出excel所读数据
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                if(matrix[i][j]!=2147483647)  System.out.print(matrix[i][j]+"  ");
            }
            System.out.println();
        }
    }

    public int getINF() {
        return INF;
    }

    public int[][] getVexs_coordinate() {
        return vexs_coordinate;
    }

    public int[] getVexs() {
        return vexs;
    }

    public int[][] getMatrix() {
        return matrix;
    }
}
