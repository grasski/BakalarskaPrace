import glob
files = glob.glob("*")
for file in files:
    with open(file, "r+") as f:
        lines = f.readlines()
        f.seek(0)
        f.truncate()
        for line in lines:
            classNumber = "4" if "Cat" in f.name else "5"
            if ("Cat" in f.name and line[0] != "0") or ("Tiger" in f.name and line[0] != "1"):
                print(f.name)
                print(line)
                classNumber = line[0]
            newLine = classNumber + line[1:]
            f.write(newLine)
print("done")
