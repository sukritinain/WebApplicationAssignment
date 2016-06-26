#!/bin/bash

#Run the drop.sql batch file to drop existing tables.
mysql < drop.sql

#Run the create.sql batch file to create the database (if it does not exist) and the tables.
mysql < create.sql

# Compile and run the convertor

javac MySAX.java

java MySAX $EBAY_DATA/*.xml


# Run the load.sql batch file to load the data
mysql ad < load.sql

#temporary files removed
rm *.csv
rm *.class