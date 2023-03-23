import os
import glob
import cv2

path = os.walk(os.curdir)
pathToMove = "./WRONG/smallImages"

# dirs = [name for name in os.listdir(".") if os.path.isdir(name)]
dirs = ["downloads/cat/2", "downloads/dog/2"]
for dr in dirs:
    if dr == "smallImages":
        continue

    for path in ["test", "train", "images"]:
        images = glob.glob(dr + f"/{path}/*.jpg")
        i = 0
        for image in images:
            im = cv2.imread(image)
            width, height, _ = im.shape
            if width <= 330 or height <= 330:
                i += 1
                labelSource = image[:-3] + "xml"        # path to XML file

                imageName = os.path.basename(image)     # name of image file
                labelName = imageName[:-3] + "xml"      # name of XML file

                moveImagePath = os.path.join(pathToMove, imageName)     # path to new folder for image
                moveLabelPath = os.path.join(pathToMove, labelName)     # path to new folder for XML

                os.rename(image, moveImagePath)
                os.rename(labelSource, moveLabelPath)

        print(os.path.join(dr, path), "finished.", f"Moved {i} files.")

print("done")
