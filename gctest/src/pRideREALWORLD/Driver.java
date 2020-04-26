/**
 * Java: 司机对象，封装了其各种操作
 * @author lfl
 * @date 2020/4/26
 *
 */
package pRideREALWORLD;

import Homo.PublicKey;
import pRIdeHalfImple.MatrixUDG;
import pRIdeHalfImple.Road_embedding;

import java.math.BigInteger;
import java.util.Arrays;

public class Driver {
    private int x;
    private int y;
    private MatrixUDG pRide_Graph;
    private int PointA;
    private int PointB;
    private int PointNow2A_len;
    private PublicKey publicKey;
    private BigInteger[] RE_Graph;    //路网嵌入图
    private BigInteger[] mu;
    private BigInteger[] RE_Graph_Cipher;
    private BigInteger[] mu_Cipher;
    private BigInteger[] RE_sub_mu_Cipher;
    public Driver(int x,int y,MatrixUDG pRide_Graph, int PointA, int PointB, int PointNow2A_len,PublicKey publicKey){
        this.x=x;
        this.y=y;
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

        this.mu=new BigInteger[pRide_Graph.mVexs.length];
        Arrays.fill(this.mu, BigInteger.valueOf(3000));

        this.RE_Graph_Cipher=new BigInteger[pRide_Graph.mVexs.length];
        for (int i = 0; i < this.pRide_Graph.mVexs.length; i++) {
            this.RE_Graph_Cipher[i]=this.HOMOencrypt(this.RE_Graph[i]);
        }

        this.mu_Cipher=new BigInteger[pRide_Graph.mVexs.length];
        for (int i = 0; i < this.pRide_Graph.mVexs.length; i++) {
            this.mu_Cipher[i]=this.HOMOencrypt(this.mu[i]);
        }

        this.RE_sub_mu_Cipher=new BigInteger[pRide_Graph.mVexs.length];

    }

    //同台加密  加密
    public BigInteger HOMOencrypt(BigInteger num){
        return this.publicKey.encrypt(num);
    }
    //同台加密 密文相加
    public BigInteger HOMOAddition(BigInteger A_Chiper,BigInteger B_Chiper){
        return A_Chiper.multiply(B_Chiper).mod(this.publicKey.getnSquared());
    }

    //仅有的一个setter，用于设置pb'=pb-mu
    public void setRE_sub_mu_Cipher(BigInteger[] RE_sub_mu_Cipher) {
        this.RE_sub_mu_Cipher = RE_sub_mu_Cipher;
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
    public BigInteger[] getMu() {
        return mu;
    }

    public BigInteger[] getRE_Graph_Cipher() {
        return RE_Graph_Cipher;
    }

    public BigInteger[] getMu_Cipher() {
        return mu_Cipher;
    }

    public BigInteger[] getRE_sub_mu_Cipher() {
        return RE_sub_mu_Cipher;
    }
}
