USE ad;

CREATE TABLE IF NOT EXISTS GEO_Coordinates (ItemID INT PRIMARY KEY, Coordinates POINT NOT NULL) ENGINE=MyISAM;


INSERT INTO GEO_Coordinates SELECT item_id, POINT(latitude, longitude) FROM item_coordinates;


CREATE SPATIAL INDEX Coordinates_index ON GEO_Coordinates (Coordinates);


