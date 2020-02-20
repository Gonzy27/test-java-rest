# test-java-rest

## How to use

All the data is created when the application is launch

#####Log in

The first step is to log in with a user.

The URL for this is

- #####localhost:8080/users/token/

Is necesary to send in header fields the next data: email and password

example: 

- Header: key = email, value = john.doe@gmail.com
- Header: key = password, value = Prueba12


The result is a token

    "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqb2huLmRvZUBnbWFpbC5jb20iLCJpc3MiOiJNci4gSm9obiIsImlhdCI6MTU4MjIyNzU5N30.KhQJh56KRBAZFZuOE2KcxF_hA_-seuYkN5a51rRH1iYCZXnwwknIQpbqw5SFWal6zmusH432DYudQNumd5F-cw"

This token is necesary to have access to all the URLs in the application.

This token is a Bearer Token type of Authorization

#####Log out

To logout the URL is:

- #####localhost:8080/users/token/logout

And the email is necesary in a header field.

example:

- Header: key = email, value = john.doe@gmail.com

## URL

All of this ULR require authentication.

#####Create user
Method: POST

- #####http://localhost:8080/api/users/

{

	"name": "Gonzalo",
	"email": "gonzalo@gmail.com",
	"password": "Pruebas12",
	"phones": [{
		"number": 7654327,
        "cityCode": 3,
        "contryCode": 51
	}]
}

#####List users
Method: GET

- #####http://localhost:8080/api/users/

#####List one user
Method: GET

- Path Variable: User id

- #####http://localhost:8080/api/users/87383306-9ec2-4a77-98b6-38e67269956b

#####Update one user
Method: PUT

- Path Variable: User id
- #####http://localhost:8080/api/users/87383306-9ec2-4a77-98b6-38e67269956b

{

	"name": "Gonzalo",
	"email": "gonzalo@gmail.com",
	"password": "Pruebas12"
}

#####Delete one user
Method: DELETE

- Path Variable: User id
- #####http://localhost:8080/api/users/87383306-9ec2-4a77-98b6-38e67269956b

#####List all phones 
Method: GET

- #####http://localhost:8080/api/users/phones/

#####List one phone
Method: GET

- Path Variable: Phone id
- #####http://localhost:8080/api/users/phones/1

#####Add one phone
Method: POST

- Path Variable: User id
- #####http://localhost:8080/api/users/dcab4b6e-802a-43bf-bf28-1c5b7c5e1c89/phones/

{

    "number": 1234569,
    "cityCode": 2,
    "contryCode": 53
}

#####Update one phone
Method: PUT
- Path Variable: User id
- Path Variable: Phone id
- #####http://localhost:8080/api/users/87383306-9ec2-4a77-98b6-38e67269956b/phones/2

{

    "number": 7654321|,
    "cityCode": 3,
    "contryCode": 51
}

#####Update one phone
Method: DELETE
- Path Variable: Phone id
- http://localhost:8080/api/users/phones/1

#####H2 console

- #####http://localhost:8080/h2-console/login.jsp

- JDBC URL: jdbc:h2:mem:testdb
- User Name: sa
- Password: sa
