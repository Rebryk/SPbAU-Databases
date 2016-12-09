WAREHOUSES = 100
PHARMACIES = 100
LABS = 20
INGREDIENTS = 70
DRUGTYPES = 10
CERTIFICATES = 1000
DRUGS = 1000
DISTRIBUTORS = 100
DELIVERIES = 1000
DELIVERYPARTS = 1000
CARS = 100
CARASSIGNMENTS = 1000
CAR_CARGOES = 1000
DRUG_SALE_INFOES = 10000

assert(DRUGS <= CERTIFICATES)

warehouses = []
pharmacies = []
labs = []
ingredients = []
drugtypes = []
certificates = []
drugs = []
distributors = []
deliveries = []
deliveryparts = []
cars = []
carassignments = []
car_cargoes = []
drug_sale_infoes = []

#################################

from random import *
import string, time

def id_gen(size=6, chars=string.ascii_lowercase):
	return "".join(choice(chars) for _ in range(size))
def addr_gen():
	return "{} street, building {}".format(id_gen(8), randint(1, 100))

def strTimeProp(start, end, format, prop):
    stime = time.mktime(time.strptime(start, format))
    etime = time.mktime(time.strptime(end, format))

    ptime = stime + prop * (etime - stime)

    return time.strftime(format, time.localtime(ptime))

def randomDate(start, end, prop):
    return strTimeProp(start, end, '%d-%m-%Y', prop)
def randomDateTime(start, end, prop):
    return strTimeProp(start, end, '%d-%m-%Y %H:%M', prop)
    
#################################

warehouse_ids = list(range(1, WAREHOUSES * 10))
shuffle(warehouse_ids)

for i in range(WAREHOUSES):
	id = warehouse_ids[i]
	address = addr_gen()
	warehouses.append((id, address))
	print(
		"INSERT INTO Warehouse (id, address) VALUES ({}, '{}');"
		.format(id, address))

pharmacies_numbers = list(range(1, PHARMACIES * 10))
shuffle(pharmacies_numbers)
	
for i in range(PHARMACIES):
	id = i + 1
	number = pharmacies_numbers[i]
	address = addr_gen()
	pharmacies.append((id, address, number))
	print(
		"INSERT INTO Pharmacy (id, address, number) VALUES ({}, '{}', {});"
		.format(id, address, number))

for i in range(LABS):
	id = i + 1
	name = "{} labs".format(id_gen(10))
	chief_last_name = "{}ov".format(id_gen(4))
	labs.append((id, name, chief_last_name))
	print(
		"INSERT INTO Laboratory (id, name, chief_last_name) VALUES ({}, '{}', '{}');"
		.format(id, name, chief_last_name))


for i in range(INGREDIENTS):
	id = i + 1
	name = id_gen(14)
	formula = id_gen(randint(5, 25), string.ascii_lowercase + string.digits + " " * 5)
	ingredients.append((id, name, formula))
	print(
		"INSERT INTO Ingredient (id, name, formula) VALUES ({}, '{}', '{}');"
		.format(id, name, formula))

for i in range(DRUGTYPES):
	id = i + 1
	type = id_gen(8)
	drugtypes.append((id, type))
	print(
		"INSERT INTO DrugType (id, type) VALUES ({}, '{}');"
		.format(id, type))

# кажется, немного странно, что у нас сертификат может быть без лекарства
for i in range(CERTIFICATES):
	id = i + 1
	expiry_date = randomDate("01-01-1980", "01-01-2016", random())
	laboratory_id = choice(labs)[0]
	certificates.append((id, expiry_date, laboratory_id))
	print(
		"INSERT INTO Certificate (id, expiry_date, laboratory_id) VALUES ({}, '{}', {});"
		.format(id, expiry_date, laboratory_id))

certificates_copy = certificates.copy()
for i in range(DRUGS):
	id = i + 1
	trade_name = id_gen(randint(5, 25), string.ascii_lowercase + " " * 3)
	international_name = id_gen(randint(5, 25), string.ascii_lowercase + " " * 3)
	type_id = choice(drugtypes)[0]
	producer = id_gen(randint(5, 10), string.ascii_lowercase)
	ingredient_id = choice(ingredients)[0]
	
	c = choice(certificates)
	certificate_id = c[0]
	certificates.remove(c)
	
	drugs.append((id, trade_name, international_name, type_id, producer, ingredient_id, certificate_id))
	print(
		"INSERT INTO Drug (id, trade_name, international_name, type_id, producer, ingredient_id, certificate_id) \
VALUES ({}, '{}', '{}', {}, '{}', {}, {});"
		.format(id, trade_name, international_name, type_id, producer, ingredient_id, certificate_id))
