

class Vaccines:

    def __init__(self, connection):
        self._connection = connection

    def insert(self, vaccine):
        with self._connection:
            self._connection.execute("""
            INSERT INTO Vaccines (id, date, supplier, quantity) VALUES (?,?,?,?)
            """, [vaccine.id, vaccine.date, vaccine.supplier, vaccine.quantity])


class Suppliers:

    def __init__(self, connection):
        self._connection = connection

    def insert(self, supplier):
        with self._connection:
            cursor = self._connection.cursor()
            add = "INSERT INTO Suppliers (id, name, logistic) VALUES (?, ?, ?)"
            val = (supplier.id, supplier.name, supplier.logistics)
            cursor.execute(add, val)


class Clinics:

    def __init__(self, connection):
        self._connection = connection

    def insert(self, clinic):
        with self._connection:
            self._connection.execute("""
            INSERT INTO Clinics (id, location, demand, logistic) VALUES (?,?,?,?)
            """, [clinic.id, clinic.location, clinic.demand, clinic.logistics])


class Logistics:

    def __init__(self, connection):
        self._connection = connection

    def insert(self, logistic):
        with self._connection:
            self._connection.execute("""
            INSERT INTO Logistics (id, name, count_sent, count_received) VALUES (?,?,?,?)
            """, [logistic.id, logistic.name, logistic.count_sent, logistic.count_received])
