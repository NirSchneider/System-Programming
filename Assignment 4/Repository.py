import sqlite3
import atexit
from DAO import Vaccines
from DAO import Suppliers
from DAO import Clinics
from DAO import Logistics


class _Repository:
    def __init__(self):
        self._connection = sqlite3.connect('database.db')
        self.vaccines = Vaccines(self._connection)
        self.suppliers = Suppliers(self._connection)
        self.clinics = Clinics(self._connection)
        self.logistics = Logistics(self._connection)

    def create_tables(self):
        self._connection.executescript("""
         CREATE          TABLE       logistics (
         id              INTEGER     PRIMARY KEY,
         name            STRING      NOT NULL,
         count_sent      INTEGER     NOT NULL,
         count_received  INTEGER     NOT NULL
         );

        CREATE      TABLE       suppliers (
        id          INTEGER     PRIMARY KEY,  
        name        STRING      NOT NULL,
        logistic    INTEGER,
        FOREIGN KEY (logistic) REFERENCES Logistics(id)
        );

        CREATE      TABLE       clinics (
        id          INTEGER     PRIMARY KEY,
        location    STRING      NOT NULL,
        demand      INTEGER     NOT NULL,
        logistic    INTEGER,
        FOREIGN KEY (logistic) REFERENCES Logistics(id)
        );

        CREATE      TABLE       vaccines (
        id          INTEGER     PRIMARY KEY,
        date        DATE        NOT NULL,
        supplier    INTEGER,
        quantity    INTEGER     NOT NULL,
        FOREIGN KEY (supplier) REFERENCES Suppliers(id)
         );
    """)

    def receive_shipment(self, name, amount, date):
        cursor = self._connection.cursor()
        supp_id = cursor.execute('SELECT id FROM suppliers WHERE name = ?', [name]).fetchone()[0]
        logistic_id = cursor.execute('SELECT logistic FROM suppliers WHERE name = ?', [name]).fetchone()[0]
        curr_amount = cursor.execute('SELECT count_received FROM logistics WHERE id = ?', [logistic_id]).fetchone()[0]
        update = "UPDATE logistics SET count_received = %d WHERE id = %s" % (amount + curr_amount, logistic_id)
        cursor.execute(update)
        cursor.execute('INSERT INTO vaccines (date, supplier, quantity) VALUES (?, ?, ?)', [date, supp_id, amount])

    def send_shipment(self, location, amount):
        cursor = self._connection.cursor()
        logistic_id = cursor.execute('SELECT logistic FROM clinics WHERE location = ?', [location]).fetchone()[0]
        curr_amount = cursor.execute('SELECT count_sent FROM logistics WHERE id = ?', [logistic_id]).fetchone()[0]
        clinic_id = cursor.execute('SELECT id FROM clinics WHERE location = ?', [location]).fetchone()[0]
        cursor.execute('UPDATE logistics SET count_sent = %d WHERE id = %s' % (amount + curr_amount, logistic_id))
        curr_demand = cursor.execute('SELECT demand FROM clinics WHERE location = ?', [location]).fetchone()[0]
        cursor.execute('UPDATE Clinics SET demand = %d WHERE id = %s' % (curr_demand - amount, clinic_id))
        self.reduce_inventory(amount)

    def reduce_inventory(self, amount):
        cursor = self._connection.cursor()
        while amount > 0:
            _id = cursor.execute('SELECT id FROM vaccines ORDER BY date ASC LIMIT 1').fetchone()[0]
            quantity = cursor.execute('SELECT quantity FROM vaccines WHERE id = ?', [_id])
            q = quantity.fetchone()[0]
            if amount < q:
                cursor.execute('UPDATE Vaccines SET quantity = %d WHERE id = %s' % (q - amount, _id))
                amount = 0
            else:
                amount = amount - q
                cursor.execute('DELETE FROM Vaccines WHERE id = ?', [_id])

    def close(self):
        self._connection.commit()
        self._connection.close()


repo = _Repository()
atexit.register(repo.close)
