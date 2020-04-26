/**
 * Java: CP对象，封装了其各种操作
 * @author lfl
 * @date 2020/4/26
 *
 */
package pRideREALWORLD;

import Homo.KeyPair;
import Homo.KeyPairBuilder;
import Homo.PublicKey;
import pRIdeHalfImple.garbled_circuit;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class CP {
    //密钥对
    private KeyPair keypair;
    //公钥
    private PublicKey publicKey;

    public CP() {
        KeyPairBuilder keygen = new KeyPairBuilder();
        this.keypair = keygen.generateKeyPair();
        this.publicKey = keypair.getPublicKey();
    }
    public PublicKey getPublicKey() {
        return publicKey;
    }
    //这个应该是乘客司机均要有的方法，int加密
    public BigInteger HOMOencrypt(int num){
        return publicKey.encrypt(BigInteger.valueOf(num));
    }
    //加密
    public BigInteger HOMOencrypt(BigInteger num){
        return publicKey.encrypt(num);
    }
    //解密
    public BigInteger HOMO_decrypt(BigInteger num_Chiper){
        return this.keypair.decrypt(num_Chiper);
    }
    //数组解密
    public BigInteger[] HOMO_decrypt(BigInteger[] num_Chiper){
        for (int i = 0; i < num_Chiper.length; i++) {
            num_Chiper[i]=this.HOMO_decrypt(num_Chiper[i]);
        }
        return num_Chiper;
    }
    public int[] HOMO_decrypt_int(BigInteger[] num_Chiper){
        int[] num_Chiper_int=new int[num_Chiper.length];
        for (int i = 0; i < num_Chiper.length; i++) {
            num_Chiper_int[i]=this.HOMO_decrypt(num_Chiper[i]).intValue();
        }
        return num_Chiper_int;
    }
    //这个应该是乘客司机均要有的方法
    public BigInteger HOMOAddition(BigInteger A_Chiper,BigInteger B_Chiper){
        return A_Chiper.multiply(B_Chiper).mod(publicKey.getnSquared());
    }
    //这里实现一个密文数组解密，取负数，再加密的过程
    public BigInteger[] HOMONEGATIVE(BigInteger[] num_Chiper){
        //解密取负数
        for (int i = 0; i < num_Chiper.length; i++) {
            num_Chiper[i]=HOMOencrypt(this.HOMO_decrypt(num_Chiper[i]).multiply(BigInteger.valueOf(-1)));
        }
        return num_Chiper;
    }
    public void get_garbled_value(BigInteger[] Rider_Driver1,BigInteger[] Driver1_mu,BigInteger[] Rider_Driver2,BigInteger[] Driver2_mu){
        List result = new ArrayList();
        int[] Rider_Driver1_int=HOMO_decrypt_int(Rider_Driver1);
        int[] Driver1_mu_int=HOMO_decrypt_int(Driver1_mu);
        int[] Rider_Driver2_int=HOMO_decrypt_int(Rider_Driver2);
        int[] Driver2_mu_int=HOMO_decrypt_int(Driver2_mu);
//        garbled_circuit.get_GATE_IN(int d1, int d2, int mu1, int mu2)
        garbled_circuit gc=new garbled_circuit();
//        return ;
    }
    //测试
    public static void main(String[] args) {
        CP cp=new CP();
        BigInteger plainA =cp.HOMOencrypt(11) ;
        BigInteger plainB = cp.HOMOencrypt(222) ;

        BigInteger plainSum = cp.HOMOAddition(plainA,plainB);

        System.out.println(cp.HOMO_decrypt(plainSum));
        System.out.println(plainSum);
    }
}
