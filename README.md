# java-filmorate

![image](https://github.com/shmelactimel/java-filmorate/assets/135132888/ed89d0eb-005a-46d5-9236-04df322ac1fc)

## Примеры запросов

Получить email подтвержденных друзей Василия:
```
SELECT us.email
FROM users AS us
INNER JOIN 
	(SELECT *
	FROM friends AS f
	INNER JOIN users AS u ON u.user_id = f.user_id
	WHERE name = 'Vasilii') as s ON us.user_id = s.friends_id
WHERE s.approved = 'true'; 
```
Получить название худшего фильма о войне по мнению пользователей:
```
SELECT m.name
FROM movies AS m
INNER JOIN likes AS l ON m.film_id = l.likes_id
WHERE movie_genre = 'War'
GROUP BY m.name
ORDER BY COUNT(m.name) DESC
LIMIT 1;
```
Получить всех пользователей с email начинающимся с 'c':
```
SELECT *
FROM users AS u
WHERE u.email LIKE 'c%';
```
Получить топ 3 названия самых свежих вышедших фильма, начиная с 2015 года по убыванию:
```
SELECT name
FROM movies AS m
WHERE EXTRACT(YEAR FROM m.releasedate) > '2014'
ORDER BY m.releasedate DESC;
```
