docker exec -i postgres-db psql -U postgres -d postgres < 01-init.sql
docker exec -i postgres-db psql -U postgres -d postgres < 02-create_artistas.sql
docker exec -i postgres-db psql -U postgres -d postgres < 03-create_boletos.sql
docker exec -i postgres-db psql -U postgres -d postgres < 04-create_catalogo.sql
docker exec -i postgres-db psql -U postgres -d postgres < 05-create_compras.sql
docker exec -i postgres-db psql -U postgres -d postgres < 06-create_notificaciones.sql
docker exec -i postgres-db psql -U postgres -d postgres < 07-create_pagos.sql
docker exec -i postgres-db psql -U postgres -d postgres < 08-create_precios.sql
docker exec -i postgres-db psql -U postgres -d postgres < 09-create_recintos.sql
docker exec -i postgres-db psql -U postgres -d postgres < 10-create_reportes.sql
docker exec -i postgres-db psql -U postgres -d postgres < 11-create_usuarios.sql
    