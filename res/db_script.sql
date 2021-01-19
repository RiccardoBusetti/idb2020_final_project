-- OTHER CONFIGURATIONS
CREATE
EXTENSION IF NOT EXISTS "uuid-ossp";

-- TABLES DEFINITIONS
CREATE TABLE Room
(
    roomNo       smallint NOT NULL,
    B_street     text     NOT NULL,
    B_streetNo   smallint NOT NULL,
    B_postalCode int      NOT NULL,
    maxPeople    smallint NOT NULL,
    m2           smallint NOT NULL,
    PRIMARY KEY (roomNo, B_street, B_streetNo, B_postalCode)
);

CREATE TABLE Apartment
(
    R_roomNo       smallint NOT NULL,
    R_B_street     text     NOT NULL,
    R_B_streetNo   smallint NOT NULL,
    R_B_postalCode int      NOT NULL,
    numerOfRooms   smallint NOT NULL,
    PRIMARY KEY (R_roomNo, R_B_street, R_B_streetNo, R_B_postalCode)
);

CREATE TABLE SingleRoom
(
    R_roomNo       smallint NOT NULL,
    R_B_street     text     NOT NULL,
    R_B_streetNo   smallint NOT NULL,
    R_B_postalCode int      NOT NULL,
    PRIMARY KEY (R_roomNo, R_B_street, R_B_streetNo, R_B_postalCode)
);

CREATE TABLE DoubleRoom
(
    R_roomNo       smallint NOT NULL,
    R_B_street     text     NOT NULL,
    R_B_streetNo   smallint NOT NULL,
    R_B_postalCode int      NOT NULL,
    PRIMARY KEY (R_roomNo, R_B_street, R_B_streetNo, R_B_postalCode)
);

CREATE TABLE MultiRoom
(
    R_roomNo       smallint NOT NULL,
    R_B_street     text     NOT NULL,
    R_B_streetNo   smallint NOT NULL,
    R_B_postalCode int      NOT NULL,
    PRIMARY KEY (R_roomNo, R_B_street, R_B_streetNo, R_B_postalCode)
);

CREATE TABLE Building
(
    street     text     NOT NULL,
    streetNo   smallint NOT NULL,
    postalCode int      NOT NULL,
    PRIMARY KEY (street, streetNo, postalCode)
);

CREATE TABLE Person
(
    ssn     text        NOT NULL,
    name    varchar(50) NOT NULL,
    surname varchar(50) NOT NULL,
    PRIMARY KEY (ssn)
);

CREATE TABLE ISA_C_P
(
    C_mail text NOT NULL,
    P_ssn  text NOT NULL UNIQUE,
    PRIMARY KEY (C_mail)
);

CREATE TABLE Guest
(
    P_ssn text NOT NULL,
    PRIMARY KEY (P_ssn)
);

CREATE TABLE Customer
(
    mail text NOT NULL,
    PRIMARY KEY (mail)
);

CREATE TABLE InChargeOf
(
    B_street     text     NOT NULL,
    B_streetNo   smallint NOT NULL,
    B_postalCode int      NOT NULL,
    P_P_ssn      text     NOT NULL,
    PRIMARY KEY (B_street, B_streetNo, B_postalCode)
);

CREATE TABLE PersonInCharge
(
    P_ssn text NOT NULL,
    PRIMARY KEY (P_ssn)
);

CREATE TABLE Skill
(
    skillName text NOT NULL,
    PRIMARY KEY (skillName)
);

CREATE TABLE Has
(
    S_skillName text NOT NULL,
    P_P_ssn     text NOT NULL,
    PRIMARY KEY (S_skillName, P_P_ssn)
);

CREATE TABLE Books
(
    B_uuid UUID NOT NULL,
    C_mail text NOT NULL,
    PRIMARY KEY (B_uuid)
);

CREATE TABLE Complaint
(
    B_uuid  UUID NOT NULL,
    message text NOT NULL,
    PRIMARY KEY (B_uuid)
);

