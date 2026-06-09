docker exec -i postgres-db psql -U postgres -d postgres < 00-create_dbs.sql
docker exec -i postgres-db psql -U postgres -d postgres < 01-artistas.sql
docker exec -i postgres-db psql -U postgres -d postgres < 02-boletos.sql
docker exec -i postgres-db psql -U postgres -d postgres < 03-catalogo.sql
docker exec -i postgres-db psql -U postgres -d postgres < 04-compras.sql
docker exec -i postgres-db psql -U postgres -d postgres < 05-notificaciones.sql
docker exec -i postgres-db psql -U postgres -d postgres < 06-pagos.sql
docker exec -i postgres-db psql -U postgres -d postgres < 07-precios.sql
docker exec -i postgres-db psql -U postgres -d postgres < 08-recintos.sql
docker exec -i postgres-db psql -U postgres -d postgres < 09-reportes.sql
docker exec -i postgres-db psql -U postgres -d postgres < 10-usuarios.sql
    

    