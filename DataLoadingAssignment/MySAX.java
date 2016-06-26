import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

public class MySAX extends DefaultHandler {
	private List<EbayData> ebayDataList = new ArrayList<EbayData>();
	private List<Category> CategoryList = new ArrayList<Category>();
	private List<Bidder> BidderList = new ArrayList<Bidder>();
	private List<BidLocation> BidLocationList = new ArrayList<BidLocation>();
	private List<BidTime> BidTimeList = new ArrayList<BidTime>();
	private List<BidAmount> BidAmountList = new ArrayList<BidAmount>();
	private List<Location> LocationList = new ArrayList<Location>();
	private List<Seller> SellerList = new ArrayList<Seller>();
	private int itemId;
	private Stack<String> elementStack = new Stack<String>();
	private Stack<Object> objectStack = new Stack<Object>();

	public static void main(String args[]) throws Exception {
		XMLReader xr = XMLReaderFactory.createXMLReader();
		MySAX handler = new MySAX();
		xr.setContentHandler(handler);
		xr.setErrorHandler(handler);

		for (int i = 0; i < args.length; i++) {
	    	FileReader r = new FileReader(args[i]);
	    	xr.parse(new InputSource(r));
		}

		// System.out.println("size:" + handler.ebayDataList.size());
		for (EbayData ebayData : handler.ebayDataList) {
			System.out.println(ebayData.getItemID());
			System.out.println(ebayData.getCurrently());

		}

		/*
		 * System.out.println("category size:" + handler.CategoryList.size());
		 * for (Category category:handler.CategoryList) {
		 * //System.out.println(category.getItemID());
		 * System.out.println(category.getCategoryName());
		 *
		 * }
		 *
		 * /*System.out.println("size:" + handler.BidderList.size()); for
		 * (Bidder bidder:handler.BidderList) {
		 * System.out.println(bidder.getItemID());
		 * System.out.println(bidder.getRating());
		 * System.out.println(bidder.getUserID());
		 * System.out.println(bidder.getCountry()); }
		 */

		/*
		 * System.out.println("bidlocation size:" +
		 * handler.BidLocationList.size()); for (BidLocation
		 * bidlocation:handler.BidLocationList) {
		 * System.out.println(bidlocation.getItemID());
		 * System.out.println(bidlocation.getLocation()); }
		 */

		/*
		 * System.out.println("size:" + handler.BidTimeList.size()); for
		 * (BidTime bidtime:handler.BidTimeList) {
		 * System.out.println(bidtime.getItemID());
		 * System.out.println(bidtime.getTime()); }
		 */

		/*
		 * System.out.println("size:" + handler.BidAmountList.size()); for
		 * (BidAmount bidamount:handler.BidAmountList) {
		 * System.out.println(bidamount.getItemID());
		 * System.out.println(bidamount.getAmount());
		 *
		 * }
		 */

		/*
		 * System.out.println("size:" + handler.LocationList.size()); for
		 * (Location location:handler.LocationList) {
		 * System.out.println(location.getItemID());
		 * System.out.println(location.getLatitude());
		 * System.out.println(location.getLongitude()); }
		 */

		/*
		 * System.out.println("size:" + handler.SellerList.size()); for (Seller
		 * seller:handler.SellerList) { System.out.println(seller.getItemID());
		 * System.out.println(seller.getRating());
		 * System.out.println(seller.getUserID()); }
		 */

		EbayDataCSVWriter.generateCsvFile(
				"ebayData.csv", handler.ebayDataList);
		CategoryCSVWriter.generateCsvFile(
				"category.csv", handler.CategoryList);
		BidAmountCSVWriter.generateCsvFile(
				"bidamount.csv",
				handler.BidAmountList);
		BidderCSVWriter.generateCsvFile("bidder.csv",
				handler.BidderList);
		BidLocationCSVWriter.generateCsvFile(
				"bidlocation.csv",
				handler.BidLocationList);
		BidTimeCSVWriter.generateCsvFile(
				"bidtime.csv", handler.BidTimeList);
		LocationCSVWriter.generateCsvFile(
				"location.csv", handler.LocationList);
		SellerCSVWriter.generateCsvFile("seller.csv",
				handler.SellerList);
	}

