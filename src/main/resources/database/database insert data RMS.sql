show databases;

use RMS;


show tables;


use RMS;


-- Inserting data into Employees table
INSERT INTO Employees (EmployeeID, Name, Position, Phone, Email, HireDate)
VALUES
    ('E000', 'Charuka', 'Admin', '0716968190', 'c44073838@gmail.com', '2020-01-01'),
    ('E001', 'Kamesh Nethsara', 'Manager', '0771399828', 'kndesilva317@gmail.com', '2022-01-01'),
    ('E002', 'Jane Smith', 'Cashier', '123-456-7891', 'janesmith@restaurant.com', '2022-03-01'),
    ('E003', 'Nisal Sahansith', 'Manager', '0759535651', 'nisalsahansith@gmail.com', '2024-11-11'),
    ('E004', 'Emily Davis', 'Chef', '123-456-7893', 'emilydavis@restaurant.com', '2021-02-01'),
    ('E005', 'Michael Green', 'Chef', '123-456-7894', 'michaelgreen@restaurant.com', '2021-06-10'),
    ('E006', 'Anna Lee', 'Waiter', '123-456-7895', 'annalee@restaurant.com', '2021-07-12'),
    ('E007', 'Sasindu ', 'Cashier', '0759535652', 'sasindu@gmail.com', '2024-11-11');

-- Inserting data into Users table
INSERT INTO Users (UserID, Username, Password, LoginTime, EmployeeID)
VALUES
    ('U001', 'Kamesh_manager', 'password123', '2024-09-16 08:00:00', 'E001'),
    ('U002', 'cashier_jane', 'password456', '2024-09-16 09:00:00', 'E002'),
    ('U003', 'cashier_james', 'password789', '2024-09-16 09:30:00', 'E003');

INSERT INTO Users (UserID, Username, Password, LoginTime, EmployeeID)VALUES ('U004', 'u', 'p', '2024-09-16 08:00:00', 'E001');
INSERT INTO Users (UserID, Username, Password, LoginTime, EmployeeID)VALUES ('U005', 'uu', 'pp', '2024-09-16 08:00:00', 'E002');
INSERT INTO Users (UserID, Username, Password, LoginTime, EmployeeID)VALUES ('U000', 'user', 'password', '2024-09-16 08:00:00', 'E000');
INSERT INTO Users (UserID, Username, Password, LoginTime, EmployeeID)VALUES ('U006', 'nisal', 'sahansith', '2024-11-12 08:00:00', 'E003');


-- Inserting data into Customers table
INSERT INTO Customers (CustomerID, Name, Phone, Email, Address, UserID)
VALUES
    ('C001', 'Alice Cooper', '0771112222', 'alice@example.com', '123 Maple St', 'U001'),
    ('C002', 'Bob Martin', '0713334444', 'bob@example.com', '456 Oak St', 'U001'),
    ('C003', 'Cathy Taylor', '0755556666', 'cathy@example.com', '789 Pine St', 'U002'),
    ('C004', 'David Clark', '0767778888', 'david@example.com', '135 Cedar St', 'U002'),
    ('C005', 'Eve Lewis', '0789991010', 'eve@example.com', '246 Birch St', 'U003'),
    ('C006', 'Frank Hall', '0701211313', 'frank@example.com', '357 Elm St', 'U003'),
    ('C007', 'Grace Young', '0741411515', 'grace@example.com', '468 Poplar St', 'U003'),
    ('C008', 'Hank Wright', '0711611717', 'hank@example.com', '579 Fir St', 'U002'),
    ('C009', 'Ivy King', '0721811919', 'ivy@example.com', '680 Redwood St', 'U003'),
    ('C010', 'Jack Scott', '0772022121', 'jack@example.com', '791 Willow St', 'U001');


