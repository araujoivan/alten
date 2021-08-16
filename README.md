# Alten test

## Initial considerations:

This application was divided into two micro-services in order to make it more scalable, as explained in the following section **5) Relevant application architecture decisions**. Some unit tests were performed using the junit and mockito libraries, but it is not 100% covered

### 1) Micro-service booking-api

The booking-api micro-service is configured to the default address http://localhost:8082 and it contains:

  * A swagger that can be accessed at the address: http://localhost:8082/swagger-ui.html
  * A H2 database console which can be accessed at the address: http://localhost:8082/h2-console
  
  Use the following credentials if you want to access the database:
  ```
	JDBC URL:  jdbc:h2:file:../db/cancun
	User Name: sa
  ```
  Password can be left in blank.
  
  Both applications connect to the same H2 database instance. The generated database files are stored in a folder called **db/** which is created one level above these applications
  
  ### 2) Micro-service listing-api
  
  The listing-api micro-service is configured to the default address http://localhost:8083 and contains:
   * A swagger that can be accessed at address: http://localhost:8083/swagger-ui.html
   
  ### 3) Bulding and running the application
  
   Before trying to build and run the application locally it is necessary to make sure that your environment has the following system requirements:
   
   * JDK 11 or above
   * JAVA_HOME variable correctly set to the jdk folder
   * maven installed
   
   The application can be built and run by executing the **build_and_run.sh** script
   
   This script performs the following actions sequentially:
   
    - Check if the JAVA_HOME environment variable is set
    - Deletes the apps/ folder and the db/ folder if they were already created in a previous run
    - Recreate /db and /apps folders
    - Build the building-api and copy the .jar artifact into the apps folder
    - Build the listing-api and copy the .jar artifact into the apps folder
    - Launch the building-api application
    - Launch listing-api application
    - Waits for user [Enter] input in order to finished the tests killing all current java processes
    - Waits for user input [Enter] in order to close the terminal
    
   ### 4) Testing the application
   
   If you choose to use the Postman application to test the endpoints, it is possible to import the collection made available for this purpose **Postman Collection.json**
   
   If you choose to use the swagger, the following json examples might be helpful:
   
   #### 4.1)  Booking API endpoints:
   
   POST  /booking
   
   According to the proposed exercise, there is only one room for reservation whose number is 463. This call books a reservation for room 463.
   
   ```
   {
		"clienId" : 1,
		"roomNumber": 463,
		"period" : {
			"checkIn" : "2021-08-20T12:59:11",
			"checkOut": "2021-08-21T17:59:11"
		}
   }
   ```
   PATCH /booking
   
   This call modifies the checkin and checkout dates of a reservation with code is **907a094e**
   
   ```
   {
		"clienId" : 1,
		"code": "907a094e",
		"period" : {
			"checkIn" : "2021-08-19T12:59:11",
			"checkOut": "2021-08-19T12:59:11"
		}
    }
   ```
   
   GET /booking/907a094e
   
   This call retrieves information from a reservation with code **907a094e**
   
   DELETE /booking/907a094e
   
   This call cancels a reservation with code **907a094e**
   
   #### 4.2)  Listing API endpoints:
   
   GET /room/463

   This call retrieves information about room 463
   
   GET /room/all
   
   This call returns all rooms registered in the system. In the case of the exercise
   proposed will only return room 463
   
   GET /room/period
    
   This call returns a list of unavailable dates for all rooms registered in the system within the date range set out below
   
   ```
    {
      "checkIn": "2021-08-01T00:00:00",
      "checkOut": "2021-08-30T23:59:58"
    }
  ```
   
 ### 5) Relevant application architecture decisions.

#### 5.1) Splitting the application into two

One of the requirements proposed in the exercise is related to the quality of the service that must be between 99.99 to 100% with no downtime. With this requirement in mind, the application was divided into
two apis, considering for this division, the room listing functionality  **listing-api**  and the booking functionality (create, delete, change reservations) being the responsibility of the **booking-api**

I understand that users spend most of their time consuming the listing service than it does booking, and many end up not actually booking.
With this division I think it is easier to scale the listing-api separately.

#### 5.2) Making the creation of validation rules more flexible
Another relevant architectural decision is related to creating business rules that validate the booking periods, such as length of stay, days of advance booking, etc...
To comply with these rules, the design pattern called Decorator was adopted. This design pattern makes the dynamic creation of new rules that run sequentially, validating the stay period
stipulated by the user. For each rule violation, a message is added to an internal list, which can be displayed to the user at the end of all validatios.

The configuration of these rules is done simply and through a chain as shown below:

The execution will take place from RuleOne until reaching the final RuleFour.

```
Rule rule = new RuleOne(new RuleTwo(new RuleThree(new RuleFour())));

```
 
