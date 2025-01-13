DROP DATABASE IF EXISTS RMS;

CREATE DATABASE RMS;

USE RMS;


-- Table: Employees
CREATE TABLE Employees (
                           EmployeeID VARCHAR(50) PRIMARY KEY,
                           Name VARCHAR(100) NOT NULL,
                           Position VARCHAR(50) NOT NULL,
                           Phone VARCHAR(30) NOT NULL,
                           Email VARCHAR(100) NOT NULL,
                           HireDate DATE NOT NULL
);

-- Table: Users
CREATE TABLE Users (
                       UserID VARCHAR(50) PRIMARY KEY,
                       Username VARCHAR(50) NOT NULL,
                       Password VARCHAR(50) NOT NULL,
                       LoginTime DATETIME NOT NULL,
                       EmployeeID VARCHAR(50) NOT NULL,
                       FOREIGN KEY (EmployeeID) REFERENCES Employees(EmployeeID)
                           ON DELETE CASCADE
                           ON UPDATE CASCADE
);

-- Table: Customers
CREATE TABLE Customers (
                           CustomerID VARCHAR(50) PRIMARY KEY,
                           Name VARCHAR(100) NOT NULL,
                           Phone VARCHAR(15) NOT NULL,
                           Email VARCHAR(100) NOT NULL,
                           Address VARCHAR(255) NOT NULL,
                           UserID VARCHAR(50) NOT NULL,
                           FOREIGN KEY (UserID) REFERENCES Users(UserID)
                               ON DELETE CASCADE
                               ON UPDATE CASCADE
);

-- Table: Suppliers
CREATE TABLE Suppliers (
                           SupplierID VARCHAR(50) PRIMARY KEY,
                           Name VARCHAR(100) NOT NULL,
                           ContactPerson VARCHAR(100) NOT NULL,
                           Phone VARCHAR(30) NOT NULL,
                           Email VARCHAR(100) NOT NULL,
                           Address VARCHAR(255) NOT NULL,
                           UserID VARCHAR(50) NOT NULL,
                           FOREIGN KEY (UserID) REFERENCES Users(UserID)
                               ON DELETE CASCADE
                               ON UPDATE CASCADE
);

-- Table: InventoryItems
CREATE TABLE InventoryItems (
                                InventoryItemID VARCHAR(50) PRIMARY KEY,
                                Name VARCHAR(100) NOT NULL,
                                Description VARCHAR(255) NOT NULL,
                                Quantity DECIMAL(10, 2) NOT NULL,
                                UnitsMeasured VARCHAR(50) NOT NULL
);

-- Table: Purchases
CREATE TABLE Purchases (
                           PurchaseID VARCHAR(50) PRIMARY KEY,
                           SupplierID VARCHAR(50) NOT NULL,
                           TotalAmount DECIMAL(10, 2) NOT NULL,
                           PurchaseDate DATE NOT NULL,
                           FOREIGN KEY (SupplierID) REFERENCES Suppliers(SupplierID)
                               ON DELETE CASCADE
                               ON UPDATE CASCADE
);

-- Table: PurchaseItems
CREATE TABLE PurchaseItems (
                               InventoryItemID VARCHAR(50) NOT NULL,
                               PurchaseID VARCHAR(50) NOT NULL,
                               Unit DECIMAL(10, 2) NOT NULL, -- how much is considered as a unit for the price
                               UnitPrice DECIMAL(10, 2) NOT NULL,
                               UnitsBought DECIMAL(10, 2) NOT NULL,
                               Status VARCHAR(50) NOT NULL,
                               FOREIGN KEY (InventoryItemID) REFERENCES InventoryItems(InventoryItemID)
                                   ON DELETE CASCADE
                                   ON UPDATE CASCADE,
                               FOREIGN KEY (PurchaseID) REFERENCES Purchases(PurchaseID)
                                   ON DELETE CASCADE
                                   ON UPDATE CASCADE
);

-- Table: MenuItems
CREATE TABLE MenuItems (
                           MenuItemID VARCHAR(50) PRIMARY KEY,
                           Name VARCHAR(100) NOT NULL,
                           Description VARCHAR(255) NOT NULL,
                           Price DECIMAL(10, 2) NOT NULL,
                           Category VARCHAR(50) NOT NULL,
                           KitchenSection VARCHAR(50) NOT NULL
);

-- Table: MenuItemIngredients (Recipe)
CREATE TABLE MenuItemIngredients (
                                     MenuItemID VARCHAR(50) NOT NULL,
                                     InventoryItemID VARCHAR(50) NOT NULL,
                                     QuantityNeeded DECIMAL(10, 2) NOT NULL, -- Quantity of the inventory item needed for one menu item
                                     FOREIGN KEY (MenuItemID) REFERENCES MenuItems(MenuItemID)
                                         ON DELETE CASCADE
                                         ON UPDATE CASCADE,
                                     FOREIGN KEY (InventoryItemID) REFERENCES InventoryItems(InventoryItemID)
                                         ON DELETE CASCADE
                                         ON UPDATE CASCADE
);

