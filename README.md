users
=====

A RESTful user app


MySQL Setup
-----------

1. Provision a MySQL server using docker:

```
docker run --name users-mysql -e MYSQL_ROOT_PASSWORD=letmein -d -p 3306:3306 mysql:latest
```

2. Create the `users` database:

```
docker exec users-mysql mysqladmin -uroot -pletmein create users
```