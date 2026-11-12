# ОПОРА

Проект о личностном росте и осмысленной жизни. Java 25, Spring Boot 3.5.16, Maven, Thymeleaf, Spring Data JPA, Liquibase и PostgreSQL.

## Запуск в IntelliJ IDEA

1. Откройте корневой pom.xml как Maven-проект, выполните Reload All Maven Projects.
2. Выберите JDK 25 в Project SDK и Maven Runner.
3. Запустите ru.aav.opora.OporaApplication. В конфигурации запуска задайте Active profiles: local и Working directory: корень проекта BEST.
4. Откройте http://localhost:8081/. Профиль local использует отдельную файловую H2 в data/opora.

Старая конфигурация BookStoreDemoApplication больше не применяется. Создайте новую запуском main в OporaApplication. Личные настройки IDE и старые локальные базы не удаляются и в коммит не входят.

## Команды из корня проекта

```sh
java -version
./mvnw -version
./mvnw clean verify
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

Остановка: Ctrl+C. Готовый JAR: target/opora-0.0.1-SNAPSHOT.jar.

```sh
java -jar target/opora-0.0.1-SNAPSHOT.jar --spring.profiles.active=local
```

Без профилей local/render требуется PostgreSQL. Используйте отдельную базу opora и переменные OPORA_DB_URL, OPORA_DB_USERNAME, OPORA_DB_PASSWORD. Пароли не записывайте в Git. Адрес сервера по умолчанию 127.0.0.1, порт 8081; переопределения SERVER_ADDRESS и PORT.

## Текущий каркас

- OporaApplication — точка входа и настройка Spring Boot.
- home/HomeController — обработка GET / и данные страницы.
- templates/home.html — серверный шаблон Thymeleaf.
- db/changelog/db.changelog-master.xml — собственная история схемы, пока без предметных таблиц.
- static/images/wolf.jpg — сохранённая копия символа проекта, сохранён для последующей работы над оформлением.
- OporaApplicationTest — проверка рендеринга страницы и независимости от магазина.

Реализованы главная, «Вернуть устойчивость», «Пережить трудное» и диалог «Смысл символа». «Найти направление» добавляется отдельным PR. Пользовательских аккаунтов, личных записей и административных функций нет.

## Первый запуск на Render

Пошаговая инструкция: [docs/render-deploy.txt](docs/render-deploy.txt).

- Dockerfile собирает и запускает приложение на Java 25.
- render.yaml описывает один бесплатный Docker Web Service для альтернативного запуска через Blueprint.
- application-render.properties включает профиль render с временной технической H2 в памяти. Задайте SPRING_PROFILES_ACTIVE=render и SERVER_ADDRESS=0.0.0.0; PORT предоставляет Render.
- RenderProfileTest проверяет запуск настоящего профиля и публичных страниц без внешнего PostgreSQL.

Профиль render не сохраняет данные после остановки. Перед добавлением аккаунтов и личных записей требуется отдельная постоянная база. Локальный профиль local продолжает использовать файловую H2. Book_Store и его настройки не используются.

Код книжного магазина доступен в предыдущих коммитах истории репозитория.
