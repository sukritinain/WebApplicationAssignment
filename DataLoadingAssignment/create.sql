CREATE DATABASE IF NOT EXISTS ad;
USE ad;

CREATE TABLE EbayData(
	ItemID INT PRIMARY KEY, 
	Name VARCHAR(255), 
	Currently DECIMAL(8,2), 
	Buy_Price DECIMAL(8,2), 
	First_Bid DECIMAL(8,2), 
	Number_of_Bids INT, 
	Country VARCHAR(255), 
	Started TIMESTAMP, 
	Ends TIMESTAMP, 
	Location VARCHAR(255), 
	Description VARCHAR(4000) 
);

CREATE TABLE Category(
	ItemID INT PRIMARY KEY, 
	CategoryName VARCHAR(255)
);


CREATE TABLE Bidder(
	ItemID INT, 
	UserID VARCHAR(255) PRIMARY KEY, 
	Rating INT NOT NULL, 
	Country VARCHAR(255),  

);

CREATE TABLE BidAmount(
	ItemID INT, 
	UserID VARCHAR(255) PRIMARY KEY, 
	Amount  DECIMAL(8,2), 

);

CREATE TABLE BidLocation(
	ItemID INT, 
	UserID VARCHAR(255) PRIMARY KEY, 
	Location VARCHAR(255), 

);

CREATE TABLE BidTime(
	ItemID INT, 
	Time TIMESTAMP, 
	UserID VARCHAR(255) PRIMARY KEY, 

);

CREATE TABLE Location(
	ItemID INT PRIMARY KEY, 
	Latitude VARCHAR(255), 
	Longitude VARCHAR(255), 

);

CREATE TABLE Seller(
	ItemID int, 
	UserID VARCHAR(255) PRIMARY KEY, 
	Rating INT NOT NULL, 

);
