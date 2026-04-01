## Introduction

This document contains the relevant design and implementation aspects of LS project's first phase.

## Modeling the database

### Conceptual model ###

The following diagram holds the Entity-Relationship model for the information managed by the system.

![Entity-Relationship diagram](EA-diagram.drawio.png)

We highlight the following aspects:

- A booking is made by a single user, necessarily, and a user can make many bookings;
- A house can have many booking, and a booking must have, necessarily, one house;
- A house has to have a location, necessarily, and a location can have many houses;
- A location can have many children, but can only have one parent.

The attributes of each entity were defined in this phase's requirements, the only additional attribute being the token
of the USER entity, since we don't use the token as an id itself.

The conceptual model has the following restrictions:

- All attributes in the ER diagram are NOT NULL, except ParentId.

#### Why we chose surrogate keys as primary keys

We decided to stick to surrogate keys only, which is why even when we had good natural keys, such as the token in the
user entity,
we still decided to create a separate attribute UserId.

This was done because of consistency, we know that the identifier name of a particular
entity is going to be its name, and the Id suffix, in Pascal Case, e.g. UserId, LocationId, etc...

### Physical Model ###

The physical model of the database is available in [here](../../../../../sql/HouseRentalsDB.sql).

We highlight the following aspects of this model:

* (_include a list of relevant design issues_)

## Software organization

### Open-API Specification ###

(_include a link to the YAML or JSON file containing the Open-API Specification_)

In our Open-API specification, we highlight the following aspects:

(_include a list of relevant issues or details in your specification_)

### Request Details

(_describe how a request goes through the different elements of your solution_)

(_describe the relevant classes/functions used internally in a request_)

(_describe how and where request parameters are validated_)

### Connection Management

(_describe how connections are created, used and disposed_, namely its relation with transaction scopes).

### Data Access

(_describe any created classes to help on data access_).

(_identify any non-trivial used SQL statements_).

### Error Handling/Processing

(_describe how errors are handled and their effects on the application behavior_).

## Critical Evaluation

(_enumerate the functionality that is not concluded and the identified defects_)

(_identify improvements to be made on the next phase_)