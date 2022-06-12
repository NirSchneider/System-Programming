class Vaccine:

    def __init__(self, _id, date, supplier, quantity):
        self.id = _id
        self.date = date
        self.supplier = supplier
        self.quantity = quantity


class Supplier:

    def __init__(self, _id, name, logistics):
        self.id = _id
        self.name = name
        self.logistics = logistics


class Clinic:

    def __init__(self, _id, location, demand, logistics):
        self.id = _id
        self.location = location
        self.demand = demand
        self.logistics = logistics


class Logistic:

    def __init__(self, _id, name, count_sent, count_received):
        self.id = _id
        self.name = name
        self.count_sent = count_sent
        self.count_received = count_received
