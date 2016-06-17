package com.mychamp.nacl;

import static org.abstractj.kalium.encoders.Encoder.HEX;

import org.abstractj.kalium.NaCl;
import org.abstractj.kalium.NaCl.Sodium;
import org.abstractj.kalium.crypto.Box;
import org.abstractj.kalium.keys.KeyPair;
import org.abstractj.kalium.keys.PublicKey;

/**
 * A demonstration of the libsodium crypto library and Kalium Java wrappers of same.
 * You must have libsodium installed on target machine. For OS X try Homebrew like this:
 *     brew install libsodium
 * 
 * @author Bill Champ
 */
public class App
{

    public static void main (String[] args)
    {
        App app = new App ();
        app.runDemo1 ();
    }

    public App ()
    {
        NaCl.init (); // initialize the sodium library
    }

    public void runDemo1 ()
    {
        /*** Set up keys ****/
        KeyPair aliceKeyPair = new KeyPair ();
        String alice_web_site_public_key = aliceKeyPair.getPublicKey ().toString ();
        System.out.println ("Alice's public key as listed on her web site: " + alice_web_site_public_key);

        KeyPair bobKeyPair = new KeyPair ();
        String bob_web_site_public_key = bobKeyPair.getPublicKey ().toString ();
        System.out.println ("Bob's public key as listed on his web site: " + bob_web_site_public_key);

        System.out.println ();

        /*** Alice prepares message and encrypts it. ****/
        String plainMsgToBob = "Hi Bob. We attack at dawn. We attack at dawn. We attack at dawn. Cíaó!";
        EncryptedMessage msg = this.aliceSendsMsgToBob (plainMsgToBob, bob_web_site_public_key, aliceKeyPair);

        System.out.println ();

        /**** Bob receives message and decrypts it. *****/
        String bobDecodedMessage = this.bobReadsMsg (msg.getCipherMessage(), msg.getNonce (), alice_web_site_public_key, bobKeyPair);
        System.out.println ("Alice's message to Bob as decrypted by Bob: " + new String (HEX.decode (bobDecodedMessage)));

    }

    /**
     * 
     * @param message
     *            Plain text message
     * @param recipientPublicKey
     *            hex-encoded string
     * @param aliceKeys
     *            Alice's own KeyPair
     * @return An array of two byte arrays. nonce in first slot of array and encrypted message in second slot.
     */
    private EncryptedMessage aliceSendsMsgToBob (String message, String recipientPublicKey, KeyPair aliceKeys)
    {
        Sodium s = NaCl.sodium ();
        byte[] nonce = new byte[s.NONCE_BYTES];
        s.randombytes (nonce, nonce.length);

        System.out.println ("Alice's plain text message: " + message);
        System.out.println ("Alice's nonce in hex: " + HEX.encode (nonce));
        Box box = new Box (new PublicKey (recipientPublicKey), aliceKeys.getPrivateKey ());
        byte[] encMsgToBob = box.encrypt (nonce, message.getBytes ());
        String alice_enc_msg_hex_string = HEX.encode (encMsgToBob);
        System.out.println ("Alice's encrypted message to Bob in hex: " + alice_enc_msg_hex_string);

        return new EncryptedMessage (HEX.encode (nonce), HEX.encode (encMsgToBob));
    }

    /**
     * 
     * @param encMsg
     *            hex-encoded string
     * @param nonce
     *            hex-encoded string
     * @param senderPublicKey
     *            hex-encoded string
     * @param bobKeys
     *            Bob's own KeyPair
     */
    private String bobReadsMsg (String encMsg, String nonce, String senderPublicKey, KeyPair bobKeys)
    {
        Box bobBox = new Box (new PublicKey (senderPublicKey), bobKeys.getPrivateKey ());
        byte[] bob_nonce = HEX.decode (nonce);
        byte[] bob_encMsg = HEX.decode (encMsg);
        byte[] decodedMsg = bobBox.decrypt (bob_nonce, bob_encMsg);
        return (HEX.encode (decodedMsg));
    }
    
    /**
     * Inner class to wrap nonce and message into a single object.
     */
    class EncryptedMessage
    {
        private String nonce;
        private String cipherMessage;

        public EncryptedMessage (String pNonce, String pCipherMessage)
        {
            this.nonce = pNonce;
            this.cipherMessage = pCipherMessage;
        }

        public String getNonce ()
        {
            return nonce;
        }

        public String getCipherMessage ()
        {
            return cipherMessage;
        }

    }

}
