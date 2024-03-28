# java-filmorate

![image](https://github.com/shmelactimel/java-filmorate/assets/135132888/6a916ef0-90f2-4682-a60a-0866e6d8b18f)


## Примеры запросов

Get all users:
```
SELECT *
FROM user;
Get user by id == 1:

SELECT *
FROM user
WHERE user_id=1;
```
Get all friends of user with id == 1:
```
SELECT user_id,
       email,
       login,
       name,
       birthdate
FROM user
WHERE user_id IN
    (SELECT friend_id
     FROM friends
     WHERE user_id = 1);
```
Get all films:
```
SELECT *
FROM film;
Get film by id == 1:

SELECT *
FROM film
WHERE film_id=1;
```
Get 10 most popular films:
```
SELECT f.film_id,
       f.name,
       f.description,
       f.release_date,
       f.duration,
       r.name
FROM film AS f
LEFT JOIN rating AS r ON f.rating_id = r.rating_id
LEFT JOIN film_likes AS l ON f.film_id = l.film_id
GROUP BY f.film_id
ORDER BY count(l.user_id) DESC
LIMIT 10;
```
