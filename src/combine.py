from CollageDataBase import *
import json

fn = "C:/Users/hawverm2967/Downloads/Pyhon/CollegeData/src/collegedataniche.json"

with open(fn) as fp:
    jdata = json.load(fp)


db = CollageDataBase()
db.load("C:/Users/hawverm2967/Downloads/Pyhon/CollegeData/src/CollageDB.json")

print(db.search("Union College"))

for k, v in jdata.items():
    c = db.search(k)
    if c is not None:
        if c.data["default"]:
            print(c.getName())