certificates = certificates_copy


for i in range(DISTRIBUTORS):
	id = i + 1
	address = addr_gen()
	bank_account = id_gen(16, string.digits)
	first_name = id_gen(randint(5, 10))
	last_name = "{}ov".format(id_gen(4))
	phone_number = "+7{}".format(id_gen(10, string.digits))
	distributors.append((id, address, bank_account, first_name, last_name, phone_number))
	print("INSERT INTO Distributor (id, address, bank_account, first_name, last_name, phone_number) \
VALUES ({}, '{}', '{}', '{}', '{}', '{}');"
		.format(id, address, bank_account, first_name, last_name, phone_number))

for i in range(DELIVERIES):
	id = i + 1
	distributor_id = choice(distributors)[0]
	destination_id = choice(warehouses)[0]
	delivery_date = randomDateTime("01-01-1980 00:00", "01-01-2016 00:00", random())
	receiver_last_name = "{}ov".format(id_gen(4))
	deliveries.append((id, distributor_id, destination_id, delivery_date, receiver_last_name))
	print("INSERT INTO Delivery (id, distributor_id, destination_id, delivery_date, receiver_last_name) \
VALUES ({}, {}, {}, '{}', '{}');"
	.format(id, distributor_id, destination_id, delivery_date, receiver_last_name))

for i in range(DELIVERYPARTS):
	id = i + 1
	delivery_id = choice(deliveries)[0]
	drug_id =  choice(drugs)[0]
	travel_package_count = randint(1, 70)
	travel_package_weight = randint(10, 1000)
	sale_package_count = randint(1, 70)
	sale_package_price = randint(10, 10000)
	deliveryparts.append((id, delivery_id, drug_id, travel_package_count, travel_package_weight, sale_package_count, sale_package_price))
	print("INSERT INTO DeliveryPart (id, delivery_id, drug_id, travel_package_count, \
travel_package_weight, sale_package_count, sale_package_price) \
VALUES ({}, {}, {}, {}, {}, {}, {});".format(id, delivery_id, drug_id, travel_package_count,
	travel_package_weight, sale_package_count, sale_package_price))

for i in range(CARS):
	id = i + 1
	number = id_gen(6, string.ascii_uppercase + string.digits)
	last_maintenance_date = randomDate("01-01-1980", "01-01-2016", random())
	cars.append((id, number, last_maintenance_date))
	print("INSERT INTO Car (id, number, last_maintenance_date) VALUES ({}, '{}', '{}');"
		.format(id, number, last_maintenance_date))

for i in range(CARASSIGNMENTS):
	id = i + 1
	date = randomDateTime("01-01-1980 00:00", "01-01-2016 00:00", random())
	car_id = choice(cars)[0]
	warehouse_id = choice(warehouses)[0]
	carassignments.append((id, date, car_id, warehouse_id))
	print("INSERT INTO CarAssignment (id, date, car_id, warehouse_id) VALUES ({}, '{}', {}, {});"
		.format(id, date, car_id, warehouse_id))
		
for i in range(CAR_CARGOES):
	id = i + 1
	car_assignment_id = choice(carassignments)[0]
	target_pharmacy_id = choice(pharmacies)[0]
	drug_id = choice(drugs)[0]
	package_count = randint(1, 100)
	car_cargoes.append((id, car_assignment_id, target_pharmacy_id, drug_id, package_count))
	print("INSERT INTO CarAssignmentCargo (id, car_assignment_id, target_pharmacy_id, drug_id, package_count) VALUES ({}, {}, {}, {}, {});"
		.format(id, car_assignment_id, target_pharmacy_id, drug_id, package_count))
		
for i in range(DRUG_SALE_INFOES):
	id = i + 1
	drug_id = choice(drugs)[0]
	pharmacy_id = choice(pharmacies)[0]
	price = randint(10, 10000)
	amount = randint(0, 100)
	drug_sale_infoes.append((id, drug_id, pharmacy_id, price, amount))
	print("INSERT INTO DrugSaleInfo (id, drug_id, pharmacy_id, price, amount) VALUES ({}, {}, {}, {}, {});"
		.format(id, drug_id, pharmacy_id, price, amount))