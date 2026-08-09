ИНСТРУКЦИЯ ПО СБОРКЕ И ЗАПУСКУ

1. ТРЕБОВАНИЯ
 - Java 8 (1.8.0_502 или выше)
 - Maven 3.6.3 или выше
 - Docker (для SFTP-сервера)

2. СБОРКА ПРОЕКТОВ
 
 Основной проект (клиент):
 cd SFTP_infotecs
 mvn clean package
 
 Тестовый проект (автотесты):
 cd SFTP_test
 mvn clean package

3. ЗАПУСК КЛИЕНТА
 java -jar SFTP_infotecs/target/SFTP_infotecs-1.0-SNAPSHOT.jar

4. ЗАПУСК АВТОТЕСТОВ
 java -jar SFTP_test/target/sftp-client-tests-1.0-SNAPSHOT.jar src/test/java/org/example/testng.xml
 
 Или через Maven:
 cd SFTP_test
 mvn clean test

5. ПОДГОТОВКА SFTP-СЕРВЕРА (Docker)
 docker run -d -p 2222:22 --name sftp-test atmoz/sftp testuser:testpass:::upload
 docker exec sftp-test bash -c 'echo "[{\"domain\":\"google.com\",\"ip\":\"8.8.8.8\"},{\"domain\":\"yandex.ru\",\"ip\":\"77.88.55.77\"}]" > /home/testuser/upload/domains.json'
