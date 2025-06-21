# Spring Boot Final Exam Project

This is my **first Spring Boot project**, built right after finishing a YouTube course by **Peachez Programming**. It’s basically like a final exam to test everything I learned during the course.

## Project Overview

The app is a simple but full-featured **Product Management System**, with CRUD operations, JWT security, filtering, caching, and a bit more. I built it from scratch using what I learned throughout the course.

Main things it covers:

- Full **CRUD** for products
- JWT-based Authentication & Authorization
- Category Management with relational mapping
- Search, sorting, filtering & pagination
- Caching for performance
- Input validation & error handling
- Unit + integration testing
- Clean logging

## Features

### Product Management
- Add, edit, delete, and get products  
- Each product has a UUID, name, description, price, category, region, etc.  
- Auto-managed created/updated timestamps  
- Region is an enum (US or CAN)  

### Search & Filtering
- Search by name or description  
- Filter by category  
- Sort by price or name  
- Pagination support  
- Results are cached for 5 minutes (Caffeine cache)

### Security
- JWT-based login  
- Two roles:  
  - `USER`: Can create/update products  
  - `SUPERUSER`: Can delete  
- Reading endpoints are public (no login needed)

### Validation
- Custom product validator  
- Good error messages  
- Global exception handler

### Testing
- Unit tests for services  
- Integration tests for repo layer  
- Controller tests for API  
- Validation tests  
- All written using **JUnit 5** + **Mockito**

## Tech Stack

- Spring Boot
- Java
- MySQL
- Spring Security + JWT
- Caffeine for caching
- JUnit & Mockito
- Lombok

## API Endpoints

### Auth
- `POST /register` – Register a new user  
- `POST /authenticate` – Log in and get JWT token

### Product (Public)
- `GET /products` – Get all products (paginated)  
- `GET /product/{id}` – Get product by ID  
- `GET /product/search` – Search by name/description  
- `GET /product/category` – Filter by category

### Product (Protected)
- `POST /product` – Create product (login required)  
- `PUT /product/{id}` – Update product (login required)  
- `DELETE /product/{id}` – Delete product (SUPERUSER only)

### Query Parameters
- `page` – Page number  
- `size` – Page size  
- `sortBy` – price_asc, price_desc, name, name_desc  
- `search` – Search keyword  
- `category` – Filter by category name

## Monitoring

Actuator endpoints included:
- `/actuator/health`  
- `/actuator/metrics`  
- `/actuator/info`

## Validation Rules

Each product must have:
- Name (required)  
- Description (min 20 chars)  
- Price (>= 0)  
- Manufacturer (required)  
- Region (US or CAN)  

## What I Learned

This project helped me practice:
- Spring Boot basics  
- JWT security  
- API design  
- Using JPA and database relationships  
- Writing clean, tested, and modular code  
- Using caching and validation  
- Debugging and logging

## Running the Project

If you want to clone this repo and run it yourself, you'll need to either:

- Set a few environment variables (for database and JWT), **or**
- Edit the `application.properties` file with your own settings.

Example env vars:
```bash
SPRING_DATASOURCE_USERNAME=your_mysql_user
SPRING_DATASOURCE_PASSWORD=your_mysql_password
JWT_SECRET=your_super_secret_key
```
Make sure you have MySQL running.

## Credit

Big thanks to the [**Peachez Programming** YouTube course](https://www.youtube.com/playlist?list=PL7TZZ2ip0DRCmJ57pzkc3EChRTJ6pm_bH) – super helpful for learning Spring Boot.
