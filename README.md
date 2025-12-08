# Ecommerce-Company-using-Microservice-Architecture
Backend for web application of an Ecommerce company using Microservices architecture.

All included Microservices:
1. Service Discovery - Service Registry using Eureka Server

2. Config Server - Centralized configuration management for all services
     Server - http://hostname:8888/
     GitHub - https://github.com/TanzeemAlam/ecommerce-application-config-repo

3. Zipkin Server - Distributed Logging & Tracing
      Server - http://hostname:9411/zipkin

4. Api Gateway -

5. User Service -

6. Product Service -

7. Product Detail Service -

8. Inventory Service -

9. Cart Service -

10. Order Service -

11. Notification Service -


## API Collection

## ----------User APIs----------
1. GET: /users/config - Get custom message from config server
2. POST: /users/register - Register new user
        {
             "username": "user",
             "password": "user",
             "role":     "ROLE_ADMIN"
        }
4. GET: /users/token - Get fresh JWT token from JWT Filter
        {
             "username": "user",
             "password": "user"
        }
5. GET: /users/verifyRegistration - Enable registered user by verifying registered token
       Example:- /users/verifyRegistration?token=lkewfsavsd

6. GET: /users/validateToken - Validate the existing user by JWT token
       Example:- /users/validateToken?token=lkewfsavsd

## ----------Product APIs----------
1. GET -  /products/config      - Get custom message from config server
2. POST - /products             - Register new product
   {
        "name":        "Trouser",
        "sku":         "P001",
        "description": "This is trouser",
        "category":    "Casual"
   }
3. GET - /products               - Get all products
4. GET - /products/{id}          - Get product by id
5. PUT - /products/{id}          - Update product by id
   {
        "name":        "Trouser",
        "sku":         "P001",
        "description": "This is trouser",
        "category":    "Casual"
   }
6. DELETE - /products/{id}        - Delete product by id
7. GET    - /products/{id}/exists - Validate if product exists

## ----------Product Detail APIs----------
1. GET  - /product-details/config     - Get custom message from config server
2. POST - /product-details            - Add product details
   {
        "price":     150,
        "size":      "M",
        "design":    "D",
        "productId":  "1"
   }
3. GET    - /product-details              - Get all product details
4. GET    - /product-details/{id}         - Get product detail by id
5. GET    - /product-details/{id}/product - Used by Product service
6. PUT    - /product-details/{id}         - Update product by id
7. DELETE - /product-details/{id}         - Delete product by id
8. GET    - /product-details/{id}/price   - Get product price
        
        
        
