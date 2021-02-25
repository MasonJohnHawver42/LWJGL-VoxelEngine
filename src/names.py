
fn = "C:/Users/hawverm2967/Downloads/Pyhon/CollegeData/data/names.txt"

f = open(fn, "r")
names = f.read().split("\n")

w = open("C:/Users/hawverm2967/Downloads/Pyhon/CollegeData/data/Aliases.txt", "w+")

for name in names:
    name = name.split("/")[4]
    name = " ".join(name.split("-"))
    name = "-".join(name.split("   "))
    print(name)
    w.write(name + "\n")

w.close()