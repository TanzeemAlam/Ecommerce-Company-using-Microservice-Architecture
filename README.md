# Ecommerce-Company-using-Microservice-Architecture
Backend for web application of an Ecommerce company using Microservices architecture.

All included Microservices:
1. **Service Discovery** - Service Registry using Eureka Server

2. **Config Server** - Centralized configuration management for all services
   **Server** - http://hostname:8888/
   **GitHub** - https://github.com/TanzeemAlam/ecommerce-application-config-repo

4. **Zipkin Server** - Distributed Logging & Tracing
      Server - http://hostname:9411/zipkin

5. **Api Gateway** - Single entry point for all client requests. Routes requests to backend services

6. **User Service** - Handles user registration, login, roles management. Issue JWT token

7. **Product Service** - Store core product information (name, SKU, category) and product kafka event to inventory

8. **Product Detail Service** - Store extended product metadata (price, design, size)

9. **Inventory Service** - Manage stock quantities and perform cart release/reserve operations

10. **Cart Service** - Manage cart item operations and produce kafka events to inventory

11. **Order Service** - Manage order related operations and publish kafka events to cart and inventory services

12. **Notification Service** - Produce notifications as logs for various events related to product, user, order.


### API Collection

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
        
## ----------Inventory APIs----------
1. POST   - /inventory/{productId}        - Add product stock count in inventory
2. PUT    - /inventory/{productId}        - Update product stock count from inventory
3. GET    - /inventory/{productId}        - Get inventory product detail
4. GET    - /inventory                    - Get all inventory products detail
5. Unaccessible APIs-
   5.1 POST - /inventory/reserve          - Kafka event to reserve requested product stock
   5.2 POST - /inventory/release          - Kafka event to release reserved product stock
   5.3 POST - /inventory/sold             - Kafka event to update reserved & total product quantity
6. GET     - /inventory/{productId}/quantity/{quantity}/validate      - Validate if request product & product quantity is available

## ----------Cart APIs----------
1. GET     - /cart/config                     - Get customer message from config server
2. POST    - /cart/{userId}/items             - Add item to cart via user id followed by inventory reserve kafka event
3. DELETE  - /cart/{userId}/items/{productId} - Remove item from cart via product id followed by inventory release kafka event
4. PUT     - /cart/{userId}/items             - Update item from cart via product dto followed by inventory reserve/release kafka event
5. GET     - /cart/{userId}                    - Get all user cart items via user id
6. DELETE  - /cart/{userId}                    - Delete all cart items along with cart followed by inventory release kafka events for all added cart items
7. GET     - /cart/{userId}/amount             - Get total cart cart

## ----------Order APIs----------
1. POST     - /order/{userId}                  - Create order via userId and cartId followed by cart status update and inventory stock update kafka events
2. GET      - /order/{userId}                  - Get order status

### Kafka Events Collection
## Topics
1. user-events
2. product-events
3. inventory-events
4. cart-reserve-events
5. cart-release-events
## Groups
1. notification-group
2. cart-group

## ----------User Service----------
1. Produce - Kafka event for Notification service via "user-events" topic to publish logs

## ----------Product Service----------
1. Produce - Kafka event for Notification service via "product-events" topic to public logs
2. Produce - Kafka event for Inventory service via "inventory-events" topic to register product in inventory

## ----------Inventory Service----------
1. Consume - Kafka event from Product service via "inventory-events" topic to register product in inventory
2. Consume - Kafka event from Cart service via "cart-reserve-events" topic to reserve product quantity in inventory
3. Consume - Kafka event from Cart service via "cart-release-events" topic to release product quantity in inventory

## ----------Cart Service----------
1. Produce - Kafka event for Inventory service via "cart-reserve-events" & "cart-release-events" topic to adjust product reserve/release quantity in inventory

## ----------Order Service----------
1. Produce - Kafka event for Cart service via "order-events" topic to place order which produce further events to confirm products sold
   
## ----------Notification Service----------
1. Consume - Kafka event from User service via "user-events" topic to log UserRegistered, TokenValidation, etc events.
2. Consume - Kafka event from Product service via "product-events" topic to log ProductCreation events.

## Docker Containers
Used docker-compose.yml file to create all 3 containers
1. Zipkin - openzipkin/zipkin:latest
2. Zookeeper - confluentinc/cp-zookeeper:7.5.0
3. Kafka - confluentinc/cp-kafka:7.5.0
