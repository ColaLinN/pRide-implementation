package yao.gate;

public class AndnotGate extends Gate
{
    public AndnotGate(Wire i1, Wire i2, Wire r) throws Exception
    {
        genEncryptedLut(0,0,1,0,i1,i2,r);
    }
}
