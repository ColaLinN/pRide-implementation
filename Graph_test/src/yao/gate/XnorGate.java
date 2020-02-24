package yao.gate;

public class XnorGate extends Gate
{
    public XnorGate(Wire i1, Wire i2, Wire r) throws Exception
    {
        genEncryptedLut(1,0,0,1,i1,i2,r);
    }
}
