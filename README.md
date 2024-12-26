# ForumHub API

ForumHub is a social networking platform designed to foster a collaborative environment for discussions and user interactions.

## Description

This project was developed as part of the Oracle Next Education (ONE) program in collaboration with Alura. ForumHub goes beyond the initial challenge requirements by offering a robust set of features, including:  
- User authentication and topic creation.  
- The ability for users to create and access new forums.  
- Participation in discussions through topics and comments.  
- A "high" (like) system to interact with content they enjoy.  

## Technologies

### Core
- **Java 21**
- **Spring Boot 3.4**
- **Spring Data JPA**
- **Spring Validation**
- **PostgreSQL**
- **Maven**

### Security
- **Spring Security 6**
- **JSON Web Token (JWT)**

### Testing
- **JUnit 5**
- **Mockito**
- **H2 Database**

### Caching
- **Spring Data Redis**
- **Spring Cache**

### Documentation
- **SpringDoc OpenAPI**

### DevOps
- **Spring Actuator**

### Development Tools
- **Spring DevTools**

## API Endpoints

### Documentation
| Method | Endpoint                | Description                      |
|--------|-------------------------|----------------------------------|
| GET    | `/swagger-ui.html`      | Access the Swagger UI.          |
| GET    | `/v3/api-docs`          | Retrieve the OpenAPI specs.     |

### Authentication
| Method | Endpoint                | Description                      |
|--------|-------------------------|----------------------------------|
| POST   | `/api/v1/auth/signup`   | User registration.              |
| POST   | `/api/v1/auth/login`    | User login.                     |

### Users
| Method | Endpoint                | Description                      |
|--------|-------------------------|----------------------------------|
| GET    | `/api/v1/users/{id}`    | Retrieve user details by ID.    |
| PUT    | `/api/v1/users/{id}`    | Update user details.            |
| DELETE | `/api/v1/users/{id}`    | Delete a user by ID.            |

### Forums
| Method | Endpoint                | Description                      |
|--------|-------------------------|----------------------------------|
| GET    | `/api/v1/forums`        | List all forums.                |
| GET    | `/api/v1/forums/{id}`   | Retrieve forum details by ID.   |
| POST   | `/api/v1/forums`        | Create a new forum.             |
| PUT    | `/api/v1/forums/{id}`   | Update forum details.           |
| DELETE | `/api/v1/forums/{id}`   | Delete a forum by ID.           |

### Topics
| Method | Endpoint                | Description                      |
|--------|-------------------------|----------------------------------|
| GET    | `/api/v1/topics`        | List all topics.                |
| GET    | `/api/v1/topics/{id}`   | Retrieve topic details by ID.   |
| POST   | `/api/v1/topics`        | Create a new topic.             |
| PUT    | `/api/v1/topics/{id}`   | Update topic details.           |
| DELETE | `/api/v1/topics/{id}`   | Delete a topic by ID.           |

### Likes
| Method | Endpoint                | Description                      |
|--------|-------------------------|----------------------------------|
| POST   | `/api/v1/likes`         | Add a like to content.          |
| DELETE | `/api/v1/likes`         | Remove a like.                  |

### Comments
| Method | Endpoint                | Description                      |
|--------|-------------------------|----------------------------------|
| GET    | `/api/v1/comments`      | List all comments.              |
| GET    | `/api/v1/comments/{id}` | Retrieve comment details by ID. |
| POST   | `/api/v1/comments`      | Add a new comment.              |
| PUT    | `/api/v1/comments/{id}` | Update comment details.         |
| DELETE | `/api/v1/comments/{id}` | Delete a comment by ID.         |
# ForumHub
# ForumHub
