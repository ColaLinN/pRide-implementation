import yao.Utils;
import yao.gate.*;

public class garbled_circuit {

    public boolean gc_compare_2(int d1,int d2,int mu1,int mu2){
        int a=0;
        try{
            //有这么一些数，这些数传给CP
            //这些数应该是同态加密的，CP会解密，不过这里没有同态加密，直接把四个数传给CP
//            int d1=12345;
//            int d2=54321;
//            int mu1=789;
//            int mu2=987;
            //什么，Java没有无符号数这回事儿我第一次听说，总之假设这些书都是小的正数

            if((d2-mu2)<0) d2=-d2;mu2=-mu2;
            if((d1-mu1)<0) d1=-d1;mu1=-mu1  ;
            //CP收到数字后先都变成位
            int i;
            int d1bit[]=new int[32];
            int d2bit[]=new int[32];
            int mu1bit[]=new int[32];
            int mu2bit[]=new int[32];
            for(i=0;i<32;i++)
            {
                d1bit[i]=(d1>>i)&1;
                d2bit[i]=(d2>>i)&1;
                mu1bit[i]=(mu1>>i)&1;
                mu2bit[i]=(mu2>>i)&1;
            }

            //接下来生成混淆电路
            //真值表，r1代表是否已决出胜负，r2代表如果比较成功哪个更大
            /*
             *   d1  d2  mu1 mu2 r1  r2
             *   0   0   0   0   0   -
             *   0   0   0   1   1   1
             *   0   0   1   0   1   0
             *   0   0   1   1   0   -
             *   0   1   0   0   1   0
             *   0   1   0   1   0   -
             *   0   1   1   0   1   0
             *   0   1   1   1   1   0
             *   1   0   0   0   1   1
             *   1   0   0   1   1   1
             *   1   0   1   0   0   -
             *   1   0   1   1   1   1
             *   1   1   0   0   0   -
             *   1   1   0   1   1   1
             *   1   1   1   0   1   0
             *   1   1   1   1   0   -
             */
            //r1=((d1 xor mu1)xnor(d2 xor mu2))and((not d1 and mu1)xnor(not d2 and mu2))
            //r2=(not d2 and mu2)or((d1 xor mu1)and not(not d1 and mu1)and not(d2 xor mu2))
            //别问上面俩怎么来的

            //生成一大堆线
            Wire[] d1w=new Wire[32];
            Wire[] d2w=new Wire[32];
            Wire[] mu1w=new Wire[32];
            Wire[] mu2w=new Wire[32];
            Wire[] t11w=new Wire[32];
            Wire[] t12w=new Wire[32];
            Wire[] t13w=new Wire[32];
            Wire[] t14w=new Wire[32];
            Wire[] t15w=new Wire[32];
            Wire[] t16w=new Wire[32];
            Wire[] r1w=new Wire[32];
            Wire[] t21w=new Wire[32];
            Wire[] t22w=new Wire[32];
            Wire[] t23w=new Wire[32];
            Wire[] t24w=new Wire[32];
            Wire[] t25w=new Wire[32];
            Wire[] t26w=new Wire[32];
            Wire[] r2w=new Wire[32];
            for(i=0;i<32;i++)
            {
                d1w[i]=new Wire();
                d2w[i]=new Wire();
                mu1w[i]=new Wire();
                mu2w[i]=new Wire();
                t11w[i]=new Wire();
                t12w[i]=new Wire();
                t13w[i]=new Wire();
                t14w[i]=new Wire();
                t15w[i]=new Wire();
                t16w[i]=new Wire();
                r1w[i]=new Wire();
                t21w[i]=new Wire();
                t22w[i]=new Wire();
                t23w[i]=new Wire();
                t24w[i]=new Wire();
                t25w[i]=new Wire();
                t26w[i]=new Wire();
                r2w[i]=new Wire();
            }

            //连接电路
            Gate[] g11=new Gate[32];
            Gate[] g12=new Gate[32];
            Gate[] g13=new Gate[32];
            Gate[] g14=new Gate[32];
            Gate[] g15=new Gate[32];
            Gate[] g16=new Gate[32];
            Gate[] g17=new Gate[32];
            Gate[] g21=new Gate[32];
            Gate[] g22=new Gate[32];
            Gate[] g23=new Gate[32];
            Gate[] g24=new Gate[32];
            Gate[] g25=new Gate[32];
            Gate[] g26=new Gate[32];
            Gate[] g27=new Gate[32];
            for(i=0;i<32;i++)
            {
                g11[i]=new XorGate(d1w[i],mu1w[i],t11w[i]);
                g12[i]=new XorGate(d2w[i],mu2w[i],t12w[i]);
                g13[i]=new XnorGate(t11w[i],t12w[i],t13w[i]);
                g14[i]=new AndnotGate(mu1w[i],d1w[i],t14w[i]);
                g15[i]=new AndnotGate(mu2w[i],d2w[i],t15w[i]);
                g16[i]=new XnorGate(t14w[i],t15w[i],t16w[i]);
                g17[i]=new AndGate(t13w[i],t16w[i],r1w[i]);

                g21[i]=new AndnotGate(mu2w[i],d2w[i],t21w[i]);//这个和g15是一样的，可以省线，不过先放着吧
                g22[i]=new XorGate(d1w[i],mu1w[i],t22w[i]);//g11
                g23[i]=new AndnotGate(mu1w[i],d1w[i],t23w[i]);//g14
                g24[i]=new AndnotGate(t22w[i],t23w[i],t24w[i]);
                g25[i]=new XorGate(d2w[i],mu2w[i],t25w[i]);//g12
                g26[i]=new AndnotGate(t24w[i],t25w[i],t26w[i]);
                g27[i]=new OrGate(t21w[i],t26w[i],r2w[i]);
            }

            //把所有门的lut弄出来，这些lut要发给计算方
            byte[][][] lut_g11=new byte[32][][];
            byte[][][] lut_g12=new byte[32][][];
            byte[][][] lut_g13=new byte[32][][];
            byte[][][] lut_g14=new byte[32][][];
            byte[][][] lut_g15=new byte[32][][];
            byte[][][] lut_g16=new byte[32][][];
            byte[][][] lut_g17=new byte[32][][];
            byte[][][] lut_g21=new byte[32][][];
            byte[][][] lut_g22=new byte[32][][];
            byte[][][] lut_g23=new byte[32][][];
            byte[][][] lut_g24=new byte[32][][];
            byte[][][] lut_g25=new byte[32][][];
            byte[][][] lut_g26=new byte[32][][];
            byte[][][] lut_g27=new byte[32][][];
            for(i=0;i<32;i++)
            {
                lut_g11[i]=g11[i].getLut();
                lut_g12[i]=g12[i].getLut();
                lut_g13[i]=g13[i].getLut();
                lut_g14[i]=g14[i].getLut();
                lut_g15[i]=g15[i].getLut();
                lut_g16[i]=g16[i].getLut();
                lut_g17[i]=g17[i].getLut();
                lut_g21[i]=g21[i].getLut();
                lut_g22[i]=g22[i].getLut();
                lut_g23[i]=g23[i].getLut();
                lut_g24[i]=g24[i].getLut();
                lut_g25[i]=g25[i].getLut();
                lut_g26[i]=g26[i].getLut();
                lut_g27[i]=g27[i].getLut();
            }

            //混淆输入，所有的输入值都要发给计算方
            byte[][] in_d1=new byte[32][];
            byte[][] in_d2=new byte[32][];
            byte[][] in_mu1=new byte[32][];
            byte[][] in_mu2=new byte[32][];
            for(i=0;i<32;i++)
            {
                if(d1bit[i]==0)
                {
                    in_d1[i]=d1w[i].getValue0();
                }
                else
                {
                    in_d1[i]=d1w[i].getValue1();
                }
                if(d2bit[i]==0)
                {
                    in_d2[i]=d2w[i].getValue0();
                }
                else
                {
                    in_d2[i]=d2w[i].getValue1();
                }
                if(mu1bit[i]==0)
                {
                    in_mu1[i]=mu1w[i].getValue0();
                }
                else
                {
                    in_mu1[i]=mu1w[i].getValue1();
                }
                if(mu2bit[i]==0)
                {
                    in_mu2[i]=mu2w[i].getValue0();
                }
                else
                {
                    in_mu2[i]=mu2w[i].getValue1();
                }
            }

            //计算方得到了所有的lut，用所有的lut构造出新的门
            Gate[] gate11=new Gate[32];
            Gate[] gate12=new Gate[32];
            Gate[] gate13=new Gate[32];
            Gate[] gate14=new Gate[32];
            Gate[] gate15=new Gate[32];
            Gate[] gate16=new Gate[32];
            Gate[] gate17=new Gate[32];
            Gate[] gate21=new Gate[32];
            Gate[] gate22=new Gate[32];
            Gate[] gate23=new Gate[32];
            Gate[] gate24=new Gate[32];
            Gate[] gate25=new Gate[32];
            Gate[] gate26=new Gate[32];
            Gate[] gate27=new Gate[32];
            for(i=0;i<32;i++)
            {
                gate11[i]=new Gate(lut_g11[i]);
                gate12[i]=new Gate(lut_g12[i]);
                gate13[i]=new Gate(lut_g13[i]);
                gate14[i]=new Gate(lut_g14[i]);
                gate15[i]=new Gate(lut_g15[i]);
                gate16[i]=new Gate(lut_g16[i]);
                gate17[i]=new Gate(lut_g17[i]);
                gate21[i]=new Gate(lut_g21[i]);
                gate22[i]=new Gate(lut_g22[i]);
                gate23[i]=new Gate(lut_g23[i]);
                gate24[i]=new Gate(lut_g24[i]);
                gate25[i]=new Gate(lut_g25[i]);
                gate26[i]=new Gate(lut_g26[i]);
                gate27[i]=new Gate(lut_g27[i]);
            }

            //输入值是in开头的byte变量
            //判别输出结果需要知道输出结果中的0和1分别对应什么，需要CP发给计算方，这里就不写了，就用r1和r2的getValue0和getValue1
            byte[] t1;
            byte[] t2;
            byte[] t3;
            byte[] t4;
            byte[] t5;
            byte[] t6;
            byte[] t7;
            //r1=((d1 xor mu1)xnor(d2 xor mu2))and((not d1 and mu1)xnor(not d2 and mu2))
            //r2=(not d2 and mu2)or((d1 xor mu1)and not(not d1 and mu1)and not(d2 xor mu2))
            for(i=0;i<32;i++)
            {
//                System.out.println("注目！");
//                System.out.println("決闘開始！");
                t1=gate11[(31-i)].operate(in_mu1[(31-i)],in_d1[(31-i)]);
                t2=gate12[(31-i)].operate(in_mu2[(31-i)],in_d2[(31-i)]);
                t3=gate13[(31-i)].operate(t2,t1);
                t4=gate14[(31-i)].operate(in_d1[(31-i)],in_mu1[(31-i)]);
                t5=gate15[(31-i)].operate(in_d2[(31-i)],in_mu2[(31-i)]);
                t6=gate16[(31-i)].operate(t5,t4);
                t7=gate17[(31-i)].operate(t6,t3);
                if(Utils.arraysAreEqual(t7,r1w[(31-i)].getValue0()))
                {
//                    System.out.println("そこまで！");
                    t1=gate21[(31-i)].operate(in_d2[(31-i)],in_mu2[(31-i)]);
                    t2=gate22[(31-i)].operate(in_mu1[(31-i)],in_d1[(31-i)]);
                    t3=gate23[(31-i)].operate(in_d1[(31-i)],in_mu1[(31-i)]);
                    t4=gate24[(31-i)].operate(t3,t2);
                    t5=gate25[(31-i)].operate(in_mu2[(31-i)],in_d2[(31-i)]);
                    t6=gate26[(31-i)].operate(t5,t4);
                    t7=gate27[(31-i)].operate(t6,t1);
                    if(Utils.arraysAreEqual(t7,r2w[(31-i)].getValue0()))
                    {
//                        System.out.println("負け犬");
//                        return false;
                        a=1;
//                        System.out.print("new"+a);
                        break;
                    }
                    else if(Utils.arraysAreEqual(t7,r2w[(31-i)].getValue1()))
                    {
//                        System.out.println("人生の勝利者");
//                        return true;//比谁大，p1赢了
                        a=2;
//                        System.out.print("new"+a);
                        break;
                    }
                }
                else
                {

                }
            }
//            if(a==0)
//            System.out.print("没有比较成功");
        }catch (Exception e){
            e.printStackTrace();
            return true;
        }
//        System.out.println("ohno");
//        System.out.println("return"+a);
        if(a==0){
//            System.out.println("没有比较成功");
            return true;
        }else if(a==1){
            return true;
        }else if(a==2){
            return false;
        }
//        System.out.println("nonono");
        return true;
    }
}
