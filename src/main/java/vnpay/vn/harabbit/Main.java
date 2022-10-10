package vnpay.vn.harabbit;

import vnpay.vn.harabbit.utils.AppUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;


/**
 * @author sontt1
 * Date:10/10/2022
 * Time:2:34 PM
 */
public class Main {
    static final String SECRET_KEY = "vnpay12345678.vn";
    public static void main(String[] args) throws NoSuchPaddingException, IllegalBlockSizeException, UnsupportedEncodingException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        String commandStr = "echo 123456 | sudo -S rabbitmqctl set_policy ha-two \"^two\\.\" \\\n" +
                "  '{\"ha-mode\":\"exactly\",\"ha-params\":2,\"ha-sync-mode\":\"automatic\"}'";
        String data = commandStr;

        System.err.println(AppUtils.encrypted(commandStr));
        System.out.println(AppUtils.decrypted(AppUtils.encrypted(commandStr)));
    }
}
