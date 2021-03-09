The Main class requires as argument the full path of the file containing the json data.
This file is already included as resource in this project. To run from local
the argument must be: src/main/resources/Daimler-test-data.json

Run the Main class passing the path to the json data file as argument. 
You'll be prompted to type a search text which can be a sku code or a
sku code followed by the number of similar sku to retrieve:

sku-1

sku-1 7

The sku code must match the pattern 'sku-[0-9]+'
