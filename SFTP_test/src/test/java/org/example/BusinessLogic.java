package org.example;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class BusinessLogic {
    private List<DataModel> records = new ArrayList<>();
    public void loadFromJson(String json) throws Exception {
        records = JsonParser.parse(json);
        System.out.println("Успешно! Загружено записей: " + records.size());
    }
    public List<DataModel> getAllSorted() {
        List<DataModel> sorted = new ArrayList<>(records);
        sorted.sort(Comparator.comparing(DataModel::getDomain));
        return sorted;
    }
    public String findIpByDomain(String domain) {
        for (DataModel record : records) {
            if (record.getDomain().equalsIgnoreCase(domain)) {
                return record.getIp();
            }
        }
        return null;
    }
    public String findDomainByIp(String ip) {
        for (DataModel record : records) {
            if (record.getIp().equals(ip)) {
                return record.getDomain();
            }
        }
        return null;
    }
    public boolean addRecord(String domain, String ip) {
        if (!AddressValidation.isValidIPv4(ip)) {
            System.out.println("Ошибка: неверный IP-адрес");
            return false;
        }
        for (DataModel record : records) {
            if (record.getDomain().equalsIgnoreCase(domain)) {
                System.out.println("Ошибка: домен '" + domain + "' уже существует");
                return false;
            }
            if (record.getIp().equals(ip)) {
                System.out.println("Ошибка: IP '" + ip + "' уже существует");
                return false;
            }
        }

        records.add(new DataModel(domain, ip));
        System.out.println("Успешно! Добавлена запись: " + domain + "-" + ip);
        return true;
    }
    public boolean deleteRecord(String value) {
        Iterator<DataModel> iterator = records.iterator();
        while (iterator.hasNext()) {
            DataModel record = iterator.next();
            if (record.getDomain().equalsIgnoreCase(value) ||
                    record.getIp().equals(value)) {
                iterator.remove();
                System.out.println("Успешно! Удалена запись: " + record);
                return true;
            }
        }
        System.out.println("Ошибка! Запись '" + value + "' не найдена");
        return false;
    }
    public List<DataModel> getAllRecords() {
        return new ArrayList<>(records);
    }
    public String toJson() {
        return JsonParser.toJson(records);
    }
}