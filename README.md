# Household Backend

## About

This project is a spring boot project with NoSQL as database.
There are REST apis for interacting with the backend.

The backend deletes all old data on start and writes some example documents into the database.

## How to run

Navigate to target directory and run:

```sh
java -jar householdbackend-0.0.1-SNAPSHOT.jar
```

## Example test

For the example data that is written at startup, only household id 2 qualify for Family Togetherness Scheme
as there is a husband and wife and a child under 18 years old.
```sh
curl http://localhost:3001/household/list-fts-qualifying-households

[{"id":2,"housingType":"Condominium","familyMembers":[{"id":3,"householdId":2,"name":"Adam","gender":"Male","maritalStatus":"Married","spouseName":"Mary","occupationType":"Employed","annualIncome":30000,"dateOfBirth":631123200},{"id":4,"householdId":2,"name":"Mary","gender":"Female","maritalStatus":"Married","spouseName":"Adam","occupationType":"Employed","annualIncome":30000,"dateOfBirth":631123200},{"id":5,"householdId":2,"name":"Jane","gender":"Female","maritalStatus":"Single","spouseName":"Jane","occupationType":"Unemployed","annualIncome":0,"dateOfBirth":1577808000}]}]
```

If we delete family member id 5, which is the child in the family, the household no longer qualifies for the Family Togetherness Scheme
```sh
curl http://localhost:3001/family-member/delete?id=5

curl http://localhost:3001/household/list-fts-qualifying-households

[]
```

## Example endpoints

Create household
```sh
curl -d '{"housingType":"HDB"}' -H "Content-Type: application/json" -X POST http://localhost:3001/household/create
```

Create family member
```sh
curl -d '{"name":"James", "gender":"Male","maritalStatus":"Single","spouseName":"None","occupationType":"Student","annualIncome":10000,"dateOfBirth":631123200}' -H "Content-Type: application/json" -X POST http://localhost:3001/family-member/create
```

Get list of all households
```sh
curl http://localhost:3001/household/get-all
```

Get list of all households with family members
```sh
curl http://localhost:3001/household/list-all
```

Get list of all family members
```sh
curl http://localhost:3001/family-member/get-all
```

Delete household and all family members with household id
```sh
curl http://localhost:3001/household/delete?id=1
```

Delete family member with family member id
```sh
curl http://localhost:3001/family-member/delete?id=1
```

Get list of all households that qualify for Student Encouragement Bonus with family members
```sh
curl http://localhost:3001/household/list-seb-qualifying-households
```

Get list of all households that qualify for Family Togetherness Scheme with family members
```sh
curl http://localhost:3001/household/list-fts-qualifying-households
```

Get list of all households that qualify for Elder Bonus Bonus with family members
```sh
curl http://localhost:3001/household/list-eb-qualifying-households
```

Get list of all households that qualify for Baby Sunshine Grant with family members
```sh
curl http://localhost:3001/household/list-bsg-qualifying-households
```

Get list of all households that qualify for YOLO GST Grant with family members
```sh
curl http://localhost:3001/household/list-ygg-qualifying-households
```