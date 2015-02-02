package bd.com.elites.bes.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.util.Log;

import android.util.Base64;

import com.sun.mail.util.BASE64EncoderStream;

//Copyright Popa Tiberiu 2011
//free to use this in any way

public class AES {
	private static Cipher ecipher;
	private static Cipher dcipher;

	private static final int iterationCount = 10;

	// 8-byte Salt
	private static byte[] salt = {

	(byte) 0xB2, (byte) 0x12, (byte) 0xD5, (byte) 0xB2,

	(byte) 0x44, (byte) 0x21, (byte) 0xC3, (byte) 0xC3 };

	static String nullPadString(String original) {
		String output = original;
		int remain = output.length() % 16;
		if (remain != 0) {
			remain = 16 - remain;
			for (int i = 0; i < remain; i++) {
				output += (char) 0;
			}
		}
		return output;
	}

	public static String encryptString(final String RAWDATA,
			final String secretKey, boolean ENCODE) throws Exception { // This
																		// was a
																		// custom
		String encrypted = null;
		byte[] encryptedBytes = null;
		byte[] key;
		key = secretKey.getBytes("UTF-8");
		SecretKeySpec skeySpec = new SecretKeySpec(key, "AES/ECB/NoPadding");
		// Inizializzo il cipher
		Cipher cipher = null;
		try {
			String input = Integer.toString(RAWDATA.length()) + '|' + RAWDATA;
			// String input = RAWDATA;
			cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
			encryptedBytes = cipher.doFinal(nullPadString(input).getBytes(
					"UTF-8"));
		} catch (Exception e) {
		}
		if (ENCODE) {
			// encrypted = new String(Base64.encodeBase64(encryptedBytes));
		} else {
			encrypted = new String(encryptedBytes);
		}
		return encrypted;
	}
	private static String cString = "sup3rS3xyeLITEs";
	// Decommentalo se ti serve il metodo di dectypt, adesso non lo usi perchè
	// il decrypt lo fa il server
	// public static String decryptString(final String ENCRYPTEDDATA,
	// final String secretKey, final boolean DECODE) throws Exception {
	// String raw = null;
	// byte[] rawBytes = null;
	// byte[] encryptedBytes;
	// if (DECODE) {
	// // encryptedBytes = Base64.decodeBase64(ENCRYPTEDDATA.getBytes());
	// } else {
	// encryptedBytes = ENCRYPTEDDATA.getBytes();
	// }
	// byte[] key;
	// key = secretKey.getBytes();
	// SecretKeySpec skeySpec = new SecretKeySpec(key, "AES/ECB/NoPadding");
	// // Inizializzo il cipher
	// Cipher cipher = null;
	// try {
	// cipher = Cipher.getInstance("AES");
	// cipher.init(Cipher.DECRYPT_MODE, skeySpec);
	// rawBytes = cipher.doFinal(encryptedBytes);
	// } catch (Exception e) {
	// }
	// raw = new String(rawBytes);
	// int delimiter = raw.indexOf('|');
	// int length = Integer.valueOf(raw.substring(0, delimiter));
	// raw = raw.substring(delimiter + 1, length + delimiter + 1);
	// return raw;
	// }

	public static String encrypt(String str) {

		try {
			String passPhrase = "My Secret Password";

			// create a user-chosen password that can be used with
			// password-based encryption (PBE)
			// provide password, salt, iteration count for generating PBEKey of
			// fixed-key-size PBE ciphers
			KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt,
					iterationCount);

			// create a secret (symmetric) key using PBE with MD5 and DES
			SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES")
					.generateSecret(keySpec);

			// construct a parameter set for password-based encryption as
			// defined in the PKCS #5 standard
			AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt,
					iterationCount);

			ecipher = Cipher.getInstance(key.getAlgorithm());
			dcipher = Cipher.getInstance(key.getAlgorithm());
			// encode the string into a sequence of bytes using the named
			// charset

			// storing the result into a new byte array.

			byte[] utf8 = str.getBytes("UTF8");

			byte[] enc = ecipher.doFinal(utf8);

			// encode to base64

			enc = BASE64EncoderStream.encode(enc);

			return new String(enc);

		}
		// catch (InvalidAlgorithmParameterException e) {
		// System.out.println("Invalid Alogorithm Parameter:" + e.getMessage());
		// return null;
		// }
		catch (InvalidKeySpecException e) {
			System.out.println("Invalid Key Spec:" + e.getMessage());
			return null;
		} catch (NoSuchAlgorithmException e) {
			System.out.println("No Such Algorithm:" + e.getMessage());
			return null;
		} catch (NoSuchPaddingException e) {
			System.out.println("No Such Padding:" + e.getMessage());
			return null;
		}
		// catch (InvalidKeyException e) {
		// System.out.println("Invalid Key:" + e.getMessage());
		// return null;
		// }
		catch (Exception e) {

			e.printStackTrace();
		}

		return null;

	}

	public static String encryptIt(String value) {
		try {
			DESKeySpec keySpec = new DESKeySpec(cString.getBytes("UTF8"));
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey key = keyFactory.generateSecret(keySpec);

			byte[] clearText = value.getBytes("UTF8");
			// Cipher is not thread safe
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.ENCRYPT_MODE, key);

			String encrypedValue = Base64.encodeToString(
					cipher.doFinal(clearText), 0);
			Log.d("tonmoy", "Encrypted: " + value + " -> " + encrypedValue);
			return encrypedValue;

		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		}
		return value;
	}
	
	public static String decryptIt(String value) {
	    try {
	        DESKeySpec keySpec = new DESKeySpec(cString.getBytes("UTF8"));
	        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
	        SecretKey key = keyFactory.generateSecret(keySpec);

	        byte[] encrypedPwdBytes = Base64.decode(value, Base64.DEFAULT);
	        // cipher is not thread safe
	        Cipher cipher = Cipher.getInstance("DES");
	        cipher.init(Cipher.DECRYPT_MODE, key);
	        byte[] decrypedValueBytes = (cipher.doFinal(encrypedPwdBytes));

	        String decrypedValue = new String(decrypedValueBytes);
	        Log.d("tonmoy", "Decrypted: " + value + " -> " + decrypedValue);
	        return decrypedValue;

	    } catch (InvalidKeyException e) {
	        e.printStackTrace();
	    } catch (UnsupportedEncodingException e) {
	        e.printStackTrace();
	    } catch (InvalidKeySpecException e) {
	        e.printStackTrace();
	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    } catch (BadPaddingException e) {
	        e.printStackTrace();
	    } catch (NoSuchPaddingException e) {
	        e.printStackTrace();
	    } catch (IllegalBlockSizeException e) {
	        e.printStackTrace();
	    }
	    return value;
	} 
}