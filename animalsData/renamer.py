# Python 3 code to rename multiple
# files in a directory or folder

# importing os module
import os


# Function to rename multiple files
def main():
    # folder = os.path.join("data", "labeled", "downloads", "kangaroo", "3", "images")
    # for count, filename in enumerate(os.listdir(folder)):
    #     source = os.path.join(folder, filename)
    #     if (".xml" in source):
    #         break
    #
    #     newName = os.path.join(source.replace("temmp", "temp"))
    #     os.rename(source, newName)

    folder = os.path.join("data", "labeled", "downloads", "capybara", "1", "images")
    # folder = os.path.join("data", "labeled", "buffalo")
    l = os.listdir(folder)
    fileName = "Capybara_Download_1"
    i = 0
    for name in list(l[0::2]):
        imgSource = os.path.join(folder, name)
        labelSource = os.path.join(folder, name[:-3] + "xml")
        newImgName = os.path.join(folder, f"{fileName}_{i}.jpg")
        newLabelName = os.path.join(folder, f"{fileName}_{i}.xml")
        # print(imgSource + "   " + newImgName)
        # print(labelSource + "   " + newLabelName)
        i += 1

        os.rename(imgSource, newImgName)
        os.rename(labelSource, newLabelName)


# Driver Code
if __name__ == '__main__':
    main()
    print("done")
