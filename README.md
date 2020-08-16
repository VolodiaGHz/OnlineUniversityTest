# OnlineUniversityTest
  В даному проекті використовується java8, Spring boot 2.2.4, PostgreSQL 10.13.2(потрібної версії не знайшов), в якості тімплейті для веб сторінок використовую плагін Mustache.
Доступ до бд знаходиться в resources/application.properties. Перед запуском потрібно заміти(пароль, логін, посилання на бд, а також назву схеми), все інше зробить Spring. 
Валідація даних(перевірка на те щоб поля не були пусті) виконується за допомогую Spring і плагіна Mustache. 
Перевірка на те щоб емейл був правильний і на те що він взагалі існує немає. Тому бажано при реєстрації нових користувачів використовуйте справжні емейли. В іншому випадку буде
викидатись помилка під час спроби надсилання листів. 
  Під час запуску потрібно екcпортувати всі залежності maven, запустити проект можливо в класі Application. Проект буде піднято на localhost:8080. На цій сторінці можна буде
побачити перехід на сторінку профілю і список всіх зарезервованих і підтверджених уроків. При переході на сторінку профілю, не залогований користувач буде перенесений на сторінку
логування. На ній можно перейти на 2 сторінки реєстрації(1. Реєстрація студентів 2. Реєстрація викладачів). Після логування вас перекине на сторінку профіля. Де можна буде
створити новий урок. А також на сторінці студента, після створення уроку, новостворений ним урок буде відображатись на його сторінці де він може його скасувати. В той же час
появиться такий же самий урок в профілі викладача, де він може або підтвердити його або скасувати.  Також буде оповіщено викладача і студента про те що було створено урок завдяки 
емейлу, де буде посилання на сторінку профілю де можна буде преглянути і модифікувати даний урок. 
