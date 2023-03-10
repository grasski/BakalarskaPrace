import os


def main():
    folder = os.path.join("data", "labeled", "downloads", "dog", "2", "images")
    # folder = os.path.join("data", "labeled", "buffalo")
    l = os.listdir(folder)
    fileName = "Dog_Download_2"
    i = 0
    for name in list(l[0::2]):
        imgSource = os.path.join(folder, name)
        labelSource = os.path.join(folder, name[:-3] + "xml")
        
        newImgName = os.path.join(folder, f"{fileName}_{i}.jpg")
        newLabelName = os.path.join(folder, f"{fileName}_{i}.xml")

        os.rename(imgSource, newImgName)
        os.rename(labelSource, newLabelName)

        i += 1


if __name__ == '__main__':
    main()
    print("done")
