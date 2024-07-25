/*
        Insecure Web App (IWA)

        Copyright (C) 2020 Micro Focus or one of its affiliates

        This program is free software: you can redistribute it and/or modify
        it under the terms of the GNU General Public License as published by
        the Free Software Foundation, either version 3 of the License, or
        (at your option) any later version.

        This program is distributed in the hope that it will be useful,
        but WITHOUT ANY WARRANTY; without even the implied warranty of
        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
        GNU General Public License for more details.

        You should have received a copy of the GNU General Public License
        along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.microfocus.example.utils;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Encrypted Password Utilities using BCryptPasswordEncoder
 *
 * @author Kevin A. Lee
 */
@SuppressWarnings("deprecation")
public class EncryptedPasswordUtils {

	private static final byte[] iv = { 22, 33, 11, 44, 55, 99, 66, 77 };
	private static final SecretKey keySpec = new SecretKeySpec(iv, "DES");
	
    public static String encryptPassword(String password) {
    	byte[] encrypted = null;
    	try {
			Cipher desCipher = Cipher.getInstance("DES");
			desCipher.init(Cipher.ENCRYPT_MODE, keySpec);
			encrypted = desCipher.doFinal(password.getBytes()); 	
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    	
        return new String(encrypted);
    }

    public static boolean matches(String password1, String password2) {
    	byte[] encrypted = null;
    	String encPassword1 = "";
    	try {
			Cipher desCipher = Cipher.getInstance("DES");
			desCipher.init(Cipher.ENCRYPT_MODE, keySpec);
			encrypted = desCipher.doFinal(password1.getBytes());
			encPassword1 = new String(encrypted); 
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
    	
        return encPassword1.equals(password2);
    }

}