	public MySAX() {
		super();
	}

	static String strip(String money) {
		if (money.equals(""))
			return money;
		else {
			double am = 8.2;
			NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
			try {
				am = nf.parse(money).doubleValue();
			} catch (ParseException e) {
				System.exit(20);
			}
			nf.setGroupingUsed(false);
			return nf.format(am).substring(1);
		}
	}

	// //////////////////////////////////////////////////////////////////
	// Event handlers.
	// //////////////////////////////////////////////////////////////////
	public void startDocument() {
		System.out.println("Start document");
	}

	public void endDocument() {
		System.out.println("End document");
	}

	public void startElement(String uri, String name, String qName,
			Attributes atts) {

		this.elementStack.push(qName);

		if (qName.equals("Item")) {
			EbayData ebayData = new EbayData();
			ebayData.setItemID(Integer.valueOf(atts.getValue(0)));
			this.objectStack.add(ebayData);
			ebayDataList.add(ebayData);
			itemId = Integer.valueOf(atts.getValue(0));
		}

		if (qName.equals("Category")) {
			Category category = new Category();
			category.setItemID(itemId);
			this.objectStack.add(category);
			CategoryList.add(category);
		}

		if (qName.equals("Bidder")) {
			Bidder bidder = new Bidder();
			bidder.setUserID(atts.getValue(1));
			bidder.setRating(Integer.valueOf(atts.getValue(0)));
			bidder.setItemID(itemId);
			this.objectStack.add(bidder);
			BidderList.add(bidder);
		}

		if (qName.equals("Location")
				&& (this.objectStack.peek() instanceof Bidder)) {
			BidLocation bidLocation = new BidLocation();
			bidLocation.setItemID(itemId);
			this.objectStack.add(bidLocation);
			BidLocationList.add(bidLocation);
		}

		if (qName.equals("Time")) {
			BidTime bidTime = new BidTime();
			bidTime.setItemID(itemId);
			this.objectStack.add(bidTime);
			BidTimeList.add(bidTime);
		}

		if (qName.equals("Amount")) {
			BidAmount bidAmount = new BidAmount();
			bidAmount.setItemID(itemId);
			this.objectStack.add(bidAmount);
			BidAmountList.add(bidAmount);
		}

		if (qName.equals("Location")
				&& (this.objectStack.peek() instanceof EbayData)) {
			Location location = new Location();
			location.setItemID(itemId);
			location.setLatitude(atts.getValue(0));
			location.setLongitude(atts.getValue(1));
			this.objectStack.add(location);
			LocationList.add(location);
		}

		if (qName.equals("Seller")) {
			Seller seller = new Seller();
			seller.setItemID(itemId);
			seller.setRating(Integer.valueOf(atts.getValue(0)));
			seller.setUserID(atts.getValue(1));
			this.objectStack.add(seller);
			SellerList.add(seller);
		}
	}

	public void endElement(String uri, String name, String qName) {
		this.elementStack.pop();
		if (this.objectStack.size() > 0) {
			Object obj = this.objectStack.peek();
			if (qName.equals("Item")
					|| qName.equals("Category")
					|| qName.equals("Bidder")
					|| (qName.equals("Location") && (obj instanceof BidLocation))
					|| (qName.equals("Location") && (obj instanceof EbayData))
					|| (qName.equals("Location") && (obj instanceof Location))
					|| (qName.equals("Time") && (obj instanceof BidTime))
					|| (qName.equals("Amount") && (obj instanceof BidAmount))
					|| qName.equals("Seller")) {
				this.objectStack.pop();
			}
		}
	}

