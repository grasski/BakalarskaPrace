# Python 3 code to rename multiple
# files in a directory or folder
 
# importing os module
import os
 
# Function to rename multiple files
def main():

    # folder = os.path.join("data", "archive", "zebra", "images")
    # for count, filename in enumerate(os.listdir(folder)):
    #     source = os.path.join(folder, filename)
    #     newName = os.path.join(folder, f"Zebra_Dow_{count}.jpg")
    #     print(source + "   " + newName)
    #     os.rename(source, newName)

    folder = os.path.join("bird_dataset", "train")
    l = os.listdir(folder)

    i = 0
    for name in list(l[0::2]):
        jpgSource = os.path.join(folder, name)
        jsonSource = os.path.join(folder, name[:-3] + "json")
        newJpgName = os.path.join(folder, f"Bird_{i}.jpg")
        newJsonName = os.path.join(folder, f"Bird_{i}.json")
        print(jpgSource + "   " + newJpgName)
        print(jsonSource + "   " + newJsonName)
        i += 1

        os.rename(jpgSource, newJpgName)
        os.rename(jsonSource, newJsonName)
        
 
# Driver Code
if __name__ == '__main__':
    main()
    print("done")
