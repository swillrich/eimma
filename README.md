# elmma

## Run all services

To run all services the docker-compose.yml file consists of, type `docker-compose up -d` (-d means running in daemon mode). To run with a prior build of all docker images, type: `docker-compose up -d --build` (recommended when running initially or after some time elapsed).

Services:

1. Dax snapshot http://localhost:8080/snapshot
2. All DAX prices between the given range as JSON: http://localhost:3000/prices?from=2011-05-02&to=2011-05-02
3. All DAX prices between the given range as CSV: http://localhost:3000/prices/csv?from=2011-05-02&to=2011-05-02 (for dygraph visualization purposes needed)
4. Show behavior of a KnockOut option tracking the DAX alongside a given period: http://localhost:3000/ko?from=2011-05-02&to=2011-05-02 ([...]:3000/ko/csv?[...] also available for CSV provision)
5. For data visualization / analysis purposes: http://localhost:3000/diagram.html

Helper:

1. To do a MySQL dump, connect yourself with the running Docker elmma-db container: `docker exec -it elmma-db bash`, then excecute the following command to dump the database to the (in the docker-compose.yml specified) shared folder (to have access to the file generated): `mysqldump -u elmma --password=123456 elmma > /var/lib/mysql/dump.sql`. After this, you get the dump.sql generated into the host root folder ./mysql.

# Domain Model #

The following picture shows the domain model / database schema of the ELMMa project.
[link text](src/[master]/elmma/elmma-model/diagram/de.elmma.model.package.png)