	public void characters(char ch[], int start, int length) {
		// //System.out.print("Characters:    \"");
		for (int i = start; i < start + length; i++) {
			switch (ch[i]) {
			case '\\':
				// //System.out.print("\\\\");
				break;
			case '"':
				// //System.out.print("\\\"");
				break;
			case '\n':
				// //System.out.print("\\n");
				break;
			case '\r':
				// //System.out.print("\\r");
				break;
			case '\t':
				// //System.out.print("\\t");
				break;
			default:
				// //System.out.print(ch[i]);
				break;
			}
		}

		String value = new String(ch, start, length);
		String currentElement = elementStack.peek();

		if (currentElement.equals("Name")) {
			EbayData ebayData = (EbayData) this.objectStack.peek();
			ebayData.setName(value);
		}

		if (currentElement.equals("Currently")) {
			EbayData ebayData = (EbayData) this.objectStack.peek();
			String currently = strip(value);
			ebayData.setCurrently(currently);
			System.out.println("name value:" + value);
		}

		if (currentElement.equals("Buy_Price")) {
			// System.out.println("name value:"+value);
			EbayData ebayData = (EbayData) this.objectStack.peek();
			String buyprice = strip(value);
			ebayData.setBuy_Price(buyprice);
		}

		if (currentElement.equals("First_Bid")) {
			//System.out.println("name value:" + value);
			EbayData ebayData = (EbayData) this.objectStack.peek();
			String firstbid = strip(value);
			ebayData.setFirst_Bid(firstbid);
		}

		if (currentElement.equals("Number_of_Bids")) {
			System.out.println("name value:" + value);
			EbayData ebayData = (EbayData) this.objectStack.peek();
			ebayData.setNumber_of_Bids(Integer.valueOf(value));
		}

		if (currentElement.equals("Country")) {
			// System.out.println("name value:"+value);
			Object obj = this.objectStack.peek();
			if (obj instanceof EbayData) {
				EbayData ebayData = (EbayData) obj;
				ebayData.setCountry(value);
			} else if (obj instanceof Bidder) {
				Bidder bidder = (Bidder) obj;
				bidder.setCountry(value);
			}
		}
		if (currentElement.equals("UserID")) {
			// System.out.println("name value:"+value);
			Object obj = this.objectStack.peek();
			if (obj instanceof BidAmount) {
				BidAmount bidamount = (BidAmount) obj;
				bidamount.UserID(value);
			} else if (obj instanceof BidTime) {
				BidTime bidtimer = (BidTime) obj;
				bidtimer.setUserID(value);
			}
		}

		if (currentElement.equals("Started")) {
			EbayData ebayData = (EbayData) this.objectStack.peek();
			ebayData.setStarted(value);
		}

		if (currentElement.equals("Ends")) {
			EbayData ebayData = (EbayData) this.objectStack.peek();
			ebayData.setEnds(value);
		}

		if (currentElement.equals("Description")) {
			EbayData ebayData = (EbayData) this.objectStack.peek();
			ebayData.setDescription(value);
		}

		if (currentElement.equals("Category")) {
			Category category = (Category) this.objectStack.peek();
			String categoryname = value.replace("&amp;", "");
			category.setCategoryName(categoryname);
		}

		if (currentElement.equals("Amount")) {
			BidAmount bidamount = (BidAmount) this.objectStack.peek();
			String bidamount1 = strip(value);
			bidamount.setAmount(bidamount1);
		}
		if (currentElement.equals("Time")) {
			BidTime bidtime = (BidTime) this.objectStack.peek();
			bidtime.setTime(value);
		}

		if (currentElement.equals("Location")) {
			Object obj1 = this.objectStack.peek();
			if (obj1 instanceof EbayData) {
				EbayData ebayData = (EbayData) obj1;
				ebayData.setLocation(value);
			} else if (obj1 instanceof BidLocation) {
				BidLocation bidlocation = (BidLocation) obj1;
				bidlocation.setLocation(value);
			}
		}
	}

	// The beginning of the models

	class BidAmount {
		private int ItemID;
		private String Amount;
		private String UserID;

		public int getItemID() {
			return ItemID;
		}

		public void setItemID(int ItemID) {
			this.ItemID = ItemID;
		}

		public String getAmount() {
			return Amount;
		}

