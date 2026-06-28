docker exec postgres-rdbms psql -U postgres -d main_postgres_db -f /docker-entrypoint-initdb.d/00-create_dbs.sql
docker exec postgres-rdbms psql -U postgres -d main_postgres_db -f /docker-entrypoint-initdb.d/01-artistas.sql
docker exec postgres-rdbms psql -U postgres -d main_postgres_db -f /docker-entrypoint-initdb.d/02-boletos.sql
docker exec postgres-rdbms psql -U postgres -d main_postgres_db -f /docker-entrypoint-initdb.d/03-catalogo.sql
docker exec postgres-rdbms psql -U postgres -d main_postgres_db -f /docker-entrypoint-initdb.d/04-compras.sql
docker exec postgres-rdbms psql -U postgres -d main_postgres_db -f /docker-entrypoint-initdb.d/05-notificaciones.sql
docker exec postgres-rdbms psql -U postgres -d main_postgres_db -f /docker-entrypoint-initdb.d/06-pagos.sql
docker exec postgres-rdbms psql -U postgres -d main_postgres_db -f /docker-entrypoint-initdb.d/07-precios.sql
docker exec postgres-rdbms psql -U postgres -d main_postgres_db -f /docker-entrypoint-initdb.d/08-recintos.sql
docker exec postgres-rdbms psql -U postgres -d main_postgres_db -f /docker-entrypoint-initdb.d/09-reportes.sql
docker exec postgres-rdbms psql -U postgres -d main_postgres_db -f /docker-entrypoint-initdb.d/10-usuarios.sql
    

    