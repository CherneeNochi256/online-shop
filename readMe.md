# Spring Boot, PostgreSQL, Spring Security, JWT, JPA, Rest API

Build Restful CRUD API for a online shop using Spring Boot, PostgreSQL, JPA and Hibernate.

## Steps to Setup

**1. Clone the application**

```bash
git clone https://github.com/CherneeNochi256/online-shop.git
```
**2. Create PostgreSQL database**
```bash
create database online_shop
```
**3. Change PostgreSQL username and password as per your installation**

+ open `src/main/resources/application.properties`
+ change `spring.datasource.username` and `spring.datasource.password` as per your PostgreSQL installation

**4. Run the app in your IDEA**

The app will start running at <http://localhost:8080>

## Explore Rest APIs

The app defines following CRUD APIs.

### Auth

| Method | Url | Decription | Sample Valid Request Body | 
| ------ | --- | ---------- | --------------------------- |
| POST   | /api/v1/auth/register | Sign up | [JSON](#register) |
| POST   | /api/v1/auth/authenticate | Log in | [JSON](#authenticate) |
| GET   | /api/v1/auth/refresh-token | Refresh token | |

### Comment

| Method | Url | Decription | Sample Valid Request Body | 
| ------ | --- | ---------- | --------------------------- |
| POST   | /api/v1/comments | Create comment | [JSON](#create-comment) |
| GET   | /api/auth/comments/{id}| Get comment by id ||

### Discount(for admins only)

| Method | Url | Decription | Sample Valid Request Body | 
| ------ | --- | ---------- | --------------------------- |
| GET   | /api/v1/discounts/{id} | Get discount by id |  |
| POST   | /api/v1/discounts  | Create discount for a product | [JSON](#create-discount) |
| POST   | /api/v1/discounts/products  | Create discount for a group of products | [JSON](#create-discount-for-group) |
| PUT   | /api/v1/discounts  | Update discount | [JSON](#update-discount) |
| PUT   | /api/v1/discounts  | Update discount for products by tag | [JSON](#update-discount-for-products) |

### Grade

| Method | Url | Decription | Sample Valid Request Body | 
| ------ | --- | ---------- | --------------------------- |
| POST   | api/v1/grades | Create a grade for a product | [JSON](#create-grade) |

### Notification

| Method | Url | Decription | Sample Valid Request Body | 
| ------ | --- | ---------- | --------------------------- |
| POST   | api/v1/notifications | Notify a user(for admins only)  | [JSON](#create-notification) |
| GET   | api/v1/notifications | Get all notifications of a current user ||

### Organization

| Method | Url | Decription | Sample Valid Request Body | 
| ------ | --- | ---------- | --------------------------- |
| PUT   | api/v1/organizations/{id}/frozen | Freeze the organization(for admins only) | |
| DELETE   | api/v1/organizations/{id} | Delete the organization(for admins only) | |
| POST   | api/v1/organizations | Create an organization | [JSON](#create-organization) |

### Product

| Method | Url | Decription | Sample Valid Request Body | 
| ------ | --- | ---------- | --------------------------- |
| POST   | api/v1/admin/products | Create product by admin| [JSON](#create-product-by-admin) |
| PUT   | api/v1/admin/products/{id} | Update product by admin | [JSON](#update-product-by-admin) |
| POST   | api/v1/products | Create product(for organization owners only) | [JSON](#create-product) |
| PUT   | api/v1/products/{id}| Update product (for organization owners only)| [JSON](#update-product) |
| GET   | api/v1/products/{id}| Get product | |
| GET   | api/v1/products| Get all products | |


### PurchaseHistory

| Method | Url | Decription | Sample Valid Request Body | 
| ------ | --- | ---------- | --------------------------- |
| GET   | api/v1/purchases/admin | Get specific user's purchase history (for admins only) | |
| GET   | api/v1/purchases | Get purchase history of current user | |
| POST   | api/v1/purchases | Buy the product | [JSON](#buy-product) |
| DELETE   | api/v1/purchases | Refund the product | [JSON](#refund-product) |

### User

| Method | Url | Decription | Sample Valid Request Body | 
| ------ | --- | ---------- | --------------------------- |
| GET   | api/v1/users/{id} | Get user by id ||
| PUT   | api/v1/users/{id}/balance | Top up user's balance (for admins only) | [JSON](#top-up-user-balance) |
| DELETE   | api/v1/users/{id} | Delete user (for admins only) ||
| PUT   | api/v1/users/{id}/frozen | Freeze user (for admins only) | |

### Validation form(for admins only)

| Method | Url | Decription | Sample Valid Request Body | 
| ------ | --- | ---------- | --------------------------- |
| GET   | api/v1/validation-forms | Get all validation forms ||
| POST   | api/v1/validation-forms/{id} | Validate the form | [JSON](#validate-form) |

Test them using postman or any other rest client.

## Sample Valid Request 

##### <a id="register">Sign Up -> /api/v1/auth/register</a>

```json
{
"username" : "Alex",
"email" : "alex@gmail.com",
"password" : "Password123"
}
```

##### <a id="authenticate">Log in -> /api/v1/auth/authenticate</a>

```json
{
"username" : "Alex",
"password" : "Password123"
}
```


##### <a id="create-comment">Create comment -> /api/v1/comments</a>

```json
{
"username" : "Alex",
"password" : "Password123"
}
```

```param
productId = 10
```

##### <a id="create-discount">Create discount -> /api/v1/discounts </a>

```json
"discount" : 0.15,
"interval" : 36000
```
```param
productId = 9
```

##### <a id="create-discount-for-group">Create discount for a group -> /api/v1/discounts/products </a>

```json
"discount" : 0.15,
"interval" : 36000
```
```param
productTag = 9
```
##### <a id="update-discount">Update discount -> /api/v1/discounts  </a>

```json
"discount" : 0.12,
"interval" : 48000
```
```param
productId = 7
```

##### <a id="update-discount-for-group">Update discount for a group -> /api/v1/discounts </a>

```json
"discount" : 0.3,
"interval" : 36000
```
```param
productTag = 11 
```

##### <a id="create-grade"> Estimate the product -> api/v1/grades </a>

```json
"value" : 4.7
```
```param
productTag = 16 
```

##### <a id="create-notification"> Notify the user -> api/v1/notifications </a>

```json
"header" : "Your header",
"text" : "Your notification"
```
```param
userId = 3 
```

##### <a id="create-organization"> Create an organization -> api/v1/organizations </a>


```json
"name" : "Organization name",
"description" : "Organization description"
```

##### <a id="update-product-by-admin"> Update product by admin -> api/v1/admin/products/{id} </a>

```json
"title" : "Product title",
"price" : 123.4,
"quantity" : 1000,
"organizationName" : "organization name"
```
```param
productId = 123
```

##### <a id="create-product-by-admin"> Create product by admin -> api/v1/admin/products </a>
##### <a id="create-product"> Create product -> api/v1/products </a>
##### <a id="update-product"> Create product -> api/v1/products/{id} </a>

```json
"title" : "Product title",
"price" : 123.4,
"quantity" : 1000,
"organizationName" : "organization name"
```

##### <a id="buy-product"> Buy the product -> api/v1/purchases </a>
##### <a id="refund-product"> Refund the product -> api/v1/purchases </a>

```param
productId = 102
```

##### <a id="top-up-user-balance"> Top up the user balance -> api/v1/users/{id}/balance </a>

```param
moneyAmount = 102.5
```

##### <a id="validate-form"> Validate the form -> api/v1/validation-forms/{id} </a>

```json
"message" : "smt is wrong",
"approved" : false
```
