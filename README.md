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
The application provides one REST endpoint which calculates the rewards based on value of `period` request parameter:

1. **`GET /calculateRewards` (period=LIFETIME)**
   - Calculates the total and month-wise reward points for a user over their **entire lifetime**.
   - **Query Parameter:** `userId`, `period`

2. **`GET /calculateRewards` (period=LASTTHREEMONTHS)**
   - Calculates the total and month-wise reward points for a user over the **last three months**.
   - **Query Parameter:** `userId`, `period`

3. **`GET /calculateRewards` (period=CUSTOMIZE)**
   - Calculates the total and month-wise reward points for a user within a **specified time period**.
   - **Query Parameters:** `userId`, `period`, `startDate`, `endDate`

## Service Layer
- The **`RewardsServiceImpl`** class contains the business logic for reward calculations.
- It interacts with the database through the **`RewardsRepository`**.

## Global Exception Handling
- The application includes **global exception handling** to catch runtime exceptions and return **custom error responses**.

## Testing
- **Unit Testing:**
    - Tests the **controller layer** using mocks for the service layer.
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
   git clone https://github.com/mayuresh2511/RewardsProgramProject.git
   cd RewardsProgramProject
   ```
2. Build the project:
   ```sh
   mvn clean install
   ```
3. Run the application:
   ```sh
   mvn spring-boot:run
   ```
   OR
   ```sh
   docker build -t spring-boot-rewards .
   ```
   ```sh
   ddocker run -d -p 8080:8080 --name rewards-project spring-boot-rewards
   ```
4. Access H2 Database Console:
   - URL: `http://localhost:8080/h2-console`
   - JDBC URL: `jdbc:h2:mem:testdb`
   - Username: `sa`
   - Password: (leave blank)

### API Testing
- You can test the API using **Postman** or **cURL**.
  - Example request GET /calculateRewards:
     ```sh
     curl -X GET "http://localhost:8080/rewards/api/calculateRewards?userId=Mayuresh&period=LIFETIME"
     ```
- Example request GET /calculateRewards:
   ```sh
   curl -X GET "http://localhost:8080/rewards/api/calculateRewards?userId=Mayuresh&period=LASTTHREEMONTHS"
   ```
- Example request GET /calculateRewards:
   ```sh
   curl -X GET "http://localhost:8080/rewards/api/calculateRewards?userId=Mayuresh&period=CUSTOMIZE&startDate=2025-01-01&endDate=2025-02-28"
   ```
     ```json
      {
          "userId": "Mayuresh",
          "monthWiseRewards": {
              "JANUARY-2025": 1650,
              "FEBRUARY-2025": 210
          },
          "totalRewards": 1860,
          "transactions": [
              {
              "transactionId": 1,
              "tranAmt": 100.00,
              "tranDate": "2025-01-22"
              },
              {
              "transactionId": 12,
              "tranAmt": 500.00,
              "tranDate": "2025-01-19"
              },
              {
              "transactionId": 19,
              "tranAmt": 450.00,
              "tranDate": "2025-01-30"
              },
              {
              "transactionId": 5,
              "tranAmt": 60.00,
              "tranDate": "2025-02-04"
              },
              {
              "transactionId": 11,
              "tranAmt": 175.00,
              "tranDate": "2025-02-20"
              }
          ],
          "rewardsAsOn": "2025-02-24"
      }
     ```