# Spring Boot Rewards Calculation Application

This is a simple Spring Boot application that calculates reward points for users based on their transaction amounts.

## Rewards Calculation Logic

1. **Spending between $50 and $100:**
   - Users earn **1 reward point** per dollar spent between **$50 and $100**.
   - Example: If a user spends **$70**, they earn **(70 - 50) = 20 points**.

2. **Spending above $100:**
   - Users earn **2 reward points** per dollar spent **above $100**.
   - Example: If a user spends **$120**, they earn **((120 - 100) * 2) + 50 = 90 points**.

## Database
- The application uses an **H2 in-memory database**.
- Sample data is loaded at startup using a `data.sql` file.

## API Endpoints (RewardsController)
The application provides three REST endpoints for reward calculations:

1. **`GET /lifetime` (getLifetimeRewards)**
   - Calculates the total and month-wise reward points for a user over their **entire lifetime**.
   - **Query Parameter:** `userId`

2. **`GET /lastThreeMonths` (getLastThreeMonthRewards)**
   - Calculates the total and month-wise reward points for a user over the **last three months**.
   - **Query Parameter:** `userId`

3. **`GET /specifiedPeriod` (getRewardsForGivenPeriod)**
   - Calculates the total and month-wise reward points for a user within a **specified time period**.
   - **Query Parameters:** `userId`, `startDate`, `endDate`

## Service Layer
- The **`RewardsServiceImpl`** class contains the business logic for reward calculations.
- It interacts with the database through the **`RewardsRepository`**.

## Global Exception Handling
- The application includes **global exception handling** to catch runtime exceptions and return **custom error responses**.

## Testing
- **Unit Testing:**
  - Tests the **service layer** using mocks for the repository layer.
- **Integration Testing:**
  - Tests the **end-to-end functionality** of the application.

## Getting Started
### Prerequisites
- Java 21 or later
- Maven

### Running the Application
1. Clone the repository:
   ```sh
   git clone https://github.com/your-repo/spring-boot-rewards.git
   cd spring-boot-rewards
   ```
2. Build the project:
   ```sh
   mvn clean install
   ```
3. Run the application:
   ```sh
   mvn spring-boot:run
   ```
4. Access H2 Database Console:
   - URL: `http://localhost:8080/h2-console`
   - JDBC URL: `jdbc:h2:mem:testdb`
   - Username: `sa`
   - Password: (leave blank)

### API Testing
- You can test the API using **Postman** or **cURL**.
- Example request:
   ```sh
   curl -X GET "http://localhost:8080/lifetime?userId=1"
   ```