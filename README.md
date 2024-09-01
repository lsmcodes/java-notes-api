# Notes API

<div style="display:inline-block">
        <picture>
                <source media="(prefers-color-scheme: light)" srcset="https://img.shields.io/badge/Java-black?style=for-the-badge&logo=OpenJDK&logoColor=white">
                <img src="https://img.shields.io/badge/Java-white?style=for-the-badge&logo=OpenJDK&logoColor=black" />
        </picture>
        <picture>
                <source media="(prefers-color-scheme: light)" srcset="https://img.shields.io/badge/Gradle-black?style=for-the-badge&logo=Gradle&logoColor=white">
                <img src="https://img.shields.io/badge/Gradle-white?style=for-the-badge&logo=Gradle&logoColor=black" />
        </picture>
        <picture>
                <source media="(prefers-color-scheme: light)" srcset="https://img.shields.io/badge/Spring_Boot-black?style=for-the-badge&logo=SpringBoot&logoColor=white">
                <img src="https://img.shields.io/badge/Spring_Boot-white?style=for-the-badge&logo=SpringBoot&logoColor=black" />
        </picture>
	<picture>
                <source media="(prefers-color-scheme: light)" srcset="https://img.shields.io/badge/Spring_Security-black?style=for-the-badge&logo=SpringSecurity&logoColor=white">
                <img src="https://img.shields.io/badge/Spring_Security-white?style=for-the-badge&logo=SpringSecurity&logoColor=black" />
        </picture>
	<picture>
                <source media="(prefers-color-scheme: light)" srcset="https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JsonWebTokens&logoColor=white">
                <img src="https://img.shields.io/badge/JWT-white?style=for-the-badge&logo=JsonWebTokens&logoColor=black" />
        </picture>
	<picture>
                <source media="(prefers-color-scheme: light)" srcset="https://img.shields.io/badge/FlyWay-black?style=for-the-badge&logo=FlyWay&logoColor=white">
                <img src="https://img.shields.io/badge/FlyWay-white?style=for-the-badge&logo=FlyWay&logoColor=black" />
        </picture>
        <picture>
                <source media="(prefers-color-scheme: light)" srcset="https://img.shields.io/badge/Swagger-black?style=for-the-badge&logo=Swagger&logoColor=white">
                <img src="https://img.shields.io/badge/Swagger-white?style=for-the-badge&logo=Swagger&logoColor=black" />
        </picture>
        <picture>
                <source media="(prefers-color-scheme: light)" srcset="https://img.shields.io/badge/PostgreSQL-black?style=for-the-badge&logo=PostgreSQL&logoColor=white">
                <img src="https://img.shields.io/badge/PostgreSQL-white?style=for-the-badge&logo=PostgreSQL&logoColor=black" />
        </picture>
</div>

## Overview

A notes API created as a challenge for the "Publicando Sua API REST na Nuvem Usando Spring Boot 3, Java 17 e Railway" course by DIO.

## Endpoints

This API provides the following endpoints:

### Authentication

| URI                           | Method | Action                                         | Parameters | Request Body          |
| ----------------------------- | ------ | ---------------------------------------------- | ---------- | --------------------- |
| `/notes-api/authentication` | POST   | Authenticates a user and generates a JWT token | N/A        | Authentication Schema |

#### Authentication schema

```json
{
  "username": "user",
  "password": "1234567890"
}
```

### User

| URI                  | Method | Action                                                                                                                                                            | Parameters | Request Body |
| -------------------- | ------ | ----------------------------------------------------------------------------------------------------------------------------------------------------------------- | ---------- | ------------ |
| `/notes-api/users` | POST   | Creates a user                                                                                                                                                    | N/A        | User Schema  |
| `/notes-api/users` | GET    | Retrieves the authenticated user details                                                                                                                          | N/A        | N/A          |
| `/notes-api/users` | PUT    | Updates the authenticated user details (invalidates the current JWT token, a new token must be issued, unless the details are reverted to their original values) | N/A        | User Schema  |
| `/notes-api/users` | DELETE | Deletes the authenticated user                                                                                                                                    | N/A        | N/A          |

#### User Schema

```json
{
  "name": "Default User",
  "username": "user",
  "password": "1234567890"
}
```

### Notes

| URI                                 | Method | Action                                                             | Parameters                                | Request Body |
| ----------------------------------- | ------ | ------------------------------------------------------------------ | ----------------------------------------- | ------------ |
| `/notes-api/notes`                | POST   | Creates a note                                                     | N/A                                       | Note Schema  |
| `/notes-api/notes`                | GET    | Retrieves all notes                                                | page, size, property, sortDirection       | N/A          |
| `/notes-api/notes/search/by-term` | GET    | Retrieves all notes containing a term whether in title or content | term, page, size, property, sortDirection | N/A          |
| `/notes-api/notes/search/by-tags` | GET    | Retrieves all notes containing at least one of the specified tags  | tags, page, size, property, sortDirection | N/A          |
| `/notes-api/notes/{id}`           | PUT    | Updates a note by id                                               | id                                        | Note Schema  |
| `/notes-api/notes/{id}`           | DELETE | Deletes a note by id                                               | id                                        | N/A          |

#### Note Schema

```json
{
  "tags": [
    "tag1",
    "tagN"
  ],
  "title": "Untitled",
  "content": "Sample content"
}
```

## Documention

Swagger available in the following URL, only with `dev` or `test` profile:

```
http://localhost:8080/swagger-ui/index.html
```

## License

This project is under the MIT license.