		public void setAmount(String Amount) {
			this.Amount = Amount;
		}

		public String UserID() {
			return UserID;
		}

		public void UserID(String UserID) {
			this.UserID = UserID;
		}

		@Override
		public String toString() {
			return this.ItemID + ":" + this.Amount + ":" + this.UserID;
		}

	}

	class Bidder {
		private int ItemID;
		private String UserID;
		private int Rating;
		private String Country;

		public int getItemID() {
			return ItemID;
		}

		public void setItemID(int ItemID) {
			this.ItemID = ItemID;
		}

		public String getCountry() {
			return Country;
		}

		public void setCountry(String Country) {
			this.Country = Country;
		}

		public String getUserID() {
			return UserID;
		}

		public void setUserID(String UserID) {
			this.UserID = UserID;
		}

		public int getRating() {
			return Rating;
		}

		public void setRating(int Rating) {
			this.Rating = Rating;
		}

		@Override
		public String toString() {
			return this.ItemID + ":" + this.UserID + ":" + this.Rating;
		}
	}

	class BidLocation {
		private int ItemID;
		private String UserID;
		private String Location;

		public int getItemID() {
			return ItemID;
		}

		public void setItemID(int ItemID) {
			this.ItemID = ItemID;
		}

		public String getLocation() {
			return Location;
		}

		public void setLocation(String Location) {
			this.Location = Location;
		}

		public String getUserID() {
			return UserID;
		}

		public void setUserID(String UserID) {
			this.UserID = UserID;
		}

		@Override
		public String toString() {
			return this.ItemID + ":" + this.Location + ":" + this.UserID;
		}
	}

	class BidTime {
		private int ItemID;
		private String Time;
		private String UserID;

		public int getItemID() {
			return ItemID;
		}

		public void setItemID(int ItemID) {
			this.ItemID = ItemID;
		}

		public String getTime() {
			return Time;
		}

		public void setTime(String Time) {
			this.Time = Time;
		}

		public String UserID() {
			return UserID;
		}

		public void setUserID(String UserID) {
			this.UserID = UserID;
		}

		@Override
		public String toString() {
			return this.ItemID + ":" + this.Time;
		}
	}

	class Category {
		private int ItemID;
		private String CategoryName;

		public int getItemID() {
			return ItemID;
		}

		public void setItemID(int ItemID) {
			this.ItemID = ItemID;
		}

		public String getCategoryName() {
			return CategoryName;
		}

		public void setCategoryName(String CategoryName) {
			this.CategoryName = CategoryName;
		}

		@Override
		public String toString() {
			return this.ItemID + ":" + this.CategoryName;
		}
	}

	class EbayData {
		private int ItemID;
		private String Name;
		private String Currently;
		private String First_Bid;
		private int Number_of_Bids;
		private String Country;
		private String Started;
		private String Ends;
		private String Description;
		private String Buy_Price;
		private String Location;

		public int getItemID() {
			return ItemID;
		}

		public void setItemID(int ItemID) {
			this.ItemID = ItemID;
		}

		public String getName() {
			return Name;
		}

		public void setName(String Name) {
			this.Name = Name;
		}

		public String getCurrently() {
			return Currently;
		}

		public void setCurrently(String Currently) {
			this.Currently = Currently;
		}

		public String getLocation() {
			return Location;
		}

		public void setLocation(String Location) {
			this.Location = Location;
		}

		public String getBuy_Price() {
			return Buy_Price;
		}

		public void setBuy_Price(String Buy_Price) {
			this.Buy_Price = Buy_Price;
		}

		public String getFirst_Bid() {
			return Buy_Price;
		}

		public void setFirst_Bid(String First_Bid) {
			this.First_Bid = First_Bid;
		}

		public int getNumber_of_Bids() {
			return Number_of_Bids;
		}

		public void setNumber_of_Bids(int Number_of_Bids) {
			this.Number_of_Bids = Number_of_Bids;
		}

		public String getCountry() {
			return Country;
		}

		public void setCountry(String Country) {
			this.Country = Country;
		}

