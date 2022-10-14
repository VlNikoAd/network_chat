# **Проект "Сетевой чат"**
## Описание проекта:
**Разработка двух приложений для обмена текстовыми сообщениями по сети с помощью консоли (терминала) между двумя и более пользователями.**

Первое приложение - сервер чата, должно ожидать подключения пользователей.
Второе приложение - клиент чата, подключается к серверу чата и осуществляет доставку и получение новых сообщений.

1. Все сообщения записываются в file.log. File.log дополняется при каждом запуске, а также при отправленном или полученном сообщении. 
2. Выход из чата осуществляется по команде exit.
3. Установка порта для подключения клиентов через файл настроек (settings.txt);
4. В программе я создал 2 клиента для имитации общения двух пользователей. При необходимости, легко расширяется.

Использованные технологии в проекте:
* Сборщик проектов **Maven**;
* фреймворк тестировнания **JUnit**;
* File.log создается при запуске программы;