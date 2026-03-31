drop table if exists BOOKING;
drop table if exists HOUSE;
drop table if exists LOCATION;
drop type if exists loc_type;
drop table if exists "user";

CREATE TABLE "user" (
    "IdUser" int GENERATED ALWAYS AS IDENTITY,
    "UserName" varchar(30) NOT NULL,
    "Email" varchar(50) NOT NULL UNIQUE,
    "Token" uuid DEFAULT gen_random_uuid(),
    CONSTRAINT PK_USER PRIMARY KEY ("IdUser")
);

CREATE TYPE loc_type AS ENUM ('COUNTRY', 'REGION', 'DISTRICT', 'MUNICIPALITY', 'LOCALITY');

CREATE TABLE LOCATION (
    "IdLocation" int GENERATED ALWAYS AS IDENTITY,
    "LocationName" varchar(30),
    "Type" loc_type NOT NULL,
    "ParentLocation" int,
    CONSTRAINT PK_LOCATION PRIMARY KEY ("IdLocation"),
    CONSTRAINT FK_LOCATION_PARENTLOCATION
        FOREIGN KEY ("ParentLocation")
        REFERENCES LOCATION("IdLocation")
);

CREATE TABLE HOUSE (
    "IdHouse" int GENERATED ALWAYS AS IDENTITY,
    "Title" varchar(30)  NOT NULL,
    "Location" int NOT NULL,
    "Area" double precision NOT NULL,
    "Price" double precision NOT NULL,
    "Description" text NOT NULL,
    CONSTRAINT PK_HOUSE PRIMARY KEY ("IdHouse"),
    CONSTRAINT FK_HOUSE_LOCATION
        FOREIGN KEY ("Location")
        REFERENCES LOCATION("IdLocation")
);

CREATE TABLE BOOKING (
    "IdBooking" int GENERATED ALWAYS AS IDENTITY,
    "StartDate" date NOT NULL,
    "EndDate" date NOT NULL,
    "IdUser" int NOT NULL,
    "IdHouse" int NOT NULL,
    CONSTRAINT PK_BOOKING PRIMARY KEY ("IdBooking"),
    CONSTRAINT FK_BOOKING_IDUSER
        FOREIGN KEY ("IdUser")
        REFERENCES "user"("IdUser"),
    CONSTRAINT FK_BOOKING_IDHOUSE
            FOREIGN KEY ("IdHouse")
            REFERENCES HOUSE("IdHouse")
);
