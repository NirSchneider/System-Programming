# This is a sample Python script.

# Press Shift+F10 to execute it or replace it with your code.
# Press Double Shift to search everywhere for classes, files, tool windows, actions, and settings.
import sys
from Repository import repo
from DTO import Vaccine
from DTO import Supplier
from DTO import Clinic
from DTO import Logistic

repo.create_tables()


def main(args):
    with open(args[1]) as inputFile:

        lines = inputFile.readlines()
        total_inventory, total_demand, total_sent, total_received = 0, 0, 0, 0
        num_of_vaccines = int(lines[0].split(',')[0])
        num_of_suppliers = int(lines[0].split(',')[1])
        num_of_clinics = int(lines[0].split(',')[2])
        num_of_logistics = int(lines[0].split(',')[3])

        flag1 = num_of_vaccines+1
        for i in range(1, flag1):
            v = lines[i].split(',')
            total_inventory = total_inventory + int(v[3])
            repo.vaccines.insert(Vaccine(int(v[0]), v[1], int(v[2]), int(v[3])))

        flag2 = flag1 + num_of_suppliers
        for i in range(flag1, flag2):
            s = lines[i].split(',')
            repo.suppliers.insert(Supplier(int(s[0]), s[1], int(s[2])))

        flag3 = flag2 + num_of_clinics
        for i in range(flag2, flag3):
            c = lines[i].split(',')
            repo.clinics.insert(Clinic(int(c[0]), c[1], int(c[2]), int(c[3])))
            total_demand = total_demand + int(c[2])

        flag4 = flag3 + num_of_logistics
        for i in range(flag3, flag4):
            l = lines[i].split(',')
            total_sent = total_sent + int(l[2])
            total_received = total_received + int(l[3])
            repo.logistics.insert((Logistic(int(l[0]), l[1], int(l[2]), int(l[3]))))

    with open(args[2]) as inputOrder:
        summary = open('output.txt', 'w+')
        lines = inputOrder.readlines()
        for line in lines:
            if len(line.split(',')) == 2:
                location = line.split(',')[0]
                amount = int(line.split(',')[1])
                total_sent = total_sent + amount
                total_demand = total_demand - amount
                total_inventory = total_inventory - amount
                repo.send_shipment(location, amount)
                add = str(total_inventory) + ',' + str(total_demand) + ',' + str(total_received) + ',' + str(total_sent)
                summary.write(add+'\n')
            else:
                name = line.split(',')[0]
                amount = int(line.split(',')[1])
                total_received = total_received + amount
                total_inventory = total_inventory + amount
                date = line.split(',')[2]
                repo.receive_shipment(name, amount, date)
                add = str(total_inventory) + ',' + str(total_demand) + ',' + str(total_received) + ',' + str(total_sent)
                summary.write(add+'\n')


# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    main(sys.argv)
