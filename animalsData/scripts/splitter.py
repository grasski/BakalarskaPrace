import glob
import os
import math
import random
import shutil


def split():
    folderNames = ["buffalo", "capybara", "elephant", "giraffe", "jaguar", "kangaroo", "rhino",
                  "tiger", "turtle", "zebra", "cat", "dog", "penguin", "cow", "deer",
                   "lion", "parrot", "sheep", "zzBackground"]

    for folder in folderNames:
        path = os.path.join(folder, "*.jpg")
        imgFiles = glob.glob(path)
        testPath = os.path.join(folder, "test")
        trainPath = os.path.join(folder, "train")
        if not os.path.exists(testPath):
            os.mkdir(testPath)
        if not os.path.exists(trainPath):
            os.mkdir(trainPath)

        filesSum = len(imgFiles)
        sumOfTest = math.ceil((filesSum * 12) / 100)
        sumOfTrain = filesSum - sumOfTest

        for i in range(sumOfTest):
            toTest = random.choice(imgFiles)    # path of image file which needs to be moved to test folder
            imgFiles.remove(toTest)

            imageName = os.path.basename(toTest)
            labelName = imageName[:-3] + "xml"

            originalImagePath = toTest
            originalLabelPath = os.path.join(folder, labelName)
            moveImagePath = os.path.join(testPath, imageName)
            moveLabelPath = os.path.join(testPath, labelName)

            os.rename(originalImagePath, moveImagePath)
            os.rename(originalLabelPath, moveLabelPath)

        print(folder + "\\test finished.")

        for f in imgFiles:
            imageName = os.path.basename(f)
            labelName = imageName[:-3] + "xml"

            originalImagePath = f
            originalLabelPath = os.path.join(folder, labelName)
            moveImagePath = os.path.join(trainPath, imageName)
            moveLabelPath = os.path.join(trainPath, labelName)

            os.rename(originalImagePath, moveImagePath)
            os.rename(originalLabelPath, moveLabelPath)

        print(folder + " finished")


# move test and train images from animal folders to one big folder containing all those images splitted into train and test
def moveToTestTrainFolders():
    pathToCopy = os.path.join("D:\\zaloha\\UTB\\modelsCreation\\tensorflow\\workspace\\6", "images")
    if not os.path.exists(pathToCopy):
       os.makedirs(pathToCopy)
       
    dirs = [name for name in os.listdir(".") if os.path.isdir(name)]
    animals = ["capybara", "cat", "dog", "elephant", "giraffe", "jaguar", "kangaroo", "parrot", "penguin", "rhino", "tiger", "turtle", "zebra", "zzBackground"]
    for dr in dirs:
        if dr in animals:        
            for path in ["test", "train"]:
                actualFolder = os.path.join(dr, path)
                
                if not os.path.exists(os.path.join(pathToCopy, path)):
                   os.makedirs(os.path.join(pathToCopy, path))
       
                file_names = os.listdir(actualFolder)
                for file_name in file_names:
                    shutil.copy2(os.path.join(actualFolder, file_name), os.path.join(pathToCopy, path))
                    
            print(dr + " finished")
    print("done")


if __name__ == '__main__':
    # split()
    moveToTestTrainFolders()