		public String getStarted() {
			return Started;
		}

		public void setStarted(String Started) {
			this.Started = Started;
		}

		public String getEnds() {
			return Ends;
		}

		public void setEnds(String Ends) {
			this.Ends = Ends;
		}

		public String getDescription() {
			return Description;
		}

		public void setDescription(String Description) {
			this.Description = Description;
		}

		@Override
		public String toString() {
			return this.ItemID + ":" + this.Name + ":" + this.Currently + ":"
					+ this.Buy_Price + ":" + this.First_Bid + ":"
					+ this.Number_of_Bids + ":" + this.Country + ":"
					+ this.Started + ":" + this.Ends + ":" + this.Description;
		}

	}

	class Location {
		private int ItemID;
		private String Latitude;
		private String Longitude;

		public int getItemID() {
			return ItemID;
		}

		public void setItemID(int ItemID) {
			this.ItemID = ItemID;
		}

		public String getLatitude() {
			return Latitude;
		}

		public void setLatitude(String Latitude) {
			this.Latitude = Latitude;
		}

		public String getLongitude() {
			return Longitude;
		}

		public void setLongitude(String Longitude) {
			this.Longitude = Longitude;
		}

		@Override
		public String toString() {
			return this.ItemID + ":" + this.Latitude + ":" + this.Longitude;

		}
	}

	class Seller {
		private int ItemID;
		private int Rating;
		private String UserID;

		public int getItemID() {
			return ItemID;
		}

		public void setItemID(int ItemID) {
			this.ItemID = ItemID;
		}

		public int getRating() {
			return Rating;
		}

		public void setRating(int Rating) {
			this.Rating = Rating;
		}

		public String getUserID() {
			return UserID;
		}

		public void setUserID(String UserID) {
			this.UserID = UserID;
		}

		@Override
		public String toString() {
			return this.ItemID + ":" + this.Rating + ":" + this.UserID;
		}
	}

	// The end of the models

	// The beginning of the Writers

	static class BidderCSVWriter {
		private final static String COLUMN_DELIMITER = "\t\r";
		private final static String LINE_DELIMITER = "\r\n";

