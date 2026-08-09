package org.example;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static SftpClient client;
    private static BusinessLogic logic;
    private static final String FILE_NAME = "domains.json";
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Окно 'SFTP КЛИЕНТ'");
        System.out.print("Введите адрес: ");
        String host = scanner.nextLine();
        System.out.print("Введите порт: ");
        int port = Integer.parseInt(scanner.nextLine());
        System.out.print("Введите логин: ");
        String login = scanner.nextLine();
        System.out.print("Введите пароль: ");
        String password = scanner.nextLine();
        try {
            client = new SftpClient(host, port, login, password);
            client.connect();
            String json = client.downloadFile(FILE_NAME);
            logic = new BusinessLogic();
            logic.loadFromJson(json);
            showMenu(scanner);
        }
        catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
            e.printStackTrace();
        }
        finally {
            if (client != null) client.disconnect();
        }
        scanner.close();
    }

    private static void showMenu(Scanner scanner) {
        while (true) {
            System.out.println("\nМеню действий");
            System.out.println("1 - Показать все записи");
            System.out.println("2 - Найти IP по домену");
            System.out.println("3 - Найти домен по IP");
            System.out.println("4 - Добавить запись");
            System.out.println("5 - Удалить запись");
            System.out.println("6 - Выход и сохранение");
            System.out.print("Выберите: ");
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            }
            catch (NumberFormatException e) {
                System.out.println("Для выбора действия введите число от 1 до 6");
                continue;
            }
            switch (choice) {
                case 1: showAll(); break;
                case 2: findIp(scanner); break;
                case 3: findDomain(scanner); break;
                case 4: addRecord(scanner); break;
                case 5: deleteRecord(scanner); break;
                case 6: saveAndExit(); return;
                default: System.out.println("Этого действия нет в системе!");
            }
        }
    }

    private static void showAll() {
        List<DataModel> list = logic.getAllSorted();
        if (list.isEmpty()) {
            System.out.println("Список пуст");
        }
        else {
            for (DataModel item : list) {
                System.out.println(item);
            }
        }
    }
    private static void findIp(Scanner scanner) {
        System.out.print("Введите домен: ");
        String domain = scanner.nextLine();
        String ip = logic.findIpByDomain(domain);
        if (ip != null) {
            System.out.println("IP: " + ip);
        }
        else {
            System.out.println("Ошибка! Введенный домен не найден в системе!");
        }
    }
    private static void findDomain(Scanner scanner) {
        System.out.print("Введите IP: ");
        String ip = scanner.nextLine();
        String domain = logic.findDomainByIp(ip);
        if (domain != null) {
            System.out.println("Домен: " + domain);
        }
        else {
            System.out.println("Ошибка! Введенный IP не найден в системе!");
        }
    }
    private static void addRecord(Scanner scanner) {
        System.out.print("Введите домен: ");
        String domain = scanner.nextLine();
        System.out.print("Введите IP: ");
        String ip = scanner.nextLine();
        logic.addRecord(domain, ip);
    }
    private static void deleteRecord(Scanner scanner) {
        System.out.print("Введите домен или IP: ");
        String value = scanner.nextLine();
        logic.deleteRecord(value);
    }
    private static void saveAndExit() {
        try {
            String json = logic.toJson();
            client.uploadFile(FILE_NAME, json);
            System.out.println("Успешно! Изменения сохранены");
        } catch (Exception e) {
            System.err.println("Провал! Ошибка сохранения: " + e.getMessage());
        }
    }
}