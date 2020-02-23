import jxl.Sheet;
import jxl.Workbook;

import java.io.File;

public class Road_embedding {
    private static final int INF = Integer.MAX_VALUE;   // 最大值
    private char[] mVexs;       // 顶点集合
    public static void main(String[] args) {
        try{
            File file=new File("allgraph.xls");
            // 获得工作簿对象
            Workbook workbook = Workbook.getWorkbook(file);
            // 获得所有工作表
            Sheet[] sheets = workbook.getSheets();
            System.out.println("dawd");
            // 遍历工作表
            // 获得行数
            int rows = sheets[0].getRows();
            // 获得列数
            int cols = sheets[0].getColumns();
            // 读取数据
            System.out.println("行数："+rows+" 列数："+cols);
            for (int row = 0; row < rows; row++)
            {
                for (int col = 0; col < cols; col++)
                {
                    System.out.printf("%6s",sheets[0].getCell(col,row).getContents());
                    System.out.print("    ");
                }
                System.out.println();
            }
            if (sheets == null)
            {
                System.out.println("dawd");
                for (Sheet sheet : sheets)
                {
                    // 获得行数
                    rows = sheet.getRows();
                    // 获得列数
                    cols = sheet.getColumns();
                    // 读取数据
                    System.out.println("行数："+rows+" 列数："+cols);
                    for (int row = 0; row < rows; row++)
                    {
                        for (int col = 0; col < cols; col++)
                        {
                            System.out.printf("%6s",sheet.getCell(col,row).getContents());
                            System.out.print("    ");
                        }
                        System.out.println();
                    }
                }
            }
            workbook.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        char[] vexs = {'A', 'B', 'C', 'D', 'E', 'F', 'G','H','i','j'};
        int matrix[][] = {
                {0, 120, INF, INF, INF, 160, 140, INF, INF, INF},
                {120, 0, 100, INF, INF, 70, INF, INF, INF, INF},
                {INF, 100, 0, 30, 50, 60, INF, INF, INF, INF},
                {INF, INF, 30, 0, 40, INF, INF, INF, INF, INF},
                {INF, INF, 50, 40, 0, 20, 80, INF, INF, INF},
                {160, 70, 60, INF, 20, 0, 90, INF, INF, INF},
                {140, INF, INF, INF, 80, 90, 0, 10, INF, INF},
                {INF, INF, INF, INF, INF, INF, 10, 0, 10, INF},
                {INF, INF, INF, INF, INF, INF, INF, 10, 0, 30},
                {INF, INF, INF, INF, INF, INF, INF, INF, 30, 0},};
        MatrixUDG pG= new MatrixUDG(vexs, matrix);
        int[][] path = new int[54][54];
        int[][] floy = new int[54][54];
        // floyd算法获取各个顶点之间的最短距离
        pG.floyd(path, floy);
//        pG.floyd_path(path,floy);
        pG.road_embedding(floy);
    }
}
