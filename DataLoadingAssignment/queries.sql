1. 	SELECT Count(UserID) 
 	FROM Bidder;

2. 	SELECT Count(ItemID) 
	FROM EbayData 
	WHERE Country = “New York”;

3. 	SELECT Count(*) 
	FROM(	SELECT ItemID 
			FROM Category 
			GROUP BY ItemID 
			HAVING COUNT(CategoryName) = 4
		);

4. 	SELECT BidAmount.ItemID 
	FROM BidAmount 
	Inner JOIN EbayData 
	ON BidAmount.ItemID = EbayData.ItemID 
	WHERE EbayData.Ends > ’2012-12-20 00:00:01’ AND BidAmount.Amount = (
													SELECT Max(Amount) 
													FROM BidAmount
													);

5. 	SELECT count(DISTINCT Bidder.UserID) 
	FROM Bidder, Seller
	WHERE Bidder.UserID = Seller.UserID and Rating > 1000;

6. 	SELECT count(DISTINCT Seller.UserID) 
	FROM Seller, Bidder 
	WHERE WHERE Bidder.UserID = Seller.UserID; 

7. 	SELECT count(DISTINCT CategoryName)
	FROM Category
	WHERE ItemID IN (
					SELECT ItemID 
					FROM BidAmount 
					WHERE Amount > 100.00
					);
