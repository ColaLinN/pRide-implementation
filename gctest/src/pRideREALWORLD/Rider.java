/**
 * Java: Rider对象，封装了其各种操作
 * @author lfl
 * @date 2020/4/26
 *
 */
package pRideREALWORLD;

import Homo.PublicKey;
import pRIdeHalfImple.MatrixUDG;

import java.math.BigInteger;
import java.util.Arrays;

public class Rider {
    private int x;
    private int y;
    private BigInteger x_Chiper;
    private BigInteger y_Chiper;
    private int zone;
    private MatrixUDG pRide_Graph;
    private int PointA;
    private int PointB;
    private int PointNow2A_len;
    private PublicKey publicKey;
    private BigInteger[] RE_Graph;    //路网嵌入图
    private BigInteger[] RE_Graph_Cipher;
    private BigInteger[] nx;
    private BigInteger[] nx_Cipher;
    private BigInteger[] ny;
    private BigInteger[] nywni_Cipher;
    public Rider(int x,int y,int zone,MatrixUDG pRide_Graph, int PointA, int PointB, int PointNow2A_len,PublicKey publicKey){
        this.x=x;
        this.y=y;
        this.x_Chiper=this.HOMOencrypt(x);
        this.y_Chiper=this.HOMOencrypt(y);

        this.zone=zone;
        this.pRide_Graph=pRide_Graph;

        this.PointA=PointA;
        this.PointB=PointB;
        this.PointNow2A_len=PointNow2A_len;
        this.publicKey=publicKey;

        this.RE_Graph=new BigInteger[pRide_Graph.mVexs.length];
        int[] RE_Graph_inttype=pRide_Graph.calc_point(PointA,PointB,PointNow2A_len);//计算自己的路网嵌入变量S
        for (int i = 0; i < pRide_Graph.mVexs.length; i++) {
            this.RE_Graph[i]= BigInteger.valueOf(RE_Graph_inttype[i]);
        }

    }

    //同台加密  加密  int型输入
    public BigInteger HOMOencrypt(int num){
        return this.publicKey.encrypt(BigInteger.valueOf(num));
    }
    public BigInteger HOMOencrypt(BigInteger num){
        return this.publicKey.encrypt(num);
    }
    //同台加密 密文相加
    public BigInteger HOMOAddition(BigInteger A_Chiper,BigInteger B_Chiper){
        return A_Chiper.multiply(B_Chiper).mod(this.publicKey.getnSquared());
    }

    public void setNx(BigInteger[] nx) {
        this.nx = nx;
    }

    public void setNx_Cipher(BigInteger[] nx_Cipher) {
        this.nx_Cipher = nx_Cipher;
    }

    public void setNy(BigInteger[] ny) {
        this.ny = ny;
    }

    public void setNywni_Cipher(BigInteger[] nywni_Cipher) {
        this.nywni_Cipher = nywni_Cipher;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public BigInteger getX_Chiper() {
        return x_Chiper;
    }

    public BigInteger getY_Chiper() {
        return y_Chiper;
    }

    public int getZone() {
        return zone;
    }

    public MatrixUDG getpRide_Graph() {
        return pRide_Graph;
    }

    public int getPointA() {
        return PointA;
    }

    public int getPointB() {
        return PointB;
    }

    public int getPointNow2A_len() {
        return PointNow2A_len;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public BigInteger[] getRE_Graph() {
        return RE_Graph;
    }
    public int[] getRE_Graph_int(){
        int[] RE_Graph_int=new int[RE_Graph.length];
        for (int i = 0; i < RE_Graph.length; i++) {
            RE_Graph_int[i]=this.RE_Graph[i].intValue();
        }
        return RE_Graph_int;
    }

    public BigInteger[] getRE_Graph_Cipher() {
        return RE_Graph_Cipher;
    }

    public BigInteger[] getNx() {
        return nx;
    }

    public BigInteger[] getNx_Cipher() {
        return nx_Cipher;
    }

    public BigInteger[] getNy() {
        return ny;
    }

    public BigInteger[] getNywni_Cipher() {
        return nywni_Cipher;
    }
}