-- Inserting data into Suppliers table
INSERT INTO Suppliers (SupplierID, Name, ContactPerson, Phone, Email, Address, UserID)
VALUES
    ('S001', 'Fresh Farms', 'Laura Adams', '777-111-2222', 'laura@freshfarms.com', 'Farm Road, City', 'U001'),
    ('S002', 'Baker Supply Co.', 'Tom Baker', '777-333-4444', 'tom@bakersupply.com', 'Baker Street, City', 'U002'),
    ('S003', 'Meat Masters', 'Sam Butcher', '777-555-6666', 'sam@meatmasters.com', 'Steak Avenue, City', 'U003'),
    ('S004', 'Organic Greens', 'Linda Green', '777-777-8888', 'linda@organicgreens.com', 'Green Street, City', 'U001'),
    ('S005', 'Dairy Delight', 'David Cream', '777-999-1010', 'david@dairydelight.com', 'Milk Lane, City', 'U002'),
    ('S006', 'Seafood Harbor', 'Nancy Ocean', '777-121-1313', 'nancy@seafoodharbor.com', 'Ocean Road, City', 'U003'),
    ('S007', 'Spices World', 'Michael Spice', '777-141-1515', 'michael@spicesworld.com', 'Spice Alley, City', 'U002'),
    ('S008', 'Golden Grains', 'Paul Wheat', '777-161-1717', 'paul@goldengrains.com', 'Wheat Road, City', 'U003'),
    ('S009', 'Juicy Fruits', 'Carol Berry', '777-181-1919', 'carol@juicyfruits.com', 'Berry Boulevard, City', 'U001'),
    ('S010', 'Perfect Poultry', 'Henry Chicken', '777-202-2121', 'henry@perfectpoultry.com', 'Chicken Lane, City', 'U003');

-- Inserting data into InventoryItems table
INSERT INTO InventoryItems (InventoryItemID, Name, Description, Quantity, Unit)
VALUES
    ('I001', 'Tomatoes', 'Fresh organic tomatoes', 100, 'Kg'),
    ('I002', 'Flour', 'All-purpose flour', 100, 'Kg'),
    ('I003', 'Beef', 'Grass-fed beef', 100, 'Kg'),
    ('I004', 'Chicken', 'Free-range chicken', 100, 'Kg'),
    ('I005', 'Sugar', 'Granulated white sugar', 100, 'Kg'),
    ('I006', 'Salt', 'Sea salt', 100, 'Kg'),
    ('I007', 'Olive Oil', 'Extra virgin olive oil', 100, 'Liters'),
    ('I008', 'Butter', 'Unsalted butter', 100, 'Kg'),
    ('I009', 'Garlic', 'Fresh garlic cloves', 100, 'Kg'),
    ('I010', 'Basil', 'Fresh basil leaves', 100, 'Kg');

INSERT INTO Purchases (PurchaseID, SupplierID, TotalAmount, PurchaseDate)
VALUES
    ('P001', 'S001', 500.00, '2024-09-10'),
    ('P002', 'S002', 300.00, '2024-09-11'),
    ('P003', 'S003', 200.00, '2024-09-12'),
    ('P004', 'S004', 700.00, '2024-09-13'),
    ('P005', 'S005', 150.00, '2024-09-14');


INSERT INTO PurchaseItems (InventoryItemID, PurchaseID, Unit, UnitPrice, UnitsBought, Status)
VALUES
    ('I001', 'P001', '1', 2.50, 50, 'Received'),
    ('I002', 'P002', '1', 3.00, 30, 'Received'),
    ('I003', 'P003', '1', 0.10, 100, 'Pending'),
    ('I004', 'P004', '1', 5.00, 20, 'Received'),
    ('I005', 'P005', '1', 1.50, 40, 'Received');

-- Inserting data into MenuItems table   -- i  have to put the available qty to this table
INSERT INTO MenuItems (MenuItemID, Name, Description, Price, Category, KitchenSection)
VALUES
    ('M001', 'Margherita Pizza', 'Classic pizza with fresh tomatoes and basil', 12.99, 'Pizza', 'Main Kitchen'),
    ('M002', 'Pepperoni Pizza', 'Spicy pepperoni with mozzarella', 14.99, 'Pizza', 'Main Kitchen'),
    ('M003', 'Cheeseburger', 'Juicy beef patty with cheddar cheese', 10.99, 'Burger', 'Grill Section'),
    ('M004', 'Caesar Salad', 'Crispy romaine with Caesar dressing', 8.99, 'Salad', 'Salad Station'),
    ('M005', 'Chicken Alfredo', 'Creamy pasta with grilled chicken', 13.99, 'Pasta', 'Main Kitchen'),
    ('M006', 'Garlic Bread', 'Toasted bread with garlic and butter', 4.99, 'Side', 'Main Kitchen'),
    ('M007', 'Grilled Chicken Sandwich', 'Chicken breast with lettuce and tomato', 9.99, 'Sandwich', 'Grill Section'),
    ('M008', 'French Fries', 'Crispy golden fries', 3.99, 'Side', 'Fryer Section'),
    ('M009', 'Tiramisu', 'Classic Italian dessert', 6.99, 'Dessert', 'Pastry Kitchen'),
    ('M010', 'Iced Tea', 'Freshly brewed iced tea', 2.99, 'Beverage', 'Beverage Station');

