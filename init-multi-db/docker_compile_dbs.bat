docker exec -i postgres_rdbms psql -U postgres -d main_postgres_db < 00-create_dbs.sql
docker exec -i postgres_rdbms psql -U postgres -d main_postgres_db < 01-artistas.sql
docker exec -i postgres_rdbms psql -U postgres -d main_postgres_db < 02-boletos.sql
docker exec -i postgres_rdbms psql -U postgres -d main_postgres_db < 03-catalogo.sql
docker exec -i postgres_rdbms psql -U postgres -d main_postgres_db < 04-compras.sql
docker exec -i postgres_rdbms psql -U postgres -d main_postgres_db < 05-notificaciones.sql
docker exec -i postgres_rdbms psql -U postgres -d main_postgres_db < 06-pagos.sql
docker exec -i postgres_rdbms psql -U postgres -d main_postgres_db < 07-precios.sql
docker exec -i postgres_rdbms psql -U postgres -d main_postgres_db < 08-recintos.sql
docker exec -i postgres_rdbms psql -U postgres -d main_postgres_db < 09-reportes.sql
docker exec -i postgres_rdbms psql -U postgres -d main_postgres_db < 10-usuarios.sql
    

    