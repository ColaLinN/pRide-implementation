/**
 * Java: ORH对象，封装了其各种操作
 * @author lfl
 * @date 2020/4/26
 *
 */
package pRideREALWORLD;

import Homo.PublicKey;
import pRIdeHalfImple.Road_embedding;

import java.math.BigInteger;

public class ORH {
    private PublicKey publicKey;
    public ORH(PublicKey publicKey){
        this.publicKey=publicKey;
    }
    //同台加密 密文相加
    public BigInteger HOMOAddition(BigInteger A_Chiper,BigInteger B_Chiper){
        return A_Chiper.multiply(B_Chiper).mod(this.publicKey.getnSquared());
    }
    public BigInteger[] calc_Pd(CP cp,BigInteger[] Pa_Chiper,BigInteger[] Pb_Chiper,BigInteger[] Pmu_chiper){
        Pb_Chiper=cp.HOMONEGATIVE(Pb_Chiper);//取负数
        //三个相加
        BigInteger Pd[]=new BigInteger[Pa_Chiper.length];
        for (int i = 0; i < Pa_Chiper.length; i++) {
            Pd[i]=HOMOAddition(Pa_Chiper[i],HOMOAddition(Pb_Chiper[i],Pmu_chiper[i]));
        }
        return Pd;
    }
    public void gc_compare_2(CP cp,BigInteger[] Rider_Driver1,BigInteger[] Driver1_mu,BigInteger[] Rider_Driver2,BigInteger[] Driver2_mu){

        Road_embedding REA=new Road_embedding();
        REA.two_point_gc_test(REA,cp.HOMO_decrypt_int(Rider_Driver1), cp.HOMO_decrypt_int(Rider_Driver2),
                                    cp.HOMO_decrypt_int(Driver1_mu),cp.HOMO_decrypt_int(Driver2_mu));
    }

//    public BigInteger[] calc_Rider_eta(){}
//    public
}