-- Inserting data into MenuItemIngredients table (Recipe)
INSERT INTO MenuItemIngredients (MenuItemID, InventoryItemID, QuantityNeeded)
VALUES
    ('M001', 'I001', 0.5),  -- Margherita Pizza needs 0.5 Kg of Tomatoes
    ('M001', 'I010', 0.1),  -- Margherita Pizza needs 0.1 Kg of Basil
    ('M002', 'I001', 0.3),  -- Pepperoni Pizza needs 0.3 Kg of Tomatoes
    ('M002', 'I003', 0.4),  -- Pepperoni Pizza needs 0.4 Kg of Beef
    ('M003', 'I003', 0.2),  -- Cheeseburger needs 0.2 Kg of Beef
    ('M004', 'I009', 0.05), -- Caesar Salad needs 0.05 Kg of Garlic
    ('M005', 'I004', 0.25), -- Chicken Alfredo needs 0.25 Kg of Chicken
    ('M006', 'I009', 0.1),  -- Garlic Bread needs 0.1 Kg of Garlic
    ('M007', 'I004', 0.2),  -- Grilled Chicken Sandwich needs 0.2 Kg of Chicken
    ('M008', 'I005', 0.3);  -- French Fries need 0.3 Kg of Potatoes (assuming potatoes as inventory item)

-- Inserting data into Reservations table
INSERT INTO Reservations (ReservationID, CustomerID, ReservationDate, NumberOfGuests, SpecialRequests, Status)
VALUES
    ('R001', 'C001', '2024-09-17', 4, 'Window seat', 'Confirmed'),
    ('R002', 'C002', '2024-09-18', 2, 'Vegan menu', 'Pending'),
    ('R003', 'C003', '2024-09-19', 6, NULL, 'Confirmed'),
    ('R004', 'C004', '2024-09-20', 3, NULL, 'Cancelled'),
    ('R005', 'C005', '2024-09-21', 2, 'Anniversary', 'Confirmed'),
    ('R006', 'C006', '2024-09-22', 5, NULL, 'Pending'),
    ('R007', 'C007', '2024-09-23', 4, 'Birthday cake', 'Confirmed'),
    ('R008', 'C008', '2024-09-24', 2, NULL, 'Confirmed'),
    ('R009', 'C009', '2024-09-25', 6, NULL, 'Cancelled'),
    ('R010', 'C010', '2024-09-26', 4, 'Gluten-free menu', 'Confirmed');

-- Inserting data into Tables table
INSERT INTO Tables (TableID, TableNumber, Capacity, Location, Status)
VALUES
    ('T001', 1, 4, 'Main Floor', 'Available'),
    ('T002', 2, 2, 'Main Floor', 'Occupied'),
    ('T003', 3, 6, 'Main Floor', 'Reserved'),
    ('T004', 4, 4, 'Patio', 'Available'),
    ('T005', 5, 2, 'Patio', 'Available'),
    ('T006', 6, 6, 'Main Floor', 'Occupied'),
    ('T007', 7, 4, 'Main Floor', 'Reserved'),
    ('T008', 8, 2, 'Main Floor', 'Occupied'),
    ('T009', 9, 6, 'Patio', 'Available'),
    ('T010', 10, 4, 'Patio', 'Reserved');

-- Inserting data into TableAssignments table
INSERT INTO TableAssignments (TableID, ReservationID, AssignmentTime)
VALUES
    ('T003', 'R001', '2024-09-17 18:00:00'),
    ('T002', 'R002', '2024-09-18 19:00:00'),
    ('T007', 'R003', '2024-09-19 20:00:00'),
    ('T010', 'R004', '2024-09-20 21:00:00'),
    ('T005', 'R005', '2024-09-21 18:00:00'),
    ('T009', 'R006', '2024-09-22 19:00:00'),
    ('T001', 'R007', '2024-09-23 20:00:00'),
    ('T008', 'R008', '2024-09-24 21:00:00'),
    ('T004', 'R009', '2024-09-25 18:00:00'),
    ('T006', 'R010', '2024-09-26 19:00:00');

-- Inserting data into Payments table
INSERT INTO Payments (PaymentID, PaymentMethod, Amount, PaymentDate)
VALUES
    ('P001', 'Credit Card', 50.75, '2024-09-17'),
    ('P002', 'Cash', 30.25, '2024-09-18'),
    ('P003', 'Credit Card', 90.50, '2024-09-19'),
    ('P004', 'Credit Card', 45.00, '2024-09-20'),
    ('P005', 'Debit Card', 60.99, '2024-09-21'),
    ('P006', 'Credit Card', 75.25, '2024-09-22'),
    ('P007', 'Cash', 35.00, '2024-09-23'),
    ('P008', 'Credit Card', 25.75, '2024-09-24'),
    ('P009', 'Debit Card', 40.50, '2024-09-25'),
    ('P010', 'Cash', 50.99, '2024-09-26');