-- Table: Reservations
CREATE TABLE Reservations (
                              ReservationID VARCHAR(50) PRIMARY KEY,
                              CustomerID VARCHAR(50) NOT NULL,
                              ReservationDate DATE NOT NULL,
                              NumberOfGuests INT NOT NULL,
                              SpecialRequests VARCHAR(255) NULL,
                              Status VARCHAR(50) NOT NULL,
                              FOREIGN KEY (CustomerID) REFERENCES Customers(CustomerID)
                                  ON DELETE CASCADE
                                  ON UPDATE CASCADE
);

-- Table: Tables
CREATE TABLE Tables (
                        TableID VARCHAR(50) PRIMARY KEY,
                        TableNumber INT NOT NULL,
                        Capacity INT NOT NULL,
                        Location VARCHAR(50) NOT NULL,
                        Status VARCHAR(50) NOT NULL
);

-- Table: TableAssignments
CREATE TABLE TableAssignments (
                                  TableID VARCHAR(50) NOT NULL,
                                  ReservationID VARCHAR(50) NOT NULL,
                                  AssignmentTime DATETIME NOT NULL,
                                  FOREIGN KEY (TableID) REFERENCES Tables(TableID)
                                      ON DELETE CASCADE
                                      ON UPDATE CASCADE,
                                  FOREIGN KEY (ReservationID) REFERENCES Reservations(ReservationID)
                                      ON DELETE CASCADE
                                      ON UPDATE CASCADE
);

-- Table: Payments
CREATE TABLE Payments (
                          PaymentID VARCHAR(50) PRIMARY KEY,
                          PaymentMethod VARCHAR(50) NOT NULL,
                          Amount DECIMAL(10, 2) NOT NULL,
                          PaymentDate DATE NOT NULL
);

-- Table: Orders
CREATE TABLE Orders (
                        OrderID VARCHAR(50) PRIMARY KEY,
                        CustomerID VARCHAR(50) NULL,-- Optional field
                        UserID VARCHAR(50) NOT NULL,
                        OrderDate DATE NOT NULL,
                        TotalAmount DECIMAL(10, 2) NOT NULL,
                        Status VARCHAR(50) NOT NULL,
                        OrderType VARCHAR(50) NOT NULL,
                        ReservationID VARCHAR(50) NULL,-- Optional field
                        PaymentID VARCHAR(50) NOT NULL,
                        FOREIGN KEY (CustomerID) REFERENCES Customers(CustomerID)
                            ON DELETE CASCADE
                            ON UPDATE CASCADE,
                        FOREIGN KEY (UserID) REFERENCES Users(UserID)
                            ON DELETE CASCADE
                            ON UPDATE CASCADE,
                        FOREIGN KEY (ReservationID) REFERENCES Reservations(ReservationID)
                            ON DELETE SET NULL
                            ON UPDATE CASCADE, -- If a reservation is deleted, set this to NULL
                        FOREIGN KEY (PaymentID) REFERENCES Payments(PaymentID)
                            ON DELETE CASCADE
                            ON UPDATE CASCADE
);

-- Table: OrderItems
CREATE TABLE OrderItems (
                            OrderID VARCHAR(50) NOT NULL,
                            MenuItemID VARCHAR(50) NOT NULL,
                            Quantity INT NOT NULL,
                            Price DECIMAL(10, 2) NOT NULL,
                            FOREIGN KEY (OrderID) REFERENCES Orders(OrderID)
                                ON DELETE CASCADE
                                ON UPDATE CASCADE,
                            FOREIGN KEY (MenuItemID) REFERENCES MenuItems(MenuItemID)
                                ON DELETE CASCADE
                                ON UPDATE CASCADE
);


-- Table: Deliveries
CREATE TABLE Deliveries (
                            DeliveryID VARCHAR(50) PRIMARY KEY,
                            OrderID VARCHAR(50) NOT NULL,
                            DeliveryDate DATE NOT NULL,
                            DeliveryStatus VARCHAR(50) NOT NULL,
                            DeliveryAddress VARCHAR(255) NOT NULL,
                            FOREIGN KEY (OrderID) REFERENCES Orders(OrderID)
                                ON DELETE CASCADE
                                ON UPDATE CASCADE
);

-- Table: Reviews
CREATE TABLE Reviews (
                         ReviewID VARCHAR(50) PRIMARY KEY,
                         CustomerID VARCHAR(50) NOT NULL,
                         MenuItemID VARCHAR(50) NOT NULL,
                         Rating INT NOT NULL,
                         Comments VARCHAR(255) NULL,
                         ReviewDate DATE NOT NULL,
                         FOREIGN KEY (CustomerID) REFERENCES Customers(CustomerID)
                             ON DELETE CASCADE
                             ON UPDATE CASCADE,
                         FOREIGN KEY (MenuItemID) REFERENCES MenuItems(MenuItemID)
                             ON DELETE CASCADE
                             ON UPDATE CASCADE
);

-- store time and date to remind

CREATE TABLE IF NOT EXISTS Reminder (
                                        ReminderID VARCHAR(50) PRIMARY KEY,   --  primary key
                                        StartingDate DATETIME NOT NULL,   -- Stores the exact date and time when the program starts
                                        Duration INTEGER NOT NULL         -- Duration in minutes (or any unit you prefer)
);




