package org.example;

import org.testng.Assert;
import org.testng.annotations.*;
import java.io.*;
import java.util.List;

public class SftpClientTests {
    private SftpClient client;
    private BusinessLogic logic;
    private ByteArrayOutputStream out;
    @BeforeMethod
    public void start() throws Exception {
        client = new SftpClient("localhost", 8888, "test_user", "test_password");
        client.connect();
        logic = new BusinessLogic();
        logic.loadFromJson(client.downloadFile("domains.json"));
        out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
    }
    @AfterMethod
    public void stop() {
        System.setOut(System.out);
        if (client != null) client.disconnect();
    }
    @Test
    public void testDownload() throws Exception {
        String json = client.downloadFile("domains.json");
        Assert.assertFalse(json.isEmpty());
        Assert.assertFalse(logic.getAllRecords().isEmpty());
    }
    @Test
    public void testFindIp() {
        Assert.assertEquals(logic.findIpByDomain("google.com"), "8.8.8.8");
    }

    @Test
    public void testAdd() {
        logic.addRecord("test.com", "1.2.3.4");
        Assert.assertEquals(logic.findIpByDomain("test.com"), "1.2.3.4");
    }
    @Test
    public void testSort() {
        logic.addRecord("zzz.com", "1.1.1.1");
        logic.addRecord("aaa.com", "2.2.2.2");
        List<DataModel> list = logic.getAllSorted();
        Assert.assertEquals(list.get(0).getDomain(), "aaa.com");
        Assert.assertEquals(list.get(2).getDomain(), "zzz.com");
    }
    @Test
    public void testNotFound() {
        out.reset();
        Assert.assertNull(logic.findIpByDomain("xxx.com"));
        Assert.assertTrue(out.toString().contains("Ошибка! Введенный домен не найден в системе!"));
    }
    @Test
    public void testBadIp() {
        out.reset();
        Assert.assertFalse(logic.addRecord("test.com", "999.999.999.999"));
        Assert.assertTrue(out.toString().contains("Ошибка: неверный IP-адрес"));
    }
    @Test
    public void testDeleteMissing() {
        out.reset();
        Assert.assertFalse(logic.deleteRecord("xxx.com"));
        Assert.assertTrue(out.toString().contains("Ошибка! Запись 'xxx.com' не найдена"));
    }
    @Test
    public void testInvalidIps() {
        String[] bad = {"999.999.999.999", "256.1.1.1", "1.2.3", "1.2.3.abc", "01.2.3.4", "1.2.3.256"};
        for (String ip : bad) {
            Assert.assertFalse(AddressValidation.isValidIPv4(ip));
        }
    }
    @Test
    public void testValidIps() {
        String[] good = {"192.168.1.1", "8.8.8.8", "0.0.0.0", "255.255.255.255", "10.0.0.1"};
        for (String ip : good) {
            Assert.assertTrue(AddressValidation.isValidIPv4(ip));
        }
    }
}