-- Inserting data into Orders table
INSERT INTO Orders (OrderID, CustomerID, UserID, OrderDate, TotalAmount, Status, OrderType, ReservationID, PaymentID)
VALUES
    ('O001', 'C001', 'U001', '2024-09-17', 50.75, 'Completed', 'Dine-In', 'R001', 'P001'),
    ('O002', NULL, 'U002', '2024-09-18', 30.25, 'Completed', 'Takeaway', NULL, 'P002'),
    ('O003', 'C003', 'U003', '2024-09-19', 90.50, 'Pending', 'Dine-In', 'R003', 'P003'),
    ('O004', 'C004', 'U001', '2024-09-20', 45.00, 'Cancelled', 'Dine-In', NULL, 'P004'),
    ('O005', 'C005', 'U002', '2024-09-21', 60.99, 'Completed', 'Dine-In', 'R005', 'P005'),
    ('O006', NULL, 'U003', '2024-09-22', 75.25, 'Pending', 'Delivery', NULL, 'P006'),
    ('O007', 'C007', 'U001', '2024-09-23', 35.00, 'Completed', 'Dine-In', 'R007', 'P007'),
    ('O008', NULL, 'U002', '2024-09-24', 25.75, 'Completed', 'Takeaway', NULL, 'P008'),
    ('O009', 'C009', 'U003', '2024-09-25', 40.50, 'Cancelled', 'Dine-In', 'R009', 'P009'),
    ('O010', NULL, 'U001', '2024-09-26', 50.99, 'Completed', 'Delivery', NULL, 'P010');

-- Inserting data into OrderItems table
INSERT INTO OrderItems (OrderID, MenuItemID, Quantity, Price)
VALUES
    ('O001', 'M001', 2, 25.98),
    ('O001', 'M004', 1, 8.99),
    ('O002', 'M003', 1, 10.99),
    ('O002', 'M006', 1, 4.99),
    ('O003', 'M005', 2, 27.98),
    ('O003', 'M008', 1, 3.99),
    ('O004', 'M002', 1, 14.99),
    ('O005', 'M007', 1, 9.99),
    ('O006', 'M009', 2, 13.98),
    ('O007', 'M001', 3, 38.97);

-- Inserting data into Deliveries table
INSERT INTO Deliveries (DeliveryID, OrderID, DeliveryDate, DeliveryStatus, DeliveryAddress)
VALUES
    ('D001', 'O006', '2024-09-22', 'Out for Delivery', '246 Birch St'),
    ('D002', 'O010', '2024-09-26', 'Delivered', '791 Willow St');

-- Inserting data into Reviews table
INSERT INTO Reviews (ReviewID, CustomerID, MenuItemID, Rating, Comments, ReviewDate)
VALUES
    ('RV001', 'C001', 'M001', 5, 'Delicious!', '2024-09-17'),
    ('RV002', 'C002', 'M003', 4, 'Good burger, but a bit salty', '2024-09-18'),
    ('RV003', 'C003', 'M005', 5, 'Pasta was amazing!', '2024-09-19'),
    ('RV004', 'C004', 'M002', 3, 'Pizza was okay', '2024-09-20'),
    ('RV005', 'C005', 'M007', 4, 'Sandwich was tasty', '2024-09-21'),
    ('RV006', 'C006', 'M009', 5, 'Best dessert ever!', '2024-09-22'),
    ('RV007', 'C007', 'M001', 5, 'My favorite pizza', '2024-09-23'),
    ('RV008', 'C008', 'M004', 4, 'Good pizza, fast service', '2024-09-24'),
    ('RV009', 'C009', 'M008', 3, 'Fries were cold', '2024-09-25'),
    ('RV010', 'C010', 'M005', 4, 'Great pasta, will order again', '2024-09-26');



--  set the starting date and time to the program
INSERT INTO Reminder (ReminderID, StartingDate, Duration)
VALUES ('RT001', '2024-11-12 15:23:00', 7200);  -- 1440 minutes = 1 day

USE RMS;


UPDATE Reminder SET StartingDate = '2024-11-12 10:40:00' WHERE ReminderID = 'RT001';
UPDATE Reminder SET Duration = 7200 WHERE ReminderID = 'RT001';

select * from Reminder;
