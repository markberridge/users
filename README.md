users
=====

A RESTful user app


MySQL Setup
-----------

1. Provision a MySQL server using docker:

```
docker run --name users-mysql -e MYSQL_ALLOW_EMPTY_PASSWORD=1 -d -p 3306:3306 mysql:latest
```

2. Create the `users` database:

```
docker exec -it users-mysql mysqladmin -uroot create users
```