CREATE TABLE Complains
(
    Co_B_uuid UUID NOT NULL,
    Cu_mail   text NOT NULL,
    PRIMARY KEY (Co_B_uuid)
);

CREATE TABLE IsIn
(
    B_uuid  UUID NOT NULL,
    G_P_ssn text NOT NULL,
    PRIMARY KEY (B_uuid, G_P_ssn)
);

CREATE TABLE Booking
(
    uuid          UUID NOT NULL DEFAULT uuid_generate_v4(),
    startDate     date NOT NULL,
    endDate       date NOT NULL,
    reviewMessage text,
    reviewStars   smallint,
    CHECK (startDate <= endDate),
    CHECK (reviewStars >= 0 AND reviewStars <= 5),
    CHECK ((reviewMessage IS NULL AND reviewStars IS NULL) OR (reviewMessage IS NOT NULL AND reviewStars IS NOT NULL)),
    PRIMARY KEY (uuid)
);

CREATE TABLE With_
(
    B_uuid   UUID NOT NULL,
    Pay_code int  NOT NULL,
    PRIMARY KEY (B_uuid)
);

CREATE TABLE The
(
    B_uuid           UUID     NOT NULL,
    P_startDate      date     NOT NULL,
    P_R_roomNo       smallint NOT NULL,
    P_R_B_street     text     NOT NULL,
    P_R_B_streetNo   smallint NOT NULL,
    P_R_B_postalCode int      NOT NULL,
    PRIMARY KEY (B_uuid)
);

CREATE TABLE WithExtra
(
    S_id   int  NOT NULL,
    B_uuid UUID NOT NULL,
    PRIMARY KEY (S_id, b_uuid)
);

CREATE TABLE HasBasic
(
    S_id           int      NOT NULL,
    R_roomNo       smallint NOT NULL,
    R_B_street     text     NOT NULL,
    R_B_streetNo   smallint NOT NULL,
    R_B_postalCode int      NOT NULL,
    PRIMARY KEY (S_id, R_roomNo, R_B_street, R_B_streetNo, R_B_postalCode)
);

CREATE TABLE HasExtra
(
    S_id           int      NOT NULL,
    R_roomNo       smallint NOT NULL,
    R_B_street     text     NOT NULL,
    R_B_streetNo   smallint NOT NULL,
    R_B_postalCode int      NOT NULL,
    cost           money    NOT NULL,
    PRIMARY KEY (S_id, R_roomNo, R_B_street, R_B_streetNo, R_B_postalCode)
);

