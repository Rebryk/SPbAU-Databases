DROP TABLE IF EXISTS Laboratory CASCADE;
CREATE TABLE Laboratory (
  id              SERIAL PRIMARY KEY,
  name            TEXT NOT NULL UNIQUE,
  chief_last_name TEXT NOT NULL
);

DROP TABLE IF EXISTS Ingredient CASCADE;
CREATE TABLE Ingredient (
  id      SERIAL PRIMARY KEY,
  name    TEXT NOT NULL UNIQUE,
  formula TEXT NOT NULL UNIQUE
);

DROP TABLE IF EXISTS DrugType CASCADE;
CREATE TABLE DrugType (
  id   SERIAL PRIMARY KEY,
  type TEXT NOT NULL UNIQUE
);

DROP TABLE IF EXISTS Car CASCADE;
CREATE TABLE Car (
  id                    SERIAL PRIMARY KEY,
  number                TEXT                     NOT NULL UNIQUE,
  last_maintenance_date TIMESTAMP WITH TIME ZONE NOT NULL
);

DROP TABLE IF EXISTS Pharmacy CASCADE;
CREATE TABLE Pharmacy (
  id      SERIAL PRIMARY KEY,
  address TEXT    NOT NULL,
  number  INTEGER NOT NULL CHECK (number > 0),
  UNIQUE (address, number)
);

DROP TABLE IF EXISTS Distributor CASCADE;
CREATE TABLE Distributor (
  id           SERIAL PRIMARY KEY,
  address      TEXT NOT NULL,
  bank_account TEXT NOT NULL,
  first_name   TEXT NOT NULL,
  last_name    TEXT NOT NULL,
  phone_number TEXT NOT NULL
);

DROP TABLE IF EXISTS Warehouse CASCADE;
CREATE TABLE Warehouse (
  id      INTEGER PRIMARY KEY,
  address TEXT NOT NULL
);

DROP TABLE IF EXISTS Certificate CASCADE;
CREATE TABLE Certificate (
  id            SERIAL PRIMARY KEY,
  expiry_date   DATE    NOT NULL,
  laboratory_id INTEGER NOT NULL REFERENCES Laboratory
);

DROP TABLE IF EXISTS Drug CASCADE;
CREATE TABLE Drug (
  id                 SERIAL PRIMARY KEY,
  trade_name         TEXT    NOT NULL UNIQUE,
  international_name TEXT    NOT NULL UNIQUE,
  type_id            INTEGER NOT NULL REFERENCES DrugType,
  producer           TEXT,
  ingredient_id      INTEGER REFERENCES Ingredient,
  certificate_id     INTEGER REFERENCES Certificate
);

DROP TABLE IF EXISTS Delivery CASCADE;
CREATE TABLE Delivery (
  id                 SERIAL PRIMARY KEY,
  distributor_id     INTEGER                  NOT NULL REFERENCES Distributor,
  destination_id     INTEGER                  NOT NULL REFERENCES Warehouse,
  delivery_date      TIMESTAMP WITH TIME ZONE NOT NULL,
  receiver_last_name TEXT                     NOT NULL
);

DROP TABLE IF EXISTS DeliveryPart CASCADE;
CREATE TABLE DeliveryPart (
  id                    SERIAL PRIMARY KEY,
  delivery_id           INTEGER NOT NULL REFERENCES Delivery,
  drug_id               INTEGER NOT NULL REFERENCES Drug,
  travel_package_count  INTEGER NOT NULL CHECK (travel_package_count > 0),
  travel_package_weight INTEGER NOT NULL CHECK (travel_package_weight > 0),
  sale_package_count    INTEGER NOT NULL CHECK (sale_package_count > 0),
  sale_package_price    INTEGER NOT NULL CHECK (sale_package_price >= 0)
);

DROP TABLE IF EXISTS DrugSaleInfo CASCADE;
CREATE TABLE DrugSaleInfo (
  id              SERIAL PRIMARY KEY,
  drug_id         INTEGER NOT NULL REFERENCES Drug,
  pharmacy_id     INTEGER NOT NULL REFERENCES Pharmacy,
  price           INTEGER NOT NULL CHECK (price >= 0),
  amount          INTEGER NOT NULL CHECK (amount >= 0)
);

DROP TABLE IF EXISTS CarAssignment CASCADE;
CREATE TABLE CarAssignment (
  id           SERIAL PRIMARY KEY,
  date         TIMESTAMP WITH TIME ZONE NOT NULL,
  car_id       INTEGER                  NOT NULL REFERENCES Car,
  warehouse_id INTEGER                  NOT NULL REFERENCES Warehouse
);

DROP TABLE IF EXISTS CarAssignmentCargo CASCADE;
CREATE TABLE CarAssignmentCargo (
  id                 SERIAL PRIMARY KEY,
  car_assignment_id  INTEGER NOT NULL REFERENCES CarAssignment,
  target_pharmacy_id INTEGER NOT NULL REFERENCES Pharmacy,
  drug_id            INTEGER NOT NULL REFERENCES Drug,
  package_count      INTEGER NOT NULL CHECK (package_count > 0)
);