		public static void generateCsvFile(String sFileName, List<Bidder> Bidder) {
			try {
				FileWriter writer = new FileWriter(
						"bidder.csv");
				for (Bidder bidder : Bidder) {
					writer.append(Integer.toString(bidder.getItemID()));
					writer.append(COLUMN_DELIMITER);
					writer.append(bidder.getUserID());
					writer.append(COLUMN_DELIMITER);
					writer.append(Integer.toString(bidder.getRating()));
					writer.append(COLUMN_DELIMITER);
					writer.append(bidder.getCountry());
					writer.append(LINE_DELIMITER);
				}
				writer.flush();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	static class BidLocationCSVWriter {
		private final static String COLUMN_DELIMITER = "\t\r";
		private final static String LINE_DELIMITER = "\r\n";

		public static void generateCsvFile(String sFileName,
				List<BidLocation> BidLocation) {
			try {
				FileWriter writer = new FileWriter(
						"bidlocation.csv");
				for (BidLocation bidlocation : BidLocation) {
					writer.append(Integer.toString(bidlocation.getItemID()));
					writer.append(COLUMN_DELIMITER);
					writer.append(bidlocation.getUserID());
					writer.append(COLUMN_DELIMITER);
					writer.append(bidlocation.getLocation());
					writer.append(LINE_DELIMITER);
				}
				writer.flush();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	static class BidTimeCSVWriter {
		private final static String COLUMN_DELIMITER = "\t\r";
		private final static String LINE_DELIMITER = "\r\n";

		public static void generateCsvFile(String sFileName,
				List<BidTime> BidTime) {
			try {
				FileWriter writer = new FileWriter(
						"bidtime.csv");
				for (BidTime bidtime : BidTime) {
					writer.append(Integer.toString(bidtime.getItemID()));
					writer.append(COLUMN_DELIMITER);
					writer.append(bidtime.getTime());

					writer.append(LINE_DELIMITER);
				}
				writer.flush();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	static class CategoryCSVWriter {
		private final static String COLUMN_DELIMITER = "\t\r";
		private final static String LINE_DELIMITER = "\r\n";

		public static void generateCsvFile(String sFileName,
				List<Category> CategoryList) {
			try {
				FileWriter writer = new FileWriter(
						"category.csv");
				for (Category category : CategoryList) {
					writer.append(Integer.toString(category.getItemID()));
					writer.append(COLUMN_DELIMITER);
					writer.append(category.getCategoryName());
					writer.append(LINE_DELIMITER);
				}
				writer.flush();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	static class EbayDataCSVWriter {

		private final static String COLUMN_DELIMITER = "\t\r";
		private final static String LINE_DELIMITER = "\r\n";

		public static void generateCsvFile(String sFileName,
				List<EbayData> ebayDataList) {
			try {
				FileWriter writer = new FileWriter(
						("ebaydata.csv"));

				for (EbayData ebayData : ebayDataList) {
					writer.append(Integer.toString(ebayData.getItemID()));
					writer.append(COLUMN_DELIMITER);
					writer.append(ebayData.getName());
					writer.append(COLUMN_DELIMITER);
					writer.append(ebayData.getCurrently());
					writer.append(COLUMN_DELIMITER);
					writer.append(ebayData.getBuy_Price());
					writer.append(COLUMN_DELIMITER);
					writer.append(ebayData.getFirst_Bid());
					writer.append(COLUMN_DELIMITER);
					writer.append(Integer.toString(ebayData.getNumber_of_Bids()));
					writer.append(COLUMN_DELIMITER);
					writer.append(ebayData.getCountry());
					writer.append(COLUMN_DELIMITER);
					writer.append(ebayData.getStarted().toString());
					writer.append(COLUMN_DELIMITER);
					writer.append(ebayData.getEnds().toString());
					writer.append(COLUMN_DELIMITER);
					writer.append(ebayData.getDescription());
					writer.append(LINE_DELIMITER);
				}

				writer.flush();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	static class LocationCSVWriter {
		private final static String COLUMN_DELIMITER = "\t\r";
		private final static String LINE_DELIMITER = "\r\n";

		public static void generateCsvFile(String sFileName,
				List<Location> Location) {
			try {
				FileWriter writer = new FileWriter(
						"location.csv");
				for (Location location : Location) {
					writer.append(Integer.toString(location.getItemID()));
					writer.append(COLUMN_DELIMITER);
					writer.append(location.getLatitude());
					writer.append(COLUMN_DELIMITER);
					writer.append(location.getLongitude());
					writer.append(LINE_DELIMITER);
				}
				writer.flush();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	static class SellerCSVWriter {

		private final static String COLUMN_DELIMITER = "\t\r";
		private final static String LINE_DELIMITER = "\r\n";

		public static void generateCsvFile(String sFileName, List<Seller> Seller) {
			try {
				FileWriter writer = new FileWriter(
						"seller.csv");
				for (Seller seller : Seller) {
					writer.append(Integer.toString(seller.getItemID()));
					writer.append(COLUMN_DELIMITER);
					writer.append(Integer.toString(seller.getRating()));
					writer.append(COLUMN_DELIMITER);
					writer.append(seller.getUserID());
					writer.append(LINE_DELIMITER);
				}
				writer.flush();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	static class BidAmountCSVWriter {

		private final static String COLUMN_DELIMITER = "\t\r";
		private final static String LINE_DELIMITER = "\r\n";

		public static void generateCsvFile(String sFileName,
				List<BidAmount> BidAmount) {
			try {
				FileWriter writer = new FileWriter(
						"bidamount.csv");
				for (BidAmount bidamount : BidAmount) {
					writer.append(Integer.toString(bidamount.getItemID()));
					writer.append(COLUMN_DELIMITER);
					writer.append(bidamount.getAmount());
					writer.append(LINE_DELIMITER);
				}
				writer.flush();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	// The end of the writers////
}

// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