CREATE TABLE Service
(
    id   SERIAL,
    name text NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE Physical
(
    P_code int NOT NULL,
    PRIMARY KEY (P_code)
);

CREATE TABLE Digital
(
    P_code   int         NOT NULL,
    provider varchar(50) NOT NULL,
    PRIMARY KEY (P_code)
);

CREATE TABLE PaymentMethod
(
    code SERIAL,
    name TEXT NOT NULL,
    PRIMARY KEY (code)
);

CREATE TABLE Offers
(
    Pay_code           int      NOT NULL,
    Pac_startDate      date     NOT NULL,
    Pac_R_roomNo       smallint NOT NULL,
    Pac_R_B_street     text     NOT NULL,
    Pac_R_B_streetNo   smallint NOT NULL,
    Pac_R_B_postalCode int      NOT NULL,
    PRIMARY KEY (Pay_code, Pac_startDate, Pac_R_roomNo, Pac_R_B_street, Pac_R_B_streetNo, Pac_R_B_postalCode)
);

CREATE TABLE Package
(
    startDate      date     NOT NULL,
    R_roomNo       smallint NOT NULL,
    R_B_street     text     NOT NULL,
    R_B_streetNo   smallint NOT NULL,
    R_B_postalCode int      NOT NULL,
    endDate        date     NOT NULL,
    costPerNight   smallint NOT NULL,
    CHECK (startDate <= endDate),
    CHECK (costPerNight > 0),
    PRIMARY KEY (startDate, R_roomNo, R_B_street, R_B_streetNo, R_B_postalCode)
);


-- FOREIGN KEY DEFINITIONS
ALTER TABLE Apartment
    ADD FOREIGN KEY (R_roomNo, R_B_street, R_B_streetNo, R_B_postalCode) REFERENCES Room (roomNo, B_street, B_streetNo, B_postalCode)
        ON UPDATE CASCADE
        ON DELETE CASCADE
        DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE SingleRoom
    ADD FOREIGN KEY (R_roomNo, R_B_street, R_B_streetNo, R_B_postalCode) REFERENCES Room (roomNo, B_street, B_streetNo, B_postalCode)
        ON UPDATE CASCADE
        ON DELETE CASCADE
        DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE DoubleRoom
    ADD FOREIGN KEY (R_roomNo, R_B_street, R_B_streetNo, R_B_postalCode) REFERENCES Room (roomNo, B_street, B_streetNo, B_postalCode)
        ON UPDATE CASCADE
        ON DELETE CASCADE
        DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE MultiRoom
    ADD FOREIGN KEY (R_roomNo, R_B_street, R_B_streetNo, R_B_postalCode) REFERENCES Room (roomNo, B_street, B_streetNo, B_postalCode)
        ON UPDATE CASCADE
        ON DELETE CASCADE
        DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE Room
    ADD FOREIGN KEY (B_street, B_streetNo, B_postalCode) REFERENCES Building (street, streetNo, postalCode)
        ON UPDATE CASCADE
        ON DELETE CASCADE
        DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE Building
    ADD FOREIGN KEY (street, streetNo, postalCode) REFERENCES InChargeOf (B_street, B_streetNo, B_postalCode)
        DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE ISA_C_P
    ADD FOREIGN KEY (C_mail) REFERENCES Customer (mail)
        ON UPDATE CASCADE
        ON DELETE CASCADE
        DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE ISA_C_P
    ADD FOREIGN KEY (P_ssn) REFERENCES Person (ssn)
        ON UPDATE CASCADE
        ON DELETE CASCADE
        DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE Guest
    ADD FOREIGN KEY (P_ssn) REFERENCES Person (ssn)
        ON UPDATE CASCADE
        ON DELETE CASCADE
        DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE Customer
    ADD FOREIGN KEY (mail) REFERENCES ISA_C_P (C_mail)
        ON UPDATE CASCADE
        ON DELETE CASCADE
        DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE InChargeOf
    ADD FOREIGN KEY (B_street, B_streetNo, B_postalCode) REFERENCES Building (street, streetNo, postalCode)
        ON UPDATE CASCADE
        ON DELETE CASCADE
        DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE InChargeOf
    ADD FOREIGN KEY (P_P_ssn) REFERENCES PersonInCharge (P_ssn)
        ON UPDATE CASCADE
        ON DELETE CASCADE
        DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE PersonInCharge
    ADD FOREIGN KEY (P_ssn) REFERENCES Person (ssn)
        ON UPDATE CASCADE
        ON DELETE CASCADE
        DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE Has
    ADD FOREIGN KEY (S_skillName) REFERENCES Skill (skillName)
        ON UPDATE CASCADE
        ON DELETE CASCADE
        DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE Has
    ADD FOREIGN KEY (P_P_ssn) REFERENCES PersonInCharge (P_ssn)
        ON UPDATE CASCADE
        ON DELETE CASCADE
        DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE Books
    ADD FOREIGN KEY (B_uuid) REFERENCES Booking (uuid)
        ON UPDATE CASCADE
        ON DELETE CASCADE
        DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE Books
    ADD FOREIGN KEY (C_mail) REFERENCES Customer (mail)
        ON UPDATE CASCADE
        ON DELETE CASCADE
        DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE Complaint
    ADD FOREIGN KEY (B_uuid) REFERENCES Booking (uuid)
        ON UPDATE CASCADE
        ON DELETE CASCADE
        DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE Complaint
    ADD FOREIGN KEY (B_uuid) REFERENCES Complains (Co_B_uuid)
        ON UPDATE CASCADE
        ON DELETE CASCADE
        DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE Complains
    ADD FOREIGN KEY (Co_B_uuid) REFERENCES Complaint (B_uuid)
        ON UPDATE CASCADE
        ON DELETE CASCADE
        DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE Complains
    ADD FOREIGN KEY (Cu_mail) REFERENCES Customer (mail)
        ON UPDATE CASCADE
        ON DELETE CASCADE
        DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE IsIn
    ADD FOREIGN KEY (B_uuid) REFERENCES Booking (uuid)
        ON UPDATE CASCADE
        ON DELETE CASCADE
        DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE IsIn
    ADD FOREIGN KEY (G_P_ssn) REFERENCES Guest (P_ssn)
        ON UPDATE CASCADE
        ON DELETE CASCADE
        DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE Booking
    ADD FOREIGN KEY (uuid) REFERENCES With_ (B_uuid)
        ON UPDATE CASCADE
        ON DELETE CASCADE
        DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE Booking
    ADD FOREIGN KEY (uuid) REFERENCES The (B_uuid)
        ON UPDATE CASCADE
        ON DELETE CASCADE
        DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE Booking
    ADD FOREIGN KEY (uuid) REFERENCES Books (B_uuid)
        ON UPDATE CASCADE
        ON DELETE CASCADE
        DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE With_
    ADD FOREIGN KEY (B_uuid) REFERENCES Booking (uuid)
        ON UPDATE CASCADE
        ON DELETE CASCADE
        DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE With_
    ADD FOREIGN KEY (Pay_code) REFERENCES PaymentMethod (code)
        ON UPDATE CASCADE
        ON DELETE CASCADE
        DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE The
    ADD FOREIGN KEY (B_uuid) REFERENCES Booking (uuid)
        ON UPDATE CASCADE
        ON DELETE CASCADE
        DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE The
    ADD FOREIGN KEY (P_startDate, P_R_roomNo, P_R_B_street, P_R_B_streetNo,
                     P_R_B_Postalcode) REFERENCES Package (startDate, R_roomNo, R_B_street, R_B_streetNo, R_B_Postalcode)
        ON UPDATE CASCADE
        ON DELETE CASCADE
        DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE Package
    ADD FOREIGN KEY (R_roomNo, R_B_street, R_B_streetNo, R_B_Postalcode) REFERENCES Room (roomNo, B_street, B_streetNo, B_postalCode)
        ON UPDATE CASCADE
        ON DELETE CASCADE
        DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE Offers
    ADD FOREIGN KEY (Pay_code) REFERENCES PaymentMethod (code)
        ON UPDATE CASCADE
        ON DELETE CASCADE
        DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE Offers
    ADD FOREIGN KEY (Pac_startDate, Pac_R_roomNo, Pac_R_B_street, Pac_R_B_streetNo,
                     Pac_R_B_postalCode) REFERENCES Package (startDate, R_roomNo, R_B_street, R_B_streetNo, R_B_postalCode)
        ON UPDATE CASCADE
        ON DELETE CASCADE
        DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE Digital
    ADD FOREIGN KEY (P_code) REFERENCES PaymentMethod (code)
        ON UPDATE CASCADE
        ON DELETE CASCADE
        DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE Physical
    ADD FOREIGN KEY (P_code) REFERENCES PaymentMethod (code)
        ON UPDATE CASCADE
        ON DELETE CASCADE
        DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE HasExtra
    ADD FOREIGN KEY (S_id) REFERENCES Service (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
        DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE HasExtra
    ADD FOREIGN KEY (R_roomNo, R_B_street, R_B_streetNo, R_B_postalCode) REFERENCES Room (roomNo, B_street, B_streetNo, B_postalCode)
        ON UPDATE CASCADE
        ON DELETE CASCADE
        DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE HasBasic
    ADD FOREIGN KEY (S_id) REFERENCES Service (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
        DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE HasBasic
    ADD FOREIGN KEY (R_roomNo, R_B_street, R_B_streetNo, R_B_postalCode) REFERENCES Room (roomNo, B_street, B_streetNo, B_postalCode)
        ON UPDATE CASCADE
        ON DELETE CASCADE
        DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE WithExtra
    ADD FOREIGN KEY (S_id) REFERENCES Service (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
        DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE WithExtra
    ADD FOREIGN KEY (B_uuid) REFERENCES Booking (uuid)
        ON UPDATE CASCADE
        ON DELETE CASCADE
        DEFERRABLE INITIALLY IMMEDIATE;


-- TRIGGERS DEFINITIONS
-- PAYMENT METHOD DISJOINT TRIGGER
CREATE
OR REPLACE FUNCTION are_payment_method_children_disjoint()
  RETURNS trigger AS
  $$
BEGIN
  	IF
NOT EXISTS (
		SELECT P_code
		FROM Physical
		INTERSECT
		SELECT P_code
		FROM Digital
	) THEN
		RETURN NEW;
ELSE
		RAISE 'A children of payment method already exists with that code.';
END IF;
END;
	$$
LANGUAGE plpgsql;

CREATE TRIGGER is_physical_disjoint_trigger
    AFTER INSERT
    ON Physical
    FOR EACH ROW
    EXECUTE PROCEDURE are_payment_method_children_disjoint();

CREATE TRIGGER is_digital_disjoint_trigger
    AFTER INSERT
    ON Digital
    FOR EACH ROW
    EXECUTE PROCEDURE are_payment_method_children_disjoint();


-- CHILDREN DISJOINT TRIGGER
CREATE
OR REPLACE FUNCTION are_person_children_disjoint()
  RETURNS trigger AS
  $$
BEGIN
  	IF
NOT EXISTS (
		SELECT P_ssn
		FROM Guest
		INTERSECT
		SELECT P_ssn
		FROM ISA_C_P
		INTERSECT
		SELECT P_ssn
		FROM PersonInCharge
	) THEN
		RETURN NEW;
ELSE
		RAISE 'A children of person already exists with that ssn.';
END IF;
END;
	$$
LANGUAGE plpgsql;

CREATE TRIGGER is_guest_disjoint_trigger
    AFTER INSERT
    ON Guest
    FOR EACH ROW
    EXECUTE PROCEDURE are_person_children_disjoint();

CREATE TRIGGER is_isa_c_p_disjoint_trigger
    AFTER INSERT
    ON ISA_C_P
    FOR EACH ROW
    EXECUTE PROCEDURE are_person_children_disjoint();

CREATE TRIGGER is_person_in_charge_disjoint_trigger
    AFTER INSERT
    ON PersonInCharge
    FOR EACH ROW
    EXECUTE PROCEDURE are_person_children_disjoint();


-- ROOM DISJOINT TRIGGER
CREATE
OR REPLACE FUNCTION are_room_children_disjoint()
  RETURNS trigger AS
  $$
BEGIN
  	IF
NOT EXISTS (
		SELECT R_roomNo
		FROM Apartment
		INTERSECT
		SELECT R_roomNo
		FROM SingleRoom
		INTERSECT
		SELECT R_roomNo
		FROM DoubleRoom
		INTERSECT
		SELECT R_roomNo
		FROM MultiRoom
	) THEN
		RETURN NEW;
ELSE
		RAISE 'A children of room already exists with that room number.';
END IF;
END;
	$$
LANGUAGE plpgsql;

CREATE TRIGGER is_apartment_disjoint_trigger
    AFTER INSERT
    ON Apartment
    FOR EACH ROW
    EXECUTE PROCEDURE are_room_children_disjoint();

CREATE TRIGGER is_single_room_disjoint_trigger
    AFTER INSERT
    ON SingleRoom
    FOR EACH ROW
    EXECUTE PROCEDURE are_room_children_disjoint();

CREATE TRIGGER is_double_room_disjoint_trigger
    AFTER INSERT
    ON DoubleRoom
    FOR EACH ROW
    EXECUTE PROCEDURE are_room_children_disjoint();

CREATE TRIGGER is_multi_room_disjoint_trigger
    AFTER INSERT
    ON MultiRoom
    FOR EACH ROW
    EXECUTE PROCEDURE are_room_children_disjoint();


-- PACKAGE DATES OVERLAP TRIGGER
CREATE
OR REPLACE FUNCTION insert_package_if_dates_do_not_overlap()
  RETURNS trigger AS
  $$
BEGIN
  	IF
(NEW.startDate > NEW.endDate) THEN
			RAISE 'The starting date must be smaller or equal the ending date.';
ELSE
		IF NOT EXISTS (
			SELECT *
			FROM Package AS P
			WHERE NOT ((P.endDate <= NEW.startDate) OR (NEW.endDate <= P.startDate))
				AND P.R_roomNo = NEW.R_roomNo AND P.R_B_street = NEW.R_B_street AND P.R_B_streetNo = NEW.R_B_streetNo AND P.R_B_postalCode = NEW.R_B_postalCode
	   ) THEN
	   		RETURN NEW;
ELSE
			RAISE 'A package already exists with an overlapping date.';
END IF;
END IF;
END;
	$$
LANGUAGE plpgsql;

CREATE TRIGGER insert_package_if_dates_do_not_overlap_trigger
    BEFORE INSERT
    ON Package
    FOR EACH ROW
    EXECUTE PROCEDURE insert_package_if_dates_do_not_overlap();


-- PACKAGE EXTRA SERVICES
CREATE
OR REPLACE FUNCTION insert_extra_services_if_possible()
  RETURNS trigger AS
  $$
BEGIN
    IF
EXISTS (
			SELECT *
			FROM Booking AS B
			JOIN The AS T ON B.uuid = T.B_uuid
			JOIN Package AS P ON T.P_startDate = P.startDate
				AND T.P_R_roomNo = P.R_roomNo
				AND T.P_R_B_street = P.R_B_street
				AND T.P_R_B_streetNo = P.R_B_streetNo
				AND T.P_R_B_postalCode = P.R_B_postalCode
			JOIN Room AS R ON P.R_roomNo = R.roomNo
				AND P.R_B_street = R.B_street
				AND P.R_B_streetNo = R.B_streetNo
				AND P.R_B_postalCode = R.B_postalCode
			JOIN HasExtra AS H ON R.B_street = H.R_B_street
				AND R.B_streetNo = H.R_B_streetNo
				AND R.B_postalCode = H.R_B_postalCode
			WHERE NEW.B_uuid = B.uuid AND NEW.S_id = H.S_id
    ) THEN
        RETURN NEW;
ELSE
        RAISE 'The extra service is not offered by the booked room.';
END IF;
END;
	$$
LANGUAGE plpgsql;

CREATE TRIGGER insert_extra_services_if_possible_trigger
    AFTER INSERT
    ON WithExtra
    FOR EACH ROW
    EXECUTE PROCEDURE insert_extra_services_if_possible();


-- BOOKING CORRECT DATES TRIGGER
CREATE
OR REPLACE FUNCTION insert_booking_if_dates_are_correct()
  RETURNS trigger AS
  $$
BEGIN
		IF
EXISTS (
			SELECT *
			FROM Booking AS B, Package AS P
			WHERE NEW.P_startDate = P.startDate
				AND NEW.P_R_roomNo = P.R_roomNo
				AND NEW.P_R_B_street = P.R_B_street
				AND NEW.P_R_B_streetNo = P.R_B_streetNo
				AND NEW.P_R_B_postalCode = P.R_B_postalCode
				AND NEW.B_uuid = B.uuid
				AND B.startDate >= P.startDate AND B.endDate <= P.endDate
	   ) THEN
	   		RETURN NEW;
ELSE
			RAISE 'The booking has date intervals that are not within the ones of the package';
END IF;
END;
	$$
LANGUAGE plpgsql;

CREATE TRIGGER insert_booking_if_dates_are_correct_trigger
    BEFORE INSERT
    ON The
    FOR EACH ROW
    EXECUTE PROCEDURE insert_booking_if_dates_are_correct();


-- BOOKING OVERLAPPING DATES TRIGGER
CREATE
OR REPLACE FUNCTION insert_booking_if_dates_do_not_overlap()
  RETURNS trigger AS
  $$
BEGIN
    IF
NOT EXISTS (
        SELECT *
        FROM Booking AS B, Package AS P, Booking AS B2
        WHERE NEW.B_uuid <> B.uuid
					AND NEW.B_uuid = B2.uuid
	        AND NEW.P_startDate = P.startDate
			    AND NEW.P_R_roomNo = P.R_roomNo
					AND NEW.P_R_B_street = P.R_B_street
					AND NEW.P_R_B_streetNo = P.R_B_streetNo
					AND NEW.P_R_B_postalCode = P.R_B_postalCode
          AND NOT ((B.endDate <= B2.startDate) OR (B2.endDate <= B.startDate))
    ) THEN
        RETURN NEW;
ELSE
        RAISE 'The booking has overlapping date intervals with another booking.';
END IF;
END;
	$$
LANGUAGE plpgsql;

CREATE TRIGGER insert_booking_if_dates_do_not_overlap_trigger
    BEFORE INSERT
    ON The
    FOR EACH ROW
    EXECUTE PROCEDURE insert_booking_if_dates_do_not_overlap();


-- SAMPLE DATA
BEGIN;
SET CONSTRAINTS ALL DEFERRED;

INSERT INTO Person
VALUES ('ABC', 'Riccardo', 'Busetti');

INSERT INTO Person
VALUES ('DEF', 'Ramon', 'Piha');

INSERT INTO Customer
VALUES ('riccardo@gmail.com');

INSERT INTO ISA_C_P
VALUES ('riccardo@gmail.com', 'ABC');

INSERT INTO PersonInCharge
VALUES ('DEF');

INSERT INTO InChargeOf
VALUES ('Via Mario Rossi', 10, 39100, 'DEF');

INSERT INTO Building
VALUES ('Via Mario Rossi', 10, 39100);

INSERT INTO Room
VALUES (1, 'Via Mario Rossi', 10, 39100, 5, 150);

INSERT INTO Apartment
VALUES (1, 'Via Mario Rossi', 10, 39100, 3);

INSERT INTO Service(name)
VALUES ('Wi-Fi'),
       ('Pulizie'),
       ('Bagno privato');

INSERT INTO HasBasic
VALUES (1, 1, 'Via Mario Rossi', 10, 39100),
       (2, 1, 'Via Mario Rossi', 10, 39100);

INSERT INTO HasExtra
VALUES (3, 1, 'Via Mario Rossi', 10, 39100, 20);

INSERT INTO Package
VALUES ('2021-05-01', 1, 'Via Mario Rossi', 10, 39100, '2021-06-01', 50);

INSERT INTO PaymentMethod(name)
VALUES ('Money'),
       ('Google Pay');

INSERT INTO Physical
VALUES (1);

INSERT INTO Digital
VALUES (2, 'Google');

INSERT INTO Offers
VALUES (1, '2021-05-01', 1, 'Via Mario Rossi', 10, 39100),
       (2, '2021-05-01', 1, 'Via Mario Rossi', 10, 39100);

DO
$$
	DECLARE
my_uuid UUID;
BEGIN
INSERT INTO Booking(startDate, endDate)
VALUES ('2021-05-10', '2021-05-15') RETURNING uuid
INTO my_uuid;
INSERT INTO With_
VALUES (my_uuid, 1);
INSERT INTO Books
VALUES (my_uuid, 'riccardo@gmail.com');
INSERT INTO The
VALUES (my_uuid, '2021-05-01', 1, 'Via Mario Rossi', 10, 39100);
END
	$$;
COMMIT;