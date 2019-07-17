package com.liujhblack;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SFTPv3Client;
import ch.ethz.ssh2.SFTPv3FileAttributes;
import com.liujhblack.util.EncryptUtil;
import com.liujhblack.util.Result;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class BroswerLinuxApplicationTests {

	@Test
	public void contextLoads() throws Exception {
		String src="10.23.4.241,root,aa,22";
		String publicKey="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDZfxIXR5W5Tmw4Be+ezM0jH7p8KRrnpQyb0679wYExAXoTXE/1kLTaz/+9+MlbxNhNk4R/A8Fduk0oHcL0328SMOZVUAAYhJCoxJYeLBXffWJR1AkljBv2mm2FOWQTeCyAmf1a2yWF3w0f0+mbg2rvycexC6iU7qiMfqsHFdqf+wIDAQAB";
		String rsaPrivateKey="MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBANl/EhdHlblObDgF757MzSMfunwpGuelDJvTrv3BgTEBehNcT/WQtNrP/734yVvE2E2ThH8DwV26TSgdwvTfbxIw5lVQABiEkKjElh4sFd99YlHUCSWMG/aabYU5ZBN4LICZ/VrbJYXfDR/T6ZuDau/Jx7ELqJTuqIx+qwcV2p/7AgMBAAECgYEAt3/zRnraAr78pQO1GHjYNmMllm2jyn7BNZOSl3u0QSFq2nzO5XNScy58Kc6GLIvWpxTn+7WyZh6xzD/X5XvBm7xCt+L6ZPt5O7/wqUqjp6WAPufymbXwBiyyzz0DJL6w+0CkMul6FivjV628zHVgSYi7jlkRTk9W9m7I2S5YusECQQDtkcxlvNrc38o/tvxWyW5sY8O9rmHIZdGVMyt0bcYBlsRrg2M6mBWtZfWUfNRpeSqBHft+q3v5ezO1VMkYO2hhAkEA6l6dB1ilHD9E+Q1LrmmYPmQN1FgP+9YNoH22CgKpLpKwWpxqaaRhhaCccrGqMjmFM5nnA3R8ifF12qIARNR12wJBAMQagBDTLg75JGgn0nCJYf9S8vcWhVz4v2JblNlM7A/Ptl/RWw25ENvLuEZULLrL7AwdBcbwIywzSOG8FStNjsECQDJ7Fo+ShF3FMvIB7x8uF2C45FGsdiTkQiMjcKZPVGl3pwydTD5c7bR+l7QMmIAg65PlvmB8IqcDn0LsSeqJaKkCQCEOF5Zk4nqXD0363LbgucRuJpn4sES/6uJL0jCDu1AGxZq2wvbl6sJ1EG5/cHVgzXc9NmWBcvFLN7q9Oq6pGXg=";

		String s = EncryptUtil.rsaPublicKeyEncode(src, publicKey);
		String s1 = EncryptUtil.rsaPrivateKeyDecode(s, rsaPrivateKey);
	}

	@Test
	public void sftp() throws IOException {
        Connection connection = new Connection("39.107.33.18", 22);
        connection.connect();
        boolean flag = connection.authenticateWithPassword("root","Liu123456");
        if (!flag) {
            System.out.println("连接失败");
        }
        SFTPv3Client sftPv3Client=new SFTPv3Client(connection);
        SFTPv3FileAttributes stat = sftPv3Client.stat("/root/test/aaa.png");
        System.out.println("连接成功");
        connection.close();
        sftPv3Client.close();
    }

}
