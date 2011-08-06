# Shortener schema
# --- !Ups
 
CREATE TABLE Shortener (
    id bigint(20) NOT NULL AUTO_INCREMENT,
    shortUrl varchar(12) NOT NULL,
    fullUrl varchar(4096) NOT NULL,
    fullUrlHash varchar(32) NOT NULL,
    PRIMARY KEY (id)
);
 
# --- !Downs
 
DROP TABLE Shortener;