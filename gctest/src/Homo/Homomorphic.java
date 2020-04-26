package Homo;//package java;

import java.math.BigInteger;

public class Homomorphic {

    private KeyPair keypair;
    private PublicKey publicKey;

    public Homomorphic() {
        KeyPairBuilder keygen = new KeyPairBuilder();
        this.keypair = keygen.generateKeyPair();
        this.publicKey = keypair.getPublicKey();
    }

    public KeyPair getKeypair() {
        return keypair;
    }

    public void setKeypair(KeyPair keypair) {
        this.keypair = keypair;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    //加法
    public void testHomomorphicAddition() {
        BigInteger plainA = BigInteger.valueOf(102);
        BigInteger plainB = BigInteger.valueOf(203);

        BigInteger encryptedA = publicKey.encrypt(plainA).multiply(new BigInteger("-1"));
        BigInteger encryptedB = publicKey.encrypt(plainB);

        BigInteger decryptedProduct = keypair.decrypt(encryptedA.multiply(encryptedB).mod(publicKey.getnSquared()));
        BigInteger plainSum = plainA.add(plainB).mod(publicKey.getN());

        System.out.println(decryptedProduct);
        System.out.println(plainSum);
    }

    public static void main(String[] args) {
        Homomorphic test=new Homomorphic();
        test.testHomomorphicAddition();
    }
}